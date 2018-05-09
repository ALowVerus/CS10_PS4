import java.util.*;


public class BaconGame {
	
	public class DistanceComparator implements Comparator<Actor> {
		public int compare(Actor a, Actor b) {
			return a.getDistance() - b.getDistance();
		}
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
	
}
