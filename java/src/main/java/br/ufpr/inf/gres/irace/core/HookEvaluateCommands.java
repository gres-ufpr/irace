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
public class HookEvaluateCommands {

    @Parameter(names = {"-help", "-h"}, help = true)
    public boolean help;

    @Parameter(names = {"--candidateId", "-ci"}, description = "ID of this execution. Used for differentiating the output files. (int)", required = true)
    public int candidateId;
    
    @Parameter(names = {"--fileName", "-fn"}, description = "File name that will put the result of evaluation (String)", required = true)
    public String fileName;
    
    @Parameter(names = {"--directory", "-dir"}, description = "Directory name (String)", required = true)
    public String directory;
    
    @Override
    public String toString() {        
        return "--candidateId=" + candidateId + "\n"                
                + "--fileName=" + fileName + "\n"
                + "--directory=" + directory + "\n";               
    }
}
