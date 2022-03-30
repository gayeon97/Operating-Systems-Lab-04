import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Paging_version4 {
	public static int globalIndex = 0;
	public static int verbose = -1;
	public static int quantum = 3;
	public static List<String> fileStream = null;
	public static boolean isFTFull = false;
	public static int highestIndex = -1;
	//public static boolean isFTEmpty = false;
	
	static double randomNum(int processNum) throws FileNotFoundException {
		int randomNumFromFile = Integer.parseInt(fileStream.remove(0));
		if (verbose == 11) {
			System.out.printf("%d uses random number: %d\n", processNum, randomNumFromFile);
		}
		return randomNumFromFile / (Integer.MAX_VALUE + 1d);
	}
	
	public static int pageHit(PageEntry[] frameTable, PageEntry wantedPage, String replaceAlg, ArrayList<PageEntry> listLRU) {
//		if (frameTable.indexOf() == -1) {
//			
//		}
		
		//int index = -1;
		boolean foundHighestIndex = false;
		//isFTEmpty = true;
		for (int j = frameTable.length -1; j >=0; j --) {
			PageEntry currFrame = frameTable[j];
			//check if there is a page entry at this index of the frame table
			if (currFrame != null ) {
				//isFTEmpty = false;
				//that page entry is EQUAL to the wanted page --> page hit!
				if (wantedPage.equals(currFrame)) {
					//make sure the time loaded for the wanted Page is consistent 
					//with the matching frame in the frame table
					wantedPage.setTimeLoaded(currFrame.getTimeLoaded());
					
					//update the listLRU if the replacement algorithm is LRU
					if (replaceAlg.equalsIgnoreCase("lru")) {
						//if the wanted page is IN the listLRU
						if (listLRU.contains(wantedPage)) {
							//the wanted page is going to be used by the system
							//and needs to be sent to the back of the listLRU
							
							//so first get the index of the wantedPage in listLRU
							int indexRm = listLRU.indexOf(wantedPage);
							
							//then remove it from that index and add to end of listLRU
							listLRU.add(listLRU.remove(indexRm));
						}
						//if the wanted page is NOT in the listLRU
						else {
							//the wanted page is going to be used by the system
							//so add to the end of the listLRU
							listLRU.add(wantedPage);
						}
					}
					
					//there was a page hit and we return the index of the 
					//frame containing the wanted page 
					return j;
				} 
			} 
			//else the frame is empty
			else {
				if (!foundHighestIndex && highestIndex < j) {
					highestIndex = j;
					foundHighestIndex = true;
				}
			}
		}
		
		//there was a page fault so we return -1
		return -1;
	}
	
	public static int add(PageEntry[] frameTable, PageEntry pageToPut, int time, String replaceAlg, ArrayList<PageEntry> listLRU, Queue<PageEntry> listFIFO) {
		
		for (int k = frameTable.length -1; k >=0; k --) {
			PageEntry currFrame = frameTable[k];
			//check if there is a page entry at this index of the frame table
			if (currFrame == null ) {
				pageToPut.setTimeLoaded(time);
				frameTable[k] = pageToPut;
				
				if (replaceAlg.equalsIgnoreCase("lru")) {
					//if the wanted page is IN the listLRU
					if (listLRU.contains(pageToPut)) {
						//the wanted page is going to be used by the system
						//and needs to be sent to the back of the listLRU
						
						//so first get the index of the wantedPage in listLRU
						int indexRm = listLRU.indexOf(pageToPut);
						
						//then remove it from that index and add to end of listLRU
						listLRU.add(listLRU.remove(indexRm));
					}
					//if the wanted page is NOT in the listLRU
					else {
						//the wanted page is going to be used by the system
						//so add to the end of the listLRU
						listLRU.add(pageToPut);
					}
					
				} else if (replaceAlg.equalsIgnoreCase("fifo")) {
					listFIFO.add(pageToPut);
				} 
				return k;
			} 
		}
		
		return -1;	
		
//		if (highestIndex >=0) {
//			pageToPut.setTimeLoaded(time);
//			frameTable[highestIndex] = pageToPut;
//			
//			if (replaceAlg.equalsIgnoreCase("lru")) {
//				//if the wanted page is IN the listLRU
//				if (listLRU.contains(pageToPut)) {
//					//the wanted page is going to be used by the system
//					//and needs to be sent to the back of the listLRU
//					
//					//so first get the index of the wantedPage in listLRU
//					int indexRm = listLRU.indexOf(pageToPut);
//					
//					//then remove it from that index and add to end of listLRU
//					listLRU.add(listLRU.remove(indexRm));
//				}
//				//if the wanted page is NOT in the listLRU
//				else {
//					//the wanted page is going to be used by the system
//					//so add to the end of the listLRU
//					listLRU.add(pageToPut);
//				}
//				
//			} else if (replaceAlg.equalsIgnoreCase("fifo")) {
//				listFIFO.add(pageToPut);
//			} 
//			highestIndex --;
//			return highestIndex + 1;
//		}
//		return -1;
	}
	
	public static int evict() {
		return -1;
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
		} else {
			System.err.printf("Invalid number of command line arguments: %d. Please try again.\n", args.length);
			System.exit(0);
		}
		
		System.out.printf("\nThe machine size is %d.\n", m_machineSize);
		System.out.printf("The page size is %d.\n", p_pageSize);
		System.out.printf("The process size is %d.\n", s_processSize);
		System.out.printf("The job mix number is %d.\n", j_jobmix);
		System.out.printf("The number of references per process is %d.\n", n_numRef);
		System.out.printf("The replacement algorithm is %s.\n", r_replaceAlg);
		System.out.printf("The level of debugging output is %s.\n\n", verbose);
		
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
					a = 1.0;
				} 
				
				//create four processes with corresponding a, b, and c probabilities
				for (int i = 1; i <= 4; i ++) {
					processes.add(new ProcessInfo(i, a, b, c, s_processSize, n_numRef));
				} //end of for loop
			} //end of inner else
		} //end of else for j_jobmix
		
		for (ProcessInfo e: processes) {
			System.out.printf("\nFor process %d,\n", e.getProcessNum());
			System.out.printf("\tThis is the a percent: %f\n", e.getAProb());
			System.out.printf("\tThis is the b percent: %f\n", e.getBProb());
			System.out.printf("\tThis is the c percent: %f\n", e.getCProb());
			System.out.printf("\tThis is the random percent: %f\n", e.getRandProb());
			System.out.printf("\tThis is the number of references: %d\n", e.getNumRef());
		}
		System.out.println();
		
		//this array will be used to store the processes that are finished
		ProcessInfo[] finalList = new ProcessInfo[processes.size()];
		//this array will represent the frame table of size M/P
		PageEntry[] frameTable = new PageEntry[m_machineSize/p_pageSize];
		//this arraylist will be used to keep track of least recently used PageEntry
		ArrayList<PageEntry> listLRU = new ArrayList<>();
		//this queue will be used to for FIFO replacement algorithm
		Queue<PageEntry> listFIFO = new LinkedList<>();
		int word = -1; //represents the word referenced
		int time = 1; //represents time measured in memory references
