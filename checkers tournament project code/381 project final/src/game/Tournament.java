package game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
//this class contains the layout of the tournament screen as well as the logic of calculating tournament matches
//and advancing the winners through the bracket
public class Tournament extends JPanel {

	JLabel prompt = new JLabel("How many players will be in this tournament?");
	JRadioButton four = new JRadioButton("4");
	JRadioButton eight = new JRadioButton("8");
	TournamentBracket bracket = new TournamentBracket(false);
	LoginScreen login = maingame.m.login;
	boolean EightPlayers = false;
	boolean winner = false;
	public Match match1 = null;
	public Match match2;
	public Match match3=null;
	public Match match4;
	public Match match5=new Match("","");
	public Match match6=new Match("","");
	public Match match7;
	public Match currentMatch=match1;
	
	public Tournament(){
		login.switchTo(true); //Changes to tournament mode
		this.setLayout(new BorderLayout());
		JPanel top = new JPanel();
		top.setLayout(new FlowLayout());
		top.add(prompt);
		top.add(four);
		four.setSelected(true);
		EightPlayers=false;
		four.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				EightPlayers=false;
				bracket = new TournamentBracket(false);
				eight.setSelected(false);
				if (maingame.m.login.playersLoggedIn >7) maingame.m.login.resetLogin();
				if (maingame.m.login.playersLoggedIn < 4) maingame.m.login.startGame.setEnabled(false);
				if (maingame.m.login.playersLoggedIn >3) maingame.m.login.startGame.setEnabled(true);
			}
		}); 
		top.add(eight);
		add(top, BorderLayout.NORTH);
		eight.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				EightPlayers = true;
				bracket = new TournamentBracket(true);
				four.setSelected(false);
				if (maingame.m.login.playersLoggedIn < 8) maingame.m.login.startGame.setEnabled(false);
				if (maingame.m.login.playersLoggedIn >7) maingame.m.login.startGame.setEnabled(true);
				if (maingame.m.login.playersLoggedIn >3)maingame.m.login.resetrb();
			}
		}); 
		add(login,BorderLayout.CENTER);
		setPreferredSize(new Dimension(400,400));
	}
	public void showBracket(){
		if (match1 == null){setMatches();}
		bracket.player1.setText(""+match1.player1.name);
		bracket.player2.setText(""+match1.player2.name);
		bracket.player3.setText(""+match2.player1.name);
		bracket.player4.setText(""+match2.player2.name);
		if (EightPlayers){//dont show the last 4 players on the bracket if a 4 player tournament has been selected
			bracket.player5.setText(""+match3.player1.name);
			bracket.player6.setText(""+match3.player2.name);
			bracket.player7.setText(""+match4.player1.name);
			bracket.player8.setText(""+match4.player2.name);
		}
		maingame.m.showBracket();
	}
	
	public void nextMatch(){
		setRestOfMatches();
		if (currentMatch == match1) {
			currentMatch = match2;
			bracket.winner1.setText(""+match1.winner.name);
		}
		else if (currentMatch == match2){
			currentMatch = match3;
			bracket.winner2.setText(""+match2.winner.name);
		}
		else if(currentMatch == match3 && !EightPlayers){
			winner = true;
			bracket.winner3.setText(""+match3.winner.name);
		}
		else if(currentMatch == match3 && EightPlayers){
			currentMatch=match4;
			bracket.winner3.setText(""+match3.winner.name);
		}
		else if (currentMatch == match4 && EightPlayers){
			currentMatch = match5;
			bracket.winner4.setText(""+match4.winner.name);
		}
		else if (currentMatch.compareTo(match5)==0 && EightPlayers){
			match5=currentMatch;
			currentMatch = match6;
			bracket.winner5.setText(""+match5.winner.name);
		}
		else if (currentMatch.compareTo(match6)==0 && EightPlayers) {
			match6=currentMatch;
			currentMatch = match7;
			bracket.winner6.setText(""+match6.winner.name);
	
		}
		else if (currentMatch.compareTo(match7)==0 && EightPlayers){
			match7=currentMatch;
			winner = true;
			bracket.winner7.setText(""+match7.winner.name);
		}
	}
	
	public void setMatches(){
		 match1=new Match(maingame.m.login.players[0],maingame.m.login.players[1]);
		 match2=new Match(maingame.m.login.players[2],maingame.m.login.players[3]);
		 if (EightPlayers) {
			 match3= new Match(maingame.m.login.players[4],maingame.m.login.players[5]);
			 match4= new Match(maingame.m.login.players[6],maingame.m.login.players[7]);
		 }
		 currentMatch=match1;
	}
	
	public void setRestOfMatches(){
		 if (!EightPlayers && match3==null && match2.winner!=null) {
			 match3 = new Match(match1.winner, match2.winner);
		 }
		 if (EightPlayers&&match2.winner!=null){	
			if (match5.winner ==null){
				match5= new Match(match1.winner.toString(), match2.winner.toString());
			}
			if (match4.winner != null && match6.winner==null){
				match6= new Match(match3.winner.toString(), match4.winner.toString());
			}
			if (match5.winner !=null && match6.winner !=null){
				match7= new Match(match5.winner.toString(), match6.winner.toString());
			}
		 }
	}
}
		 
	
