/**
 * Class for Parent Selection
 */
package cs580.evolution.function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import cs580.evolution.pojo.Environment;
import cs580.evolution.pojo.Genome;

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
	public List<Genome> getParents(List<Genome> parentsPool, Environment envmt) {
		//TODO (optional - Natalia): increase the probability of individuals with higher fitness to be selected
		
		//to store 2 parents: first mom, second dad
		List<Genome> parents =  new ArrayList<Genome>();
		Genome mom, dad;

		//if just two individuals in the parents pool then no need to select parents, just use these two
		if (parentsPool.size() == 2) {
			mom = parentsPool.get(0);
			dad = parentsPool.get(1);
		} else {
			//selecting parents randomly from the culled population
			Random rand = new Random();
			int index = rand.nextInt(parentsPool.size());
			mom = parentsPool.remove(index);	//remove reference to mom from parents pool so mom cannot be picked again as a dad
			
			index = rand.nextInt(parentsPool.size());
			dad = parentsPool.get(index);
		}
		
		//added mom and dad to parents list
		parents.add(mom);
		parents.add(dad);
		//System.out.println("Mom's offspring count BEFORE = "+mom.getOffspringCount());///
		//System.out.println("Dad's offspring count BEFORE = "+dad.getOffspringCount());
		
		return parents;
	}
	
	/**
	 * Culls population: discards ~50% least fit individuals from it
	 * @param population original population
	 * @param envmt current environment
	 * @return culled population (roughly half of original population size) 
	 */
	@SuppressWarnings("unchecked")
	public List<Genome> cullPopulation(List<Genome> population, Environment envmt) {
		//calculate fitness levels for original population
		List<Genome> origPopulation = new ArrayList<Genome>(population);
		origPopulation = fit.calculatePopulationFitness(origPopulation, envmt);
		
		//if two or less individuals in population, there's no culling
		if (origPopulation.isEmpty() || origPopulation.size() <= 2)
			return origPopulation;
		
		List<Genome> culledPopulation = new ArrayList<Genome>();	//to store fitter individuals
		
		//sort original population by fitness level in asc order
		Collections.sort(origPopulation);
		
		//dump fitness levels into array to calculate the fitness level threshold (median)
		Integer[] arrInt = new Integer[origPopulation.size()];
		int i = 0;
		for (Genome gen : origPopulation) {
			//System.out.println("fitness: "+gen.getFitness());///
			arrInt[i] = gen.getFitness();
			i++;
		}
		
		//calculate fitness level threshold (median) - all individuals below this threshold will be discarded from parents pool
		int half = (arrInt.length + 1)/2;
		int fitTheshold = arrInt[half-1];
		//System.out.println("MEDIAN: "+fitTheshold);///
		
		//populating the parents pool with fit enough individuals
		for (Genome gen : origPopulation) {
			if (gen.getFitness() >= fitTheshold)
				culledPopulation.add(gen);
		}

		return culledPopulation;
	}
		
}
