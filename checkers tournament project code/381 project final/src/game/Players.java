package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.StringTokenizer;
//this class contains information about each player. The class is essentially an arraylist of PlayerStats objects
//with some extra methods to handle adding wins and losses to player's stats and write their stats to the file
public class Players {
	ArrayList<PlayerStats> pStats;
	Charset charset = Charset.forName("US-ASCII");
	Path path = FileSystems.getDefault().getPath(System.getProperty("user.dir"),"src/res/gameinfo");
	
	public Players(){
		pStats = new ArrayList<PlayerStats>();
	}
	
	public void addPlayer(PlayerStats info){

		if(!pStats.contains(info)){
			pStats.add(info);
		}
	}
	
	public void resetStats(){
		for (PlayerStats i : pStats){
			i.resetStats();
			savePlayer(i);
		}
	}
	
	int p,q,r;
	public void winLoseFor(String win, String lose){
		for(p = 0; p < pStats.size(); p++){
			if(win.compareTo(pStats.get(p).name) == 0){
				r=p;
				pStats.get(r).addWin();		
				savePlayer(pStats.get(r));
			}
			if(lose.compareTo(pStats.get(p).name) == 0){
				pStats.get(p).addLoss();
				savePlayer(pStats.get(p));
			}
		}
		for(q = 0; q < pStats.size(); q++){
			if(lose.compareTo(pStats.get(q).name) == 0){
				pStats.get(q).adjustScore(false, pStats.get(r).score);
				savePlayer(pStats.get(q));
			}
			else if(win.compareTo(pStats.get(q).name) == 0){
				pStats.get(r).adjustScore(true, pStats.get(q).score);
				savePlayer(pStats.get(r));
			}
		};
	}
	
	public void savePlayer(PlayerStats toSave){
		try {
			File tmp = File.createTempFile("tmp", "");
			String toWrite = toSave.name+"\t"+toSave.pw+"\t"+toSave.wins+"\t"+toSave.losses+"\t"+toSave.score+"\n";
			BufferedReader reader = Files.newBufferedReader(path, charset);
			BufferedWriter writer = new BufferedWriter(new FileWriter(tmp));
			String temp = null;
			String check = null;
			StringTokenizer token;
			while((temp = reader.readLine()) != null){
				token = new StringTokenizer(temp);
				check = token.nextToken("\t");
				if(check.compareTo(toSave.name) == 0){
					break;
				}
				writer.write(String.format("%s%n",temp));
			}
			writer.write(String.format("%s",toWrite));
			String end;
			while((end = reader.readLine()) != null){
				writer.write(String.format("%s%n",end));
			}
			reader.close();
			writer.close();
			String pathname = path.toString();
			File oldFile = new File(pathname);
			if(oldFile.delete()){
				boolean result = tmp.renameTo(oldFile);
				System.out.println(result);
			} 
		} 
		catch(IOException e){
		}
	}
}
