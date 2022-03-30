import java.nio.charset.Charset;

public class checking {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String pagingcode = "/**\n" + 
				" * This lab was produced by Gayeon Park on April 2020\n" + 
				" */\n" + 
				"\n" + 
				"import java.io.FileNotFoundException;\n" + 
				"import java.io.IOException;\n" + 
				"import java.nio.charset.Charset;\n" + 
				"import java.nio.file.Files;\n" + 
				"import java.nio.file.Paths;\n" + 
				"import java.util.*;\n" + 
				"\n" + 
				"public class Paging {\n" + 
				"	//these are the global variables used throughout this java program\n" + 
				"	public static int globalIndex = 0;\n" + 
				"	public static int verbose = -1;\n" + 
				"	public static int quantum = 3;\n" + 
				"	public static List<String> fileStream = null;\n" + 
				"	\n" + 
				"	//get the random number from the \"random-numbers\" file used\n" + 
				"	static double randomNum(int processNum) throws FileNotFoundException {\n" + 
				"		int randomNumFromFile = Integer.parseInt(fileStream.remove(0));\n" + 
				"		if (verbose == 11) {\n" + 
				"			System.out.printf(\"%d uses random number: %d\\n\", processNum, randomNumFromFile);\n" + 
				"		}\n" + 
				"		return randomNumFromFile / (Integer.MAX_VALUE + 1d);\n" + 
				"	}\n" + 
				"\n" + 
				"	public static void main(String[] args) throws FileNotFoundException {\n" + 
				"		int m_machineSize = 0;\n" + 
				"		int p_pageSize = 0;\n" + 
				"		int s_processSize = 0;\n" + 
				"		int j_jobmix = 0;\n" + 
				"		int n_numRef = 0;\n" + 
				"		String r_replaceAlg = \"\";\n" + 
				"		ArrayList<ProcessInfo> processes = new ArrayList<>(); \n" + 
				"		\n" + 
				"		//read the \"random-numbers\" file\n" + 
				"		try {\n" + 
				"			fileStream = Files.readAllLines(Paths.get(System.getProperty(\"user.dir\") + \"/random-numbers\"),Charset.forName(\"UTF-8\"));\n" + 
				"		} catch (IOException e) {\n" + 
				"			e.printStackTrace();\n" + 
				"		}\n" + 
				"		\n" + 
				"		//check if appropriate number of command line arguments were passed in\n" + 
				"		if (args.length >= 6) {\n" + 
				"			m_machineSize = Integer.parseInt(args[0]);\n" + 
				"			p_pageSize = Integer.parseInt(args[1]);\n" + 
				"			s_processSize = Integer.parseInt(args[2]);\n" + 
				"			j_jobmix = Integer.parseInt(args[3]);\n" + 
				"			n_numRef = Integer.parseInt(args[4]);\n" + 
				"			r_replaceAlg = args[5];\n" + 
				"			if (args.length == 7) {\n" + 
				"				verbose = Integer.parseInt(args[6]);\n" + 
				"			}\n" + 
				"		} else { //throw error and exit the program\n" + 
				"			System.err.printf(\"Invalid number of command line arguments: %d. Please try again.\\n\", args.length);\n" + 
				"			System.exit(0);\n" + 
				"		}\n" + 
				"		\n" + 
				"		//print the information read in from command line arguments, supplying description for each\n" + 
				"		System.out.printf(\"\\nThe machine size is %d.\\n\", m_machineSize);\n" + 
				"		System.out.printf(\"The page size is %d.\\n\", p_pageSize);\n" + 
				"		System.out.printf(\"The process size is %d.\\n\", s_processSize);\n" + 
				"		System.out.printf(\"The job mix number is %d.\\n\", j_jobmix);\n" + 
				"		System.out.printf(\"The number of references per process is %d.\\n\", n_numRef);\n" + 
				"		System.out.printf(\"The replacement algorithm is %s.\\n\", r_replaceAlg);\n" + 
				"		System.out.printf(\"The level of debugging output is %s.\\n\\n\", verbose);\n" + 
				"		\n" + 
				"		//populate the processes list depending on the jobmix size\n" + 
				"		if (j_jobmix == 1) {\n" + 
				"			processes.add(new ProcessInfo(1, 1.0, 0.0, 0.0, s_processSize, n_numRef));\n" + 
				"		} else {\n" + 
				"			//for j_jobmix == 3, all values for a, b, and c are 0.0\n" + 
				"			double a = 0.0;\n" + 
				"			double b = 0.0;\n" + 
				"			double c = 0.0;\n" + 
				"			\n" + 
				"			if (j_jobmix == 4) {\n" + 
				"				processes.add(new ProcessInfo(1, 0.75, 0.25, 0.0, s_processSize, n_numRef));\n" + 
				"				processes.add(new ProcessInfo(2, 0.75, 0.0, 0.25, s_processSize, n_numRef));\n" + 
				"				processes.add(new ProcessInfo(3, 0.75, 0.125, 0.125, s_processSize, n_numRef));\n" + 
				"				processes.add(new ProcessInfo(4, 0.5, 0.125, 0.125, s_processSize, n_numRef));\n" + 
				"			} else { //j_jobmix = 2 or 3\n" + 
				"				if (j_jobmix == 2) {\n" + 
				"					//for j_jobmix == 2, a = 1.0, b = 0.0, and c = 0.0\n" + 
				"					a = 1.0;\n" + 
				"				} \n" + 
				"				\n" + 
				"				//create four processes with corresponding a, b, and c probabilities\n" + 
				"				for (int i = 1; i <= 4; i ++) {\n" + 
				"					processes.add(new ProcessInfo(i, a, b, c, s_processSize, n_numRef));\n" + 
				"				} //end of for loop\n" + 
				"			} //end of inner else\n" + 
				"		} //end of else for j_jobmix\n" + 
				"		\n" + 
				"		//this array will be used to store the processes that are finished\n" + 
				"		ProcessInfo[] finalList = new ProcessInfo[processes.size()];\n" + 
				"		//this ArrayList will represent the frame table of size M/P\n" + 
				"		ArrayList<PageEntry> frameTable = new ArrayList<PageEntry>();\n" + 
				"		//this arraylist will be used to keep track of least recently used PageEntry\n" + 
				"		ArrayList<PageEntry> listLRU = new ArrayList<>();\n" + 
				"		//this queue will be used to for FIFO replacement algorithm\n" + 
				"		Queue<PageEntry> queueFIFO = new LinkedList<>();\n" + 
				"		int word = -1; //represents the word referenced\n" + 
				"		int time = 1; //represents time measured in memory references\n" + 
				"		\n" + 
				"		//initialize the frameTable with empty frames (<--represented by PageEntry(-1,-1))\n" + 
				"		for (int z= 0; z < (m_machineSize/p_pageSize); z ++) {\n" + 
				"			frameTable.add(new PageEntry(-1,-1));\n" + 
				"		}\n" + 
				"\n" + 
				"		//loop until the list of processes is empty\n" + 
				"		while (!processes.isEmpty()) {\n" + 
				"			//go through each process in the list of processes\n" + 
				"			for (int i = 0; i < processes.size(); i ++) {\n" + 
				"				//get the process from the list of processes\n" + 
				"				ProcessInfo currP = processes.get(i);\n" + 
				"				\n" + 
				"				//check if the current process is referenced for the first time ever\n" + 
				"				if (currP.isFirstReference()) {\n" + 
				"					//get the first word reference using formula: (111*processNumber) mod processSize\n" + 
				"					currP.setWord(((111 * currP.getProcessNum()) + s_processSize) % s_processSize);\n" + 
				"					currP.didFirstReference();	\n" + 
				"				}\n" + 
				"				\n" + 
				"				//this is the word being referenced\n" + 
				"				word = currP.getWord();\n" + 
				"		\n" + 
				"				//each process gets maximum of 3 quantum to resolve process references\n" + 
				"				for (int ref = 0; ref < 3; ref ++) {\n" + 
				"					//FIRST, simulate this reference for this process\n" + 
				"					//create the page wanted for the word being referenced\n" + 
				"					int pageNumber = word / p_pageSize; //calculate the page number for the page \n" + 
				"					PageEntry wantedPage = new PageEntry(currP.getProcessNum(), pageNumber);\n" + 
				"					\n" + 
				"					//CHECK FOR PAGE HIT\n" + 
				"					if (frameTable.indexOf(wantedPage) != -1) {\n" + 
				"						//resort the listLRU if the algorithm being used is \"LRU\"\n" + 
				"						if (r_replaceAlg.equalsIgnoreCase(\"lru\")) {\n" + 
				"							//check if the wanted page is IN the listLRU\n" + 
				"							if (listLRU.contains(wantedPage)) {\n" + 
				"								//the wanted page is going to be used by the system\n" + 
				"								//and needs to be sent to the back of the listLRU\n" + 
				"								\n" + 
				"								//so first get the index of the wantedPage in listLRU\n" + 
				"								int indexRm = listLRU.indexOf(wantedPage);\n" + 
				"								\n" + 
				"								//then remove it from that index and add to end of listLRU\n" + 
				"								listLRU.add(listLRU.remove(indexRm));\n" + 
				"							}\n" + 
				"							//else if the wanted page is NOT in the listLRU\n" + 
				"							else {\n" + 
				"								//the wanted page is going to be used by the system\n" + 
				"								//so add to the end of the listLRU\n" + 
				"								listLRU.add(wantedPage);\n" + 
				"							}	\n" + 
				"						}\n" + 
				"						\n" + 
				"						//update the finalList\n" + 
				"						finalList[currP.getProcessNum()-1] = currP;\n" + 
				"						\n" + 
				"						//check for the debug flag to show what the system is doing\n" + 
				"						if (verbose == 1 || verbose == 11) {\n" + 
				"							//indicate page hit by printing info\n" + 
				"							System.out.printf(\"\\n%d references word %d (page %d) at time %d: Hit in frame %d\\n\",currP.getProcessNum(),word,pageNumber,time,frameTable.indexOf(wantedPage));\n" + 
				"						}\n" + 
				"						\n" + 
				"					} \n" + 
				"					//ELSE PAGE FAULT HAS OCCURED\n" + 
				"					else {\n" + 
				"						\n" + 
				"						//CHECK if frame Table is NOT FULL yet, meaning there is space to put the page wanted\n" + 
				"						if (frameTable.indexOf(new PageEntry(-1,-1)) != -1) {\n" + 
				"							//go through the frame table, starting at the highest index\n" + 
				"							// because we must choose the highest numbered free frame\n" + 
				"							for (int k = frameTable.size() -1; k >=0; k --) {\n" + 
				"								PageEntry currFrame = frameTable.get(k);\n" + 
				"								\n" + 
				"								//check if the current frame is \"empty\" (empty frame is represented by PageEntry(-1,-1))\n" + 
				"								if (currFrame.equals(new PageEntry(-1,-1))) {\n" + 
				"									//System.out.println(\"PAGE FAULT OCCURED AND TABLE NOT FULL YET!\");\n" + 
				"									\n" + 
				"									//increase the number of fault for the current process\n" + 
				"									currP.increaseNumFaults();\n" + 
				"									\n" + 
				"									//update the time loaded for the wanted page\n" + 
				"									wantedPage.setTimeLoaded(time);\n" + 
				"									//System.out.printf(\"This is the time page %d of process %d was loaded: %d\\n\",wantedPage.getPageNumber(),wantedPage.getProcessNumber(), wantedPage.getTimeLoaded());\n" + 
				"\n" + 
				"									//store the wanted page into the correct highest && free index of the frame table\n" + 
				"									frameTable.set(k, wantedPage);\n" + 
				"									\n" + 
				"									//resort the listLRU if the algorithm being used is \"LRU\"\n" + 
				"									if (r_replaceAlg.equalsIgnoreCase(\"lru\")) {\n" + 
				"										//check if the wanted page is IN the listLRU\n" + 
				"										if (listLRU.contains(wantedPage)) {\n" + 
				"											//the wanted page is going to be used by the system\n" + 
				"											//and needs to be sent to the back of the listLRU\n" + 
				"											\n" + 
				"											//so first get the index of the wantedPage in listLRU\n" + 
				"											int indexRm = listLRU.indexOf(wantedPage);\n" + 
				"											\n" + 
				"											//then remove it from that index and add to end of listLRU\n" + 
				"											listLRU.add(listLRU.remove(indexRm));\n" + 
				"										}\n" + 
				"										//else if the wanted page is NOT in the listLRU\n" + 
				"										else {\n" + 
				"											//the wanted page is going to be used by the system\n" + 
				"											//so add to the end of the listLRU\n" + 
				"											listLRU.add(wantedPage);\n" + 
				"										}\n" + 
				"										\n" + 
				"									} \n" + 
				"									\n" + 
				"									//add the page to the the \"queueFIFO\" if the algorithm being used is \"FIFO\"\n" + 
				"									else if (r_replaceAlg.equalsIgnoreCase(\"fifo\")) {\n" + 
				"										queueFIFO.add(wantedPage);\n" + 
				"									} \n" + 
				"									\n" + 
				"									//update the finalList\n" + 
				"									finalList[currP.getProcessNum()-1] = currP;\n" + 
				"									\n" + 
				"									//check for the debug flag to show what the system is doing\n" + 
				"									if (verbose == 1 || verbose == 11) {\n" + 
				"										//indicate there was initially page fault and \n" + 
				"										//the frame table had empty frame, to which we loaded the wanted page\n" + 
				"										System.out.printf(\"%d references word %d (page %d) at time %d: Fault, using free frame %d\\n\",currP.getProcessNum(),word,pageNumber,time,k);\n" + 
				"									}\n" + 
				"									\n" + 
				"									break;\n" + 
				"								} \n" + 
				"								\n" + 
				"							}\n" + 
				"							\n" + 
				"							\n" + 
				"						} \n" + 
				"						//ELSE, frame Table is FULL and MUST EVICT\n" + 
				"						else {\n" + 
				"							\n" + 
				"							//increase the number of fault for the current process\n" + 
				"							currP.increaseNumFaults();\n" + 
				"							\n" + 
				"							//next four variables will be used for eviction for all three replacement algorithms\n" + 
				"							PageEntry pageToEvict = null;\n" + 
				"							int indexEvict = -1;\n" + 
				"							int pageNumBeingEvicted = -1;\n" + 
				"							int numOfProcessBeingEvicted = -1;\n" + 
				"							\n" + 
				"							//if the algorithm being used is \"LRU\"\n" + 
				"							if (r_replaceAlg.equalsIgnoreCase(\"lru\")) {\n" + 
				"								pageToEvict = listLRU.remove(0); //get the page to be evicted\n" + 
				"								indexEvict = frameTable.indexOf(pageToEvict); //get the frame table index of the page to be evicted\n" + 
				"								pageNumBeingEvicted = pageToEvict.getPageNumber(); //get the page number of the page to be evicted\n" + 
				"								numOfProcessBeingEvicted = pageToEvict.getProcessNumber(); //get the process number of the page to be evicted\n" + 
				"								\n" + 
				"								\n" + 
				"								//calculate the current residency time\n" + 
				"								double residencyTime =  time - pageToEvict.getTimeLoaded();\n" + 
				"								//get the process whose page is being evicted\n" + 
				"								ProcessInfo processThatHasPageToEvict = finalList[pageToEvict.getProcessNumber()-1];\n" + 
				"								//update that process's running sum of residency time\n" + 
				"								processThatHasPageToEvict.addEviction(residencyTime);\n" + 
				"								//increase that process's number of evictions\n" + 
				"								processThatHasPageToEvict.increaseNumEvictions();\n" + 
				"								//update the final list\n" + 
				"								finalList[pageToEvict.getProcessNumber()-1] = processThatHasPageToEvict;\n" + 
				"								\n" + 
				"\n" + 
				"								//update loaded time of the page we want to add \n" + 
				"								wantedPage.setTimeLoaded(time);\n" + 
				"								//store the wanted page into the index of the frame table where we just evicted page from\n" + 
				"								frameTable.set(indexEvict, wantedPage);\n" + 
				"								\n" + 
				"								//if the wanted page is IN the listLRU, need to update listLRU\n" + 
				"								if (listLRU.contains(wantedPage)) {\n" + 
				"									//the wanted page is going to be used by the system\n" + 
				"									//and needs to be sent to the back of the listLRU\n" + 
				"									\n" + 
				"									//so first get the index of the wantedPage in listLRU\n" + 
				"									int indexRm = listLRU.indexOf(wantedPage);\n" + 
				"									\n" + 
				"									//then remove it from that index and add to end of listLRU\n" + 
				"									listLRU.add(listLRU.remove(indexRm));\n" + 
				"								}\n" + 
				"								//else if the wanted page is NOT in the listLRU\n" + 
				"								else {\n" + 
				"									//the wanted page is going to be used by the system\n" + 
				"									//so add to the end of the listLRU\n" + 
				"									listLRU.add(wantedPage);\n" + 
				"								}\n" + 
				"								\n" + 
				"								//check for the debug flag to show what the system is doing\n" + 
				"								if (verbose == 1 || verbose == 11) {\n" + 
				"									//indicate there was initially page fault and the frame table was FULL,\n" + 
				"									// so we found a frame to evict the old page and put the page the system wants\n" + 
				"									System.out.printf(\"\\n%d references word %d (page %d) at time %d: Fault, evicting page %d of %d from frame %d\\n\",currP.getProcessNum(),word,pageNumber,time,pageNumBeingEvicted,numOfProcessBeingEvicted,indexEvict);\n" + 
				"								}\n" + 
				"								\n" + 
				"							} //end of eviction using LRU replacement algorithm\n" + 
				"							\n" + 
				"							//if the algorithm being used is \"FIFO\"\n" + 
				"							else if (r_replaceAlg.equalsIgnoreCase(\"fifo\")) {\n" + 
				"								//pop the page to evict from the head of the queueFIFO\n" + 
				"								pageToEvict = queueFIFO.poll();\n" + 
				"								if (pageToEvict != null) { //check that the pageToEvict is NOT null\n" + 
				"\n" + 
				"									indexEvict = frameTable.indexOf(pageToEvict); //get the frame table index of the page to be evicted\n" + 
				"									pageNumBeingEvicted = pageToEvict.getPageNumber(); //get the page number of the page to be evicted\n" + 
				"									numOfProcessBeingEvicted = pageToEvict.getProcessNumber(); //get the process number of the page to be evicted\n" + 
				"									\n" + 
				"									\n" + 
				"									//calculate the current residency time\n" + 
				"									double residencyTime =  time - pageToEvict.getTimeLoaded();\n" + 
				"									//get the process whose page is being evicted\n" + 
				"									ProcessInfo processThatHasPageToEvict = finalList[pageToEvict.getProcessNumber()-1];\n" + 
				"									//update that process's running sum of residency time\n" + 
				"									processThatHasPageToEvict.addEviction(residencyTime);\n" + 
				"									//increase that process's number of evictions\n" + 
				"									processThatHasPageToEvict.increaseNumEvictions();\n" + 
				"									//update the final list\n" + 
				"									finalList[pageToEvict.getProcessNumber()-1] = processThatHasPageToEvict;\n" + 
				"									\n" + 
				"									\n" + 
				"									//update loaded time of the page we want to add \n" + 
				"									wantedPage.setTimeLoaded(time);\n" + 
				"									//store the wanted page into the index of the frame table where we just evicted page from\n" + 
				"									frameTable.set(indexEvict, wantedPage);\n" + 
				"									//the wanted page is going to be used by the system\n" + 
				"									// so add to the end of the queueFIFO\n" + 
				"									queueFIFO.add(wantedPage);\n" + 
				"									\n" + 
				"									//check for the debug flag to show what the system is doing\n" + 
				"									if (verbose == 1 || verbose == 11) {\n" + 
				"										//indicate there was initially page fault and the frame table was FULL,\n" + 
				"										// so we found a frame to evict the old page and put the page the system wants\n" + 
				"										System.out.printf(\"\\n%d references word %d (page %d) at time %d: Fault, evicting page %d of %d from frame %d\\n\",currP.getProcessNum(),word,pageNumber,time,pageNumBeingEvicted,numOfProcessBeingEvicted,indexEvict);\n" + 
				"									}			\n" + 
				"									\n" + 
				"								} //end of checking if the pageToEvict is NOT null\n" + 
				"							} //end of eviction using FIFO replacement algorithm\n" + 
				"							\n" + 
				"							//if the algorithm being used is \"RANDOM\"\n" + 
				"							else if (r_replaceAlg.equalsIgnoreCase(\"random\")) {\n" + 
				"								//generate a random number from the \"random-numbers\" file\n" + 
				"								int randomNum = Integer.parseInt(fileStream.remove(0));\n" + 
				"								\n" + 
				"								//check for the debug flag to show random number used\n" + 
				"								if (verbose == 11) {\n" + 
				"									System.out.printf(\"\\n%d uses random number for eviction: %d\", currP.getProcessNum(), randomNum);\n" + 
				"								}\n" + 
				"								\n" + 
				"								//get the frame table index of the page to be evicted using the random number generated\n" + 
				"								indexEvict = (randomNum + frameTable.size()) % frameTable.size(); \n" + 
				"								pageToEvict = frameTable.get(indexEvict); //get the page to be evicted using the index calculated								\n" + 
				"								pageNumBeingEvicted = pageToEvict.getPageNumber(); //get the page number of the page to be evicted\n" + 
				"								numOfProcessBeingEvicted = pageToEvict.getProcessNumber(); //get the process number of the page to be evicted								\n" + 
				"								\n" + 
				"								\n" + 
				"								//calculate the current residency time\n" + 
				"								double residencyTime =  time - pageToEvict.getTimeLoaded();\n" + 
				"								//get the process whose page is being evicted\n" + 
				"								ProcessInfo processThatHasPageToEvict = finalList[pageToEvict.getProcessNumber()-1];\n" + 
				"								//update that process's running sum of residency time\n" + 
				"								processThatHasPageToEvict.addEviction(residencyTime);\n" + 
				"								//increase that process's number of evictions\n" + 
				"								processThatHasPageToEvict.increaseNumEvictions();\n" + 
				"								//update the final list\n" + 
				"								finalList[pageToEvict.getProcessNumber()-1] = processThatHasPageToEvict;\n" + 
				"								\n" + 
				"								\n" + 
				"								//update loaded time of the page we want to add \n" + 
				"								wantedPage.setTimeLoaded(time);								\n" + 
				"								//store the wanted page into the index of the frame table where we just evicted page from\n" + 
				"								frameTable.set(indexEvict, wantedPage);\n" + 
				"								\n" + 
				"								//check for the debug flag to show what the system is doing\n" + 
				"								if (verbose == 1 || verbose == 11) {\n" + 
				"									//indicate there was initially page fault and the frame table was FULL,\n" + 
				"									// so we found a frame to evict the old page and put the page the system wants\n" + 
				"									System.out.printf(\"\\n%d references word %d (page %d) at time %d: Fault, evicting page %d of %d from frame %d\\n\",currP.getProcessNum(),word,pageNumber,time,pageNumBeingEvicted,numOfProcessBeingEvicted,indexEvict);\n" + 
				"								}	\n" + 
				"								\n" + 
				"							} //end of eviction using RANDOM replacement algorithm\n" + 
				"						}\n" + 
				"						\n" + 
				"						//update the finalList\n" + 
				"						finalList[currP.getProcessNum()-1] = currP;\n" + 
				"						\n" + 
				"					} //end of resolving the PAGE FAULT that occurred\n" + 
				"					\n" + 
				"\n" + 
				"					//since we resolved the reference for current process, \n" + 
				"					// decrease the number of references for current process\n" + 
				"					currP.decreaseNumRef();\n" + 
				"					\n" + 
				"					\n" + 
				"					//SECOND, calculate the next reference for this process\n" + 
				"					double y = randomNum(currP.getProcessNum());\n" + 
				"					//System.out.printf(\"This is y: %f\\n\", y);\n" + 
				"					if (y < currP.getAProb()) {\n" + 
				"						//System.out.println(\"do case 1\");\n" + 
				"						word = (word + 1 + s_processSize ) % s_processSize;\n" + 
				"					} else if (y < (currP.getAProb() + currP.getBProb()) ) {\n" + 
				"						//System.out.println(\"do case 2\");\n" + 
				"						word = (word - 5 + s_processSize ) % s_processSize;\n" + 
				"					} else if (y < (currP.getAProb() + currP.getBProb() + currP.getCProb()) ) {\n" + 
				"						//System.out.println(\"do case 3\");\n" + 
				"						word = (word + 4 + s_processSize ) % s_processSize;\n" + 
				"					} else if (y >= (currP.getAProb() + currP.getBProb() + currP.getCProb()) ) {\n" + 
				"						//System.out.println(\"do case 4, FIGURE OUT HOW TO USE ANOTHER RANDOM # to generate the next word!\");						\n" + 
				"						\n" + 
				"						//generate a random number from the \"random-numbers\" file\n" + 
				"						int randomNum = Integer.parseInt(fileStream.remove(0));\n" + 
				"						\n" + 
				"						//check for the debug flag to show random number used\n" + 
				"						if (verbose == 11) {\n" + 
				"							System.out.printf(\"%d uses random number: %d\\n\", currP.getProcessNum(), randomNum);\n" + 
				"						}\n" + 
				"						word = randomNum % s_processSize;\n" + 
				"					}\n" + 
				"					//set the word we just got as the word to be referenced next for the current process\n" + 
				"					currP.setWord(word);\n" + 
				"					//update the finalList\n" + 
				"					finalList[currP.getProcessNum()-1] = currP;\n" + 
				"					\n" + 
				"					//check if the current process resolved all the references for the current process\n" + 
				"					if (currP.getNumRef() == 0) {\n" + 
				"						//first update the finalList\n" + 
				"						finalList[currP.getProcessNum()-1] = currP;\n" + 
				"						//then pop the process from the current processes list\n" + 
				"						processes.remove(currP);\n" + 
				"						i --; //do this so you are reading the correct process\n" + 
				"						ref = 4; //do this to exit out of the for loop for keeping the quantum (of 3)\n" + 
				"					}\n" + 
				"					time ++; //increase the time (measured in memory references)\n" + 
				"					\n" + 
				"				} //end of the for loop for keeping the quantum (of 3)\n" + 
				"				\n" + 
				"			} //end of the for loop for going through each process in the list of processes\n" + 
				"			\n" + 
				"		} //end of while loop\n" + 
				"		\n" + 
				"		\n" + 
				"		int totalFaults = 0;\n" + 
				"		double totalResidency = 0.0;\n" + 
				"		int totalEvictions = 0;\n" + 
				"		\n" + 
				"		System.out.println();\n" + 
				"		for (ProcessInfo p: finalList) {\n" + 
				"			//calculate the average residency time for each process\n" + 
				"			double avgR = p.getSumEvictions()/p.getNumEvictions();\n" + 
				"			\n" + 
				"			totalFaults += p.getNumFaults(); //add each process number of faults\n" + 
				"			totalResidency += p.getSumEvictions(); //add each process running sum\n" + 
				"			totalEvictions += p.getNumEvictions(); //add each process number of evictions\n" + 
				"			\n" + 
				"			//check if a process had no evictions\n" + 
				"			if (p.getNumEvictions() == 0) {\n" + 
				"				System.out.printf(\"Process %d had %d faults. \\n\\tWith no evictions, the average residence is undefined.\\n\", p.getProcessNum(),p.getNumFaults());\n" + 
				"\n" + 
				"			} else {\n" + 
				"				System.out.printf(\"Process %d had %d faults and %f average residency.\\n\", p.getProcessNum(),p.getNumFaults(),avgR);\n" + 
				"			}\n" + 
				"		}\n" + 
				"		\n" + 
				"		double overallAvgResidency = totalResidency / totalEvictions;\n" + 
				"		//check if there was no eviction overall\n" + 
				"		if (totalEvictions == 0) {\n" + 
				"			System.out.printf(\"\\nThe total number of faults is %d. \\n\\tWith no evictions, the overall average residence is undefined.\\n\\n\", totalFaults);\n" + 
				"\n" + 
				"		} else {\n" + 
				"			System.out.printf(\"\\nThe total number of faults is %d and the overall average residency is %f.\\n\\n\", totalFaults,overallAvgResidency);\n" + 
				"		}\n" + 
				"		\n" + 
				"	} //end of main method\n" + 
				"\n" + 
				"}\n" + 
				"";
		
