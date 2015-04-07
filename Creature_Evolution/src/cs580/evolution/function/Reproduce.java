/**
 * Class for Child function
 */
package cs580.evolution.function;
import java.util.Random;

import cs580.evolution.pojo.Genome;

/**
 * @author Ashwin Hole
 *
 */
public class Reproduce {

	/**
	 * Reproduce function
	 * @param mom
	 * @param dad
	 * @return kid
	 */
	public Genome produceChild(Genome mom, Genome dad) {
		Random r = new Random();
		
		
		int[] momarray = new int[8];
		int[] dadarray = new int[8];
		int[] childarray = new int[8];
		double h;
		boolean coop;
		
		momarray[0] = mom.getEyesNumber();
		momarray[1] = mom.getEarsNumber();
		momarray[2] = mom.getLegsNumber();
		momarray[3] = mom.getFinsNumber();
		momarray[4] = mom.getWingsNumber();
		momarray[5] = mom.getCoatThickness();
		momarray[6] = mom.getWeight();
		momarray[7] = mom.getMetabolism();
		
		dadarray[0] = dad.getEyesNumber();
		dadarray[1] = dad.getEarsNumber();
		dadarray[2] = dad.getLegsNumber();
		dadarray[3] = dad.getFinsNumber();
		dadarray[4] = dad.getWingsNumber();
		dadarray[5] = dad.getCoatThickness();
		dadarray[6] = dad.getWeight();
		dadarray[7] = dad.getMetabolism();
		
		Genome kid = null;
		do
		{
		int crossover = r.nextInt(8);
		
		if(crossover > 0)
		for(int i=0;i<crossover;i++)
		childarray[i] = momarray[i];
		
		if(crossover < 8)
		for(int i=crossover;i<8;i++)
		childarray[i] = dadarray[i];
		
		coop = dad.isCooperationFlag();
		
		
		if(crossover == 8)
		h = mom.getHeight();	
		else
		h = dad.getHeight();
		
		kid = new Genome(childarray[0], childarray[1], childarray[2], childarray[3], childarray[4], childarray[5], childarray[6], childarray[7], h, coop);
		
			
		}while(!GenomeCheck(kid));
		
		return kid;
	}
	
	/**
	 * Check if the resulting child has realistic genome
	 * @param kid
	 * @return true if pass
	 */
	public static boolean GenomeCheck(Genome kid){
		
		if(kid.getHeight() <= 1 && kid.getWeight() <= 150 && kid.getMetabolism() <= 500)
		return true;
		else if(kid.getHeight()>1 && kid.getHeight()<=10 && kid.getWeight()>150 && kid.getWeight()<=300 && kid.getMetabolism()>500 && kid.getMetabolism()<=1500)
		return true;
		else if(kid.getHeight()>10 && kid.getHeight()<=30 && kid.getWeight()>300 && kid.getWeight()<=1500 && kid.getMetabolism()>1500 && kid.getMetabolism()<=5000)
		return true;
		else if(kid.getHeight()>30 && kid.getHeight()<=60 && kid.getWeight()>1500 && kid.getWeight()<=5000 && kid.getMetabolism()>5000 && kid.getMetabolism()<=10000)
		return true;
		else if(kid.getHeight()>60 && kid.getHeight()<=80 && kid.getWeight()>5000 && kid.getWeight()<=7500 && kid.getMetabolism()>10000 && kid.getMetabolism()<=100000)
		return true;
		else if(kid.getHeight()>80 && kid.getWeight()>7500 && kid.getMetabolism()>100000)
		return true;
		else return false;
	}
}
