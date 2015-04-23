package game;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
//defines the layout and event listeners for the buttons on the main menu
public class MainMenu extends JPanel {

    private JButton quickGame;
    private JButton rankedGame;
    private JButton tournament;
    private JButton viewLadder;
    private JLabel titleLabel;
	   
    public MainMenu() {
        initComponents();
    }
   
    private void initComponents() {

        titleLabel = new JLabel("Checkers Tournament", SwingConstants.CENTER);
        titleLabel.setPreferredSize(new Dimension(40,100));
        titleLabel.setFont(new Font("Comic Sans MS", 3, 18));
        quickGame = new JButton();
        quickGame.setPreferredSize(new Dimension(20,50));
        rankedGame = new JButton();
        rankedGame.setPreferredSize(new Dimension(20,50));
        tournament = new JButton();
        tournament.setPreferredSize(new Dimension(20,50));
        viewLadder = new JButton();
        viewLadder.setPreferredSize(new Dimension(20,50));
        quickGame.setText("Start Quick Game");
        quickGame.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		maingame.m.startgame(false);
			}
        });
        rankedGame.setText("Start Ranked Game");
        rankedGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				maingame.m.toLogin();				
			}
		});
        tournament.setText("Start Tournament");
        tournament.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				maingame.m.toTournament();				
			}
		});
        viewLadder.setText("View Ladder");
        viewLadder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				maingame.m.toLadder();				
			}
		});
        setLayout(new GridLayout(0,1));
        add(titleLabel);
        add(quickGame);
        add(rankedGame);
        add(tournament);
        add(viewLadder);
    }
}

      
                   

                   

