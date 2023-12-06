package org.conferatus.timetable.backend.algorithm.scheduling;

import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionStart;
import io.jenetics.internal.math.Probabilities;
import io.jenetics.util.Factory;
import io.jenetics.util.ISeq;
import lombok.NoArgsConstructor;
import org.conferatus.timetable.backend.algorithm.constraints.PenaltyChecker;
import org.conferatus.timetable.backend.model.AudienceType;
import org.conferatus.timetable.backend.model.TableTime;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.random.RandomGenerator;

/**
 * TODO: checker in another class
 * TODO: refactor audience optimize
 * TODO: add constraints
 */
@NoArgsConstructor
public class GeneticAlgorithmScheduler {
    ArrayList<AudienceTimeCell> cells = new ArrayList<>();
    Map<AudienceType, List<AudienceTimeCell>> audienceMap = new HashMap<>();
    Map<AudienceTimeCell, Integer> audienceToIndex = new HashMap<>();
    ArrayList<LessonGene> lessonGenes = new ArrayList<>();
    private int satisfiedScheduleAmount = 3;
    PenaltyChecker penaltyChecker;
    Map<GroupEvolve, List<Integer>> groupToIndexes = new HashMap<>();


    public void setSatisfiedScheduleAmount(int satisfiedScheduleAmount) {
        this.satisfiedScheduleAmount = satisfiedScheduleAmount;
    }

    public void setPenalties(PenaltyChecker penaltyChecker) {
        this.penaltyChecker = penaltyChecker;
    }

    public static final class DataForConstraint {
        private final List<LessonWithTime> allLessons;
        private final LessonWithTime currentLesson;
        private final List<List<List<LessonWithTime>>> timeTable;
        private List<LessonWithTime> otherLessons = null;


        public DataForConstraint(List<LessonWithTime> allLessons,
                                 LessonWithTime currentLesson,
                                 List<List<List<LessonWithTime>>> timeTable) {
            this.allLessons = allLessons;
            this.currentLesson = currentLesson;
            this.timeTable = timeTable;
        }

        public DataForConstraint(List<LessonWithTime> allLessons, LessonWithTime currentLesson) {
            this(allLessons, currentLesson, generate(allLessons));
        }

        public List<LessonWithTime> getLessonsInDay(int day) {
            var dayCells = timeTable.get(day);
            List<LessonWithTime> lessons = new ArrayList<>();
            dayCells.forEach(lessons::addAll);
            return lessons;
        }

        public List<LessonWithTime> getLessonsInCell(LessonWithTime lesson) {
            return getLessonsInCell(lesson.cell().time());
        }

        public List<LessonWithTime> getLessonsInCell(TableTime tableTime) {
            return getLessonsInCell(tableTime.day(), tableTime.cellNumber());
        }

        public List<LessonWithTime> getLessonsInCell(int day, int cellNumber) {
            return new ArrayList<>(timeTable.get(day).get(cellNumber));
        }

        public List<LessonWithTime> getOtherLessons(LessonWithTime lesson) {
            if (otherLessons == null) {
                otherLessons = getLessonsInCell(lesson);
                otherLessons.remove(lesson);
            }
            return otherLessons;
        }

        public static List<List<List<LessonWithTime>>> generate(List<LessonWithTime> lessons) {
            List<List<List<LessonWithTime>>> timeTable = new ArrayList<>();
            for (int dayNumber = 0; dayNumber < TableTime.getDaysAmount(); dayNumber++) {
                List<List<LessonWithTime>> dayCells = new ArrayList<>();
                for (int cellNumber = 0; cellNumber < TableTime.getCellsAmount(); cellNumber++) {
                    dayCells.add(new ArrayList<>());
                }
                timeTable.add(dayCells);
            }
            for (LessonWithTime lesson : lessons) {
                var timeData = lesson.cell().time();
                timeTable.get(timeData.day()).get(timeData.cellNumber()).add(lesson);
            }
            return timeTable;
        }

        public List<LessonWithTime> allLessons() {
            return allLessons;
        }

        public LessonWithTime currentLesson() {
            return currentLesson;
        }

        public List<List<List<LessonWithTime>>> timeTable() {
            return timeTable;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (DataForConstraint) obj;
            return Objects.equals(this.allLessons, that.allLessons) &&
                    Objects.equals(this.currentLesson, that.currentLesson) &&
                    Objects.equals(this.timeTable, that.timeTable);
        }

        @Override
        public int hashCode() {
            return Objects.hash(allLessons, currentLesson, timeTable);
        }

        @Override
        public String toString() {
            return "DataForConstraint[" +
                    "allLessons=" + allLessons + ", " +
                    "currentLesson=" + currentLesson + ", " +
                    "timeTable=" + timeTable + ']';
        }

    }

    public GeneticAlgorithmScheduler(int satisfiedScheduleAmount) {
        this.satisfiedScheduleAmount = satisfiedScheduleAmount;
    }

    public GeneticAlgorithmScheduler(PenaltyChecker penaltyChecker) {
        this.penaltyChecker = penaltyChecker;
    }

