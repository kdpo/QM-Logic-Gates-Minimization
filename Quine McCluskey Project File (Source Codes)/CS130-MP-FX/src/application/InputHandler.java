package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class InputHandler {
 	
	private int numVariables;
	private ArrayList<Integer> mintermsInt = new ArrayList<Integer>();
	private ArrayList<Integer> dcInt = new ArrayList<Integer>();
	private ArrayList<String> minterms = new ArrayList<String>();
	ArrayList<Integer> ndcInt = new ArrayList<Integer>();
	
	/* Sorting in increasing order
	 * conversion to binary */
	public void generateInput(ArrayList<String> mintermsRaw, ArrayList<String> dcRaw){
		if(!dcRaw.isEmpty()) {
			mintermsRaw.addAll(dcRaw);
			
			for(String s: dcRaw) {
				dcInt.add(Integer.parseInt(s));
			}
		}
		
		Collections.sort(mintermsRaw, new Comparator<String>() {
	        @Override
	        public int compare(String o1, String o2) throws RuntimeException {
	            return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
	        }
	    });
		
		//The last element (largest number) was accessed to determine the length or number of variables needed.
		String lastDigit = Integer.toBinaryString(Integer.parseInt(mintermsRaw.get(mintermsRaw.size()-1)));
		numVariables = lastDigit.length();
		
		for (int i=0; i < mintermsRaw.size(); i++) {
			int number = Integer.parseInt(mintermsRaw.get(i));
			
			// to avoid repeating inputs in integer form minterms
			if (!mintermsInt.contains(number)) {
				mintermsInt.add(number);
			}
			
			
			String binary = Integer.toBinaryString(number);
			// right padding zeroes so that each binary string are of the same length.
			if (binary.length() < numVariables) {
				int index = binary.length();
				while(index != numVariables) {
					binary = "0" + binary;
					index++;
				}
			}
			
			// avoid repeating inputs in binary form minterms
			if(!minterms.contains(binary))
				minterms.add(binary);
		}

		ndcInt.addAll(mintermsInt);
		ndcInt.removeAll(dcInt);
	}
	
	// returns number of variables 
	public int getNumberOfVar() {
		return numVariables;
	}
	
	//returns the list of minterms in integer form.
	public ArrayList<Integer> getListOfNumbers() {
		return mintermsInt;
	}
	
	public ArrayList<Integer> getListDontCaresRemoved() {	
		return ndcInt;
	}
	
	// returns the list of minterms in binary form.
	public ArrayList<String> getMinterms() {
		return minterms;
	}
}
