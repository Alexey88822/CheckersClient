package Model;
import java.util.LinkedList;

import EnumConstants.Checkers;

public class Board {
	
	private Square[][] squares;
	
	public Board(){
		squares = new Square[8][8];
		
		setSquares();
		assignPlayerIDs();
		//printSquareDetails();
	}
	
	private void setSquares() {
		boolean rowInitialFilled, isFilled;
		int count = 0;
		
		//Rows
		for(int r=0;r<Checkers.NUM_ROWS.getValue();r++){
			rowInitialFilled = (r%2 == 1) ? true : false;
			
			//Columns
			for(int c=0;c<Checkers.NUM_COLS.getValue();c++){
				isFilled = (rowInitialFilled && c%2 == 0) ? true : (!rowInitialFilled && c%2 == 1) ? true : false;
				count++;
				
				squares[r][c] = new Square(count, r, c, isFilled);
//				System.out.println(i+" ---- " + rowInitialFilled + " + "+ k + " ---"+isFilled);
			}
		}		
	}

	public Square[][] getSquares(){
		return this.squares;
	}
	
	public int getTotlaSquares(){
		return squares.length;
	}
	
	public void printSquareDetails(){
		for(int r=0;r<Checkers.NUM_ROWS.getValue();r++){
			for(int c=0;c<Checkers.NUM_COLS.getValue();c++){
				/*System.out.println(squares[r][c].getSquareID() + "--" + squares[r][c].getSquareRow() + "--" + squares[r][c].getSquareCol()
						+ squares[r][c].getPlayerID());*/
				System.out.println(squares[r][c].getSquareID() + " --"+ squares[r][c].isPossibleToMove());
			}
		}
	}
	
	private void assignPlayerIDs() {
		
		//Rows 0-2 for player ONE
		for(int r=0;r<3;r++){					
			//Columns
			for(int c=0;c<Checkers.NUM_COLS.getValue();c++){
				if(squares[r][c].getIsFilled()){
					squares[r][c].setPlayerID(Checkers.PLAYER_ONE.getValue());
				}
			}
		}
		
		//Rows 5-7 for player TWO
		for(int r=5;r<8;r++){					
			//Columns
			for(int c=0;c<Checkers.NUM_COLS.getValue();c++){
				if(squares[r][c].getIsFilled()){
					squares[r][c].setPlayerID(Checkers.PLAYER_TWO.getValue());
				}
			}
		}
	}

