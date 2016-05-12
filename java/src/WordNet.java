import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {


	private Map<Integer, String> synsets_ids = new TreeMap<>();
	private Map<String, Integer> synsets_name_set = new TreeMap<>();


	private Digraph graph;

	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		if (synsets == null || hypernyms == null)
			throw new NullPointerException();

		Scanner scan_synsets, scan_hypernyms;
		try {
			scan_synsets = new Scanner(new File(synsets));
			scan_synsets.useDelimiter(",");
			scan_hypernyms = new Scanner(new File(hypernyms));
			scan_hypernyms.useDelimiter(",");
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException();
		}

		while (scan_synsets.hasNextLine()) {
			String[] line = scan_synsets.nextLine().split(",");
			int id = Integer.parseInt(line[0]);
			String words = line[1];

			synsets_ids.put(id, words);
			for (String word : words.split(" "))
				synsets_name_set.put(word, id);
		}
		scan_synsets.close();

		//Initializes digraph
		graph = new Digraph(synsets_ids.size());

		while (scan_hypernyms.hasNextLine()) {
			String[] line = scan_hypernyms.nextLine().split(",");
			int myId = Integer.parseInt(line[0]);
			for (int i = 1; i < line.length; i++)
				graph.addEdge(myId, Integer.parseInt(line[1]));

		}
		scan_hypernyms.close();

		//TODO: check out if there is a cycle to throw 
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return synsets_name_set.keySet();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word)
	{
		if (word == null)
			throw new NullPointerException();
		return synsets_name_set.containsKey(word);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB)
	{
		if (!isNoun(nounA))
			throw new IllegalArgumentException();
		if (!isNoun(nounB))
			throw new IllegalArgumentException();

		int v1 = synsets_name_set.get(nounA);
		int v2 = synsets_name_set.get(nounB);

		if (v1 == v2)
			return 0;

		int[] visited = new int[graph.V()];
		int[] distances = new int[graph.V()];
		for (int i = 0; i < visited.length; i++) {
			visited[i] = -1;
			distances[i] = 0;
		}

		visited[v1] = v1;
		visited[v2] = v2;

		//Breadth first search
		Queue<Integer> to_visit_v1 = new Queue<>();
		Queue<Integer> to_visit_v2 = new Queue<>();
		to_visit_v1.enqueue(v1);
		to_visit_v2.enqueue(v2);

		while (!to_visit_v1.isEmpty() || !to_visit_v2.isEmpty()) {
			if (!to_visit_v1.isEmpty()) {
				int parent = to_visit_v1.dequeue();
				for (int node : graph.adj(parent)) {
					if (visited[node] == -1) {
						visited[node] = v1;
						distances[node] = distances[parent] + 1;
						to_visit_v1.enqueue(node);
					}
					else if (visited[node] == v2) {
						//Find visited node by v2
						return distances[parent] + 1 + distances[node];
					}
					//Already visited: do nothing
				}
			}
			if (!to_visit_v2.isEmpty()) {
				int parent = to_visit_v2.dequeue();
				for (int node : graph.adj(parent)) {
					if (visited[node] == -1) {
						visited[node] = v2;
						distances[node] = distances[parent] + 1;
						to_visit_v2.enqueue(node);
					}
					else if (visited[node] == v1) {
						//Find visited node by v1
						return distances[parent] + 1 + distances[node];
					}
					//Already visited: do nothing
				}
			}
		}

		return -1;
	}

	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB)
	{
		if (!isNoun(nounA))
			throw new IllegalArgumentException();
		if (!isNoun(nounB))
			throw new IllegalArgumentException();

		int v1 = synsets_name_set.get(nounA);
		int v2 = synsets_name_set.get(nounB);

		if (v1 == v2)
			return synsets_ids.get(v1);

		int[] visited = new int[graph.V()];
		for (int i = 0; i < visited.length; i++)
			visited[i] = -1;

		visited[v1] = v1;
		visited[v2] = v2;

		//Breadth first search
		Queue<Integer> to_visit_v1 = new Queue<>();
		Queue<Integer> to_visit_v2 = new Queue<>();
		to_visit_v1.enqueue(v1);
		to_visit_v2.enqueue(v2);

		while (!to_visit_v1.isEmpty() || !to_visit_v2.isEmpty()) {
			if (!to_visit_v1.isEmpty()) {
				for (int node : graph.adj(to_visit_v1.dequeue())) {
					if (visited[node] == -1) {
						visited[node] = v1;
						to_visit_v1.enqueue(node);
					}
					else if (visited[node] == v2) {
						//Find visited node by v2
						return synsets_ids.get(node);
					}
					//Already visited: do nothing
				}
			}
			if (!to_visit_v2.isEmpty()) {
				for (int node : graph.adj(to_visit_v2.dequeue())) {
					if (visited[node] == -1) {
						visited[node] = v2;
						to_visit_v2.enqueue(node);
					}
					else if (visited[node] == v1) {
						//Find visited node by v1
						return synsets_ids.get(node);
					}
					//Already visited: do nothing
				}
			}
		}

		return null;
	}

	// do unit testing of this class
	public static void main(String[] args)
	{
		WordNet net = new WordNet(args[0], args[1]);
		StdOut.print(net.distance("a", "f"));
	}
}
