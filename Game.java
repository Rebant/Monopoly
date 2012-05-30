import java.util.Random;


public class Game {

	public Board board;
	public Player[] allPlayers;
	public double startingMoney;
	public int numberOfPlayers;
	int[] rolls = new int[2];
	Random generator = new Random();
	
	
	public Game(String filename, int numberOfPlayers, double startingMoney) throws NullBoardException {
		this(filename);
		this.numberOfPlayers = numberOfPlayers;
		setPlayers();
		setStartingMoney(startingMoney);
		
	}
	
	
	public Game(String filename) throws NullBoardException {
		board = new Board(filename);
		
		
	}
	
	public void turn(Player currentPlayer) {
		int steps = roll();
		for (int i = 0; i < steps; i = i + 1) {
			currentPlayer.incrementSpace();
		}
	}
	
	/**
	 * @return Rolls two dice and stores the result into "rolls"
	 */
	public int roll() {
		rolls[0] = rollDice();
		rolls[1] = rollDice();
		return rolls[0] + rolls[1];
	}
	
	/**
	 * @return One dice roll
	 */
	public int rollDice() {
		return generator.nextInt(6) + 1;
	}
	
	/**
	 * @param n
	 * @param player
	 * Add a player to position n.
	 */
	public void addPlayer(int n, Player player) {
		allPlayers[n] = player;
	}
	
	public void createPlayers(Player[] players) {
		for (int i = 0; i < players.length; i = i + 1) {
			allPlayers[i] = players[i];
		}
	}
	
	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}
	
	public void setPlayers(int numberOfPlayers) {
		allPlayers = new Player[numberOfPlayers];
	}
	
	public void setPlayers() {
		this.setPlayers(numberOfPlayers);
	}
	
	public void setStartingMoney(double startingMoney) {
		this.startingMoney = startingMoney;
	}
	
	
	
	
}
