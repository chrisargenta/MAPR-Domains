package com.chrisargenta.domains;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TeamFactory{
	private Random rand=null;
	
	public TeamFactory(int seed){
		rand=new Random(seed);
	}
	
	public List<PartialIntrepretation> generateTeams(int agentCount,int goalCount){
		return generateTeams(agentCount,goalCount,1,agentCount);
	}
	
	public List<PartialIntrepretation> generateTeams(int agentCount,int goalCount, int minTeams, int maxTeams){
		int teamCount=rand.nextInt(maxTeams-(minTeams-1))+(minTeams);
		if(teamCount>agentCount)
			teamCount=agentCount;
		List<PartialIntrepretation> teams=new ArrayList<PartialIntrepretation>(teamCount);
		List<Integer> agents=new ArrayList<Integer>(agentCount);
		for(int i=0;i<agentCount;i++){
			agents.add(i);
		}
		Collections.shuffle(agents,rand);
		// Assign at least 1 agent to each team
		for(int i=0;i<teamCount;i++){
			teams.add(new PartialIntrepretation());
			int x=rand.nextInt(agents.size());
			teams.get(i).addAgent(agents.get(x));
			agents.remove(x);
		}
		// Assign the remaining agents
		while(!agents.isEmpty()){
			int t=rand.nextInt(teamCount);
			teams.get(t).addAgent(agents.get(0));
			agents.remove(0);
		}
		return teams;
	}
	
	public List<String> names(int agentCount){
		List<String> r=new ArrayList<String>(agentCount);
		for (int i=0;i<agentCount;i++){
			r.add(name(i));
		}
		return r;
	}
	
	public static String name(int id){
		return "Agent"+id;
	}
	
}
