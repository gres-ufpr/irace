library(irace)

# Create the R objects scenario and parameters
parameters<-readParameters("parameters.txt")
scenario<-readScenario(filename="scenario.txt",scenario=defaultScenario())
irace(scenario= scenario,parameters= parameters)