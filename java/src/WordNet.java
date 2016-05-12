import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {


	private Map<Integer, String> synsets_ids = new TreeMap<>();
	private Map<String, Integer> synsets_name_set = new TreeMap<>();


	private SAP sap;

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
		Digraph graph = new Digraph(synsets_ids.size());

		while (scan_hypernyms.hasNextLine()) {
			String[] line = scan_hypernyms.nextLine().split(",");
			int myId = Integer.parseInt(line[0]);
			for (int i = 1; i < line.length; i++)
				graph.addEdge(myId, Integer.parseInt(line[1]));

		}
		scan_hypernyms.close();
		sap = new SAP(graph);
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

		int v1 = synsets_name_set.get(nounA);
		int v2 = synsets_name_set.get(nounB);

		return synsets_ids.get(sap.ancestor(v1, v2));
	}

	// do unit testing of this class
	public static void main(String[] args)
	{
		WordNet net = new WordNet(args[0], args[1]);
		StdOut.print(net.distance("a", "f"));
	}
}
