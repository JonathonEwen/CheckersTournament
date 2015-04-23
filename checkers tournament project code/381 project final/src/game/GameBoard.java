package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
//this class defines the basic rules for the game of checkers, 
//as well as the way the game board should look and respond to input
public class GameBoard extends JPanel {
	JPanel board;
	GameTile[][] gameTile;
	GamePiece[] redPlayer;
	GamePiece[] blackPlayer;
	Color grey;
	Color highlight;
	int fromRow;
	int fromCol;
	boolean isBlackTurn = true;
	boolean iJumped = false;
	boolean iGotKinged = false;
	boolean mustJump = false;
	boolean LoggedIn = false;
	public int redPieces = 0;
	public int blackPieces = 0;
	int redMoves;
	int blackMoves = 7;
	boolean ranked = false;
	String redmoves = "";
	String blackmoves = "moves available for black player: "+blackMoves;
	
	public GameBoard(boolean isRanked){
		ranked = isRanked;
	}

	public void SetGameBoard(){
		board = new JPanel();
		board.setLayout(new GridLayout(0,8));
		gameTile = new GameTile[8][8];
		int mod = 0;
		grey = new Color(135,135,135);
		highlight = new Color(255,255,0);
		for(int row = 0; row < 8; row++){
			for(int col = 0; col < 8; col++){
			/* Here we set the colors and if the game tile is use-able or not
			 * Atm I have it set to use white and grey tiles.
			 * if we want red and black tiles move the comment in the first case to the first
			 * set background and remove the comment in the second case */
				if((col+mod)%2==0){
					gameTile[row][col] = new GameTile(true,row,col);
					gameTile[row][col].setBackground(grey);
					gameTile[row][col].addActionListener(new GameTileListener(row,col));
					//gameTile[i].setBackground(new Color(255,255,255));
				}
				else{
					gameTile[row][col] = new GameTile(false,row,col);
					//gameTile[i].setBackground(new Color(255,0,0));
				}
				board.add(gameTile[row][col]);
			}
			switch(mod){
			case 0:
				mod = 1;
				break;
			case 1:
				mod = 0;
				break;
			}
		}
		
		SetUpPlayers();
	
	}
	//this function places the red and black players' pieces on the board at the start of the game
	public void SetUpPlayers(){
		redPlayer = new GamePiece[12];
		blackPlayer = new GamePiece[12];
		int redIndex, blackIndex, row, col;
		redIndex = 0;
		blackIndex = 0;
		for(row = 0; row < 4; row++){
			for(col = 0; col < 8; col++){
				if(gameTile[row][col].useable){
					redPlayer[redIndex] = new GamePiece("Red");
					gameTile[row][col].setGamePiece(redPlayer[redIndex]);
					gameTile[row][col].setIcon(new ImageIcon(redPlayer[redIndex].getImage()));
					redIndex++;
					redPieces++;
				}
				if(redIndex == 12)
					break;
			}
		}
		
		for(row = 7; row > 4; row--){
			for(col = 0; col < 8; col++){
				if(gameTile[row][col].useable){
					blackPlayer[blackIndex] = new GamePiece("Black");
					gameTile[row][col].setGamePiece(blackPlayer[blackIndex]);
					gameTile[row][col].setIcon(new ImageIcon(blackPlayer[blackIndex].getImage()));
					blackIndex++;
					blackPieces++;
				}
				if(blackIndex == 12)
					break;
			}
		}
	}
	
	//this is a function that un-highlights any squares currently highlighted
	public  void resetHighlights(){
		for(int row = 0; row < 8; row++){
			for(int col = 0; col < 8; col++){
				if(gameTile[row][col].isUseable()){
					gameTile[row][col].setBackground(grey);
					gameTile[row][col].setHighlight(false);
				}
			}
		}
	}
	
	public  boolean hasBlackPiece(int row, int col){
		if(gameTile[row][col].getGamePiece() != null && gameTile[row][col].getGamePiece().getPlayer().compareTo("Black") == 0){
			return true;
		}
		return false;
				
	}
	public  boolean hasRedPiece(int row, int col){
		if(gameTile[row][col].getGamePiece() != null && gameTile[row][col].getGamePiece().getPlayer().compareTo("Red") == 0){
			return true;
		}
		return false;
				
	}
	
	public  boolean isBlackTurn(){
		if (isBlackTurn == true) return true;
		return false;
	}
	
