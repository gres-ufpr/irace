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

import com.beust.jcommander.JCommander;
import java.io.IOException;
import  br.ufpr.inf.gres.irace.evaluation.indicator.HypervolumeIndicator;

/**
 * This class is a external part to use the <i>irace package</i> (for R) with
 * Java programs. This hook is the command that is executed every run and define
 * how will be executed the target program.
 *
 * <b>Details:</b>
 * <p>
 * When a race starts, each configuration is evaluated on the first instance by
 * means of the cost measure.
 *
 * The evaluation of a candidate configuration is done by means of a user-given
 * function or, alternatively, a user-given auxiliary program. The function (or
 * program name) is specified by the option hookRun. In this case, this file
 * defines how each candidate configuration will be evaluated.
 *
 * At each step of the race, the candidate configurations are evaluated on a
 * single instance. After each step, those candidate configurations that perform
 * statistically worse than at least another one are discarded, and the race
 * continues with the remaining surviving configurations.
 * </p>
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class HookEvaluate {

    public static void main(String[] args) throws IOException {
        HookEvaluateCommands jct = new HookEvaluateCommands();
        JCommander jCommander = new JCommander(jct, args);
        jCommander.setProgramName(HookEvaluate.class.getSimpleName());

        if (jct.help) {
            jCommander.usage();
            return;
        }

        System.out.println("[HookEvaluate] Parameters used: \n" + jct.toString());
        
        new HypervolumeIndicator().run(jct);
    }
}
