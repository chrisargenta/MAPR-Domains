package com.chrisargenta.utils;

import java.util.ArrayList;
import java.util.List;

public class StringPermutation {
	// string permutation code adapted from http://stackoverflow.com/questions/4240080/generating-all-permutations-of-a-given-string
	
	public static List<String> permute(String str) { 
		List<String> all=new ArrayList<String>();
	    _permutation(all,"", str); 
	    return all;
	}
	
	public static List<String> permuteSubs(String str){
		List<String> all=new ArrayList<String>();
		for (int i=0;i<str.length();i++){
			char suffix=str.charAt(i);
			all.add(""+suffix);
			int growth=all.size()-1;
			for (int j=0;j<growth;j++){
				if(all.get(j).length()<str.length())
					_permutation(all,"",all.get(j)+suffix);
			}
		}
		return all;
	}

	public static List<String> comboSubs(String str){
		List<String> all=new ArrayList<String>();
		for (int i=0;i<str.length();i++){
			char suffix=str.charAt(i);
			all.add(""+suffix);
			int growth=all.size()-1;
			for (int j=0;j<growth;j++){
				if(all.get(j).length()<str.length())
					all.add(all.get(j)+suffix);
			}
		}
		return all;
	}
	
	private static void _permutation(List<String> all, String prefix, String str) {
	    int n = str.length();
	    if (n == 0) all.add(prefix);
	    else {
	        for (int i = 0; i < n; i++)
	            _permutation(all, prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n));
	    }
	}
}