	public  void changeTurn(){
		mustJump = false;
		iJumped = false;
		iGotKinged = false;
		if (isBlackTurn == true) isBlackTurn = false;
		else isBlackTurn = true;
		scanForJump();
	}
	//this function counts all of the jumps that a player has on his turn. Since the rules state that if you can jump then you must, 
	//we have to make sure the player doesn't have any jumps available before allowing him to move freely
	public  void scanForJump(){
		for(int row = 0; row < 8; row++){
			for(int col = 0; col < 8; col++){
				if(isBlackTurn){
					if(hasBlackPiece(row,col)){
						if(gameTile[row][col].getGamePiece().isKing()){
							if(row > 1 && col > 1
									&& hasRedPiece(row-1,col-1)
									&& !gameTile[row-2][col-2].isOccupied()){
								mustJump = true;		
							}
							if(row > 1 && col < 6
									&& hasRedPiece(row-1,col+1)
									&& !gameTile[row-2][col+2].isOccupied()){
								mustJump = true;
							}
							if(row < 6 && col < 6
									&& hasRedPiece(row+1,col+1)
									&& !gameTile[row+2][col+2].isOccupied()){
								mustJump = true;
							}
							if(row < 6 && col > 1
									&& hasRedPiece(row+1,col-1)
									&& !gameTile[row+2][col-2].isOccupied()){
								mustJump = true;
							}
						}
						else { //not a king piece
							if(row > 1 && col > 1
									&& hasRedPiece(row-1,col-1)
									&& !gameTile[row-2][col-2].isOccupied()){
								mustJump = true;		
							}
							if(row > 1 && col < 6
									&& hasRedPiece(row-1,col+1)
									&& !gameTile[row-2][col+2].isOccupied()){
								mustJump = true;
							}
						}
					}
				}
				else{//it must be red players turn then
					if(hasRedPiece(row,col)){
						if(gameTile[row][col].getGamePiece().isKing()){
							if(row > 1 && col > 1
									&& hasBlackPiece(row-1,col-1)
									&& !gameTile[row-2][col-2].isOccupied()){
								mustJump = true;		
							}
							if(row > 1 && col < 6
									&& hasBlackPiece(row-1,col+1)
									&& !gameTile[row-2][col+2].isOccupied()){
								mustJump = true;
							}
							if(row < 6 && col < 6
									&& hasBlackPiece(row+1,col+1)
									&& !gameTile[row+2][col+2].isOccupied()){
								mustJump = true;
							}
							if(row < 6 && col > 1
									&& hasBlackPiece(row+1,col-1)
									&& !gameTile[row+2][col-2].isOccupied()){
								mustJump = true;
							}
						}
						else { //not a king piece
							if(row < 6 && col < 6
									&& hasBlackPiece(row+1,col+1)
									&& !gameTile[row+2][col+2].isOccupied()){
								mustJump = true;
							}
							if(row < 6 && col > 1
									&& hasBlackPiece(row+1,col-1)
									&& !gameTile[row+2][col-2].isOccupied()){
								mustJump = true;
							}
						}
					}
				}
			}
		}
	}
	