    public GeneticAlgorithmScheduler(PenaltyChecker penaltyChecker, int satisfiedScheduleAmount) {
        this.penaltyChecker = penaltyChecker;
        this.satisfiedScheduleAmount = satisfiedScheduleAmount;
    }


    public record AlgoSchedule(List<LessonWithTime> allLessons, PenaltyChecker.CheckResult checkResult) {

    }

    public class AlgorithmStatus {
        double percentage;
        boolean running;

        PenaltyChecker.CheckResult checkResult;

        CompletableFuture<List<AlgoSchedule>> result;
    }

    private AlgorithmStatus algorithmStatus;

    /**
     * @param studyPlanEvolves
     * @param audiences
     * @return few schedules
     */
    public List<AlgoSchedule> algorithm(List<StudyPlanEvolve> studyPlanEvolves,
                                        List<AudienceEvolve> audiences) {

        cells.clear();
        lessonGenes.clear();
        prepareData(studyPlanEvolves, audiences);

        final Factory<Genotype<IntegerGene>> gtf =
                Genotype.of(IntegerChromosome.of(0, cells.size()), lessonGenes.size());
        final Engine<IntegerGene, Double> engine = Engine
                .builder(this::fitness, gtf)
                .populationSize(1000)
                .alterers(new MyMutator())
                .build();
        var evolutionResult = engine.evolve(EvolutionStart.empty());
        int goodPhenotypeCounter = 0;
        int maxCounter = 3000;
        double prev = -1e10;
        int i = 0;
        algorithmStatus = new AlgorithmStatus();
        algorithmStatus.percentage = 1;
        algorithmStatus.running = false;
        var initialCheck = penaltyChecker.calculatePenalty(phenotypeToLessons(evolutionResult.bestPhenotype()));
        algorithmStatus.checkResult = initialCheck;
        double initialMaximum = initialCheck.total();

        while (goodPhenotypeCounter < satisfiedScheduleAmount) {
            evolutionResult = engine.evolve(evolutionResult.next());
            goodPhenotypeCounter = 0;
            double bestResult = evolutionResult.bestFitness();
            if (bestResult <= prev) {

                maxCounter--;

            } else {
                prev = bestResult;
                i++;
                System.out.println(bestResult);

                if (i >= 2) {
                    i = 0;
                    PenaltyChecker.CheckResult checkResult =
                            penaltyChecker.calculatePenalty(phenotypeToLessons(evolutionResult.bestPhenotype()));
                    checkResult.penaltyToError().forEach((pen, res) -> {
                        System.out.println(pen + ":" + res.getSummaryPenalty());
                    });
                }
                maxCounter = 3000;
            }
            if (bestResult >= 0) {
                for (Phenotype<IntegerGene, Double> phenotype : evolutionResult.population()) {
                    if (phenotype.fitness() >= 0) {
                        goodPhenotypeCounter++;
                        if (goodPhenotypeCounter >= satisfiedScheduleAmount) {
                            break;
                        }
                    }
                }
            }
            if (maxCounter <= 0) {
                break;
            }

        }
        List<AlgoSchedule> sortedResults = evolutionResult
                .population()
                .stream()
                .sorted((ph1, ph2) -> Double.compare(ph2.fitness(), ph1.fitness()))
                .limit(satisfiedScheduleAmount)
                .map(this::phenotypeToLessons)
                .map((List<LessonWithTime> allLessons) ->
                        new AlgoSchedule(allLessons, penaltyChecker.calculatePenalty(allLessons)))
                .toList();
        return sortedResults;
    }

    private List<LessonWithTime> phenotypeToLessons(Phenotype<IntegerGene, Double> phenotype) {
        var gt = phenotype.genotype();
        List<LessonWithTime> timeList = new ArrayList<>();
        for (int i = 0; i < gt.length(); i++) {
            LessonGene lesson = lessonGenes.get(i);
            int cellIndex = gt.get(i).gene().intValue();
            AudienceTimeCell audCell = cells.get(cellIndex);
            LessonWithTime lessonWithTime = new LessonWithTime(audCell, lesson);
            timeList.add(lessonWithTime);
        }
        return timeList;
    }

