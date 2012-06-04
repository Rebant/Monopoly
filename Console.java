import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;


public class Console {
	
	static int statusBit;
	static String stringToPrint;
	static InputStreamReader iReader = new InputStreamReader(System.in);
	static BufferedReader bReader = new BufferedReader(iReader);
	static Card drewCard;
	static Game game;
	
	public static void main(String[] args) {
		
		String filename;

		int numPlayers = -1;
		
		System.out.println("Welcome to Monopoly!");
		
		boolean ready = false;
		while(!ready){
			System.out.println("Please input a filename:");
			try{
				filename = bReader.readLine();
				System.out.println("Enter the number of players:");
				numPlayers = Integer.parseInt(bReader.readLine());
				System.out.println("Enter the starting money for each player:");
				double startingMoney = Double.parseDouble(bReader.readLine());
				game = new Game(filename, numPlayers, startingMoney);
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
		
		System.out.println("Game is valid!");
		
		for (int i = 0; i < numPlayers; i = i + 1) {
			System.out.println("Enter name of player" + (i + 1) + ":");
			try { game.addPlayer(bReader.readLine()); } catch (Exception e) { }
		}
		
		System.out.println("Starting game...");
		
		game.nextTurn();
		int choice;
		
		while (!Game.isGameWon()) {
			statusBit = 0;
			boolean endTurn = false;
			boolean rolledDice = false;
			boolean jailOperation = false;
			System.out.println("It is " + game.currentPlayer.getName() + "'s turn.");
			if (game.currentPlayer.inJail && game.stillInJail()) { //***TODO: If last time in jail, tell user to pay 50.
				game.inJail(-999); if (game.currentPlayer.inJail)
				System.out.println("You are in jail, what would you like to do:");
				while (true) {
					stringToPrint = "0. Pay $50 to get out of jail\n1. Roll dice to get out\n2. Use 'Get out of Jail' card\n3. Build houses\n4. Sell houses\n5. Mortgage property\n6. Unmortgage property\n7. Trade\n8. End turn\n9. Get stats";
					choice = getChoice();
					switch (choice) {
						case 0: if (jailOperation) { System.out.println("You already tried to get out of jail."); break; }
								jailOperation = true; statusBit = game.inJail(choice); printWhatHappened(); break;
						case 1: if (jailOperation) { System.out.println("You already tried to get out of jail."); break; }
								jailOperation = true; statusBit = game.inJail(choice); printWhatHappened(); break;
						case 2: if (jailOperation) { System.out.println("You already tried to get out of jail."); break; }
								jailOperation = true; statusBit = game.inJail(choice); printWhatHappened(); break;
						case 3: stringToPrint = "Enter the property number:"; statusBit = game.buildHouse(getChoice()); printWhatHappened(); break;
						case 4: stringToPrint = "Enter the property number:"; statusBit = game.sellHouse(getChoice()); printWhatHappened(); break;
						case 5: stringToPrint = "Enter the property number:"; statusBit = game.mortgagedProperty(getChoice()); printWhatHappened(); break;
						case 6: stringToPrint = "Enter the property number:"; statusBit = game.unmortgageProperty(getChoice()); printWhatHappened(); break;
						case 7: System.out.println("Not implemented yet!"); break;
						case 8: if (!jailOperation) { System.out.println("You must first try to get out."); break; } System.out.println("Ending turn..."); endTurn = true; break;
						case 9: System.out.println(game.currentPlayer.getStats()); break;
					}
					if (jailOperation) { if (statusBit != Game.gotOutOfJail && statusBit != Game.stillInJail) { jailOperation = false; } }
					if (statusBit == Game.gotOutOfJail) { break; }
					if (endTurn) { break; }
				}
			}
			while (true && !game.currentPlayer.inJail) {
				statusBit = Game.nop;
				boolean doubles = false;
				System.out.println("What would you like to do:");
				stringToPrint = "0. Roll dice\n1. Build houses\n2. Sell houses\n3. Mortgage property\n4. Unmortgage property\n5. Trade\n6. End turn\n7. Get stats";
				choice = getChoice();
				switch (choice) {
					case 0: if (rolledDice) { System.out.println("You already rolled."); break; } rolledDice = true; int rolled = game.roll();
							game.movePlayer(rolled); doubles = game.currentPlayer.rolledDoubles; if (doubles) { System.out.println("You rolled doubles!"); }
							statusBit = game.activateEffect(); printWhatHappened(); while (game.anotherEffect) { statusBit = game.activateEffect(); printWhatHappened(); } break;
					case 1: stringToPrint = "Enter the property number:"; statusBit = game.buildHouse(getChoice()); printWhatHappened(); break;
					case 2: stringToPrint = "Enter the property number:"; statusBit = game.sellHouse(getChoice()); printWhatHappened(); break;
					case 3: stringToPrint = "Enter the property number:"; statusBit = game.mortgagedProperty(getChoice()); printWhatHappened(); break;
					case 4: stringToPrint = "Enter the property number:"; statusBit = game.unmortgageProperty(getChoice()); printWhatHappened(); break;
					case 5: System.out.println("Not implemented yet!"); break;
					case 6: if (!rolledDice) { System.out.println("You have to roll."); break; } System.out.println("Ending turn..."); endTurn = true; break;
					case 7: System.out.println(game.currentPlayer.getStats()); break;
				}
				if (statusBit == Game.canBuy) {
					while (true) {
						boolean doneBuying = false;
						stringToPrint = "0. Buy property\n1. Auction property\n2. Build houses\n3. Sell houses\n4. Mortgage property\n5. Unmortgage property\n6. Trade\n7. Get stats";
						choice = getChoice();
						switch (choice) {
							case 0: statusBit = game.buyProprety(); printWhatHappened(); if (statusBit == Game.notEnoughMoney) { continue; } doneBuying = true; break;
							case 1: game.auctionProperty(game.currentPlayer.getOnSpace()); int k = -1; double highestBid = -100; HashSet<Integer> noBid = new HashSet<Integer>();
									for (int i = 0; i < game.numOfPlayers(); i = i + 1) { if (game.players[i].bankrupt) { noBid.add(i); continue; } k = i; }
									while (((Property) game.board.getSpace(game.currentPlayer.getOnSpace())).auction) {
										while (noBid.size() != game.numOfPlayers() - 1) {
											if (game.players[k].getMoney() < highestBid) { noBid.add(k); break; }
											System.out.println("It is " + game.players[k].getName() + "'s turn to bid:");
											try { boolean rightBid = true; double bid = Double.parseDouble(bReader.readLine()); if (bid < 0) { noBid.add(k); rightBid = false; } if (bid > 0 && bid <= highestBid) { throw new NullBoardException("You must bid higher than the highest bid (" + highestBid + ")"); } if (rightBid) { highestBid = bid; } }
											catch (NullBoardException nfe) { System.out.println(nfe.getError()); continue; } catch (Exception e) { System.out.println("Bid an actual number!"); continue; }
											int l = 0; for (int i = ((k + 1) % game.numOfPlayers()); l < game.numOfPlayers(); l = l + 1) { if (game.players[i].bankrupt || noBid.contains(i)) { continue; } else { k = i; break; } }
										}
										((Property) game.board.getSpace(game.currentPlayer.getOnSpace())).doneAuction();
										game.players[k].addMoney(-highestBid);
										game.players[k].addProperty(game.currentPlayer.getOnSpace());
										if (((Property) game.board.getSpace(game.currentPlayer.getOnSpace())).isOwned()) {
											((Property) game.board.getSpace(game.currentPlayer.getOnSpace())).getOwnedBy().removeProperty(game.currentPlayer.getOnSpace());
										}
										((Property) game.board.getSpace(game.currentPlayer.getOnSpace())).setOwned(game.players[k]);
										System.out.println(game.players[k].getName() + " won the auction and bought the property for " + highestBid);
									} doneBuying = true; break;
							case 2: stringToPrint = "Enter the property number:"; statusBit = game.buildHouse(getChoice()); printWhatHappened(); break;
							case 3: stringToPrint = "Enter the property number:"; statusBit = game.sellHouse(getChoice()); printWhatHappened(); break;
							case 4: stringToPrint = "Enter the property number:"; statusBit = game.mortgagedProperty(getChoice()); printWhatHappened(); break;
							case 5: stringToPrint = "Enter the property number:"; statusBit = game.unmortgageProperty(getChoice()); printWhatHappened(); break;
							case 6: System.out.println("Not implemented yet!"); break;
							case 7: System.out.println(game.currentPlayer.getStats()); break;
						}
						if (doneBuying) { break; }
					}
				}
				if (endTurn) { break; }
				if (doubles) { rolledDice = false; }
			}
			game.nextTurn();
		}
		
		System.out.println("The winner is " + game.currentPlayer.getName() + "!");
		
	}
	
	public static void printWhatHappened() {
		switch (statusBit) {
			case Game.alreadyMortgaged: System.out.println("The property is already mortgaged."); break;
			case Game.boughtProperty: System.out.println("The property, " + game.board.getSpace(game.currentPlayer.getOnSpace()).getName() + " was bought."); break;
			case Game.canBuy: System.out.println("You can buy this property."); break;
			case Game.didNotBuildEvenly: System.out.println("You cannot buy this property."); break;
			case Game.doesNotOwnAllPropertiesInGroup: System.out.println("You cannot build a house because you do not own all the properties."); break;
			case Game.doesNotOwnProperty: System.out.println("You do not own this property."); break;
			case Game.doNotHaveJailCard: System.out.println("You do not have a 'Get Out of Jail' card."); break;
			case Game.drewGetOutOfJailCard: System.out.println("You drew a card: " + game.drawnCard.getDescription()); break;
			case Game.drewGoBack: System.out.println("You drew a card: " + game.drawnCard.getDescription()); break;
			case Game.drewGoToGo: System.out.println("You drew a card: " + game.drawnCard.getDescription()); break;
			case Game.drewGoToJail: System.out.println("You drew a card: " + game.drawnCard.getDescription()); break;
			case Game.drewMoveToGroup: System.out.println("You drew a card: " + game.drawnCard.getDescription()); break;
			case Game.drewMoveToSpace: System.out.println("You drew a card: " + game.drawnCard.getDescription()); break;
			case Game.drewReward: System.out.println("You drew a card: " + game.drawnCard.getDescription()); break;
			case Game.drewSetSpace: System.out.println("You drew a card: " + game.drawnCard.getDescription()); break;
			case Game.hotelBuilt: System.out.println("You built a hotel."); break;
			case Game.hotelIsBuilt: System.out.println("A hotel is already built."); break;
			case Game.gotOutOfJail: System.out.println("You got out of jail."); break;
			case Game.isMortgaged: System.out.println("The property is already mortgaged."); break;
			case Game.mortgagedProperty: System.out.println("You mortgaged the property."); break;
			case Game.mustSellEvenly: System.out.println("You must sell houses evenly."); break;
			case Game.noHousesToSell: System.out.println("There are no houses to sell."); break;
			case Game.notAValidOption: System.out.println("That is not a valid option."); break;
			case Game.notEnoughMoney: System.out.println("You do not have enough money."); break;
			case Game.notMortgaged: System.out.println("This property is not mortgaged."); break;
			case Game.onFreeParking: System.out.println("You are on 'Free Parking'."); break;
			case Game.onGo: System.out.println("You are on 'Go'."); break;
			case Game.onGoToJail: System.out.println("You are on 'Go to Jail'"); break;
			case Game.onIncome: System.out.println("You are on 'Income Tax'"); break;
			case Game.onJail: System.out.println("You are on 'Jail'"); break;
			case Game.onLuxury: System.out.println("You are on 'Luxury Tax'"); break;
			case Game.ownsProperty: System.out.println("You own this proprety so you pay rent."); break;
			case Game.paidMoney: System.out.println("You paid money to " + game.getPaidMoneyTo().getName()); break;
			case Game.soldHotel: System.out.println("You sold a hotel."); break;
			case Game.soldHouse: System.out.println("You sold a house."); break;
			case Game.stillInJail: System.out.println("You are still in jail."); break;
			case Game.unmortgagedProperty: System.out.println("You unmortgaged the property."); break;
		}
	}
	
	/**
	 * @return Number entered by player.
	 */
	public static int getChoice() {
		while (true) {
			System.out.println("");
			System.out.println(stringToPrint);
			try {
				return Integer.parseInt(bReader.readLine());
			}
			catch (Exception e) { System.out.println("Try again."); continue; }
		}
	}
		
}
		
//		//Reset all stuff
//		game.resetVariable();
//		
//		while (!game.isGameOver()) {
//			
//		}
//		
//	}
//	
//}
