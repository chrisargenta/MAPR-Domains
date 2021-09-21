package com.chrisargenta.domains.storybook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.chrisargenta.domains.Domain;
import com.chrisargenta.domains.PartialIntrepretation;

public class Storybook extends Domain{
	private final String[] goodlocations={"Town","Garden","Field","Stable","Blacksmith","Cave","Castle","Tower","Forest"};
	private final String[] badlocations={"Cave","Castle","Tower","Forest"};
	//private final String[] monsters={"Witch","Dragon","Tyrant","Brute","Giant"};
	//private final String[] maidens={"Princess","Maiden","Wench","Barmaid","NONE"};
	//private final String[] treasure={"Gold","Ruby","Diamond","Silver","NONE"};
	private final String[] monsters={"Witch","Dragon","Giant"};
	private final String[] maidens={"Princess","Maiden","NONE"};
	private final String[] treasure={"Gold","Ruby","Diamond"};
	private final String[] goalsList={"FindLove","HaveHonor","BeFamous","GetRich","-"};
	
	private List<String> allGoals=new ArrayList<String>();

	@Override
	public String replaceProblem() {
		return "Storybook";
	}

	@Override
	public String replaceObjectsOther() {
		String out=prefixInit;
		for(String s: monsters){
			out+=s+" ";
		}
		out+=" - monster"+postfixInit;
		out+=postfixInit;
		out+=prefixInit;
		for(String s: maidens){
			out+=s+" ";
		}
		out+=" - folk"+postfixInit;
		out+=postfixInit;
		out+=prefixInit;
		for(String s: treasure){
			out+=s+" ";
		}
		out+=" - treasure"+postfixInit;
		return out;
	}

	@Override
	public String replaceInitAgents() {
		String out="";
		for(String a: agents){
			String l=goodlocations[rand.nextInt(goodlocations.length)];
			out+=prefixInit+"(AT "+a+" "+l+")"+postfixInit;
			out+=prefixInit+"(RESTED "+a+" )"+postfixInit;
			out+=prefixInit+"(SINGLE "+a+" )"+postfixInit;
			out+=prefixInit+"(EMPTYHANDED "+a+" )"+postfixInit;
			if(rand.nextBoolean()) out+=prefixInit+"(CANRIDE "+a+" )"+postfixInit;
		}
		return out;
	}

	@Override
	public String replaceInitOther() {
		List<String> maidenList= Arrays.asList(maidens);
		Collections.shuffle(maidenList,rand);
		List<String> treasureList= Arrays.asList(treasure);
		Collections.shuffle(treasureList,rand);
		List<String> monsterList= Arrays.asList(monsters);
		Collections.shuffle(monsterList,rand);
		List<String> locationList=Arrays.asList(badlocations);
		Collections.shuffle(locationList,rand);
		
		String out="";
		for(int i=0;i<treasure.length;i++){
			out+=prefixInit+"(at "+monsterList.get(i)+" "+locationList.get(i)+")"+postfixInit;
			if(!maidenList.get(i).equals("NONE"))
				out+=prefixInit+"(imprisoned "+maidenList.get(i)+" "+monsterList.get(i)+")"+postfixInit;
			else 
				out+=prefixInit+"(notorious "+monsterList.get(i)+")"+postfixInit;
			if(!treasureList.get(i).equals("NONE"))
				out+=prefixInit+"(guarding "+monsterList.get(i)+" "+treasureList.get(i)+")"+postfixInit;
			
		}
		return out;
	}

	@Override
	protected boolean assignGoals(List<PartialIntrepretation> teams){
		// randomly assign a unique goal for each team
		if(teams.size()>goals) return false;
		
		for (PartialIntrepretation t: teams){
			while (t.getGoal()==-1){
				int g=rand.nextInt(allGoals.size());
				for (PartialIntrepretation other: teams){
					if (other==t){
						t.setGoal(g);
						break;
					}
					if(other.getGoal()==g){
						break;
					}
				}
			}
		}
		return true;
	}
	
	List<String> goalPatterns=new ArrayList<String>();
		
	protected void generateGoalPatterns(){
		for(int i=0;i<goalsList.length;i++){
			for (int j=i+1;j<goalsList.length;j++){
				for (int k=j+1;k<goalsList.length;k++){
					String g= toGoal(i)+" "+toGoal(j)+" "+toGoal(k);
					goalPatterns.add(g);
				}
			}
		}
	}
	
	private String toGoal(int i){
		if(goalsList[i].equals("-")) return "";
		return "("+goalsList[i]+")";
	}

	@Override
	public List<String> getAllGoals() {
		return allGoals;
	}

	@Override
	public void init(){
		
	}
	
	@Override
	public void initGoals(int goalCount) {
		allGoals=new ArrayList<String>();
		for(int i=0;i<goalsList.length;i++){
			for (int j=i+1;j<goalsList.length;j++){
				for (int k=j+1;k<goalsList.length;k++){
					String g= toGoal(i)+" "+toGoal(j)+" "+toGoal(k);
					allGoals.add(g);
				}
			}
		}		
		Collections.sort(allGoals);
	}
	

	@Override
	public String getTemplateFile() {
		return "/com/chrisargenta/domains/storybook/Storybook_template.pddl";
	}
	
	@Override
	public String getDomainFile() {
		return "/com/chrisargenta/domains/storybook/Storybook_domain.pddl";
	}

}
