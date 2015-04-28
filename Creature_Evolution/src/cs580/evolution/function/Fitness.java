/**
 * Class for fitness function
 */
package cs580.evolution.function;

import java.util.List;

import cs580.evolution.pojo.AntForager;
import cs580.evolution.pojo.Environment;

/**
 * @author Natalia Anpilova
 *
 */
public class Fitness {

	/**
	 * fitness function
	 * Min possible level is 0
	 * Max possible level is <TBD>
	 * @param ind
	 * @param env current environment
	 * @return fitness level of individual
	 */
	public double getFitnessLevel(AntForager ind, Environment env) {
		double fitnessLevel = 0;
		double foodFound = 0;
		double foodConsumed = 0;
		int yearsAlive = 2; //work ants live 1 to 3 years - makes 2 the average
		int days = 30; //how many days forager went for food - TODO research and add real number
		//TODO (Natalia): finish and refine the calculations based on combination of environmental parameters
		
		foodFound = days*ind.getWeight()*20/1000.00;	//ant forager can carry on average 20 times of its weight
		
		fitnessLevel = foodFound - foodConsumed;
		return (Math.round(fitnessLevel*10000)/10000.00);
		
/*		//if there's light around, more eyes the better
		if (env.getLightLevel() > Environment.MIN_LIGHT_LEVEL)
			fitnessLevel += ind.getEyesNumber();
		
		//ears are always good to have
		fitnessLevel += ind.getEarsNumber();
		
		//if air not water environment then legs are good to have
		if (env.getHumidityLevel() < Environment.MAX_HUMIDITY_LEVEL) {
			fitnessLevel += ind.getLegsNumber();
			//if air is not too humid then wings will work
			if (env.getHumidityLevel() < 70)
				fitnessLevel += ind.getWingsNumber();
		} else
			//water environment - only fins matter
			fitnessLevel += ind.getFinsNumber();
		
		//if chilly or even cold, coat thickness matters
		if (env.getTemperature() < 10)
			fitnessLevel += Math.log1p(ind.getCoatThickness());
		
		//the bigger the stronger
		fitnessLevel += Math.log1p(ind.getWeight());
		
		//the taller the better
		fitnessLevel += Math.log1p(ind.getHeight());
		
		//the less foods individual needs, the better
		fitnessLevel -= Math.log1p(ind.getMetabolism());
		
		//if cooperates then it's a big plus
		if (ind.isCooperationFlag())
			fitnessLevel += 10;
		
		//high pollution lowers the fitness level
		if (env.getPollutionLevel() > Environment.POLLUTION_MUTATION_THRESHOLD)
			fitnessLevel -= Math.log1p(env.getPollutionLevel());*/
		
	}
	
	/**
	 * Calculates fitness level for all individuals in population
	 * @param population
	 * @param envmt environment
	 * @return same population with calculated fitness levels
	 */
	public List<AntForager> calculatePopulationFitness(List<AntForager> population, Environment envmt) {
		double fitLevel;

		for (AntForager ant : population) {
			fitLevel = getFitnessLevel(ant, envmt);
			ant.setFoodSurplus(fitLevel);
		}
		
		return population;
	}
}
