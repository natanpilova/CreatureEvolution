/**
 * CS580 Spring 2015 Team 5 Term project
 */
package cs580.evolution.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.NumberFormatter;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import cs580.evolution.function.EnviGen;
import cs580.evolution.function.Fitness;
import cs580.evolution.function.Mutation;
import cs580.evolution.function.Reproduce;
import cs580.evolution.function.Selection;
import cs580.evolution.pojo.AntForager;
import cs580.evolution.pojo.Environment;
import cs580.evolution.pojo.PopulationPair;

/**
 * @author Natalia Anpilova
 * @author Ashwin Hole
 * @author Divya Kondaviti
 * @author Ankith Raj
 * @author Manjusha Upadhye
 *
 */
public class CreatureEvolution extends JPanel implements ActionListener {
	private static final long serialVersionUID = -6211365806865371892L;
	
	//colors and fonts
	private static final Color btnBackground = new Color(54, 88, 19);	//dark green for buttons background and labels foreground
	private static final  Color btnForeground = new Color(239, 235, 214);	//pale yellow for buttons foreground
	private static final Color inBackground = new Color(253, 253, 253);	//light pale yellow for inputs background
	private static final Font font = new Font("Courier New", Font.BOLD, 16);
	private static final Font fontBig = new Font("Courier New", Font.BOLD, 26);
	private static final Font fontSmall = new Font("Courier New", Font.BOLD, 12);
	
	//constants for output file where we dump the randomly generated initial population
	private static final String CSV_SEPARATOR = ", ";
	private static final String OUT_FILENAME_BASE = "C:/evolution/init_population";
	//ant image source - copy ant.png from img subfolder to this path
	private static final String IMG_FILENAME = "C:/evolution/img/ant.png";
	
	//log to C:/evolution/log/evolution.log file - configuration is in log4j.xml
	final static Logger log = Logger.getLogger(CreatureEvolution.class);

	private static int winWidth;
	private static int winHeight;
	
	private JLabel lbInitPopul;
	private JLabel lbInitPopulSize;
	private JLabel lbGenNumber;
	private JLabel infoMsg;
	private JLabel infoGenerationCount;
	private JLabel infoPopulationSize;
	private JLabel infoFittestAnt;
	private JLabel infoEnvironment;
	private JFormattedTextField inInitPopulSize;
	private JFormattedTextField inGenNumber;
	private JTextField inInitPopulFile;
	private JRadioButton rbtnRandomPopul;
	private JRadioButton rbtnFilePopul;
	private ButtonGroup group;
	private JButton btnOpenFile;
	private JButton btnInit;  
	private JButton btnStart;
	
	private BufferedImage antPicture;
	private JLabel picLabel;
	
	private JFreeChart barChart;
	private ChartPanel chartPanel;
	