    private void prepareData(List<StudyPlanEvolve> studyPlanEvolves, List<AudienceEvolve> audiences) {
        studyPlanEvolves.forEach(studyPlanEvolve -> {
            for (SubjectEvolve subjectEvolve : studyPlanEvolve.subjectEvolves()) {
                if (subjectEvolve.lectureAmount() != 0) {
                    LessonGene lessonGene = new LessonGene(new ArrayList<>(studyPlanEvolve.groupEvolves()), subjectEvolve.lectureTeacherEvolve(), subjectEvolve);

                    for (GroupEvolve group : lessonGene.groups()) {
                        if (!groupToIndexes.containsKey(group)) {
                            groupToIndexes.put(group, new ArrayList<>());
                        }
                        groupToIndexes.get(group).add(lessonGenes.size());
                    }

                    lessonGenes.add(lessonGene);
                }
                for (GroupEvolve groupEvolve : studyPlanEvolve.groupEvolves()) {
                    for (int subId = 0; subId < subjectEvolve.seminarAmount(); subId++) {
                        LessonGene lessonGene = new LessonGene(groupEvolve,
                                subjectEvolve.teacherToGroup().get(groupEvolve.id()),
                                subjectEvolve.withSubId(subId));
                        for (GroupEvolve group : lessonGene.groups()) {
                            if (!groupToIndexes.containsKey(group)) {
                                groupToIndexes.put(group, new ArrayList<>());
                            }
                            groupToIndexes.get(group).add(lessonGenes.size());
                        }
                        lessonGenes.add(lessonGene);


                    }
                }
            }
        });
        int times = TableTime.getDaysAmount() * TableTime.getCellsAmount();
        int lastIndex = 0;
        for (AudienceEvolve auditory : audiences) {
            for (int i = 0; i < times; i++) {
                AudienceTimeCell cell = new AudienceTimeCell(auditory, i);
                audienceToIndex.put(cell, lastIndex++);
                cells.add(cell);
                if (!audienceMap.containsKey(cell.audience().auditoryType())) {
                    audienceMap.put(cell.audience().auditoryType(), new ArrayList<>());
                }
                audienceMap.get(cell.audience().auditoryType()).add(cell);

            }
        }
    }

    private Double fitness(Genotype<IntegerGene> gt) {
        Map<Integer, List<LessonWithTime>> subjectCells = new HashMap<>();
        for (int i = 0; i < gt.length(); i++) {
            LessonGene lessonGene = lessonGenes.get(i);
            int cellIndex = gt.get(i).gene().intValue();
            AudienceTimeCell audCell = cells.get(cellIndex);
            LessonWithTime subjectCell = new LessonWithTime(audCell, lessonGene);
            if (!subjectCells.containsKey(audCell.time().toIndex())) {
                subjectCells.put(audCell.time().toIndex(), new ArrayList<>());
            }
            var timeList = subjectCells.get(audCell.time().toIndex());
            timeList.add(subjectCell);
        }
        double penalty = 0;


        for (Map.Entry<Integer, List<LessonWithTime>> integerListEntry : subjectCells.entrySet()) {
            List<LessonWithTime> timeCells = integerListEntry.getValue();
            penalty += penaltyChecker.calculatePenalty(timeCells).total();
        }
        return penalty;
    }

    private class MyMutator extends Mutator<IntegerGene, Double> {

        @Override
        protected MutatorResult<Genotype<IntegerGene>> mutate(Genotype<IntegerGene> genotype, double p, RandomGenerator random) {
            final int P = Probabilities.toInt(p);
            List<MutatorResult<Chromosome<IntegerGene>>> mutatorResults = new ArrayList<>();

            for (int i = 0; i < genotype.length(); i++) {
                IntegerChromosome chromosome = genotype.get(i).as(IntegerChromosome.class);
                if (random.nextInt() < P) {
                    mutatorResults.add(mutate(chromosome, i, p, random, new int[1]));
                } else {
                    mutatorResults.add(new MutatorResult<>(chromosome, 0));
                }
            }

            final ISeq<MutatorResult<Chromosome<IntegerGene>>> result = ISeq.of(mutatorResults);

            return new MutatorResult<>(
                    Genotype.of(result.map(MutatorResult::result)),
                    result.stream().mapToInt(MutatorResult::mutations).sum()
            );
        }


        protected MutatorResult<Chromosome<IntegerGene>> mutate(Chromosome<IntegerGene> chromosome, int index, double p, RandomGenerator random, int[] arr) {
            final int P = Probabilities.toInt(p);
            var integerChromosome = chromosome.as(IntegerChromosome.class);
            List<MutatorResult<IntegerGene>> mutatorResults = new ArrayList<>();

            var gene = integerChromosome.gene();
            if (random.nextInt() < P) {
                mutatorResults.add(new MutatorResult<>(mutate(gene, index, random, arr), 1));
            } else {
                mutatorResults.add(new MutatorResult<>(gene, 0));
            }

            final ISeq<MutatorResult<IntegerGene>> result = ISeq.of(mutatorResults);

            return new MutatorResult<>(
                    chromosome.newInstance(result.map(MutatorResult::result)),
                    result.stream().mapToInt(MutatorResult::mutations).sum()
            );
        }


        private IntegerGene mutate(IntegerGene gene, int index, RandomGenerator random, int[] arr) {

            LessonGene lesson = lessonGenes.get(index);
            AudienceTimeCell audienceTimeCell = cells.get(gene.intValue());


            List<AudienceTimeCell> possibleAudiences = audienceMap.get(lesson.teacher().teacherType());


            int randomCellIndex = random.nextInt(0, possibleAudiences.size());

            var cell = audienceToIndex.get(possibleAudiences.get(randomCellIndex));

            return gene.newInstance(cell);
        }
    }
}
