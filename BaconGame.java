
public class BaconGame {
	
	
	Graph<String, Integer> baconGame = new AdjacencyMapGraph<String, Integer>();
	
	Iterator<String> iterator = baconGame.outNeighbors(current).iterator(); 
}