	//count all available moves a player has
	public  void ScanForMoves(){
		blackMoves = 0;
		redMoves = 0;
		for(int row = 0; row < 8; row++){
			for(int col = 0; col < 8; col++){
				if(isBlackTurn){
					if(hasBlackPiece(row,col)){
						if(gameTile[row][col].getGamePiece().isKing()){
							//black kings
							if(row > 1 && col > 1
									&& hasRedPiece(row-1,col-1)
									&& !gameTile[row-2][col-2].isOccupied()){
								blackMoves++;		
							}
							if(row > 1 && col < 6
									&& hasRedPiece(row-1,col+1)
									&& !gameTile[row-2][col+2].isOccupied()){
								blackMoves++;
							}
							if(row < 6 && col < 6
									&& hasRedPiece(row+1,col+1)
									&& !gameTile[row+2][col+2].isOccupied()){
								blackMoves++;
							}
							if(row < 6 && col > 1
									&& hasRedPiece(row+1,col-1)
									&& !gameTile[row+2][col-2].isOccupied()){
								blackMoves++;
							}
							if (row == 0 && col == 0){
								if(!gameTile[row+1][col+1].isOccupied() && !mustJump){
									blackMoves++;
								}							
							}														
							else if (row == 7 && col == 7){								
								if(!gameTile[row-1][col-1].isOccupied() && !mustJump){
									blackMoves++;								
								}
							}
							else if (row == 0){
								if(!gameTile[row+1][col+1].isOccupied() && !mustJump){
									blackMoves++;
								}
								
								if(!gameTile[row+1][col-1].isOccupied() && !mustJump){
									blackMoves++;	
								}
							
							}																	
							else if (row == 7){
								if(!gameTile[row-1][col+1].isOccupied() && !mustJump){				
									blackMoves++;	
								}							
								if(!gameTile[row-1][col-1].isOccupied() && !mustJump){
									blackMoves++;
								}							
							}
							else if(col == 0){
								if(!gameTile[row+1][col+1].isOccupied() && !mustJump){
									blackMoves++;	
								}								
								if(!gameTile[row-1][col+1].isOccupied() && !mustJump){
									blackMoves++;
								}							
							}
							else if(col == 7){
								if(!gameTile[row+1][col-1].isOccupied() && !mustJump){
									blackMoves++;
								}							
								if(!gameTile[row-1][col-1].isOccupied() && !mustJump){
									blackMoves++;	
								}								
							}
							
							else{
								if(!gameTile[row+1][col+1].isOccupied() && !mustJump){
									blackMoves++;
								}							
								if(!gameTile[row+1][col-1].isOccupied() && !mustJump){
									blackMoves++;
								}							
								if(!gameTile[row-1][col+1].isOccupied() && !mustJump){
									blackMoves++;
								}							
								if(!gameTile[row-1][col-1].isOccupied() && !mustJump){
									blackMoves++;	
								}							
							}	
						}
				
						else { //not a black king piece
							if(row > 1 && col > 1
									&& hasRedPiece(row-1,col-1)
									&& !gameTile[row-2][col-2].isOccupied()){
								blackMoves++;		
							}
							if(row > 1 && col < 6
									&& hasRedPiece(row-1,col+1)
									&& !gameTile[row-2][col+2].isOccupied()){
								blackMoves++;
							}
							
								
								if(col == 0){
									if(!gameTile[row-1][col+1].isOccupied()&& !mustJump){
										blackMoves++;
									}								
								}
								else if(col == 7){
									if(!gameTile[row-1][col-1].isOccupied()&& !mustJump){
										blackMoves++;					
									}									
								}
								else { 
									if(col != 7 && !gameTile[row-1][col+1].isOccupied() && !mustJump){
										blackMoves++;	
									}									
									if(col != 0 && !gameTile[row-1][col-1].isOccupied() && !mustJump){
										blackMoves++;	
									}								
								}
							
						}
					}
				}
				else{//it must be red's turn
					if(hasRedPiece(row,col)){
						if(gameTile[row][col].getGamePiece().isKing()){
							//red kings
							if(row > 1 && col > 1
									&& hasBlackPiece(row-1,col-1)
									&& !gameTile[row-2][col-2].isOccupied()){
								redMoves++;		
							}
							if(row > 1 && col < 6
									&& hasBlackPiece(row-1,col+1)
									&& !gameTile[row-2][col+2].isOccupied()){
								redMoves++;								}
							if(row < 6 && col < 6
									&& hasBlackPiece(row+1,col+1)
									&& !gameTile[row+2][col+2].isOccupied()){
								redMoves++;	
							}
							if(row < 6 && col > 1
									&& hasBlackPiece(row+1,col-1)
									&& !gameTile[row+2][col-2].isOccupied()){
								redMoves++;	
							}							
							
							if (row == 0 && col == 0){
								if(!gameTile[row+1][col+1].isOccupied() && !mustJump){
									redMoves++;	
								}	
							}
		
							else if (row == 7 && col == 7){
								 if(!gameTile[row-1][col-1].isOccupied() && !mustJump){
									redMoves++;	
								}	
							}
							else if (row == 0){
								if(!gameTile[row+1][col+1].isOccupied() && !mustJump){
									redMoves++;	
								}
									
								if(!gameTile[row+1][col-1].isOccupied() && !mustJump){
									redMoves++;	
								}
	
							}
									
							else if (row == 7){
								if(!gameTile[row-1][col+1].isOccupied() && !mustJump){
									redMoves++;	
								}
								
								if(!gameTile[row-1][col-1].isOccupied() && !mustJump){
									redMoves++;	
								}		
							}
							
							else if(col == 0){
								if(!gameTile[row+1][col+1].isOccupied() && !mustJump){
									gameTile[row+1][col+1].setBackground(highlight);
									gameTile[row+1][col+1].setHighlight(true);
									redMoves++;	
								}
							
								if(!gameTile[row-1][col+1].isOccupied() && !mustJump){
									redMoves++;	
								}
							}
							
							else if(col == 7){
								if(!gameTile[row+1][col-1].isOccupied() && !mustJump){
									gameTile[row+1][col-1].setBackground(highlight);
									gameTile[row+1][col-1].setHighlight(true);
									redMoves++;	
								}
								if(!gameTile[row-1][col-1].isOccupied() && !mustJump){
									redMoves++;	
								}		
							}
					
							else {
								if(!gameTile[row+1][col+1].isOccupied() && !mustJump){
									redMoves++;	
								}
								
								if(!gameTile[row+1][col-1].isOccupied() && !mustJump){
									redMoves++;	
								}
								
								if(!gameTile[row-1][col+1].isOccupied() && !mustJump){
									redMoves++;	
								}
								
								if(!gameTile[row-1][col-1].isOccupied() && !mustJump){
									redMoves++;	
								}
							}	
						}
						else { //not a red king piece
							if(row < 6 && col < 6
									&& hasBlackPiece(row+1,col+1)
									&& !gameTile[row+2][col+2].isOccupied()){
								redMoves++;	
							}
							if(row < 6 && col > 1
									&& hasBlackPiece(row+1,col-1)
									&& !gameTile[row+2][col-2].isOccupied()){
								redMoves++;	
							}
							
							if(col == 0){
								if(!gameTile[row+1][col+1].isOccupied() && !mustJump){
									redMoves++;	
								}
								
							}
							
							else if(col == 7){
								if(!gameTile[row+1][col-1].isOccupied() && !mustJump){
									redMoves++;	
								}
							}
							
							else {
								if(!gameTile[row+1][col+1].isOccupied() && !mustJump){
									redMoves++;	
								}
									
								if(!gameTile[row+1][col-1].isOccupied() && !mustJump){
									redMoves++;	
								}
							}	
						}					
					}
				}
			}
		}	
		blackmoves = "moves available for black player: "+ blackMoves;
		redmoves = "moves available for red player: "+ redMoves;		
	}
	
