package org.conferatus.timetable.backend.algorithm.scheduling;

import io.jenetics.Genotype;
import io.jenetics.IntegerChromosome;
import io.jenetics.IntegerGene;
import io.jenetics.Phenotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.util.Factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.jenetics.engine.EvolutionResult.toBestPhenotype;
import static io.jenetics.engine.Limits.bySteadyFitness;

public class SchedSolver {
    public ArrayList<AuditoryTimeCell> cells = new ArrayList<>();
    public ArrayList<SubjGene> subjGenes = new ArrayList<>();


    public static void main(String[] args) {
        AuditoryEvolve auditory2128 = new AuditoryEvolve("2128", AuditoryEvolve.AuditoryType.LECTURE);
        AuditoryEvolve auditory3307 = new AuditoryEvolve("3307", AuditoryEvolve.AuditoryType.LECTURE);
        AuditoryEvolve auditory3333 = new AuditoryEvolve("3333", AuditoryEvolve.AuditoryType.SEMINAR);
        AuditoryEvolve auditory4444 = new AuditoryEvolve("4444", AuditoryEvolve.AuditoryType.SEMINAR);
        AuditoryEvolve auditory5555 = new AuditoryEvolve("5555", AuditoryEvolve.AuditoryType.SEMINAR);
        AuditoryEvolve auditory6666 = new AuditoryEvolve("6666", AuditoryEvolve.AuditoryType.SEMINAR);
        Group group21213 = new Group("21213");
        Group group21214 = new Group("21214");
        Group group21215 = new Group("21215");
        Group group20214 = new Group("20214");
        Group group20215 = new Group("20215");
        Teacher gatilov = new Teacher("gatilov", AuditoryEvolve.AuditoryType.LECTURE);
        Teacher kutalev = new Teacher("kutalev", AuditoryEvolve.AuditoryType.SEMINAR);
        Teacher shvab = new Teacher("shvab", AuditoryEvolve.AuditoryType.SEMINAR);
        Teacher molochev = new Teacher("molochev", AuditoryEvolve.AuditoryType.SEMINAR);
        Subject subject20_1 = new Subject("proga:20_1", 1, 1, List.of(kutalev, shvab, molochev), gatilov);
        Subject subject20_2 = new Subject("proga:20_2", 1, 1, List.of(kutalev, shvab, molochev), gatilov);
        Subject subject21_1 = new Subject("proga:21_1", 1, 1, List.of(kutalev, shvab, molochev), gatilov);
        Subject subject21_2 = new Subject("proga:21_2", 1, 1, List.of(kutalev, shvab, molochev), gatilov);
        StudyPlan studyPlan20 = new StudyPlan(List.of(subject20_1, subject20_2), List.of(group20214, group20215));
        StudyPlan studyPlan21 = new StudyPlan(List.of(subject21_1, subject21_2), List.of(group21213, group21214, group21215));
        new SchedSolver().run(List.of(studyPlan20, studyPlan21),
                List.of(auditory2128,
                        auditory3307,
                        auditory5555,
                        auditory3333,
                        auditory4444
                ), 6,
                400);
    }

    private record AuditoryTimeCell(AuditoryEvolve auditory, int times) {
    }

    private record SubjGene(List<Group> groups, Teacher teacher, Subject subject) {
        public SubjGene(Group group, Teacher teacher, Subject subject) {
            this(new ArrayList<>(List.of(group)), teacher, subject);
        }

        @Override
        public String toString() {
            return "{G:" + groups + "}" +
                    " {T:" + teacher + "}" +
                    " {S:" + subject + "}";
        }
    }

    public record SubjCell(AuditoryTimeCell cell, SubjGene subjGene) {
        @Override
        public String toString() {
            return cell.auditory +
                    ":" + subjGene;
        }
    }

