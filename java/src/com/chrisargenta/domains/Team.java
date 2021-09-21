package com.chrisargenta.domains;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Team {
	private List<Integer> composition=null;
	private int goal=-1;
	
	public Team(){
		composition=new ArrayList<Integer>();
	}
	
	public Team(List<Integer> composition, int goal){
		this.composition=new ArrayList<Integer>(composition.size());
		this.composition.addAll(composition);
		this.goal=goal;
	}
	
	public void addAgent(int id){
		composition.add(id);
		Collections.sort(composition);
	}
	
	public List<Integer> getAgents(){
		return composition;
	}
	
	public void setGoal(int goal){
		this.goal=goal;
	}
	
	public int getGoal(){
		return goal;
	}
	
	public String toString(){
		String out="(";
		for(int i=0;i<composition.size();i++){
			out+=composition.get(i);
			if(i+1!=composition.size()){
				out+="+";
			}
		}
		out+=":"+goal+")";
		return out;
	}
}
