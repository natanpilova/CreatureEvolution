/**
 * CS580 Spring 2015 Team 5 Term project
 */
package cs580.evolution.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import cs580.evolution.function.EnviGen;
import cs580.evolution.function.Fitness;
import cs580.evolution.function.Mutation;
import cs580.evolution.function.Reproduce;
import cs580.evolution.function.Selection;
import cs580.evolution.pojo.Environment;
import cs580.evolution.pojo.Genome;
import cs580.evolution.pojo.PopulationPair;

/**
 * @author Natalia Anpilova
 * @author Ashwin Hole
 * @author Divya Kondaviti
 * @author Ankith Raj
 * @author Manjusha Upadhye
 *
 */
public class CreatureEvolution {
	//constants for output file where we dump the randomly generated initial population
	private static final String CSV_SEPARATOR = ", ";
	private static final String OUT_FILENAME_BASE = "C:/evolution/init_population";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//TODO (REQUIRED - Natalia): add choice from input args: read population from file or generate random
		//TODO (optional - Natalia): verbose flag as additional input argument
		//TODO (optional - Natalia): better input arguments validation (can skip since we will know what values to plug in for demo)
		//TODO (REQUIRED - Natalia): save randomly generated population to a file so it's possible to re-use it later
		//TODO (REQUIRED - Natalia): direct output to log file in addition to system output
		//TODO (optional - Natalia): set environmental characteristics from input args, add whatever other input args can be handy
		//TODO (optional - Natalia): use some existing or new method in parent selection class to calculate prevalence of offspring - like calculate potential offspring count for winners
		
