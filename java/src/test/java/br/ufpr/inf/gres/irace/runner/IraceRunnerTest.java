/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.gres.irace.runner;

import br.ufpr.inf.gres.irace.core.HookRunCommands;
import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 */
public class IraceRunnerTest {   

    @Test      
    @Ignore
    public void testNSGAII() {
        try {
            HookRunCommands jct = new HookRunCommands();

            jct.candidateId = 1;
            jct.directory = System.getProperty("user.dir") + File.separator +  "execDir";
            jct.algorithmName = "NSGA-II";
            jct.populationSize = 10;
            jct.maxEvaluations = 10;
            jct.selectionOperator = "Binary";
            jct.crossoverOperator = "SinglePoint";
            jct.crossoverProbability = 0.9;
            jct.mutationOperator = "BitFlipMutation";
            jct.mutationProbability = 0.3;

            new IraceRunner().run(jct);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test    
    @Ignore
    public void testSPEA2() {
        try {
            HookRunCommands jct = new HookRunCommands();

            jct.candidateId = 2;
            jct.directory = System.getProperty("user.dir") + File.separator +  "execDir";
            jct.algorithmName = "SPEA2";
            jct.populationSize = 10;
            jct.maxEvaluations = 10;
            jct.selectionOperator = "Binary";
            jct.crossoverOperator = "SinglePoint";
            jct.crossoverProbability = 0.9;
            jct.mutationOperator = "BitFlipMutation";
            jct.mutationProbability = 0.3;

            new IraceRunner().run(jct);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
}
