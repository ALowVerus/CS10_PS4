import java.io.*;
import java.util.*;

import sun.misc.VM;


public class BaconGame {
	
	public class DistanceComparator implements Comparator<Actor> {
		public int compare(Actor a, Actor b) {
			return a.getDistance() - b.getDistance();
		}
	}
	
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
	
	
	public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source) {
		
	}
	public static <V,E> List<V> getPath(Graph<V,E> tree, V v) {
		
	}
	public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph) {
		
	}
	public static <V,E> double averageSeparation(Graph<V,E> tree, V root) {
		
	}
	
	public int findBaconNumber(Graph<Actor, Integer> theGraph, Actor chosen, Actor goal) {
		Actor current = chosen;
		Iterator<Actor> iterator = theGraph.outNeighbors(current).iterator(); 
		PriorityQueue<Actor> theQueue = new PriorityQueue<Actor>();
		
		theQueue.add(chosen);
		while(theQueue.size() > 0)
			
		
//		while(iterator.hasNext()) {
//			Actor actor = iterator.next();
//			if(actor.equals(goal)) {
//				return actor.getDistance();
//			}	
//		}
		return -1;

	}

	public static void main(String args[]){
		// Make referencable maps from the input files
		TreeMap<String, Actor> actorMap = makeMap("inputs/ps4/actorsTest.txt");
		TreeMap<String, Actor> movieMap = makeMap("inputs/ps4/moviesTest.txt");
		
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
	}	
}
