package org.conferatus.timetable.backend.algorithm.constraints;

record CalculateResult(double value, String message) {
    CalculateResult(double value) {
        this(value, "OK");
    }

    public static CalculateResult ok() {
        return new CalculateResult(0);
    }

    public static CalculateResult ok(double value) {
        return new CalculateResult(value);
    }

    public static CalculateResult problem(double value, String message) {
        return new CalculateResult(value, message);
    }
}