import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class paging_version2 {
	public static int globalIndex = 0;
	public static int verbose = -1;
	public static int quantum = 3;
	public static List<String> fileStream = null;
	
	static double randomNum(int processNum) throws FileNotFoundException {
//		List<String> fileStream = null;
//		try {
//			fileStream = Files.readAllLines(Paths.get(System.getProperty("user.dir") + "/random-numbers"),Charset.forName("UTF-8"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		int randomNumFromFile = Integer.parseInt(fileStream.get(globalIndex));
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
		
//		int i = 1;
//		for (ProcessInfo e: processes) {
//			System.out.printf("\nFor process %d,\n", i);
//			System.out.printf("\tThis is the a percent: %f\n", e.getAProb());
//			System.out.printf("\tThis is the b percent: %f\n", e.getBProb());
//			System.out.printf("\tThis is the c percent: %f\n", e.getCProb());
//			System.out.printf("\tThis is the random percent: %f\n", e.getRandProb());
//			i ++;
//		}
		
		//int word = (111 + s_processSize) % s_processSize;
		int word = -1;
		
		int counter = 0;
		while (counter < 12) {
			for (ProcessInfo e: processes) {
				
				if (r_replaceAlg.equals("random")) {
					if (processes.indexOf(e) == 0) {
						//System.out.println("the process size: " + s_processSize);
						e.setWord(((111 * (processes.indexOf(e)+1)) + s_processSize) % s_processSize);
						e.didFirstReference();	
						//System.out.println("at first, word is: "+ e.getWordReferenced());
					} else {
						//globalIndex ++;
						
						int randomNum = Integer.parseInt(fileStream.get(globalIndex));
						
						if (verbose == 11) {
							System.out.printf("%d uses random number: %d\n", processes.indexOf(e)+1, randomNum);
						}
						
						word = (randomNum + s_processSize) % s_processSize;
						e.setWord(word);
						
						System.out.println("word is: "+ e.getWord());
					}
				} else {
					if (e.isFirstReference()) {
						//System.out.println("the process size: " + s_processSize);
						e.setWord(((111 * (processes.indexOf(e)+1)) + s_processSize) % s_processSize);
						e.didFirstReference();	
						//System.out.println("at first, word is: "+ e.getWordReferenced());
					}
				}
				
				
				
				
				word = e.getWord();

				for (int ref = 0; ref < 3; ref ++) {
					System.out.printf("%d references word %d.\n",processes.indexOf(e)+1,word);
					
					double y = randomNum(processes.indexOf(e)+1);
					System.out.printf("This is y: %f\n", y);
					if (y < e.getAProb()) {
						System.out.println("do case 1");
						word = (word + 1 + s_processSize ) % s_processSize;
					} else if (y < (e.getAProb() + e.getBProb()) ) {
						System.out.println("do case 2");
						word = (word - 5 + s_processSize ) % s_processSize;
					} else if (y < (e.getAProb() + e.getBProb() + e.getCProb()) ) {
						System.out.println("do case 3");
						word = (word + 4 + s_processSize ) % s_processSize;
					} else if (y >= (e.getAProb() + e.getBProb() + e.getCProb()) ) {
						System.out.println("do case 4, FIGURE OUT HOW TO USE ANOTHER RANDOM # to generate the next word!");						
						
						globalIndex ++;
						
						int randomNum = Integer.parseInt(fileStream.get(globalIndex));
						
						if (verbose == 11) {
							System.out.printf("%d uses random number: %d\n", processes.indexOf(e)+1, randomNum);
						}
						
						word = randomNum % s_processSize;
					}
					e.setWord(word);
					globalIndex ++;				
				}
				
				System.out.printf("for next quantum, %d will reference word %d.\n\n",processes.indexOf(e)+1,e.getWord());
			}
			
			counter ++;
		}
		
		
		
//		for (int ref = 0; ref < 3; ref ++) {
//			System.out.println(word);
//			
//			double y = randomNum(1);
//			System.out.printf("This is y: %f\n", y);
//			if (y < processes.get(0).getAProb()) {
//				System.out.println("do case 1");
//				word = (word + 1 + s_processSize ) % s_processSize;
//			} else if (y < (processes.get(0).getAProb() + processes.get(0).getBProb()) ) {
//				System.out.println("do case 2");
//				word = (word - 5 + s_processSize ) % s_processSize;
//			} else if (y < (processes.get(0).getAProb() + processes.get(0).getBProb() + processes.get(0).getCProb()) ) {
//				System.out.println("do case 3");
//				word = (word + 4 + s_processSize ) % s_processSize;
//			} else if (y >= (processes.get(0).getAProb() + processes.get(0).getBProb() + processes.get(0).getCProb()) ) {
//				System.out.println("do case 4");
//				word = (word + 1 + s_processSize ) % s_processSize;
//			}
//			globalIndex ++;			
//			
//			i ++;
//		}
//		
//		System.out.println(word);
				
		


	} //end of main method

}
