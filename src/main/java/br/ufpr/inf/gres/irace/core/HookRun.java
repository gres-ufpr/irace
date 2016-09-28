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
import com.beust.jcommander.ParameterException;
import br.ufpr.inf.gres.irace.enums.SelectionOperatorType;
import br.ufpr.inf.gres.irace.runner.IraceRunner;

/**
 * This class is a external part to use the <i>irace package</i> (for R) with
 * Java programs. This hook is the command that is executed every run and define
 * how will be executed the target program.
 *
 * <b>Details extracted from
 * http://iridia.ulb.ac.be/IridiaTrSeries/link/IridiaTr2011-004.pdf:</b>
 * <p>
 * HookRun is invoked for each candidate configuration, passing as arguments:
 * the instance, a numeric identifier, and the <i>commandline</i> parameters of
 * the candidate configuration. The numeric identifier uniquely identifies a
 * configuration within a race (but not across the races in a single iterated
 * race). The
 * <i>commandline</i> is constructed by appending to each parameter label
 * (switch), without separator, the value of the parameter, following the order
 * given in the parameter table. The program hookRun must print (only) a real
 * number, which corresponds to the cost measure of the candidate configuration
 * for the given instance. The working directory of hookRun is set to the
 * execution directory specified by the option execDir. This allows the user to
 * execute several runs of irace in parallel without the runs interfering with
 * each other.</p>
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class HookRun {

    public static void main(String[] args) throws Exception {
        HookRunCommands jct = new HookRunCommands();
        JCommander jCommander = new JCommander(jct, args);
        jCommander.setProgramName(HookRun.class.getSimpleName());

        if (jct.help) {
            jCommander.usage();
            return;
        }

        if (jct.selectionOperator.equals(SelectionOperatorType.KTOURNAMENT.getValue()) && jct.tournamentSize == 0) {
            throw new ParameterException("The following option is required: --tournamentSize");
        }

        System.out.println("[HookRun] Parameters used:");

        System.out.println(jct.toString());

        new IraceRunner().run(jct);
    }
}
