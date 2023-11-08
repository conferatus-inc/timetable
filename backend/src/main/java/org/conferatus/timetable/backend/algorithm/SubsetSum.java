package org.conferatus.timetable.backend.algorithm;

import io.jenetics.EnumGene;
import io.jenetics.Mutator;
import io.jenetics.PartiallyMatchedCrossover;
import io.jenetics.Phenotype;
import io.jenetics.engine.*;
import io.jenetics.internal.collection.Array;
import io.jenetics.internal.collection.ArrayISeq;
import io.jenetics.util.ISeq;

import java.util.function.Function;

public class SubsetSum implements Problem<ISeq<Integer>, EnumGene<Integer>, Integer> {
    @Override
    public Function<ISeq<Integer>, Integer> fitness() {
        return (integers -> integers.stream().reduce(0, Integer::sum));
    }

    @Override
    public Codec<ISeq<Integer>, EnumGene<Integer>> codec() {
        return Codecs.ofSubSet(new ArrayISeq<>(Array.ofLength(5)), 5);
    }

    public static void main(String[] args) {
        SubsetSum problem = new SubsetSum();
        Engine<EnumGene<Integer>, Integer> engine = Engine.builder(problem)
                .minimizing()
                .maximalPhenotypeAge(5)
                .alterers(new PartiallyMatchedCrossover<>(0.4), new Mutator<>(0.3))
                .build();
        Phenotype<EnumGene<Integer>, Integer> result = engine.stream()
                .limit(Limits.bySteadyFitness(55))
                .collect(EvolutionResult.toBestPhenotype());
    }
    // implementation
}