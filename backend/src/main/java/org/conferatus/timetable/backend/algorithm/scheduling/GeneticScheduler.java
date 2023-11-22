package org.conferatus.timetable.backend.algorithm.scheduling;

import io.jenetics.Genotype;
import io.jenetics.IntegerChromosome;
import io.jenetics.IntegerGene;
import io.jenetics.Phenotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionStart;
import io.jenetics.util.Factory;
import lombok.NoArgsConstructor;
import org.conferatus.timetable.backend.algorithm.constraints.Penalties;
import org.conferatus.timetable.backend.model.AudienceType;
import org.conferatus.timetable.backend.model.TableTime;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;

@NoArgsConstructor
public class GeneticScheduler {
    ArrayList<AudienceTimeCell> cells = new ArrayList<>();
    ArrayList<LessonGene> lessonGenes = new ArrayList<>();
    List<Function<DataForConstraint, Double>> penalties = new ArrayList<>();
    private int satisfiedScheduleAmount = 3;

    public void setSatisfiedScheduleAmount(int satisfiedScheduleAmount) {
        this.satisfiedScheduleAmount = satisfiedScheduleAmount;
    }

    public void setPenalties(List<Function<DataForConstraint, Double>> newPenalties) {
        penalties = newPenalties;
    }

    public record DataForConstraint(List<LessonWithTime> allLessons,
                                    LessonWithTime currentLesson,
                                    List<List<List<LessonWithTime>>> timeTable) {
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

        private static List<List<List<LessonWithTime>>> generate(List<LessonWithTime> lessons) {
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
    }

    public GeneticScheduler(int satisfiedScheduleAmount) {
        this.satisfiedScheduleAmount = satisfiedScheduleAmount;
    }

    public GeneticScheduler(List<Function<DataForConstraint, Double>> penalties) {
        this.penalties = penalties;
    }

    public GeneticScheduler(List<Function<DataForConstraint, Double>> penalties, int satisfiedScheduleAmount) {
        this.penalties = penalties;
        this.satisfiedScheduleAmount = satisfiedScheduleAmount;
    }

    public static void main(String[] args) {
        AudienceEvolve auditory2128 = new AudienceEvolve("2128", AudienceType.LECTURE);
        AudienceEvolve auditory3307 = new AudienceEvolve("3307", AudienceType.LECTURE);
        AudienceEvolve auditory3333 = new AudienceEvolve("3333", AudienceType.PRACTICAL);
        AudienceEvolve auditory4444 = new AudienceEvolve("4444", AudienceType.PRACTICAL);
        AudienceEvolve auditory5555 = new AudienceEvolve("5555", AudienceType.PRACTICAL);
        AudienceEvolve auditory6666 = new AudienceEvolve("6666", AudienceType.PRACTICAL);
        GroupEvolve groupEvolve21213 = new GroupEvolve("21213");
        GroupEvolve groupEvolve21214 = new GroupEvolve("21214");
        GroupEvolve groupEvolve20214 = new GroupEvolve("20214");
        GroupEvolve groupEvolve20215 = new GroupEvolve("20215");
        TeacherEvolve gatilov = new TeacherEvolve("gatilov", AudienceType.LECTURE);
        TeacherEvolve kutalev = new TeacherEvolve("kutalev", AudienceType.PRACTICAL);
        TeacherEvolve shvab = new TeacherEvolve("shvab", AudienceType.PRACTICAL);
        TeacherEvolve molochev = new TeacherEvolve("molochev", AudienceType.PRACTICAL);
        SubjectEvolve subjectEvolve20_1 = new SubjectEvolve("proga:20_1", 1, 1, List.of(kutalev, shvab, molochev), gatilov);
        SubjectEvolve subjectEvolve20_2 = new SubjectEvolve("proga:20_2", 1, 1, List.of(kutalev, shvab, molochev), gatilov);
        SubjectEvolve subjectEvolve21_1 = new SubjectEvolve("proga:21_1", 1, 1, List.of(kutalev, shvab, molochev), gatilov);
        SubjectEvolve subjectEvolve21_2 = new SubjectEvolve("proga:21_2", 1, 1, List.of(kutalev, shvab, molochev), gatilov);
        StudyPlanEvolve studyPlanEvolve20 = new StudyPlanEvolve(List.of(subjectEvolve20_1, subjectEvolve20_2), List.of(groupEvolve20214, groupEvolve20215));
        StudyPlanEvolve studyPlanEvolve21 = new StudyPlanEvolve(List.of(subjectEvolve21_1, subjectEvolve21_2), List.of(groupEvolve21213, groupEvolve21214));
        Instant instant = Instant.now();
        List<List<LessonWithTime>> results = new GeneticScheduler(Arrays.stream(Penalties.values()).map(Penalties::getPenaltyFunction).toList())
                .algorithm(List.of(studyPlanEvolve20, studyPlanEvolve21),
                        List.of(auditory2128,
                                auditory3307,
                                auditory5555,
                                auditory6666,
                                auditory3333,
                                auditory4444
                        ), TableTime.getDaysAmount() * TableTime.getCellsAmount());
        Instant after = Instant.now();
        System.out.println(Date.from(after).getTime() - Date.from(instant).getTime());
        for (int i = 0; i < results.get(0).size(); i++) {
            var lessons = results.get(0).get(i);
            System.out.println(lessons.time() + ": " + lessons);
        }
    }

    public List<List<LessonWithTime>> algorithm(List<StudyPlanEvolve> studyPlanEvolves,
                                                List<AudienceEvolve> audiences,
                                                int populationSize) {
        cells.clear();
        lessonGenes.clear();
        prepareData(studyPlanEvolves, audiences);

        final Factory<Genotype<IntegerGene>> gtf =
                Genotype.of(IntegerChromosome.of(0, cells.size()), lessonGenes.size());


        final Engine<IntegerGene, Double> engine = Engine
                .builder(this::fitness, gtf)
                .populationSize(populationSize)
                .build();
        var evolutionResult = engine.evolve(EvolutionStart.empty());
        int goodPhenotypeCounter = 0;
        while (goodPhenotypeCounter < satisfiedScheduleAmount) {
            evolutionResult = engine.evolve(evolutionResult.next());
            goodPhenotypeCounter = 0;
            for (Phenotype<IntegerGene, Double> phenotype : evolutionResult.population()) {
                if (phenotype.fitness() >= 0) {
                    goodPhenotypeCounter++;
                    if (goodPhenotypeCounter >= satisfiedScheduleAmount) {
                        break;
                    }
                }
            }
        }
        List<List<LessonWithTime>> sortedResults = evolutionResult
                .population()
                .stream()
                .sorted((ph1, ph2) -> Double.compare(ph2.fitness(), ph1.fitness()))
                .limit(satisfiedScheduleAmount)
                .map(this::phenotypeToLessons)
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
                    lessonGenes.add(lessonGene);
                }
                int i = 0;
                for (GroupEvolve groupEvolve : studyPlanEvolve.groupEvolves()) {
                    for (int subId = 0; subId < subjectEvolve.seminarAmount(); subId++) {
                        LessonGene lessonGene = new LessonGene(groupEvolve, subjectEvolve.seminarTeacherEvolve().get(i),
                                subjectEvolve.withSubId(subId));
                        lessonGenes.add(lessonGene);
                    }
                    i++;
                }
            }
        });
        int times = TableTime.getDaysAmount() * TableTime.getCellsAmount();
        for (AudienceEvolve auditory : audiences) {
            for (int i = 0; i < times; i++) {
                AudienceTimeCell cell = new AudienceTimeCell(auditory, i);
                cells.add(cell);
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
            for (LessonWithTime timeCell : timeCells) {
                for (Function<DataForConstraint, Double> penaltyFunction : penalties) {
                    penaltyFunction.apply(new DataForConstraint(timeCells, timeCell));
                }
            }
        }

        return -penalty;
    }
}
