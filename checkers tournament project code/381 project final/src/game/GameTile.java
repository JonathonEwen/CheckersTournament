package game;

import java.awt.Dimension;

import javax.swing.JButton;
//this class defines the Game Tile object, which represents the idea of one square on a checkers board
//it extends from JButton so that it is a simple matter for us to listen for the user to click the piece
//he wants and where he wants to go
public class GameTile extends JButton {

	GamePiece gamePiece;
	boolean occupied;
	boolean useable;
	//we want to highlight the squares a user can move to when he clicks one of his pieces
	boolean highlighted;
	int row;
	int col;

	public String toString(){
		return ""+row+","+col;
	}
	
	public GameTile(boolean canUse, int r, int c){
		super();
		useable = canUse;
		highlighted = false;
		gamePiece = null;
		occupied = false;
		row = r;
		col = c;
		this.setPreferredSize(new Dimension(50,50));
	}
	
	public void setGamePiece(GamePiece gp){
		gamePiece = gp;
		occupied = true;
	}
	
	public void removeGamePiece(){
		gamePiece = null;
		occupied = false;
	}
	
	public GamePiece getGamePiece(){
		return gamePiece;
	}
	
	public boolean isUseable(){
		return useable;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getCol(){
		return col;
	}
	
	public void setHighlight(boolean b){
		highlighted = b;
	}
	
	public boolean isOccupied(){
		return occupied;
	}
}
