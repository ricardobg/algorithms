import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {


	private Map<Integer, String> synsetsIds = new TreeMap<>();
	private Map<String, Set<Integer>> synsetsNameSet = new TreeMap<>();


	private SAP sap;

	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {

		if (synsets == null || hypernyms == null)
			throw new NullPointerException();

		In scan_synsets = new In(synsets);
		In scan_hypernyms = new In(hypernyms);


		while (scan_synsets.hasNextLine()) {
			String[] line = scan_synsets.readLine().split(",");
			int id = Integer.parseInt(line[0]);
			String words = line[1];

			synsetsIds.put(id, words);
			for (String word : words.split(" ")) {
				if (!synsetsNameSet.containsKey(word))
					synsetsNameSet.put(word, new TreeSet<>());
				synsetsNameSet.get(word).add(id);

			}
		}
		scan_synsets.close();

		//Initializes digraph
		Digraph graph = new Digraph(synsetsIds.size());

		while (scan_hypernyms.hasNextLine()) {
			String[] line = scan_hypernyms.readLine().split(",");
			int myId = Integer.parseInt(line[0]);
			for (int i = 1; i < line.length; i++)
				graph.addEdge(myId, Integer.parseInt(line[i]));

		}
		scan_hypernyms.close();

		//Check if is single rooted
		boolean[] is_root = new boolean[graph.V()];
		for (int i = 0; i < is_root.length; i++)
			is_root[i] = true;
		for (int i = 0; i < graph.V(); i++)
			if (graph.outdegree(i) != 0)
				is_root[i] = false;

		Queue<Integer> roots = new Queue<>();
		for (int i = 0; i < is_root.length; i++)
			if (is_root[i])
				roots.enqueue(i);

		if (roots.size() != 1)
			throw new IllegalArgumentException();
		//Verify if is DAG
		boolean[] visited = new boolean[graph.V()];
		Set<Integer> current = new TreeSet<>();
		for (int i = 0; i < visited.length; i++)
			visited[i] = false;

		for (int i : roots) {
			visit(i, graph, current, visited);
		}
		sap = new SAP(graph);
	}

	private static void visit(int visit, Digraph graph, Set<Integer> recursion, boolean[] visited) {
		recursion.add(visit);
		visited[visit] = true;
		for (int neighbor : graph.adj(visit)) {
			if (recursion.contains(neighbor))
				throw new IllegalArgumentException();
			if (!visited[neighbor])
				visit(neighbor, graph, recursion, visited);
		}
		recursion.remove(visit);

	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return synsetsNameSet.keySet();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word)
	{
		if (word == null)
			throw new NullPointerException();
		return synsetsNameSet.containsKey(word);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB)
	{
		if (!isNoun(nounA))
			throw new IllegalArgumentException();
		if (!isNoun(nounB))
			throw new IllegalArgumentException();

		Set<Integer> v1 = synsetsNameSet.get(nounA);
		Set<Integer> v2 = synsetsNameSet.get(nounB);

		return sap.length(v1, v2);
	}

	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB)
	{
		if (!isNoun(nounA))
			throw new IllegalArgumentException();
		if (!isNoun(nounB))
			throw new IllegalArgumentException();

		Set<Integer> v1 = synsetsNameSet.get(nounA);
		Set<Integer> v2 = synsetsNameSet.get(nounB);

		return synsetsIds.get(sap.ancestor(v1, v2));
	}

	// do unit testing of this class
	public static void main(String[] args)
	{
		WordNet net = new WordNet(args[0], args[1]);
		StdOut.print(net.distance("b", "c"));
	}
}
