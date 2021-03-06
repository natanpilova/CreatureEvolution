/**
 * Class for Parent Selection
 */
package cs580.evolution.function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import cs580.evolution.pojo.AntForager;
import cs580.evolution.pojo.Environment;

/**
 * 
 * @author Manjusha Upadhye, Natalia Anpilova
 */

public class Selection {
	
	private Fitness fit = new Fitness();
	
	/**
	 * Selects parents from culled population
	 * @param parentsPool parents pool: culled population with fitness levels
	 * @param env current environment
	 * @return list of two parents: first mom, second dad
	 */
	public List<AntForager> getParents(List<AntForager> parentsPool, Environment envmt) {
		//to store 2 parents: first mom, second dad
		List<AntForager> parents =  new ArrayList<AntForager>();
		AntForager mom, dad;

		//if just two individuals in the parents pool then no need to select parents, just use these two
		if (parentsPool.size() == 2) {
			mom = parentsPool.get(0);
			dad = parentsPool.get(1);
		} else {
			//selecting parents randomly from the culled population
			Random rand = new Random();
			int index = rand.nextInt(parentsPool.size());
			//temporary remove reference to mom from parents pool so mom cannot be picked again as a dad
			mom = parentsPool.remove(index);
			
			index = rand.nextInt(parentsPool.size());
			dad = parentsPool.get(index);
			
			//add mom back to parents pool so it can be selected for next reproduction
			parentsPool.add(mom);
		}
		
		//added mom and dad to parents list
		parents.add(mom);
		parents.add(dad);
		
		return parents;
	}
	
	/**
	 * Culls population: discards ~50% least fit individuals from it
	 * @param population original population
	 * @param envmt current environment
	 * @return culled population (roughly half of original population size) 
	 */
	@SuppressWarnings("unchecked")
	public List<AntForager> cullPopulation(List<AntForager> population, Environment envmt) {
		//calculate fitness levels for original population
		List<AntForager> origPopulation = new ArrayList<AntForager>(population);
		origPopulation = fit.calculatePopulationFitness(origPopulation, envmt);
		
		//if two or less individuals in population, there's no culling
		if (origPopulation.isEmpty() || origPopulation.size() <= 2)
			return origPopulation;
		
		List<AntForager> culledPopulation = new ArrayList<AntForager>();	//to store fitter individuals
		
		//sort original population by fitness level in asc order
		Collections.sort(origPopulation);
		
		//dump fitness levels into array to calculate the fitness level threshold (median)
		Double[] arr = new Double[origPopulation.size()];
		int i = 0;
		for (AntForager gen : origPopulation) {
			//System.out.println("fitness: "+gen.getFitness());///
			arr[i] = gen.getFoodSurplus();
			i++;
		}
		
		//calculate fitness level threshold (median) - all individuals below this threshold will be discarded from parents pool
		int half = (arr.length + 1)/2;
		double fitTheshold = arr[half-1];
		//System.out.println("MEDIAN: "+fitTheshold);///
		
		//populating the parents pool with fit enough individuals
		for (AntForager gen : origPopulation) {
			if (gen.getFoodSurplus() >= fitTheshold)
				culledPopulation.add(gen);
		}

		return culledPopulation;
	}
		
}
