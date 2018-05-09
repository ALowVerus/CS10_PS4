import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class inputInterpreter {

	public static void main(String args[]) {
		try {
			BufferedReader file = new BufferedReader(new FileReader("inputs/ps4/actors.txt"));
		}
		catch (FileNotFoundException e) {
			System.out.println("Blah");
		}
	}
}
