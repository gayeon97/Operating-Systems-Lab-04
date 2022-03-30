/**
 * This lab was produced by Gayeon Park on April 2020
 */

public class ProcessInfo {
	private int processNum;
	private double probA;
	private double probB;
	private double probC;
	private double probRand;
	private boolean firstReference;
	private int numReferences;
	private int wordRef;
	private int numFaults;
	private double sumEvictions;
	private int numEvictions;
	
	public ProcessInfo(int pNum, double a, double b, double c, int size, int numberofRefs) {
		processNum = pNum;
		probA = a;
		probB = b;
		probC = c;
		probRand = (1 - a - b - c) / size;
		firstReference = true;
		numReferences = numberofRefs;
		numFaults = 0;
		sumEvictions = 0.0;
		numEvictions = 0;
	}
	
	public int getProcessNum() {
		return processNum;
	}
	
	public double getAProb() {
		return probA;
	}
	
	public double getBProb() {
		return probB;
	}
	
	public double getCProb() {
		return probC;
	}
	
	public double getRandProb() {
		return probRand;
	}
	
	public boolean isFirstReference() {
		return firstReference;
	}
	
	public void didFirstReference() {
		firstReference = false;
	}
	
	public int getNumRef() {
		return numReferences;
	}
	
	public void decreaseNumRef() {
		numReferences --;
	}
	
	public int getWord() {
		return wordRef;
	}
	
	public void setWord(int nxtRef) {
		wordRef = nxtRef;
	}
	
	public int getNumFaults() {
		return numFaults;
	}
	
	public void increaseNumFaults() {
		numFaults ++;
	}
	
	public double getSumEvictions() {
		return sumEvictions;
	}
	
	public void addEviction(double newEviction) {
		sumEvictions += newEviction;
	}
	
	public int getNumEvictions() {
		return numEvictions;
	}
	
	public void increaseNumEvictions() {
		numEvictions ++;
	}
	
}
