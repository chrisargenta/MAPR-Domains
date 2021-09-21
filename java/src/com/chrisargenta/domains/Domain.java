package com.chrisargenta.domains;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

import com.chrisargenta.io.FileUtils;

public abstract class Domain {
	
	protected String env="UNKNOWNENV";
	protected String scene="UNKNOWNSCENE";
	protected String template="UNSPECIFIED TEMPLATE";
	protected String domain="UNSPECIFIED DOMAIN";
	protected String path=".";
	protected List<String> agents=null;
	protected int teams=1;
	protected int goals=100;
	protected List<PartialIntrepretation> composition=null;
	protected final String prefixInit="\t\t";
	protected final String postfixInit="\n";
	protected final String prefixGoal="\t\t";
	protected final String postfixGoal="";
	protected final String prefixObj="\t\t";
	protected final String postfixObj="\n";
	protected Random rand=new Random();
	
	public abstract void init();
	
	public abstract String getTemplateFile();

	public abstract String getDomainFile();

	public String generateEnv() throws IOException {
		InputStream u=this.getClass().getResourceAsStream(getTemplateFile());
		template=FileUtils.streamToString(u);
		template=template.replaceAll("###OBJECTSOTHER###", getObjectsOther());
		template=template.replaceAll("###INITOTHER###", getInitOther());
		return template;
	}
	
	public String generateScene(List<PartialIntrepretation> composition) throws IOException {
		setComposition(composition);
		assignGoals(composition);
		String sceneTemplate= template.replaceAll("###PROBLEM###", getProblem());
		sceneTemplate= sceneTemplate.replaceAll("###OBJECTSAGENTS###", getObjectsAgents());
		sceneTemplate= sceneTemplate.replaceAll("###INITAGENTS###", getInitAgents());
		//sceneTemplate= sceneTemplate.replaceAll("###GOAL###",);
		return sceneTemplate;
	}
	
	protected boolean assignGoals(List<PartialIntrepretation> teams){
		// randomly assign a unique goal for each team
		if(teams.size()>goals) return false;
		
		for (PartialIntrepretation t: teams){
			while (t.getGoal()==-1){
				int g=rand.nextInt(goals);
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
	
	public void setComposition(List<PartialIntrepretation> composition){
		this.composition=composition;
	}
	
	// Problem
	private String problem=null;
	private String getProblem(){
		if(problem==null) problem=replaceProblem();
		return problem;
	}
	public abstract String replaceProblem();

	// Object Agents
	private String objectsAgents=null;
	private String getObjectsAgents(){
		if(objectsAgents==null) objectsAgents=replaceObjectsAgents();
		return objectsAgents;
	}
	
	public String replaceObjectsAgents() {
		String out="\n";
		for(String a: agents){
			out+=prefixObj+a+" - Agent"+postfixObj;
		}
		return out;
	}
	
	// Object Other
	private String objectsOther=null;
	private String getObjectsOther(){
		if(objectsOther==null) objectsOther=replaceObjectsOther();
		return objectsOther;
	}
	public abstract String replaceObjectsOther();

	// Init Agents
	private String initAgents=null;
	private String getInitAgents(){
		if(initAgents==null) initAgents=replaceInitAgents();
		return initAgents;
	}
	public abstract String replaceInitAgents();
	
	// Init Other
	private String initOther=null;
	private String getInitOther(){
		if(initOther==null) initOther=replaceInitOther();
		return initOther;
	}
	public abstract String replaceInitOther();
		
	public abstract List<String> getAllGoals();

	public final void setSeed(int seed){
		rand=new Random(seed);
	}
	
	public final void setEnv(String env){
		this.env=env;
		objectsOther=null;
		initOther=null;
	}

	public final void setScene(String scene){
		this.scene=scene;
	}
	
	public final void setScenePath(String path){
		this.path=path;
	}

	public final void setAgents(List<String> agents) {
		this.agents=agents;
		objectsAgents=null;
		initAgents=null;
	}

	public final void setTeamCount(int teams) {
		this.teams=teams;
	}

	public abstract void initGoals(int goals);
	
	public final void generateGoals(int goals) {
		this.goals=goals;
		this.initGoals(goals);
	}

	protected final String loadxxxFileToString(String resourceName) {
		String s=null;
		URL u=null;
		try {
			u=this.getClass().getResource(resourceName);
			s = FileUtils.fileToString(u.getPath());
		} catch (IOException e) {
			System.err.println("Error reading template ("+u+"): "+e);
			System.exit(-1);
		}
		return s;
	}
	
}
