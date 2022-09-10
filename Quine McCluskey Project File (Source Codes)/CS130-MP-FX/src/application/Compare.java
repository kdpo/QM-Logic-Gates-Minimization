package application;


public class Compare {
	
	//gets the number of ones in a binary string
	public int getNumberOfOnes(String binary) {
		int one = 0;		
		 for (int i = 0; i < binary.length(); i++) {
	            if (binary.charAt(i) == '1') 
	                one++;
	        }
		return one;
	}
	
	// compares number of ones between 2 strings
	public int compare(String a, String b) {
		return getNumberOfOnes(a) - getNumberOfOnes(b);
	 }
	
	// returns a string containing a replaced character with a dash. It accepts 2 strings and finds the index of difference
	public String difference(String str1, String str2) {
	    int at = indexOfDifference(str1, str2);
	    StringBuilder temp = new StringBuilder(str1);
	    temp.setCharAt(at, '-');
	    String newString = temp.toString();
	    return newString;
	}

	public static int indexOfDifference(CharSequence cs1, CharSequence cs2) {
	    int i;
	    for (i = 0; i < cs1.length(); ++i) {
	        if (cs1.charAt(i) != cs2.charAt(i)) {
	            break;
	        }
	    }
	    return i;
	}
	
	boolean onlyOneDifference(String t1, String t2) {
        
        // if dashes not in same place or hamming dist != 1, return false
        int k = 0;
        for (int i = 0; i < t1.length(); i++) {
            if (t1.charAt(i) == '-' && t2.charAt(i) != '-')
                return false;
            else if (t1.charAt(i) != '-' && t2.charAt(i) == '-')
                return false;
        	else if (t1.charAt(i) != t2.charAt(i))
                k++;
            else
                continue;
        }
        if (k != 1)
            return false;
        else
            return true;
    }
}
