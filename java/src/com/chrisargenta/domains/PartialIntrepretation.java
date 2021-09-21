package com.chrisargenta.domains;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class PartialIntrepretation {
	private List<Integer> composition=null;
	private int goal=-1;
	
	public PartialIntrepretation(){
		composition=new ArrayList<Integer>();
	}
	
	public PartialIntrepretation(List<Integer> composition, int goal){
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
	
	public static PartialIntrepretation parse(String text){
		String[] temp=text.trim().split("[\\(\\)]+");
		if (temp.length!=1){
			System.err.println("Error parsing Partial Intrepretation '"+text+"' found "+temp.length+"potential partials.");
		}
		String[] s=temp[0].split("\\:");
		String[] teamsText= s[0].split("\\+");
		ArrayList<Integer> teams= new ArrayList<Integer>(teamsText.length);
		for(int i=0;i<teamsText.length;i++){
			teams.add(Integer.parseInt(teamsText[i]));
		}
		return new PartialIntrepretation(teams,Integer.parseInt(s[1]));
		
	}
	
	public static List<PartialIntrepretation> parseAll(String text){
		String[] partials=text.trim().split("[\\(\\)]+");
		List<PartialIntrepretation> out= new ArrayList<PartialIntrepretation>(partials.length);
		for(int i=0;i<partials.length;i++){
			if(partials[i].length()>=3)
				out.add(parse(partials[i]));
		}
		return out;
	}

	public boolean hasAgent(int c) {
		for (int a: composition){
			if(a==c) return true;
		}
		return false;
	}
}
