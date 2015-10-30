package NS;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;



public class Individual {
	static int k = 15; 
    static int defaultGeneLength = 76;
    
    
    private final byte[] genes = new byte[defaultGeneLength];
    float competence = 0;
    private List<Double> distances;
 
    // Create a random individual
    public void generateIndividual() {
        for (int i = 0; i < size(); i++) {
            byte gene = (byte) Math.round(Math.random());
            genes[i] = gene;
        }
    }
 
    /* Getters and setters */
    // Use this if you want to create individuals with different gene lengths
    public static void setDefaultGeneLength(int length) {
        defaultGeneLength = length;
    }
 
    public byte getGene(int index) {
        return genes[index];
    }
 
    public void setGene(int index, byte value) {
        genes[index] = value;
        competence = 0;
    }
 
    /* Public methods */
    public int size() {
        return genes.length;
    }
 
    public float getCompetence() {
		return competence;
	}

	public void setCompetence(Population pop,List<Individual> archive) {
	 
		distances = new LinkedList<Double>();
    	double DistanceFromkNearest = 0;
    	compareGenesToPop(getGenes(), pop);
    	compareGenesToArchive(getGenes(), archive);
    	Collections.sort(distances);
//    	System.out.println("pop + archive :"+(pop.size()+archive.size()));
//    	System.out.println("distances size :"+distances.size());
//    	System.out.println("all sorted "+distances);
    	for (int i = 0; i < distances.size(); i++) {
			//System.out.println(distances.get(i));
		}

    	setDistances(distances.subList(1, k+1));
       	for (int i = 0; i < distances.size(); i++) {
    			
    		}
//       	System.out.println("selected "+distances);
    	for(double dis : distances){
    		DistanceFromkNearest+=dis;
    	}
    	  
    	competence= (float) (DistanceFromkNearest/k);
//    	System.out.println(competence);
    	
	}
 
	public void compareGenesToPop(byte[] genes,Population pop) {
    	for (int i = 0; i < pop.size(); i++) {
    		//System.out.println("gene1 "+genes.toString());
    		//System.out.println("gene2 "+pop.getIndividual(i).getGenes().toString());
    		//System.out.println("size "+genes.length);
    		//System.out.println(pop.size());
    		distances.add(compare2Genes(genes, pop.getIndividual(i).getGenes()));
        }
    }
	
	public void compareGenesToArchive(byte[] genes,List<Individual> archive) {
    	for (int i = 0; i < archive.size(); i++) {
    		distances.add(compare2Genes(genes, archive.get(i).getGenes()));
        }
    }
	
    public double compare2Genes(byte[] genes1,byte[] genes2) {
        int difference2genes=0;
    	for (int i = 0; i < genes1.length; i++) {
        	
        		if (genes1[i]!=genes2[i]) {
        			difference2genes++;
        		}
        }
    	//System.out.println("diff "+difference2genes);
    	return difference2genes;
    }
 
   
	
    public byte[] getGenes() {
		return genes;
	}
    @Override
    public String toString() {
        String geneString = "";
        for (int i = 0; i < size(); i++) {
            geneString += getGene(i);
        }
     
        
        return geneString;
    }
    
	public void setDistances(List<Double> distances) {
		this.distances = distances;
	}


}