    public void run(List<StudyPlan> studyPlans, List<AuditoryEvolve> auditories, int times, int populationSize) {

        studyPlans.forEach(studyPlan -> {
            for (Subject subject : studyPlan.subjects) {
                if (subject.lectureAmount != 0) {
                    SubjGene subjGene = new SubjGene(new ArrayList<>(studyPlan.groups), subject.lectureTeacher, subject);
                    subjGenes.add(subjGene);
                }
                int i = 0;
                for (Group group : studyPlan.groups) {
                    SubjGene subjGene = new SubjGene(group, subject.seminarTeacher.get(i), subject);
                    subjGenes.add(subjGene);
                    i++;
                }
            }
        });
        for (AuditoryEvolve auditory : auditories) {
            for (int i = 0; i < times; i++) {
                AuditoryTimeCell cell = new AuditoryTimeCell(auditory, i);
                cells.add(cell);
            }
        }


        final Factory<Genotype<IntegerGene>> gtf =
                Genotype.of(IntegerChromosome.of(0, cells.size()), subjGenes.size());
        final Engine<IntegerGene, Double> engine = Engine
                .builder(this::fitness, gtf)
                .populationSize(populationSize)
                .build();
        final EvolutionStatistics<Double, ?> statistics = EvolutionStatistics.ofNumber();
        final Phenotype<IntegerGene, Double> best = engine.stream()
                // Truncate the evolution stream after 7 "steady"
                // generations.
                .limit(bySteadyFitness(1000))
                .limit(5000)
                // Update the evaluation statistics after
                // each generation
                .peek(statistics)
                // Collect (reduce) the evolution stream to
                // its best phenotype.
                .collect(toBestPhenotype());

        System.out.println(statistics);
        System.out.println(best);
        var gt = best.genotype();
        Map<Integer, List<SubjCell>> subjectCells = new HashMap<>();
        for (int i = 0; i < gt.length(); i++) {
            SubjGene subjGene = subjGenes.get(i);
            int cellIndex = gt.get(i).gene().intValue();
            AuditoryTimeCell audCell = cells.get(cellIndex);
            SubjCell subjectCell = new SubjCell(audCell, subjGene);
            if (!subjectCells.containsKey(audCell.times)) {
                subjectCells.put(audCell.times, new ArrayList<>());
            }
            var timeList = subjectCells.get(audCell.times);
            timeList.add(subjectCell);
        }
        subjectCells.forEach((time, cellz) -> {
            System.out.println("time: " + time);
            for (SubjCell subjCell : cellz) {
                System.out.println(subjCell);
            }
            System.out.println("#".repeat(10) + "\n");
        });


    }

    private Double fitness(Genotype<IntegerGene> gt) {
        Map<Integer, List<SubjCell>> subjectCells = new HashMap<>();
        for (int i = 0; i < gt.length(); i++) {
            SubjGene subjGene = subjGenes.get(i);
            int cellIndex = gt.get(i).gene().intValue();
            AuditoryTimeCell audCell = cells.get(cellIndex);
            SubjCell subjectCell = new SubjCell(audCell, subjGene);
            if (!subjectCells.containsKey(audCell.times)) {
                subjectCells.put(audCell.times, new ArrayList<>());
            }
            var timeList = subjectCells.get(audCell.times);
            timeList.add(subjectCell);
        }

        double penalty = 0;
        for (Map.Entry<Integer, List<SubjCell>> integerListEntry : subjectCells.entrySet()) {
            int time = integerListEntry.getKey();
            List<SubjCell> timeCells = integerListEntry.getValue();
            for (SubjCell timeCell : timeCells) {
                //aud type
                if (!timeCell.cell.auditory.auditoryType
                        .equals(timeCell.subjGene.teacher.teacherType)) {
                    penalty += 20;
                }
                //audit unique
                if (timeCells.stream()
                        .anyMatch(subjCell -> !subjCell.equals(timeCell)
                                && subjCell.cell.auditory.id.equals(timeCell.cell.auditory.id))) {
                    penalty += 5;
                }
                //teacher uniq
                if (timeCells.stream()
                        .anyMatch(subjCell -> !subjCell.equals(timeCell)
                                && subjCell.subjGene.teacher.id.equals(timeCell.subjGene.teacher.id))) {
                    penalty += 5;
                }
                //groups uniq
                if (!timeCell.cell.auditory.auditoryType.equals(AuditoryEvolve.AuditoryType.LECTURE)
                        && timeCells.stream()
                        .anyMatch(subjCell -> !subjCell.equals(timeCell)
                                && subjCell.subjGene.groups.stream().anyMatch(
                                timeCell.subjGene.groups::contains
                        ))
                ) {
                    penalty += 5;
                }
            }
        }

        return -penalty;
    }
}
