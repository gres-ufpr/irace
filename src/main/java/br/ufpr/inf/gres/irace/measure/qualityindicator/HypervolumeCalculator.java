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
package br.ufpr.inf.gres.irace.measure.qualityindicator;

import java.io.FileNotFoundException;
import java.util.List;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.util.PointSolution;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class HypervolumeCalculator {

    private double[] maximumValues;
    private double[] minimumValues;

    public HypervolumeCalculator() {
        this.maximumValues = null;
        this.minimumValues = null;
    }

    public double[] getMaximumValues() {
        return maximumValues;
    }

    public double[] getMinimumValues() {
        return minimumValues;
    }

    public void addParetoFront(Front front) {
        double[] tempMinimum = FrontUtils.getMinimumValues(front);
        double[] tempMaximum = FrontUtils.getMaximumValues(front);

        addMinMaxReference(tempMinimum, tempMaximum);
    }

    public void addMinMaxReference(double[] tempMinimum, double[] tempMaximum) {
        // Compare min 
        if (minimumValues == null) {
            minimumValues = tempMinimum;
        } else {
            for (int i = 0; i < tempMinimum.length; i++) {
                minimumValues[i] = Double.min(tempMinimum[i], minimumValues[i]);
            }
        }

        // Compare max
        if (maximumValues == null) {
            maximumValues = tempMaximum;
        } else {
            for (int i = 0; i < tempMaximum.length; i++) {
                maximumValues[i] = Double.max(tempMaximum[i], maximumValues[i]);
            }
        }
    }

    public void addParetoFront(List<? extends Solution<?>> front) {
        addParetoFront(new ArrayFront(front));
    }

    public void addParetoFront(String path) throws FileNotFoundException {
        addParetoFront(new ArrayFront(path));
    }

    public void clear() {
        this.minimumValues = null;
        this.maximumValues = null;
    }

    public double calculateHypervolume(String frontPath) throws FileNotFoundException {
        return calculateHypervolume(new ArrayFront(frontPath));
    }

    public double calculateHypervolume(List<? extends Solution<?>> front) {
        return calculateHypervolume(new ArrayFront(front));
    }

    public double calculateHypervolume(Front front) {
        return calculateHypervolume(front, maximumValues, minimumValues);
    }

    public double calculateHypervolume(String frontPath, double[] maximumValues, double[] minimumValues) throws FileNotFoundException {
        return calculateHypervolume(new ArrayFront(frontPath), maximumValues, minimumValues);
    }

    public double calculateHypervolume(List<? extends Solution<?>> front, double[] maximumValues, double[] minimumValues) {
        return calculateHypervolume(new ArrayFront(front), maximumValues, minimumValues);
    }

    public double calculateHypervolume(Front front, double[] maximumValues, double[] minimumValues) {
        if (maximumValues != null && minimumValues != null) {
            FrontNormalizer frontNormalizer = new FrontNormalizer(minimumValues, maximumValues);
            Front normalizedFront = frontNormalizer.normalize(front);
            List<PointSolution> normalizedPopulation = FrontUtils.convertFrontToSolutionList(normalizedFront);

            return new PISAHypervolume<PointSolution>(normalizedFront).evaluate(normalizedPopulation);
        }
        return 0D;
    }
}
