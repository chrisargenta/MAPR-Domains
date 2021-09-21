package com.chrisargenta.utils;

import java.util.Random;

public class GuidUtils {
	private String alphabet="abcdefghijklmnopqrstuvwxyz";
	private int size=6;
	private Random rand=null;
	
	
	public GuidUtils(){
		rand=new Random();
	}
	
	public GuidUtils(int seed){
		rand=new Random(seed);
	}
	
	public void setAlphaBet(String alphabet){
		this.alphabet=alphabet;
	}
	
	public void setSize(int size){
		this.size=size;
	}
	
	public void setSeed(long seed){
		rand=new Random(seed);
	}
	
	public String getGuid(){
		String out="";
		for (int i=0;i<size;i++)
			out+=alphabet.charAt(rand.nextInt(alphabet.length()));
		return out;
	}
}
