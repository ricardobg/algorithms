import edu.princeton.cs.algs4.Queue;

public class Board {

	private int[][] blocks;

	// construct a board from an N-by-N array of blocks
	// (where blocks[i][j] = block in row i, column j)
	public Board(int[][] blocks) {
		this.blocks = new int[blocks.length][blocks.length];
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks.length; j++)
				this.blocks[i][j] = blocks[i][j];
		}
	}

	public int dimension() // board dimension N
	{
		return blocks.length;
	}

	private static int abs(int val) {
		if (val >= 0)
			return val;
		return -val;
	}

	public int hamming() // number of blocks out of place
	{
		int ham = 0;
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks.length; j++) {
				if (blocks[i][j] == 0)
					continue;
				int expected = (i * blocks.length + j + 1);
				if (blocks[i][j] != expected)
					ham += 1;
			}
		}
		return ham;
	}

	public int manhattan() // sum of Manhattan distances between blocks and goal
	{
		int man = 0;
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks.length; j++) {
				if (blocks[i][j] == 0)
					continue;
				int x = (blocks[i][j] - 1) % blocks.length;
				int y = (blocks[i][j] - 1) / blocks.length;
				man += abs(x - j) + abs(y - i);
			}
		}
		return man;
	}

	public boolean isGoal() // is this board the goal board?
	{
		return hamming() == 0;
	}

	public Board twin() // a board that is obtained by exchanging any pair of
	// blocks
	{
		Board copy = new Board(blocks);
		int i0 = 0, j0 = 0;
		boolean foundFirst = false;
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks.length; j++) {
				if (blocks[i][j] != 0) {
					if (foundFirst) {
						copy.blocks[i0][j0] = blocks[i][j];
						copy.blocks[i][j] = blocks[i0][j0];
						return copy;
					} else {
						i0 = i;
						j0 = j;
						foundFirst = true;
					}
				}
			}
		}
		return null;
	}

	public boolean equals(Object y) // does this board equal y?
	{
		if (this == y)
			return true;
		if (y == null)
			return false;
		if (y.getClass() != this.getClass())
			return false;
		Board cmp = (Board) y;
		if (cmp.dimension() != dimension())
			return false;
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks.length; j++) {
				if (blocks[i][j] != cmp.blocks[i][j])
					return false;
			}
		}
		return true;
	}

	public Iterable<Board> neighbors() // all neighboring boards
	{
		Queue<Board> boards = new Queue<>();
		final int[][] moves = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
		// Find zero position
		int zero_i = -1, zero_j = -1;
		for (int i = 0; i < blocks.length && zero_i == -1; i++)
			for (int j = 0; j < blocks.length; j++)
				if (blocks[i][j] == 0) {
					zero_i = i;
					zero_j = j;
					break;
				}
		// Try all possible moves
		for (int[] move : moves) {
			int swap_i = move[0] + zero_i;
			int swap_j = move[1] + zero_j;
			if (swap_i < 0 || swap_i >= blocks.length || swap_j < 0 || swap_j >= blocks.length)
				continue;
			// It is a valid change
			// Copy board
			Board neighbor = new Board(blocks);
			// Change zero
			neighbor.blocks[zero_i][zero_j] = blocks[swap_i][swap_j];
			neighbor.blocks[swap_i][swap_j] = 0;
			// Add to boards
			boards.enqueue(neighbor);
		}
		return boards;
	}

	public String toString() // string representation of this board (in the
	// output format specified below)
	{
		StringBuilder str = new StringBuilder(dimension() + "\n");
		for (int i = 0; i < dimension(); i++) {
			for (int j = 0; j < dimension(); j++) {
				str.append(String.format("%2d ", blocks[i][j]));
			}
			str.append("\n");
		}
		return str.toString();
	}

	public static void main(String[] args) // unit tests (not graded)
	{
		Board board = new Board(new int[][] { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } });
		System.out.println("Initial board 3x3:");
		System.out.println(board);
		System.out.println("Hamming = " + board.hamming() + " (expected 5)");
		System.out.println("Manhattan = " + board.manhattan() + " (expected 10)");
		System.out.println("is Goal? " + board.isGoal());
		System.out.println("Neighbors: ");
		for (Board b : board.neighbors()) {
			System.out.println(b);
		}
	}
}
