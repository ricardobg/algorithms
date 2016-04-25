import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
	// Node in A* search
	private class Node implements Comparable<Node> {
		private Board board;
		private Node previous;
		private int cost;
		private int moves;

		private Node(Board board, Node previous) {
			this.board = board;
			this.previous = previous;
			if (previous == null)
				this.moves = 0;
			else
				this.moves = previous.moves + 1;
			this.cost += this.moves + board.manhattan();
		}

		@Override
		public int compareTo(Node o) {
			return cost - o.cost;
		}
	}

	private Node solution = null;

	public Solver(Board initial) // find a solution to the initial board (using
	// the A* algorithm)
	{
		MinPQ<Node> openList = new MinPQ<>();
		openList.insert(new Node(initial, null));
		// Test for solvable
		MinPQ<Node> openListTest = new MinPQ<>();
		openListTest.insert(new Node(initial.twin(), null));

		// Removes node with smaller cost
		while (true) {
			Node current = openList.delMin();
			Node currentTest = openListTest.delMin();
			// Check if it is solution
			if (current.board.isGoal()) {
				solution = current;
				return;
			} else if (currentTest.board.isGoal()) {
				// Not solvable
				solution = null;
				return;
			}
			// Get neighbors
			for (Board b : current.board.neighbors()) {
				if (current.previous == null || !b.equals(current.previous.board)) {
					openList.insert(new Node(b, current));
				}
			}
			// Get neighbors in test
			for (Board b : currentTest.board.neighbors()) {
				if (currentTest.previous == null || !b.equals(currentTest.previous.board)) {
					openListTest.insert(new Node(b, currentTest));
				}
			}
		}
	}

	public boolean isSolvable() // is the initial board solvable?
	{
		return solution != null;
	}

	public int moves() // min number of moves to solve initial board; -1 if
	// unsolvable
	{
		if (solution == null)
			return -1;
		return solution.moves;
	}

	public Iterable<Board> solution() // sequence of boards in a shortest
	// solution; null if unsolvable
	{
		if (solution == null)
			return null;
		Stack<Board> boards = new Stack<>();
		Node current = solution;
		while (current != null) {
			boards.push(current.board);
			current = current.previous;
		}
		return boards;
	}

	public static void main(String[] args) {

		// create initial board from file
		In in = new In(args[0]);
		int N = in.readInt();
		int[][] blocks = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);

		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}
	}
}
