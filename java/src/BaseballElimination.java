import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
	
	private class TeamData {
		private String name;
		private int wins;
		private int losses;
		private int remaining;
		private int nextGames[];
		
		public TeamData (String name, int wins, int losses, int remaining, int nextGames[]) {
			this.name = name;
			this.wins = wins;
			this.losses = losses;
			this.remaining = remaining;
			this.nextGames = nextGames;
		}
	}
	
	private TeamData[] teamsData;
	private Map<String, Integer> teams;
	private List<List<String>> eliminated;
	
	public BaseballElimination(String filename)                    // create a baseball division from given filename in format specified below
	{
		In input = new In(filename);
		int nTeams = input.readInt();
		teams = new TreeMap<>();
		teamsData = new TeamData[nTeams];
		for (int i = 0; i < nTeams; i++) {
			String team = input.readString();
			int wins = input.readInt();
			int losses = input.readInt();
			int remaining = input.readInt();
			int games[] = new int[nTeams];
			for (int j = 0; j < nTeams; j++) {
				games[j] = input.readInt();
			}
			teams.put(team, i);
			teamsData[i] = new TeamData(team, wins, losses, remaining, games);
		}
		calculateEliminated();
	}
	
	private void calculateEliminated() {
		eliminated = new ArrayList<List<String>>(numberOfTeams());
		for (int i = 0; i < numberOfTeams(); i++) {
			eliminated.add(new LinkedList<>());
			
			//Check to see if it is cannot be champion
			for (int j = 0; j < numberOfTeams(); j++)
				if (j != i)
					if (teamsData[j].wins > teamsData[i].wins + teamsData[i].remaining)
						eliminated.get(i).add(teamsData[j].name);
				
			//Eliminated by number already
			if (eliminated.get(i).size() > 0)
				continue;
			int v = numberOfTeams() + 1;
			int nGames = 0;
			//Add games vertices
			for (int j = 0; j < numberOfTeams(); j++) {
				if (j != i) {
					for (int k = 0; k < numberOfTeams(); k++) {
						if (k != i && teamsData[j].nextGames[k] > 0)
							nGames++;
					}
				}
			}
			nGames /= 2;
			//s + t + teams vertices
			v += nGames;
			FlowNetwork fn = new FlowNetwork(v);
			int current_game = numberOfTeams();
			//Iterate over teams
			for (int j = 0; j < numberOfTeams(); j++) {
				if (j != i) {
					//Iterate over each reamining game
					int[] games = teamsData[j].nextGames;
					for (int g = j+1; g < games.length; g++) {
						//Only if is not i and has games to play
						if (g != i && games[g] > 0) {
							//From s to game
							fn.addEdge(new FlowEdge(i, current_game, games[g]));
							//From game to teams
							fn.addEdge(new FlowEdge(current_game, j, Double.POSITIVE_INFINITY));
							fn.addEdge(new FlowEdge(current_game, g, Double.POSITIVE_INFINITY));
							current_game++;
						}
					}
					//Add edges from teams to sink (t)
					int weight = teamsData[i].wins + teamsData[i].remaining - teamsData[j].wins;
					fn.addEdge(new FlowEdge(j, v - 1, weight > 0 ? weight : 0));
				}
			}
			// 0 to nTeams-1 are teams
			// s is i, v-1 is t
			// nTeams to nTeams+games are games
			/*StdOut.println("Team " + i + ": " + teamsData[i].name);
			StdOut.println("Wins = " + teamsData[i].wins);
			StdOut.println("Losses = " + teamsData[i].losses);
			StdOut.println("Remaining = " + teamsData[i].remaining);
			StdOut.println("Games = " + nGames);;
			StdOut.print(fn.toString());
			
			StdOut.println("MaxFlow = " + ff.value());*/
			FordFulkerson ff = new FordFulkerson(fn, i, v - 1);
			//Iterate over teams
			for (int j = 0; j < numberOfTeams(); j++) {
				if (j != i) {
					if (ff.inCut(j)) {
						//j is responsible for elimination in some way
						eliminated.get(i).add(teamsData[j].name);
					}
				}
			}
		}
	}
	
	public int numberOfTeams()                        // number of teams
	{
		return teamsData.length;
	}
	
	public Iterable<String> teams()                                // all teams
	{
		ArrayList<String> teams = new ArrayList<>(numberOfTeams());
		for (int i = 0; i < numberOfTeams(); i++)
			teams.add(teamsData[i].name);
		return teams;
	}
	
	public int wins(String team)                      // number of wins for given team
	{
		if (!teams.containsKey(team))
			throw new IllegalArgumentException();
		return teamsData[teams.get(team)].wins;
	}
	
	public int losses(String team)                    // number of losses for given team
	{
		if (!teams.containsKey(team))
			throw new IllegalArgumentException();
		return teamsData[teams.get(team)].losses;
	}
	
	public int remaining(String team)                 // number of remaining games for given team
	{
		if (!teams.containsKey(team))
			throw new IllegalArgumentException();
		return teamsData[teams.get(team)].remaining;
	}
	
	public int against(String team1, String team2)    // number of remaining games between team1 and team2
	{
		if (!teams.containsKey(team1) || !teams.containsKey(team2))
			throw new IllegalArgumentException();
		return teamsData[teams.get(team1)].nextGames[teams.get(team2)];
	}
	
	public boolean isEliminated(String team)              // is given team eliminated?
	{
		if (!teams.containsKey(team))
			throw new IllegalArgumentException();
		return eliminated.get(teams.get(team)).size() > 0;
	}
	public Iterable<String> certificateOfElimination(String team)  // subset R of teams that eliminates given team; null if not eliminated
	{
		if (!isEliminated(team))
			return null;
		return eliminated.get(teams.get(team));
	}
	
	public static void main(String[] args) {
	    BaseballElimination division = new BaseballElimination(args[0]);
	    for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team)) {
	                StdOut.print(t + " ");
	            }
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }
	}

}
