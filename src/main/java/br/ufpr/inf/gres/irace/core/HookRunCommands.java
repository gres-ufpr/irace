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
package br.ufpr.inf.gres.irace.core;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
@Parameters(separators = " =")
public class HookRunCommands {

    @Parameter(names = {"-help", "-h"}, help = true)
    public boolean help;

    @Parameter(names = {"--directory", "-dir"}, description = "Directory name (String)", required = true)
    public String directory;
    
    @Parameter(names = {"--candidateId", "-ci"}, description = "ID of this execution. Used for differentiating the output files. (int)", required = true)
    public int candidateId;
    
    @Parameter(names = {"--algorithm", "-alg"}, description = "The algorithm name that will be executed (String)", required = true)
    public String algorithmName;

    @Parameter(names = "--populationSize", description = "Population size (int)", required = true)
    public int populationSize;

    @Parameter(names = "--maxEvaluations", description = "Maximum Evaluations (int)", required = true)
    public int maxEvaluations;
    
    @Parameter(names = "--selectionOperator", description = "Selection Operator (String)", required = true)
    public String selectionOperator;

    @Parameter(names = "--tournamentSize", description = "Tournament Size for the K-Tournament Selection Operator (int)")
    public int tournamentSize;

    @Parameter(names = "--crossoverOperator", description = "Crossover Operator (String)", required = true)
    public String crossoverOperator;

    @Parameter(names = "--crossoverProbability", description = "Crossover Probability (double)")
    public double crossoverProbability;

    @Parameter(names = "--mutationOperator", description = "Mutation Operator (String)", required = true)
    public String mutationOperator;

    @Parameter(names = "--mutationProbability", description = "Mutation Probability (double)")
    public double mutationProbability;

//    @Parameter(names = "--archiveSize", description = " Archive Size in relation the Population Size (double)")
//    public double archiveSize;

    @Override
    public String toString() {              
        return  "--directory=" + directory + "\n"
                + "--candidateId=" + candidateId + "\n"                
                + "--algorithm=" + algorithmName + "\n"                
                + "--populationSize=" + populationSize + "\n"
                + "--maxEvaluations=" + maxEvaluations + "\n"                
                + "--selectionOperator=" + selectionOperator  + "\n"
                + "--tournamentSize=" + tournamentSize + "\n" 
                + "--crossoverOperator=" + crossoverOperator + "\n"
                + "--crossoverProbability=" + crossoverProbability + "\n"
                + "--mutationOperator=" + mutationOperator + "\n"
                + "--mutationProbability=" + mutationProbability + "\n";
                //+ "--archiveSize=" + archiveSize + "\n";
    }
}
