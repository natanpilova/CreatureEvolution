/**
 * Class for Parent Selection
 */
package cs580.evolution.function;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import cs580.evolution.pojo.Environment;
import cs580.evolution.pojo.Genome;

/**
 * 
 * @author Manjusha Upadhye
 * edited by Natalia Anpilova
 */

public class Selection {
	
	private Fitness fit = new Fitness();
	
	/**
	 * Selects parents from culled population
	 * @param popMap parents pool: culled population with fitness levels
	 * @param env current environment
	 * @return list of two parents: first mom, second dad
	 */
	public List<Genome> getParents(TreeMap<Integer, Genome> popMap, Environment envmt) {
		//TODO after progress check:
		// 1) increase the probability of individuals with higher fitness to be selected
		// 2) account for the max offspring number: individual with max possible offspring count cannot be selected anymore
		
		//to store 2 parents: first mom, second dad
		List<Genome> parents =  new ArrayList<Genome>();
		
		TreeMap<Integer, Genome> popuMap = new TreeMap<Integer, Genome>(popMap);
		Genome mom, dad;
		
		//if just two individuals in the parents pool then no need to select parents, just use these two
		if (popuMap.size() == 2) {
			//parents = (List<Genome>) popuMap.values();//FIX
			mom = popuMap.pollFirstEntry().getValue();
			dad = popuMap.pollLastEntry().getValue();
		} else {
			//selecting parents randomly from the culled population
			Random rand = new Random();
			Integer lastKey = popuMap.lastKey();
			Integer randomKey = rand.nextInt(lastKey);
			while (!popuMap.containsKey(randomKey)) {
				randomKey = rand.nextInt(lastKey);
			}
			mom = popuMap.remove(randomKey);	//get and remove mom from map
			
			lastKey = popuMap.lastKey();		//need to get new last key to avoid having null value for dad
			randomKey = rand.nextInt(lastKey);
			while (!popuMap.containsKey(randomKey)) {
				randomKey = rand.nextInt(lastKey);
			}
			dad = popuMap.get(randomKey);	//get dad
			
		}
		
		//added mom and dad to parents list
		parents.add(mom);
		parents.add(dad);
		
		return parents;
	}
	
	/**
	 * Culls population: discards 50% least fit individuals from it
	 * @param population original population
	 * @param envmt current environment
	 * @return culled population (50% of original population size) - 
	 * tree map where key is fitness level, value is genome; map is sorted by fitness level in ascending order
	 */
	public TreeMap<Integer, Genome> cullPopulation(List<Genome> population, Environment envmt) {
		//calculate fitness levels for population
		TreeMap<Integer, Genome> popuMap = fit.calculatePopulationFitness(population, envmt);
		
		//FIX
		//keep roughly 50% most fit individuals
		Integer[] arrInt = popuMap.keySet().toArray(new Integer[0]);
		int halfSize = arrInt.length/2;
		Integer fromKey = arrInt[halfSize];
		
		//get greater half
		TreeMap<Integer, Genome> culledMap = new TreeMap<Integer, Genome>(popuMap.tailMap(fromKey, false));

		return culledMap;
	}
		
}