	//when a player clicks a piece, we should highlight any squares that it can move to. this function finds the moves for a piece
	public void HighlightMoves(int row, int col){
		resetHighlights();
		gameTile[row][col].setBackground(highlight);
		if(gameTile[row][col].getGamePiece() != null){
			if(gameTile[row][col].getGamePiece().getPlayer().compareTo("Red") == 0){
				if(gameTile[row][col].getGamePiece().isKing()){
					//Allow both direction movement	for kings			
					if (row == 0 && col == 0){
						//highlight jump
						if(col != 6 && row != 6 && gameTile[row+1][col+1].isOccupied()
								&& gameTile[row+1][col+1].getGamePiece().player.compareTo("Black") == 0
								&& !gameTile[row+2][col+2].isOccupied()){
							highlightMove(row,col,2,2);
						}
						else if(!gameTile[row+1][col+1].isOccupied() && !mustJump){
							highlightMove(row,col,1,1);
						}	
					}
					
					else if (row == 7 && col == 7){
						//highlight jump
						if(col != 1 && row != 1 && gameTile[row-1][col-1].isOccupied()
								&& gameTile[row-1][col-1].getGamePiece().player.compareTo("Black") == 0
								&& !gameTile[row-2][col-2].isOccupied()){
							highlightMove(row,col,-2,-2);
						}
						else if(!gameTile[row-1][col-1].isOccupied() && !mustJump){
							highlightMove(row,col,-1,-1);
						}
					}
					
					else if (row == 0){
						//highlight jump
						if(col != 6 && row != 6 && gameTile[row+1][col+1].isOccupied()
								&& gameTile[row+1][col+1].getGamePiece().player.compareTo("Black") == 0
								&& !gameTile[row+2][col+2].isOccupied()){
							highlightMove(row,col,2,2);
						}
						else if(!gameTile[row+1][col+1].isOccupied() && !mustJump){
							highlightMove(row,col,1,1);
						}
						
						if(!gameTile[row+1][col-1].isOccupied() && !mustJump){
							highlightMove(row,col,1,-1);
						}
						//highlight jump
						if(col != 1 && row != 6 && gameTile[row+1][col-1].isOccupied()
								&& gameTile[row+1][col-1].getGamePiece().player.compareTo("Black") == 0
								&& !gameTile[row+2][col-2].isOccupied()){
							highlightMove(row,col,2,-2);
						}
						
					}
						
					else if (row == 7){
						if(!gameTile[row-1][col+1].isOccupied() && !mustJump){
							highlightMove(row,col,-1,1);
						}
						//highlight jump
						if(col != 6 && row != 1 && gameTile[row-1][col+1].isOccupied()
								&& gameTile[row-1][col+1].getGamePiece().player.compareTo("Black") == 0
								&& !gameTile[row-2][col+2].isOccupied()){
							highlightMove(row,col,-2,+2);
						}
						if(!gameTile[row-1][col-1].isOccupied() && !mustJump){
							highlightMove(row,col,-1,-1);
						}
						//highlight jump
						if(col != 1 && row != 1 && gameTile[row-1][col-1].isOccupied()
								&& gameTile[row-1][col-1].getGamePiece().player.compareTo("Black") == 0
								&& !gameTile[row-2][col-2].isOccupied()){
							highlightMove(row,col,-2,-2);
						}
					}
					else if(col == 0){
						if(!gameTile[row+1][col+1].isOccupied() && !mustJump){
							gameTile[row+1][col+1].setBackground(highlight);
							gameTile[row+1][col+1].setHighlight(true);
							highlightMove(row,col,1,1);
						}
						//highlight jump
						if(col != 6 && row != 6 && gameTile[row+1][col+1].isOccupied()
								&& gameTile[row+1][col+1].getGamePiece().player.compareTo("Black") == 0
								&& !gameTile[row+2][col+2].isOccupied()){
							highlightMove(row,col,2,2);
						}
						if(!gameTile[row-1][col+1].isOccupied() && !mustJump){
							highlightMove(row,col,-1,1);
						}
						//highlight jump
						if(col != 6 && row != 1 && gameTile[row-1][col+1].isOccupied()
								&& gameTile[row-1][col+1].getGamePiece().player.compareTo("Black") == 0
								&& !gameTile[row-2][col+2].isOccupied()){
							highlightMove(row,col,-2,2);
						}
					}
					else if(col == 7){
						if(!gameTile[row+1][col-1].isOccupied() && !mustJump){
							gameTile[row+1][col-1].setBackground(highlight);
							gameTile[row+1][col-1].setHighlight(true);
							highlightMove(row,col,1,-1);
						}
						//highlight jump
						else if(col != 1 && row != 6 && gameTile[row+1][col-1].isOccupied()
								&& gameTile[row+1][col-1].getGamePiece().player.compareTo("Black") == 0
								&& !gameTile[row+2][col-2].isOccupied()){
							highlightMove(row,col,2,-2);
						}
						if(!gameTile[row-1][col-1].isOccupied() && !mustJump){
							highlightMove(row,col,-1,-1);
						}
						//highlight jump
						if(col != 1 && row != 1 && gameTile[row-1][col-1].isOccupied()
								&& gameTile[row-1][col-1].getGamePiece().player.compareTo("Black") == 0
								&& !gameTile[row-2][col-2].isOccupied()){
							highlightMove(row,col,-2,-2);
						}
					}
					
					else {
						if(!gameTile[row+1][col+1].isOccupied() && !mustJump){
							highlightMove(row,col,1,1);
						}
						//highlight jump
						if(col != 6 && row != 6 && gameTile[row+1][col+1].isOccupied()
								&& gameTile[row+1][col+1].getGamePiece().player.compareTo("Black") == 0
								&& !gameTile[row+2][col+2].isOccupied()){
							highlightMove(row,col,2,2);
						}
						
						if(!gameTile[row+1][col-1].isOccupied() && !mustJump){
							highlightMove(row,col,1,-1);
						}
						//highlight jump
						if(col != 1 && row != 6 && gameTile[row+1][col-1].isOccupied()
								&& gameTile[row+1][col-1].getGamePiece().player.compareTo("Black") == 0
								&& !gameTile[row+2][col-2].isOccupied()){

							highlightMove(row,col,2,-2);
						}
						if(!gameTile[row-1][col+1].isOccupied() && !mustJump){
							highlightMove(row,col,-1,1);
						}
						//highlight jump
						if(col != 6 && row != 1 && gameTile[row-1][col+1].isOccupied()
								&& gameTile[row-1][col+1].getGamePiece().player.compareTo("Black") == 0
								&& !gameTile[row-2][col+2].isOccupied()){
							highlightMove(row,col,-2,2);
						}
						if(!gameTile[row-1][col-1].isOccupied() && !mustJump){
							highlightMove(row,col,-1,-1);
						}
						//highlight jump
						if(col != 1 && row != 1 && gameTile[row-1][col-1].isOccupied()
								&& gameTile[row-1][col-1].getGamePiece().player.compareTo("Black") == 0
								&& !gameTile[row-2][col-2].isOccupied()){
							highlightMove(row,col,-2,-2);
						}
					}
				}
				else{
					//Can only move down
					if(col == 0){
						if(!gameTile[row+1][col+1].isOccupied() && !mustJump){
							highlightMove(row,col,1,1);
						}
						//highlight jump
						if(col != 6 && row != 6 && gameTile[row+1][col+1].isOccupied()
								&& gameTile[row+1][col+1].getGamePiece().player.compareTo("Black") == 0
								&& !gameTile[row+2][col+2].isOccupied()){
							highlightMove(row,col,2,2);
						}
					}
					else if(col == 7){
						if(!gameTile[row+1][col-1].isOccupied() && !mustJump){
							highlightMove(row,col,1,-1);
						}
						//highlight jump
						if(col != 1 && row != 6 && gameTile[row+1][col-1].isOccupied()
								&& gameTile[row+1][col-1].getGamePiece().player.compareTo("Black") == 0
								&& !gameTile[row+2][col-2].isOccupied()){							
							highlightMove(row,col,2,-2);
						}
					}
					else {
						if(!gameTile[row+1][col+1].isOccupied() && !mustJump){
							highlightMove(row,col,1,1);
						}
						//highlight jump
						if(col != 6 && row != 6 && gameTile[row+1][col+1].isOccupied()
								&& gameTile[row+1][col+1].getGamePiece().player.compareTo("Black") == 0
								&& !gameTile[row+2][col+2].isOccupied()){
							highlightMove(row,col,2,2);
						}
						
						if(!gameTile[row+1][col-1].isOccupied() && !mustJump){
							highlightMove(row,col,1,-1);
						}
						//highlight jump
						if(col != 1 && row != 6 && gameTile[row+1][col-1].isOccupied()
								&& gameTile[row+1][col-1].getGamePiece().player.compareTo("Black") == 0
								&& !gameTile[row+2][col-2].isOccupied()){
							highlightMove(row,col,2,-2);
						}
						
					}
				}
			}
			else if(gameTile[row][col].getGamePiece().getPlayer().compareTo("Black") == 0){
				if(gameTile[row][col].getGamePiece().isKing()){
					//Allow both direction movement					
					if (row == 0 && col == 0){
						if(!gameTile[row+1][col+1].isOccupied() && !mustJump){
							highlightMove(row,col,1,1);
						}
						//highlight jump
						if(col != 6 && row != 6 && gameTile[row+1][col+1].isOccupied()
								&& gameTile[row+1][col+1].getGamePiece().player.compareTo("Red") == 0
								&& !gameTile[row+2][col+2].isOccupied()){
							highlightMove(row,col,2,2);
						}
					}
					
					else if (row == 7 && col == 7)
					{
						
						if(!gameTile[row-1][col-1].isOccupied() && !mustJump){
							highlightMove(row,col,-1,-1);
						}
						//highlight jump
						if(col != 1 && row != 1 && gameTile[row-1][col-1].isOccupied()
								&& gameTile[row-1][col-1].getGamePiece().player.compareTo("Red") == 0
								&& !gameTile[row-2][col-2].isOccupied()){
							highlightMove(row,col,-2,-2);
						}
					}
					else if (row == 0){
						if(!gameTile[row+1][col+1].isOccupied() && !mustJump){
							highlightMove(row,col,1,1);
						}
						//highlight jump
						if(col != 6 && row != 6 && gameTile[row+1][col+1].isOccupied()
								&& gameTile[row+1][col+1].getGamePiece().player.compareTo("Red") == 0
								&& !gameTile[row+2][col+2].isOccupied()){
							highlightMove(row,col,2,2);
						}
						if(!gameTile[row+1][col-1].isOccupied() && !mustJump){
							highlightMove(row,col,1,-1);
						}
						//highlight jump
						if(col != 1 && row != 6 && gameTile[row+1][col-1].isOccupied()
								&& gameTile[row+1][col-1].getGamePiece().player.compareTo("Red") == 0
								&& !gameTile[row+2][col-2].isOccupied()){
							highlightMove(row,col,2,-2);
						}					
					}
						
					else if (row == 7){
						if(!gameTile[row-1][col+1].isOccupied() && !mustJump){				
							highlightMove(row,col,-1,1);
						}
						//highlight jump
						if(col != 6 && row != 1 && gameTile[row-1][col+1].isOccupied()
								&& gameTile[row-1][col+1].getGamePiece().player.compareTo("Red") == 0
								&& !gameTile[row-2][col+2].isOccupied()){
							highlightMove(row,col,-2,2);
						}
						if(!gameTile[row-1][col-1].isOccupied() && !mustJump){
							highlightMove(row,col,-1,-1);
						}
						//highlight jump
						if(col != 1 && row != 1 && gameTile[row-1][col-1].isOccupied()
								&& gameTile[row-1][col-1].getGamePiece().player.compareTo("Red") == 0
								&& !gameTile[row-2][col-2].isOccupied()){
							highlightMove(row,col,-2,-2);
						}
					}
					else if(col == 0){
						if(!gameTile[row+1][col+1].isOccupied() && !mustJump){
							highlightMove(row,col,1,1);
						}
						//highlight jump
						if(col != 6 && row != 6 && gameTile[row+1][col+1].isOccupied() 
								&& gameTile[row+1][col+1].getGamePiece().player.compareTo("Red") == 0
								&& !gameTile[row+2][col+2].isOccupied()){
							highlightMove(row,col,2,2);
						}
						if(!gameTile[row-1][col+1].isOccupied() && !mustJump){
							highlightMove(row,col,-1,1);
						}
						//highlight jump
						if(col != 6 && row != 1 && gameTile[row-1][col+1].isOccupied() 
								&& gameTile[row-1][col+1].getGamePiece().player.compareTo("Red") == 0
								&& !gameTile[row-2][col+2].isOccupied()){
							highlightMove(row,col,-2,2);
						}
					}
					else if(col == 7){
						if(!gameTile[row+1][col-1].isOccupied() && !mustJump){
							highlightMove(row,col,1,-1);
						}
						//highlight jump
						if(col != 1 && row != 6 && gameTile[row+1][col-1].isOccupied()
								&& gameTile[row+1][col-1].getGamePiece().player.compareTo("Red") == 0
								&& !gameTile[row+2][col-2].isOccupied()){
							highlightMove(row,col,2,-2);
						}
						if(!gameTile[row-1][col-1].isOccupied() && !mustJump){
							highlightMove(row,col,-1,-1);
						}
						//highlight jump
						if(col != 1 && row != 1 && gameTile[row-1][col-1].isOccupied()
								&& gameTile[row-1][col-1].getGamePiece().player.compareTo("Red") == 0
								&& !gameTile[row-2][col-2].isOccupied()){
							highlightMove(row,col,-2,-2);
						}
					}
					
					else {
						if(!gameTile[row+1][col+1].isOccupied() && !mustJump){
							highlightMove(row,col,1,1);
						}
						//highlight jump
						if(col != 6 && row != 6 && gameTile[row+1][col+1].isOccupied()
								&& gameTile[row+1][col+1].getGamePiece().player.compareTo("Red") == 0
								&& !gameTile[row+2][col+2].isOccupied()){
							highlightMove(row,col,2,2);
						}
						if(!gameTile[row+1][col-1].isOccupied() && !mustJump){
							highlightMove(row,col,1,-1);
						}
						//highlight jump
						if(col != 1 && row != 6 && gameTile[row+1][col-1].isOccupied()
								&& gameTile[row+1][col-1].getGamePiece().player.compareTo("Red") == 0
								&& !gameTile[row+2][col-2].isOccupied()){
							highlightMove(row,col,2,-2);
						}
						if(!gameTile[row-1][col+1].isOccupied() && !mustJump){
							highlightMove(row,col,-1,1);
						}
						//highlight jump
						if(col != 6 && row != 1 && gameTile[row-1][col+1].isOccupied()
								&& gameTile[row-1][col+1].getGamePiece().player.compareTo("Red") == 0
								&& !gameTile[row-2][col+2].isOccupied()){
							highlightMove(row,col,-2,2);
						}
						if(!gameTile[row-1][col-1].isOccupied() && !mustJump){
							highlightMove(row,col,-1,-1);
						}
						//highlight jump
						if(col != 1 && row != 1 && gameTile[row-1][col-1].isOccupied()
								&& gameTile[row-1][col-1].getGamePiece().player.compareTo("Red") == 0
								&& !gameTile[row-2][col-2].isOccupied()){
							highlightMove(row,col,-2,-2);
						}
					}
				}
				else{
					//can only move up
					if(col == 0){
						if(!gameTile[row-1][col+1].isOccupied()&& !mustJump){
							highlightMove(row,col,-1,+1);
						}
						//highlight jump
						if(col != 6 && row != 1 && gameTile[row-1][col+1].isOccupied() 
								&& gameTile[row-1][col+1].getGamePiece().player.compareTo("Red") == 0
								&& !gameTile[row-2][col+2].isOccupied()){
							highlightMove(row,col,-2,+2);
						}
					}
					else if(col == 7){
						if(!gameTile[row-1][col-1].isOccupied()&& !mustJump){
							highlightMove(row,col,-1,-1);
						}
						//highlight jump
						if(col != 1 && row != 1 && gameTile[row-1][col-1].isOccupied()
								&& gameTile[row-1][col-1].getGamePiece().player.compareTo("Red") == 0
								&& !gameTile[row-2][col-2].isOccupied()){
							highlightMove(row,col,-2,-2);
						}
					}
					else { 

						if(col != 7 && !gameTile[row-1][col+1].isOccupied() && !mustJump){
							highlightMove(row,col,-1,+1);
						}
						//highlight jump
						if(col < 6 && row > 1
								&& col != 6 && row != 1 && gameTile[row-1][col+1].isOccupied() 
								&& gameTile[row-1][col+1].getGamePiece().player.compareTo("Red") == 0 
								&& !gameTile[row-2][col+2].isOccupied()){
							highlightMove(row,col,-2,+2);
						}
						if(col != 0 && !gameTile[row-1][col-1].isOccupied() && !mustJump){
							highlightMove(row,col,-1,-1);
						}
						//highlight jump
						if(col > 1 && row > 1
								&& col != 1 && row != 1 && gameTile[row-1][col-1].isOccupied()
								&& gameTile[row-1][col-1].getGamePiece().player.compareTo("Red") == 0
								&& !gameTile[row-2][col-2].isOccupied()){
							highlightMove(row,col,-2,-2);
						}
					}
				}
			}
		}
	}
	
