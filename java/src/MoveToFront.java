import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class MoveToFront {
	
	private static final int R = 256;
	// apply move-to-front encoding, reading from standard input and writing to standard output
	public static void encode() {
		//Build dictionary
		char[] letters = new char[R];
		for (int i = 0; i < R; i++)
			letters[i] = (char) i;
		while (!BinaryStdIn.isEmpty()) {
			char i = BinaryStdIn.readChar();
			int pos = -1;
			while (letters[++pos] != i) ;
			BinaryStdOut.write((char) pos);
			BinaryStdOut.flush();
			//Copy
			while (pos-- > 0) 
				letters[pos + 1] = letters[pos];
			letters[0] = i;
		}
		BinaryStdOut.flush();
		
	}
	
	// apply move-to-front decoding, reading from standard input and writing to standard output
	public static void decode() {
		//Build dictionary
		char[] letters = new char[R];
		for (int i = 0; i < R; i++)
			letters[i] = (char) i;
		while (!BinaryStdIn.isEmpty()) {
			int pops = BinaryStdIn.readInt(8);
			char ch = letters[pops];
			BinaryStdOut.write(ch);
			while (pops-- > 0) 
				letters[pops + 1] = letters[pops];
			letters[0] = ch;
			
		}
		BinaryStdOut.flush();
	}

	// if args[0] is '-', apply move-to-front encoding
	// if args[0] is '+', apply move-to-front decoding
	public static void main(String[] args) {
		if (args[0].equals("-")) {
			encode();
		}
		else if (args[0].equals("+"))
			decode();
	}
}
