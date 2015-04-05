package cs580.evolution.function;

import java.util.Random;

import cs580.evolution.pojo.Genome;


/**
 * @author Divya Kondaviti
 *
 */
public class Mutation {
	//TODO (optional - Divya): add some additional small value if current environment's pollution exceeds mutation threshold, like:
	//if (env.getPollutionLevel => Envoronment.POLLUTION_MUTATION_THRESHOLD {randomNumber += env.getPollutionLevel/100}
	//Remember that the result should be less than 1
	//Also, mutationProbability() will need to have current Environment as an argument
	
	public double mutationProbability()
	{
		Random rand = new Random(); 
		double randomNumber = rand.nextDouble(); 
		return randomNumber;	
	}
	
	public static Genome mutate(Genome individual)
	{
		Random mutationGenerator = new Random();
		int mutationSelector = mutationGenerator.nextInt(10);
		GenomeEnum selectedType = GenomeEnum.values()[mutationSelector];
		switch (selectedType) {
		
		case EYES :
			int eyesNumber = individual.getEyesNumber();
			eyesNumber = (eyesNumber + 1 > Genome.MAX_EYES_NUMBER) ? eyesNumber-1: eyesNumber+1;
			individual.setEyesNumber(eyesNumber);
			break;
		case EARS :
			int earsNumber= individual.getEarsNumber();
			earsNumber = (earsNumber + 1 > Genome.MAX_EARS_NUMBER) ? earsNumber-1: earsNumber+1;
			individual.setEarsNumber(earsNumber);
			break;
		case LEGS:
			int legsNumber= individual.getLegsNumber();
			legsNumber=(legsNumber + 1)>Genome.MAX_LEGS_NUMBER ? legsNumber-1 : legsNumber+1;
			individual.setLegsNumber(legsNumber);
			break;
		case FINS:
			int finsNumber= individual.getFinsNumber();
			finsNumber=(finsNumber +1)> Genome.MAX_FINS_NUMBER?finsNumber-1: finsNumber+1;
			individual.setFinsNumber(finsNumber);
			break;
		case WINGS:
			int wingsNumber= individual.getFinsNumber();
			wingsNumber=(wingsNumber+1)> Genome.MAX_WINGS_NUMBER ? wingsNumber-1: wingsNumber+1;
			individual.setWingsNumber(wingsNumber);
			break;
		case THICKNESS:
			int thickness= individual.getCoatThickness();
			thickness=(thickness+10)> Genome.MAX_COAT_THICKNESS? thickness-1: thickness+1;
			individual.setCoatThickness(thickness);
			break;
		case HEIGHT:
			double height= individual.getHeight();
			height=(height+10)> Genome.MAX_HEIGHT? height-10: height +10;
			individual.setHeight((int) height);
			break;
		case WEIGHT:
			int weight= individual.getWeight();
			weight= (weight+10)> Genome.MAX_WEIGHT? weight-10 :weight+10;
			individual.setWeight(weight);
			break;
		case METABOLISM:
			int metabolism= individual.getMetabolism();
			metabolism= (metabolism+100) > Genome.MAX_METABOLISM? metabolism-100: metabolism+100;
			individual.setMetabolism(metabolism);
			break;
		case COOPERATIONFLAG:
			Random random = new Random();
		    boolean cooperationFlag_random=random.nextBoolean();
		    individual.setCooperationFlag(cooperationFlag_random);
		    break;
		
		}	
		return individual;
		
	}

}
