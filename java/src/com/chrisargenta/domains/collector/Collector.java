package com.chrisargenta.domains.collector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import com.chrisargenta.domains.Domain;
import com.chrisargenta.domains.PartialIntrepretation;

public class Collector extends Domain {
	public final String template="collector_template.pddl";
	public final String domain="collector_domain.pddl";
	private final String[] locations={"Barcelona","Paris","Brussels","Milan","Hamburg",
			"Munich","Berlin","Copenhagen","Vienna","Warsaw","Belgrade","Bucharest",
			"Minsk","Kiev"};
	private final String[] rawStuff={"Gold","Stamp","Jewel","Painting"};
	
	//BIGGER VERSION
	//private final String[] locations={"Madrid","Barcelona","Paris","London","Brussels","Milan","Hamburg",
	//		"Munich","Berlin","Copenhagen","Stockholm","Rome","Vienna","Warsaw","Belgrade","Bucharest",
	//		"Minsk","Kiev","StPetersburg","Moscow"};
	//private final String[] rawStuff={"Gold","Stamp","Jewel","Painting","Secret","Coin","Sculpture","Automobile","Diamond","Artifact"};
	
	private int stuffCount=4;
	private List<String> stuff=null;
	
	private List<CollectionGoal> allGoals=new ArrayList<CollectionGoal>();
	
	private class CollectionGoal{
		public int location=-1;
		public int stuff=-1;
		public int team=-1;
		public CollectionGoal(int team, int location, int stuff){
			this.team=team;
			this.location=location;
			this.stuff=stuff;
		}
		public boolean conflicts(CollectionGoal other){
			if (this.location==other.location) return true;
			if (this.stuff==other.stuff) return true;
			if (this.team==other.team) return true;
			return false;
		}
		public boolean equals(int l, int s){
			if (this.location!=l) return false;
			if (this.stuff!=s) return false;
			return true;
		}
	}
	
	
	public Collector(){
		stuff=new ArrayList<String>(rawStuff.length*stuffCount);
		for(String s: rawStuff){
			for (int i=0;i<stuffCount;i++){
				stuff.add(s+"_"+i);
			}
		}
	}
	
	@Override
	public String replaceProblem() {
		return "Collector";
	}
	
	@Override
	public String replaceObjectsOther() {
		String out=prefixInit;
		for(String s: stuff){
			out+=s+" ";
		}
		out+="- stuff"+postfixInit;
		return out;
	}
	
	@Override
	public String replaceInitOther() {
		String initOther="";
		for(String s: stuff){
			int i=rand.nextInt(locations.length);
			initOther+=prefixInit+"(location "+s+" "+locations[i]+")"+postfixInit;
		}
		return initOther;
	}

	@Override
	public void initGoals(int goals){
		allGoals=new ArrayList<CollectionGoal>();
		int max=locations.length*rawStuff.length;
		if (goals>max){
			System.err.println("Collector generation required more goals than feasible, truncating list at max.");
			goals=max;
		}
		// generate all of the possible goals
		List<CollectionGoal> possible=new ArrayList<CollectionGoal>(max);
		for (int l=0;l<locations.length;l++){
			for(int s=0;s<rawStuff.length;s++){				
				possible.add(new CollectionGoal(-1,l,s));
			}
		}
		Collections.shuffle(possible);
		for (int i=0;i<goals;i++){
			allGoals.add(possible.get(i));
		}
	}
	
	public String GoalToPddl(CollectionGoal cg){
		String g=""; //"(AND";
		for(String stuff: generateStuff(rawStuff[cg.stuff],stuffCount)){
			g+=" (LOCATION "+stuff+" "+locations[cg.location]+")";
		}
		return g.trim();
	}
	
	@Override
	public List<String> getAllGoals() {
		ArrayList<String> out=new ArrayList<String>(allGoals.size());
		for(CollectionGoal cg:allGoals){
			out.add(GoalToPddl(cg));
		}
		return out;
	}

	@Override
	protected boolean assignGoals(List<PartialIntrepretation> teamSet){
		Hashtable<Integer,CollectionGoal> assignedByLoc=new Hashtable<Integer,CollectionGoal>(teams);
		Hashtable<Integer,CollectionGoal> assignedByStuff=new Hashtable<Integer,CollectionGoal>(teams);

		// assign a unique combination of location and stuff as goals to each team
		for (int i=0;i<teams;i++){
			CollectionGoal cg=null;
			while(cg==null){
				int pgi=rand.nextInt(allGoals.size());
				CollectionGoal pg=allGoals.get(pgi);
				if(pg.team==-1 && !assignedByLoc.containsKey(pg.location) && !assignedByStuff.containsKey(pg.stuff)){
					teamSet.get(i).setGoal(pgi);
					pg.team=i;
					assignedByLoc.put(pg.location, pg);
					assignedByStuff.put(pg.stuff, pg);
					cg=allGoals.get(pgi);
				}
			}
		}
		return true;
	}
	
	public List<String> generateStuff(String stuff, int count){
		ArrayList<String> allStuff=new ArrayList<String>(stuffCount);
		for (int i=0;i<count;i++){
			allStuff.add(stuff+"_"+i);
		}
		return allStuff;
	}
	
	@Override
	public void init() {
		for(CollectionGoal cg: allGoals){
			cg.team=-1;
		}
	}

	@Override
	public String getTemplateFile() {
		return "/com/chrisargenta/domains/collector/Collector_template.pddl";
	}
	
	@Override
	public String getDomainFile() {
		return "/com/chrisargenta/domains/collector/Collector_domain.pddl";
	}

	@Override
	public String replaceInitAgents() {
		String out="";
		for(String a: agents){
			String l=locations[rand.nextInt(locations.length)];
			out+=prefixInit+"(AT "+a+" "+l+")"+postfixInit;
		}
		return out;
	}

}
