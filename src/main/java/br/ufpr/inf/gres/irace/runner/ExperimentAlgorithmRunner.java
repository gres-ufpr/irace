/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.gres.irace.runner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import br.ufpr.inf.gres.irace.util.DirectoryUtils;
import br.ufpr.inf.gres.irace.enums.CrossoverOperatorType;
import br.ufpr.inf.gres.irace.enums.MutationOperatorType;
import br.ufpr.inf.gres.irace.enums.SelectionOperatorType;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.NullCrossover;
import org.uma.jmetal.operator.impl.crossover.SinglePointCrossover;
import org.uma.jmetal.operator.impl.mutation.BitFlipMutation;
import org.uma.jmetal.operator.impl.mutation.NullMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.operator.impl.selection.NaryTournamentSelection;
import org.uma.jmetal.operator.impl.selection.RandomSelection;
import org.uma.jmetal.operator.impl.selection.TournamentSelection;
import org.uma.jmetal.runner.AbstractAlgorithmRunner;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class ExperimentAlgorithmRunner extends AbstractAlgorithmRunner {

    /**
     * Write the population into two files and prints some data on screen
     *
     * @param population
     * @param path1
     * @param path2
     */
    public static void printFinalSolutionSet(List<? extends Solution<?>> population, String path1, String path2) {
        // Create the directory if it doesn't exist
        DirectoryUtils.createDirectory(path1);

        String varFileName = String.format("%s%sVAR_%s", path1, File.separator, path2);
        String funFileName = String.format("%s%sFUN_%s", path1, File.separator, path2);

        new SolutionListOutput(population)
                .setSeparator("\t")
                .setVarFileOutputContext(new DefaultFileOutputContext(varFileName))
                .setFunFileOutputContext(new DefaultFileOutputContext(funFileName))
                .print();
    }

    public static void printComputingTime(long computingTime, String path1, String path2) {
        try {
            // Create the directory if it doesn't exist
            DirectoryUtils.createDirectory(path1);

            String fileName = String.format("%s%sTIME_%s", path1, File.separator, path2);

            File file = new File(fileName);
            FileWriter principalFile = null;
            BufferedWriter bw = null;

            if (file.exists()) {
                principalFile = new FileWriter(file, true);
                bw = new BufferedWriter(principalFile);
            } else {
                file.createNewFile();
                principalFile = new FileWriter(file);
                bw = new BufferedWriter(principalFile);
            }

            bw.write(computingTime + "");
            bw.write(System.lineSeparator());

            bw.close();
            principalFile.close();
        } catch (IOException ex) {
            System.err.println("Error in printComputingTime - IOException. See Details: " + ex.getMessage());
        }
    }

    public static CrossoverOperator getCrossoverOperator(double crossoverProbability, CrossoverOperatorType crossoverOperator) {
        switch (crossoverOperator) {
            case SINGLE_POINT:
                return new SinglePointCrossover(crossoverProbability);
            default:
                return new NullCrossover();
        }
    }

    public static MutationOperator getMutationOperator(double mutationProbability, MutationOperatorType mutationOperator) {
        switch (mutationOperator) {
            case BIT_FLIP_MUTATION:
                return new BitFlipMutation(mutationProbability);
            default:
                return new NullMutation();
        }
    }

    public static SelectionOperator getSelectionOperator(SelectionOperatorType selectionOperator, int tournamentSize) {
        switch (selectionOperator) {
            case KTOURNAMENT:
                return new TournamentSelection(tournamentSize);
            case RANDOM:
                return new RandomSelection();
            case NARYTOURNAMENT:
                return new NaryTournamentSelection();
            case BINARY_RANKING_CROWDING:
                return new BinaryTournamentSelection<>(new RankingAndCrowdingDistanceComparator<>());
            default:
                return new BinaryTournamentSelection<>();
        }
    }
}
