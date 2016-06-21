import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class MoveToFront {
	
	private static final int R = 256;
	// apply move-to-front encoding, reading from standard input and writing to standard output
	public static void encode() {
		//Build dictionary
		Stack<Byte> letters = new Stack<>();
		for (int i = R - 1; i >= 0; i--)
			letters.push((byte) i);
		Stack<Byte> temp_stack = new Stack<>();
		while (!BinaryStdIn.isEmpty()) {
			byte i = BinaryStdIn.readByte();
			byte pos = 0;
			while (letters.peek() != i) {
				temp_stack.push(letters.pop());
				pos++;
			}
			letters.pop();
			BinaryStdOut.write(pos);
			while (!temp_stack.isEmpty())
				letters.push(temp_stack.pop());
			letters.push(i);
		}
		BinaryStdOut.flush();
		
	}
	
	// apply move-to-front decoding, reading from standard input and writing to standard output
	public static void decode() {
		//Build dictionary
		Stack<Character> letters = new Stack<>();
		for (int i = R - 1; i >= 0; i--)
			letters.push((char) i);
		Stack<Character> temp_stack = new Stack<>();
		while (!BinaryStdIn.isEmpty()) {
			int pops = BinaryStdIn.readInt(8);
			
			while (pops-- > 0) {
				temp_stack.push(letters.pop());
			}
			char ch = letters.pop();
			BinaryStdOut.write(ch);
			while (!temp_stack.isEmpty())
				letters.push(temp_stack.pop());
			letters.push(ch);
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
