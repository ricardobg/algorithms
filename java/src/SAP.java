import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

	private Digraph digraph;
	
	private class Result {
		private int length;
		private int ancestor;
		
		public Result(int length, int ancestor) {
			this.length = length;
			this.ancestor = ancestor;
		}
		
		public void setIfBetter(int length, int ancestor) {
			if (this.length == -1 || this.ancestor == -1 || this.length > length) {
				this.length = length;
				this.ancestor = ancestor;
			}
		}
	};
	
	private static final int UNSEEN_NODE = -1;
	private static final int V_NODE = -2;
	private static final int W_NODE = -3;
	
	private Result ancestor_and_distance(int v, int w) {
		Queue<Integer> vs = new Queue<>();
		vs.enqueue(v);
		Queue<Integer> ws = new Queue<>();
		ws.enqueue(w);
		return ancestor_and_distance(vs, ws);
	}
	private Result ancestor_and_distance(Iterable<Integer> v, Iterable<Integer> w) {
		
		if (v == null || w == null)
			throw new NullPointerException();
		
		int[] visited = new int[digraph.V()];
		int[] distances = new int[digraph.V()];
		for (int i = 0; i < visited.length; i++) {
			visited[i] = UNSEEN_NODE;
			distances[i] = 0;
		}

		//Breadth first search
		Queue<Integer> to_visit = new Queue<>();
		for (int i : v) {
			if (i < 0 || i >= digraph.V())
				throw new IndexOutOfBoundsException();
			visited[i] = V_NODE;
			to_visit.enqueue(i);
		}
		int alread_present = UNSEEN_NODE;
		for (int i : w) {
			if (i < 0 || i >= digraph.V())
				throw new IndexOutOfBoundsException();
			if (visited[i] == V_NODE && alread_present == -1)
				alread_present = i;
			visited[i] = W_NODE;
			to_visit.enqueue(i);
		}
		
		if (alread_present != UNSEEN_NODE)
			return new Result(0, alread_present);
		
		Result min = new Result(-1, -1);
		while (!to_visit.isEmpty()) {
			int parent = to_visit.dequeue();
			for (int node : digraph.adj(parent)) {
				if (visited[node] == -1) {
					visited[node] = visited[parent];
					distances[node] = distances[parent] + 1;
					to_visit.enqueue(node);
				}
				else if (visited[node] != visited[parent]) {
					//Find visited node by w
					min.setIfBetter(distances[parent] + 1 + distances[node], node);
				}
				//Already visited: do nothing
			}
		}
		
		return min;
	}
	
	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		if (G == null)
			throw new NullPointerException();
		digraph = new Digraph(G.V());
		for (int i = 0; i < G.V(); i++) {
			for (int j : G.adj(i)) 
				digraph.addEdge(i, j);
		}
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		return ancestor_and_distance(v, w).length;
	}

	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w) {
		return ancestor_and_distance(v, w).ancestor;
	}

	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		return ancestor_and_distance(v, w).length;
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		return ancestor_and_distance(v, w).ancestor;
	}

	// do unit testing of this class
	public static void main(String[] args) {
		In in = new In(args[0]);
		Digraph G = new Digraph(in);
		SAP sap = new SAP(G);
		while (!StdIn.isEmpty()) {
			int v = StdIn.readInt();
			int w = StdIn.readInt();
			int length   = sap.length(v, w);
			int ancestor = sap.ancestor(v, w);
			StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		}
	}
}