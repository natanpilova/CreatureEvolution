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
import cs580.evolution.function.Selection;
import cs580.evolution.pojo.Environment;
import cs580.evolution.pojo.Genome;

/**
 * @author Natalia Anpilova
 * @author Ashwin Hole
 * @author Divya Kondaviti
 * @author Ankith Raj
 * @author Manjusha Upadhye
 *
 */
public class CreatureEvolution {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//TODO after progress check: add choice from input args: read population from file or generate random; verbose flag
		//List<Genome> initPopulation = new ArrayList<Genome>();
		/*
		 * generating random initial population
		 */
		List<Genome> initPopulation = generateRandomPopulation(1001);	//arbitrary number of individuals - TODO input arg
		//TODO after progress check: save randomly generated population to a file so it's possible to re-use it later
		
		//TODO after progress check: set environmental characteristics from input args
		/*
		 * initial livable environment
		 */
		Environment initEnvironment = new Environment();
		
		//TODO now just one input argument - number of generations; will add the rest and validation later
		if (args.length > 0) {
			int generationsNumber = Integer.parseInt(args[0]);
			Entry<Integer, Genome> winner = geneticAlgoritm(initPopulation, initEnvironment, generationsNumber);
			if (winner == null)
				System.out.println("\n*** NO WINNER ***");
			else {
				System.out.println("\n*********** WINNER ***********");	
				System.out.println("Individual with genome:");
				System.out.println(winner.getValue().toString());
				System.out.println("Fitness level: " + winner.getKey());
			}
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
		List<Genome> population = initPopulation;		//current whole population
		TreeMap<Integer, Genome> parentsPool = null;	//potential parents - population (with fitness levels) after discarding individuals with low fitness
		List<Genome> parents = null;					//actual parents list
		List<Genome> newPopulation = null;				//offspring
		Genome mom, dad, child;
		Environment environment = initEnvironment;
		EnviGen envGenerator = new EnviGen();
		Reproduce reproduce = new Reproduce();
		Mutation mutation = new Mutation();
		Selection selection = new Selection();
		double tmp;	//to calculate log10 for generation number output
		
		System.out.println("Initial population size = " + population.size());
		
		System.out.println("\nInitial environment:");
		System.out.println(environment.toString());
		System.out.println();
		
		/*
		 * outer loop: one iteration = one generation
		 */
		for (int generationCount = 1; generationCount <= generationsNumber; generationCount++) {
			/*
			 * output generation number - log10 scale
			 */
			tmp = Math.log10(generationCount);
			if (tmp == (int)tmp || generationCount == generationsNumber)
				System.out.println("Generation " + generationCount);
			newPopulation = new ArrayList<Genome>();
			
			/*
			 * potential parents pre-selection based on the fitness level
			 */
			//System.out.print("culling...");
			parentsPool = selection.cullPopulation(population, environment);
			//System.out.println("...done.");
			System.out.println("** generation "+generationCount+" culled population size " + parentsPool.size());
			if (parentsPool.isEmpty() || parentsPool.size() < 2) {
				System.out.println("WARNING: Not enough (" + parentsPool.size() + ") individuals in the parents pool");
				System.out.println("Final population size = " + population.size());
				System.out.println("\nFinal environment:");
				System.out.println(environment.toString());
				return null;
			}
			
			/*
			 * inner loop: generating offspring as new population
			 */
			//TODO maybe: instead of population size of the upper limit, use food limit from current environment state: if no more food left, no more offspring produced
			for (int i = 0; i < population.size(); i++) {
				/*
				 * parents selection
				 */
				//System.out.print("selection parents (culled pop size "+parentsPool.size()+") for child "+i+"...");
				parents = selection.getParents(parentsPool, environment);
				//System.out.println("...done.");
				mom = parents.get(0);
				dad = parents.get(1);
				
				/*
				 * reproduce
				 */
				//TODO after progress check: adjust metabolism with weight, height and other genes. Also account for unlikely situations like height is 10cm and weight is 1 ton. -Natalia
				child = reproduce.produceChild(mom, dad);
				//TODO add offspring count back to corresponding genomes in parentsPool 
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
			
			//offspring can be parents in the next iteration
			population.clear();
			population.addAll(newPopulation);
			
			/*
			 * environment change with every new generation
			 */
			environment = envGenerator.EG(environment, generationCount);
		}
		
		System.out.println("Final population size = " + population.size());
		System.out.println("\nFinal environment:");
		System.out.println(environment.toString());
		
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
	 * @return the <fitnessLevel, Genome> map entry with the highest fitness level
	 */
	private static Entry<Integer, Genome> getWinner(List<Genome> population, Environment envmt) {
		//calculate fitness levels for population
		Fitness fit = new Fitness();
		TreeMap<Integer, Genome> popuMap = fit.calculatePopulationFitness(population, envmt);
		
		//the <fitnessLevel, Genome> entry with the highest fitness level
		return popuMap.lastEntry();
	}
}