//		int counter = 0;
		//while (counter < 14) {
		while (!processes.isEmpty()) {
			for (int i = 0; i < processes.size(); i ++) {
				//get the process from the list of processes
				ProcessInfo currP = processes.get(i);
				
				if (currP.isFirstReference()) {
					//System.out.println("the process size: " + s_processSize);
					currP.setWord(((111 * currP.getProcessNum()) + s_processSize) % s_processSize);
					currP.didFirstReference();	
					//System.out.println("at first, word is: "+ e.getWordReferenced());
				}
				
				//this is the word being referenced
				word = currP.getWord();
								
				//get the page wanted for the word being referenced
				int pageNumber = word / p_pageSize;
				PageEntry wantedPage = new PageEntry(currP.getProcessNum(), pageNumber);

				for (int ref = 0; ref < 3; ref ++) {
					//simulate this reference for this process
					//check for page hit
					int pageFound = pageHit(frameTable, wantedPage, r_replaceAlg, listLRU);
					if (pageFound >= 0) {
						System.out.printf("\n%d references word %d (page..) at time %d: Hit in frame %d\n",currP.getProcessNum(),word,time,pageFound);
						
					} else {
						int framedAddedorUsed = add(frameTable, wantedPage, time, r_replaceAlg, listLRU, listFIFO);
						
						System.out.printf("\n%d references word %d (page..) at time %d: Fault, using free frame %d\n",currP.getProcessNum(),word,time,framedAddedorUsed);
					}
					
					
					currP.decreaseNumRef();
					System.out.println("the remaining references: " + currP.getNumRef());
					
					//calculate the next reference for this process
					double y = randomNum(currP.getProcessNum());
					System.out.printf("This is y: %f\n", y);
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
						
						//globalIndex ++;
						int randomNum = Integer.parseInt(fileStream.remove(0));
						
						if (verbose == 11) {
							System.out.printf("%d uses random number: %d\n", currP.getProcessNum(), randomNum);
						}
						word = randomNum % s_processSize;
					}
					currP.setWord(word);
					//globalIndex ++;		
					
					//addressed all the references for the current process
					if (currP.getNumRef() == 0) {
						//pop the process from the current processes list
						finalList[currP.getProcessNum()-1] = currP;
						processes.remove(currP);
						i --;
						ref = 4;
					}
					time ++;
				}
				
				//System.out.printf("for next quantum, %d will reference word %d.\n\n",currP.getProcessNum(),currP.getWord());
			}
			
			//counter ++;
		} //end of while loop
		
		
		

	} //end of main method

}
