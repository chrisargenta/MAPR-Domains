package com.chrisargenta.domains.teamblocks;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.chrisargenta.domains.Domain;
import com.chrisargenta.domains.PartialIntrepretation;
import com.chrisargenta.io.FileUtils;
import com.chrisargenta.io.ListUtils;

public class TeamBlocks extends Domain{
	private String alphabet="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public List<String> words=null;
	public List<List<Character>> stacks=null;
	public Hashtable<Character,List<Character>> stackLookup=new Hashtable<Character,List<Character>>(alphabet.length());
	private List<String> allGoals=new ArrayList<String>();
	private boolean realWords=false;
	
	public TeamBlocks(){
	}

	@Override
	public String replaceProblem() {
		return "TeamBlocks";
	}

	@Override
	public String replaceObjectsOther() {
		String out="\n";
		for(int i=0;i<alphabet.length();i++){
			out+=prefixObj+alphabet.charAt(i)+" - Block"+postfixObj;
		}
		return out;
	}

	@Override
	public String replaceInitAgents() {
		String out="\n";
		for(String a: agents){
			out+=prefixInit+"(HANDEMPTY "+a+")"+postfixInit;
		}
		return out;
	}

	@Override
	public String replaceInitOther() {
		if(stacks==null) buildBlockStacks();
		return describeBlockStacks();
	}

	@Override
	public List<String> getAllGoals() {
		ArrayList<String> fin=new ArrayList<String>(allGoals.size());
		for (String s:allGoals){
			fin.add(wordToGoal(s));
		}
		return fin;
	}
	
	@Override
	protected boolean assignGoals(List<PartialIntrepretation> teams){
		// randomly assign a unique goal for each team
		if(teams.size()>goals) return false;
		
		int trying=0;
		for (int i=0;i<teams.size();i++){
			teams.get(i).setGoal(-1);
			while (teams.get(i).getGoal()==-1){
				int test=rand.nextInt(allGoals.size());
				boolean accept=true;
				String testWord=allGoals.get(test);
				for(int j=0;j<i;j++){
					String existingWord=allGoals.get(teams.get(j).getGoal());
					if(!uniqueStacks(existingWord,testWord)){
						accept=false;
						break;
					}
				}
				if(accept) teams.get(i).setGoal(test);				
				else if(trying++>10000) {
					i=-1; // if we are trying too much (used up vowels) reset
					trying=0;
					break;
				}
			}
		}
		return true;
	}

	private String wordToGoal(String in){
		String word=in.toUpperCase();
		String goal=prefixGoal+"(AND (ONTABLE "+word.charAt(0)+") ";
		for (int n=1;n<word.length();n++){
			goal+="(ON "+word.charAt(n)+" "+word.charAt(n-1)+") ";
		}
		goal+="(CLEAR "+word.charAt(word.length()-1)+"))"+postfixGoal;
		return goal;
	}

	@Override
	public String getTemplateFile() {
		return "/com/chrisargenta/domains/teamblocks/TeamBlocks_template.pddl";
	}
	
	@Override
	public String getDomainFile() {
		return "/com/chrisargenta/domains/teamblocks/TeamBlocks_domain.pddl";
	}

	@Override
	public void init() {
		alphabet="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	}
	
	@Override
	public void initGoals(int goalCount){
		loadWords();
		fillUniqueWords(goalCount,allGoals);
		Collections.sort(allGoals);
		HashSet<Character> letters=new HashSet<Character>();
		for(String g:allGoals){
			for (int i=0;i<g.length();i++){
				letters.add(g.charAt(i));
			}
		}
		alphabet="";
		for(Character c:letters){
			alphabet+=c;
		}
	}
	
	private void fillUniqueWords(int count, List<String> list){
		while(list.size()<count){
			int i=rand.nextInt(words.size());
			if(!list.contains(words.get(i)))
				list.add(words.get(i));
		}
	}
	
	private boolean uniqueLetters(String word1, String word2) {
		word1=word1.toLowerCase();
		word2=word2.toLowerCase();
		Map<Character,Boolean> c= new HashMap<Character,Boolean>();
		for(int i=0;i<word1.length();i++){
			c.put(word1.charAt(i), new Boolean(true));
		}
		for(int i=0;i<word2.length();i++){
			if(c.containsKey(word2.charAt(i))) return false;
		}
		return true;
	}
	
	private boolean uniqueStacks(String word1, String word2) {
		
		if(!uniqueLetters(word1, word2)) return false;
		
		word1=word1.toUpperCase();
		word2=word2.toUpperCase();
		Map<Character,Boolean> c= new HashMap<Character,Boolean>();
		for(int i=0;i<word1.length();i++){
			List<Character> col=stackLookup.get(word1.charAt(i));
			for(Character x: col)
				c.put(x, new Boolean(true));
		}
		for(int i=0;i<word2.length();i++){
			if(c.containsKey(word2.charAt(i))) return false;
		}
		return true;
	}

	private void loadWords(){
		if (realWords){
			if (words==null){
				words=new ArrayList<String>();
				try {
					InputStream u=this.getClass().getResourceAsStream("/com/chrisargenta/domains/teamblocks/TeamBlocks_isograms.txt");
					ListUtils.readList(u, words);
				} catch (IOException e) {
					System.err.println("Error reading Team Blocks isograms: "+e);
					System.exit(-1);
				}
			}
		} else {
			IsogramGenerator ig=new IsogramGenerator(alphabet);
			ig.setSeed(rand.nextInt());
			words=ig.generate(3,5);
		}
	}
	
	private void buildBlockStacks(){
		
		int cols=(int) Math.ceil(alphabet.length()/1.5);
		stacks=new ArrayList<List<Character>>(cols);
		
		for (int i=0;i<cols;i++){
			List<Character> col=new ArrayList<Character>();
			stacks.add(col);
		}
		
		stackLookup=new Hashtable<Character,List<Character>>(alphabet.length());
		for(int i=0;i<alphabet.length();i++){
			List<Character> col=stacks.get(rand.nextInt(cols));
			col.add(alphabet.charAt(i));
			stackLookup.put(alphabet.toUpperCase().charAt(i),col);
		}
	
		for (List<Character> col: stacks){
			Collections.shuffle(col,rand);
		}
		Collections.shuffle(stacks,rand);
		try {
			FileUtils.stringToFile(path+"/blocks.txt",dumpBlockStacks());
		} catch (IOException e) {
			System.err.println("Error writing out blocks.txt file to "+path);
		}
	}

	
	private String describeBlockStacks(){
		String out="\n";
		for (List<Character> col: stacks){
			for (int i=0;i<col.size();i++){
				if(i==0){
					out+=prefixInit+"(ONTABLE "+col.get(i)+" )"+postfixInit;
				} else {
					out+=prefixInit+"(ON "+col.get(i)+" "+col.get(i-1)+" )"+postfixInit;
				}
				if(i==col.size()-1){
					out+=prefixInit+"(CLEAR "+col.get(i)+" )"+postfixInit;
				}
				out+=prefixInit+"(ISFREE "+col.get(i)+" )"+postfixInit;
				
			}
		}
		return out;
	}
	
	private String dumpBlockStacks(){
		String out="# Initial Stack ("+env+"/"+scene+"):\n";
		for (List<Character> col: stacks){
			for (int i=0;i<col.size();i++){
				out+=(col.get(i)+" ");
			}
			out+="\n";
		}
		return out;
	}
}
