import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

	private Digraph digraph;
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

		int[] visited = new int[digraph.V()];
		int[] distances = new int[digraph.V()];
		for (int i = 0; i < visited.length; i++) {
			visited[i] = -1;
			distances[i] = 0;
		}
		visited[v] = v;
		visited[w] = w;

		//Breadth first search
		Queue<Integer> to_visit_v = new Queue<>();
		Queue<Integer> to_visit_w = new Queue<>();
		to_visit_v.enqueue(v);
		to_visit_w.enqueue(w);

		while (!to_visit_v.isEmpty() || !to_visit_w.isEmpty()) {
			if (!to_visit_v.isEmpty()) {
				int parent = to_visit_v.dequeue();
				for (int node : digraph.adj(parent)) {
					if (visited[node] == -1) {
						visited[node] = v;
						distances[node] = distances[parent] + 1;
						to_visit_v.enqueue(node);
					}
					else if (visited[node] == w) {
						//Find visited node by v2
						return distances[parent] + 1 + distances[node];
					}
					//Already visited: do nothing
				}
			}
			if (!to_visit_w.isEmpty()) {
				int parent = to_visit_w.dequeue();
				for (int node : digraph.adj(parent)) {
					if (visited[node] == -1) {
						visited[node] = w;
						distances[node] = distances[parent] + 1;
						to_visit_w.enqueue(node);
					}
					else if (visited[node] == v) {
						//Find visited node by v1
						return distances[parent] + 1 + distances[node];
					}
					//Already visited: do nothing
				}
			}
		}

		return -1;
	}

	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w) {
		int[] visited = new int[digraph.V()];
		for (int i = 0; i < visited.length; i++)
			visited[i] = -1;
		visited[v] = v;
		visited[w] = w;

		//Breadth first search
		Queue<Integer> to_visit_v = new Queue<>();
		Queue<Integer> to_visit_w = new Queue<>();
		to_visit_v.enqueue(v);
		to_visit_w.enqueue(w);

		while (!to_visit_v.isEmpty() || !to_visit_w.isEmpty()) {
			if (!to_visit_v.isEmpty()) {
				int parent = to_visit_v.dequeue();
				for (int node : digraph.adj(parent)) {
					if (visited[node] == -1) {
						visited[node] = v;
						to_visit_v.enqueue(node);
					}
					else if (visited[node] == w) {
						//Find visited node by v2
						return node;
					}
					//Already visited: do nothing
				}
			}
			if (!to_visit_w.isEmpty()) {
				int parent = to_visit_w.dequeue();
				for (int node : digraph.adj(parent)) {
					if (visited[node] == -1) {
						visited[node] = w;
						to_visit_w.enqueue(node);
					}
					else if (visited[node] == v) {
						//Find visited node by v1
						return node;
					}
					//Already visited: do nothing
				}
			}
		}

		return -1;
	}

	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		int[] visited = new int[digraph.V()];
		int[] distances = new int[digraph.V()];
		for (int i = 0; i < visited.length; i++) {
			visited[i] = -1;
			distances[i] = 0;
		}

		//Breadth first search
		Queue<Integer> to_visit_v = new Queue<>();
		Queue<Integer> to_visit_w = new Queue<>();
		//-2 for v nodes and -3 for w nodes
		for (int i : v) {
			visited[i] = -2;
			to_visit_v.enqueue(i);
		}
		for (int i : w) {
			if (visited[i] == -2)
				return 0;
			visited[i] = -3;
			to_visit_w.enqueue(i);
		}

		while (!to_visit_v.isEmpty() || !to_visit_w.isEmpty()) {
			if (!to_visit_v.isEmpty()) {
				int parent = to_visit_v.dequeue();
				for (int node : digraph.adj(parent)) {
					if (visited[node] == -1) {
						visited[node] = -2;
						distances[node] = distances[parent] + 1;
						to_visit_v.enqueue(node);
					}
					else if (visited[node] == -3) {
						//Find visited node by v2
						return distances[parent] + 1 + distances[node];
					}
					//Already visited: do nothing
				}
			}
			if (!to_visit_w.isEmpty()) {
				int parent = to_visit_w.dequeue();
				for (int node : digraph.adj(parent)) {
					if (visited[node] == -1) {
						visited[node] = -3;
						distances[node] = distances[parent] + 1;
						to_visit_w.enqueue(node);
					}
					else if (visited[node] == -2) {
						//Find visited node by v1
						return distances[parent] + 1 + distances[node];
					}
					//Already visited: do nothing
				}
			}
		}

		return -1;
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		int[] visited = new int[digraph.V()];
		for (int i = 0; i < visited.length; i++)
			visited[i] = -1;

		//Breadth first search
		Queue<Integer> to_visit_v = new Queue<>();
		Queue<Integer> to_visit_w = new Queue<>();
		//-2 for v nodes and -3 for w nodes
		for (int i : v) {
			visited[i] = -2;
			to_visit_v.enqueue(i);
		}
		for (int i : w) {
			if (visited[i] == -2)
				return 0;
			visited[i] = -3;
			to_visit_w.enqueue(i);
		}

		while (!to_visit_v.isEmpty() || !to_visit_w.isEmpty()) {
			if (!to_visit_v.isEmpty()) {
				int parent = to_visit_v.dequeue();
				for (int node : digraph.adj(parent)) {
					if (visited[node] == -1) {
						visited[node] = -2;
						to_visit_v.enqueue(node);
					}
					else if (visited[node] == -3) {
						//Find visited node by v2
						return node;
					}
					//Already visited: do nothing
				}
			}
			if (!to_visit_w.isEmpty()) {
				int parent = to_visit_w.dequeue();
				for (int node : digraph.adj(parent)) {
					if (visited[node] == -1) {
						visited[node] = -3;
						to_visit_w.enqueue(node);
					}
					else if (visited[node] == -2) {
						//Find visited node by v1
						return node;
					}
					//Already visited: do nothing
				}
			}
		}
		return -1;
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