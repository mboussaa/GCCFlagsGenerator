package NS;
 
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Population {
	 
    Individual[] individuals;
 
    // Create a population
    public Population(int populationSize, boolean initialise) {
        individuals = new Individual[populationSize];
        // Initialise population
        if (initialise) {
            // Loop and create individuals
            for (int i = 0; i < size(); i++) {
                Individual newIndividual = new Individual();
                newIndividual.generateIndividual();
                saveIndividual(i, newIndividual);
            }
        }
    }
 
    /* Getters */
    public Individual getIndividual(int index) {
        return individuals[index];
    }
 
    public Individual[] getMoreCompetent(int eliteIndividuals) {
        Individual[] moreCompetent= individuals;
        boolean swapped = true;
        int j = 0;
        Individual tmp;
        while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i < individuals.length - j; i++) {
                if (moreCompetent[i].getCompetence() < moreCompetent[i + 1].getCompetence()) {
                    tmp = moreCompetent[i];
                    moreCompetent[i] = moreCompetent[i + 1];
                    moreCompetent[i + 1] = tmp;
                    swapped = true;
                }
            }
        }
        
        moreCompetent = Arrays.copyOfRange(moreCompetent, 0, eliteIndividuals);
 
        return moreCompetent;
    }
 
    public Individual getMaxCompetence() {
    	float c=0;
    	int x=0;
    	for (int i = 0; i < size(); i++) {
    		if (getIndividual(i).competence>c){
    			c=getIndividual(i).getCompetence();
    			x=i;
    		}
		}
    	return getIndividual(x);
    }
    
    public void evaluate(List<Individual> archive) {
    	for (int i = 0; i < size(); i++) {
    		getIndividual(i).setCompetence(this, archive);
		}
    }
    
    /* Public methods */
    // Get population size
    public int size() {
        return individuals.length;
    }
 
    // Save individual
    public void saveIndividual(int index, Individual indiv) {
        individuals[index] = indiv;
    }

	public List<Individual> getArchive(double j) {
		List<Individual> toArchive = new LinkedList<Individual>();
		for (int i = 0; i < size(); i++) {
			if (getIndividual(i).getCompetence()>=j){
				toArchive.add(getIndividual(i));
			}
		}
		return toArchive;
	}
}