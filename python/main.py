import argparse
import logging
import matplotlib.pyplot as plt
import multiprocessing
import numpy as np
import pandas as pd
import random

from deap import base
from deap import creator
from deap import tools
from scipy.sparse import csr_matrix
#from scoop import futures
from sklearn.datasets import load_svmlight_file
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeClassifier

__author__ = "Jackson Antonio do Prado Lima"
__email__ = "jacksonpradolima@gmail.com"
__license__ = "GPL"
__version__ = "1.0"

# Split my data in 3 set with same size: Train, Test, and Validation.
# Train and Validation are used in the fitness function, after all, the Test dataset is used to evaluate the best individual
features, labels = load_svmlight_file("../data/data")

x, X_test, y, y_test = train_test_split(features, labels, test_size=0.3333, random_state=42, stratify=labels)
X_train, X_cv, y_train, y_cv = train_test_split(x, y, test_size=0.5, train_size=0.5, random_state=42, stratify=y)

# Classifier instance
clf = DecisionTreeClassifier()

# Store fitness evolution
graph = []
# Store features
num_features = []

### Functions

def run_classifier(_X_train, _X_test, _y_test):
    """
    Execute the classifier
    """
    model = clf.fit(_X_train, y_train)
    predictions = clf.predict(_X_test) 
    score = clf.score(_X_test, _y_test)
    probas = clf.predict_proba(_X_test)

    return predictions, probas, score

def select_idx(individual):
    """
    Get the indexes with 1 (ones) from a individual - this means what features were selected
    """
    return np.array(individual).nonzero()[0].tolist()

def select_features(columns, dataset):
    """
    Filter the dataset only the features selected
    """
    df = pd.DataFrame(dataset.toarray())
    return csr_matrix(pd.DataFrame(df, columns=columns))

def evaluate(individual, _x_test, _y_test):
    """
    Evaluate the dataset with the feature set selected
    """
    columns = select_idx(individual)
    return run_classifier(select_features(columns, X_train), select_features(columns, _x_test), _y_test)

def evalOneMax(individual):
    """
    Fitness function
    """
    predictions, probas, score = evaluate(individual, X_cv, y_cv)
    return score,

def store_fitness(pop):
    logging.debug("--- Fitness ---")
    length = len(pop)
    # Gather all the fitnesses in one list and logging.debug the stats
    fits = [ind.fitness.values[0] for ind in pop]
            
    mean = sum(fits) / length
    sum2 = sum(x*x for x in fits)
    std = abs(sum2 / length - mean**2)**0.5
            
    logging.debug("  Min %s" % min(fits))
    logging.debug("  Max %s" % max(fits))
    logging.debug("  Avg %s" % mean)
    logging.debug("  Std %s" % std)
    
    graph.append(max(fits))

def store_features(pop):
    logging.debug("--- Features ---")
    length = len(pop)
    nf = [np.count_nonzero(ind) for ind in pop]
    
    mean = sum(nf) / length
    sum2 = sum(x*x for x in nf)
    std = abs(sum2 / length - mean**2)**0.5
            
    logging.debug("  Min %s" % min(nf))
    logging.debug("  Max %s" % max(nf))
    logging.debug("  Avg %s" % mean)
    logging.debug("  Std %s" % std)
    num_features.append(max(nf))

### DEAP CONFIGUTATION ###
creator.create("FitnessMax", base.Fitness, weights=(1.0,))
creator.create("Individual", list, fitness=creator.FitnessMax)

toolbox = base.Toolbox()

# Attribute generator
toolbox.register("attr_bool", random.randint, 0, 1)

# Structure initializers
toolbox.register("individual", tools.initRepeat, creator.Individual, toolbox.attr_bool, 132)
toolbox.register("population", tools.initRepeat, list, toolbox.individual)

# Operator registering
toolbox.register("evaluate", evalOneMax)
toolbox.register("mate", tools.cxOnePoint)
toolbox.register("mutate", tools.mutFlipBit, indpb=0.05)
toolbox.register("select", tools.selTournament, tournsize=3)

# SCOOP
#toolbox.register("map", futures.map)

def main(POP, CXPB, MUTPB, DATFILE):
    random.seed(64)

    # Process Pool of 4 workers
    #pool = multiprocessing.Pool(processes=4)
    pool = multiprocessing.Pool(processes=2)
    toolbox.register("map", pool.map)

    ## population size
    pop = toolbox.population(n=POP)

    ## Probabilities for Crossover, Mutation and number of generations (iterations)
    NGEN = 300

    logging.debug("Start of evolution")

    # Evaluate the entire population
    fitnesses = list(map(toolbox.evaluate, pop))
    for ind, fit in zip(pop, fitnesses):
        ind.fitness.values = fit
    
    logging.debug("  Evaluated %i individuals" % len(pop))

    # Begin the evolution
    for g in range(NGEN):
        logging.debug("\n-- Generation %i --" % g)

        # Select the next generation individuals
        offspring = toolbox.select(pop, len(pop))

        # Clone the selected individuals
        offspring = list(map(toolbox.clone, offspring))

        # Apply crossover and mutation on the offspring
        for child1, child2 in zip(offspring[::2], offspring[1::2]):
            if random.random() < CXPB:
                toolbox.mate(child1, child2)
                del child1.fitness.values
                del child2.fitness.values

        for mutant in offspring:
            if random.random() < MUTPB:
                toolbox.mutate(mutant)
                del mutant.fitness.values

        # Evaluate the individuals with an invalid fitness
        invalid_ind = [ind for ind in offspring if not ind.fitness.valid]

        #invalid_ind = [ind for ind in offspring]
        fitnesses = map(toolbox.evaluate, invalid_ind)
        for ind, fit in zip(invalid_ind, fitnesses):
            ind.fitness.values = fit
            #logging.debug("Ind: {} - Fit: {}".format(ind, fit))

        logging.debug("  Evaluated %i individuals" % len(invalid_ind))

        # The population is entirely replaced by the offspring
        pop[:] = offspring

        #store_fitness(pop)
        #store_features(pop)

    logging.debug("-- End of (successful) evolution --")

    best_ind = tools.selBest(pop, 1)[0]
    logging.debug("Best individual is %s, %s" % (best_ind, best_ind.fitness.values))

    predictions, probas, score = evaluate(best_ind, X_test, y_test)

    logging.debug("Test score: {} - N. features selected: {}".format(score, np.count_nonzero(best_ind)))

    # line =plt.plot(graph)
    # plt.show()

    # line =plt.plot(num_features)
    # plt.show()

    with open(DATFILE, 'w') as f:
        f.write(str(score*100))

    pool.close()

if __name__ == "__main__":
    ap = argparse.ArgumentParser(description='Feature Selection using GA with DecisionTreeClassifier')
    ap.add_argument("-v", "--verbose", help="increase output verbosity", action="store_true")
    ap.add_argument('--pop', dest='pop', type=int, required=True, help='Population size')
    ap.add_argument('--cros', dest='cros', type=float, required=True, help='Crossover probability')
    ap.add_argument('--mut', dest='mut', type=float, required=True, help='Mutation probability')
    ap.add_argument('--datfile', dest='datfile', type=str, required=True, help='File where it will be save the score (result)')

    args = ap.parse_args()

    if args.verbose:
        logging.basicConfig(level=logging.DEBUG)

    logging.debug(args)

    main(args.pop, args.cros, args.mut, args.datfile)
