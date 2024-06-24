package org.conferatus.timetable.backend.algorithm.constraints;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import org.conferatus.timetable.backend.algorithm.scheduling.GeneticAlgorithmScheduler;
import org.conferatus.timetable.backend.algorithm.scheduling.LessonWithTime;

@Getter
@Setter
public class PenaltyChecker {
    private final List<Penalty> penalties;

    public static PenaltyCheckerBuilder newBuilder() {
        return new PenaltyCheckerBuilder();
    }


    private PenaltyChecker(List<Penalty> penalties) {
        this.penalties = penalties;
    }

    @Getter
    @Setter
    public static final class CheckResult {
        final Map<Penalty, PenaltyResult> penaltyToError;
        double total;

        public CheckResult(Map<Penalty, PenaltyResult> penaltyToError) {
            this.penaltyToError = penaltyToError;
            this.total = 0;
        }

        public Map<Penalty, PenaltyResult> penaltyToError() {
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

    public record ProblemLesson(LessonWithTime lesson, String message) {
        @Override
        public String toString() {
            return "{" +
                    "L=" + lesson +
                    ":'" + message +
                    '}';
        }
    }

    public static class PenaltyResult {
        double summaryPenalty = 0;
        List<ProblemLesson> problemLessons = new ArrayList<>();

        public double getSummaryPenalty() {
            return summaryPenalty;
        }

        public List<ProblemLesson> getProblemLessons() {
            return problemLessons;
        }

        @Override
        public String toString() {
            return "PR{" +
                    "P=" + summaryPenalty +
                    "L=" + problemLessons +
                    '}';
        }
    }

    public CheckResult calculatePenalty(GeneticAlgorithmScheduler.DataForConstraint dataForOneLesson) {
        Map<Penalty, PenaltyResult> penaltyToError = new HashMap<>();
        var checkResult = new CheckResult(penaltyToError);
        penalties.forEach(penalty -> {
            var calculateResult = penalty.penaltyFunction.apply(dataForOneLesson);
            var penaltyValue = calculateResult.value();
            if (penaltyValue < 0) {
                if (!penaltyToError.containsKey(penalty)) {
                    penaltyToError.put(penalty, new PenaltyResult());
                }
                PenaltyResult result = penaltyToError.get(penalty);
                result.summaryPenalty += penaltyValue;
                checkResult.total += penaltyValue;
                result.problemLessons.add(new ProblemLesson(dataForOneLesson.currentLesson(), calculateResult.message()));
            }
        });
        return checkResult;
    }

    public CheckResult calculatePenalty(List<LessonWithTime> lessonWithTimes) {
        return calculatePenalty(lessonWithTimes, GeneticAlgorithmScheduler.DataForConstraint.generate(lessonWithTimes));
    }

    public CheckResult calculatePenalty(List<LessonWithTime> lessonWithTimes,
                                        List<List<List<LessonWithTime>>> allLessons) {
        Map<Penalty, PenaltyResult> penaltyToError = new HashMap<>();
        var checkResult = new CheckResult(penaltyToError);
        lessonWithTimes.forEach(lesson -> {
            var data = new GeneticAlgorithmScheduler.DataForConstraint(lessonWithTimes, lesson, allLessons);
            penalties.forEach(penalty -> {
                var calculateResult = penalty.penaltyFunction.apply(data);
                var value = calculateResult.value();
                if (value < 0) {
                    if (!penaltyToError.containsKey(penalty)) {
                        penaltyToError.put(penalty, new PenaltyResult());
                    }
                    PenaltyResult result = penaltyToError.get(penalty);
                    result.summaryPenalty += value;
                    checkResult.total += value;
                    result.problemLessons.add(new ProblemLesson(data.currentLesson(), calculateResult.message()));
                }
            });
                }
        );
        return checkResult;
    }


    public static class PenaltyCheckerBuilder {
        private final List<Penalty> penalties = new ArrayList<>();
        private final Set<Penalty> penaltySet = new HashSet<>();

        public PenaltyCheckerBuilder addPenalty(Penalty penalty) {
            if (!penaltySet.contains(penalty)) {
                penaltySet.add(penalty);
                penalties.add(penalty);
            }
            return this;
        }

        public PenaltyCheckerBuilder addPenalties(Collection<PenaltyEnum> penalties) {
            penalties.stream().map(PenaltyEnum::toPenalty).forEach(this::addPenalty);
            return this;
        }


        public PenaltyChecker build() {
            return new PenaltyChecker(penalties);
        }
    }
}
