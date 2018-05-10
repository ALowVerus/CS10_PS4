import java.io.*;
import java.util.*;

public class BaconGame {
	
	// For generating the graph.
	public static BufferedReader buffer(String fileAddress) {
		TreeMap thisMap = new TreeMap();
		try {
			return new BufferedReader(new FileReader(fileAddress));
		}
		catch (FileNotFoundException e) { System.out.println("File not found."); }
		catch (IOException e) { System.out.println("Index out of bounds.");	}
		return null;
	}
	public static TreeMap<Integer, String> makeMap(String fileAddress) {
		BufferedReader file = buffer(fileAddress);
		TreeMap<Integer, String> thisMap = new TreeMap<Integer, String>();
		String line;
		try {
			while ((line = file.readLine()) != null) {
				String[] thisLine = line.split("\\|");
				thisMap.put(Integer.valueOf(thisLine[0]), thisLine[1]);
			}
		}
		catch (IOException e){ System.out.println("Line failed to read."); }
		return thisMap;
	}
	public static AdjacencyMapGraph<String, String> makeGraph() {	
		TreeMap<Integer, String> actorMap = makeMap("inputs/ps4/actorsTest.txt");
		TreeMap<Integer, String> movieMap = makeMap("inputs/ps4/moviesTest.txt");
		AdjacencyMapGraph<String, String> thisGraph = new AdjacencyMapGraph<String, String>();
		for (Integer actorKey : actorMap.keySet()) {thisGraph.insertVertex(actorMap.get(actorKey));}
		for (Integer movieKey : movieMap.keySet()) {thisGraph.insertVertex(movieMap.get(movieKey));}
		
		// Add links from actors to movie objects
		BufferedReader lineFile = buffer("inputs/ps4/movie-actorsTest.txt"); String line;
		try {
			while ((line = lineFile.readLine()) != null ) {
				String[] thisLine = line.split("\\|");
				String movie = movieMap.get(Integer.valueOf(thisLine[0]));
				String actor = actorMap.get(Integer.valueOf(thisLine[1]));
				thisGraph.insertUndirected(actor, movie, "Bump");
				// System.out.println("link from " + movie.getName() + " to " + actor.getName());
			}
		}
		catch (IOException e) { System.out.println("Line failed to read."); }
		
		// Consolidate the movie objects, turning them into the names of direct links from actor to actor
		for (Integer movieKey : movieMap.keySet()) {
			Iterable<String> neighbors = thisGraph.inNeighbors(movieMap.get(movieKey));
			for (String actor : neighbors) {
				for (String neighbor : neighbors) {
					if (actor != neighbor && !thisGraph.hasEdge(actor, neighbor)) {
						thisGraph.insertUndirected(actor, neighbor, movieMap.get(movieKey));
						// System.out.println("link from " + neighbor.getName() + " to " + actor.getName());
					}
				}
			}
			thisGraph.removeVertex(movieMap.get(movieKey));
		}
		return thisGraph;
	}
	
	// Return a graph with all vertices leading back to the source.
	public static <V,E> AdjacencyMapGraph<String, String> bfs(Graph<String, String> g, String source) {
		LinkedQueue<String> queue = new LinkedQueue<String>();
		queue.enqueue(source);
		String current;
		AdjacencyMapGraph<String, String> universeGraph = new AdjacencyMapGraph<String, String>();
		universeGraph.insertVertex(source);
		while (!queue.isEmpty()) {
			current = queue.dequeue();
			for (String neighbor : g.inNeighbors(current)) {
				if (!universeGraph.hasVertex(neighbor)) {
					universeGraph.insertVertex(neighbor);
					universeGraph.insertDirected(current, neighbor, g.getLabel(current, neighbor));
				}
			}
		}
		return universeGraph;
	}
	
	// Given a BFS graph, find the path from a point to the source.
	public static <V,E> ArrayList<String> getPath(Graph<String, String> tree, String origin) {
		ArrayList<String> pathConnectionStrings = new ArrayList<String>();
		String current = origin;
		Iterator<String> iterator = tree.outNeighbors(current).iterator();
		while (iterator.hasNext()) {
			String next = iterator.next();
			pathConnectionStrings.add(current + " was in " + next + " in " + tree.getLabel(current, next) + ".");
			current = next;
		}
		return pathConnectionStrings;
	}
	
	// Add all vertices to a set, then remove everything in the BFS graph. Whatever's left was not added to the BFS graph.
	public static <V,E> Set<String> missingVertices(Graph<String,String> graph, Graph<String,String> subgraph) {
		TreeSet<String> thisSet = new TreeSet<String>();
		Iterator<String> iterator;
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

	// Run the code.
	public static void main(String args[]) throws IOException{
		// Make referencable maps from the input file.
		AdjacencyMapGraph<String, String> thisGraph = makeGraph();
//		// Check for graph working.
//		for (String key : thisGraph.in.keySet()) {
//			System.out.println(key + " w " + thisGraph.in.get(key));
//		}
//		System.out.println("\n");
		// Infinite loop for the interface.
		while (true) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Enter a source: \n");
			String sourceName = reader.readLine();
			System.out.println("Enter a target: ");
			String targetName = reader.readLine();
			// Check for failed inputs.
			if (!thisGraph.hasVertex(sourceName) || !thisGraph.hasVertex(targetName)) { System.out.println("An input was invalid."); }
			else if (sourceName == targetName) { System.out.println("Inputs are the same person."); }
			// All systems are a go, initiate computation.
			else {
				System.out.println("Making connection from " + sourceName + " to " + targetName + ".");
				AdjacencyMapGraph<String, String> tree = bfs(thisGraph, sourceName);
				List<String> pathConnectionStrings = getPath(tree, targetName);
				Set<String> missingActors = missingVertices(thisGraph, tree);
				for (String step : pathConnectionStrings) {
					System.out.println(step + "\n");
				}
			}
		}
	}	
}
