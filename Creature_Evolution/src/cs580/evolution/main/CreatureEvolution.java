/**
 * CS580 Spring 2015 Team 5 Term project
 */
package cs580.evolution.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import cs580.evolution.function.EnviGen;
import cs580.evolution.function.Fitness;
import cs580.evolution.function.Mutation;
import cs580.evolution.function.Reproduce;
import cs580.evolution.pojo.Environment;
import cs580.evolution.pojo.Genome;

/**
 * @author Natalia Anpilova
 * @author Ashwin Hole
 * @author Divya Kondaviti
 * @author Ankith Raj
 * @author Manjusha
 *
 */
public class CreatureEvolution {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//TODO after progress check: add choice from input args: read population from file or generate random
		//List<Genome> initPopulation = new ArrayList<Genome>();
		/*
		 * generating random initial population
		 */
		List<Genome> initPopulation = generateRandomPopulation(1000);	//arbitrary number of individuals
		//TODO after progress check: save randomly generated population to a file so it's possible to re-use it later
		
		//TODO after progress check: set environmental characteristics from input args
		Environment initEnvironment = new Environment();
		
		//TODO now just one input argument - number of generations; will add the rest and validation later
		if (args.length > 0) {
			int generationsNumber = Integer.parseInt(args[0]);
			Entry<Integer, Genome> winner = geneticAlgoritm(initPopulation, initEnvironment, generationsNumber);
			System.out.println("WINNER:");	
			System.out.println("individual with genome:");
			System.out.println(winner.getValue().toString());
			System.out.println("fitness level:");
			System.out.println(winner.getKey());
		} else
			System.err.println("Invalid number of input arguments");
	}

	/**
	 * Main logic
	 * @param initPopulation
	 * @param initEnvironment
	 * @param generationsNumber
	 * @return winner's genome
	 */
	public static Entry<Integer, Genome> geneticAlgoritm(List<Genome> initPopulation, Environment initEnvironment, int generationsNumber) {
		List<Genome> population = initPopulation;	//current population, parents
		List<Genome> newPopulation = null;			//offspring
		Genome mom, dad, child;
		Environment environment = initEnvironment;
		EnviGen envGenerator = new EnviGen();
		Reproduce reproduce = new Reproduce();
		Mutation mutation = new Mutation();
		//TODO instantiate selection function
		
		//outer loop: one iteration = one generation
		for (int generationCount = 1; generationCount <= generationsNumber; generationCount++) {
			newPopulation = new ArrayList<Genome>();
			
			/*
			 * parents pre-selection
			 */
			//TODO culling - discard individuals whose fitness is too low (define threshold in parent selector class)
			//will be like: population = parentSelector.cull(population, environment), use fitness function fitness.getFitnessLevel() in cull()
			
			//inner loop: generating offspring as new population
			//TODO maybe: instead of population size of the upper limit, use food limit from current environment state: if no more food left, no more offspring produced
			for (int i = 0; i < population.size(); i++) {
				/*
				 * parents selection
				 */
				//TODO: both mom and dad will be selected like: mom = parentSelection.getParent(population, environment), use fitness function fitness.getFitnessLevel()
				mom = population.get(0);
				dad = population.get(1);
				
				/*
				 * reproduce
				 */
				//TODO after progress check: adjust metabolism with weight, height and other genes. Also account for unlikely situations like height is 10cm and weight is 1 ton. -Natalia
				child = reproduce.produceChild(mom, dad);
				mom.addOffspring(1);
				dad.addOffspring(1);
				
				/*
				 * mutate
				 */
				if (mutation.mutationProbability() > 0.99)
					child = Mutation.mutate(child);
				
				//offspring added
				newPopulation.add(child);
			}
			
			//offspring will be parents in the next iteration
			population.clear();
			population.addAll(newPopulation);
			
			/*
			 * environment change with every new generation
			 */
			environment = envGenerator.EG(environment, generationCount);
		}
		
		//select individual with max fitness level
		Entry<Integer, Genome> winner = getWinner(population, environment);
		
		//TODO after progress check: use some existing or new method in parent selection class to calculate prevalence of offspring (maybe)
		return winner;
	}
	
	/**
	 * Generates random population of the given size
	 * @param size number of individuals in population
	 * @return population consisting of randomly generated genomes
	 */
	private static List<Genome> generateRandomPopulation(int size) {
		List<Genome> randomPopulation = new ArrayList<Genome>();
		for (int i = 0; i < size; i++) {
			randomPopulation.add(Genome.generateRandomGenome());
		}
		return randomPopulation;
	}
	
	/**
	 * Determines the winner individual - the genome with the highest fitness level
	 * @param population
	 * @param envmt environment
	 * @return the <fitnessLevel, Genome> entry with the highest fitness level
	 */
	private static Entry<Integer, Genome> getWinner(List<Genome> population, Environment envmt) {
		TreeMap<Integer, Genome> popuMap = new TreeMap<Integer, Genome>();	//to store calculated fitness levels for population
		Fitness fit = new Fitness();
		int fitLevel;
		
		//calculate fitness for population and add to the map
		for (Genome gen : population) {
			fitLevel = fit.getFitnessLevel(gen, envmt);
			popuMap.put(fitLevel, gen);
		}
		
		//the <fitnessLevel, Genome> entry with the highest fitness level
		return popuMap.lastEntry();
	}
}