	private int populationSize;
	private int generationsNumber;
	private List<AntForager> initPopulation = null;
	private Environment initEnvironment = null;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    JFrame window = new JFrame("Best Ant Forager | CS580 Spring 2015 Term Project | Team 5");
	    CreatureEvolution content = new CreatureEvolution();
	    Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
	    window.setContentPane(content);
	    window.setSize((int)(screensize.width*0.65), (int)(screensize.height*0.75));
	    //window.pack();
	    window.setLocation((screensize.width - window.getWidth())/2, (screensize.height - window.getHeight())/2);
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    //window.setBackground(new Color(184, 206, 162)); //pale green
	    window.setResizable(false);  
	    window.setVisible(true);
	    winWidth = window.getWidth();
	    winHeight = window.getHeight();
	    //log.debug("window width: "+window.getWidth()+", window height: "+window.getHeight());
	}

	
	/**
	 * Constructor
	 */
	public CreatureEvolution() {
		setLayout(null);	//custom layout
	     
	    /*
	     * Create the components
	     */
	    //radio buttons with label
		lbInitPopul = new JLabel("Initial Population:",JLabel.LEFT);
		lbInitPopul.setFont(font);
		lbInitPopul.setForeground(btnBackground);
		
	    rbtnRandomPopul = new JRadioButton("generate random");
	    rbtnRandomPopul.setMnemonic(KeyEvent.VK_R);
	    rbtnRandomPopul.setActionCommand("random");
	    rbtnRandomPopul.setSelected(true);

	    lbInitPopulSize = new JLabel("of size:",JLabel.LEFT);
	    lbInitPopulSize.setFont(font);
	    lbInitPopulSize.setForeground(btnBackground);
		
	    NumberFormat intFormat = NumberFormat.getIntegerInstance();

	    NumberFormatter numberFormatter = new NumberFormatter(intFormat);
	    numberFormatter.setValueClass(Integer.class);
	    numberFormatter.setAllowsInvalid(false);
	    numberFormatter.setMinimum(1);
	    numberFormatter.setMaximum(Integer.MAX_VALUE);

	    inInitPopulSize = new JFormattedTextField(numberFormatter);
	    inInitPopulSize.setFont(font);
	    inInitPopulSize.setBorder(null);
	    inInitPopulSize.setForeground(btnBackground);
	    inInitPopulSize.setBackground(inBackground);
	    
	    rbtnFilePopul = new JRadioButton("load from file");
	    rbtnFilePopul.setMnemonic(KeyEvent.VK_F);
	    rbtnFilePopul.setActionCommand("from_file");
	    
	    inInitPopulFile = new JTextField();
	    inInitPopulFile.setFont(fontSmall);
	    inInitPopulFile.setBorder(null);
	    inInitPopulFile.setForeground(btnBackground);
	    inInitPopulFile.setBackground(inBackground);
        
	    btnOpenFile = new JButton("Choose");
	    btnOpenFile.addActionListener(this);
	    btnOpenFile.setFont(fontSmall);
	    btnOpenFile.setBackground(btnBackground); 		
	    btnOpenFile.setForeground(btnForeground);
	    btnOpenFile.setBorder(null);
	    
	    //group the radio buttons
	    group = new ButtonGroup();
	    group.add(rbtnRandomPopul);
	    group.add(rbtnFilePopul);

	    rbtnRandomPopul.addActionListener(this);
	    rbtnFilePopul.addActionListener(this);
	    rbtnRandomPopul.setFont(font);
	    rbtnFilePopul.setFont(font);
	    rbtnRandomPopul.setForeground(btnBackground);
	    rbtnFilePopul.setForeground(btnBackground);
	    
	    lbGenNumber = new JLabel("Number of generations:",JLabel.LEFT);
	    lbGenNumber.setFont(font);
	    lbGenNumber.setForeground(btnBackground);
	    
	    inGenNumber = new JFormattedTextField(numberFormatter);
	    inGenNumber.setFont(font);
	    inGenNumber.setBorder(null);
	    inGenNumber.setForeground(btnBackground);
	    inGenNumber.setBackground(inBackground);
	    
	    btnInit = new JButton("Initialize");
	    btnInit.addActionListener(this);
	    btnInit.setFont(font);
	    btnInit.setBackground(btnBackground); 		
	    btnInit.setForeground(btnForeground);
	    btnInit.setBorder(null);
	    
	    btnStart = new JButton("Start");
	    btnStart.addActionListener(this);
	    btnStart.setFont(font);
	    btnStart.setBackground(btnBackground);
	    btnStart.setForeground(btnForeground);
	    btnStart.setBorder(null);
	    
		infoMsg = new JLabel("",JLabel.LEFT);
		infoMsg.setFont(fontBig);
		infoMsg.setForeground(btnBackground);
		
		infoGenerationCount = new JLabel("Generation 1",JLabel.LEFT);
		infoGenerationCount.setFont(font);
		infoGenerationCount.setForeground(btnBackground);
		
		infoPopulationSize = new JLabel("Population size: ",JLabel.LEFT);
		infoPopulationSize.setFont(font);
		infoPopulationSize.setForeground(btnBackground);
		
		infoFittestAnt = new JLabel("Fittest ant forager:",JLabel.LEFT);
		infoFittestAnt.setFont(font);
		infoFittestAnt.setForeground(btnBackground);
		
		infoEnvironment = new JLabel("Environment:",JLabel.LEFT);
		infoEnvironment.setFont(font);
		infoEnvironment.setForeground(btnBackground);
		
		try {
			antPicture = ImageIO.read(new File(IMG_FILENAME));
			picLabel = new JLabel(new ImageIcon(antPicture));
			add(picLabel);
			picLabel.setBounds(10, 350, 350, 400);
		} catch (IOException e) {
			System.err.println("Cannot load ant picture");
		}
		
		barChart = ChartFactory.createBarChart("", "", "",            
											     createDataset(10, 10),          
											     PlotOrientation.VERTICAL,           
											     false, true, false);
		barChart.setBackgroundPaint(inBackground);
		chartPanel = new ChartPanel(barChart);
		
		add(lbInitPopul);
		add(rbtnRandomPopul);
		add(lbInitPopulSize);
		add(inInitPopulSize);
		add(inInitPopulFile);
		add(btnOpenFile);
		add(inGenNumber);
		add(rbtnFilePopul);
		add(lbGenNumber);
	    add(btnInit);
	    add(btnStart);
	    add(infoMsg);
	    add(infoGenerationCount);
	    add(infoPopulationSize);
	    add(infoFittestAnt);
	    add(infoEnvironment);
	    add(chartPanel);
	    
	    /* 
	     * Set the position and size of each component
	     */
	    lbInitPopul.setBounds(20, 20, 200, 30);
	    rbtnRandomPopul.setBounds(20, 55, 185, 30);
	    
	    lbInitPopulSize.setBounds(40, 85, 100, 30);
	    inInitPopulSize.setBounds(130, 85, 150, 30);
	    
	    rbtnFilePopul.setBounds(20, 120, 190, 30);
	    inInitPopulFile.setBounds(40, 150, 250, 30);
	    btnOpenFile.setBounds(290, 150, 55, 30);
	    
	    lbGenNumber.setBounds(20, 190, 220, 30);
	    inGenNumber.setBounds(40, 225, 200, 30);
	    
	    btnInit.setBounds(20, 280, 220, 30);
	    btnStart.setBounds(20, 320, 220, 30);
	    
	    infoMsg.setBounds(600, 20, 500, 30);
	    infoGenerationCount.setBounds(370, 70, 250, 30);
	    infoPopulationSize.setBounds(820, 70, 250, 30);
	    infoFittestAnt.setBounds(370, 130, 500, 250);
	    infoFittestAnt.setVerticalAlignment(JLabel.TOP);
	    infoEnvironment.setBounds(820, 130, 400, 250);
	    infoEnvironment.setVerticalAlignment(JLabel.TOP);

	    btnInit.setEnabled(true);
	    btnStart.setEnabled(false);
	    btnOpenFile.setVisible(false);
		inInitPopulFile.setVisible(false);
		
		infoGenerationCount.setVisible(false);
	    infoPopulationSize.setVisible(false);
	    infoFittestAnt.setVisible(false);
	    infoEnvironment.setVisible(false);
	    
	    chartPanel.setBounds(370, 350, 800, 400);
	    //chartPanel.setBounds((int)0.33*winWidth, (int)0.42*winHeight, (int)0.7*winWidth, (int)0.49*winHeight);
	    chartPanel.setVisible(false);
	    
	    validate();
	}
	
	/**
	 * Actions listener
	 */
	public void actionPerformed(ActionEvent evt) {
		Object src = evt.getSource();
		if (evt.getActionCommand().equals("random")) {
			lbInitPopulSize.setVisible(true);
			inInitPopulSize.setVisible(true);
			btnOpenFile.setVisible(false);
			inInitPopulFile.setVisible(false);
			
		} else if (evt.getActionCommand().equals("from_file")) {
			lbInitPopulSize.setVisible(false);
			inInitPopulSize.setVisible(false);
			btnOpenFile.setVisible(true);
			inInitPopulFile.setVisible(true);
			
		} else if (src == btnOpenFile) {	
			JFileChooser initPopulFileChooser = new JFileChooser();
			FileFilter filterCSV = new FileNameExtensionFilter("CSV file", "csv", "CSV");
			initPopulFileChooser.setFileFilter(filterCSV);
		    if (initPopulFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		    	inInitPopulFile.setText(initPopulFileChooser.getSelectedFile().getAbsolutePath());
		    	
		} else if (src == btnInit)
			init();
		else if (src == btnStart)
			start();
	}
 
    /**
     * Initialize: generate random population or load it from file; display init numbers
     */
    void init() {
    	chartPanel.setVisible(false);
        infoMsg.setText("Initial State");
        infoGenerationCount.setText("Generation 1");
        
        log.info("************** BEST ANT FORAGER START **************");
        
		/*
		 * initial population
		 */
		//random population generation of a given size
		if (rbtnRandomPopul.isSelected()) {
			try {
				populationSize = Integer.parseInt(inInitPopulSize.getText().replace(",", ""));
				initPopulation = generateRandomPopulation(populationSize);
				
			} catch (NumberFormatException e) {
				System.err.println("Invalid population size. " + e.getLocalizedMessage());
			}
		
		//reading the population and its size from a file
		} else {
			String inFilePath = inInitPopulFile.getText();
			File inFile = new File(inFilePath);
			
			if (!inFile.exists()) {
				System.err.println("File " + inFilePath + " does not exist. Please enther the correct full path to a file containing initial population data");
				return;
			} else {
				PopulationPair popl;
				try {
					popl = readPopulationFromFile(inFile);
					populationSize = popl.getSize();
					initPopulation = popl.getPopul();
				} catch (IOException e) {
					System.err.println("File " + inFilePath + " does not exist. Please enther the correct full path to a file containing initial population data");
					return;
				}
			}
		}
			
		/*
		 * initial livable environment
		 */
		initEnvironment = new Environment();
		
        /*
         * number of generations
         */
		generationsNumber = Integer.parseInt(inGenNumber.getText().replace(",", ""));
		
		//output both to terminal and log file
		System.out.println("\nInitial population size = " + populationSize);
		System.out.println("Number of generations to create = " + generationsNumber);
		log.info("Initial population size = " + populationSize);
		log.info("Number of generations to create = " + generationsNumber);
		
		System.out.println("\nInitial environment:");
		System.out.println(initEnvironment.toString());
		log.info("Initial environment:\n" + initEnvironment.toString());
		System.out.println();
		
		//output most fit individuals from initial population
		List<AntForager> initWinners = getWinners(initPopulation, initEnvironment);
		if (!initWinners.isEmpty()) {
			System.out.println("\n*** Fittest individual" + (initWinners.size() == 1 ? "" : "s") + " from initial population ***");	
			log.info("*** Fittest individual" + (initWinners.size() == 1 ? "" : "s") + " from initial population ***");
			int i = 1;
			for (AntForager winner : initWinners) {
				System.out.println("\n" + (initWinners.size() == 1 ? "" : i+". ") + "AntForager:");
				System.out.println(winner.toString());
				log.info((initWinners.size() == 1 ? "" : i+". ") + "AntForager: " + winner.toString());
				i++;
			}
			infoFittestAnt.setText(convertToMultiline("Fittest Ant Forager:\n" + initWinners.get(0).toString()));
		} else
			infoFittestAnt.setText("Fittest Ant Forager: none");
		System.out.println("*****\n");
		log.info("*****");
		
		
		infoGenerationCount.setVisible(true);
		infoPopulationSize.setText("Population Size: " + String.format("%,d", populationSize));
		infoPopulationSize.setVisible(true);
	    infoFittestAnt.setVisible(true);
	    infoEnvironment.setText(convertToMultiline("Environment:\n" + initEnvironment.toString()));
	    infoEnvironment.setVisible(true);
	    
        btnInit.setEnabled(true);
        btnStart.setEnabled(true);
        
		repaint();
    }	
    
     /**
      * Start: validate inputs and run genetic algorithm
      */
     void start() {
        infoMsg.setText("In Progress...");
        btnInit.setEnabled(false);
        btnStart.setEnabled(false);
        infoFittestAnt.setVisible(false);
	    infoEnvironment.setVisible(false);
	    chartPanel.setVisible(true);
        repaint();
        validate();
		
		/*
		 * call genetic algorithm
		 */
		List<AntForager> winners = this.geneticAlgorithm(initPopulation, initEnvironment, generationsNumber);
		
		if (winners.isEmpty()) {
			System.out.println("\n*** NO WINNER ***");
			log.info("*** NO WINNER ***");
			infoFittestAnt.setText("Fittest Ant Forager: none");
		} else {
			System.out.println("\n*********** WINNER" + (winners.size() == 1 ? "" : "S") + " ***********");	
			log.info("*********** WINNER" + (winners.size() == 1 ? "" : "S") + " ***********");
			int i = 1;
			for (AntForager winner : winners) {
				System.out.println("\n" + (winners.size() == 1 ? "" : i+". ") + "AntForager:");
				System.out.println(winner.toString());
				log.info((winners.size() == 1 ? "" : i+". ") + "AntForager: " + winner.toString());
				i++;
			}
			infoFittestAnt.setText(convertToMultiline("WINNER Ant Forager:\n" + winners.get(0).toString()));
		}
		
		log.info("************** BEST ANT FORAGER END **************");
		
		infoMsg.setText("Final State");
		infoFittestAnt.setVisible(true);
	    infoEnvironment.setVisible(true);
	    
        btnInit.setEnabled(true);
        btnStart.setEnabled(true);
        repaint();
     }

	
	/**
	 * Main logic - genetic algorithm
	 * @param initPopulation
	 * @param initEnvironment
	 * @param generationsNumber number of generations
	 * @return winners
	 */
	public List<AntForager> geneticAlgorithm(List<AntForager> initPopulation, Environment initEnvironment, int generationsNumber) {
		List<AntForager> population = initPopulation;		//current whole population
		List<AntForager> parentsPool = null;				//potential parents - population (with fitness levels) after discarding individuals with low fitness
		List<AntForager> parents = null;					//actual parents list
		List<AntForager> newPopulation = null;				//offspring
		AntForager mom, dad, child;
		Environment environment = initEnvironment;
		EnviGen envGenerator = new EnviGen();
		Reproduce reproduce = new Reproduce();
		Mutation mutation = new Mutation();
		Selection selection = new Selection();
		Fitness fit = new Fitness();
		List<AntForager> winners;
		double tmp;	//to calculate log10 for generation number output
		int deadAntsNumber = 0;
		
		/*
		 * outer loop: one iteration = one generation
		 * stopping rule: required number of generations passed
		 */
		for (int generationCount = 1; generationCount <= generationsNumber; generationCount++) {
			/*
			 * output generation number - log10 scale
			 */
			tmp = Math.log10(generationCount);
			if (tmp == (int)tmp || generationCount == generationsNumber) {
				System.out.println("Generation " + generationCount);
				log.info("Generation " + generationCount);
			}
			infoGenerationCount.setText("Generation " + String.format("%,d", generationCount));
			repaint();
			
			newPopulation = new ArrayList<AntForager>();
			
			/*
			 * potential parents pre-selection based on the fitness level
			 */
			parentsPool = selection.cullPopulation(population, environment);
			
			/*
			 * inner loop: generating offspring as new population
			 */
			for (int i = 0; i < population.size(); i++) {
				//if less than two potential parents, then offspring cannot be generated
				if (parentsPool.isEmpty() || parentsPool.size() < 2) {
					System.out.println("WARNING: Not enough individuals in the parents pool: actual number is " + parentsPool.size() + ", min allowed number is 2");
					System.out.println("Final population size = " + population.size());
					System.out.println("\nFinal environment:");
					System.out.println(environment.toString());
					
					log.warn("Not enough individuals in the parents pool: actual number is " + parentsPool.size() + ", min allowed number is 2");
					log.info("Final population size = " + population.size());
					log.info("Final environment:");
					log.info(environment.toString());
					
					//output the last generation number
					if (tmp != (int)tmp && generationCount != generationsNumber) {
						System.out.println("Generation " + generationCount);
						log.info("Generation " + generationCount);
					}
					
					if (parentsPool.size() < 2) {
						System.out.println("The last survivor is the winner");
						log.info("The last survivor is the winner");
					}
					
					return parentsPool;
				}
				
				/*
				 * parents selection
				 */
				parents = selection.getParents(parentsPool, environment);
				mom = parents.get(0);
				dad = parents.get(1);
				
				/*
				 * reproduce
				 */
				child = reproduce.produceChild(mom, dad);
				mom.addOffspring(1);
				dad.addOffspring(1);
				
				//remove individuals with max possible number of offspring from parents pool
				parentsPool.removeIf(p -> (p.getOffspringCount() == AntForager.MAX_OFFSPRING_COUNT));
				
				/*
				 * mutate
				 */
				if (mutation.mutationProbability() >= 0.99)
					child = Mutation.mutate(child);
				
				//offspring added
				newPopulation.add(child);
			}
			
			//current population is about to die so we add up to the dead ants counter here
			deadAntsNumber += population.size();
			//draw charts for current population before they die
			drawCharts(population, deadAntsNumber, generationCount);
			repaint();
			
			//offspring can be parents in the next iteration
			population.clear();
			population.addAll(newPopulation);
			
			/*
			 * environment change with every new generation
			 */
			environment = envGenerator.EG(environment, generationCount);
		}
		population = fit.calculatePopulationFitness(population, environment);
		drawCharts(population, deadAntsNumber, generationsNumber);
		repaint();
		validate();
		
		System.out.println("Final population size = " + population.size());
		System.out.println("Final environment:\n");
		System.out.println(environment.toString());
		
		log.info("Final population size = " + population.size());
		log.info("Final environment: " + environment.toString());
		
		infoEnvironment.setText(convertToMultiline("Environment:\n" + environment.toString()));
		
		//select individual with max fitness level
		winners = getWinners(population, environment);
		
		return winners;
	}
	
	
	/**
	 * Generates random population of the given size
	 * and writes this population to the CSV file
	 * @param size number of individuals in population
	 * @return population consisting of randomly generated AntForager	 */
	private static List<AntForager> generateRandomPopulation(int size) {
		//generating random population
		List<AntForager> randomPopulation = new ArrayList<AntForager>();
		for (int i = 0; i < size; i++) {
			randomPopulation.add(AntForager.generateRandomAnt());
		}
		
		//write generated population to a CSV file
		try {
			String outFileName = OUT_FILENAME_BASE + "1.csv";
			File outFile = new File(outFileName);
			//not overwriting existing file but creating a new one
			int j = 2;
			while (outFile.exists()) {
				outFileName = OUT_FILENAME_BASE + j + ".csv";
				outFile = new File(outFileName);
				j++;
			}
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFileName), "UTF-8"));
			StringBuffer oneLine = new StringBuffer();
			//header
			oneLine = new StringBuffer();
            oneLine.append("Size (mm)");
            oneLine.append(CSV_SEPARATOR);
            oneLine.append("Weight (mg)");
            oneLine.append(CSV_SEPARATOR);
            oneLine.append("Jaw size (mm)");
            oneLine.append(CSV_SEPARATOR);
            oneLine.append("Feromone intensity (%)");
            oneLine.append(CSV_SEPARATOR);
            oneLine.append("Antennae censory cells");
            oneLine.append(CSV_SEPARATOR);
            oneLine.append("Offspring Count");
            
            bw.write(oneLine.toString());
            bw.newLine();
            
			//data
	        for (AntForager ant : randomPopulation) {
	           	oneLine = new StringBuffer();
	            oneLine.append(ant.getSize());
	            oneLine.append(CSV_SEPARATOR);
	            oneLine.append(ant.getWeight());
	            oneLine.append(CSV_SEPARATOR);
	            oneLine.append(ant.getJawSize());
	            oneLine.append(CSV_SEPARATOR);
	            oneLine.append(ant.getFeromoneIntensity());
	            oneLine.append(CSV_SEPARATOR);
	            oneLine.append(ant.getAntennaeSensors());
	            oneLine.append(CSV_SEPARATOR);
	            oneLine.append(ant.getOffspringCount());
	            
	            bw.write(oneLine.toString());
	            bw.newLine();
	        }
	        bw.flush();
	        bw.close();
	        System.out.println("Initial population saved in file " + outFileName);
	        log.info("Initial population saved in file " + outFileName);
	        
	    //if failed to write to a file - proceed anyway
		} catch (IOException e) {
			System.out.println("WARNING: unable to write generated population to CSV file. Writing to the file skipped.\n");
			log.warn("Unable to write generated population to CSV file. Writing to the file skipped.");
		}
		
		return randomPopulation;
	}


	/**
	 * Reads population from the CSV file
	 * @param inFile input file containing the list of ants
	 * @return instance containing population and its size
	 * @throws IOException 
	 */
	private static PopulationPair readPopulationFromFile(File inFile) throws IOException {
		List<AntForager> inPopulation = new ArrayList<AntForager>();
		AntForager ant;
		int inPopulationSize = -1;	//to skip the header line
		
		BufferedReader br = null;
		String line = "";
		String[] AntForager;	//to store one line as array
		br = new BufferedReader(new FileReader(inFile));
		
		//reading file line by line
		while ((line = br.readLine()) != null) {
			//skip the header line
            if (inPopulationSize >= 0) {
				AntForager = line.split(CSV_SEPARATOR);
				/*for (String s : AntForager) {
					System.out.print(s+"|");
				}*/
				ant = new AntForager();
				ant.setSize(Double.parseDouble(AntForager[0]));
				ant.setWeight(Double.parseDouble(AntForager[1]));
				ant.setJawSize(Double.parseDouble(AntForager[2]));
				ant.setFeromoneIntensity(Double.parseDouble(AntForager[3]));
				ant.setAntennaeSensors(Integer.parseInt(AntForager[4]));
				ant.setOffspringCount(Integer.parseInt(AntForager[5]));
				inPopulation.add(ant);
            }
			inPopulationSize ++;
		}
		if (br != null) 
			br.close();

		PopulationPair popl = new PopulationPair(inPopulationSize, inPopulation);
		return popl;
	}
	
	
	/**
	 * Determines the winner individuals - the ants with the highest fitness level
	 * @param population
	 * @param envmt environment
	 * @return list of individuals with the highest fitness level
	 */
	@SuppressWarnings("unchecked")
	private static List<AntForager> getWinners(List<AntForager> population, Environment envmt) {
		if (population.isEmpty() || population == null)
			return population;
		
		//calculate fitness levels for population
		Fitness fit = new Fitness();
		population = fit.calculatePopulationFitness(population, envmt);
		List<AntForager> winners = new ArrayList<AntForager>();
		
		//sort population by fitness level in asc order
		Collections.sort(population);
		
		//obtaining the top fitness level value - fitness level of the last individual in the sorted list
		double topFitness = population.get(population.size()-1).getFoodSurplus();
		
		for (AntForager ant : population) {
			if (ant.getFoodSurplus() == topFitness)
				winners.add(ant);
		}
		
		//get rid of duplicate AntForagers from the winners list so we have distinct AntForagers in output
		winners = new ArrayList<AntForager> (new HashSet<AntForager>(winners));
		return winners;
	}

	/**
	 * Makes a text label output multiple lines
	 * @param orig
	 * @return
	 */
	public static String convertToMultiline(String orig) {
	    return "<html>" + orig.replaceAll("\n", "<br>");
	}	
	
	/**
	 * Draws bar charts for current generation food surplus and total discarded number of ants
	 * @param population
	 * @param deadAntsNumber
	 * @param generationNumber
	 */
	public void drawCharts(List<AntForager> population, int deadAntsNumber, int generationCount) {
		double generationFoodSurplus = 0;	//total food surplus for current population
		
		if (population != null)
			for (AntForager ant : population)
				generationFoodSurplus += ant.getFoodSurplus();
		remove(chartPanel);
		barChart = ChartFactory.createBarChart("", "", "",            
											     createDataset(generationFoodSurplus, deadAntsNumber),          
											     PlotOrientation.VERTICAL,           
											     false, true, false);
		
		barChart.setBackgroundPaint(inBackground);
		chartPanel = new ChartPanel(barChart);
		add(chartPanel);
		chartPanel.setBounds(370, 350, 800, 400);
		//chartPanel.setBounds((int)0.33*winWidth, (int)0.42*winHeight, (int)0.7*winWidth, (int)0.49*winHeight);
		chartPanel.setVisible(true);
		validate();
	}

	private CategoryDataset createDataset(double f , int a) {
	    final DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
	    dataset.addValue(a , "Total Ants Discarded" , "Total Ants Discarded"); 
	    dataset.addValue(f , "Food Collected by current generation" , "Food Collected by current generation, g"); 
	    return dataset; 
	}
	
}