		System.out.println("Paging.java is pure ASCII: " + Charset.forName("US-ASCII").newEncoder().canEncode(pagingcode));
		
		String bankercode = "import java.io.*;\n" + 
				"import java.util.*;\n" + 
				"\n" + 
				"public class Banker {\n" + 
				"\n" + 
				"	/**\n" + 
				"	 * This method sorts all the Tasks that were executed in the same time frame (namely, a cycle)\n" + 
				"	 * 	by the order the tasks were created.\n" + 
				"	 * So if there're 3 tasks in total, the 1st task will have an index of 0, \n" + 
				"	 * 	the 2nd one will have an index of 1, and the 3rd task will have an index of 2.\n" + 
				"	 * So the ordering is done by the ascending order of the index of the task.\n" + 
				"	 * No two tasks have same index because that would mean that those two are actually the same task.\n" + 
				"	 * @param executedT is an ArrayList of Tasks that contains all the Tasks whose activity was executed during a cycle\n" + 
				"	 */\n" + 
				"	static void sortExecutedTasksList(ArrayList<Task> executedT) {\n" + 
				"		Collections.sort(executedT, new Comparator<Task>() {\n" + 
				"			@Override\n" + 
				"			public int compare(Task o1, Task o2) { //compare two Tasks, o1 and o2	\n" + 
				"				if (o1.getIndex() > o2.getIndex()) { //if the o1's index is greater than the o2's index\n" + 
				"					return 1;\n" + 
				"				} else if (o1.getIndex() < o2.getIndex()) { //if the o1's index is less than the o2's index\n" + 
				"					return -1;\n" + 
				"				} else { //if the o1's index is equal to the o2's index\n" + 
				"					return 0;\n" + 
				"				}\n" + 
				"			}		\n" + 
				"		});	\n" + 
				"	}\n" + 
				"\n" + 
				"	/**\n" + 
				"	 * This main method is where all the action happens: \n" + 
				"	 * 	The file is read from the command line argument, which is the name of the file containing the input.\n" + 
				"	 * 	The optimistic resource manager and the Banker's algorithm are implemented here.\n" + 
				"	 * 	The output is generated as standard output for both the Optimistic resource manager and the Banker algorithm.\n" + 
				"	 * @param args are the command line arguments passed in when running this Java program\n" + 
				"	 */\n" + 
				"	public static void main(String[] args) {		\n" + 
				"\n" + 
				"		int numTasks = 0; //represents the number of Tasks in the system\n" + 
				"		int numResources = 0; //represents the number of resource types\n" + 
				"		ArrayList<Integer> listOfUnitsOfEachResource = new ArrayList<>(); //represents the list of units of each resource type available\n" + 
				"		ArrayList<Integer> listOfReturnedUnitsOfEachResource = new ArrayList<>(); //represents the list of units returned of each resource type\n" + 
				"		ArrayList<Task> tasksOptimistic = new ArrayList<>(); //represents the list of tasks in the system for the Optimistic resource manager\n" + 
				"		ArrayList<Task> tasksBanker = new ArrayList<>(); //represents the list of tasks in the system for the Banker's algorithm\n" + 
				"		ArrayList<Task> tasksFinalizedOpt = new ArrayList<>(); //represents the finalized list of tasks in the system after Opt. resource manager finishes\n" + 
				"		ArrayList<Task> tasksFinalizedBkr = new ArrayList<>(); //represents the finalized list of tasks in the system after Banker's algorithm finishes\n" + 
				"		ArrayList<Task> executedTasks = new ArrayList<>(); //represents a list of tasks that was executed during the current cycle\n" + 
				"		int cycle = 0; //represents the fixed unit of time. So 1 cycle means 1 unit of time has passed\n" + 
				"		int totalTime = 0; //represents the total time it took for all Tasks to terminate\n" + 
				"		int totalWaitingTime = 0; //represents the total waiting time of all Tasks\n" + 
				"		String errorOpt = \"\"; //represents the error generated from the Optimistic resource manager\n" + 
				"		String errorBanker = \"\"; //represents the error generated from the Banker's Algorithm\n" + 
				"\n" + 
				"		Scanner sc = null;\n" + 
				"		//scanner is created from a file created from the name of the file passed as the 1st parameter to the command line argument\n" + 
				"		try {\n" + 
				"			sc = new Scanner(new File(args[0])); \n" + 
				"		} catch (Exception e){ //the file name was NOT passed in as the 1st parameter to the command line argument\n" + 
				"			System.out.println(\"Please make sure that you are giving the file name as a command line argument.\");\n" + 
				"		}\n" + 
				"\n" + 
				"		while (sc.hasNext()) { //the scanner reads from the file until there is no more content left to read	\n" + 
				"\n" + 
				"			//save T, the number of Tasks\n" + 
				"			numTasks = sc.nextInt();\n" + 
				"\n" + 
				"			//save R, the number of resource types\n" + 
				"			numResources = sc.nextInt();\n" + 
				"\n" + 
				"			//loop through R many times to save R additional values, the number of units present of each resource type\n" + 
				"			for (int i = 0; i < numResources; i ++) {\n" + 
				"				//for each resource type, save the number of units available into the \"listOfUnitsOfEachResource\"\n" + 
				"				listOfUnitsOfEachResource.add(sc.nextInt());\n" + 
				"\n" + 
				"				//for each resource type, we are also creating a returnedUnits variable that is of value 0 for now.\n" + 
				"				//we are storing in 0 because before the resource allocation, no resource has been allocated\n" + 
				"				// and hence there is 0 unit of each resource type returned\n" + 
				"				listOfReturnedUnitsOfEachResource.add(0);\n" + 
				"			}\n" + 
				"\n" + 
				"			//'T' many Tasks need to be made and stored into a list storing all the Tasks\n" + 
				"			for (int i = 0; i < numTasks; i ++) {\n" + 
				"				//create and add a new Task of index i to the arrayList of tasks for Optimistic resource manager\n" + 
				"				tasksOptimistic.add(new Task(i));\n" + 
				"\n" + 
				"				//create and add a new Task of index i to the arrayList of tasks for the Banker's Algorithm\n" + 
				"				tasksBanker.add(new Task(i));\n" + 
				"\n" + 
				"				//create and add a new Task of index i to the finalized arrayList of tasks for the Optimistic resource manager\n" + 
				"				//for now, an empty Task is created and later appropriate Task will be set to the appropriate index\n" + 
				"				tasksFinalizedOpt.add(new Task(i));\n" + 
				"\n" + 
				"				//create and add a new Task of index i to the finalized arrayList of tasks for the Banker's Algorithm\n" + 
				"				//for now, an empty Task is created and later appropriate Task will be set to the appropriate index\n" + 
				"				tasksFinalizedBkr.add(new Task(i));\n" + 
				"			}\n" + 
				"\n" + 
				"			//the first three numbers have been read, so until you reach the end of the file,\n" + 
				"			// you are reading in different Tasks' activity informations \n" + 
				"			while (sc.hasNext()) {				\n" + 
				"				//read the activity state (one of: initiate, request, release, terminate)\n" + 
				"				// and save it to a temporary String, tempActivity\n" + 
				"				String tempActivityType = sc.next();\n" + 
				"\n" + 
				"				//create an ArrayList of integers to store informations of the activity\n" + 
				"				ArrayList<Integer> activityInfo = new ArrayList<>();\n" + 
				"				//for the activity, save the four unsigned integers \n" + 
				"				// that represent the information of the activity to the 'activityInfo' ArrayList created above\n" + 
				"				for (int i = 0; i < 4; i ++) {\n" + 
				"					activityInfo.add(sc.nextInt());\n" + 
				"				}\n" + 
				"\n" + 
				"				//FOR OPTIMISTIC RESOURCE MANAGER\n" + 
				"				//get the correct task from the list of tasks that matches the 'task-number' of the current activity being read\n" + 
				"				Task tempTaskOp = tasksOptimistic.get(activityInfo.get(0) - 1);	\n" + 
				"				//create a new Activity pair (activityName, listOfActivityInfo) using HashMap\n" + 
				"				HashMap<String, ArrayList<Integer>> activityTemp0 = new HashMap<>();\n" + 
				"				activityTemp0.put(tempActivityType, activityInfo);\n" + 
				"				//save the new activity pair into the retrieved task's activities list\n" + 
				"				tempTaskOp.getActivities().add(activityTemp0);\n" + 
				"\n" + 
				"				//FOR BANKER'S ALGORITHM\n" + 
				"				//get the correct task from the list of tasks matching the 'task-number' of the current activity being read\n" + 
				"				Task tempTaskBnkr = tasksBanker.get(activityInfo.get(0) - 1);\n" + 
				"				//create a new Activity pair (activityName, listOfActivityInfo) using HashMap\n" + 
				"				HashMap<String, ArrayList<Integer>> activityTemp1 = new HashMap<>();\n" + 
				"				activityTemp1.put(tempActivityType, activityInfo);\n" + 
				"				//save the new activity pair into the retrieved task's activities list\n" + 
				"				tempTaskBnkr.getActivities().add(activityTemp1);\n" + 
				"\n" + 
				"				//get the list of claims for the Banker's algorithm\n" + 
				"				HashMap<Integer,Integer> claimsBnkr = tempTaskBnkr.getClaims();\n" + 
				"				//if the current activity type is \"initiate,\" save the initial claim of the current activity's resource type\n" + 
				"				if (tempActivityType.equals(\"initiate\")) {\n" + 
				"					int claimedResourceIndex = activityInfo.get(2)-1;\n" + 
				"					int claimedAmt = activityInfo.get(3);\n" + 
				"					claimsBnkr.put(claimedResourceIndex, claimedAmt);\n" + 
				"				}\n" + 
				"				//then set 'claimsBnkr' as the list of claims for the retrieved task\n" + 
				"				tempTaskBnkr.setClaims(claimsBnkr);\n" + 
				"\n" + 
				"			} //end of the inner while loop for reading the file\n" + 
				"\n" + 
				"		} //end of the outer while loop for reading the file\n" + 
				"\n" + 
				"\n" + 
				"		//FOR IMPLEMENTING THE OPTIMISTIC RESOURCE MANAGER\n" + 
				"		boolean taskExecuted = false; //used to indicate whether a Task has been executed (if Task tried to satisfy an activity)\n" + 
				"\n" + 
				"		//run until there is no more task remaining in the list of tasks for the Optimistic resource manager\n" + 
				"		while (!tasksOptimistic.isEmpty()) { \n" + 
				"			cycle ++; //increment to indicate time passing\n" + 
				"\n" + 
				"			//for each resources available, \n" + 
				"			for (int k = 0; k < listOfUnitsOfEachResource.size(); k ++) {\n" + 
				"				//add back the each returned amount from the previous run\n" + 
				"				listOfUnitsOfEachResource.set(k, listOfUnitsOfEachResource.get(k) + listOfReturnedUnitsOfEachResource.get(k));\n" + 
				"				//reset each returnedResources value to 0\n" + 
				"				listOfReturnedUnitsOfEachResource.set(k,0);\n" + 
				"			}\n" + 
				"\n" + 
				"			//go through the entire list of tasks\n" + 
				"			for (int i = 0; i < tasksOptimistic.size(); i ++) { \n" + 
				"\n" + 
				"				//get the current task\n" + 
				"				Task currTask = tasksOptimistic.get(i);\n" + 
				"\n" + 
				"				//get the first activity of the current task's activities list\n" + 
				"				HashMap<String, ArrayList<Integer>> currActivity = currTask.getActivities().get(0);\n" + 
				"\n" + 
				"				//if the task's activity is \"initiate\"\n" + 
				"				if (currActivity.containsKey(\"initiate\")) {\n" + 
				"					//get the ArrayList of information about the current activity\n" + 
				"					ArrayList<Integer> activityInfos = currActivity.get(\"initiate\"); \n" + 
				"\n" + 
				"					//the current task has been executed\n" + 
				"					taskExecuted = true;\n" + 
				"\n" + 
				"					//since the activity is initiate, the current task holds no resource of any type\n" + 
				"					//so we put value 0 for the number of units the task holds of the current resource type\n" + 
				"					currTask.getResourcesHeld().put((activityInfos.get(2))-1, 0);\n" + 
				"\n" + 
				"					//initiate has been granted, so remove the activity from the activities list\n" + 
				"					currTask.getActivities().remove(0);\n" + 
				"				} else {\n" + 
				"					//if the task's activity is \"request\"\n" + 
				"					if (currActivity.containsKey(\"request\")) {\n" + 
				"						//get the ArrayList of information about the current activity\n" + 
				"						ArrayList<Integer> activityInfos = currActivity.get(\"request\"); \n" + 
				"\n" + 
				"						int resourceIndex = activityInfos.get(2)-1; //represents the index of the resource requested by the task's activity\n" + 
				"						int resourceRequested = activityInfos.get(3); //represents the number of the resource requested by the task's activity\n" + 
				"						int resourceAvail = listOfUnitsOfEachResource.get(resourceIndex); //represents the available units of the resource requested\n" + 
				"\n" + 
				"						int requestDelay = currTask.getRequestdelay(); //represents the delay counter kept by the current task\n" + 
				"						int activityDelay = activityInfos.get(1); //represents the delay given by the current task's activity\n" + 
				"\n" + 
				"						//check if there are enough resources for the request to be satisfied\n" + 
				"						if (resourceRequested <= resourceAvail) {\n" + 
				"\n" + 
				"							//if there are enough resources & the requestDelay counter is equal to the activityDelay, \n" + 
				"							// the task's activity can be satisfied\n" + 
				"							if (requestDelay == activityDelay) {\n" + 
				"								\n" + 
				"								//the current task has been executed\n" + 
				"								taskExecuted = true;\n" + 
				"\n" + 
				"								//reset the requestDelay to 0\n" + 
				"								currTask.resetRequestdelay();\n" + 
				"\n" + 
				"								//first calculate the new amount of units held for the current resource as: \n" + 
				"								//      current value of resourceHeld + value of resourceRequested\n" + 
				"								//  then update the units held for the current resource as the sum of those two numbers (shown above) \n" + 
				"								currTask.getResourcesHeld().put(resourceIndex,currTask.getResourcesHeld().get(resourceIndex)+resourceRequested);\n" + 
				"\n" + 
				"								//calculate the remaining units available for the current resource type\n" + 
				"								int newResourceAvail = resourceAvail - resourceRequested;\n" + 
				"								//update the list that stores the available units of each resource type\n" + 
				"								listOfUnitsOfEachResource.set(resourceIndex, newResourceAvail);\n" + 
				"\n" + 
				"								//the request has been granted, so remove the activity from the activities list\n" + 
				"								currTask.getActivities().remove(0);\n" + 
				"\n" + 
				"							} else { //the delay counter is NOT equal to the delay given by the current task's activity, \n" + 
				"										//so it needs to wait for the delay to finish\n" + 
				"\n" + 
				"								//the current task has been executed\n" + 
				"								taskExecuted = true;\n" + 
				"\n" + 
				"								//increase the requestdelay counter\n" + 
				"								currTask.increaseRequestdelay();\n" + 
				"							}\n" + 
				"\n" + 
				"							//pop the current Task and add it to the executedTasks list\n" + 
				"							executedTasks.add(tasksOptimistic.remove(i));\n" + 
				"							i--; //do this so the order of reading from the list of tasks isn't disturbed\n" + 
				"\n" + 
				"						} else { //there AREN'T enough resources for the request to be satisfied, so wait\n" + 
				"							currTask.increaseWaitTime();\n" + 
				"						}\n" + 
				"					} //end of \"request\" handling\n" + 
				"\n" + 
				"					//if the task's activity is \"release\"\n" + 
				"					else if (currActivity.containsKey(\"release\")) {\n" + 
				"						//get the ArrayList of information about the current activity\n" + 
				"						ArrayList<Integer> activityInfos = currActivity.get(\"release\"); \n" + 
				"\n" + 
				"						int resourceIndex = activityInfos.get(2)-1; //represents the index of the resource released by the task's activity\n" + 
				"						int resourceReleased = activityInfos.get(3); //represents the number of the resource released by the task's activity\n" + 
				"						//represents the overall returned units of the current resource type\n" + 
				"						int resourceReturned = listOfReturnedUnitsOfEachResource.get(resourceIndex); \n" + 
				"\n" + 
				"						int releasedelay = currTask.getReleasedelay(); //represents the delay counter kept by the current task\n" + 
				"						int activitydelay = activityInfos.get(1); //represents the delay given by the current task's activity\n" + 
				"\n" + 
				"						//if the releaseDelay counter is EQUAL to the activityDelay, \n" + 
				"						// the task's activity can be satisfied\n" + 
				"						if (releasedelay == activitydelay) {\n" + 
				"							\n" + 
				"							//the current task has been executed\n" + 
				"							taskExecuted = true;\n" + 
				"\n" + 
				"							//reset the releasedelay to 0\n" + 
				"							currTask.resetReleasedelay();\n" + 
				"\n" + 
				"							//release the units held for the resource type specified\n" + 
				"							currTask.getResourcesHeld().put(resourceIndex,0);\n" + 
				"\n" + 
				"							//add the resource units released by the current task \n" + 
				"							// to the total units returned for the current resource type\n" + 
				"							resourceReturned += resourceReleased;\n" + 
				"							//then update the list that stores the total returned units of each resource type\n" + 
				"							listOfReturnedUnitsOfEachResource.set(resourceIndex, resourceReturned);\n" + 
				"\n" + 
				"							//request has been granted, so remove the activity from the activities list\n" + 
				"							currTask.getActivities().remove(0);\n" + 
				"\n" + 
				"						} else { //the releasedelay counter is NOT equal to the delay given by the current task's activity, \n" + 
				"									//so it needs to wait for the delay to finish\n" + 
				"\n" + 
				"							//the current task has been executed\n" + 
				"							taskExecuted = true;\n" + 
				"							\n" + 
				"							//increase the releasedelay counter\n" + 
				"							currTask.increaseReleasedelay();\n" + 
				"						}		\n" + 
				"\n" + 
				"						//in the case where the last 'release' activity of the current task has been done\n" + 
				"						//and there is only one activity remaining: TERMINATE, which does NOT require another cycle to complete\n" + 
				"						if (currTask.getActivities().size() == 1 ) {\n" + 
				"							//we get the final activity, 'terminate'\n" + 
				"							HashMap<String, ArrayList<Integer>> finalActivity = currTask.getActivities().get(0); \n" + 
				"							//get the info for 'terminate' activity\n" + 
				"							ArrayList<Integer> terminateInfo = finalActivity.get(\"terminate\"); \n" + 
				"\n" + 
				"							//if the task does NOT have to delay to terminate, DO TERMINATE\n" + 
				"							if (terminateInfo.get(1) == 0) {\n" + 
				"								//the current task has been executed\n" + 
				"								taskExecuted = true;\n" + 
				"\n" + 
				"								//set the current cycle as the time the current task terminated\n" + 
				"								currTask.setTimeTerminated(cycle);\n" + 
				"\n" + 
				"								//terminate has been granted, so remove the activity from the activities list\n" + 
				"								currTask.getActivities().remove(0);\n" + 
				"\n" + 
				"								//store the current task to the right index \n" + 
				"								// in the finalized list holding tasks for Opt. resource manager\n" + 
				"								tasksFinalizedOpt.set(currTask.getIndex(), currTask);\n" + 
				"								//remove the CURRENT TASK from the list of tasks since it is COMPLETED\n" + 
				"								tasksOptimistic.remove(currTask); \n" + 
				"								i--; //do this so the order of reading from the list of tasks isn't disturbed\n" + 
				"\n" + 
				"							} else { //the task DOES have to delay to terminate\n" + 
				"								\n" + 
				"								//the current task has been executed\n" + 
				"								taskExecuted = true;\n" + 
				"\n" + 
				"								//increase the terminatedelay counter for the current task\n" + 
				"								currTask.increaseTerminatedelay();\n" + 
				"\n" + 
				"								//pop the current Task and add it to the executedTasks list\n" + 
				"								executedTasks.add(tasksOptimistic.remove(i));\n" + 
				"								i--; //do this so the order of reading from the list of tasks isn't disturbed\n" + 
				"							}\n" + 
				"						} else { //in the case where there are still multiple activities left \n" + 
				"									//that are waiting to be satisfied for the current task\n" + 
				"							\n" + 
				"							//pop the current Task and add it to the executedTasks list\n" + 
				"							executedTasks.add(tasksOptimistic.remove(i));\n" + 
				"							i--; //do this so the order of reading from the list of tasks isn't disturbed\n" + 
				"						}\n" + 
				"					} //end of \"release\" handling\n" + 
				"\n" + 
				"					//if the task's activity is \"terminate\"\n" + 
				"					else {	\n" + 
				"						//get the ArrayList of information about the current activity\n" + 
				"						ArrayList<Integer> terminateInfo = currActivity.get(\"terminate\"); \n" + 
				"\n" + 
				"						//the current task has been executed\n" + 
				"						taskExecuted = true;\n" + 
				"\n" + 
				"						//does NOT have to delay to terminate, so DO TERMINATE\n" + 
				"						if (currTask.getTerminatedelay() == terminateInfo.get(1)) {\n" + 
				"							\n" + 
				"							//set the current cycle as the time the current task terminated\n" + 
				"							currTask.setTimeTerminated(cycle);\n" + 
				"\n" + 
				"							//terminate has been granted, so remove the activity from the activities list\n" + 
				"							currTask.getActivities().remove(0);\n" + 
				"							//store the current task to the right index in the finalized list holding tasks for Opt. resource manager\n" + 
				"							tasksFinalizedOpt.set(currTask.getIndex(), currTask);\n" + 
				"							//remove the CURRENT TASK from the list of tasks since it is COMPLETED\n" + 
				"							tasksOptimistic.remove(currTask);\n" + 
				"							i--; //do this so the order of reading from the list of tasks isn't disturbed\n" + 
				"							\n" + 
				"						} else { //the task DOES have to delay to terminate\n" + 
				"							//increase the terminatedelay counter for the current task\n" + 
				"							currTask.increaseTerminatedelay();\n" + 
				"\n" + 
				"							//pop the current Task and add it to the executedTasks list\n" + 
				"							executedTasks.add(tasksOptimistic.remove(i));\n" + 
				"							i--; //do this so the order of reading from the list of tasks isn't disturbed\n" + 
				"						}\n" + 
				"					} //end of 'terminate' handling\n" + 
				"\n" + 
				"				} //end of handling activities other than 'initiate'				\n" + 
				"			} //end of the for loop for going through the entire list of tasks\n" + 
				"\n" + 
				"			//if more than one task executed in the current cycle\n" + 
				"			if (executedTasks.size() > 1) { \n" + 
				"				//sort the list of executed tasks according to the task's index\n" + 
				"				sortExecutedTasksList(executedTasks); \n" + 
				"			}\n" + 
				"\n" + 
				"			//add the sorted executed tasks to the end of the list of tasks\n" + 
				"			for (int z = 0; z < executedTasks.size(); z ++) { \n" + 
				"				tasksOptimistic.add(executedTasks.remove(z));\n" + 
				"				z --;\n" + 
				"			} //after this, the list 'executedTasks' should be empty\n" + 
				"\n" + 
				"			//check for deadlock\n" + 
				"			boolean canExecute = false;\n" + 
				"			//if not a single task executed and the cycle is > 1, \n" + 
				"			// meaning, some time has passed since the tasks' activities has been first executed\n" + 
				"			if ( !taskExecuted && cycle > 1) { //we encountered a deadlock!\n" + 
				"				//store the error message\n" + 
				"				errorOpt += String.format(\"A deadlock has occured at cycle %d!\\n\", cycle);\n" + 
				"				\n" + 
				"				//the counter for how many times a task had to be aborted\n" + 
				"				int keeper = 0;\n" + 
				"				//try to resolve the deadlock\n" + 
				"				while (!canExecute) {\n" + 
				"					//if the counter for the number of times a task had been aborted \n" + 
				"					// is greater than 0\n" + 
				"					if (keeper > 0) {\n" + 
				"						//the deadlock is remaining after aborting at least one task, so store additional error message\n" + 
				"						errorOpt += \"The deadlock still remains!\\n\";\n" + 
				"					}\n" + 
				"					\n" + 
				"					//GET the Task with the lowest index\n" + 
				"					//initializing the lowest index\n" + 
				"					int taskIndexToAbort = tasksOptimistic.get(0).getIndex(); \n" + 
				"					//the index of the Task with the lowest index WITHIN the list of tasks for Opt. resource manager\n" + 
				"					int indexInTasksOpt = 0; \n" + 
				"					\n" + 
				"					//go through the entire list of tasks for the Opt. resource manager to find the task with lowest index\n" + 
				"					for (int m = 1; m < tasksOptimistic.size(); m ++) {\n" + 
				"						int nextIndex = tasksOptimistic.get(m).getIndex();\n" + 
				"						\n" + 
				"						//if the currentTask's index is GREATER than the nextTask's index\n" + 
				"						if (taskIndexToAbort > nextIndex) { \n" + 
				"							//set the currentTask to the nextTask's index that is smaller\n" + 
				"							taskIndexToAbort = nextIndex; \n" + 
				"							//save the position of the updated currentTask's WITHIN the list of tasks for Opt. resource manager\n" + 
				"							indexInTasksOpt = m; \n" + 
				"						}\n" + 
				"					}\n" + 
				"					\n" + 
				"					//this is the task to be aborted\n" + 
				"					Task taskAbort = tasksOptimistic.get(indexInTasksOpt);\n" + 
				"					\n" + 
				"					//store the info for the task being aborted\n" + 
				"					errorOpt += String.format(\"Task %d is aborted and its resources are\\n\" + \n" + 
				"							\"available next cycle (%d-%d)\\n\",taskAbort.getTaskNum(),cycle,cycle+1);\n" + 
				"\n" + 
				"					//the task is to be aborted\n" + 
				"					taskAbort.doAbort();\n" + 
				"\n" + 
				"					//release the resources of the task to be aborted\n" + 
				"					HashMap<Integer,Integer> itsHeldRsrcs = taskAbort.getResourcesHeld();\n" + 
				"					for (int n = 0; n < itsHeldRsrcs.size(); n ++) {\n" + 
				"						//get the units held of the current resource type by the current task\n" + 
				"						int resourceHeld = itsHeldRsrcs.get(n); \n" + 
				"						//get the currently all returned units of the current resource type\n" + 
				"						int resourceReturned = listOfReturnedUnitsOfEachResource.get(n); \n" + 
				"\n" + 
				"						//add the resource release by the current task to the total units returned for the current resource\n" + 
				"						resourceReturned += resourceHeld;\n" + 
				"						//then update the list that stores the total returned units of each resource type\n" + 
				"						listOfReturnedUnitsOfEachResource.set(n, resourceReturned);\n" + 
				"					}\n" + 
				"\n" + 
				"					//store the current task to the right index in the finalized list holding tasks for Opt. resource manager\n" + 
				"					tasksFinalizedOpt.set(taskAbort.getIndex(),taskAbort);\n" + 
				"					//remove the CURRENT TASK from the list of tasks since it is ABORTED\n" + 
				"					tasksOptimistic.remove(indexInTasksOpt);\n" + 
				"\n" + 
				"					//go through the modified list of tasks to see if deadlock is resolved\n" + 
				"					for (int b = 0; b < tasksOptimistic.size(); b ++) {\n" + 
				"						Task t = tasksOptimistic.get(b);\n" + 
				"\n" + 
				"						HashMap<String, ArrayList<Integer>> activity0 = t.getActivities().get(0);\n" + 
				"						for (Map.Entry m : activity0.entrySet()) { \n" + 
				"							//get activity info of current task\n" + 
				"							ArrayList<Integer> info = activity0.get((String) m.getKey()); \n" + 
				"\n" + 
				"							//if the sum of the units of current resource type available \n" + 
				"							// and the units of current resource type released is enough to satisfy the current task's request\n" + 
				"							if (info.get(3) <= listOfReturnedUnitsOfEachResource.get(info.get(2)-1) + listOfUnitsOfEachResource.get(info.get(2)-1)) {\n" + 
				"								canExecute = true; //the deadlock is resolved and you can execute\n" + 
				"							}					\n" + 
				"						}						\n" + 
				"					} //end of the for loop for going through the modified list of tasks\n" + 
				"					\n" + 
				"					//increase the counter that keeps track of\n" + 
				"					// how many times a task had to be aborted\n" + 
				"					keeper ++;\n" + 
				"					\n" + 
				"				} //end of the while loop for resolving the deadlock\n" + 
				"\n" + 
				"			} else { //a Task has been running, i.e. NO deadlock\n" + 
				"				taskExecuted = false; //reset the taskExecuted to false for the next cycle\n" + 
				"			}\n" + 
				"			\n" + 
				"		} //end of the while loop FOR IMPLEMENTING THE OPTIMISTIC RESOURCE MANAGER\n" + 
				"\n" + 
				"		//sort the finalized list of tasks for the Optimistic resource manager by task's index\n" + 
				"		sortExecutedTasksList(tasksFinalizedOpt); \n" + 
				"		//print any error detected\n" + 
				"		System.out.println(\"\\n-----------------------------\\n\" + errorOpt);\n" + 
				"		System.out.printf(\"%14s\\n\",\"FIFO\");\n" + 
				"		//print out each Task info after the Optimistic resource manager finishes\n" + 
				"		for (Task ee: tasksFinalizedOpt) { //go through the sorted finalized list of tasks\n" + 
				"			//print each Task's info for the time taken, the waiting time, and the percentage of time spent waiting\n" + 
				"			ee.printTaskInfo(); \n" + 
				"			totalTime += ee.getTimeTerminated(); //add up all Task's time taken\n" + 
				"			totalWaitingTime += ee.getWaitTime(); //add up all Task's waiting time\n" + 
				"		}\n" + 
				"\n" + 
				"		//Print the total time for all tasks, the total waiting time, and the overall percentage of time spent waiting\n" + 
				"		System.out.printf(\"total %8d %4d %5d%s\",(int) totalTime,(int) totalWaitingTime,Math.round((totalWaitingTime/(totalTime*1.0)) * 100),\"%\");\n" + 
				"		System.out.println(\"\\n-----------------------------\");\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"		//FOR IMPLEMENTING THE BANKER'S RESOURCE MANAGER\n" + 
				"		//reset the shared variables to default value of 0\n" + 
				"		cycle = 0;\n" + 
				"		totalTime = 0;\n" + 
				"		totalWaitingTime = 0;\n" + 
				"		boolean isSafeState = true; //this variable is used for indicating if it is safe state or not\n" + 
				"\n" + 
				"		//run until there is no more task remaining in the list of tasks for the Banker's Algorithm\n" + 
				"		while (!tasksBanker.isEmpty()) { \n" + 
				"			cycle ++; //increment to indicate time passing\n" + 
				"\n" + 
				"			//for each resources available, \n" + 
				"			for (int k = 0; k < listOfUnitsOfEachResource.size(); k ++) {\n" + 
				"				//add back the each returned amount from the previous run\n" + 
				"				listOfUnitsOfEachResource.set(k, listOfUnitsOfEachResource.get(k) + listOfReturnedUnitsOfEachResource.get(k));\n" + 
				"				//reset each returnedResources value to 0\n" + 
				"				listOfReturnedUnitsOfEachResource.set(k,0);\n" + 
				"			}\n" + 
				"\n" + 
				"			//go through the entire list of tasks\n" + 
				"			for (int i = 0; i < tasksBanker.size(); i ++) { \n" + 
				"\n" + 
				"				//get the current task\n" + 
				"				Task currTask = tasksBanker.get(i);\n" + 
				"\n" + 
				"				//get the first activity of the current task's activities list\n" + 
				"				HashMap<String, ArrayList<Integer>> currActivity = currTask.getActivities().get(0);\n" + 
				"\n" + 
				"				//if the task's activity is \"initiate\"\n" + 
				"				if (currActivity.containsKey(\"initiate\")) {\n" + 
				"					//get the ArrayList of information about the current activity\n" + 
				"					ArrayList<Integer> activityInfos = currActivity.get(\"initiate\"); \n" + 
				"\n" + 
				"					//HANDLE the error where a tasks's initial claim EXCEEDS the resource present\n" + 
				"					//represents the index of the resource\n" + 
				"					int resourceIndex = activityInfos.get(2)-1; \n" + 
				"					//represents the available units of the resource claimed\n" + 
				"					int resourceAvail = listOfUnitsOfEachResource.get(resourceIndex); \n" + 
				"					\n" + 
				"					//represents the list of claims for the current task\n" + 
				"					HashMap<Integer,Integer> currentClaims = currTask.getClaims();\n" + 
				"					int resourceInitClaimed = currentClaims.get(resourceIndex);\n" + 
				"					//if the amount of current resource type claimed is less than or equal \n" + 
				"					// to the amount of the resource type available\n" + 
				"					if (resourceInitClaimed <= resourceAvail) {\n" + 
				"						//no error, proceed with granting the 'initiate' activity\n" + 
				"						//since the activity is initiate, the current task holds no resource of any type\n" + 
				"						//so we put value 0 for the number of units the task holds of each resource type\n" + 
				"						currTask.getResourcesHeld().put((activityInfos.get(2))-1, 0);\n" + 
				"\n" + 
				"						//initiate has been granted, so remove the activity from the activities list\n" + 
				"						currTask.getActivities().remove(0);\n" + 
				"						\n" + 
				"					} else { //error, bc the tasks's initial claim EXCEEDS the resource present\n" + 
				"						//the task is to be aborted\n" + 
				"						currTask.doAbort();\n" + 
				"						//store the current task to the right index in the finalized list holding tasks for Banker's Algorithm\n" + 
				"						tasksFinalizedBkr.set(currTask.getIndex(),currTask);\n" + 
				"						//remove the CURRENT TASK from the list of tasks since it is ABORTED\n" + 
				"						tasksBanker.remove(i);\n" + 
				"						i--; //do this so the order of reading from the list of tasks isn't disturbed\n" + 
				"\n" + 
				"						//then we store the information for the error that happened\n" + 
				"						// because the task's initial claim exceeded the resources present\n" + 
				"						errorBanker += String.format(\"Banker aborts Task %d before run begins:\\n\",currTask.getTaskNum());\n" + 
				"						errorBanker += String.format(\"\\tclaim for resource %d (%d) exceeds number of units present (%d)\\n\",resourceIndex+1,resourceInitClaimed,resourceAvail);\n" + 
				"					}\n" + 
				"\n" + 
				"				} else {\n" + 
				"					//if the task's activity is \"request\"\n" + 
				"					if (currActivity.containsKey(\"request\")) {\n" + 
				"						//get the ArrayList of information about the current activity\n" + 
				"						ArrayList<Integer> activityInfos = currActivity.get(\"request\"); \n" + 
				"\n" + 
				"						//get the list of claims for the current Task\n" + 
				"						HashMap<Integer,Integer> allClaims = currTask.getClaims();\n" + 
				"\n" + 
				"						//represents the index of the resource requested by the task's activity\n" + 
				"						int resourceIndex = activityInfos.get(2)-1; \n" + 
				"						//represents the number of the resource requested by the task's activity\n" + 
				"						int resourceRequested = activityInfos.get(3); \n" + 
				"						//represents the available units of the resource requested\n" + 
				"						int resourceAvail = listOfUnitsOfEachResource.get(resourceIndex); \n" + 
				"						//represents the amount claimed of the current resource type by the current task\n" + 
				"						int resourceClaimed = allClaims.get(resourceIndex); \n" + 
				"\n" + 
				"						int requestDelay = currTask.getRequestdelay(); //represents the delay counter kept by the current task\n" + 
				"						int activityDelay = activityInfos.get(1); //represents the delay given by the current task's activity\n" + 
				"\n" + 
				"						//first, set isSafeState to TRUE\n" + 
				"						isSafeState = true;\n" + 
				"						//go through the entire list of claims for the current Task\n" + 
				"						// to check if it is a safe state for the current Task to run\n" + 
				"						for (Map.Entry m:currTask.getClaims().entrySet()) { \n" + 
				"							//represents the index of the current resource type claimed\n" + 
				"							int claimResourceIndex = ((Integer) m.getKey()).intValue();\n" + 
				"							//represents the number of claims of the current resource that the current Task has\n" + 
				"							int numClaims = ((Integer) m.getValue()).intValue();\n" + 
				"\n" + 
				"							//if the current task's claim for the current resource type EXCEEDS \n" + 
				"							// the number of resources available\n" + 
				"							if (numClaims > listOfUnitsOfEachResource.get(claimResourceIndex)) {\n" + 
				"								//set the safe state to FALSE\n" + 
				"								isSafeState = false;\n" + 
				"							}\n" + 
				"						} \n" + 
				"\n" + 
				"						//IF DON'T HAVE TO WAIT FOR DELAY\n" + 
				"						if (requestDelay == activityDelay) {			\n" + 
				"\n" + 
				"							//check if there are enough resources for the CLAIM to be satisfied\n" + 
				"							if (resourceClaimed <= resourceAvail) {\n" + 
				"\n" + 
				"								//check if it is safe state \n" + 
				"								if (isSafeState ) {\n" + 
				"									//YES, it is safe state to execute\n" + 
				"									\n" + 
				"									//check if the current task's resources requested DOES NOT EXCEED its claim\n" + 
				"									if (resourceRequested <= resourceClaimed) {\n" + 
				"										// THE REQUEST CAN BE GRANTED\n" + 
				"\n" + 
				"										//reset the requestDelay to 0\n" + 
				"										currTask.resetRequestdelay();\n" + 
				"\n" + 
				"										//calculate the claim remaining for the current requested resource type\n" + 
				"										resourceClaimed -= resourceRequested;\n" + 
				"										//update the list that stores the claims of each resource type\n" + 
				"										allClaims.put(resourceIndex, resourceClaimed);\n" + 
				"										//update the claims list of current task to the modified claims list from above\n" + 
				"										currTask.setClaims(allClaims);\n" + 
				"										\n" + 
				"										//first calculate the new amount of units held for the current resource as: \n" + 
				"										//      current value of resourceHeld + value of resourceRequested\n" + 
				"										//  then update the units held for the current resource as the sum of those two numbers (shown above) \n" + 
				"										currTask.getResourcesHeld().put(resourceIndex,currTask.getResourcesHeld().get(resourceIndex)+resourceRequested);\n" + 
				"\n" + 
				"										//calculate the remaining resources available for the Banker's Algorithm\n" + 
				"										int newResourceAvail = resourceAvail - resourceRequested;\n" + 
				"										//update the list that stores the available units of each resource type\n" + 
				"										listOfUnitsOfEachResource.set(resourceIndex, newResourceAvail);\n" + 
				"\n" + 
				"										//the request has been granted, so remove the activity from the activities list\n" + 
				"										currTask.getActivities().remove(0);\n" + 
				"\n" + 
				"										//pop the current Task and add it to the executedTasks list\n" + 
				"										executedTasks.add(tasksBanker.remove(i));\n" + 
				"										i--; //do this so the order of reading from the list of tasks isn't disturbed\n" + 
				"\n" + 
				"									} else { //the current task's resources requested DOES EXCEED its claim --> ERROR!\n" + 
				"\n" + 
				"										//the task is to be aborted\n" + 
				"										currTask.doAbort();\n" + 
				"\n" + 
				"										//create a string to store how many of each resource was released by the task being aborted\n" + 
				"										String errorInfo = \"\";\n" + 
				"										//release all the resources of the task to be aborted\n" + 
				"										HashMap<Integer,Integer> itsHeldRsrcs = currTask.getResourcesHeld();\n" + 
				"										for (int n = 0; n < itsHeldRsrcs.size(); n ++) {\n" + 
				"											//get the units held of the current resource type by the current task\n" + 
				"											int resourceHeld = itsHeldRsrcs.get(n); \n" + 
				"											//get the currently all returned units of the current resource type\n" + 
				"											int resourceReturned = listOfReturnedUnitsOfEachResource.get(n); \n" + 
				"\n" + 
				"											//add the resource released by the current task to the total units returned for the current resource\n" + 
				"											resourceReturned += resourceHeld;\n" + 
				"											listOfReturnedUnitsOfEachResource.set(n, resourceReturned);\n" + 
				"\n" + 
				"											//store how many units of which resource has been returned\n" + 
				"											errorInfo += String.format(\" %d unit(s) of resource %d\", resourceReturned, n+1);\n" + 
				"										}\n" + 
				"\n" + 
				"										//store the current task to the right index in the finalized list holding tasks for Opt. resource manager\n" + 
				"										tasksFinalizedBkr.set(currTask.getIndex(), currTask);\n" + 
				"										//remove the CURRENT TASK from the list of tasks since it is COMPLETED\n" + 
				"										tasksBanker.remove(currTask);\n" + 
				"\n" + 
				"										//then save the error that occured because\n" + 
				"										// the task's requests exceeded its claims during execution\n" + 
				"										errorBanker += String.format(\"During cycle %d-%d of Banker's algorithms\\n\",cycle-1,cycle);\n" + 
				"										errorBanker += String.format(\"\\tTask %d's request exceeds its claim; aborted;\",currTask.getTaskNum());\n" + 
				"										errorBanker += errorInfo;\n" + 
				"										errorBanker += \" available next cycle\\n\";\n" + 
				"									}\n" + 
				"									\n" + 
				"								} else { //NO, it is NOT safe state to execute\n" + 
				"									//so the request CANNOT be satisfied, so WAIT\n" + 
				"									//increase the wait time of the current task\n" + 
				"									currTask.increaseWaitTime();\n" + 
				"								}\n" + 
				"								\n" + 
				"							} else { //the claim is BIGGER than the resources available				\n" + 
				"								//so the request CANNOT be satisfied, so WAIT\n" + 
				"								//increase the wait time of the current task\n" + 
				"								currTask.increaseWaitTime();\n" + 
				"							}\n" + 
				"\n" + 
				"						} else { //the delay counter is NOT equal to the delay given by the current task's activity, \n" + 
				"									//so there is a DELAY and the current task needs to wait for the delay to finish\n" + 
				"\n" + 
				"							//increase the delay counter\n" + 
				"							currTask.increaseRequestdelay();\n" + 
				"\n" + 
				"							//pop the current Task and add it to the executedTasks list\n" + 
				"							executedTasks.add(tasksBanker.remove(i));\n" + 
				"							i--; //do this so the order of reading from the list of tasks isn't disturbed\n" + 
				"						}\n" + 
				"						\n" + 
				"					} //end of \"request\" handling\n" + 
				"\n" + 
				"					//if the task's activity is \"release\"\n" + 
				"					else if (currActivity.containsKey(\"release\")) {\n" + 
				"						//get the ArrayList of information about the current activity\n" + 
				"						ArrayList<Integer> activityInfos = currActivity.get(\"release\"); \n" + 
				"\n" + 
				"						//represents the index of the resource released by the task's activity\n" + 
				"						int resourceIndex = activityInfos.get(2)-1; \n" + 
				"						//represents the number of the resource released by the task's activity\n" + 
				"						int resourceReleased = activityInfos.get(3); \n" + 
				"						//represents the overall returned units of the resource type\n" + 
				"						int resourceReturned = listOfReturnedUnitsOfEachResource.get(resourceIndex); \n" + 
				"\n" + 
				"						int releasedelay = currTask.getReleasedelay(); //represents the delay counter kept by the current task\n" + 
				"						int activitydelay = activityInfos.get(1); //represents the delay given by the current task's activity\n" + 
				"\n" + 
				"						//if the releaseDelay counter is EQUAL to the activityDelay, the task's activity can be satisfied\n" + 
				"						if (releasedelay == activitydelay) {\n" + 
				"\n" + 
				"							//reset the releasedelay to 0\n" + 
				"							currTask.resetReleasedelay();\n" + 
				"\n" + 
				"							//get the task's current claim for the resource type that is being released\n" + 
				"							int currentClaim = currTask.getClaims().get(resourceIndex);\n" + 
				"							//recalculate the claim because resource has been released\n" + 
				"							currTask.getClaims().put(resourceIndex,currentClaim + resourceReleased);\n" + 
				"							\n" + 
				"							//release the units held for the resource type specified by the current task\n" + 
				"							currTask.getResourcesHeld().put(resourceIndex,0);\n" + 
				"\n" + 
				"							//add the resource units released by the current task \n" + 
				"							// to the total units returned for the current resource type\n" + 
				"							resourceReturned += resourceReleased;\n" + 
				"							listOfReturnedUnitsOfEachResource.set(resourceIndex, resourceReturned);\n" + 
				"\n" + 
				"							//request has been granted, so remove the activity from the activities list\n" + 
				"							currTask.getActivities().remove(0);\n" + 
				"\n" + 
				"						} else { //the releasedelay counter is NOT equal to the delay given by the current task's activity, \n" + 
				"									//so it needs to wait for the delay to finish\n" + 
				"							\n" + 
				"							//increase the delay counter\n" + 
				"							currTask.increaseReleasedelay();\n" + 
				"						}		\n" + 
				"\n" + 
				"						//in the case where the last 'release' activity of the current task has been done\n" + 
				"						//and there is only one activity remaining: TERMINATE, which does NOT require another cycle to complete\n" + 
				"						if (currTask.getActivities().size() == 1 ) {\n" + 
				"							//we get the final activity, 'terminate'\n" + 
				"							HashMap<String, ArrayList<Integer>> finalActivity = currTask.getActivities().get(0); \n" + 
				"							//get the info for 'terminate' activity\n" + 
				"							ArrayList<Integer> terminateInfo = finalActivity.get(\"terminate\"); \n" + 
				"\n" + 
				"							//if the task does NOT have to delay to terminate, DO TERMINATE\n" + 
				"							if (terminateInfo.get(1) == 0) {\n" + 
				"\n" + 
				"								//set the current cycle as the time the current task terminated\n" + 
				"								currTask.setTimeTerminated(cycle);\n" + 
				"\n" + 
				"								//terminate has been granted, so remove the activity from the activities list\n" + 
				"								currTask.getActivities().remove(0);\n" + 
				"\n" + 
				"								//store the current task to the right index in the finalized list holding tasks for Opt. resource manager\n" + 
				"								tasksFinalizedBkr.set(currTask.getIndex(), currTask);\n" + 
				"								//remove the CURRENT TASK from the list of tasks since it is COMPLETED\n" + 
				"								tasksBanker.remove(currTask); \n" + 
				"								i--; //do this so the order of reading from the list of tasks isn't disturbed\n" + 
				"\n" + 
				"							} else { //the task DOES have to delay to terminate\n" + 
				"\n" + 
				"								//increase the terminatedelay counter for the current task\n" + 
				"								currTask.increaseTerminatedelay();\n" + 
				"\n" + 
				"								//pop the current Task and add it to the executedTasks list\n" + 
				"								executedTasks.add(tasksBanker.remove(i));\n" + 
				"								i--; //do this so the order of reading from the list of tasks isn't disturbed\n" + 
				"							}\n" + 
				"						} else { //in the case where there are still multiple activities waiting to be satisfied for the current task\n" + 
				"							\n" + 
				"							//pop the current Task and add it to the executedTasks list\n" + 
				"							executedTasks.add(tasksBanker.remove(i));\n" + 
				"							i--; //do this so the order of reading from the list of tasks isn't disturbed\n" + 
				"						}\n" + 
				"					} //end of \"release\" handling\n" + 
				"\n" + 
				"					//if the task's activity is \"terminate\"\n" + 
				"					else {	\n" + 
				"						//get the ArrayList of information about the current activity\n" + 
				"						ArrayList<Integer> terminateInfo = currActivity.get(\"terminate\"); \n" + 
				"\n" + 
				"						//does NOT have to delay to terminate, so DO TERMINATE\n" + 
				"						if (currTask.getTerminatedelay() == terminateInfo.get(1)) {\n" + 
				"\n" + 
				"							//set the current cycle as the time the current task terminated\n" + 
				"							currTask.setTimeTerminated(cycle);\n" + 
				"\n" + 
				"							//terminate has been granted, so remove the activity from the activities list\n" + 
				"							currTask.getActivities().remove(0);\n" + 
				"							//store the current task to the right index in the finalized list holding tasks for Banker's Algorithm\n" + 
				"							tasksFinalizedBkr.set(currTask.getIndex(), currTask);\n" + 
				"							//remove the CURRENT TASK from the list of tasks since it is COMPLETED\n" + 
				"							tasksBanker.remove(currTask);\n" + 
				"							i--; //do this so the order of reading from the list of tasks isn't disturbed\n" + 
				"							\n" + 
				"						} else { //the task DOES have to delay to terminate\n" + 
				"\n" + 
				"							//increase the terminatedelay counter for the current task\n" + 
				"							currTask.increaseTerminatedelay();\n" + 
				"\n" + 
				"							//pop the current Task and add it to the executedTasks list\n" + 
				"							executedTasks.add(tasksBanker.remove(i));\n" + 
				"							i--; //do this so the order of reading from the list of tasks isn't disturbed\n" + 
				"						}\n" + 
				"					} //end of 'terminate' handling\n" + 
				"\n" + 
				"				} //end of handling activities other than 'initiate'				\n" + 
				"			} //end of the for loop for going through the entire list of tasks\n" + 
				"\n" + 
				"			//if more than one task executed in the current cycle\n" + 
				"			if (executedTasks.size() > 1) { \n" + 
				"				//sort the list of executed tasks according to the task's index\n" + 
				"				sortExecutedTasksList(executedTasks); \n" + 
				"			}\n" + 
				"\n" + 
				"			//add the sorted executed tasks to the end of the list of tasks\n" + 
				"			for (int z = 0; z < executedTasks.size(); z ++) { \n" + 
				"				tasksBanker.add(executedTasks.remove(z));\n" + 
				"				z --;\n" + 
				"			} //after this, the list 'executedTasks' should be empty\n" + 
				"\n" + 
				"		} //end of the while loop FOR IMPLEMENTING THE BANKER'S ALGORITHM\n" + 
				"\n" + 
				"		//sort the finalized list of tasks for the Banker's algorithm by task's index\n" + 
				"		sortExecutedTasksList(tasksFinalizedBkr); \n" + 
				"\n" + 
				"		//print any error detected\n" + 
				"		System.out.println(\"\\n-----------------------------\\n\" + errorBanker);\n" + 
				"		System.out.printf(\"%16s\\n\",\"BANKER'S\");\n" + 
				"		//print out each Task info after the Banker's resource manager finishes\n" + 
				"		for (Task ee: tasksFinalizedBkr) { //go through the sorted finalized list of tasks\n" + 
				"			//print each Task's info for the time taken, the waiting time, and the percentage of time spent waiting\n" + 
				"			ee.printTaskInfo(); \n" + 
				"			totalTime += ee.getTimeTerminated(); //add up all Task's time taken\n" + 
				"			totalWaitingTime += ee.getWaitTime(); //add up all Task's waiting time\n" + 
				"		}\n" + 
				"\n" + 
				"		//Print the total time for all tasks, the total waiting time, and the overall percentage of time spent waiting\n" + 
				"		System.out.printf(\"total %8d %4d %5d%s\",(int) totalTime,(int) totalWaitingTime,Math.round((totalWaitingTime/(totalTime*1.0)) * 100),\"%\");\n" + 
				"		System.out.println(\"\\n-----------------------------\");\n" + 
				"\n" + 
				"	} //end of the main function\n" + 
				"\n" + 
				"} //end of the Banker class\n" + 
				"";
		
