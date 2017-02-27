# irace
## A simple example for the IRACE application with Java programs

[![Gittip](https://img.shields.io/badge/Latest%20stable-1.0-green.svg?style=flat-squared)]()
[![Gittip](https://img.shields.io/badge/build-passing-brightgreen.svg)]()

# Requirements

- Java 1.7 or above to run
- Maven >= 3.3.9
- R >= 3.3.0
- IRACE (R package) = 1.07

Try execute the command above for install the irace specific version in R:

```
require(devtools)
install_version("irace", version = "1.07", repos = "http://cran.us.r-project.org")
```

# How do I start?

1. Access [here](https://github.com/gres-ufpr/irace/archive/master.zip) and dowload the project;
2. Unzip in some directory of your choise;
3. Make the command:

```
mvn install
```
4. The target folder will be created. You must to check if the .jar (project) and the lib folder were created in the target folder.

5. Go to the irace folder;
6. Create a folder called "execDir";
7. Open the terminal and execute:

```R
R < irace.run.R --no-save
```

Or Open the terminal in the irace folder and type R for open it (R program), after:

1. Install in R the irace package
2. Type require(irace)
3. Type irace.cmdline("")

# Tips

## About the files

### tune-conf

The principal file that contains the Configuration for Iterated Race (iRace).

In this file you must set your preferences for the tunning (details in the file).

For pattern is configured to run in parallel with 8 threads. You can change to run using sge cluster uncommenting the line **sgeCluster = 1** and commenting the **parallel = 8**.

You can change too the test confidence level, directory with the results of the test, etc.

### parameters

The parameters that will be passed to the algorithm (details in the file).

### instances-list

It's a file that contains problems that will be tested.

### hook-run (Unix OS) / hook-run.bat (Windows OS)

Represents how the algorithm will be called and executed.

This hook is the command that is executed every run. This hook is run in the execution directory (execDir, --exec-dir), the same directory where hook-evaluate is executed. Hence, you may need to copy extra files needed by the executable to this directory.

This hook is executed, after is called the hook-evaluate.

### hook-run-sge-cluster (Unix OS)

The same function that the hook-run, but constains commands to use qsub. 

You must to check the list of clusters!

### hook-evaluate (Unix OS) / hook-evaluate.bat (Windows OS)

Represents how the algorithm will be evaluated. This hook is run for each candidate to evaluate it after all candidate configurations have been run on a single instance.

### forbidden

A set of commands to ignore, for example, **crossoverProbability == "0.00"**.

### execute_best_candidates.sh 

A command that you execute to evaluate the best candidates when there are not the **best**. You must to set the **best_candidates_tunning.properties** file in resources (**src\main\resources**) - after make the command to install again.

With this "feature" you can compare the time, hypervolume, etc, for 10 evaluations (configurable in the source code).

The results will appear in tunningResults folder.

# Screenshots

![Using irace-run.R script](http://i.imgur.com/XbxFOby.png)
![IRACE running](http://i.imgur.com/YwvokQb.png)
