package com.chrisargenta.domains;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import com.chrisargenta.domains.collector.Collector;
import com.chrisargenta.domains.storybook.Storybook;
import com.chrisargenta.domains.teamblocks.TeamBlocks;
import com.chrisargenta.io.AnnotatedList;
import com.chrisargenta.io.DirUtils;
import com.chrisargenta.io.FileUtils;
import com.chrisargenta.io.ListUtils;
import com.chrisargenta.utils.GuidUtils;
import com.chrisargenta.utils.UtilException;

public class Generator {
	public GuidUtils gUtil=new GuidUtils();
	public Properties properties=new Properties();
	private String directory=null;
	private Domain domain=null;
	private int agentsMin=1;
	private int agentsMax=4;
	private int teamsMin=1;
	private int teamsMax=4;
	private int goalCount=100;
	private int sceneCount=10;
	private TeamFactory tf=null;
	private Random rand=new Random();
	private String domainName="UNSPECIFIED";
	private static ArrayList<String> generated=new ArrayList<String>();
	
	// string for property files
	public static final String AGENTSMIN="agents_min";
	public static final String TEAMSMIN="teams_min";
	public static final String AGENTSMAX="agents_max";
	public static final String TEAMSMAX="teams_max";
	public static final String GOALCOUNT="goals";
	private static final String SCENCECOUNT = "scene_count";
	
