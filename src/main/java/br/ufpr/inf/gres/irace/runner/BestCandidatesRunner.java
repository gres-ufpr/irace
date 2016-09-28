/*
 * Copyright 2016 Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.ufpr.inf.gres.irace.runner;

import com.beust.jcommander.JCommander;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import static br.ufpr.inf.gres.irace.runner.ExperimentAlgorithmRunner.getCrossoverOperator;
import static br.ufpr.inf.gres.irace.runner.ExperimentAlgorithmRunner.getMutationOperator;
import static br.ufpr.inf.gres.irace.runner.ExperimentAlgorithmRunner.getSelectionOperator;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.spea2.SPEA2Builder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.BinaryProblem;
import org.uma.jmetal.problem.multiobjective.OneZeroMax;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.AlgorithmRunner;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class BestCandidatesRunner extends ExperimentAlgorithmRunner {

    public static void main(String[] args) {
        try {

            File best_candidates_tunning = new File(BestCandidatesRunner.class.getClassLoader().getResource("best_candidates_tunning.properties").getPath());

            FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                    .configure(new Parameters().fileBased()
                            .setFile(best_candidates_tunning)
                            .setListDelimiterHandler(new DefaultListDelimiterHandler(',')));

            Configuration config = builder.getConfiguration();

            List<String> candidates = config.getList(String.class, "Candidates");

            for (String candidate : candidates) {
                args = candidate.replace("  ", " ").replace("=", " ").split(" ");

                BestCandidatesCommands jct = new BestCandidatesCommands();
                JCommander jCommander = new JCommander(jct, args);
                jCommander.setProgramName(BestCandidatesRunner.class.getSimpleName());

                System.out.println("Candidate: " + jct.candidateId);
                System.out.println("Command Line: " + candidate);

                System.out.println("Starting");
                BinaryProblem problem = new OneZeroMax();

                br.ufpr.inf.gres.irace.enums.AlgorithmType algorithmType = br.ufpr.inf.gres.irace.enums.AlgorithmType.getEnum(jct.algorithmName);

                MutationOperator mutationOperator = getMutationOperator(jct.mutationProbability, br.ufpr.inf.gres.irace.enums.MutationOperatorType.getEnum(jct.mutationOperator));
                CrossoverOperator crossoverOperator = getCrossoverOperator(jct.crossoverProbability, br.ufpr.inf.gres.irace.enums.CrossoverOperatorType.getEnum(jct.crossoverOperator));
                SelectionOperator selectionOperator = getSelectionOperator(br.ufpr.inf.gres.irace.enums.SelectionOperatorType.getEnum(jct.selectionOperator), jct.tournamentSize);

                Algorithm<List<BinarySolution>> algorithm = null;

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

                System.out.println("Started in " + dateFormat.format(new Date()));

                for (int i = 1; i <= 10; i++) {
                    String run = i + "";
                    System.out.println("Run: " + run);
                    switch (algorithmType) {
                        case NSGAII:
                            algorithm = new NSGAIIBuilder<>(problem, crossoverOperator, mutationOperator)
                                    .setSelectionOperator(selectionOperator)
                                    .setMaxEvaluations(jct.maxEvaluations)
                                    .setPopulationSize(jct.populationSize)
                                    .build();
                            break;
                        case SPEA2:
                            algorithm = new SPEA2Builder<>(problem, crossoverOperator, mutationOperator)
                                    .setSelectionOperator(selectionOperator)
                                    .setMaxIterations(jct.maxEvaluations)
                                    .setPopulationSize(jct.populationSize)
                                    .build();
                            break;                            
                    }

                    //long runningTime = System.nanoTime();
                    algorithm.run();
                    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
                    //long computingTime = System.nanoTime() - runningTime;                    
                    List<BinarySolution> population = algorithm.getResult();

                    String directory = String.format("tunningResults/best_candidates/%s/%s/best_candidate_%s", problem.getName(), algorithm.getName(), jct.candidateId);

                    printFinalSolutionSet(population, directory, run);
                    printComputingTime(algorithmRunner.getComputingTime(), directory, run);
                }

                System.out.println("Finished in " + dateFormat.format(new Date()) + "\n\n\n");
            }

            System.gc();
            System.exit(0);
        } catch (Exception ex) {
            System.err.println("Error in BestCandidatesRunner. See Details: " + ex.getMessage());
            System.exit(1);
        }
    }
}
