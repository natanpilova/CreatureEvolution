/**
 * Class for Parent Selection
 */
package cs580.evolution.function;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import cs580.evolution.pojo.Environment;
import cs580.evolution.pojo.Genome;

/**
 * 
 * @author Manjusha Upadhye
 *
 */

public class Selection {
	
	private Fitness fit = new Fitness();
	
	/**
	 * Selects parents from culled population
	 * @param population culled population
	 * @param env current environment
	 * @return list of two parents: first mom, second dad
	 */
	public List<Genome> getParents(List<Genome> population, Environment envmt) {
		//to store 2 parents: first mom, second dad
		ArrayList<Genome> parents = new ArrayList<Genome>();
		
		TreeMap<Integer, Genome> popuMap = new TreeMap<Integer, Genome>();	//to store calculated fitness levels for culled population
		int fitLevel;
		
		//calculate fitness for population and add to the map
		for (Genome gen : population) {
			fitLevel = fit.getFitnessLevel(gen, envmt);
			popuMap.put(fitLevel, gen);
		}
		
		//TODO after progress check:
		// 1) optimize for speed and resources
		// 2) increase the probability of individuals with higher fitness to be selected
		// 3) account for the max offspring number: individual with max possible offspring count cannot be selected anymore
		
		//selecting parents randomly from the culled population
		Random rand = new Random();
		Genome mom, dad;
		Integer lastKey = popuMap.lastKey();
		Integer randomKey = rand.nextInt(lastKey);
		while (!popuMap.containsKey(randomKey)) {
			randomKey = rand.nextInt(lastKey);
		}
		mom = popuMap.remove(randomKey);	//get and remove mom from map
		parents.add(mom);					//added mom to parents list
		
		lastKey = popuMap.lastKey();		//need to get new last key to avoid having null value for dad
		randomKey = rand.nextInt(lastKey);
		while (!popuMap.containsKey(randomKey)) {
			randomKey = rand.nextInt(lastKey);
		}
		dad = popuMap.get(randomKey);	//get dad
		parents.add(dad);				//added dad to parents list
		
		return parents;
	}
	
	/**
	 * Culls population: discards 50% least fit individuals from it
	 * @param population original population
	 * @param envmt current environment
	 * @return culled population (50% of original population size)
	 */
	public List<Genome> cullPopulation(List<Genome> population, Environment envmt) {
		TreeMap<Integer, Genome> popuMap = new TreeMap<Integer, Genome>();	//to store calculated fitness levels for population
		List<Genome> culledPopulation = new ArrayList<Genome>();			//to store 50% most fit individuals
		int fitLevel;
		
		//calculate fitness for population and add to the map; map by default will be sorted by fitness level in ascending order
		for (Genome gen : population) {
			fitLevel = fit.getFitnessLevel(gen, envmt);
			popuMap.put(fitLevel, gen);
		}
		
		//keep 50% most fit individuals
		int halfSize = popuMap.size()/2;
		int i = 1;
		for (Entry<Integer, Genome> entry : popuMap.entrySet()) {
			if (i > halfSize)
				culledPopulation.add(entry.getValue());
			i++;
		}
		return culledPopulation;
	}
		
}
