package org.conferatus.timetable.backend.algorithm.knapsack;

import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.Genotype;
import io.jenetics.Phenotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.util.Factory;

import static io.jenetics.engine.EvolutionResult.toBestPhenotype;
import static io.jenetics.engine.Limits.bySteadyFitness;

public class App {
    private final static int ITEM_COUNT = 10;
    private final static int KNAPSACK_SIZE = 20;
    private final static int POPULATION_SIZE = 500;
    private final Knapsack knapsack = Knapsack.initializeWithRandomItems(ITEM_COUNT, KNAPSACK_SIZE);

    public static void main(String[] args) {
        new App().run(POPULATION_SIZE);
    }

    public void run(int populationSize) {
        final Factory<Genotype<BitGene>> gtf =
                Genotype.of(BitChromosome.of(this.knapsack.getItemCount(), 0.3));
        final Engine<BitGene, Integer> engine = Engine
                .builder(this::fitness, gtf)
                .populationSize(populationSize)
                .build();
        final EvolutionStatistics<Integer, ?> statistics = EvolutionStatistics.ofNumber();
        final Phenotype<BitGene, Integer> best = engine.stream()
                // Truncate the evolution stream after 7 "steady"
                // generations.
                .limit(bySteadyFitness(10))
                // Update the evaluation statistics after
                // each generation
                .peek(statistics)
                // Collect (reduce) the evolution stream to
                // its best phenotype.
                .collect(toBestPhenotype());

        System.out.println(statistics);
        System.out.println(best);
        Genotype<BitGene> gen = best.genotype();
        for (int i = 0; i < knapsack.getItemCount(); i++) {
            System.out.println(knapsack.getItemByIndex(i));
        }
        System.out.println("!!");
        for (int i = 0; i < knapsack.getItemCount(); i++) {

            if (gen.chromosome().as(BitChromosome.class).get(i).bit()) {
                System.out.println(knapsack.getItemByIndex(i));
            }
        }
    }

    private Integer fitness(Genotype<BitGene> gt) {
        BitChromosome chromosome = gt.chromosome().as(BitChromosome.class);
        int size = 0;
        int value = 0;
        for (int i = 0; i < knapsack.getItemCount(); i++) {
            if (chromosome.get(i).bit()) {
                size += knapsack.getItemByIndex(i).getSize();
                value += knapsack.getItemByIndex(i).getValue();
            }
        }
        if (size > knapsack.getKnapsackSize()) {
            return -(size - knapsack.getKnapsackSize() + 1);
        }

        return value;
    }
}