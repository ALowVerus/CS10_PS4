import java.util.ArrayList;

public class Actor {
	private static final int infinity = 2147483647;
	private String name;
	private boolean visited = false;
	private ArrayList<Actor> path;
	private int distance = infinity;

	public Actor(String name) {
		this.name = name;
	}
	
	// Getters
	public boolean getVisited() {return visited;}
	public ArrayList<Actor> getPath(){return path;}
	public int getDistance() {return distance;}
	
	// Setters
	public void setVisited(boolean visited) {this.visited = visited;}
	public void setPath(ArrayList<Actor> path) {this.path = path;}
	public void setDistance(int distance) {this.distance = distance;}
}
