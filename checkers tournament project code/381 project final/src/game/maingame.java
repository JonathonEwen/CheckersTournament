package game;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
//this is the driver class for the game. it defines when we should see each screen and has logic for clearing away
//the previous screen. also defines the functionality of navigation buttons
public class maingame {
		JFrame window = new JFrame();
		JLabel messages = new JLabel();
		JLabel players = new JLabel();
		JLabel tournamentMessage;
		static maingame m = new maingame();
		LadderScreen ladder;
		GameBoard gb;
		MainMenu menu = new MainMenu();
		JButton toMenu = new JButton("Return to menu");
		JPanel panel = new JPanel();
		JButton next = new JButton("play next game");
		LoginScreen login = new LoginScreen(false);
		Tournament tournament;
		JPanel tournamentPanel = new JPanel();
		JPanel panel2;

		public void startgame(boolean ranked){
			panel.removeAll();
			gb = new GameBoard(ranked);
			window.remove(menu);
			gb.SetGameBoard();
			messages.setText(gb.blackmoves);
			toMenu.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
					maingame.m.returnToMenu();			
				}
	        });
			GroupLayout layout = new GroupLayout(panel);
			panel.setLayout(layout);	
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			layout.setHorizontalGroup(
					   layout.createParallelGroup(Alignment.CENTER, false)
					      .addComponent(gb.board, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
					      .addComponent(messages)
					      .addComponent(toMenu)					     
					);
			
					layout.setVerticalGroup(
					   layout.createSequentialGroup()
					      .addComponent(gb.board)
					      .addComponent(messages)
					      .addComponent(toMenu)	
					);
			window.setPreferredSize(new Dimension(450,500));
			window.add(panel);
			window.pack();	
		}
		
		
	public void returnToMenu() {
			window.remove(panel);
			window.add(menu);
			window.repaint();
			window.setPreferredSize(new Dimension(400,400));
			window.pack();
	}

	public void startup(){
		window.add(menu);
		window.setTitle("Checkers!");
		window.setPreferredSize(new Dimension(400,400));
		window.setMinimumSize(new Dimension(350,350));
		window.setMaximumSize(new Dimension(500,500));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true); 
	}
	
	public void returnToMenuFromLogin() {
		window.remove(login);
		if(m.login.tournament)window.remove(tournament);
		window.add(menu);
		window.repaint();
		window.setPreferredSize(new Dimension(400,400));
		window.pack();
	}

	public void toLogin(){
		window.remove(menu);
		login.switchTo(false);
		window.add(login);
		window.repaint();
		window.setPreferredSize(new Dimension(400,400));
		window.pack();
	}
	public static void main(String[] args){
		m.startup();
	}

	public void toTournament() {
		tournament = new Tournament();
		window.remove(menu);
		window.add(tournament);
		window.setPreferredSize(new Dimension(400,400));
		window.pack();
		
	}

	public void quitTournament(){
		tournamentPanel.remove(panel2);
			window.remove(tournamentPanel);
			window.add(menu);
			window.repaint();
			window.setPreferredSize(new Dimension(400,400));
			window.pack();
	}
	
	public void showBracket(){
		tournamentPanel.removeAll();
		window.remove(tournament);
		panel2 = tournament.bracket;
		GroupLayout layout = new GroupLayout(tournamentPanel);
		tournamentPanel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		JButton nextGame = new JButton("play next match") ;
		if(m.tournament.winner){
			tournamentMessage = new JLabel("the winner of the tournament is: "+maingame.m.tournament.currentMatch.winner.name);
			nextGame.setEnabled(false);
		}
		else if (!m.tournament.winner){
			tournamentMessage = new JLabel("the next match is: "+maingame.m.tournament.currentMatch);
		}
		nextGame.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
				maingame.m.startTournamentGame();			
			}
        });
		JButton quit = new JButton("quit tournament") ;
		quit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
				maingame.m.quitTournament();	
				maingame.m.login.resetLogin();
			}
        });
		layout.setHorizontalGroup(
				   layout.createParallelGroup(Alignment.LEADING, true)
				      .addComponent(panel2)
				      .addComponent(tournamentMessage)
				      
				      .addGroup(layout.createSequentialGroup()
				    		  .addComponent(quit)
				    		  .addComponent(nextGame))
				);
		
				layout.setVerticalGroup(
				   layout.createSequentialGroup()
				      .addComponent(panel2)
				      .addComponent(tournamentMessage)
				      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				      	.addComponent(quit)	
				      	.addComponent(nextGame))
				);

       
		window.add(tournamentPanel);
		window.setPreferredSize(new Dimension(500,500));
		window.repaint();
		window.pack();
	}

	public void next() {
		window.remove(panel);
		window.remove(tournamentPanel);
		window.repaint();
		m.showBracket();
	}

	public void startTournamentGame(){
		panel.removeAll();
		window.remove(tournamentPanel);
		gb = new GameBoard(true);
		gb.SetGameBoard();
		messages.setText(gb.blackmoves);
		toMenu.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
				maingame.m.returnToMenu();	
				m.login.resetLogin();
			}
        });
		next.setEnabled(false);
		next.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
				maingame.m.next();
				m.login.resetLogin();
			}
        });
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		players.setText("Black Player: "+tournament.currentMatch.player1.name+ " Red Player: "+tournament.currentMatch.player2.name);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(
				   layout.createParallelGroup(Alignment.CENTER, false)
				      .addComponent(gb.board, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				      .addComponent(messages)
				      .addComponent(players)
				      .addGroup(layout.createSequentialGroup()
				      .addComponent(toMenu)		
				      .addComponent(next))
				);
		
				layout.setVerticalGroup(
				   layout.createSequentialGroup()
				      .addComponent(gb.board)
				      .addComponent(messages)
				      .addComponent(players)
				      .addGroup(layout.createParallelGroup(Alignment.CENTER)
				      .addComponent(toMenu)	
				      .addComponent(next))
				);
				
		window.setPreferredSize(new Dimension(450,500));
		window.add(panel);
		window.repaint();
		window.pack();			
	}
	
	public void startranked() {
		panel.removeAll();
		gb = new GameBoard(true);
		window.remove(login);
		gb.SetGameBoard();
		messages.setText(gb.blackmoves);
		toMenu.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
				maingame.m.returnToMenu();		
				maingame.m.login.resetLogin();
			}
        });
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		players.setText("Black Player: "+login.players[0]+ " Red Player: "+login.players[1]);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(
				   layout.createParallelGroup(Alignment.CENTER, false)
				      .addComponent(gb.board, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				      .addComponent(messages)
				      .addComponent(players)
				      .addComponent(toMenu)					     
				);
		
				layout.setVerticalGroup(
				   layout.createSequentialGroup()
				      .addComponent(gb.board)
				      .addComponent(messages)
				      .addComponent(players)
				      .addComponent(toMenu)	
				);

		window.setPreferredSize(new Dimension(450,500));
		window.add(panel);
		window.repaint();
		window.pack();	
		}	
	public void toLadder(){
		/* We make a new screen every time because the thing is updated when its made */
		ladder  = new LadderScreen();
		window.remove(menu);
		window.add(ladder);
		menu.updateUI();
		ladder.updateUI();
		window.pack();
	}

	public void returnToMenuFromLadder() {
		window.remove(ladder);
		window.add(menu);
		window.setPreferredSize(new Dimension(400,400));
		window.repaint();
		window.pack();
		
	}
}


