package org.conferatus.timetable.backend.algorithm.experementations;

import org.conferatus.timetable.backend.algorithm.constraints.Penalty;
import org.conferatus.timetable.backend.algorithm.constraints.PenaltyChecker;
import org.conferatus.timetable.backend.algorithm.constraints.PenaltyEnum;
import org.conferatus.timetable.backend.algorithm.scheduling.*;
import org.conferatus.timetable.backend.model.AudienceType;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlgSimulation {
    public static void main(String[] args) throws InterruptedException {
        GeneticAlgorithmScheduler geneticAlgorithmScheduler = new GeneticAlgorithmScheduler();
        geneticAlgorithmScheduler.setPenalties(PenaltyChecker.newBuilder().addPenalties(Arrays.stream(PenaltyEnum.values())
                .toList()).build());

        int audiencesAmount = 400;
        int lectureAmount = 30;

        List<AudienceEvolve> audienceEvolves = new ArrayList<>(audiencesAmount + lectureAmount);
        for (long i = 1; i <= audiencesAmount; i++) {
            audienceEvolves.add(new AudienceEvolve(i, AudienceType.PRACTICAL));
        }
        for (long i = 1; i <= lectureAmount; i++) {
            audienceEvolves.add(new AudienceEvolve(i, AudienceType.LECTURE));
        }

        int groups = 16;
        int studyPlans = 3;

        int subjectsPerPlan = 7;
        Map<Long, List<GroupEvolve>> planNumberToGroups = new HashMap<>();
        Map<Long, List<SubjectEvolve>> planToSubjects = new HashMap<>();
        Map<Long, List<TeacherEvolve>> planToLectureTeachers = new HashMap<>();
        Map<Long, List<TeacherEvolve>> planToSeminarTeachers = new HashMap<>();
        for (long i = 1; i <= studyPlans; i++) {
            planNumberToGroups.put(i, new ArrayList<>(groups / studyPlans));
            planToSubjects.put(i, new ArrayList<>(subjectsPerPlan));
            planToLectureTeachers.put(i, new ArrayList<>());
            planToSeminarTeachers.put(i, new ArrayList<>());
        }


        for (long i = 1; i <= groups; i++) {
            long studyPlan = i % studyPlans + 1;
            var list = planNumberToGroups.get(studyPlan);
            var group = new GroupEvolve(i);
            list.add(group);
        }

        int teachersAmount = 4800;
        int lectureTeachersAmount = 1600;
        for (long i = 1; i <= teachersAmount; i++) {
            long studyPlan = i % studyPlans + 1;
            TeacherEvolve teacherEvolve = new TeacherEvolve(i, AudienceType.PRACTICAL);
            planToSeminarTeachers.get(studyPlan).add(teacherEvolve);

        }

        for (long i = 1; i <= lectureTeachersAmount; i++) {
            long studyPlan = i % studyPlans + 1;
            TeacherEvolve teacherEvolve = new TeacherEvolve(i, AudienceType.LECTURE);
            planToLectureTeachers.get(studyPlan).add(teacherEvolve);
        }

        planToSubjects.forEach((plaNumber, lsubjList) -> {
            List<TeacherEvolve> lectureT = new ArrayList<>(planToLectureTeachers.get(plaNumber));
            List<TeacherEvolve> seminarT = new ArrayList<>(planToSeminarTeachers.get(plaNumber));
            var plGroups = planNumberToGroups.get(plaNumber);


            for (long i = 1; i <= subjectsPerPlan; i++) {
                Map<Long, TeacherEvolve> teacherEvolveMap = new HashMap<>();
                for (GroupEvolve plGroup : plGroups) {
                    teacherEvolveMap.put(plGroup.id(), seminarT.remove(seminarT.size() - 1));
                }
                SubjectEvolve subjectEvolve = new SubjectEvolve(plaNumber, 1,
                        1, teacherEvolveMap, lectureT.remove(lectureT.size() - 1));
                lsubjList.add(subjectEvolve);
            }
        });
        List<StudyPlanEvolve> plansList = new ArrayList<>();
        for (long i = 1; i <= studyPlans; i++) {
            StudyPlanEvolve studyPlanEvolve = new StudyPlanEvolve(planToSubjects.get(i), planNumberToGroups.get(i));
            plansList.add(studyPlanEvolve);
        }
        Instant instant = Instant.now();
        System.out.println("Start");
        ExecutorService service = Executors.newSingleThreadExecutor();
        var algorithmStatus = geneticAlgorithmScheduler.asyncStart(plansList, audienceEvolves, service);
        algorithmStatus.getResult()
                .thenAccept(results -> {
                    Instant after = Instant.now();
                    System.out.println((double) (Date.from(after).getTime() - Date.from(instant).getTime()) / 1000);
                    System.out.println("total penalty: " + results.get(0).checkResult().total());
                    GeneticAlgorithmScheduler.AlgoSchedule firstSchedule = results.get(0);
                    System.out.println(firstSchedule.checkResult());
                    List<LessonWithTime> firstAllLessons = firstSchedule.allLessons();
                    firstAllLessons.sort((o1, o2) -> {
                        var t1 = o1.time();
                        var t2 = o2.time();
                        if (t1.day() != t2.day()) {
                            return Integer.compare(t1.day(), t2.day());
                        }
                        if (t1.cellNumber() != t2.cellNumber()) {
                            return Integer.compare(t1.cellNumber(), t2.cellNumber());
                        }
                        return 0;
                    });
                    List<LessonWithTime> firstLessonForGroup1 = firstAllLessons.stream()
                            .filter(lesson -> lesson.groups().stream().map(GroupEvolve::id).toList().contains(1L))
                            .toList();
                    for (LessonWithTime lessons : firstAllLessons) {
                        System.out.println(lessons.time() + ": " + lessons);
                    }


                    var checkResult = results.get(0).checkResult();
                    for (Map.Entry<Penalty, PenaltyChecker.PenaltyResult> penaltyPenaltyResultEntry : checkResult.penaltyToError().entrySet()) {
                        Penalty penalty = penaltyPenaltyResultEntry.getKey();
                        PenaltyChecker.PenaltyResult penResult = penaltyPenaltyResultEntry.getValue();
                        System.out.println(penalty + ":" + penResult);
                    }
                    System.out.println("\n\nGROUP1");
                    for (LessonWithTime lessons : firstLessonForGroup1) {
                        System.out.println(lessons.time() + ": " + lessons);
                    }
                }).join();
        while (!geneticAlgorithmScheduler.algorithmStatus.getResult().isDone()) {
            var perc = geneticAlgorithmScheduler.algorithmStatus.getPercentage();
            System.out.println(perc);
        }
        var status = geneticAlgorithmScheduler.algorithmStatus;
        while (true) {
            System.out.println(status.getPercentage());
            if (status.getResult().isDone()) {
                break;
            }
            Thread.sleep(50);
        }
        service.shutdown();
    }
}
