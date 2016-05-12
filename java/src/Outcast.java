import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

	private WordNet wordnet;
	public Outcast(WordNet wordnet)         // constructor takes a WordNet object
	{
		this.wordnet = wordnet;
	}

	public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
	{
		int max = -1;
		String outcast = "";
		for (String noun : nouns) {
			int temp = 0;
			for (String cmp : nouns) {
				temp += wordnet.distance(noun, cmp);
			}
			if (temp > max) {
				max = temp;
				outcast = noun;
			}
		}
		return outcast;
	}

	public static void main(String[] args) {
		WordNet wordnet = new WordNet(args[0], args[1]);
		Outcast outcast = new Outcast(wordnet);
		for (int t = 2; t < args.length; t++) {
			In in = new In(args[t]);
			String[] nouns = in.readAllStrings();
			StdOut.println(args[t] + ": " + outcast.outcast(nouns));
		}
	}
}