	//function to actually change the color of the tile to highlighted
	public void highlightMove(int r, int c, int rM, int cM){
		gameTile[r+rM][c+cM].setBackground(highlight);
		gameTile[r+rM][c+cM].setHighlight(true);
		fromRow = r;
		fromCol = c;
	}
	
	//detect victory for black player when red has no pieces left or can not make a move on his turn
	public  boolean blackVictory(){
		if (redPieces == 0) return true;
		if (redMoves == 0 && !isBlackTurn) return true;
		return false;
	}
	
	//detect victory for red player when black has no pieces left or can not make a move on his turn
	public  boolean redVictory(){
		if (blackPieces == 0) return true;
		if (blackMoves == 0 && isBlackTurn) return true;
		return false;
	}

	//a player can jump again if he has a jump available in his new position after a jump, but has not just been kinged after a jump
	public void canJumpAgain(int row, int col){
		if(iGotKinged) {
			changeTurn();
			return;
		}
		if(!iJumped) {
			changeTurn();
			return;
		}
		if(gameTile[row][col].getGamePiece().isKing()) {
			if (col > 0 && row >0
					&& col != 1 && row != 1 && gameTile[row-1][col-1].isOccupied()
					&& gameTile[row-1][col-1].getGamePiece().player.compareTo(gameTile[row][col].getGamePiece().getPlayer()) != 0
					&& !gameTile[row-2][col-2].isOccupied() || col < 7 && row > 0
					&& col != 6 && row != 1 && gameTile[row-1][col+1].isOccupied()
					&& gameTile[row-1][col+1].getGamePiece().player.compareTo(gameTile[row][col].getGamePiece().getPlayer()) != 0
					&& !gameTile[row-2][col+2].isOccupied() || col < 7 && row < 6
					&& col != 1 && row != 1 && gameTile[row+1][col+1].isOccupied()
					&& gameTile[row+1][col+1].getGamePiece().player.compareTo(gameTile[row][col].getGamePiece().getPlayer()) != 0
					&& !gameTile[row+2][col+2].isOccupied() || col > 0 && row < 6
					&& col != 1 && row != 1 && gameTile[row+1][col-1].isOccupied()
					&& gameTile[row+1][col-1].getGamePiece().player.compareTo(gameTile[row][col].getGamePiece().getPlayer()) != 0
					&& !gameTile[row+2][col-2].isOccupied()){
				if(col > 1 && row > 1
						&& col != 1 && row != 1 && gameTile[row-1][col-1].isOccupied()
						&& gameTile[row-1][col-1].getGamePiece().player.compareTo(gameTile[row][col].getGamePiece().getPlayer()) != 0
						&& !gameTile[row-2][col-2].isOccupied()){
					highlightMove(row,col,-2,-2);
				
				}
				if(col < 6 && row > 1
						&& col != 6 && row != 1 && gameTile[row-1][col+1].isOccupied()
						&& gameTile[row-1][col+1].getGamePiece().player.compareTo(gameTile[row][col].getGamePiece().getPlayer()) != 0
						&& !gameTile[row-2][col+2].isOccupied()){
					highlightMove(row,col,-2,2);
				}
				if(col < 6 && row < 6
						&& col != 1 && row != 1 && gameTile[row+1][col+1].isOccupied()
						&& gameTile[row+1][col+1].getGamePiece().player.compareTo(gameTile[row][col].getGamePiece().getPlayer()) != 0
						&& !gameTile[row+2][col+2].isOccupied()){
					highlightMove(row,col,2,2);
				}
				if(col > 1 && row < 6
						&& col != 1 && row != 1 && gameTile[row+1][col-1].isOccupied()
						&& gameTile[row+1][col-1].getGamePiece().player.compareTo(gameTile[row][col].getGamePiece().getPlayer()) != 0
						&& !gameTile[row+2][col-2].isOccupied()){
					highlightMove(row,col,2,-2);
				}
			return;
			}
		}
		else {
			if(gameTile[row][col].getGamePiece().player.compareTo("Black") == 0){
				if(row == 1 || row == 0){
					changeTurn();
					return;
				}
				
				if(col > 0 && row > 1
						&& col != 1 && row != 1 && gameTile[row-1][col-1].isOccupied()
						&& gameTile[row-1][col-1].getGamePiece().player.compareTo(gameTile[row][col].getGamePiece().getPlayer()) != 0
						&& !gameTile[row-2][col-2].isOccupied() || col < 6 && row > 1
						&& col != 6 && row != 1 && gameTile[row-1][col+1].isOccupied()
						&& gameTile[row-1][col+1].getGamePiece().player.compareTo(gameTile[row][col].getGamePiece().getPlayer()) != 0
						&& !gameTile[row-2][col+2].isOccupied()){
					if(col > 1 && row > 1
							&& col != 1 && row != 1 && gameTile[row-1][col-1].isOccupied()
							&& gameTile[row-1][col-1].getGamePiece().player.compareTo(gameTile[row][col].getGamePiece().getPlayer()) != 0
							&& !gameTile[row-2][col-2].isOccupied()){
						highlightMove(row,col,-2,-2);
					}
					if(col < 6 && row > 1
							&& col != 6 && row != 1 && gameTile[row-1][col+1].isOccupied()
							&& gameTile[row-1][col+1].getGamePiece().player.compareTo(gameTile[row][col].getGamePiece().getPlayer()) != 0
							&& !gameTile[row-2][col+2].isOccupied()){
						highlightMove(row,col,-2,2);
					}
				return;
				}	
			}
			else{
				if(row == 7 || row == 6){
					changeTurn();
					return;
				}
				if((col < 6 && row < 6
						&& col != 1 && row != 1 && gameTile[row+1][col+1].isOccupied()
						&& gameTile[row+1][col+1].getGamePiece().player.compareTo(gameTile[row][col].getGamePiece().getPlayer()) != 0
						&& !gameTile[row+2][col+2].isOccupied()) || (col > 1 && row < 6
						&& col != 1 && row != 6 && gameTile[row+1][col-1].isOccupied()
						&& gameTile[row+1][col-1].getGamePiece().player.compareTo(gameTile[row][col].getGamePiece().getPlayer()) != 0
						&& !gameTile[row+2][col-2].isOccupied())){
					if(col < 6 && row < 6
							&& col != 1 && row != 1 && gameTile[row+1][col+1].isOccupied()
							&& gameTile[row+1][col+1].getGamePiece().player.compareTo(gameTile[row][col].getGamePiece().getPlayer()) != 0
							&& !gameTile[row+2][col+2].isOccupied()){
						highlightMove(row,col,2,2);
					}
					if(col > 1 && row < 6
							&& col != 1 && row != 6 && gameTile[row+1][col-1].isOccupied()
							&& gameTile[row+1][col-1].getGamePiece().player.compareTo(gameTile[row][col].getGamePiece().getPlayer()) != 0
							&& !gameTile[row+2][col-2].isOccupied()){
						highlightMove(row,col,2,-2);
					}	
					return;
				}
			}
		}
		changeTurn();
	}