	public LinkedList<Square> findPlayableSquares(Square selectedSquare){

		LinkedList<Square> playableSquares = new LinkedList<Square>();

		int selectedRow = selectedSquare.getSquareRow();
		int selectedCol = selectedSquare.getSquareCol();

		int movableRowFront = (selectedSquare.getPlayerID()==1) ? selectedRow+1 : selectedRow-1;
		int movableRowBack = (selectedSquare.getPlayerID()==1) ? selectedRow-1 : selectedRow+1;
		Square square1;
		Square square2;
		Square square3;
		Square square4;
		Square square5;
		Square square6;
		Square square7;
		Square square8;
		twoFrontSquares(playableSquares, movableRowFront, selectedCol);
		square1=crossJumpFrontRight(playableSquares, (selectedSquare.getPlayerID()==1) ? movableRowFront+1 : movableRowFront-1, selectedCol, movableRowFront);
		if (square1!=null){
			int selectedRow1 = square1.getSquareRow();
			int selectedCol1 = square1.getSquareCol();
			crossJumpFrontRight(playableSquares, (selectedSquare.getPlayerID()==1)? selectedRow1 + 2 : selectedRow1 - 2, selectedCol1, (selectedSquare.getPlayerID()==1) ? selectedRow1+1 : selectedRow1-1);
		}
		square2 = crossJumpFrontLeft(playableSquares, (selectedSquare.getPlayerID()==1) ? movableRowFront+1 : movableRowFront-1, selectedCol, movableRowFront);
		if (square2!=null){
			int selectedRow2 = square2.getSquareRow();
			int selectedCol2 = square2.getSquareCol();
			crossJumpFrontLeft(playableSquares, (selectedSquare.getPlayerID()==1)? selectedRow2 + 2 : selectedRow2 - 2, selectedCol2, (selectedSquare.getPlayerID()==1) ? selectedRow2+1 : selectedRow2-1);
		}
		square3 = crossJumpBackRight(playableSquares, (selectedSquare.getPlayerID()==1) ? movableRowBack-1 : movableRowBack+1, selectedCol, movableRowBack);
		if (square3!=null){
			int selectedRow3 = square3.getSquareRow();
			int selectedCol3 = square3.getSquareCol();
			crossJumpBackRight(playableSquares, (selectedSquare.getPlayerID()==1)? selectedRow3 - 2 : selectedRow3 + 2, selectedCol3, (selectedSquare.getPlayerID()==1) ? selectedRow3-1 : selectedRow3+1);
		}
		square4 = crossJumpBackLeft(playableSquares, (selectedSquare.getPlayerID()==1) ? movableRowBack-1 : movableRowBack+1, selectedCol, movableRowBack);
		if (square4!=null){
			int selectedRow4 = square4.getSquareRow();
			int selectedCol4 = square4.getSquareCol();
			crossJumpBackLeft(playableSquares, (selectedSquare.getPlayerID()==1)? selectedRow4 - 2 : selectedRow4 + 2, selectedCol4, (selectedSquare.getPlayerID()==1) ? selectedRow4-1 : selectedRow4+1);
		}
		if(selectedSquare.isKing()){
			int movableRowFrontKing = (selectedSquare.getPlayerID()==1) ? selectedRow-1 : selectedRow+1;
			int movableRowBackKing = (selectedSquare.getPlayerID()==1) ? selectedRow+1 : selectedRow-1;
			twoFrontSquares(playableSquares, movableRowFrontKing , selectedCol);
			twoFrontSquares(playableSquares,movableRowBackKing,selectedCol);
			square5 = crossJumpFrontRight(playableSquares, (selectedSquare.getPlayerID()==1) ? movableRowFrontKing-1 : movableRowFrontKing+1, selectedCol, movableRowFrontKing);
			if (square5!=null){
				int selectedRow5 = square5.getSquareRow();
				int selectedCol5 = square5.getSquareCol();
				crossJumpFrontRight(playableSquares, (selectedSquare.getPlayerID()==1)? selectedRow5 - 2 : selectedRow5 + 2, selectedCol5, (selectedSquare.getPlayerID()==1) ? selectedRow5-1 : selectedRow5+1);
			}
			square6 = crossJumpFrontLeft(playableSquares, (selectedSquare.getPlayerID()==1) ? movableRowFrontKing-1 : movableRowFrontKing+1, selectedCol, movableRowFrontKing);
			if (square6!=null){
				int selectedRow6 = square6.getSquareRow();
				int selectedCol6 = square6.getSquareCol();
				crossJumpFrontLeft(playableSquares, (selectedSquare.getPlayerID()==1)? selectedRow6 - 2 : selectedRow6 + 2, selectedCol6, (selectedSquare.getPlayerID()==1) ? selectedRow6-1 : selectedRow6+1);
			}
			square7 = crossJumpBackRight(playableSquares, (selectedSquare.getPlayerID()==1) ? movableRowBackKing+1 : movableRowBackKing-1, selectedCol, movableRowBackKing);
			if (square7!=null){
				int selectedRow7 = square7.getSquareRow();
				int selectedCol7 = square7.getSquareCol();
				crossJumpBackRight(playableSquares, (selectedSquare.getPlayerID()==1)? selectedRow7 + 2 : selectedRow7 - 2, selectedCol7, (selectedSquare.getPlayerID()==1) ? selectedRow7+1 : selectedRow7-1);
			}
			square8 = crossJumpBackLeft(playableSquares, (selectedSquare.getPlayerID()==1) ? movableRowBackKing+1 : movableRowBackKing-1, selectedCol, movableRowBackKing);
			if (square8!=null){
				int selectedRow8 = square8.getSquareRow();
				int selectedCol8 = square8.getSquareCol();
				crossJumpBackLeft(playableSquares, (selectedSquare.getPlayerID()==1)? selectedRow8 + 2 : selectedRow8 - 2, selectedCol8, (selectedSquare.getPlayerID()==1) ? selectedRow8+1 : selectedRow8-1);
			}
		}
		return playableSquares;
	}
	
