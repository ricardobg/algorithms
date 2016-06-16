/******************************************************************************
 *  Compilation:  javac TrieSET.java
 *  Execution:    java TrieSET < words.txt
 *  Dependencies: StdIn.java
 *
 *  An set for extended ASCII strings, implemented  using a 256-way trie.
 *
 *  Sample client reads in a list of words from standard input and
 *  prints out each word, removing any duplicates.
 *
 ******************************************************************************/
import java.util.Iterator;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class MyTrie implements Iterable<String> {

	private static final int R = 26;        // extended ASCII

	private Node root;      // root of trie
	private int N;          // number of keys in trie

	// R-way trie node
	public static class Node {
		private Node[] next = new Node[R];
		private boolean isString;
		public boolean isWord() { return isString; }
	}

	/**
	 * Initializes an empty set of strings.
	 */
	public MyTrie() {
	}
	
	public Node root() {
		return root;
	}

	/**
	 * Does the set contain the given key?
	 * @param key the key
	 * @return <tt>true</tt> if the set contains <tt>key</tt> and
	 *     <tt>false</tt> otherwise
	 * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
	 */
	public boolean contains(String key) {
		Node x = get(root, key, 0);
		if (x == null) return false;
		return x.isString;
	}

	private Node get(Node x, String key, int d) {
		if (x == null) return null;
		if (d == key.length()) return x;
		char c = key.charAt(d);
		return get(x.next[c - 'A'], key, d+1);
	}

	/**
	 * Does the set contain words with the prefix?
	 * @param prefix
	 * @return <tt>Node</tt> if the set contains  words with the <tt>prefix</tt> and
	 *     <tt>null</tt> otherwise
	 * @throws NullPointerException if <tt>prefix</tt> is <tt>null</tt>
	 */
	public Node prefix(String prefix) {
		return prefix(root, prefix);
	}
	

	/**
	 * Does the set contain words with the prefix?
	 * @param from the start node to search prefix 
	 * @param prefix
	 * @return <tt>Node</tt> if the set contains  words with the <tt>prefix</tt> and
	 *     <tt>null</tt> otherwise
	 * @throws NullPointerException if <tt>prefix</tt> is <tt>null</tt>
	 */
	public Node prefix(Node from, String prefix) {
		return get(from, prefix, 0);
	}

	/**
	 * Adds the key to the set if it is not already present.
	 * @param key the key to add
	 * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
	 */
	public void add(String key) {
		root = add(root, key, 0);
	}

	private Node add(Node x, String key, int d) {
		if (x == null) x = new Node();
		if (d == key.length()) {
			if (!x.isString) N++;
			x.isString = true;
		}
		else {
			char c = key.charAt(d);
			x.next[c - 'A'] = add(x.next[c - 'A'], key, d+1);
		}
		return x;
	}

	/**
	 * Returns the number of strings in the set.
	 * @return the number of strings in the set
	 */
	public int size() {
		return N;
	}

	/**
	 * Is the set empty?
	 * @return <tt>true</tt> if the set is empty, and <tt>false</tt> otherwise
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Returns all of the keys in the set, as an iterator.
	 * To iterate over all of the keys in a set named <tt>set</tt>, use the
	 * foreach notation: <tt>for (Key key : set)</tt>.
	 * @return an iterator to all of the keys in the set
	 */
	public Iterator<String> iterator() {
		return keysWithPrefix("").iterator();
	}

	/**
	 * Returns all of the keys in the set that start with <tt>prefix</tt>.
	 * @param prefix the prefix
	 * @return all of the keys in the set that start with <tt>prefix</tt>,
	 *     as an iterable
	 */
	public Iterable<String> keysWithPrefix(String prefix) {
		Queue<String> results = new Queue<String>();
		Node x = get(root, prefix, 0);
		collect(x, new StringBuilder(prefix), results);
		return results;
	}

	private void collect(Node x, StringBuilder prefix, Queue<String> results) {
		if (x == null) return;
		if (x.isString) results.enqueue(prefix.toString());
		for (char c = 0; c < R; c++) {
			prefix.append(c);
			collect(x.next[c], prefix, results);
			prefix.deleteCharAt(prefix.length() - 1);
		}
	}

	/**
	 * Returns all of the keys in the set that match <tt>pattern</tt>,
	 * where . symbol is treated as a wildcard character.
	 * @param pattern the pattern
	 * @return all of the keys in the set that match <tt>pattern</tt>,
	 *     as an iterable, where . is treated as a wildcard character.
	 */  
	public Iterable<String> keysThatMatch(String pattern) {
		Queue<String> results = new Queue<String>();
		StringBuilder prefix = new StringBuilder();
		collect(root, prefix, pattern, results);
		return results;
	}

	private void collect(Node x, StringBuilder prefix, String pattern, Queue<String> results) {
		if (x == null) return;
		int d = prefix.length();
		if (d == pattern.length() && x.isString)
			results.enqueue(prefix.toString());
		if (d == pattern.length())
			return;
		char c = pattern.charAt(d);
		if (c == '.') {
			for (char ch = 0; ch < R; ch++) {
				prefix.append(ch);
				collect(x.next[ch], prefix, pattern, results);
				prefix.deleteCharAt(prefix.length() - 1);
			}
		}
		else {
			prefix.append(c);
			collect(x.next[c], prefix, pattern, results);
			prefix.deleteCharAt(prefix.length() - 1);
		}
	}

	/**
	 * Returns the string in the set that is the longest prefix of <tt>query</tt>,
	 * or <tt>null</tt>, if no such string.
	 * @param query the query string
	 * @return the string in the set that is the longest prefix of <tt>query</tt>,
	 *     or <tt>null</tt> if no such string
	 * @throws NullPointerException if <tt>query</tt> is <tt>null</tt>
	 */
	public String longestPrefixOf(String query) {
		int length = longestPrefixOf(root, query, 0, -1);
		if (length == -1) return null;
		return query.substring(0, length);
	}

	// returns the length of the longest string key in the subtrie
	// rooted at x that is a prefix of the query string,
	// assuming the first d character match and we have already
	// found a prefix match of length length
	private int longestPrefixOf(Node x, String query, int d, int length) {
		if (x == null) return length;
		if (x.isString) length = d;
		if (d == query.length()) return length;
		char c = query.charAt(d);
		return longestPrefixOf(x.next[c], query, d+1, length);
	}

	/**
	 * Removes the key from the set if the key is present.
	 * @param key the key
	 * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
	 */
	public void delete(String key) {
		root = delete(root, key, 0);
	}

	private Node delete(Node x, String key, int d) {
		if (x == null) return null;
		if (d == key.length()) {
			if (x.isString) N--;
			x.isString = false;
		}
		else {
			char c = key.charAt(d);
			x.next[c] = delete(x.next[c], key, d+1);
		}

		// remove subtrie rooted at x if it is completely empty
		if (x.isString) return x;
		for (int c = 0; c < R; c++)
			if (x.next[c] != null)
				return x;
		return null;
	}


	/**
	 * Unit tests the <tt>TrieSET</tt> data type.
	 */
	public static void main(String[] args) {
		MyTrie set = new MyTrie();
		while (!StdIn.isEmpty()) {
			String key = StdIn.readString();
			set.add(key);
		}

		// print results
		if (set.size() < 100) {
			StdOut.println("keys(\"\"):");
			for (String key : set) {
				StdOut.println(key);
			}
			StdOut.println();
		}

		StdOut.println("longestPrefixOf(\"shellsort\"):");
		StdOut.println(set.longestPrefixOf("shellsort"));
		StdOut.println();

		StdOut.println("longestPrefixOf(\"xshellsort\"):");
		StdOut.println(set.longestPrefixOf("xshellsort"));
		StdOut.println();

		StdOut.println("keysWithPrefix(\"shor\"):");
		for (String s : set.keysWithPrefix("shor"))
			StdOut.println(s);
		StdOut.println();

		StdOut.println("keysWithPrefix(\"shortening\"):");
		for (String s : set.keysWithPrefix("shortening"))
			StdOut.println(s);
		StdOut.println();

		StdOut.println("keysThatMatch(\".he.l.\"):");
		for (String s : set.keysThatMatch(".he.l."))
			StdOut.println(s);
	}
}

/******************************************************************************
 *  Copyright 2002-2015, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
