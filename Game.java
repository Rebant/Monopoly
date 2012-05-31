import java.util.Random;

public class Game {

	public Board board;
	public Player[] allPlayers;
	public double startingMoney;
	public int numberOfPlayers;
	int[] rolls = new int[2];
	Random generator = new Random();
	private Player currentPlayer;
	private Player tradingPlayer;
	private boolean doubles;
	private int numOfDoubles;
	private int turn = -1; //-1 to start
	private boolean deciding;
	private boolean gameOver = false;
	
	//Status boolean: { landedOnProperty, canBuy, isMortgaged, 
	private boolean[] status;
	
	public Game(String filename, int numberOfPlayers, double startingMoney) throws NullBoardException {
		this(filename);
		this.numberOfPlayers = numberOfPlayers;
		setPlayers();
		setStartingMoney(startingMoney);
		
	}
	
	
	public Game(String filename) throws NullBoardException {
		board = new Board(filename);
		
		
	}

	public void resetVariable() {
		numOfDoubles = 0;
		deciding = false;
		nextTurn();
		currentPlayer = allPlayers[turn];
		
	}
	
	private void nextTurn() {
		turn = (turn + 1) % allPlayers.length;
	}
	
	
	private void playerRollAndMove() {
		int steps = roll();
		if (rolls[0] == rolls[1]) { doubles = true; numOfDoubles = numOfDoubles + 1; }
		if (numOfDoubles == 3) { System.out.println("You rolled doubles three times."); movePlayerToJail(); return; }
		for (int i = 0; i < steps; i = i + 1) {
			movePlayer();
		}
	}
	
	/**
	 * @param currentPlayer
	 * Moves 'currentPlayer' one space forward and checks to see if the player is on the
	 * "Go" space and adds $200 to 'currentPlayer' money
	 */
	public void movePlayer() {
		currentPlayer.incrementSpace();
		if (currentPlayer.getOnSpace() == 0) { currentPlayer.addMoney(200); }
	}
	
	/**
	 * @param currentPlayer
	 * @param toSpace
	 * Moves 'currentPlayer' to the space specified.
	 */
	public void movePlayer(int toSpace) {
		currentPlayer.setSpace(toSpace);
	}
	
	public void movePlayerToSpace(int spaceToLandOn) {
		while (currentPlayer.getOnSpace() != spaceToLandOn) {
			movePlayer();
		}
	}
	
	public void movePlayerToGroup(int group) {
		int onGroup = -1;
		while (onGroup != group) {
			movePlayer();
			Space currentSpace = board.getSpace(currentPlayer.getOnSpace());
			if (!(currentSpace instanceof Property)) { onGroup = -1; continue; }
			onGroup = ((Property) currentSpace).getGroup();
		}
	}
	
	/**
	 * @param currentPlayer
	 * Moves the player to jail.
	 */
	public void movePlayerToJail() {
		currentPlayer.setSpace(10);
		currentPlayer.setInJail(true);
		currentPlayer.setTimeInJail(0);
		System.out.println("You were put in jail");
	}
	
	public void activateEffect() {
		Space currentSpace = board.getSpace(currentPlayer.getOnSpace());
		//Big space stuff
		if (currentSpace instanceof BigSpace) {
			if (currentSpace.getName().equals("Go to Jail")) { movePlayerToJail(); return; }
		}
		//Not a property stuff
		if (currentSpace instanceof NotProperty) {
			String whatType = ((NotProperty) currentSpace).getType().toString();
			if (whatType.equals("Chance")) { activateEffect(drawCard(false)); return; }
			if (whatType.equals("CommunityChest")) { activateEffect(drawCard(true)); return; }
			if (whatType.equals("IncomeTax")) { if (currentPlayer.getMoney() < 200) { currentPlayer.addMoney(-currentPlayer.getMoney() * 0.10); } else { currentPlayer.addMoney(-200); } return; } //***TODO: Decide which to pay.
			if (whatType.equals("LuxuryTax")) { currentPlayer.addMoney(-75); return; }
		}
		// [ Player is on a property - let the cases begin!]
		deciding = true;
	}
	
	/**
	 * @param currentPlayer
	 * @param drawnCard
	 * Activate effect of a card.
	 */
	public void activateEffect(Card drawnCard) {
		double money = drawnCard.getMoney() + (getNumberOfPlayers() - 1)*drawnCard.getCostPerPerson() +
				currentPlayer.getNumberOfHouses()*drawnCard.getCostOfHouseAndHotel()[0] +
				currentPlayer.getNumberOfHotels()*drawnCard.getCostOfHouseAndHotel()[1];
		currentPlayer.addMoney(money);
		
		//***TODO: What about when the person is supposed to go to a railroad/utility? Cost = 2x or 4x.

		//Subtract cost from each player if needed
		if (drawnCard.getCostPerPerson() > 0) {
			for (int i = 0; i < allPlayers.length; i = i + 1) { if (allPlayers[i] == currentPlayer) { continue; } allPlayers[i].addMoney(-drawnCard.getCostPerPerson());	}
		}
		
		if (drawnCard.getName().equals("Get Out of Jail Card")) { currentPlayer.incrementNumJailCard(); }
		
		//Move the player to the appropriate space according to some cases:
		//Case 1: Increment is true and space is valid - move to a space
		if (drawnCard.isIncrement() && (drawnCard.getSpace() < 40 && drawnCard.getSpace() >= 0)) {
			movePlayerToSpace(drawnCard.getSpace());
		}
		//Case 2: Increment is true and space is not valid and group is valid - move to a group
		if (drawnCard.isIncrement() && drawnCard.getSpace() > 40 && drawnCard.getGroup() >= 0) {
			movePlayerToGroup(drawnCard.getGroup());
		}
		//Case 3: Increment is false and space is valid - set player to a specific space
		if (!drawnCard.isIncrement() && drawnCard.getSpace() < 40) {
			if (drawnCard.getName().equals("Go to Jail")) { movePlayerToJail(); checkToReshuffle(); return; }
			else { movePlayer(drawnCard.getSpace()); }
			//Check to see if in jail or not:
		}
		checkToReshuffle();

		activateEffect();

	}
	
