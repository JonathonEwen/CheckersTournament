package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
//this class defines the gamepiece object, which represents the physical idea of a real checkers piece
//it contains logic for assigning the source images for each piece when it is created and when it becomes a king
public class GamePiece {
	String player; //either Red or Black
	BufferedImage img;
	boolean king;
	boolean canJump;
	boolean canBeJumped;
	
	public GamePiece(String player){
		this.player = player;
		king = false;
		canJump = false;
		canBeJumped = false;
		img = null;
		try {
			if(this.player.compareTo("Red") == 0){
				img = ImageIO.read(new FileImageInputStream(new File(System.getProperty("user.dir")+"/src/res/Red.bmp")));
			}
			else {
				img = ImageIO.read(new FileImageInputStream(new File(System.getProperty("user.dir")+"/src/res/Black.bmp")));
			}
		}
		catch (IOException e) {
			System.out.println(e);
		}	
	}
	
	public String getPlayer(){
		return player;
	}
	
	public boolean isKing(){
		return king;
	}
	
	public void kingMe(){
		king = true;
		try{
			if(player.compareTo("Red") == 0){
				img = ImageIO.read(new FileImageInputStream(new File(System.getProperty("user.dir")+"/src/res/Redking.bmp")));
			}
			else
				img = ImageIO.read(new FileImageInputStream(new File(System.getProperty("user.dir")+"/src/res/Blackking.bmp")));
		} 
		catch (IOException e) {
			System.out.println(e);
		}
	}
	
	public BufferedImage getImage(){
		return img;
	}
}
