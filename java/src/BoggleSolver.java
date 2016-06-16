import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver
{
	private MyTrie trie = new MyTrie();

	private static final int[][] positions = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};

	//private int count = 0;
	private void findCombs(MyTrie.Node start, String prefix, int i, int j, BoggleBoard board, boolean[][] visited, SET<String> matches) {
		String newPrefix = prefix;
		String add = board.getLetter(i, j) + "";
		if (board.getLetter(i, j) == 'Q')
			add += "U";
		newPrefix += add;
		MyTrie.Node newStart = trie.prefix(start, add);
		if (newStart == null)
			return;
		if (newStart.isWord())
			matches.add(newPrefix);

		for (int[] pos : positions) {
			int y = i + pos[0], x = j + pos[1];
			if (y < 0 || y >= board.rows() || x < 0 || x >= board.cols())
				continue;
			if (visited[y][x])
				continue; 
			visited[y][x] = true;
			findCombs(newStart, newPrefix, y, x, board, visited, matches);
			visited[y][x] = false;
		}
		//End
		//StdOut.println(newPrefix);
		//count++;
		
	}

	// Initializes the data structure using the given array of strings as the dictionary.
	// (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
	public BoggleSolver(String[] dictionary) {
		if (dictionary == null)
			throw new NullPointerException();
		for (String word : dictionary) {
			if (word.length() >= 3) {
				trie.add(word);
			}
		}
	}

	// Returns the set of all valid words in the given Boggle board, as an Iterable.
	public Iterable<String> getAllValidWords(BoggleBoard board) {
		if (board == null)
			throw new NullPointerException();
		SET<String> matches = new SET<>();
		boolean[][] visited = new boolean[board.rows()][board.cols()];
		for (int i = 0; i < board.rows(); i++) {
			for (int j = 0; j < board.cols(); j++) {
				visited[i][j] = true;
				findCombs(trie.root(), "", i, j, board, visited, matches);
				visited[i][j] = false;
			}
		}
		return matches;
	}

	// Returns the score of the given word if it is in the dictionary, zero otherwise.
	// (You can assume the word contains only the uppercase letters A through Z.)
	public int scoreOf(String word) {
		if (trie.contains(word)) {
			switch (word.length()) {
			case 0:
			case 1:
			case 2:
				return 0;
			case 3:
			case 4:
				return 1;
			case 5:
				return 2;
			case 6:
				return 3;
			case 7:
				return 5;
			default:
				return 11;

			}
		}
		return 0;
	}
	public static void main(String[] args)
	{
		In in = new In(args[0]);
		String[] dictionary = in.readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);
		BoggleBoard board = new BoggleBoard(args[1]);

		for (int i = 0; i < 10000; i++) {
			int score = 0;
			for (String word : solver.getAllValidWords(board))
			{
				//StdOut.println(word);
				score += solver.scoreOf(word);
			}
			StdOut.println("Score = " + score);
		}

		//  StdOut.println("Count = " + solver.count);
	}
}