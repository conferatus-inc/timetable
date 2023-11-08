package org.conferatus.timetable.backend.algorithm.knapsack;

import io.jenetics.internal.util.Requires;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Knapsack {
    private final List<Item> possibleKnapsackItems; // items that *might* end up in the knapsack, depending on chromosome
    private final int knapsackSize;

    public Knapsack(List<Item> possibleItems, int knapsackSize) {
        this.possibleKnapsackItems = possibleItems;
        this.knapsackSize = knapsackSize;
    }

    public static Knapsack initializeWithRandomItems(int size, int knapsackSize) {
        Random random = new Random(123);
        List<Item> items = Stream.generate(() ->
                        new Item((int) (random.nextDouble() * 10), (int) (random.nextDouble() * 10)))
                .limit(size)
                .collect(Collectors.toList());
        return new Knapsack(items, knapsackSize);
    }

    public Item getItemByIndex(int index) {
        return this.possibleKnapsackItems.get(index);
    }

    public int getItemCount() {
        return this.possibleKnapsackItems.size();
    }

    public int getKnapsackSize() {
        return this.knapsackSize;
    }

    public static final class Item {
        private final int size;
        private final int value;

        public Item(final int size, final int value) {
            this.size = Requires.nonNegative(size);
            this.value = Requires.nonNegative(value);
        }

        public int getSize() {
            return size;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "size=" + size +
                    ", value=" + value +
                    '}';
        }
    }
}
