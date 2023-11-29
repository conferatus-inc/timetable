package org.conferatus.timetable.backend.algorithm.constraints;

import org.conferatus.timetable.backend.algorithm.scheduling.GeneticAlgorithmScheduler;
import org.conferatus.timetable.backend.algorithm.scheduling.LessonWithTime;

import java.util.*;

public class PenaltyChecker {
    private final List<Penalty> penalties;
    private final EnumSet<Penalty> penaltySet;

    public static PenaltyCheckerBuilder newBuilder() {
        return new PenaltyCheckerBuilder();
    }


    private PenaltyChecker(List<Penalty> penalties, EnumSet<Penalty> penaltySet) {
        this.penalties = penalties;
        this.penaltySet = penaltySet;
    }

    public static final class CheckResult {
        final EnumMap<Penalty, PenaltyResult> penaltyToError;
        double total;

        public CheckResult(EnumMap<Penalty, PenaltyResult> penaltyToError) {
            this.penaltyToError = penaltyToError;
            this.total = 0;
        }

        public EnumMap<Penalty, PenaltyResult> penaltyToError() {
            return penaltyToError;
        }

        public double total() {
            return total;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (CheckResult) obj;
            return Objects.equals(this.penaltyToError, that.penaltyToError) &&
                    Double.doubleToLongBits(this.total) == Double.doubleToLongBits(that.total);
        }

        @Override
        public int hashCode() {
            return Objects.hash(penaltyToError, total);
        }

        @Override
        public String toString() {
            return "CheckResult[" +
                    "penaltyToError=" + penaltyToError + ", " +
                    "total=" + total + ']';
        }


    }

    public class PenaltyResult {
        double summaryPenalty = 0;
        List<LessonWithTime> problemLessons = new ArrayList<>();

        public double getSummaryPenalty() {
            return summaryPenalty;
        }
    }

    public CheckResult calculatePenalty(GeneticAlgorithmScheduler.DataForConstraint dataForOneLesson) {
        EnumMap<Penalty, PenaltyResult> penaltyToError = new EnumMap<>(Penalty.class);
        var checkResult = new CheckResult(penaltyToError);
        penalties.forEach(penalty -> {
            double value = penalty.penaltyFunction.apply(dataForOneLesson);
            if (value < 0) {
                if (!penaltyToError.containsKey(penalty)) {
                    penaltyToError.put(penalty, new PenaltyResult());
                }
                PenaltyResult result = penaltyToError.get(penalty);
                result.summaryPenalty += value;
                checkResult.total += value;
                result.problemLessons.add(dataForOneLesson.currentLesson());
            }
        });
        return checkResult;
    }

    public CheckResult calculatePenalty(List<LessonWithTime> lessonWithTimes) {
        EnumMap<Penalty, PenaltyResult> penaltyToError = new EnumMap<>(Penalty.class);
        var checkResult = new CheckResult(penaltyToError);
        lessonWithTimes.forEach(lesson -> {
                    var data = new GeneticAlgorithmScheduler.DataForConstraint(lessonWithTimes, lesson);
                    penalties.forEach(penalty -> {
                        double value = penalty.penaltyFunction.apply(data);
                        if (value < 0) {
                            if (!penaltyToError.containsKey(penalty)) {
                                penaltyToError.put(penalty, new PenaltyResult());
                            }
                            PenaltyResult result = penaltyToError.get(penalty);
                            result.summaryPenalty += value;
                            checkResult.total += value;
                            result.problemLessons.add(data.currentLesson());
                        }
                    });
                }
        );
        return checkResult;
    }


    public static class PenaltyCheckerBuilder {
        private final List<Penalty> penalties = new ArrayList<>();
        private final EnumSet<Penalty> penaltySet = EnumSet.noneOf(Penalty.class);

        public PenaltyCheckerBuilder addPenalty(Penalty penalty) {
            if (!penaltySet.contains(penalty)) {
                penaltySet.add(penalty);
                penalties.add(penalty);
            }
            return this;
        }

        public PenaltyCheckerBuilder addPenalties(Collection<Penalty> penalties) {
            penalties.forEach(this::addPenalty);
            return this;
        }


        public PenaltyChecker build() {
            return new PenaltyChecker(penalties, penaltySet);
        }
    }
}
