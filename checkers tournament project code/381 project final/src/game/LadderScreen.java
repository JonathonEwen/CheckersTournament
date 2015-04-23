package game;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
//this class defines the layout of the ladder screen, as well as the ability to reset the stats and sort the scores
//of users to display them in descending order
public class LadderScreen extends JPanel {

	Charset charset = Charset.forName("US-ASCII");
	Path path = FileSystems.getDefault().getPath(System.getProperty("user.dir"),"src/res/gameinfo");
	PlayerStats toSort[];
	
	public LadderScreen(){
		int size = maingame.m.login.rankedStats.pStats.size();
		toSort = new PlayerStats[size];
		//get all of the users who have scores on the ladder and place them in an array so we can sort them
		for(int i = 0; i < size; i++){
			toSort[i] = maingame.m.login.rankedStats.pStats.get(i);
		}
		
		//create the reset stats button, which will reset all ladder points back to zero
		JPanel display = new JPanel();
		display.setLayout(new GridBagLayout());
		JButton reset = new JButton("reset all stats");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				maingame.m.login.rankedStats.resetStats();
				maingame.m.returnToMenuFromLadder();
				maingame.m.toLadder();
			}
		});
		
		//create the button to return to the main menu
		JButton exit = new JButton("Return to menu");
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				maingame.m.returnToMenuFromLadder();
			}
		});
		
		//define the layout of the screen, including the final form of the sorted list of players
		GridBagConstraints pc = new GridBagConstraints();
		pc.gridx = 0;
		pc.gridy = 0;
		pc.ipadx = 10;
		pc.ipady = 10;
		GridBagConstraints nc = new GridBagConstraints();
		nc.gridwidth = 2;
		nc.gridx = 1;
		nc.gridy = 0;
		nc.ipadx = 10;
		nc.ipady = 10;
		GridBagConstraints wc = new GridBagConstraints();
		wc.gridx = 3;
		wc.gridy = 0;
		wc.ipadx = 10;
		wc.ipady = 10;
		GridBagConstraints lc = new GridBagConstraints();
		lc.gridx = 4;
		lc.gridy = 0;
		lc.ipadx = 10;
		lc.ipady = 10;
		GridBagConstraints rc = new GridBagConstraints();
		rc.gridx = 5;
		rc.gridy = 0;
		rc.ipadx = 10;
		rc.ipady = 10;
		GridBagConstraints sc = new GridBagConstraints();
		sc.gridx = 6;
		sc.gridy = 0;
		sc.ipadx = 10;
		sc.ipady = 10;
		JLabel place = new JLabel("Place");
		JLabel name = new JLabel("Name");
		JLabel wins = new JLabel("Wins");
		JLabel losses = new JLabel("Losses");
		JLabel ratio = new JLabel("W/L Ratio");
		JLabel score = new JLabel("Score");
		display.add(place,pc);
		display.add(name,nc);
		display.add(wins,wc);
		display.add(losses,lc);
		display.add(ratio,rc);
		display.add(score,sc);
		DecimalFormat df = new DecimalFormat("#.##");
		bubbleSort();
		for(int i = 0; i < toSort.length; i++){
			if(toSort[i] != null){
				place = new JLabel(""+(i+1));
				name = new JLabel(toSort[i].getName());
				wins = new JLabel(""+toSort[i].getWins());
				losses = new JLabel(""+toSort[i].getLosses());
				ratio = new JLabel(df.format(toSort[i].getRatio()));
				score = new JLabel(""+toSort[i].getScore());
				pc.gridy++;
				nc.gridy++;
				wc.gridy++;
				lc.gridy++;
				rc.gridy++;
				sc.gridy++;
				display.add(place,pc);
				display.add(name,nc);
				display.add(wins,wc);
				display.add(losses,lc);
				display.add(ratio,rc);
				display.add(score,sc);
			}
		}
		
		//add everything to the screen
		setLayout(new BorderLayout());
		add(display, BorderLayout.CENTER);
		JPanel panel = new JPanel();
		panel.add(exit);
		panel.add(reset);
		add(panel, BorderLayout.SOUTH);
	}

	//simple bubble sort procedure to sort the ladder scores. Bubble sort is justified since we expect
	//the number of users to remain low for all time (or else we should have used a database instead of a
	//simple text file to store users and their data)
	private void bubbleSort() {
		int i;
		boolean swapped = true;
		PlayerStats temp;
		while(swapped){
			swapped = false;
			for(i = 0; i < toSort.length-1; i++){
				if(toSort[i].compareTo(toSort[i+1]) == 1){
					temp = toSort[i];
					toSort[i] = toSort[i+1];
					toSort[i+1] = temp;
					swapped = true;
				}
			}
		}
	}
}