	//check two front squares
	private void twoFrontSquares(LinkedList<Square> pack, int movableRow, int selectedCol){
		
		if(movableRow>=0 && movableRow<8){
			//right Corner
			if(selectedCol>=0 && selectedCol<7){
				Square rightCorner = squares[movableRow][selectedCol+1];
				if(rightCorner.getPlayerID()==0){
					rightCorner.setPossibleToMove(true);
					pack.add(rightCorner);
				}
			}
			
			//left upper corner
			if(selectedCol>0 && selectedCol <=7){
				Square leftCorner = squares[movableRow][selectedCol-1];
				if(leftCorner.getPlayerID()==0){
					leftCorner.setPossibleToMove(true);
					pack.add(leftCorner);
				}
			}
		}
	}

	//cross jump - two front
	private Square crossJumpFrontRight(LinkedList<Square> pack, int movableRow, int selectedCol, int middleRow){

		int middleCol;

		if(movableRow>=0 && movableRow<8){
			//right upper Corner
			if(selectedCol>=0 && selectedCol<6){
				Square rightCorner = squares[movableRow][selectedCol+2];
				middleCol = (selectedCol+selectedCol+2)/2;
				if(rightCorner.getPlayerID()==0 && isOpponentInbetween(middleRow, middleCol)){
					rightCorner.setPossibleToMove(true);
					pack.add(rightCorner);
					return rightCorner;
				}
			}
		}
		return null;
	}

	private Square crossJumpFrontLeft(LinkedList<Square> pack, int movableRow, int selectedCol, int middleRow){
		//left upper corner
		int middleCol;

		if(movableRow>=0 && movableRow<8) {
			if (selectedCol > 1 && selectedCol <= 7) {
				Square leftCorner = squares[movableRow][selectedCol - 2];
				middleCol = (selectedCol + selectedCol - 2) / 2;
				if (leftCorner.getPlayerID() == 0 && isOpponentInbetween(middleRow, middleCol)) {
					leftCorner.setPossibleToMove(true);
					pack.add(leftCorner);
					return leftCorner;
				}
			}
		}
		return null;
	}

	private Square crossJumpBackRight(LinkedList<Square> pack, int movableRow, int selectedCol, int middleRow){

		int middleCol;

		if(movableRow>=0 && movableRow<8){
			//right down Corner
			if(selectedCol>=0 && selectedCol<6){
				Square rightCorner = squares[movableRow][selectedCol+2];
				middleCol = (selectedCol+selectedCol+2)/2;
				if(rightCorner.getPlayerID()==0 && isOpponentInbetween(middleRow, middleCol)){
					rightCorner.setPossibleToMove(true);
					pack.add(rightCorner);
					return rightCorner;
				}
			}
		}
		return null;
	}

	private Square crossJumpBackLeft(LinkedList<Square> pack, int movableRow, int selectedCol, int middleRow){
		//left down corner
		int middleCol;

		if(movableRow>=0 && movableRow<8) {
			if (selectedCol > 1 && selectedCol <= 7) {
				Square leftCorner = squares[movableRow][selectedCol - 2];
				middleCol = (selectedCol + selectedCol - 2) / 2;
				if (leftCorner.getPlayerID() == 0 && isOpponentInbetween(middleRow, middleCol)) {
					leftCorner.setPossibleToMove(true);
					pack.add(leftCorner);
					return leftCorner;
				}
			}
		}
		return null;
	}
	
	private boolean isOpponentInbetween(int row, int col){
		return squares[row][col].isOpponentSquare();
	}
}