		System.out.println("Banker.java is pure ASCII: " + Charset.forName("US-ASCII").newEncoder().canEncode(bankercode));
		
		String processinfocode = "/**\n" + 
				" * This lab was produced by Gayeon Park on April 2020\n" + 
				" */\n" + 
				"\n" + 
				"public class ProcessInfo {\n" + 
				"	private int processNum;\n" + 
				"	private double probA;\n" + 
				"	private double probB;\n" + 
				"	private double probC;\n" + 
				"	private double probRand;\n" + 
				"	private boolean firstReference;\n" + 
				"	private int numReferences;\n" + 
				"	private int wordRef;\n" + 
				"	private int numFaults;\n" + 
				"	private double sumEvictions;\n" + 
				"	private int numEvictions;\n" + 
				"	\n" + 
				"	public ProcessInfo(int pNum, double a, double b, double c, int size, int numberofRefs) {\n" + 
				"		processNum = pNum;\n" + 
				"		probA = a;\n" + 
				"		probB = b;\n" + 
				"		probC = c;\n" + 
				"		probRand = (1 - a - b - c) / size;\n" + 
				"		firstReference = true;\n" + 
				"		numReferences = numberofRefs;\n" + 
				"		numFaults = 0;\n" + 
				"		sumEvictions = 0.0;\n" + 
				"		numEvictions = 0;\n" + 
				"	}\n" + 
				"	\n" + 
				"	public int getProcessNum() {\n" + 
				"		return processNum;\n" + 
				"	}\n" + 
				"	\n" + 
				"	public double getAProb() {\n" + 
				"		return probA;\n" + 
				"	}\n" + 
				"	\n" + 
				"	public double getBProb() {\n" + 
				"		return probB;\n" + 
				"	}\n" + 
				"	\n" + 
				"	public double getCProb() {\n" + 
				"		return probC;\n" + 
				"	}\n" + 
				"	\n" + 
				"	public double getRandProb() {\n" + 
				"		return probRand;\n" + 
				"	}\n" + 
				"	\n" + 
				"	public boolean isFirstReference() {\n" + 
				"		return firstReference;\n" + 
				"	}\n" + 
				"	\n" + 
				"	public void didFirstReference() {\n" + 
				"		firstReference = false;\n" + 
				"	}\n" + 
				"	\n" + 
				"	public int getNumRef() {\n" + 
				"		return numReferences;\n" + 
				"	}\n" + 
				"	\n" + 
				"	public void decreaseNumRef() {\n" + 
				"		numReferences --;\n" + 
				"	}\n" + 
				"	\n" + 
				"	public int getWord() {\n" + 
				"		return wordRef;\n" + 
				"	}\n" + 
				"	\n" + 
				"	public void setWord(int nxtRef) {\n" + 
				"		wordRef = nxtRef;\n" + 
				"	}\n" + 
				"	\n" + 
				"	public int getNumFaults() {\n" + 
				"		return numFaults;\n" + 
				"	}\n" + 
				"	\n" + 
				"	public void increaseNumFaults() {\n" + 
				"		numFaults ++;\n" + 
				"	}\n" + 
				"	\n" + 
				"	public double getSumEvictions() {\n" + 
				"		return sumEvictions;\n" + 
				"	}\n" + 
				"	\n" + 
				"	public void addEviction(double newEviction) {\n" + 
				"		sumEvictions += newEviction;\n" + 
				"	}\n" + 
				"	\n" + 
				"	public int getNumEvictions() {\n" + 
				"		return numEvictions;\n" + 
				"	}\n" + 
				"	\n" + 
				"	public void increaseNumEvictions() {\n" + 
				"		numEvictions ++;\n" + 
				"	}\n" + 
				"	\n" + 
				"}\n" + 
				"";
		System.out.println("ProcessInfo.java is pure ASCII: " + Charset.forName("US-ASCII").newEncoder().canEncode(processinfocode));

