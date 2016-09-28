/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.gres.irace.evaluation.indicator;

import br.ufpr.inf.gres.irace.core.HookEvaluateCommands;
import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 */
public class HypervolumeIndicatorTest {

    @Test
    @Ignore
    public void testHV_NSGAII() {
        try {
            HookEvaluateCommands jct = new HookEvaluateCommands();

            jct.candidateId = 1;
            jct.directory = System.getProperty("user.dir") + File.separator + "execDir";
            jct.fileName = "c1.dat";

            new HypervolumeIndicator().run(jct);

            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    @Ignore
    public void testHV_SPEA2() {
        try {
            HookEvaluateCommands jct = new HookEvaluateCommands();

            jct.candidateId = 2;
            jct.directory = System.getProperty("user.dir") + File.separator + "execDir";
            jct.fileName = "c2.dat";

            new HypervolumeIndicator().run(jct);

            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
}
