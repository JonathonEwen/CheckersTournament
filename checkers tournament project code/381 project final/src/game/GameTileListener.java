package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//this class decides what to do when the user clicks on one of the game tiles
//we need to differentiate between the user clicking on a tile because he wants to move that piece,
//and the user clicking on a tile because he wants to move his selected piece to that tile.
public class GameTileListener implements ActionListener{
	int row;
	int col;

	public GameTileListener(int r, int c){
		super();
		row = r;
		col = c;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//if it is black's turn, and the user clicks on a black piece, highlight the moves that it can do
		if(maingame.m.gb.hasBlackPiece(row, col) && maingame.m.gb.isBlackTurn() && !maingame.m.gb.iJumped){
			maingame.m.gb.HighlightMoves(row, col);
			maingame.m.gb.fromRow = row;
			maingame.m.gb.fromCol = col;
		}
		//if it is red's turn and the user clicks on a red piece, highlight the moves that it can do
		if(maingame.m.gb.hasRedPiece(row, col) && !maingame.m.gb.isBlackTurn() && !maingame.m.gb.iJumped){
			maingame.m.gb.HighlightMoves(row, col);
			maingame.m.gb.fromRow = row;
			maingame.m.gb.fromCol = col;
		}
		//we will allow the user to move his piece by clicking on a highlighted tile
		if(maingame.m.gb.gameTile[row][col].highlighted){
			maingame.m.gb.MovePiece(maingame.m.gb.fromRow, maingame.m.gb.fromCol, row, col);
		}
	}
}
