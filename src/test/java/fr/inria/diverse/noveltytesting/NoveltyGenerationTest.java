package fr.inria.diverse.noveltytesting;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.junit.Test;

import NS.*;

/**
 * each client instantiates the engine by giving as a parameter the services'
 * interface, the pop and the archive size and then apply the different services
 * of the novelty algorithm
 * 
 * Unit test
 * 
 * 
 * @author mboussaa
 *
 */

public class NoveltyGenerationTest {
	

	 
	@Test
	public void testTestClass() throws Exception {
		final int popSize=100;
    	final int nbGenerations=3;
        
    	// Create an initial population
        Population myPop = new Population(popSize, true);
 
        // Evolve our population until we reach an optimum solution
        int generationCount = 0;
        FileWriter fw = new FileWriter("NS-gcc.txt", false);
		BufferedWriter bw = new BufferedWriter ( fw ) ; 
		PrintWriter pw = new PrintWriter ( bw ) ; 
		pw.close( ) ; 
		Algorithm.setPasses();

        while (generationCount!=nbGenerations) {
        	generationCount++;
        	//Algorithm.writePopToFile(myPop);
            myPop = Algorithm.evolvePopulation(myPop);

	}
    	FileWriter fw1;
		BufferedWriter bw1 = null;
		PrintWriter pw1 = null;
		fw1 = new FileWriter("NS-gcc.txt", true);
	    bw1 = new BufferedWriter ( fw1 ) ; 
	    pw1 = new PrintWriter ( bw1 ) ; 
		pw1.print("-O0\n-O1\n-O2\n-O3\n-Ofast\n-Os") ; 
		bw1.newLine();
		pw1.close( ) ; 

}
}
