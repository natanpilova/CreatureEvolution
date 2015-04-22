package cs580.evolution.pojo;

import java.util.List;

/**
 * @author Natalia Anpilova
 * Helper class to store population and its size together
 */
public class PopulationPair {
    private int size;
	private List<AntForager> popul;

    public PopulationPair(int size, List<AntForager> popul) {
        this.size = size;
        this.popul = popul;
    }
    
    /**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @return the popul
	 */
	public List<AntForager> getPopul() {
		return popul;
	}
}
