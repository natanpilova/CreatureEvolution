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
		int crossover = r.nextInt(8);
		
		int[] momarray = new int[8];
		int[] dadarray = new int[8];
		int[] childarray = new int[8];
		double h;
		boolean coop;
		//int offspring;
		/*
		 * Child always have default offspring count set to zero - he simply did not reproduce yet.
		 * We add offspring count in main class, not here. We may bring it back or change later though - will discuss. -Natalia
		 */
		
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
		
		//Please make sure at least one gene is inherited from mom/dad: if all genes inherited from only one parent then we just have a clone. -Natalia
		if(crossover > 0)
		for(int i=0;i<crossover;i++)
		childarray[i] = momarray[i];
		
		if(crossover < 0)
		for(int i=crossover;i<8;i++)
		childarray[i] = dadarray[i];
		
		//offspring = mom.getOffspringCount();
		//So cooperation flag is always inherited from dad? That can work. -Natalia
		coop = dad.isCooperationFlag();
		
		//Why 4? Shouldn't it be 6 since height is gene #7? -Natalia
		if(crossover > 4 )
		h = dad.getHeight();	//here and below - changed getHeight to getHeight() -Natalia
		else
		h = mom.getHeight();
		
		Genome kid = new Genome(childarray[0], childarray[1], childarray[2], childarray[3], childarray[4], childarray[5], h, childarray[6], childarray[7], coop);
		//kid.setOffspringCount(offspring);
		
		
		return kid;
	}
}