	public static void main(String[] args) {
		if(args.length<4){
			System.err.println("Usage: GenerateProblems {domain} {directory} {count} {seed}");
			System.exit(-1);
		}
		
		try{
			String directory=args[1];
			int count=Integer.parseInt(args[2]);
			
			Domain d=null;
			switch(args[0]){
				case("TeamBlocks"): d=new TeamBlocks(); break;
				case("Collector"): d=new Collector(); break;
				case("Storybook"): d=new Storybook(); break;
				default: {
					System.err.println("Error do such domain "+args[0]);
					System.exit(-1);
				}
			}
			
			
			Generator g=new Generator(d,directory);
			g.setDomainName(args[0]);
			
			if(args.length==4){
				int seed=Integer.parseInt(args[3]);
				g.setSeed(seed);

			}
			
			for (int i=0;i<count;i++){
				String desc=g.generate();
				System.out.println("Generated Env: "+desc);
			}
			
			try {
				ListUtils.writeList(directory+"/scenes.txt",generated);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			// Log scenarios
			
		}
	}
	
	private void setDomainName(String domainName) {
		this.domainName=domainName;
	}

	private String getGuid(){
		gUtil.setSize(5);
		return gUtil.getGuid();
	}
	
	public void setSeed(int seed){
		rand=new Random(seed);
	}
	
	public Generator(Domain domain, String directory){
		try {
			this.directory=directory;
			this.domain=domain;
			DirUtils.ensureDir(directory);
			properties=new Properties();
			loadProperties(directory);
		} catch (IOException e){
			System.err.println("Error loading files: "+e);
			System.exit(-1);
		} catch (UtilException e) {
		 	System.err.println("Error Ensuring Directory ("+directory+"): "+e);
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public String generate(){
		tf=new TeamFactory(rand.nextInt());
		domain.setSeed(rand.nextInt());
		gUtil.setSeed(rand.nextInt());
		String env=generateEnv();
		for(int i=0;i<sceneCount;i++){
			String scene =generateScene(env);
			generated.add(directory+"/"+env+"/"+scene);
		}
		return env;
	}
	
	private String generateEnv(){
		String env=getGuid();
		String envPath=directory+"/"+env;
		String template=null;
		try {
			DirUtils.ensureNew(envPath);
			domain.generateGoals(goalCount);
			template=domain.generateEnv();
			FileUtils.stringToFile(envPath+"/template.pddl", template);
			writeGoalsFile(envPath+"/goals.txt",domain.getAllGoals());
			
		} catch (UtilException | IOException e) {
			System.err.println("Error Setting Up Environment Directory ("+directory+")/("+env+"): "+e);
			e.printStackTrace();
			System.exit(-1);
		} 
		return env;
	}
	
	private String generateScene(String env){
		String scene=getGuid();
		String scenePath=directory+"/"+env+"/"+scene;
		String sceneTemplate=null;
		try {
			DirUtils.ensureNew(scenePath);
			
			// Setup Agents and Teams
			int agentCount=agentsMin;
			if(agentsMax-agentsMin!=0) agentCount=rand.nextInt(agentsMax-agentsMin)+agentsMin;
			List<String> agentList=tf.names(agentCount);
			ListUtils.writeList(scenePath+"/agents.txt",tf.names(agentCount));

			List<PartialIntrepretation> teams= tf.generateTeams(agentCount, goalCount, teamsMin, teamsMax);
			int teamCount=teams.size();
			
			// Domain Setup
			domain.setScene(scene);
			domain.setAgents(agentList);
			domain.setScenePath(scenePath);
			domain.setTeamCount(teamCount);
			domain.init();
			sceneTemplate=domain.generateScene(teams);
			FileUtils.stringToFile(scenePath+"/problem.pddl", sceneTemplate);
			
			// Desc file
			AnnotatedList desc=new AnnotatedList(scenePath+"/desc.txt");
			desc.setAttribute("Domain",domainName);
			desc.setAttribute("Env", env);
			desc.setAttribute("Scenario", scene);
			desc.setAttribute("TeamCount", teamCount+"");
			desc.setAttribute("AgentCount",agentCount+"");
			desc.setAttribute("GoalCount", goalCount+"");
			desc.setAttribute("GenTime",(new Date()).toString());
			desc.write();
			
			// Key File
			writeKeyFile(scenePath+"/key.txt",teams);
 
			// Domain File
			InputStream u=this.getClass().getResourceAsStream(domain.getDomainFile());
			String temp=FileUtils.streamToString(u); //this is inefficient but we might need to modify the file...
			writeStringToFile(scenePath+"/domain.pddl",temp);
			
			//Rewrite goal file for autonomy of scenarios
			writeGoalsFile(scenePath+"/goals.txt",domain.getAllGoals());
			
			
		} catch (UtilException e) {
			System.err.println("Error Ensuring Problem Directory ("+directory+")/("+env+")/("+scene+"): "+e);
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			System.err.println("Error writing out files to ("+scenePath+"): "+e);
			e.printStackTrace();
			System.exit(-1);
		} 
		return scene;
	}
	
	private void writeKeyFile(String path,List<PartialIntrepretation> key) throws IOException{
		String line="";
		for(PartialIntrepretation team: key){
			line+=team.toString();
		}	
		writeStringToFile(path,line);
	}
	
	private void writeGoalsFile(String path,List<String> goals) throws IOException{
		ListUtils.writeList(path, goals);
	}
	
	private void writeStringToFile(String fullpath,String content) throws IOException{
		FileWriter fstream=null;
		BufferedWriter out=null;
		fstream= new FileWriter(fullpath);
		out = new BufferedWriter(fstream);
		out.write(content);
		out.close();
	}
	
	public void loadProperties(String directory) throws IOException{
		File f=new File(directory+"/genconfig.txt");
		if(f.exists()){
			FileInputStream in=new FileInputStream(f);
			properties.load(in);
			in.close();
		}
		agentsMin=getProp(Generator.AGENTSMIN,agentsMin);
		agentsMax=getProp(Generator.AGENTSMAX,agentsMax);
		teamsMin=getProp(Generator.TEAMSMIN,teamsMin);
		teamsMax=getProp(Generator.TEAMSMAX,teamsMax);
		goalCount=getProp(Generator.GOALCOUNT,goalCount);
		sceneCount=getProp(Generator.SCENCECOUNT,sceneCount);
		if(!f.exists()){
			FileOutputStream out=new FileOutputStream(f);
			properties.store(out, "Autogenerated Defaults");
			out.close();
		}
	
	}
	
	private int getProp(String token, int value){
		if(!properties.containsKey(token)){
			properties.setProperty(token, ""+value);
		}
		return Integer.parseInt(properties.getProperty(token));
	}
	
	

}
