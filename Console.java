import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Console {
	
	public static void main(String[] args) {
		
		String filename;
		Game game = null;
		
		InputStreamReader iReader = new InputStreamReader(System.in);
		BufferedReader bReader = new BufferedReader(iReader);

		System.out.println("Welcome to Monopoly!");
		System.out.println("Please input a filename:");
		
		boolean ready = false;
		while(!ready){
			try{
				filename = bReader.readLine();
				System.out.println("Enter the number of lives:");
				int numLives = Integer.parseInt(bReader.readLine());
				System.out.println("Enter the starting money for each player:");
				double startingMoney = Double.parseDouble(bReader.readLine());
				game = new Game(filename, numLives, startingMoney);
				ready = true;
			}
			catch (NullBoardException nbe) {
				System.out.println(nbe.getError());
				System.out.println("Please try again.");
			}
			catch (NumberFormatException nfe) {
				System.out.println("Try again - you must have entered a wrong number.");
			}
			catch (Exception e) {
				System.out.println("There is some sort of error that I did not account for");
				e.printStackTrace();
				System.exit(-1);
			}
		}
		
		//Reset all stuff
		game.resetVariable();
		
		while (!game.isGameOver()) {
			
		}
		
	}
	
}
