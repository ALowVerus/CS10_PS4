import java.io.*;
import java.util.*;

public class BaconGame {
	
	// For generating the graph.
	public static BufferedReader buffer(String fileAddress) {
		try { return new BufferedReader(new FileReader(fileAddress)); }
		catch (FileNotFoundException e) { System.out.println("File not found."); }
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
		TreeMap<Integer, String> actorMap = makeMap("inputs/ps4/actors.txt");
		TreeMap<Integer, String> movieMap = makeMap("inputs/ps4/movies.txt");
		AdjacencyMapGraph<String, String> thisGraph = new AdjacencyMapGraph<String, String>();
		for (Integer actorKey : actorMap.keySet()) {thisGraph.insertVertex(actorMap.get(actorKey));}
		for (Integer movieKey : movieMap.keySet()) {thisGraph.insertVertex(movieMap.get(movieKey));}
		
		// Add links from actors to movie objects
		BufferedReader lineFile = buffer("inputs/ps4/movie-actors.txt"); String line;
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
	public static <V,E> AdjacencyMapGraph<String, String> bfs(AdjacencyMapGraph<String, String> g, String source) {
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
					universeGraph.insertDirected(neighbor, current, g.getLabel(current, neighbor));
					queue.enqueue(neighbor);
				}
			}
		}
		return universeGraph;
	}
	
	// Given a BFS graph, find the path from a point to the source.
	public static <V,E> ArrayList<String> getPath(AdjacencyMapGraph<String, String> tree, String origin) {
		ArrayList<String> pathConnectionStrings = new ArrayList<String>();
		try {
			String current = origin;
			Iterator<String> iterator = tree.outNeighbors(current).iterator();
			while (iterator.hasNext()) {
				String next = iterator.next();
				pathConnectionStrings.add(current + " was with " + next + " in " + tree.getLabel(current, next) + ".");
				current = next;
				iterator = tree.outNeighbors(current).iterator();
				return pathConnectionStrings;
			}
		}

		catch (NullPointerException e){
			System.out.println("No path to " + origin);
		}
		return pathConnectionStrings;		
		
	}
	
	// Add all vertices to a set, then remove everything in the BFS graph. Whatever's left was not added to the BFS graph.
	public static <V,E> Set<String> missingVertices(AdjacencyMapGraph<String,String> graph, AdjacencyMapGraph<String,String> subgraph) {
		TreeSet<String> thisSet = new TreeSet<String>();
		Iterator<String> iterator = graph.vertices().iterator();
		while (iterator.hasNext()) { thisSet.add(iterator.next()); }
		iterator = subgraph.vertices().iterator();
		while (iterator.hasNext()) { thisSet.remove(iterator.next()); }
		return thisSet;
	}
	
	// I have no idea what this is supposed to be.
	public static double averageSeparation(AdjacencyMapGraph<String,String> tree, String root) {
		double totalSeparation = (double)getSubHeight(tree, root, 0);
		double size = (double)tree.numVertices();
		return totalSeparation / size;
	}
	public static <V,E> int getSubHeight(AdjacencyMapGraph<String,String> tree, String currentVertex, int currentHeight) {
		int i = currentHeight;
		for (String parent : tree.inNeighbors(currentVertex)) {
			i += getSubHeight(tree, parent, currentHeight + 1);
		}
		return i;
	}
	
	public static void findBestAverageSeparations(AdjacencyMapGraph<String,String> graph){
		// Find contiguous networks
		Iterator<String> allVertices = graph.vertices().iterator();
		TreeSet<String> remainingVertices = new TreeSet<String>();
		while (allVertices.hasNext()) { remainingVertices.add(allVertices.next()); }
		ArrayList<TreeSet<String>> networks = new ArrayList<TreeSet<String>>();
		while (!remainingVertices.isEmpty()) {
			String current = remainingVertices.first();
			Iterator<String> map = bfs(graph, current).vertices().iterator();
			TreeSet<String> network = new TreeSet<String>();
			while (map.hasNext()) { 
				String next = map.next();
				network.add(next);
				remainingVertices.remove(next);
			}
			networks.add(network);
		}
		
		// Find biggest, which will be the 7494 strong list
		TreeSet<String> biggest = networks.remove(0);
		for (TreeSet<String> network : networks) {
			if (biggest.size() < network.size()) {
				biggest = network;
			}
		}
				
		// Find best average in biggest
		String bestVert = "Placeholder";
		double bestAve = -1.0;
		for (String newVert : biggest) {
			AdjacencyMapGraph<String, String> tree = bfs(graph, newVert);
			double newAve = averageSeparation(tree, newVert);
			if (bestAve > newAve || bestAve < 0) {
				bestAve = newAve;
				bestVert = newVert;
				System.out.println("The current best node is " + bestVert + ", with an average length of " + String.valueOf(bestAve));
			}
		}
		
		// Print result
		System.out.println("The best node is " + bestVert + ", with an average length of " + String.valueOf(bestAve));
	}

	// Run the code.
	public static void main(String args[]) throws IOException{
		// Make referencable maps from the input file.
		AdjacencyMapGraph<String, String> thisGraph = makeGraph();
		System.out.println("Enter ~GETBEST~ as your center to find the BEST center of the universe.");

		// Infinite loop for the interface.
		while (true) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Enter a center of universe: \n");
			String centUniName = reader.readLine();
			if (centUniName == "GETBEST") {
				findBestAverageSeparations(thisGraph);
			}
			else {
				System.out.println("Enter an actor: ");
				String targetName = reader.readLine();
				// Check for failed inputs.
				if (!thisGraph.hasVertex(centUniName) || !thisGraph.hasVertex(targetName)) { System.out.println("An input was invalid."); }
				else if (centUniName == targetName) { System.out.println("Inputs are the same person."); }
				// All systems are a go, initiate computation.
				else {
					System.out.println("Making connection from " + centUniName + " to " + targetName + "...");
					AdjacencyMapGraph<String, String> tree = bfs(thisGraph, centUniName);
					List<String> pathConnectionStrings = getPath(tree, targetName);
					
					// Print Bacon number
					if (pathConnectionStrings.size() > 0) {
						System.out.println(targetName + "'s " + centUniName + " number is " + String.valueOf(pathConnectionStrings.size()) + ".");
					}
					else {
						System.out.println(targetName + " has no " + centUniName + " number.");
					}
					
					// Print connection steps
					for (String step : pathConnectionStrings) { System.out.println(step); }
					
//					// Print missing actors
//					Set<String> missingActors = missingVertices(thisGraph, tree);
//					String s = "";
//					int n = 0;
//					for (String vertex : missingActors) {
//						s += vertex + ", "; 
//						n += 1;
//						if (n >= 20) {
//							s += "\n";
//							n = 0;
//						}
//					}
//					System.out.println("The missing links were:");
//					System.out.println(s);
					
					// Print average destination length
					System.out.println(averageSeparation(tree, centUniName));
					
					// Break
					System.out.println("");
				}
			}
		}
	}	
}
