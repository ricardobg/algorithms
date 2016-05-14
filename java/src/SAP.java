import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {


	private static final int UNSEEN_NODE = 1;
	private static final int V_NODE = 2;
	private static final int W_NODE = 4;
	private static final int V_START = 8;
	private static final int W_START = 16;
	
	
	private class Result {
		private int length;
		private int ancestor;

		public Result(int len, int anc) {
			this.length = len;
			this.ancestor = anc;
		}

		public void setIfBetter(int len, int anc) {
			if (this.length == -1 || this.ancestor == -1 || this.length > len) {
				this.length = len;
				this.ancestor = anc;
			}
		}
	};


	private Digraph digraph;
	private int[] visited;
	private int[] distancesFromV;
	private int[] distancesFromW;
	private Set<Integer> changedEntries = new TreeSet<>();

	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		if (G == null)
			throw new NullPointerException();
		digraph = new Digraph(G);
		visited = new int[digraph.V()];
		
		distancesFromV = new int[digraph.V()];
		distancesFromW = new int[digraph.V()];
		for (int i = 0; i < visited.length; i++) {
			visited[i] = UNSEEN_NODE;
			distancesFromV[i] = -1;
			distancesFromW[i] = -1;
		}
	}


	private Result ancestorAndDistance(int v, int w) {
		Queue<Integer> vs = new Queue<>();
		vs.enqueue(v);
		Queue<Integer> ws = new Queue<>();
		ws.enqueue(w);
		return ancestorAndDistance(vs, ws);
	}

	private Result ancestorAndDistance(Iterable<Integer> v, Iterable<Integer> w) {

		if (v == null || w == null)
			throw new NullPointerException();
		
		for (int i : changedEntries) {
			visited[i] = UNSEEN_NODE;
			distancesFromV[i] = -1;
			distancesFromW[i] = -1;
		}
		changedEntries.clear();
		
		//Breadth first search
		Queue<Integer> to_visit = new Queue<>();
		for (int i : v) {
			if (i < 0 || i >= digraph.V())
				throw new IndexOutOfBoundsException();
			visited[i] = V_NODE | V_START; 
			distancesFromV[i] = 0;
			to_visit.enqueue(i);
			changedEntries.add(i);
		}
		int alread_present = -1;
		for (int i : w) {
			if (i < 0 || i >= digraph.V())
				throw new IndexOutOfBoundsException();
			if ((visited[i] & V_NODE) != 0 && alread_present == -1)
				alread_present = i;
			visited[i] = W_NODE | W_START;
			distancesFromW[i] = 0;
			to_visit.enqueue(i);
			changedEntries.add(i);
		}

		if (alread_present != -1)
			return new Result(0, alread_present);

		Result min = new Result(-1, -1);
		int last_v = -1;
		while (!to_visit.isEmpty()) {
			int parent = to_visit.dequeue();
			for (int node : digraph.adj(parent)) {
				if ((visited[node] & UNSEEN_NODE) != 0 || 
						((visited[node] & (~UNSEEN_NODE) & (~V_START) & (~W_START)) & visited[parent]) 
						!= (visited[parent] & (~UNSEEN_NODE) & (~V_START) & (~W_START))) {
					if ((visited[node] & UNSEEN_NODE) != 0)
						to_visit.enqueue(node);
					visited[node] &= ~UNSEEN_NODE;
					visited[node] |= (visited[parent] & (~V_START) & (~W_START));

					//Updates distances
					if (distancesFromV[parent] != -1)
						distancesFromV[node] = min(distancesFromV[node] , distancesFromV[parent] + 1);
					if (distancesFromW[parent] != -1)
						distancesFromW[node] = min(distancesFromW[node] , distancesFromW[parent] + 1);
					changedEntries.add(node);

				}
				if (distancesFromV[node] != -1 && distancesFromW[node] != -1)
					min.setIfBetter(distancesFromV[node] + distancesFromW[node], node);
			} 
			if ((visited[parent] & V_NODE) != 0) {
				if (last_v != -1 && min.length != -1 && ((distancesFromV[last_v] != -1 && distancesFromV[last_v] > min.length)))
					break;
				last_v = parent;
			}
		}
		return min;
	}

	private static int min(int n1, int n2) {
		if (n1 == -1)
			return n2;
		if (n2 == -1)
			return n1;
		if (n1 <= n2)
			return n1;
		return n2;
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		return ancestorAndDistance(v, w).length;
	}

	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w) {
		return ancestorAndDistance(v, w).ancestor;
	}

	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		return ancestorAndDistance(v, w).length;
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		return ancestorAndDistance(v, w).ancestor;
	}

	// do unit testing of this class
	public static void main(String[] args) {
		In in = new In(args[0]);
		Digraph G = new Digraph(in);
		SAP sap = new SAP(G);
		while (!StdIn.isEmpty()) {
			int v = StdIn.readInt();
			int w1 = StdIn.readInt();
			//  int w2 = StdIn.readInt();
			Queue<Integer> vs = new Queue<>();
			vs.enqueue(v);
			Queue<Integer> ws = new Queue<>();
			ws.enqueue(w1);
			//ws.enqueue(w2);
			int length   = sap.length(vs, ws);
			int ancestor = sap.ancestor(vs, ws);
			StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		}
	}
}