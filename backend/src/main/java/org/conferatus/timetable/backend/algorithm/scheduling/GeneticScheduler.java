package org.conferatus.timetable.backend.algorithm.scheduling;

import io.jenetics.Genotype;
import io.jenetics.IntegerChromosome;
import io.jenetics.IntegerGene;
import io.jenetics.Phenotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionStart;
import io.jenetics.util.Factory;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@NoArgsConstructor
public class GeneticScheduler {
    ArrayList<AuditoryTimeCell> cells = new ArrayList<>();
    ArrayList<LessonGene> lessonGenes = new ArrayList<>();
    List<Function<DataForConstraint, Double>> penalties = new ArrayList<>();
    private int satisfiedScheduleAmount = 3;

    public void setSatisfiedScheduleAmount(int satisfiedScheduleAmount) {
        this.satisfiedScheduleAmount = satisfiedScheduleAmount;
    }

    public void setPenalties(List<Function<DataForConstraint, Double>> newPenalties) {
        penalties = newPenalties;
    }

    public record DataForConstraint(List<LessonWithTime> allLessons, LessonWithTime currentLesson) {
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
        AudienceEvolve auditory2128 = new AudienceEvolve("2128", AudienceEvolve.AuditoryType.LECTURE);
        AudienceEvolve auditory3307 = new AudienceEvolve("3307", AudienceEvolve.AuditoryType.LECTURE);
        AudienceEvolve auditory3333 = new AudienceEvolve("3333", AudienceEvolve.AuditoryType.SEMINAR);
        AudienceEvolve auditory4444 = new AudienceEvolve("4444", AudienceEvolve.AuditoryType.SEMINAR);
        AudienceEvolve auditory5555 = new AudienceEvolve("5555", AudienceEvolve.AuditoryType.SEMINAR);
        AudienceEvolve auditory6666 = new AudienceEvolve("6666", AudienceEvolve.AuditoryType.SEMINAR);
        GroupEvolve groupEvolve21213 = new GroupEvolve("21213");
        GroupEvolve groupEvolve21214 = new GroupEvolve("21214");
//        Group group21215 = new Group("21215");
        GroupEvolve groupEvolve20214 = new GroupEvolve("20214");
        GroupEvolve groupEvolve20215 = new GroupEvolve("20215");
        TeacherEvolve gatilov = new TeacherEvolve("gatilov", AudienceEvolve.AuditoryType.LECTURE);
        TeacherEvolve kutalev = new TeacherEvolve("kutalev", AudienceEvolve.AuditoryType.SEMINAR);
        TeacherEvolve shvab = new TeacherEvolve("shvab", AudienceEvolve.AuditoryType.SEMINAR);
        TeacherEvolve molochev = new TeacherEvolve("molochev", AudienceEvolve.AuditoryType.SEMINAR);
        SubjectEvolve subjectEvolve20_1 = new SubjectEvolve("proga:20_1", 1, 1, List.of(kutalev, shvab, molochev), gatilov);
        SubjectEvolve subjectEvolve20_2 = new SubjectEvolve("proga:20_2", 1, 1, List.of(kutalev, shvab, molochev), gatilov);
        SubjectEvolve subjectEvolve21_1 = new SubjectEvolve("proga:21_1", 1, 1, List.of(kutalev, shvab, molochev), gatilov);
        SubjectEvolve subjectEvolve21_2 = new SubjectEvolve("proga:21_2", 1, 1, List.of(kutalev, shvab, molochev), gatilov);
        StudyPlanEvolve studyPlanEvolve20 = new StudyPlanEvolve(List.of(subjectEvolve20_1, subjectEvolve20_2), List.of(groupEvolve20214, groupEvolve20215));
        StudyPlanEvolve studyPlanEvolve21 = new StudyPlanEvolve(List.of(subjectEvolve21_1, subjectEvolve21_2), List.of(groupEvolve21213, groupEvolve21214));
        new GeneticScheduler().algorithm(List.of(studyPlanEvolve20, studyPlanEvolve21),
                List.of(auditory2128,
                        auditory3307,
                        auditory5555,
                        auditory6666,
                        auditory3333,
                        auditory4444
                ), 12,
                400);
    }

    public List<List<LessonWithTime>> algorithm(List<StudyPlanEvolve> studyPlanEvolves, List<AudienceEvolve> audiences, int times, int populationSize) {
        cells.clear();
        lessonGenes.clear();
        prepareData(studyPlanEvolves, audiences, times);

        final Factory<Genotype<IntegerGene>> gtf =
                Genotype.of(IntegerChromosome.of(0, cells.size()), lessonGenes.size());

        final Engine<IntegerGene, Double> engine = Engine
                .builder(this::fitness, gtf)
                .populationSize(populationSize)
                .build();
//        final EvolutionStatistics<Double, ?> statistics = EvolutionStatistics.ofNumber();
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
            AuditoryTimeCell audCell = cells.get(cellIndex);
            LessonWithTime lessonWithTime = new LessonWithTime(audCell, lesson);
            timeList.add(lessonWithTime);
        }
        return timeList;
    }

    private void prepareData(List<StudyPlanEvolve> studyPlanEvolves, List<AudienceEvolve> audiences, int times) {
        studyPlanEvolves.forEach(studyPlanEvolve -> {
            for (SubjectEvolve subjectEvolve : studyPlanEvolve.subjectEvolves) {
                if (subjectEvolve.lectureAmount != 0) {
                    LessonGene lessonGene = new LessonGene(new ArrayList<>(studyPlanEvolve.groupEvolves), subjectEvolve.lectureTeacherEvolve, subjectEvolve);
                    lessonGenes.add(lessonGene);
                }
                int i = 0;
                for (GroupEvolve groupEvolve : studyPlanEvolve.groupEvolves) {
                    LessonGene lessonGene = new LessonGene(groupEvolve, subjectEvolve.seminarTeacherEvolve.get(i), subjectEvolve);
                    lessonGenes.add(lessonGene);
                    i++;
                }
            }
        });
        for (AudienceEvolve auditory : audiences) {
            for (int i = 0; i < times; i++) {
                AuditoryTimeCell cell = new AuditoryTimeCell(auditory, i);
                cells.add(cell);
            }
        }
    }


    private Double fitness(Genotype<IntegerGene> gt) {
        Map<Integer, List<LessonWithTime>> subjectCells = new HashMap<>();
        for (int i = 0; i < gt.length(); i++) {
            LessonGene lessonGene = lessonGenes.get(i);
            int cellIndex = gt.get(i).gene().intValue();
            AuditoryTimeCell audCell = cells.get(cellIndex);
            LessonWithTime subjectCell = new LessonWithTime(audCell, lessonGene);
            if (!subjectCells.containsKey(audCell.times())) {
                subjectCells.put(audCell.times(), new ArrayList<>());
            }
            var timeList = subjectCells.get(audCell.times());
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
