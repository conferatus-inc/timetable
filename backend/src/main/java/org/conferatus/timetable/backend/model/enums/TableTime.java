package org.conferatus.timetable.backend.model.enums;

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
        return day + "/" + cellNumber;
    }
}
