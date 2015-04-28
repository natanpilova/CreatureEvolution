/**
 * Class for fitness function
 */
package cs580.evolution.function;

import java.util.List;
import java.util.Random;

import cs580.evolution.pojo.AntForager;
import cs580.evolution.pojo.Environment;

/**
 * @author Natalia Anpilova
 *
 */
public class Fitness {

	/**
	 * fitness function
	 * numbers are from Wikipedia,
	 * http://www.theincredibleant.com/ant-how/how-much-do-ants-eat, and
	 * http://bioteaching.com/cool-new-paper-ants-pheromones-and-temperature/
	 * Min possible level is 0
	 * Max possible level is <TBD>
	 * @param ind
	 * @param env current environment
	 * @return fitness level of individual - amount of food surplus ant get for its life, in grams
	 */
	public double getFitnessLevel(AntForager ind, Environment env) {
		int hibernateTemperature = 15;	//ants do not go foraging if it is below 15C outside
		if (env.getTemperature() <= hibernateTemperature)
			return 0;
		
		int feromoneLostTemperature = 40;				//if the temperature is 40C and above, the feromones lose its power
		int yearsAlive = 2; 							//work ants live 1 to 3 years - makes 2 the average
		int days = yearsAlive*365;						//how many days forager went for food - every day in subtropical climate
		int tripsPerDay = 30*env.getLightLevel()/12;	//30 trips per day if the day is 12 hrs long; adjusting proportionally for the current day length
		
		double fitnessLevel = 0;
		double foodFound = 0;
		double foodConsumed = 0;
		
		foodFound = days*tripsPerDay*ind.getWeight()*20;	//ant forager can carry on average 20 times of its weight
		
		/*
		 * adjust for sensory cells count: if average or above then able to smell all the trails and find all the food,
		 * if less then smell is worsen therefore less food can be found
		 */
		double senseCoeff = Math.min(100, ind.getAntennaeSensors()*100/((AntForager.MIN_SENSORS+AntForager.MAX_SENSORS)/2));
		foodFound = foodFound*senseCoeff/100;
		
		/*
		 * if feromones do not work, there is a random chance ant will find food
		 */
		if (env.getTemperature() >= feromoneLostTemperature) {
			Random rand = new Random(); 
			foodFound = foodFound*rand.nextDouble();
		} else
			/*
			 * the more ant marks trails with feromones, the easier to find food;
			 * but still can smell others' marks so will be at least half of max possible food
			 */
			foodFound = (foodFound + foodFound*ind.getFeromoneIntensity()/100)/2;
		
		/*
		 * if there are predators, larger jaws win
		 */
		if (env.getpredatorLevel() > Environment.MIN_PREDATOR_LEVEL) {
			double potLossToPredators = foodFound*env.getpredatorLevel()/100;							//potential lost of found food to predators
			double savedFromPredators = potLossToPredators*(ind.getJawSize()*100/AntForager.MAX_JAW_SIZE)/100;	//food saved from predators using jaws
			foodFound = foodFound - potLossToPredators + savedFromPredators;
		}
		
		/*
		 * adjust for food available in environment
		 */
		foodFound = foodFound*env.getfoodLevel()/100;
		
		foodConsumed = days*ind.getWeight()*0.28;	//on average forager ant eats 28% of his bodyweight of food per day
		
		fitnessLevel = (foodFound - foodConsumed)/1000;	//calculate the food surplus and converting to grams from milligrams
		return Math.max(0, Math.round(fitnessLevel*10000)/10000.00);
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
