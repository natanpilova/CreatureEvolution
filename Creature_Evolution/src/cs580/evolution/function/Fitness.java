/**
 * Class for fitness function
 */
package cs580.evolution.function;

import cs580.evolution.pojo.Environment;
import cs580.evolution.pojo.Genome;

/**
 * @author Natalia Anpilova
 *
 */
public class Fitness {

	/**
	 * fitness function
	 * @param ind
	 * @param env current environment
	 * @return fitness level of individual
	 */
	public int getFitnessLevel(Genome ind, Environment env) {
		double fitnessLevel = 0;
		//TODO refine the calculations based on combination of environmental parameters
		
		//if there's light around, more eyes the better
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
			fitnessLevel += ind.getCoatThickness();
		
		//natural log from weight plus 1 adds up to fitness level - the bigger the stronger
		fitnessLevel += Math.log1p(ind.getWeight());
		
		//natural log from height plus 1 adds up to fitness level - the taller the better
		fitnessLevel += Math.log1p(ind.getHeight());
		
		//the less foods individual needs, the better
		fitnessLevel -= Math.log1p(ind.getMetabolism());
		
		//if cooperates then it's a big plus
		if (ind.isCooperationFlag())
			fitnessLevel += 10;
		
		return (int) fitnessLevel;
	}
}