		//just 2 input args for now
		if (args.length > 1) {
			int populationSize;
			int generationsNumber = Integer.parseInt(args[1].replace("generations_number=", ""));
			
			/*
			 * initial population
			 */
			List<Genome> initPopulation = null;
			//random population generation of a given size
			if (args[0].contains("init_population_size")) {
				try {
					populationSize = Integer.parseInt(args[0].replace("init_population_size=", ""));
					if (populationSize <= 0) {
						System.err.println("Invalid population size. Please enter number greater than 0");
						System.exit(0);
					}
					initPopulation = generateRandomPopulation(populationSize);
					
				} catch (NumberFormatException e) {
					System.err.println("Invalid population size. Please enter integer value greater than 0");
					System.exit(0);
				}
				
			} else {
				//reading the population and its size from a file
				String inFilePath = args[0].replace("init_population_file=", "");
				File inFile = new File(inFilePath);
				
				if (!inFile.exists()) {
					System.err.println("File " + inFilePath + " does not exist. Please enther the correct full path to a file containing initial population data");
					System.exit(0);
				} else {
					PopulationPair popl;
					try {
						popl = readPopulationFromFile(inFile);
						populationSize = popl.getSize();
						initPopulation = popl.getPopul();
					} catch (FileNotFoundException e) {
						System.err.println("File " + inFilePath + " does not exist. Please enther the correct full path to a file containing initial population data");
						System.exit(0);
					}
				}
			}
				
			if (generationsNumber <= 0) {
				System.err.println("Invalid number of generations. Please use number greater than 0");
				System.exit(0);
			}
			
			/*
			 * initial livable environment
			 */
			Environment initEnvironment = new Environment();
			
			/*
			 * call genetic algorithm
			 */
			List<Genome> winners = geneticAlgorithm(initPopulation, initEnvironment, generationsNumber);
			
			if (winners.isEmpty())
				System.out.println("\n*** NO WINNER ***");
			else {
				System.out.println("\n*********** WINNER" + (winners.size() == 1 ? "" : "S") + " ***********");	
				int i = 1;
				for (Genome winner : winners) {
					System.out.println("\n" + (winners.size() == 1 ? "" : i+". ") + "Genome:");
					System.out.println(winner.toString());
					i++;
				}
			}
			
		} else {
			System.err.println("Invalid number of input arguments. Please use 2 argumens in one of two options:");
			System.err.println("1. For random initial population, use arguments \ninit_population_size=<population size>\ngenerations_number=<number of generations>");
			System.err.println("2. For reading initial population from a file, use arguments \ninit_population_size<population file>\ngenerations_number=<number of generations>");
		}
	}

	
	/**
	 * Main logic - genetic algorithm
	 * @param initPopulation
	 * @param initEnvironment
	 * @param generationsNumber number of generations
	 * @return winners' genomes
	 */
	public static List<Genome> geneticAlgorithm(List<Genome> initPopulation, Environment initEnvironment, int generationsNumber) {
		List<Genome> population = initPopulation;		//current whole population
		List<Genome> parentsPool = null;				//potential parents - population (with fitness levels) after discarding individuals with low fitness
		List<Genome> parents = null;					//actual parents list
		List<Genome> newPopulation = null;				//offspring
		Genome mom, dad, child;
		Environment environment = initEnvironment;
		EnviGen envGenerator = new EnviGen();
		Reproduce reproduce = new Reproduce();
		Mutation mutation = new Mutation();
		Selection selection = new Selection();
		List<Genome> winners;
		double tmp;	//to calculate log10 for generation number output
		
		System.out.println("Initial population size = " + population.size());
		System.out.println("Number of generations to create = " + generationsNumber);
		
		System.out.println("\nInitial environment:");
		System.out.println(environment.toString());
		System.out.println();
		
		//output most fit individuals from initial population
		winners = getWinners(population, environment);
		if (!winners.isEmpty()) {
			System.out.println("\n*** Fittest individual" + (winners.size() == 1 ? "" : "s") + " from initial population ***");	
			int i = 1;
			for (Genome winner : winners) {
				System.out.println("\n" + (winners.size() == 1 ? "" : i+". ") + "Genome:");
				System.out.println(winner.toString());
				i++;
			}
		}
		System.out.println();
		
		/*
		 * outer loop: one iteration = one generation
		 * stopping rule: required number of generations passed
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
			//System.out.println("** generation "+generationCount+" culled population size " + parentsPool.size());
			
			/*
			 * inner loop: generating offspring as new population
			 */
			//TODO (optional - Natalia): instead of population size of the upper limit, use food limit from current environment state: if no more food left, no more offspring produced
			for (int i = 0; i < population.size(); i++) {
				//if less than two potential parents, then offspring cannot be generated
				if (parentsPool.isEmpty() || parentsPool.size() < 2) {
					System.out.println("WARNING: Not enough individuals in the parents pool: actual number is " + parentsPool.size() + ", min allowed number is 2");
					System.out.println("Final population size = " + population.size());
					System.out.println("\nFinal environment:");
					System.out.println(environment.toString());
					
					//output the last generation number
					if (tmp != (int)tmp && generationCount != generationsNumber)
						System.out.println("Generation " + generationCount);
					
					if (parentsPool.size() < 2)
						System.out.println("The last survivor is the winner");
					
					return parentsPool;
				}
				
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
				child = reproduce.produceChild(mom, dad);
				mom.addOffspring(1);
				dad.addOffspring(1);
				
				//remove individuals with max possible number of offspring from parents pool
				parentsPool.removeIf(p -> (p.getOffspringCount() == Genome.MAX_OFFSPRING_COUNT));
				
				/*
				 * mutate
				 */
				//TODO (optional - Natalia): if Divya adds pollution in mutationProbability(), replace with mutationProbability(environment)
				if (mutation.mutationProbability() >= 0.99)
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
		winners = getWinners(population, environment);
		
		return winners;
	}
	
	/**
	 * Generates random population of the given size
	 * and writes this population to the CSV file
	 * @param size number of individuals in population
	 * @return population consisting of randomly generated genomes
	 */
	private static List<Genome> generateRandomPopulation(int size) {
		//generating random population
		List<Genome> randomPopulation = new ArrayList<Genome>();
		for (int i = 0; i < size; i++) {
			randomPopulation.add(Genome.generateRandomGenome());
		}
		
		//write generated population to a CSV file
		try {
			String outFileName = OUT_FILENAME_BASE + "1.csv";
			File outFile = new File(outFileName);
			//not overwriting existing file but creating a new one
			int j = 2;
			while (outFile.exists()) {
				outFileName = OUT_FILENAME_BASE + j + ".csv";
				outFile = new File(outFileName);
				j++;
			}
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFileName), "UTF-8"));
			StringBuffer oneLine = new StringBuffer();
			//header
			oneLine = new StringBuffer();
            oneLine.append("Eyes Number");
            oneLine.append(CSV_SEPARATOR);
            oneLine.append("Ears Number");
            oneLine.append(CSV_SEPARATOR);
            oneLine.append("Legs Number");
            oneLine.append(CSV_SEPARATOR);
            oneLine.append("Fins Number");
            oneLine.append(CSV_SEPARATOR);
            oneLine.append("Wings Number");
            oneLine.append(CSV_SEPARATOR);
            oneLine.append("Coat Thickness, cm");
            oneLine.append(CSV_SEPARATOR);
            oneLine.append("Weight, kg");
            oneLine.append(CSV_SEPARATOR);
            oneLine.append("Metabolism, kCal/day");
            oneLine.append(CSV_SEPARATOR);
            oneLine.append("Height, m");
            oneLine.append(CSV_SEPARATOR);
            oneLine.append("Cooperates with others?");
            oneLine.append(CSV_SEPARATOR);
            oneLine.append("Offspring Count");
            
            bw.write(oneLine.toString());
            bw.newLine();
            
			//data
	        for (Genome gen : randomPopulation) {
	           	oneLine = new StringBuffer();
	            oneLine.append(gen.getEyesNumber());
	            oneLine.append(CSV_SEPARATOR);
	            oneLine.append(gen.getEarsNumber());
	            oneLine.append(CSV_SEPARATOR);
	            oneLine.append(gen.getLegsNumber());
	            oneLine.append(CSV_SEPARATOR);
	            oneLine.append(gen.getFinsNumber());
	            oneLine.append(CSV_SEPARATOR);
	            oneLine.append(gen.getWingsNumber());
	            oneLine.append(CSV_SEPARATOR);
	            oneLine.append(gen.getCoatThickness());
	            oneLine.append(CSV_SEPARATOR);
	            oneLine.append(gen.getWeight());
	            oneLine.append(CSV_SEPARATOR);
	            oneLine.append(gen.getMetabolism());
	            oneLine.append(CSV_SEPARATOR);
	            oneLine.append(gen.getHeight());
	            oneLine.append(CSV_SEPARATOR);
	            oneLine.append(gen.isCooperationFlag());
	            oneLine.append(CSV_SEPARATOR);
	            oneLine.append(gen.getOffspringCount());
	            
	            bw.write(oneLine.toString());
	            bw.newLine();
	        }
	        bw.flush();
	        bw.close();
	        System.out.println("Initial population saved in file " + outFileName);
	        
	    //if failed to write to a file - proceed anyway
		} catch (IOException e) {
			System.out.println("WARNING: unable to write generated population to CSV file. Writing to the file skipped.\n");
		}
		
		return randomPopulation;
	}


	/**
	 * Reads population from the CSV file
	 * @param inFile input file containing the list of genomes
	 * @return instance containing population and its size
	 * @throws FileNotFoundException
	 */
	private static PopulationPair readPopulationFromFile(File inFile) throws FileNotFoundException {
		List<Genome> inPopulation = new ArrayList<Genome>();
		Genome gen;
		int inPopulationSize = -1;	//to skip the header line
		
		Scanner scanner = new Scanner(inFile);
        scanner.useDelimiter(CSV_SEPARATOR);
        
        while (scanner.hasNextLine()) {
            System.out.print(scanner.next()+"|");///
            //skip the header line
            if (inPopulationSize >= 0) {
	            gen = new Genome();///TODO -Natalia
	            inPopulation.add(gen);
            }
            inPopulationSize ++;
        }
        scanner.close();
        
        PopulationPair popl = new PopulationPair(inPopulationSize, inPopulation);
        
		return popl;
	}
	
	/**
	 * Determines the winner individuals - the genomes with the highest fitness level
	 * @param population
	 * @param envmt environment
	 * @return list of individuals with the highest fitness level
	 */
	@SuppressWarnings("unchecked")
	private static List<Genome> getWinners(List<Genome> population, Environment envmt) {
		if (population.isEmpty() || population == null)
			return population;
		
		//calculate fitness levels for population
		Fitness fit = new Fitness();
		population = fit.calculatePopulationFitness(population, envmt);
		List<Genome> winners = new ArrayList<Genome>();
		
		//sort population by fitness level in asc order
		Collections.sort(population);
		
		//obtaining the top fitness level value - fitness level of the last individual in the sorted list
		int topFitness = population.get(population.size()-1).getFitness();
				
		for (Genome gen : population) {
			if (gen.getFitness() == topFitness)
				winners.add(gen);
		}
		
		//get rid of duplicate genomes from the winners list so we have distinct genomes in output
		winners = new ArrayList<Genome> (new HashSet<Genome>(winners));
		return winners;
	}

}