	public void decidingWhatToDo() {
		Property theProperty = (Property) board.getSpace(currentPlayer.getOnSpace());
		//Case 1: Player is on a property which is not owned - player can buy it or auction the land
		if (!theProperty.isOwned()) {
			System.out.println("You can buy or auction this piece of land.");
			//***TODO: HOW THE FUCK DO WE DO THIS NOW!?!? TURN BASED...
		}
		//Case 2: Player is on a property which is owned
		if (theProperty.isOwned()) {
			//Case 2a: The property is mortgaged
			if (theProperty.isMortgage()) { System.out.println("The property is mortgaged so you do not need to pay rent."); }
			//Case 2b: The property is not mortgaged
			else { System.out.println("You must pay rent for this land equal to: " + theProperty.getRentPrice()); }
		}
	}
	
	public void build() {
		System.out.println("What would you like to build houses and/or hotels on?");
		
	}
	
	public void mortgage() {
		System.out.println("Which property would you like to mortgage?");
		
	}

	public void jailMode() {
		//See if the user has been in jail for three turns
		if (currentPlayer.getTimeInJail() == 3) { currentPlayer.setInJail(false); return; }

		currentPlayer.incrementTimeInJail();
		
		//***TODO: Tell user to roll doubles or pay 50 or use the out of jail card to get out
		
	}
		
	/**
	 * @param pile True for Community Chest, false for Chance
	 * Draw a card from the pile specified by 'pile' and perform the appropriate actions as stated on the card.
	 */
	private Card drawCard(boolean pile) {
		return board.removeCard(pile);
	}
	
	/**
	 * Reshuffles cards if there are no more cards in a certain pile.
	 */
	public void checkToReshuffle() {
		if (board.numberOfCommunityCards() == 0) { board.reshuffleCards(true); }
		if (board.numberOfChanceCards() == 0) { board.reshuffleCards(false); }
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
	
	public int getNumberOfPlayers() {
		return allPlayers.length;
	}
	
	public void setStartingMoney(double startingMoney) {
		this.startingMoney = startingMoney;
	}
	
	public boolean isGameOver() {
		return gameOver;
	}


	public Player getTradingPlayer() {
		return tradingPlayer;
	}


	public void setTradingPlayer(Player tradingPlayer) {
		this.tradingPlayer = tradingPlayer;
	}


	public boolean isDoubles() {
		return doubles;
	}


	public void setDoubles(boolean doubles) {
		this.doubles = doubles;
	}


	public boolean isDeciding() {
		return deciding;
	}


	public void setDeciding(boolean deciding) {
		this.deciding = deciding;
	}


	public boolean[] getStatus() {
		return status;
	}


	public void setStatus(boolean[] status) {
		this.status = status;
	}
	
	public void setStatus(int n, boolean status) {
		this.status[n] = status;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	//***TODO: Maybe this should go in the Monopoly class? It makes sense to do so...
//	public void play() {
//		
//		while (!gameOver) {
//			int currentTurn = -1;
//			for (int i = 0; i < allPlayers.length; i = i + 1) { if(allPlayers[i].isTurn()) { currentTurn = i; break; } } //Find the player
//			turn(allPlayers[currentTurn]);
//			nextPlayer();
//		}
//	}
//	
//	private void nextTurn() {
//		if (turn + 1 == allPlayers.length) { moves = moves + 1; }
//		turn = (turn + 1) % allPlayers.length;
//	}
//	
//	private void nextPlayer() {
//		nextTurn();
//		for (int i = 0; i < allPlayers.length; i = i + 1) {
//			allPlayers[i].setTurn(i == turn && !allPlayers[i].getBankrupt());
//		}		
//	}
//	
//	public void turn(Player currentPlayer) {
//		boolean rollAgain = false; int numOfDoubles = 0;
//		do {
//			if (currentPlayer.getInJail()) { jailMode(currentPlayer); if (currentPlayer.getInJail()) { return; } } //If still in jail, done.
//			int steps = roll();
//			if (rolls[0] == rolls[1]) {
//				if (numOfDoubles != 2) { rollAgain = true; numOfDoubles = numOfDoubles + 1; }
//				else { rollAgain = false; movePlayerToJail(currentPlayer); }
//			}
//			for (int i = 0; i < steps; i = i + 1) {
//				movePlayer(currentPlayer);
//			}
//			activateEffect(currentPlayer);
//		}
//		while (rollAgain);
//	}
	
}
