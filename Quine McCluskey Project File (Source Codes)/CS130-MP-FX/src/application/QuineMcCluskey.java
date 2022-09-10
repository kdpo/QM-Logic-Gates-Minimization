package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class QuineMcCluskey {
	 
	Compare compare = new Compare();
	ArrayList<ArrayList<Integer>>[] intQMTerms;
	private InputHandler inputs = new InputHandler();
	private HashMap<ArrayList<Integer>, String> dataContent = new HashMap<ArrayList<Integer>, String>();
	HashSet<ArrayList<Integer>> essentialPrimeImplicants =  new HashSet<ArrayList<Integer>>();
	StringBuilder toPrint = new StringBuilder();
	
	/* InputHandler class contains all necessary info */
	
	public QuineMcCluskey(InputHandler inputs) {
		this.inputs = inputs;
	}
	
	// grouping according to number of ones.
	public HashMap<ArrayList<Integer>, String>[] groupByOnes() {

		System.out.println("STEP 1: GROUPING ACCORDING TO THE NUMBER OF ONES.\n");
		toPrint.append("STEP 1: GROUP ACCORDING TO THE NUMBER OF ONES.\n");
		toPrint.append("--------------------------------------------------------------------------------\n");
		
		/* @data will contain grouped binary strings that are mapped to their integer counterparts. 
		 * The keys are the pairs of minterms(which for the mean time are the individual minterms) and the values will be 
		 * the binary strings. */
		
        @SuppressWarnings("unchecked")
		HashMap<ArrayList<Integer>, String>[] data = new HashMap[inputs.getNumberOfVar()+1];
        for (int i = 0; i < data.length; i++) {
            data[i] = new HashMap<>();
        }
        
        /* k represents the group which is the number of ones
         * every integer is put into an individually separated ArrayList<Integer>
         * this is for grouping in pairs for later. */
        for (int i = 0; i < inputs.getMinterms().size(); i++) {
            int k = compare.getNumberOfOnes(inputs.getMinterms().get(i));
            ArrayList<Integer> temp = new ArrayList<Integer>();
            temp.add(inputs.getListOfNumbers().get(i));
            data[k].put(temp, inputs.getMinterms().get(i));
        }
      
        // for printing
        for (int i = 0; i < data.length; i++) {
        	if (!data[i].isEmpty()) {
            	System.out.println("Group " + i);
            	toPrint.append("Group " + i + "\n");
        		for (ArrayList<Integer> x : data[i].keySet()) {
        			System.out.println(x + " = " + data[i].get(x));
        			toPrint.append( x + " = " + data[i].get(x) + "\n");
        		}
        		toPrint.append("\n");
        		System.out.println();
            }
        }
        return data;
    }
	
	@SuppressWarnings("unchecked")
	public void solve() {
		/* @dataContent will contain the pre-filtered implicants (keys are the pair of integers, values are the binary form.) */
		HashMap<ArrayList<Integer>, String>[] groupedInitially = groupByOnes();
		HashMap<ArrayList<Integer>, String>[] results;
		
		/* @marked returns true if it is taken(has a partner with only one difference in their binary string), 
		 * false if not taken. */
		boolean marked;
		String  apostrophe = "";
    	
		System.out.println("STEP 2: FILTER OUT THE IMPLICANTS.");
		toPrint.append("STEP 2: FILTER OUT THE IMPLICANTS BY MERGING MINTERMS FROM ADJACENT GROUPS. REPEAT UNTIL NO MERGING IS POSSIBLE. \n");
		do {
			marked = false;
			/* @results contains the initial filtered for every merging, it changes contents every iteration. 
			 * its length is reduced at every iteration. At every filter, the groups are reduced by one. */
			results = new HashMap[groupedInitially.length - 1];
			
			/* @taken contains pairs with one difference */
			HashSet<Object> taken = new HashSet<>();
			/* @temp is for checking so that no pair would repeat in results. */
			ArrayList<Object> temp;
			
			toPrint.append("--------------------------------------------------------------------------------\n");
        	
			/* iterate at every group. */
            for (int i = 0; i < groupedInitially.length - 1; i++) {
            	
            	results[i] = new HashMap<>();
                temp = new ArrayList<>();

                /* iterate every element in the preceding group */
                for (ArrayList<Integer> keyPair : groupedInitially[i].keySet()) {
                	
                	ArrayList<Integer> pair = new ArrayList<Integer>();
                	
                	/* iterate every element in the succeeding group */
                	for (ArrayList<Integer> keyPair2 : groupedInitially[i+1].keySet()) {
                		ArrayList<Integer> tempPair = new ArrayList<Integer>();	
                		
                		/* add the first half of the pair */
                		tempPair.addAll(keyPair);
                        
                		/* if there is only one difference, add it to the taken HashSet */
                		if (compare.onlyOneDifference(groupedInitially[i].get(keyPair), groupedInitially[i+1].get(keyPair2))) {
                        	taken.add(groupedInitially[i].get(keyPair));
                            taken.add(groupedInitially[i + 1].get(keyPair2));                  
                            String diff = compare.difference(groupedInitially[i].get(keyPair), groupedInitially[i+1].get(keyPair2));
                            taken.add(diff);           
                            tempPair.addAll(keyPair2);
                           
                            /* if temp does not have the pair and binary, add it to the results, this way
                             * there would be no repetitions. */
                           if (!temp.contains(diff) && !temp.contains(tempPair)) {
                               results[i].put(tempPair, diff);
                        	   marked = true;
                            }
                        	   
                            temp.add(diff);
                            temp.add(tempPair);
                        }
                		
                		/* pair may contain a pair(keyPair, keyPair2) if it is taken, it may only contain keyPair if it is
                		 * not taken (NOTE: keyPair may contain a single number if from the first merging, it has no partner.)*/
                        pair = tempPair;
                    }
                }
            }
            int i=0;
            for (HashMap<ArrayList<Integer>, String> hash : groupedInitially) {
            	if(!hash.isEmpty()) {
            		System.out.println("Group " + i + apostrophe);
            		toPrint.append("Group " + i + apostrophe + "\n");
            		for(ArrayList<Integer> keypair: hash.keySet()) {
                		System.out.println(keypair + " = " +hash.get(keypair));
                		toPrint.append(keypair + " = " +hash.get(keypair) + "\n");
                	}
            		toPrint.append("\n");
            	}
            	i++;
            }
            System.out.println("-------------------------------------------");
            /* the results contain the filtered, replace contents of groupedInitially then repeat the filtering until 
             * filtering becomes impossible to conduct. */
            if (marked) {
            	/* if it is not taken, add it immediately to the list of finalists */
            	 for (HashMap<ArrayList<Integer>, String> hash : groupedInitially) {
                     for (ArrayList<Integer> keypair: hash.keySet()) {
                         if (!taken.contains(hash.get(keypair))) {
                             dataContent.put(keypair, hash.get(keypair));
                         }
                     }
                 }
            	groupedInitially = results;
            	apostrophe += "'";
            }
            
		} while(marked && (groupedInitially.length > 1));
		
		/* after filtering everything, add the remaining implicants */
        for (HashMap<ArrayList<Integer>, String> filtered : groupedInitially) {
        	dataContent.putAll(filtered);
        }
        
        /* print the remaining implicants. */
        System.out.println("\nRemaining Implicants:");
        toPrint.append("\nRemaining Implicants:\n");
        for (ArrayList<Integer> key : dataContent.keySet()) {
            System.out.println(key + "\t\t" + dataContent.get(key));
            toPrint.append(key + " = " + dataContent.get(key) + "\n");
        }
        
        if (dataContent.size() > 1)
        	elimination();
        else {
        	
        	 toPrint.append("\nFinal Answer:\n");
        	for (ArrayList<Integer> key : dataContent.keySet()) {
        		 toPrint.append(key + " = " + dataContent.get(key) + "\n");
            }
        	
        	toPrint.append("\n FINAL STEP: CONVERT TO BOOLEAN EXPRESSION.\n"
            		+ "--------------------------------------------------------------------------------\nMinimized Function: \n");
        	
        	StringBuilder builder = new StringBuilder();
            for (ArrayList<Integer> keys : dataContent.keySet()) {
            	if (builder.length() != 0) {
                    builder.append(" + ");
                }
                builder.append(convertToLetters(dataContent.get(keys)));
            }
            toPrint.append(builder);
        }
	}
	
	public void elimination() {
		
		HashSet<ArrayList<Integer>> NprimeImplicants =  new HashSet<ArrayList<Integer>>();
		ArrayList<Integer> primes = new ArrayList<Integer>();
		ArrayList<Integer> tempMinterms = inputs.getListDontCaresRemoved();
	
		for (Integer minterm: tempMinterms) {	
			int count = 0;
			for (ArrayList<Integer> keyPair : dataContent.keySet()) {
				if(keyPair.contains(minterm)) {
					count++;
					if(count > 1) {
						break;
					}
				}
			}
			if (count == 1) {
				System.out.println(minterm);
				primes.add(minterm);
			}
		}
		
		Set<Integer> forCheck = new HashSet<Integer>();
		for (ArrayList<Integer> keyPair : dataContent.keySet()) {
			for(Integer prime : primes)
				if(keyPair.contains(prime)) {
					essentialPrimeImplicants.add(keyPair);
					forCheck.addAll(keyPair);
				}
			if(!essentialPrimeImplicants.contains(keyPair))
				NprimeImplicants.add(keyPair);
		}
		
		toPrint.append("\nSTEP 3: FIND THE ESSENTIAL MINTERMS. THE PRIME IMPLICANTS CONTAINING THE ESSENTIAL MINTERMS ARE THE ESSENTIAL PRIME IMPLICANTS. (EXCLUDE DON'T CARES FROM THE LIST)\n");
		toPrint.append("--------------------------------------------------------------------------------\n");
		toPrint.append("\nEssential Minterm/s:\n" + primes);
		toPrint.append("\nEssential Prime Implicant/s:\n");
        for (ArrayList<Integer> keys : essentialPrimeImplicants) {
            System.out.println(keys + "\t\t" + dataContent.get(keys));
            toPrint.append( keys + " = " + dataContent.get(keys) + "\n");
            tempMinterms.removeAll(keys);
        }
        
		if (!forCheck.containsAll(inputs.getListDontCaresRemoved())) {
			toPrint.append("\nSTEP 4: ADD THE REMAINING PRIME IMPLICANTS CONTAINING THE LEFT MINTERMS TO THE FUNCTION UNTIL ALL MINTERMS ARE COVERED. (EXCLUDE DON'T CARES FROM THE LIST)\n"
					+ "--------------------------------------------------------------------------------\n");
			toPrint.append("\nRemaining Minterm/s:\n" + tempMinterms);
		}
		
        while(!forCheck.containsAll(inputs.getListDontCaresRemoved())) {
			ArrayList<Integer> temp = new ArrayList<Integer>();
			for (ArrayList<Integer> k : NprimeImplicants) {
				Set<Integer> intersection = new HashSet<Integer>(k);
				Set<Integer> intersection2 = new HashSet<Integer>(temp);
				intersection.retainAll(tempMinterms);
				intersection2.retainAll(tempMinterms);
				if(intersection.size() > intersection2.size()) {
					temp = k;
				}
			}
			tempMinterms.removeAll(temp);
			essentialPrimeImplicants.add(temp);
			forCheck.addAll(temp);
		}
		
		System.out.println("\nFinal Minterms:");
		 toPrint.append("\nFinal Minterms: \n");
        for (ArrayList<Integer> keys : essentialPrimeImplicants) {
            System.out.println(keys + " = " + dataContent.get(keys));
            toPrint.append(keys + " = " + dataContent.get(keys) + "\n");
        }
        
        toPrint.append("\nFINAL STEP: CONVERT TO BOOLEAN EXPRESSION.\n"
        		+ "--------------------------------------------------------------------------------\nMinimized Function: \n");
        StringBuilder builder = new StringBuilder();
        for (ArrayList<Integer> keys : essentialPrimeImplicants) {
        	if (builder.length() != 0) {
                builder.append(" + ");
            }
            builder.append(convertToLetters(dataContent.get(keys)));
        }
        toPrint.append(builder);
	}
	
	private String convertToLetters(String binary) {
		String letters = "";
		char var = 'A';
		for (int i = 0; i < binary.length(); i++) {
			if(binary.charAt(i) == '1') {
				letters += var;
			}
			else if(binary.charAt(i) == '0')
				letters += var + "'";
			var++;
		}
		return letters;
	}
}
