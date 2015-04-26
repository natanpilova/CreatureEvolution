package cs580.evolution.function;

import java.util.Random;

import cs580.evolution.pojo.AntForager;
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
	
	public static AntForager mutate(AntForager individual)
	{
		Random mutationGenerator = new Random();
		int mutationSelector = mutationGenerator.nextInt(5);
		GenomeEnum selectedType = GenomeEnum.values()[mutationSelector];
		switch (selectedType) {
		
		case SIZE :
			double size = individual.getSize();
			size = (size + 1 > AntForager.MAX_SIZE) ? size-1: size+1;
			individual.setSize(size);
			break;
		case WEIGHT :
			double weight= individual.getWeight();
			weight = (weight + 1 > AntForager.MAX_WEIGHT) ? weight-1: weight+1;
			individual.setWeight(weight);
			break;
		case JAWSIZE:
			double jawsize= individual.getJawSize();
			jawsize=(jawsize + 0.2)>AntForager.MAX_JAW_SIZE ? jawsize-0.2 : jawsize+0.2;
			individual.setJawSize(jawsize);
			break;
		case FERONOMEINTENSITY:
			double feronomeintensity= individual.getFeromoneIntensity();
			feronomeintensity=(feronomeintensity +10)> AntForager.MAX_FEROMONE_INTENSITY?feronomeintensity-10: feronomeintensity+10;
			individual.setFeromoneIntensity(feronomeintensity);
			break;
		case ANTENNAESENSORS:
			int antennaeSensors= individual.getAntennaeSensors();
			antennaeSensors=(antennaeSensors+100)> AntForager.MAX_SENSORS ? antennaeSensors-100: antennaeSensors+100;
			individual.setAntennaeSensors(antennaeSensors);
			break;
		}	
		
		return individual;
	}

}
