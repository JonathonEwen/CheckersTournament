package game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.JLabel;
import javax.swing.JPanel;
//this class defines the layout of the tournament bracket screen as well as the logic of where to place
//the names of each player in the bracket after they have won a match
public class TournamentBracket extends JPanel {
	boolean EightPlayers;
	BufferedImage bracket;
	public JLabel player1= new JLabel();;
	JLabel player2 = new JLabel();
	JLabel player3 = new JLabel();
	JLabel player4 = new JLabel();
	JLabel player5 = new JLabel("player 5");
	JLabel player6 = new JLabel("player 6");
	JLabel player7 = new JLabel("player 7");
	JLabel player8 = new JLabel("player 8");
	
	JLabel winner1 = new JLabel("winner 1");
	JLabel winner2 = new JLabel("winner 2");
	JLabel winner3 = new JLabel("winner 3");
	JLabel winner4 = new JLabel("winner 4");
	JLabel winner5 = new JLabel("winner 5");
	JLabel winner6 = new JLabel("winner 6");
	JLabel winner7 = new JLabel("winner 7");
	
	Insets insets = this.getInsets();
	Dimension size = (new Dimension(100,20));
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bracket, 0, 0, null);           
    }
    
	public TournamentBracket (boolean eightPlayers){
		EightPlayers = eightPlayers;
		bracket = null;
		try {	
			bracket = ImageIO.read(new FileImageInputStream(new File(System.getProperty("user.dir")+"/src/res/bracket.bmp")));
		} 
		catch (IOException e) {
			System.out.println(e);
		}
		setLayout(null);
	    if (EightPlayers){
		    add(player1);
		    add(player2);
		    add(player3);
		    add(player4);
		    add(player5);
		    add(player6);
		    add(player7);
		    add(player8);
		    
		    add(winner1);
		    add(winner2);
		    add(winner3);
		    add(winner4);
		    add(winner5);
		    add(winner6);
		    add(winner7);
		    
		    //we define the locations of names on the bracket manually
		    player1.setBounds(25 + insets.left, 20 + insets.top,
		                 size.width, size.height);
		    player2.setBounds(25 + insets.left, 45 + insets.top,
		                 size.width, size.height);
		    player3.setBounds(25 + insets.left, 80 + insets.top,
		                 size.width, size.height);
		    player4.setBounds(25 + insets.left, 110 + insets.top,
		            size.width, size.height);
		    player5.setBounds(25 + insets.left, 140 + insets.top,
		                 size.width, size.height);
		    player6.setBounds(25 + insets.left, 165 + insets.top,
		                 size.width, size.height);
		    player7.setBounds(25 + insets.left, 202 + insets.top,
		                 size.width, size.height);
		    player8.setBounds(25 + insets.left, 230 + insets.top,
		            size.width, size.height);
		    
		    winner1.setBounds(150 + insets.left, 32 + insets.top,
		            size.width, size.height);
		    winner2.setBounds(150 + insets.left, 100 + insets.top,
		            size.width, size.height);
		    winner3.setBounds(150 + insets.left, 152 + insets.top,
		            size.width, size.height);
		    winner4.setBounds(150 + insets.left, 220 + insets.top,
		       size.width, size.height);
		    winner5.setBounds(240 + insets.left, 62 + insets.top,
		        size.width, size.height);
		    winner6.setBounds(240 + insets.left, 190 + insets.top,
		        size.width, size.height);
		    winner7.setBounds(347 + insets.left, 127 + insets.top,
		        size.width, size.height);
		}
		//there are 4 players
		else{
		    add(player1);
		    add(player2);
		    add(player3);
		    add(player4);
		    
		    add(winner1);
		    add(winner2);
		    add(winner3);
		    
		    player1.setBounds(150 + insets.left, 32 + insets.top,
		                 size.width, size.height);
		    player2.setBounds(150 + insets.left, 100 + insets.top,
		                 size.width, size.height);
		    player3.setBounds(150 + insets.left, 152 + insets.top,
		                 size.width, size.height);
		    player4.setBounds(150 + insets.left, 220 + insets.top,
		            size.width, size.height);
		    
		    winner1.setBounds(240 + insets.left, 62 + insets.top,
	                 size.width, size.height);
		    winner2.setBounds(240 + insets.left, 190 + insets.top,
	                 size.width, size.height);
		    winner3.setBounds(347 + insets.left, 127 + insets.top,
	                 size.width, size.height);
		}
	}
}