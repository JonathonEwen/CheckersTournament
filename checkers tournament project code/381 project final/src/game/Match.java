package game;
//this class defines the idea of a match between two players. A match consists of two players and might 
//have a winner depending on if the match has been played. 
public class Match implements Comparable<Match> {

	PlayerStats player1;
	PlayerStats player2;
	PlayerStats winner = null;
	public Match(PlayerStats Player1, PlayerStats Player2){
		player1 = Player1;
		player2 = Player2;
	}
	public Match(String Player1, String Player2){
		player1 = new PlayerStats(Player1,"");
		player2 = new PlayerStats(Player2,"");
	}

	public String toString(){
		return ""+player1.name+" vs "+player2.name;
	}
	public void blackWins() {
		winner = player1;
	}

	public void redWins() {
		winner = player2;
	}

	//a match is the same if it has the same players
	public int compareTo(Match m){
		if (this.player1.toString().compareTo(m.player1.toString()) == 0 &&	this.player2.toString().compareTo(m.player2.toString()) == 0){
			return 0;
		}
		else return -1;		
	}
}
