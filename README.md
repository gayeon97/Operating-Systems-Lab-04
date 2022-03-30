Demand Paging Lab
===============
*Operating Systems, Spring 2020*

**Gayeon Park**


- - - -
### Overview ###

For this program, we are simulating demand paging in Java and seeing how the number of page faults depends on page size, program size, replacement algorithm, and job mix. 


The goal is to have a driver generate memory references and then have the demand paging simulator (called "pager" in this lab) decide if each page reference causes a page hit or a page fault. The **exact mechanism** and **expected output** is described in the [Program Specification](#mech) below.

- - - -
### Program Layout ###
For the simplicity of the lab, we are assuming that all memory references are for entities of one fixed size - i.e. modeling a word-oriented machine containing M words. 

In a real-life OS, memory is needed for a wide range of things, like page tables, OS code, and  etc, but here we are assuming that all M words are available for page frames only. 

The pager program is invoked with 6 command line arguments, 5 positive integers and one string:
* M, the machine size in words.
* P, the page size in words.
* S, the size of each process, i.e., the references are to virtual addresses 0..S-1.
* J, the “job mix”, which determines A, B, and C, as described below.
* N, the number of references for each process.
* R, the replacement algorithm, FIFO, RANDOM, or LRU.

***Although it wasn't a requirement, I have allowed extra command line argument at the end to produce debugging information optionally. Please refer to the [instructions for the code about the verbose flag](#verbose).*** 

The driver reads all inputs first, then simulates N memory references per program, and produces all output.
- - - -
### Program Specification ###
***I would like to make it clear that the following specification is taken from the lab instructon written by Professor Allan Gottlieb, distributed to our OS class by Professor Yan Shvartzshnaider. I have made some edits, but the writing is Professor Gottlieb's. Please refer to the linked file for the actual lab instruction.***

The driver models locatliy by ensuring that:
* a fraction A of the references are to the address one higher than the current (representing a sequential memory reference)
* a fraction B are to a nearby lower address (representing a backward branch)
* a fraction C are to a nearby higher address (representing a jump around a “then” or “else” block), and the remaining fraction (1-A-B-C) are to random addresses. 


Specifically, if the current word referenced by a process is w, then the next reference by this process is to the word with address
* w+1 mod S with probability A
* w-5 mod S with probability B
* w+4 mod S with probability C
* a random value in 0..S-1 each with probability (1-A-B-C)/S


Since there are S possible references in case 4 each with probability (1-A-B-C)/S, the total probability of case 4 is 1-A-B-C, and the total probability for all four cases is A+B+C+(1-A-B-C) =1 as required.
There are four possible sets of processes (i.e., values for J):
* J=1: One process with A=1 and B=C=0, the simplest (fully sequential) case.
* J=2: Four processes, each with A=1 and B=C=0.
* J=3: Four processes, each with A=B=C=0 (fully random references).
* J=4: Four Processes. 
    * The first process has A=.75, B=.25 and C=0;
    * The second process has A=.75, B=0, and C=.25;
    * The third process has A=.75, B=.125 and C=.125;
    * The fourth process has A=.5, B=.125 and C=.125.

## Pager Mechanism <a name="mech"></a>
The pager routine processes each reference and determines if a fault occurs, in which case it makes the page resident (load the desired page into the frame table and continue on). 

If there are no free frames for this faulting page, an already resident page is evicted using replacement algorithm R (specified as one of the command line arguments), the desired page is loaded into the evicted place (now freed up), and the program continues. 

The algorithms are global (i.e., the victim can be any frame, not just ones used by the faulting
process). 

Because the lab only simulates demand paging and does not simulate the running of actual processes, it has been recommended to just implement a frame table (see next paragraph) and not page tables and that's how my pager program is written.

As we know, **each** process has an associated page table, which contains in its ***i*** th entry the number of the frame containing this process’s ***i*** th page (or an indication that the page is not resident). 

The frame table (there is only one for the entire system) contains the reverse mapping: 
* The ***i*** th entry specifies the page contained in the ***i*** th frame (or an indication that the frame is empty). 
* Specifically the ***i*** th entry contains the pair (P, p) if page ***p*** of process P is contained in frame ***i***.

The system begins with all frames empty, i.e. no pages loaded. So the first reference for each process will definitely be a page fault. 
If a run has D processes (J=1 has D=1, the others have D=4), then process k (1≤k≤D) begins by referencing word 111*k mod S.


## Expected Program Output
The program prints the input values read from the input file and prints out the following output for each process:
* Number of page faults and the average residency time. 
    * The latter is defined as the time (measured in memory references) that the page was evicted minus the time it was loaded. 
    * So at eviction, calculate the current page’s residency time and add it to a running sum. (Pages never evicted do not contribute to this sum.) 
    * The average is this sum divided by the number of evictions. 
* The total number of faults and the overall average residency time (the total of the running sums divided by the total number of evictions).

Here is an example of expected output of run-03:
```
The machine size is 10.
The page size is 10.
The process size is 10.
The job mix number is 2.
The number of references per process is 10.
The replacement algorithm is lru.
The level of debugging output is 0

Process 1 had 4 faults and 2.5 average residency.
Process 2 had 4 faults and 2.5 average residency.
Process 3 had 4 faults and 2.5 average residency.
Process 4 had 4 faults and 3.0 average residency.

The total number of faults is 16 and the overall average residency is 2.6.
```

### Notes regarding Implementation ###
<details>  
    <summary>Click to Expand</summary>

1. Despite what some books may say, the % operator in C, C++, 
    and Java is the remainder function, not the mod function. 
    For most (perhaps all) C/C++/Java compilers, (-2)%9 is -2; 
    whereas (-2) mod 9 = 7. So to calculate (w-5) mod S above, write (w-5+S)%S.
2. The big issue in this lab is the REplacement of pages. 
    The placement question does arise early in the run when there are multiple 
    free frames. The highest numbered free frame is chosen so that I can get the
    same answers and debugging output as those provided by the professor. 
3. Since random numbers are involved, the random numbers are chosen in the 
    same order. Here is a non-obvious example. 
    1. In the beginning of the program, the referenced word for each job is set
        to be 111*k as described in the lab. Now to simulate q (quantum) references
        for each job, following code was suggested by the professor and is used in
        the program:
        ```
            for (int ref=0; ref<q; ref++) {
                simulate this reference for this process
                calculate the next reference for this process
            }
    2. One effect is that after simulating the qth reference will the first reference for the next quantum be calculated. Hence, the random number file may be read before switching to the next process.
    3. Specifically, at the beginning of the run the first reference is given for process 1, namely 111*1=111 mod S. Now q references (the first to address 111 mod S) are simulated and the next q addresses are calculated.
    4. These calculations use one or two random numbers for each reference (two if a random reference occurs). So, the random number file is read once or twice for the last reference (q+1), even though the pager program will be context switching before simulating this reference. 
4. When calculating the next word to reference, there are four cases with probability A, B, C, and 1-A-B-C.
    1. Read a random number from the file and divide it by RAND MAX+1 = 2147483648 (RAND MAX is the largest value returned by the random number generator used to produce the file; it happens to equal Integer.MAX VALUE). This gives a quotient y satisfying 0≤y<1. 
    2. If the random number was called r (an integer), 
```
        // the statement you want in Java is (note the 1d):
            double y = r / (Integer.MAX VALUE + 1d)
            
        // the C/C++ equivalent is (note the 1.0)
            double y = r / (MAXINT + 1.0)
            
        If y<A, 
            do case 1 (it will occur with probability A),
        else if y<A+B, 
            do case 2, (it will occur with probability B),
        else if y<A+B+C, 
            do case 3 (it will occur with probability C).
        else /* y>=A+B+C */, 
            do case 4 (it will occur with probability 1-A-B-C.)
```
</details>

- - - -
## Instructions to run the code 

To run on crackle1 on cims.nyu.edu server, please first go to crackle1, where the folder "Lab4" is uploaded. Then navigate to the Paging.java file like the following from crackle1:
* Change directory to Lab4, then change directory to src.
Inside of the "src" folder, Paging.java file is located.

As mentioned above, execute the program using 6 command line arguments: 5 positive integers followed by a string, with a space between each of them.  The program prints output to the screen as System.out in Java.

## Verbose Flag <a name="verbose"></a>
The program accepts an optional integer value for verbose flag. When the verbose flag is given, the program produce detailed output that's helpful for debugging. 

To get the corresponding debugging or “show random” output, enter the integer value after the string. It can be one of the following values:
* value 0 (same as not having the "verbose" flag and this would make 6 command line arguments to enter), 
* value 1 (returns debugging output and this would make 7 command line arguments to enter ), 
* value 11 (returns "show-random"" output and this would make 7 command line arguments to enter).
The "-show-random" output gives more detailed output along with the random number chosen each time. This is a more verbose version.

Two possible invocations of the program are:
<program-name> <int> <int> <int> <int> <int> <string>
<program-name> <int> <int> <int> <int> <int> <string> <int>


**ONE IMPORTANT THING TO NOTE: the "random-numbers" file you are using MUST be in the same folder as the Paging.java file. So Paging.java file and "random-numbers" file HAVE to be in the same folder (namely, src folder inside of the Lab4 folder).**


Type the below instruction into the Terminal to compile the Paging.java program. When compiling, you have to make sure that you are compiling all the following files: 
* Paging.java
* ProcessInfo.java
* PageEntry.java.
This is because Paging.java uses an instance of ProcessInfo class and PageEntry class.


***Please ignore the java classes with title including "unclean" or "part", as I have multiple versions of the project that has partially working parts. This was done along the development process to avoid confusion or contaminating the working code. The two java programs that matter are "Banker.java" and "Task.java"".***

- - - - 

### Compiling
```
javac Paging.java ProcessInfo.java PageEntry.java
```

### Running
To execute the code, type following:
```
java Paging int int int int int string
```
- - - -
For example, if you want to execute the Paging.java file with the following: 10 10 20 1 10 lru, please do the following: 

### Compiling
```
javac Paging.java ProcessInfo.java PageEntry.java
```

### Running
To execute the code, type following:
```
java Paging 10 10 20 1 10 lru
```
- - - -
If you want to execute the Paging.java file with the following: 10 10 20 1 10 lru, AND include the VERBOSE FLAG, please do the following: 

### Compiling
```
javac Paging.java ProcessInfo.java PageEntry.java
```

### Running
To execute the code with verbose flag of 0, type following:
```
//will yield the same output as 6 command line arguments
java Paging 10 10 20 1 10 lru 0     
```
### Running
To execute the code with verbose flag of 1, type following:
```
//these 7 command line arguments will yield debug output
java Paging 10 10 20 1 10 lru 1   
```
### Running
To execute the code with verbose flag of 11, type following:
```
//these 7 command line arguments will yield "show-random" output
java Paging 10 10 20 1 10 lru 11   
```
