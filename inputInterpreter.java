import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class inputInterpreter {

	public static void main(String args[]) {
		try {
			BufferedReader file = new BufferedReader(new FileReader("inputs/ps4/actors.txt"));
			String line;
			while ((line = file.readLine()) != null) {
				line.split("\\|");
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}
		catch (IOException e) {
			System.out.println("Index out of bounds.");
		}
	}
}
