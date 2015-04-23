package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringTokenizer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
//class to define the layout and functionality of the login screen, including all of the logic for 
//writing player information to the file
public class LoginScreen extends JPanel{

	boolean tournament;
	String[] players;
	Players rankedStats = new Players();
	JRadioButton rb[];
	int radioIndex;
	int count;
	JPanel panel;
	ButtonGroup people;
	String password = null;
	int playersLoggedIn = 0;
	JButton startGame;
	Charset charset = Charset.forName("US-ASCII");
	Path path = FileSystems.getDefault().getPath(System.getProperty("user.dir"),"src/res/gameinfo");
	
	//we will have some different functionality depending on whether we are logging in for a tournament or 
	//just a regular ranked game. For this reason, the constructor takes just one argument about whether this is a tournament
	public LoginScreen(boolean tourny){
		tournament = tourny;
		players = new String[100];
		//getCount();
		setLayout(new BorderLayout());
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		//list all known players so returning players can click their name to log in
		populateKnownPlayers();
		//define layout
		JLabel message = new JLabel("Select which players will play this match or create a new player.");
		JLabel message2 = new JLabel("Selected players are highlighted. Click again to de-select a player.");
		JLabel message3 = new JLabel("You will be able to select only the maximum number of players.");
		add(panel, BorderLayout.CENTER);
		JPanel panel2 = new JPanel();
		GridLayout layout = new GridLayout(0,1);
		panel2.setLayout(layout);
		panel2.add(message);
		panel2.add(message2);
		panel2.add(message3);
		add(panel2, BorderLayout.NORTH);
		//define new player button
		JButton newPlayer = new JButton("New Player");
		newPlayer.setPreferredSize(new Dimension(75,50));
		newPlayer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				makeNewPlayer();
			}
		});
		//define start game button
		startGame = new JButton("Start Game");
		startGame.setPreferredSize(new Dimension(75,50));
		startGame.setEnabled(false);
		startGame.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(tournament){
					maingame.m.tournament.showBracket();
				}
				else{
					maingame.m.startranked();
				}
			}
		});
		//define cancel button
		JButton cancel = new JButton("Cancel");
		cancel.setPreferredSize(new Dimension(75,50));
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				resetLogin();
				maingame.m.returnToMenuFromLogin();
			}
		});
		//add everything to screen
		JPanel bottom = new JPanel();
		bottom.setLayout(new GridLayout());
		bottom.add(newPlayer);
		bottom.add(startGame);
		bottom.add(cancel);
		add(bottom, BorderLayout.SOUTH);
	}
	
	public void switchTo(Boolean mode){
		tournament = mode;
		/* Check to see if we need to turn a button off after swaping modes */
		if(playersLoggedIn < 2 && !tournament){
			startGame.setEnabled(false);
		}
		else if(playersLoggedIn < 4 && tournament){
			startGame.setEnabled(false);
		}
	}
	
	/*
	private void getCount() {
		try(BufferedReader reader = Files.newBufferedReader(path, charset)){
			String temp = null;
			count = 0;
			while((temp = reader.readLine()) != null){
				count++;
			}
			reader.close();
			count++;
			playerStats = new PlayerStats[count];
		}
		catch(IOException x){
			System.err.format("IOException: %s%n", x);
		} 
		
	} */
	
	protected void resetLogin() {
		players = new String[100];
		playersLoggedIn = 0;
		for(int i = 0; i < radioIndex; i++){
			rb[i].setBackground(panel.getBackground());
			rb[i].setName("LoggedOff");
			rb[i].setEnabled(true);
		}	
	}
	
	protected void resetrb() {
		for (JRadioButton r : rb){
			if (r != null) r.setEnabled(true);
			updateUI();
		}
	}
	
	//function to handle all aspects of creating a new player, including updating the UI and writing to the file,
	//as well as creating pop ups to accept new player information
	void makeNewPlayer(){
		final JFrame window = new JFrame("New Player");
		final JPanel panel = new JPanel();
		final JTextField name = new JTextField(12);
		final JPasswordField password = new JPasswordField(12);
		JLabel nameL = new JLabel("Name:");
		JLabel passL = new JLabel("Password:");
		JButton accept = new JButton("Accept");
		accept.addActionListener(new ActionListener(){
			Boolean okToWrite = true;
			Boolean uniqueName =true;
			public void actionPerformed(ActionEvent e){
				for (JRadioButton r : rb){
					if(r != null){
						if (name.getText().compareTo(r.getText()) == 0){
							JOptionPane.showMessageDialog(null,"Enter a unique name","Ops!",JOptionPane.WARNING_MESSAGE);
							uniqueName =false;
						}
						else uniqueName = true;
					}
				}
				if(name.getText().compareTo("") == 0){
					JOptionPane.showMessageDialog(null,"Enter your name","Ops!",JOptionPane.WARNING_MESSAGE);				
				}
				
				else if(password.getText().compareTo("") == 0){
					JOptionPane.showMessageDialog(null,"Enter your password","Ops",JOptionPane.WARNING_MESSAGE);
					okToWrite=false;		
				}
				else if (password.getText().compareTo("") != 0){
					okToWrite=true;
				}
				//everything is OK
				if(uniqueName && okToWrite){
					WriteNewToFile(name.getText(), String.copyValueOf(password.getPassword()));
					window.remove(panel);
					window.dispose();
				}
			}
		});
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				window.remove(panel);
				window.dispose();
				
			}
		});
		panel.setLayout(new GridLayout(2,3));
		panel.add(passL);
		panel.add(password);
		panel.add(cancel);
		panel.add(nameL,0);
		panel.add(name,1);
		panel.add(accept,2);
		
		window.add(panel);
		window.pack();
		window.show();
	}
	
	//helper function to handle all logic of writing to file
	protected void WriteNewToFile(String name, String pass) {
		//Write to file with names
		String temp = name+"\t"+pass+"\t"+"0\t0\t1000";
		try{
		FileWriter fw = new FileWriter(System.getProperty("user.dir")+"/src/res/gameinfo", true);
		BufferedWriter writer = new BufferedWriter(fw);
			
			writer.append(temp,0,temp.length());
			writer.newLine();
			writer.flush();
			writer.close();
		}
		catch(IOException x){
			System.err.format("IOException: %s%n", x);
		}
		
		rankedStats.addPlayer(new PlayerStats(name, pass));
		
		rb[radioIndex] = new JRadioButton(name);
		people.add(rb[radioIndex]);
		rb[radioIndex].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				
				checkPassword();
			}
		});
		rb[radioIndex].setName(pass);
		panel.add(rb[radioIndex]);
		radioIndex++;
		this.updateUI();
		
	}
	
	//helper function to check if the entered password matches the saved one
	private void checkPassword(){
		int i = 0;
		while(!rb[i].isSelected()){
			i++;
		}
		if(rb[i].getName().compareTo("LoggedIn") == 0){
			rb[i].setBackground(startGame.getBackground());
			int j = 0;
			while(players[j].compareTo(rb[i].getText()) != 0){
				j++;
			}
			players[j] = "";
			playersLoggedIn--;
			if(playersLoggedIn < 2 && !tournament){
				startGame.setEnabled(false);
			}
			else if(playersLoggedIn < 4 && tournament){
				startGame.setEnabled(false);
			}
			rb[i].setName("LoggedOff");
			this.updateUI();
			
		}
		else {
			JPasswordField pf = new JPasswordField();
			int getOption = JOptionPane.showConfirmDialog(null, pf,"Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if(getOption == JOptionPane.OK_OPTION){
				password = String.copyValueOf(pf.getPassword());
			}
			if(password != null){
				if(rb[i].getName().compareTo("LoggedOff") == 0){
					try(BufferedReader reader = Files.newBufferedReader(path, charset)){
						String temp = null;
						String notNeededNow;
						StringTokenizer token;
						while((temp = reader.readLine()) != null){
							token = new StringTokenizer(temp);
							notNeededNow = token.nextToken("\t");
							if(notNeededNow.compareTo(rb[i].getText()) == 0){
								rb[i].setName(token.nextToken("\t"));
								break;
							}
							else{
								notNeededNow = token.nextToken("\n");
							}
						}
						reader.close();
					}
					catch(IOException x){
					}
				}
				//if the password is wrong...
				if(rb[i].getName().compareTo(password) != 0){
					JOptionPane.showMessageDialog(null, "Incorrect Password!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				//otherwise the password was correct so log them in. make sure we disable the ability for more players
				//to log in once the maximum number of players for the match or tournament is reached
				else{
					players[playersLoggedIn] = rb[i].getText();
					playersLoggedIn++;
					rb[i].setBackground(new Color(200,200,200));
					rb[i].setSelected(false);
					if(playersLoggedIn > 1 && !tournament){
						startGame.setEnabled(true);
						for (JRadioButton r : rb){
							if (r != null) r.setEnabled(false);
						}
					}
					if(playersLoggedIn > 3 && tournament && !maingame.m.tournament.EightPlayers){
						startGame.setEnabled(true);
						for (JRadioButton r : rb){
							if (r != null) r.setEnabled(false);
						}
					}
					if(playersLoggedIn > 7 && tournament && maingame.m.tournament.EightPlayers){
						startGame.setEnabled(true);
						for (JRadioButton r : rb){
							if (r != null) r.setEnabled(false);
						}
					}
					rb[i].setName("LoggedIn");
					this.updateUI();
				}
				password = null;
			}
		}
	}
	
	//helper function to handle the task of reading the data about past players and displaying their info to the UI
	public void populateKnownPlayers(){
		rb = new JRadioButton[100];
		people = new ButtonGroup();
		int i = 0;
		try(BufferedReader reader = Files.newBufferedReader(path, charset)){
			String temp = null;
			String name = null;
			String pass = null;
			int wins, losses, score;
			StringTokenizer token;
			while((temp = reader.readLine()) != null){
				token = new StringTokenizer(temp);
				name = token.nextToken("\t");
				pass = token.nextToken("\t");
				wins = Integer.parseInt(token.nextToken("\t"));
				losses = Integer.parseInt(token.nextToken("\t"));
				score = Integer.parseInt(token.nextToken("\t"));
				rankedStats.addPlayer(new PlayerStats(name, pass,wins,losses,score));
				rb[i] = new JRadioButton(name);
				/* The actual name of the button is its Text field, so this name is hidden, so we will be sneaky
				 * and store each players password into a field not seen by the gui so we can access it without 
				 * reading the file again
				 */
				rb[i].setName(pass);
				people.add(rb[i]);
				rb[i].addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						checkPassword();
					}
				}); 
				panel.add(rb[i]);
				i++;
			}
			reader.close();
			radioIndex = i;
		}
		catch(IOException x){
			System.err.format("IOException: %s%n", x);
		} 
	}
}
