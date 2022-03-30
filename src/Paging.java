/**
 * This lab was produced by Gayeon Park on April 2020
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Paging {
	//these are the global variables used throughout this java program
	public static int verbose = -1;
	public static int quantum = 3;
	public static List<String> fileStream = null;
	
	//get the random number from the "random-numbers" file used
	static double randomNum(int processNum) throws FileNotFoundException {
		int randomNumFromFile = Integer.parseInt(fileStream.remove(0));
		if (verbose == 11) {
			System.out.printf("%d uses random number: %d\n", processNum, randomNumFromFile);
		}
		return randomNumFromFile / (Integer.MAX_VALUE + 1d);
	}

	public static void main(String[] args) throws FileNotFoundException {
		int m_machineSize = 0;
		int p_pageSize = 0;
		int s_processSize = 0;
		int j_jobmix = 0;
		int n_numRef = 0;
		String r_replaceAlg = "";
		ArrayList<ProcessInfo> processes = new ArrayList<>(); 
		
		//read the "random-numbers" file
		try {
			fileStream = Files.readAllLines(Paths.get(System.getProperty("user.dir") + "/random-numbers"),Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//check if appropriate number of command line arguments were passed in
		if (args.length >= 6) {
			m_machineSize = Integer.parseInt(args[0]);
			p_pageSize = Integer.parseInt(args[1]);
			s_processSize = Integer.parseInt(args[2]);
			j_jobmix = Integer.parseInt(args[3]);
			n_numRef = Integer.parseInt(args[4]);
			r_replaceAlg = args[5];
			if (args.length == 7) {
				verbose = Integer.parseInt(args[6]);
			}
		} else { //throw error and exit the program
			System.err.printf("Invalid number of command line arguments: %d. Please try again.\n", args.length);
			System.exit(0);
		}
		
		//print the information read in from command line arguments, supplying description for each
		System.out.printf("\nThe machine size is %d.\n", m_machineSize);
		System.out.printf("The page size is %d.\n", p_pageSize);
		System.out.printf("The process size is %d.\n", s_processSize);
		System.out.printf("The job mix number is %d.\n", j_jobmix);
		System.out.printf("The number of references per process is %d.\n", n_numRef);
		System.out.printf("The replacement algorithm is %s.\n", r_replaceAlg);
		System.out.printf("The level of debugging output is %s.\n\n", verbose);
		
		//populate the processes list depending on the jobmix size
		if (j_jobmix == 1) {
			processes.add(new ProcessInfo(1, 1.0, 0.0, 0.0, s_processSize, n_numRef));
		} else {
			//for j_jobmix == 3, all values for a, b, and c are 0.0
			double a = 0.0;
			double b = 0.0;
			double c = 0.0;
			
			if (j_jobmix == 4) {
				processes.add(new ProcessInfo(1, 0.75, 0.25, 0.0, s_processSize, n_numRef));
				processes.add(new ProcessInfo(2, 0.75, 0.0, 0.25, s_processSize, n_numRef));
				processes.add(new ProcessInfo(3, 0.75, 0.125, 0.125, s_processSize, n_numRef));
				processes.add(new ProcessInfo(4, 0.5, 0.125, 0.125, s_processSize, n_numRef));
			} else { //j_jobmix = 2 or 3
				if (j_jobmix == 2) {
					//for j_jobmix == 2, a = 1.0, b = 0.0, and c = 0.0
					a = 1.0;
				} 
				
				//create four processes with corresponding a, b, and c probabilities
				for (int i = 1; i <= 4; i ++) {
					processes.add(new ProcessInfo(i, a, b, c, s_processSize, n_numRef));
				} //end of for loop
			} //end of inner else
		} //end of else for j_jobmix
		
		//this array will be used to store the processes that are finished
		ProcessInfo[] finalList = new ProcessInfo[processes.size()];
		//this ArrayList will represent the frame table of size M/P
		ArrayList<PageEntry> frameTable = new ArrayList<PageEntry>();
		//this arraylist will be used to keep track of least recently used PageEntry
		ArrayList<PageEntry> listLRU = new ArrayList<>();
		//this queue will be used to for FIFO replacement algorithm
		Queue<PageEntry> queueFIFO = new LinkedList<>();
		int word = -1; //represents the word referenced
		int time = 1; //represents time measured in memory references
		
		//initialize the frameTable with empty frames (<--represented by PageEntry(-1,-1))
		for (int z= 0; z < (m_machineSize/p_pageSize); z ++) {
			frameTable.add(new PageEntry(-1,-1));
		}

		//loop until the list of processes is empty
		while (!processes.isEmpty()) {
			//go through each process in the list of processes
			for (int i = 0; i < processes.size(); i ++) {
				//get the process from the list of processes
				ProcessInfo currP = processes.get(i);
				
				//check if the current process is referenced for the first time ever
				if (currP.isFirstReference()) {
					//get the first word reference using formula: (111*processNumber) mod processSize
					currP.setWord(((111 * currP.getProcessNum()) + s_processSize) % s_processSize);
					currP.didFirstReference();	
				}
				
				//this is the word being referenced
				word = currP.getWord();
		
				//each process gets maximum of 3 quantum to resolve process references
				for (int ref = 0; ref < 3; ref ++) {
					//FIRST, simulate this reference for this process
					//create the page wanted for the word being referenced
					int pageNumber = word / p_pageSize; //calculate the page number for the page 
					PageEntry wantedPage = new PageEntry(currP.getProcessNum(), pageNumber);
					
					//CHECK FOR PAGE HIT
					if (frameTable.indexOf(wantedPage) != -1) {
						//resort the listLRU if the algorithm being used is "LRU"
						if (r_replaceAlg.equalsIgnoreCase("lru")) {
							//check if the wanted page is IN the listLRU
							if (listLRU.contains(wantedPage)) {
								//the wanted page is going to be used by the system
								//and needs to be sent to the back of the listLRU
								
								//so first get the index of the wantedPage in listLRU
								int indexRm = listLRU.indexOf(wantedPage);
								
								//then remove it from that index and add to end of listLRU
								listLRU.add(listLRU.remove(indexRm));
							}
							//else if the wanted page is NOT in the listLRU
							else {
								//the wanted page is going to be used by the system
								//so add to the end of the listLRU
								listLRU.add(wantedPage);
							}	
						}
						
						//update the finalList
						finalList[currP.getProcessNum()-1] = currP;
						
						//check for the debug flag to show what the system is doing
						if (verbose == 1 || verbose == 11) {
							//indicate page hit by printing info
							System.out.printf("\n%d references word %d (page %d) at time %d: Hit in frame %d\n",currP.getProcessNum(),word,pageNumber,time,frameTable.indexOf(wantedPage));
						}
						
					} 
					//ELSE PAGE FAULT HAS OCCURED
					else {
						
						//CHECK if frame Table is NOT FULL yet, meaning there is space to put the page wanted
						if (frameTable.indexOf(new PageEntry(-1,-1)) != -1) {
							//go through the frame table, starting at the highest index
							// because we must choose the highest numbered free frame
							for (int k = frameTable.size() -1; k >=0; k --) {
								PageEntry currFrame = frameTable.get(k);
								
								//check if the current frame is "empty" (empty frame is represented by PageEntry(-1,-1))
								if (currFrame.equals(new PageEntry(-1,-1))) {
									//System.out.println("PAGE FAULT OCCURED AND TABLE NOT FULL YET!");
									
									//increase the number of fault for the current process
									currP.increaseNumFaults();
									
									//update the time loaded for the wanted page
									wantedPage.setTimeLoaded(time);
									//System.out.printf("This is the time page %d of process %d was loaded: %d\n",wantedPage.getPageNumber(),wantedPage.getProcessNumber(), wantedPage.getTimeLoaded());

									//store the wanted page into the correct highest && free index of the frame table
									frameTable.set(k, wantedPage);
									
									//resort the listLRU if the algorithm being used is "LRU"
									if (r_replaceAlg.equalsIgnoreCase("lru")) {
										//check if the wanted page is IN the listLRU
										if (listLRU.contains(wantedPage)) {
											//the wanted page is going to be used by the system
											//and needs to be sent to the back of the listLRU
											
											//so first get the index of the wantedPage in listLRU
											int indexRm = listLRU.indexOf(wantedPage);
											
											//then remove it from that index and add to end of listLRU
											listLRU.add(listLRU.remove(indexRm));
										}
										//else if the wanted page is NOT in the listLRU
										else {
											//the wanted page is going to be used by the system
											//so add to the end of the listLRU
											listLRU.add(wantedPage);
										}
										
									} 
									
									//add the page to the the "queueFIFO" if the algorithm being used is "FIFO"
									else if (r_replaceAlg.equalsIgnoreCase("fifo")) {
										queueFIFO.add(wantedPage);
									} 
									
									//update the finalList
									finalList[currP.getProcessNum()-1] = currP;
									
									//check for the debug flag to show what the system is doing
									if (verbose == 1 || verbose == 11) {
										//indicate there was initially page fault and 
										//the frame table had empty frame, to which we loaded the wanted page
										System.out.printf("%d references word %d (page %d) at time %d: Fault, using free frame %d\n",currP.getProcessNum(),word,pageNumber,time,k);
									}
									
									break;
								} 
								
							}
							
							
						} 
						//ELSE, frame Table is FULL and MUST EVICT
						else {
							
							//increase the number of fault for the current process
							currP.increaseNumFaults();
							
							//next four variables will be used for eviction for all three replacement algorithms
							PageEntry pageToEvict = null;
							int indexEvict = -1;
							int pageNumBeingEvicted = -1;
							int numOfProcessBeingEvicted = -1;
							
							//if the algorithm being used is "LRU"
							if (r_replaceAlg.equalsIgnoreCase("lru")) {
								pageToEvict = listLRU.remove(0); //get the page to be evicted
								indexEvict = frameTable.indexOf(pageToEvict); //get the frame table index of the page to be evicted
								pageNumBeingEvicted = pageToEvict.getPageNumber(); //get the page number of the page to be evicted
								numOfProcessBeingEvicted = pageToEvict.getProcessNumber(); //get the process number of the page to be evicted
								
								
								//calculate the current residency time
								double residencyTime =  time - pageToEvict.getTimeLoaded();
								//get the process whose page is being evicted
								ProcessInfo processThatHasPageToEvict = finalList[pageToEvict.getProcessNumber()-1];
								//update that process's running sum of residency time
								processThatHasPageToEvict.addEviction(residencyTime);
								//increase that process's number of evictions
								processThatHasPageToEvict.increaseNumEvictions();
								//update the final list
								finalList[pageToEvict.getProcessNumber()-1] = processThatHasPageToEvict;
								

								//update loaded time of the page we want to add 
								wantedPage.setTimeLoaded(time);
								//store the wanted page into the index of the frame table where we just evicted page from
								frameTable.set(indexEvict, wantedPage);
								
								//if the wanted page is IN the listLRU, need to update listLRU
								if (listLRU.contains(wantedPage)) {
									//the wanted page is going to be used by the system
									//and needs to be sent to the back of the listLRU
									
									//so first get the index of the wantedPage in listLRU
									int indexRm = listLRU.indexOf(wantedPage);
									
									//then remove it from that index and add to end of listLRU
									listLRU.add(listLRU.remove(indexRm));
								}
								//else if the wanted page is NOT in the listLRU
								else {
									//the wanted page is going to be used by the system
									//so add to the end of the listLRU
									listLRU.add(wantedPage);
								}
								
								//check for the debug flag to show what the system is doing
								if (verbose == 1 || verbose == 11) {
									//indicate there was initially page fault and the frame table was FULL,
									// so we found a frame to evict the old page and put the page the system wants
									System.out.printf("\n%d references word %d (page %d) at time %d: Fault, evicting page %d of %d from frame %d\n",currP.getProcessNum(),word,pageNumber,time,pageNumBeingEvicted,numOfProcessBeingEvicted,indexEvict);
								}
								
							} //end of eviction using LRU replacement algorithm
							
							//if the algorithm being used is "FIFO"
							else if (r_replaceAlg.equalsIgnoreCase("fifo")) {
								//pop the page to evict from the head of the queueFIFO
								pageToEvict = queueFIFO.poll();
								if (pageToEvict != null) { //check that the pageToEvict is NOT null

									indexEvict = frameTable.indexOf(pageToEvict); //get the frame table index of the page to be evicted
									pageNumBeingEvicted = pageToEvict.getPageNumber(); //get the page number of the page to be evicted
									numOfProcessBeingEvicted = pageToEvict.getProcessNumber(); //get the process number of the page to be evicted
									
									
									//calculate the current residency time
									double residencyTime =  time - pageToEvict.getTimeLoaded();
									//get the process whose page is being evicted
									ProcessInfo processThatHasPageToEvict = finalList[pageToEvict.getProcessNumber()-1];
									//update that process's running sum of residency time
									processThatHasPageToEvict.addEviction(residencyTime);
									//increase that process's number of evictions
									processThatHasPageToEvict.increaseNumEvictions();
									//update the final list
									finalList[pageToEvict.getProcessNumber()-1] = processThatHasPageToEvict;
									
									
									//update loaded time of the page we want to add 
									wantedPage.setTimeLoaded(time);
									//store the wanted page into the index of the frame table where we just evicted page from
									frameTable.set(indexEvict, wantedPage);
									//the wanted page is going to be used by the system
									// so add to the end of the queueFIFO
									queueFIFO.add(wantedPage);
									
									//check for the debug flag to show what the system is doing
									if (verbose == 1 || verbose == 11) {
										//indicate there was initially page fault and the frame table was FULL,
										// so we found a frame to evict the old page and put the page the system wants
										System.out.printf("\n%d references word %d (page %d) at time %d: Fault, evicting page %d of %d from frame %d\n",currP.getProcessNum(),word,pageNumber,time,pageNumBeingEvicted,numOfProcessBeingEvicted,indexEvict);
									}			
									
								} //end of checking if the pageToEvict is NOT null
							} //end of eviction using FIFO replacement algorithm
							
							//if the algorithm being used is "RANDOM"
							else if (r_replaceAlg.equalsIgnoreCase("random")) {
								//generate a random number from the "random-numbers" file
								int randomNum = Integer.parseInt(fileStream.remove(0));
								
								//check for the debug flag to show random number used
								if (verbose == 11) {
									System.out.printf("\n%d uses random number for eviction: %d", currP.getProcessNum(), randomNum);
								}
								
								//get the frame table index of the page to be evicted using the random number generated
								indexEvict = (randomNum + frameTable.size()) % frameTable.size(); 
								pageToEvict = frameTable.get(indexEvict); //get the page to be evicted using the index calculated								
								pageNumBeingEvicted = pageToEvict.getPageNumber(); //get the page number of the page to be evicted
								numOfProcessBeingEvicted = pageToEvict.getProcessNumber(); //get the process number of the page to be evicted								
								
								
								//calculate the current residency time
								double residencyTime =  time - pageToEvict.getTimeLoaded();
								//get the process whose page is being evicted
								ProcessInfo processThatHasPageToEvict = finalList[pageToEvict.getProcessNumber()-1];
								//update that process's running sum of residency time
								processThatHasPageToEvict.addEviction(residencyTime);
								//increase that process's number of evictions
								processThatHasPageToEvict.increaseNumEvictions();
								//update the final list
								finalList[pageToEvict.getProcessNumber()-1] = processThatHasPageToEvict;
								
								
								//update loaded time of the page we want to add 
								wantedPage.setTimeLoaded(time);								
								//store the wanted page into the index of the frame table where we just evicted page from
								frameTable.set(indexEvict, wantedPage);
								
								//check for the debug flag to show what the system is doing
								if (verbose == 1 || verbose == 11) {
									//indicate there was initially page fault and the frame table was FULL,
									// so we found a frame to evict the old page and put the page the system wants
									System.out.printf("\n%d references word %d (page %d) at time %d: Fault, evicting page %d of %d from frame %d\n",currP.getProcessNum(),word,pageNumber,time,pageNumBeingEvicted,numOfProcessBeingEvicted,indexEvict);
								}	
								
							} //end of eviction using RANDOM replacement algorithm
						}
						
						//update the finalList
						finalList[currP.getProcessNum()-1] = currP;
						
					} //end of resolving the PAGE FAULT that occurred
					

					//since we resolved the reference for current process, 
					// decrease the number of references for current process
					currP.decreaseNumRef();
					
					
					//SECOND, calculate the next reference for this process
					double y = randomNum(currP.getProcessNum());
					//System.out.printf("This is y: %f\n", y);
					if (y < currP.getAProb()) {
						//System.out.println("do case 1");
						word = (word + 1 + s_processSize ) % s_processSize;
					} else if (y < (currP.getAProb() + currP.getBProb()) ) {
						//System.out.println("do case 2");
						word = (word - 5 + s_processSize ) % s_processSize;
					} else if (y < (currP.getAProb() + currP.getBProb() + currP.getCProb()) ) {
						//System.out.println("do case 3");
						word = (word + 4 + s_processSize ) % s_processSize;
					} else if (y >= (currP.getAProb() + currP.getBProb() + currP.getCProb()) ) {
						//System.out.println("do case 4, FIGURE OUT HOW TO USE ANOTHER RANDOM # to generate the next word!");						
						
						//generate a random number from the "random-numbers" file
						int randomNum = Integer.parseInt(fileStream.remove(0));
						
						//check for the debug flag to show random number used
						if (verbose == 11) {
							System.out.printf("%d uses random number: %d\n", currP.getProcessNum(), randomNum);
						}
						word = randomNum % s_processSize;
					}
					//set the word we just got as the word to be referenced next for the current process
					currP.setWord(word);
					//update the finalList
					finalList[currP.getProcessNum()-1] = currP;
					
					//check if the current process resolved all the references for the current process
					if (currP.getNumRef() == 0) {
						//first update the finalList
						finalList[currP.getProcessNum()-1] = currP;
						//then pop the process from the current processes list
						processes.remove(currP);
						i --; //do this so you are reading the correct process
						ref = 4; //do this to exit out of the for loop for keeping the quantum (of 3)
					}
					time ++; //increase the time (measured in memory references)
					
				} //end of the for loop for keeping the quantum (of 3)
				
			} //end of the for loop for going through each process in the list of processes
			
		} //end of while loop
		
		
		int totalFaults = 0;
		double totalResidency = 0.0;
		int totalEvictions = 0;
		
		System.out.println();
		for (ProcessInfo p: finalList) {
			//calculate the average residency time for each process
			double avgR = p.getSumEvictions()/p.getNumEvictions();
			
			totalFaults += p.getNumFaults(); //add each process number of faults
			totalResidency += p.getSumEvictions(); //add each process running sum
			totalEvictions += p.getNumEvictions(); //add each process number of evictions
			
			//check if a process had no evictions
			if (p.getNumEvictions() == 0) {
				System.out.printf("Process %d had %d faults. \n\tWith no evictions, the average residence is undefined.\n", p.getProcessNum(),p.getNumFaults());

			} else {
				System.out.printf("Process %d had %d faults and %f average residency.\n", p.getProcessNum(),p.getNumFaults(),avgR);
			}
		}
		
		double overallAvgResidency = totalResidency / totalEvictions;
		//check if there was no eviction overall
		if (totalEvictions == 0) {
			System.out.printf("\nThe total number of faults is %d. \n\tWith no evictions, the overall average residence is undefined.\n\n", totalFaults);

		} else {
			System.out.printf("\nThe total number of faults is %d and the overall average residency is %f.\n\n", totalFaults,overallAvgResidency);
		}
		
	} //end of main method

}
