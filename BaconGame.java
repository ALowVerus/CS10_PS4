import java.io.*;
import java.util.*;

import sun.misc.VM;


public class BaconGame {
	
	public static BufferedReader buffer(String fileAddress) {
		TreeMap thisMap = new TreeMap();
		try {
			return new BufferedReader(new FileReader(fileAddress));
		}
		catch (FileNotFoundException e) { System.out.println("File not found."); }
		catch (IOException e) { System.out.println("Index out of bounds.");	}
		return null;
	}
	
	public static TreeMap<String, Actor> makeMap(String fileAddress) {
		BufferedReader file = buffer(fileAddress);
		TreeMap<String, Actor> thisMap = new TreeMap();
		String line;
		try {
			while ((line = file.readLine()) != null) {
				String[] thisLine = line.split("\\|");
				thisMap.put(thisLine[0], new Actor(thisLine[1]));
			}
		}
		catch (IOException e){ System.out.println("Line failed to read."); }
		return thisMap;
	}
	
	public static AdjacencyMapGraph<Actor, String> makeGraph(TreeMap<String, Actor> actorMap, TreeMap<String, Actor> movieMap) {		
		AdjacencyMapGraph<Actor, String> thisGraph = new AdjacencyMapGraph<Actor, String>();
		for (String actorKey : actorMap.keySet()) {thisGraph.insertVertex(actorMap.get(actorKey));}
		for (String movieKey : movieMap.keySet()) {thisGraph.insertVertex(movieMap.get(movieKey));}
		
		// Add links from actors to movie objects
		BufferedReader lineFile = buffer("inputs/ps4/movie-actorsTest.txt"); String line;
		try {
			while ((line = lineFile.readLine()) != null ) {
				String[] thisLine = line.split("\\|");
				Actor movie = movieMap.get(thisLine[0]);
				Actor actor = actorMap.get(thisLine[1]);
				thisGraph.insertUndirected(actor, movie, "Bump");
				// System.out.println("link from " + movie.getName() + " to " + actor.getName());
			}
		}
		catch (IOException e) { System.out.println("Line failed to read."); }
		
		// Consolidate the movie objects, turning them into the names of direct links from actor to actor
		for (String movieKey : movieMap.keySet()) {
			Iterable<Actor> neighbors = thisGraph.inNeighbors(movieMap.get(movieKey));
			for (Actor actor : neighbors) {
				for (Actor neighbor : neighbors) {
					if (actor != neighbor && !thisGraph.hasEdge(actor, neighbor)) {
						thisGraph.insertUndirected(actor, neighbor, movieMap.get(movieKey).getName());
						// System.out.println("link from " + neighbor.getName() + " to " + actor.getName());
					}
				}
			}
		}

		return thisGraph;
	}
	
	public static <V,E> AdjacencyMapGraph<Actor, String> bfs(Graph<Actor, String> g, Actor source) {
		LinkedQueue<Actor> queue = new LinkedQueue<Actor>();
		queue.enqueue(source);
		Actor current = null;
		AdjacencyMapGraph<Actor, String> universeGraph = new AdjacencyMapGraph<Actor, String>();
		while (!queue.isEmpty()) {
			current = queue.dequeue();
			for (Actor neighbor : g.inNeighbors(current)) {
				if (!universeGraph.hasVertex(neighbor)) {
					universeGraph.insertVertex(neighbor);
					universeGraph.insertDirected(current, neighbor, g.getLabel(current, neighbor));
				}
			}
		}
		return universeGraph;
	}
	public static <V,E> ArrayList<String> getPath(Graph<Actor, String> tree, Actor origin) {
		ArrayList<String> pathConnectionStrings = new ArrayList<String>();
		Actor current = origin;
		while (tree.outNeighbors(current) != null) {
			Actor next = tree.outNeighbors(current).iterator().next();
			pathConnectionStrings.add(current.getName() + " was in " + next.getName() + " in " + tree.getLabel(current, next) + ".");
			current = next;
		}
		return pathConnectionStrings;
	}
	public static <V,E> Set<Actor> missingVertices(Graph<Actor,String> graph, Graph<Actor,String> subgraph) {
		TreeSet<Actor> thisSet = new TreeSet<Actor>();
		Iterator<Actor> iterator;
		iterator = graph.vertices().iterator();
		while (iterator.hasNext()) { thisSet.add(iterator.next()); }
		iterator = subgraph.vertices().iterator();
		while (iterator.hasNext()) { thisSet.remove(iterator.next()); }
		return thisSet;
	}
	
	// I have no idea what this is supposed to be.
	public static <V,E> double averageSeparation(Graph<V,E> tree, V root) {
		return 0;
	}

	public static void main(String args[]) throws IOException{
		// Make referencable maps from the input files
		TreeMap<String, Actor> actorMap = makeMap("inputs/ps4/actorsTest.txt");
		TreeMap<String, Actor> movieMap = makeMap("inputs/ps4/moviesTest.txt");
		AdjacencyMapGraph<Actor, String> thisGraph = makeGraph(actorMap, movieMap);
		while (true) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Enter a source: \n");
			String sourceName = reader.readLine();
			System.out.println("Enter a target: ");
			String targetName = reader.readLine();
			if (actorMap.get(sourceName) == null || actorMap.get(targetName) == null) {
				System.out.println("An input was invalid.");
			}
			else if (sourceName == targetName) {
				System.out.println("Inputs are the same person.");
			}
			else {
				System.out.println("Making connection from " + sourceName + " to " + targetName + ". \n");
				AdjacencyMapGraph<Actor, String> tree = bfs(thisGraph, actorMap.get(targetName));
				List<String> pathConnectionStrings = getPath(tree, actorMap.get(sourceName));
				Set<Actor> missingActors = missingVertices(thisGraph, tree);
				
			}
		}
	}	
}
