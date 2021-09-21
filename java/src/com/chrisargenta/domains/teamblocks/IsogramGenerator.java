package com.chrisargenta.domains.teamblocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class IsogramGenerator {
	private String alphabet="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static Random rand=new Random();
	
	public static void main(String[] args){
		int size=4;
		int partitions=5;
		
		try{
			size=Integer.parseInt(args[1]);
			partitions=Integer.parseInt(args[2]);
		} catch (Exception e){
			System.err.println("Usage: IsogramGenerator <size> <partitions>");
		}
		
		IsogramGenerator ig=new IsogramGenerator();
		List<String> list=ig.generate(size,partitions);
				
		System.out.println("Generated "+list.size()+ " words.");

		/*
		for (String s: list){
			System.out.println(s);
		}
		*/
		
	}
	
	public void setSeed(int seed){
		rand=new Random(seed);
	}
	
	public IsogramGenerator(String alphabet){
		this.alphabet=alphabet;
	}
	
	public IsogramGenerator(){
	}
		
	public List<String> generate(int size, int partitions){	
		List<String> list=new ArrayList<String>();
		int total=size*partitions;
		if (total>alphabet.length()){
			System.err.println("WARNING: Insufficient alphabet size for request. Need "+total+"but have "+alphabet.length()+", using all availible.");
			total=alphabet.length();
		}
		
		String part=take(size*partitions);
		list.addAll(permutationsOfSize(part,size));
			

		//Collections.sort(list);
		return list;
	}
	
	private String take(int count){
		Set<Character> chars=new HashSet<Character>(count);
		if (alphabet.length()<count){
			System.err.println("ERROR: extracting too many characters from alphabet.");
			count=alphabet.length();
		};
		
		while(chars.size()<count){
			int x=rand.nextInt(alphabet.length());
			chars.add(alphabet.charAt(x));
			alphabet=alphabet.substring(0, x)+alphabet.substring(x+1,alphabet.length());
		}
		
		String out="";
		for (Character c: chars){
			out+=c;
		}
		return out;
	}
		
	public List<String> filterandSort (List<String> inputs, int minLength, int maxLength){
		List<String> output=new ArrayList<String>(inputs.size());
		for (String s: inputs){
			if(s.length()>minLength && s.length()<maxLength)
				output.add(s);
		}
		
		return output;
	}	
	
	private Set<String> permutationsOfSize(String str, int size) { 
		Set<String> all=new HashSet<String>();
	    permuteToSize(all,"", str,size); 
	    return all;
	}

	private void permuteToSize(Set<String> all, String partial, String remaining, int size) {
	    int n = remaining.length();
	    if (partial.length()==size) all.add(partial);
	    else {
	        for (int i = 0; i < n; i++)
	        	permuteToSize(all, partial + remaining.charAt(i), remaining.substring(0, i) + remaining.substring(i+1, n), size);
	    }
	}	
	
}
