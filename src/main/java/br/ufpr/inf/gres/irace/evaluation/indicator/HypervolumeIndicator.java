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
package br.ufpr.inf.gres.irace.evaluation.indicator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import br.ufpr.inf.gres.irace.core.HookEvaluateCommands;
import br.ufpr.inf.gres.irace.core.IHookEvaluate;
import br.ufpr.inf.gres.irace.measure.qualityindicator.HypervolumeCalculator;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class HypervolumeIndicator implements IHookEvaluate {

    @Override
    public void run(HookEvaluateCommands jct) {
        try {
            HypervolumeCalculator hv = new HypervolumeCalculator();

            File dir = new File(jct.directory);

            for (File file : dir.listFiles((directory, name) -> name.startsWith("FUN_"))) {
                hv.addParetoFront(file.getPath());
            }

            File pointsHV = new File(jct.directory + File.separator + "EXTREME_POINTS_HV.properties");

            FileBasedConfigurationBuilder<FileBasedConfiguration> builder
                    = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                    .configure(new Parameters().fileBased()
                            .setFile(pointsHV)
                            .setListDelimiterHandler(new DefaultListDelimiterHandler(',')));

            Configuration config = null;
            
            if (pointsHV.exists()) {
                config = builder.getConfiguration();

                List<Double> min = config.getList(Double.class, "Min");
                List<Double> max = config.getList(Double.class, "Max");

                hv.addMinMaxReference(min.stream().mapToDouble(d -> d).toArray(), max.stream().mapToDouble(d -> d).toArray());
            } else {
                pointsHV.createNewFile();
                config = builder.getConfiguration();
            }

            config.setProperty("Min", String.join(",", Arrays.toString(hv.getMinimumValues()).replace("[", "").replace("]", "").trim()));
            config.setProperty("Max", String.join(",", Arrays.toString(hv.getMaximumValues()).replace("[", "").replace("]", "").trim()));

            // save properties to project resources folder            
            builder.save();

            double hypervolume = hv.calculateHypervolume(jct.directory + File.separator + "FUN_" + jct.candidateId);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(jct.directory + File.separator + jct.fileName), false))) {
                writer.write(hypervolume + "");
            }

            System.gc();
            System.exit(0);
        } catch (IOException ex) {
            System.err.println("Failed in to save hypervolume value. See details: \n" + ex.getMessage());
        } catch (ConfigurationException ex) {
            System.err.println("Failed in commons configuration2. See details: \n" + ex.getMessage());
        }

        System.exit(1);
    }
}