		String pageentrycode = "/**\n" + 
				" * This lab was produced by Gayeon Park on April 2020\n" + 
				" */\n" + 
				"\n" + 
				"public class PageEntry {\n" + 
				"	private int processNumber;\n" + 
				"	private int pageNumber;\n" + 
				"	private int timeLoaded;\n" + 
				"	\n" + 
				"	PageEntry (int processNum, int pageNum) {\n" + 
				"		processNumber = processNum;\n" + 
				"		pageNumber = pageNum;\n" + 
				"	}\n" + 
				"	\n" + 
				"	public int getProcessNumber() {\n" + 
				"		return processNumber;\n" + 
				"	}\n" + 
				"	\n" + 
				"	public int getPageNumber() {\n" + 
				"		return pageNumber;\n" + 
				"	}\n" + 
				"	\n" + 
				"	public int getTimeLoaded() {\n" + 
				"		return timeLoaded;\n" + 
				"	}\n" + 
				"	\n" + 
				"	public void setTimeLoaded(int tLoaded) {\n" + 
				"		timeLoaded = tLoaded;\n" + 
				"	}	\n" + 
				"\n" + 
				"	@Override\n" + 
				"	public boolean equals(Object obj) {\n" + 
				"		PageEntry other = (PageEntry) obj;\n" + 
				"		if (pageNumber == other.getPageNumber() && processNumber == other.getProcessNumber())\n" + 
				"			return true;\n" + 
				"		return false;\n" + 
				"	}\n" + 
				"\n" + 
				"}\n" + 
				"";
		System.out.println("PageEntry.java is pure ASCII: " + Charset.forName("US-ASCII").newEncoder().canEncode(pageentrycode));
		

	}

}
