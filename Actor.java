import java.util.ArrayList;

public class Actor {
	private static final int infinity = 2147483647;
	private String name;
	private boolean visited = false;
	private ArrayList<Actor> path;
	private ArrayList<Actor> fellowActors;
	private int distance = infinity;

	public Actor(String name, ArrayList<Actor> fellows) {
		this.name = name;
		this.fellowActors = fellows;
	}
	
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	public boolean getVisited() {
		return visited;
	}
	
	public void setPath(ArrayList<Actor> path) {
		this.path = path;
	}
	
	public ArrayList<Actor> getPath(){
		return path;
	}
	
	public void setFellowActors(ArrayList<Actor> fellows) {
		this.fellowActors = fellows;
	}
	
	public ArrayList<Actor> getFellowActors(){
		return fellowActors;
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public int getDistance() {
		return distance;
	}
}
