package org.conferatus.timetable.backend.model.enums;

import java.time.DayOfWeek;
import java.util.Objects;

public record TableTime(int day, int cellNumber) {
    public int toIndex() {
        return day * cellsAmount + cellNumber;
    }

    private static int daysAmount = 6;
    private static int cellsAmount = 6;

    public TableTime(int index) {
        this(index / cellsAmount, index % cellsAmount);
    }

    public static int getDaysAmount() {
        return daysAmount;
    }

    public static void setDaysAmount(int daysAmount) {
        TableTime.daysAmount = daysAmount;
    }

    public static int getCellsAmount() {
        return cellsAmount;
    }

    public static void setCellsAmount(int cellsAmount) {
        TableTime.cellsAmount = cellsAmount;
    }

    @Override
    public String toString() {
        return DayOfWeek.of(day + 1) + "/" + (cellNumber + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableTime tableTime = (TableTime) o;
        return day == tableTime.day && cellNumber == tableTime.cellNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, cellNumber);
    }

}
