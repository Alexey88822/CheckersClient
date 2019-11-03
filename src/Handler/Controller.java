package Handler;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import EnumConstants.Checkers;
import Model.Board;
import Model.Player;
import Model.Square;
import View.BoardPanel;

public class Controller implements Runnable {
	private boolean continueToPlay;
	private boolean waitingForAction;
	private boolean isOver;
	
	//Network
	private DataInputStream fromServer;
	private DataOutputStream toServer;
	
	private BoardPanel boardPanel;
	private Player player;
	
	//Data
	private LinkedList<Square> selectedSquares;
	private LinkedList<Square> playableSquares;
	//private LinkedList<Square> crossableSquares;
	
	public Controller(Player player, DataInputStream input, DataOutputStream output){
		this.player = player;
		this.fromServer = input;
		this.toServer= output;
		
		selectedSquares = new LinkedList<Square>();
		playableSquares = new LinkedList<Square>();
	}
	
	public void setBoardPanel(BoardPanel panel){
		this.boardPanel = panel;
	}
	
	@Override
	public void run() {
		continueToPlay = true;
		waitingForAction = true;
		isOver=false;
		
		try {

			if(player.getPlayerID()==Checkers.PLAYER_ONE.getValue()){
				fromServer.readInt();
				player.setMyTurn(true);
			}
					
			while(continueToPlay && !isOver){
				if(player.getPlayerID()==Checkers.PLAYER_ONE.getValue()){
					waitForPlayerAction();
					if(!isOver)
						receiveInfoFromServer();
				}else if(player.getPlayerID()==Checkers.PLAYER_TWO.getValue()){
					receiveInfoFromServer();
					if(!isOver)
						waitForPlayerAction();
				}
			}
			
			if(isOver){
				JOptionPane.showMessageDialog(null, "Игра окончена",
						"Information", JOptionPane.INFORMATION_MESSAGE, null);
				System.exit(0);
			}
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Соединение потеряно",
					"Error", JOptionPane.ERROR_MESSAGE, null);
			System.exit(0);
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(null, "Соединение прервано",
					"Error", JOptionPane.ERROR_MESSAGE, null);
		}			
	}
	
	private void receiveInfoFromServer() throws IOException {
		player.setMyTurn(false);
		int from = fromServer.readInt();
		if(from==Checkers.YOU_LOSE.getValue()){
			from = fromServer.readInt();
			int to = fromServer.readInt();
			updateReceivedInfo(from, to);
			isOver=true;
		}else if(from==Checkers.YOU_WIN.getValue()){
			isOver=true;
			continueToPlay=false;
		}else{
			int to = fromServer.readInt();
			updateReceivedInfo(from, to);
		}
	}	

	private void sendMove(Square from, Square to) throws IOException {
		toServer.writeInt(from.getSquareID());
		toServer.writeInt(to.getSquareID());
	}

	private void waitForPlayerAction() throws InterruptedException {
		player.setMyTurn(true);
		while(waitingForAction){
			Thread.sleep(100);
		}
		waitingForAction = true;		
	}
	
	public void move(Square from, Square to){
		to.setPlayerID(from.getPlayerID());
		from.setPlayerID(Checkers.EMPTY_SQUARE.getValue());
		checkCrossJump(from, to);
		checkKing(from, to);
		squareDeselected();
		
		waitingForAction = false;
		try {
			sendMove(from, to);
		} catch (IOException e) {
			System.out.println("Отправка не удалась");
		}		
	}
	
	//When a square is selected
	public void squareSelected(Square s) {
		
		if(selectedSquares.isEmpty()){
			addToSelected(s);
		}		
		//if one is already selected, check if it is possible move
		else if(selectedSquares.size()>=1){
			if(playableSquares.contains(s)){
				move(selectedSquares.getFirst(),s);
			}else{
				squareDeselected();
				addToSelected(s);
			}
		}
	}
	
	private void addToSelected(Square s){
		s.setSelected(true);
		selectedSquares.add(s);
		getPlayableSquares(s);
	}

	public void squareDeselected() {
		
		for(Square square:selectedSquares)
			square.setSelected(false);
		
		selectedSquares.clear();
		
		for(Square square:playableSquares){
			square.setPossibleToMove(false);
		}
		
		playableSquares.clear();
		boardPanel.repaintPanels();
	}
	
	
	private void getPlayableSquares(Square s){
		playableSquares.clear();		
		playableSquares = boardPanel.getPlayableSquares(s);
		
		for(Square square:playableSquares){
			System.out.println(square.getSquareID());
		}		
		boardPanel.repaintPanels();
	}
	
	public boolean isHisTurn(){
		return player.isMyTurn();
	}
	
	private void checkCrossJump(Square from, Square to){		
		if(Math.abs(from.getSquareRow()-to.getSquareRow())==2){		
			int middleRow = (from.getSquareRow() + to.getSquareRow())/2;
			int middleCol = (from.getSquareCol() + to.getSquareCol())/2;
			
			Square middleSquare = boardPanel.getSquare((middleRow*8)+middleCol+1);
			middleSquare.setPlayerID(Checkers.EMPTY_SQUARE.getValue());
			middleSquare.removeKing();
		}
		if(Math.abs(from.getSquareRow()-to.getSquareRow())==4){
			if(from.getSquareCol()!=to.getSquareCol()) {
				int middleRow = (from.getSquareRow() + to.getSquareRow()) / 2;
				int firstCheckerRow = (middleRow + from.getSquareRow()) / 2;
				int secondCheckerRow = (middleRow + to.getSquareRow()) / 2;
				int middleCol = (from.getSquareCol() + to.getSquareCol()) / 2;
				int firstCheckerCol = (middleCol + from.getSquareCol()) / 2;
				int secondCheckerCol = (middleCol + to.getSquareCol()) / 2;

				Square firstSquare = boardPanel.getSquare((firstCheckerRow * 8) + firstCheckerCol + 1);
				firstSquare.setPlayerID(Checkers.EMPTY_SQUARE.getValue());
				firstSquare.removeKing();
				Square secondSquare = boardPanel.getSquare((secondCheckerRow * 8) + secondCheckerCol + 1);
				secondSquare.setPlayerID(Checkers.EMPTY_SQUARE.getValue());
				secondSquare.removeKing();
			}  /*else {
				int middleRow = (from.getSquareRow() + to.getSquareRow()) / 2;
				int firstCheckerRow = (middleRow + from.getSquareRow()) / 2;
				int secondCheckerRow = (middleRow + to.getSquareRow()) / 2;
				Square square1 = boardPanel.getSquare((firstCheckerRow*8) + from.getSquareCol()-1+1);
				Square square2 = boardPanel.getSquare((firstCheckerRow*8) + from.getSquareCol()+1+1);
				//if(square1.isOpponentSquare()){
					int firstCheckerCol=from.getSquareCol()-1;
					int secondCheckerCol=from.getSquareCol()-1;
					Square firstSquare = boardPanel.getSquare((firstCheckerRow * 8) + firstCheckerCol + 1);
					firstSquare.setPlayerID(Checkers.EMPTY_SQUARE.getValue());
					firstSquare.removeKing();
					Square secondSquare = boardPanel.getSquare((secondCheckerRow * 8) + secondCheckerCol + 1);
					secondSquare.setPlayerID(Checkers.EMPTY_SQUARE.getValue());
					secondSquare.removeKing();
				//} else if (square2.isOpponentSquare()){
					firstCheckerCol=from.getSquareCol()+1;
					secondCheckerCol=from.getSquareCol()+1;
					firstSquare = boardPanel.getSquare((firstCheckerRow * 8) + firstCheckerCol + 1);
					firstSquare.setPlayerID(Checkers.EMPTY_SQUARE.getValue());
					firstSquare.removeKing();
					secondSquare = boardPanel.getSquare((secondCheckerRow * 8) + secondCheckerCol + 1);
					secondSquare.setPlayerID(Checkers.EMPTY_SQUARE.getValue());
					secondSquare.removeKing();
				 } */
			}
		}
	
	private void checkKing(Square from, Square movedSquare){		
		if(from.isKing()){
			movedSquare.setKing();
			from.removeKing();
		}else if(movedSquare.getSquareRow()==7 && movedSquare.getPlayerID()==Checkers.PLAYER_ONE.getValue()){
			movedSquare.setKing();
		}else if(movedSquare.getSquareRow()==0 && movedSquare.getPlayerID()==Checkers.PLAYER_TWO.getValue()){
			movedSquare.setKing();
		}
	}
	
	private void updateReceivedInfo(int from, int to){
		Square fromSquare = boardPanel.getSquare(from);
		Square toSquare = boardPanel.getSquare(to);
		toSquare.setPlayerID(fromSquare.getPlayerID());
		fromSquare.setPlayerID(Checkers.EMPTY_SQUARE.getValue());
		checkCrossJump(fromSquare, toSquare);
		checkKing(fromSquare, toSquare);
		boardPanel.repaintPanels();
	}
}