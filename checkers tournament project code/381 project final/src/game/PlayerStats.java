package game;
//this class contains everything you need to know about a given player, as well as methods for updating the information
//and calculating the player's ladder score
public class PlayerStats{
	
	String name;
	String pw;
	int wins;
	int losses;
	double ratio;
	int score;
	
	public PlayerStats(String name, String pw){
		this.name = name;
		this.pw = pw;
		wins = 0;
		losses = 0;
		ratio = 0;
		score = 1000;
	}
	
	public PlayerStats(String name, String pw, int wins, int losses, int score){
		this.name = name;
		this.pw = pw;
		this.wins = wins;
		this.losses = losses;
		this.score = score;
		fixRatio();
	}
	
public String toString(){
	return name;
}
	public void addWin(){
		wins++;
		fixRatio();
	}
	public void addLoss(){
		losses++;
		fixRatio();
	}
	public void fixRatio(){
		if(wins+losses != 0){
			ratio = ((double)wins/(double)(wins+losses));
		}
		else{
			ratio = 0.00;
		}
	}
	
	//A player always starts off with a ladder score of 1000 when they first register. They will gain more points
	//by beating a higher ranked player and lose more points for losing to a lower ranked player
	public void adjustScore(boolean win, int opposingScore){
		int scoreDiff;
		if(win){
			scoreDiff = opposingScore - score;
		}
		else {
			scoreDiff = score - opposingScore;
		}
		
		double expectedScore = 1 / (1+ Math.pow(10, scoreDiff/400));
		
		double scoreChange = (32 * (1-expectedScore));
		if(win){
			score += (int)scoreChange;
		}
		else{
			score -= (int)scoreChange;
		}
	}
	
	public void modScore(int change){
		score = score + change;
	}
	public String getName(){
		return name;
	}
	public int getWins(){
		return wins;
	}
	public int getLosses(){
		return losses;
	}
	public double getRatio(){
		return ratio;
	}
	public int getScore(){
		return score;
	}
	//we provide the option of resetting stats, in case one person gets too far ahead, or for any other reason
	public void resetStats(){
		wins = 0;
		losses = 0;
		score = 1000;
		ratio=0;
	}
	public int compareTo(PlayerStats p1){
		if(p1 == null) return -1;
		if(this.getScore() > p1.getScore()) return -1;
		else if(this.getScore() < p1.getScore()) return 1;
		else if(this.getScore() == p1.getScore()){
			if(this.getWins() > p1.getWins()) return -1;
			else if(this.getWins() < p1.getWins()) return 1;
			else if(this.getWins() == p1.getWins()){
				if(this.getRatio() > p1.getRatio()) return -1;
				else if(this.getRatio() < p1.getRatio()) return 1;
				else
					return 0;
			}
		}
		return -2;
	}
}
