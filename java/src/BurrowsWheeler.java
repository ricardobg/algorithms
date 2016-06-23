import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;

public class BurrowsWheeler {
	private static final int R = 256;
	// apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
	public static void encode() {
		String s = BinaryStdIn.readString();
		CircularSuffixArray array = new CircularSuffixArray(s);
		int n = 0;
		char[] out = new char[s.length()];
		for (int i = 0; i < s.length(); i++) {
			out[i] = s.charAt((array.index(i) + s.length() - 1) % s.length());
			if (array.index(i) == 0)
				n = i;
		}
		BinaryStdOut.write(n);
		for (char c : out)
			BinaryStdOut.write(c);
		BinaryStdOut.flush();
	}
	
	// apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
	@SuppressWarnings("unchecked")
	public static void decode() {
		int n = BinaryStdIn.readInt();
		String s = BinaryStdIn.readString();
		char[] sorted = new char[s.length()];
		int[] next = new int[s.length()];
		Object[] inverted = new Object[R];
		for (int i = 0; i < R; i++)
			inverted[i] = new Queue<Integer>();
		for (int i = 0; i < s.length(); i++)
			((Queue<Integer>) inverted[(int) s.charAt(i)]).enqueue(i);
		int pos = 0;
		for (int i = 0; i < R; i++)
			while (!((Queue<Integer>) inverted[i]).isEmpty()) {
				int to = ((Queue<Integer>) inverted[i]).dequeue(); 
				sorted[pos] = (char) i;
				//StdOut.println("pos=" + pos + " and to=" + to);
				next[pos++] = to;
			}
		
		int i = n;
		int printed = 0;
		do {
			//StdOut.println("i=" + i + " and next=" + next[i]);
			BinaryStdOut.write(sorted[i]);
			i = next[i];
			printed++;
		}
		while (printed < s.length());
		//StdOut.println(n);
		BinaryStdOut.flush();
			
	}

	// if args[0] is '-', apply Burrows-Wheeler encoding
	// if args[0] is '+', apply Burrows-Wheeler decoding
	public static void main(String[] args) {
		if (args[0].equals("-")) {
			encode();
		}
		else if (args[0].equals("+"))
			decode();
	}
}