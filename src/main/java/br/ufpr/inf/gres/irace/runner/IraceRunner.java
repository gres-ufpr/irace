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

import br.ufpr.inf.gres.irace.core.HookRunCommands;
import br.ufpr.inf.gres.irace.core.IHookRun;
import br.ufpr.inf.gres.irace.enums.AlgorithmType;
import br.ufpr.inf.gres.irace.enums.CrossoverOperatorType;
import br.ufpr.inf.gres.irace.enums.MutationOperatorType;
import br.ufpr.inf.gres.irace.enums.SelectionOperatorType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
public class IraceRunner extends ExperimentAlgorithmRunner implements IHookRun {

    @Override
    public void run(HookRunCommands jct) {
        try {
            System.out.println("Starting");
            BinaryProblem problem = new OneZeroMax();

            AlgorithmType algorithmType = AlgorithmType.getEnum(jct.algorithmName);

            MutationOperator mutationOperator = getMutationOperator(jct.mutationProbability, MutationOperatorType.getEnum(jct.mutationOperator));
            CrossoverOperator crossoverOperator = getCrossoverOperator(jct.crossoverProbability, CrossoverOperatorType.getEnum(jct.crossoverOperator));
            SelectionOperator selectionOperator = getSelectionOperator(SelectionOperatorType.getEnum(jct.selectionOperator), jct.tournamentSize);

            Algorithm<List<BinarySolution>> algorithm = null;

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

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            Date date = new Date();
            System.out.println("Started in " + dateFormat.format(date));

            //long runningTime = System.nanoTime();
            algorithm.run();

            // Ver se funciona aqui
            AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

            //long computingTime = System.nanoTime() - runningTime;
            List<BinarySolution> population = algorithm.getResult();

            printFinalSolutionSet(population, jct.directory, jct.candidateId + "");
            printComputingTime(algorithmRunner.getComputingTime(), jct.directory, jct.candidateId + "");

            date = new Date();
            System.out.println("Finished in " + dateFormat.format(date));

            System.gc();
            System.exit(0);
        } catch (Exception ex) {
            System.err.println("Error in MOHomIraceRunner.run - IOException. See Details: " + ex.getMessage());
            System.exit(1);
        }
    }
}
