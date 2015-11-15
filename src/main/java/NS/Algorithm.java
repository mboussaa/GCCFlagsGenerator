package NS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.google.common.collect.EvictingQueue;

public class Algorithm {
	//String[] tab = new String[]{1,2,4,5,6};
    /* GA parameters */
    private static final double uniformRate = 0.5;
    private static final double mutationRate =0.1;
    private static final int tournamentSize = 2;
    private static final int eliteIndividuals = 10;
    private static final boolean elitism = true;
    
    private static double T=30;
    private static final double scaleUp=1.05;
    private static final double scaleDown=0.95;
    private static final int differenceArchive=3;
    private static final int updateScale=1500;
    private static final int L=500;
    

    
     /* Public methods */
    private static int evaluations=0;
    private static int archiveNew=0;
    private static int archiveTotal=0;
    private static final Queue<Individual> archive = EvictingQueue.create(L); 
    private static final List<String> passes = new LinkedList<String>();
    private static final List<String> no_passes = new LinkedList<String>();
    private static final List<String> optimizations = new LinkedList<String>();
    // Evolve a population
    public static Population evolvePopulation(Population pop) throws Exception {
  
    	updateNoveltyThreshold(pop.size(),archiveTotal, updateScale, differenceArchive, scaleUp, scaleDown) ;
    	
    	
    	//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    	System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        Population newPopulation = new Population(pop.size(), false);

        // First evaluate all individuals
        pop.evaluate(queueToList(archive));
        
        writePopToFile(pop);
      
        
        // Add individuals to archive
        archive.addAll(pop.getArchive(T));
        archiveTotal+=pop.getArchive(T).size();
        //System.out.println("***************************archive********************************");
        
        	//System.out.println("archive "+archive);
 
        
        //System.out.println("***************************************************************************");
        
        // Keep our best individual
        if (elitism) {
        	for (int i = 0; i < eliteIndividuals; i++) {
        		newPopulation.saveIndividual(i, pop.getMoreCompetent(eliteIndividuals)[i]);	
			}
        }
        int elitismOffset;
        if (elitism) {
            elitismOffset = eliteIndividuals;
        } else {
            elitismOffset = 0;
        }
       // System.exit(0);
        // Loop over the population size and create new individuals with
        // crossover
        for (int i = elitismOffset; i < pop.size(); i++) {
            Individual indiv1 = tournamentSelection(pop);
            Individual indiv2 = tournamentSelection(pop);
            //System.out.println("selected 1 = "+indiv1+" fitness = "+indiv1.getCompetence());
            //System.out.println("selected 2 = "+indiv2+" fitness = "+indiv2.getCompetence());
            Individual newIndiv = crossover(indiv1, indiv2);
            //System.out.println("crossover = "+newIndiv);
            newPopulation.saveIndividual(i, newIndiv);
        }
 
        // Mutate population
        for (int i = elitismOffset; i < newPopulation.size(); i++) {
            mutate(newPopulation.getIndividual(i));
        }
 
        return newPopulation;
    }
 
    private static void updateNoveltyThreshold(int pop,int archive ,int limit, int difference, double scaleUp, double scaleDown) {
       	evaluations+=pop;
    	if (evaluations==limit) {
    		//System.out.println("size archive : "+archive);
    		//System.out.println("size ancien archive : "+archiveNew);
    		//System.out.println("difference size archive : "+(archive-archiveNew));
    		//System.out.println("ancien T : "+T);
			if ((archive-archiveNew)<difference){
				T=T*scaleDown;
			}else{
				T=T*scaleUp;
			}
			//System.out.println("new T : "+T);
			archiveNew=archive;
    		evaluations=0;
		}
	}

	// Crossover individuals
    private static Individual crossover(Individual indiv1, Individual indiv2) {
        Individual newSol = new Individual();
        // Loop through genes
        for (int i = 0; i < indiv1.size(); i++) {
            // Crossover
            if (Math.random() <= uniformRate) {
                newSol.setGene(i, indiv1.getGene(i));
            } else {
                newSol.setGene(i, indiv2.getGene(i));
            }
        }
        return newSol;
    }
 
    // Mutate an individual
    private static void mutate(Individual indiv) {
        // Loop through genes
        for (int i = 0; i < indiv.size(); i++) {
            if (Math.random() <= mutationRate) {
                // Create random gene
                byte gene = (byte) Math.round(Math.random());
                indiv.setGene(i, gene);
            }
        }
    }
 
    // Select individuals for crossover
    private static Individual tournamentSelection(Population pop) {
        // Create a tournament population
        Population tournament = new Population(tournamentSize, false);
        // For each place in the tournament get a random individual
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.size());
            tournament.saveIndividual(i, pop.getIndividual(randomId));
            //System.out.println("tournament selected = "+pop.getIndividual(randomId)+" fitness = "+pop.getIndividual(randomId).getCompetence());
        }
        // Get the fittest
        Individual fittest = tournament.getMaxCompetence();
        //System.out.println("tournament selected = "+fittest);
        return fittest;
    }
    
    private static  List<Individual> queueToList(Queue<Individual> archive) {
    	  List<Individual> list = new LinkedList<Individual>();
          Iterator<Individual> it = archive.iterator();
          while(it.hasNext()){
             list.add(it.next());
          }
        return list;
    }
	public static void setPasses() throws Exception {
        FileReader fr = new FileReader("optimizations.txt");
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while(line!= null) {
        	//System.out.println(line);
        	//System.out.println(line.substring(0,2)+"no-"+line.substring(2));
    		
            passes.add(line);
            no_passes.add(line.substring(0,2)+"no-"+line.substring(2));
            line = br.readLine();
        }   
        br.close();

        //System.out.println(passes.size());
	}
	public static void writePopToFile(Population pop) throws Exception {
    
		  for (int i = 0; i < pop.size(); i++) {
			  String opti="";
				for (int j = 0; j < pop.getIndividual(i).getGenes().length; j++) {
					if (pop.getIndividual(i).getGenes()[j]==1) {
						
						opti+=passes.get(j)+" ";
						
					}else{
						opti+=no_passes.get(j)+" ";
					}
				}
			  System.out.println(pop.getIndividual(i).getCompetence());
				FileWriter fw = new FileWriter("NS-gcc.txt", true);
				BufferedWriter bw = new BufferedWriter ( fw ) ; 
				 
				PrintWriter pw = new PrintWriter ( bw ) ; 
				
				pw.print(opti) ; 
			    bw.newLine();
				pw.close( ) ; 
				
				System.out.println(opti);
			}

	}
}