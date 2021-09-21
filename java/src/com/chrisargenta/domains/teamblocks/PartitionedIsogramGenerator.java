package com.chrisargenta.domains.teamblocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PartitionedIsogramGenerator {
	private String alphabet="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static Random rand=new Random();
	
	public static void main(String[] args){
		int size=3;
		int partitions=5;
		
		try{
			size=Integer.parseInt(args[1]);
			partitions=Integer.parseInt(args[2]);
		} catch (Exception e){
			System.err.println("Usage: IsogramGenerator <size> <partitions>");
		}
		
		PartitionedIsogramGenerator ig=new PartitionedIsogramGenerator();
		List<String> list=ig.generate(size,partitions);
				
		for (String s: list){
			System.out.println(s);
		}
		
	}
	
	public PartitionedIsogramGenerator(String alphabet){
		this.alphabet=alphabet;
	}
	
	public PartitionedIsogramGenerator(){
	}
		
	public List<String> generate(int size, int partitions){	
		List<String> list=new ArrayList<String>();
		
		for (int i=0;i<partitions;i++){
			String part=take(size);
			System.out.println("Partition "+i+": "+part+" ("+alphabet+")");
			list.addAll(permutations(part));
		}
		System.out.println("Generated "+list.size()+ " words.");

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
	
	// string permutation code adapted from http://stackoverflow.com/questions/4240080/generating-all-permutations-of-a-given-string
	
	private Set<String> permutations(String str) { 
		Set<String> all=new HashSet<String>();
	    permute(all,"", str); 
	    return all;
	}

	private void permute(Set<String> all, String prefix, String str) {
	    int n = str.length();
	    if (n == 0) all.add(prefix);
	    else {
	        for (int i = 0; i < n; i++)
	            permute(all, prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n));
	    }
	}	
	
}
