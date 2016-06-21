import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
	
	
	private static final int R = 256; 
	private int[] index;
	
	public CircularSuffixArray(String s)  // circular suffix array of s
	{
		if (s == null)
			throw new NullPointerException();
		index = new int[s.length()];
		
		//Do index counting on first character
		int[] count = new int[R + 1];
		for (int i = 0; i < s.length(); i++)
			count[((int) s.charAt(i)) + 1]++; 
		for (int i = 1; i < R + 1; i++)
			count[i] += count[i - 1];
		for (int i = 0; i < s.length(); i++)
			index[count[(int) s.charAt(i)]++] = i;
		
		int size = 2;
		int[] reverse = new int[s.length()];
		for (int i = 0; i < s.length(); i++)
			reverse[index[i]] = i;
		while (size < s.length()) {
			
		}
		
	}
	
	public int length()                   // length of s
	{
		return index.length;
	}
	
	public int index(int i)               // returns index of ith sorted suffix
	{
		if (i < 0 || i >= length())
			throw new IndexOutOfBoundsException();
		
		return index[i];
		
	}
	
	public static void main(String[] args)// unit testing of the methods (optional)
	{
		CircularSuffixArray ar = new CircularSuffixArray("olaa");
		for (int i = 0; i < ar.length(); i++)
			StdOut.print(ar.index(i) + " ");
	}
}