	public void MovePiece(int fromRow, int fromCol, int row, int col) {
		//move the piece in [fromRow, fromCol] to [row, col]
		//this will delete the piece in [fromRow, fromCol] and create a new piece that is the same as the deleted piece in [row, col]
		GamePiece gp;
		iJumped = false;
		iGotKinged = false;
		gp = gameTile[fromRow][fromCol].getGamePiece();
		gameTile[fromRow][fromCol].removeGamePiece();
		gameTile[fromRow][fromCol].setIcon(null);
		gameTile[row][col].setGamePiece(gp);
		if (row == 0 || row == 7){
			gp.kingMe();
			iGotKinged = true;
		}
		gameTile[row][col].setIcon(new ImageIcon(gp.getImage()));
		//if the move was a jump, remove the jumped piece
		if (row - fromRow == 2 && col - fromCol == 2 ){
			if (gameTile[fromRow+1][fromCol+1].getGamePiece().getPlayer().compareTo("Black") == 0) blackPieces--;
			if (gameTile[fromRow+1][fromCol+1].getGamePiece().getPlayer().compareTo("Red") == 0) redPieces--;
			gameTile[fromRow+1][fromCol+1].removeGamePiece();
			gameTile[fromRow+1][fromCol+1].setIcon(null);
			iJumped = true;
		}
		else if (fromRow-row == 2 && col - fromCol == 2 ){
			if (gameTile[fromRow-1][fromCol+1].getGamePiece().getPlayer().compareTo("Black") == 0) blackPieces--;
			if (gameTile[fromRow-1][fromCol+1].getGamePiece().getPlayer().compareTo("Red") == 0) redPieces--;
			gameTile[fromRow-1][fromCol+1].removeGamePiece();
			gameTile[fromRow-1][fromCol+1].setIcon(null);
			iJumped = true;
		}
		else if (row - fromRow == 2 && fromCol - col == 2 ){
			if (gameTile[fromRow+1][fromCol-1].getGamePiece().getPlayer().compareTo("Black") == 0) blackPieces--;
			if (gameTile[fromRow+1][fromCol-1].getGamePiece().getPlayer().compareTo("Red") == 0) redPieces--;			
			gameTile[fromRow+1][fromCol-1].removeGamePiece();
			gameTile[fromRow+1][fromCol-1].setIcon(null);
			iJumped = true;
		}
		else if (fromRow-row == 2 && fromCol -col == 2 ){
			if (gameTile[fromRow-1][fromCol-1].getGamePiece().getPlayer().compareTo("Black") == 0) blackPieces--;
			if (gameTile[fromRow-1][fromCol-1].getGamePiece().getPlayer().compareTo("Red") == 0) redPieces--;			
			gameTile[fromRow-1][fromCol-1].removeGamePiece();
			gameTile[fromRow-1][fromCol-1].setIcon(null);
			iJumped = true;
		}
		resetHighlights();
		canJumpAgain(row, col);
		ScanForMoves();
		if (blackVictory()){
			maingame.m.messages.setText("black wins!");
			if (ranked){
				if(maingame.m.login.tournament){
					maingame.m.tournament.currentMatch.blackWins();
					maingame.m.login.rankedStats.winLoseFor(maingame.m.tournament.currentMatch.player1.toString(), maingame.m.tournament.currentMatch.player2.toString());
					maingame.m.tournament.nextMatch();
					maingame.m.next.setEnabled(true);
				}
				else{
					maingame.m.login.rankedStats.winLoseFor(maingame.m.login.players[0], maingame.m.login.players[1]);
				}
			}
		}
		else if (redVictory()){
			maingame.m.messages.setText("red wins!");
			if (ranked){
				if(maingame.m.login.tournament){
					maingame.m.tournament.currentMatch.redWins();
					maingame.m.login.rankedStats.winLoseFor(maingame.m.tournament.currentMatch.player2.toString(), maingame.m.tournament.currentMatch.player1.toString());
					maingame.m.tournament.nextMatch();
					maingame.m.next.setEnabled(true);
				}
				else{
					maingame.m.login.rankedStats.winLoseFor(maingame.m.login.players[1], maingame.m.login.players[0]);
				}
			}
		}
		else if (isBlackTurn && !blackVictory()){
			maingame.m.messages.setText(blackmoves);
		}	

		else if (!isBlackTurn && !redVictory()){
			maingame.m.messages.setText(redmoves);
		}
	}	
}
