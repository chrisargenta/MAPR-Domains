package com.chrisargenta.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


public class ListUtils {
	
	public static final Comparator<String> NUMBERED_ORDER = 
		new Comparator<String>() {
	        public int compare(String s1, String s2) {
	        	String[] s1s = s1.split("\t");
	        	String[] s2s = s2.split("\t");
	            Integer s1n = Integer.parseInt(s1s[0]);
	            Integer s2n = Integer.parseInt(s2s[0]);
	            return s1n.compareTo(s2n);
	        }
		};
		
	public static void readList(String name, String path, List<String> list) throws IOException{
		String fullPath=path+"/"+name;
		readList(fullPath,list);
	}
	
	public static void readList(InputStream input, List<String> list) throws IOException{
		InputStreamReader isr=new InputStreamReader(input);
	    BufferedReader reader = new BufferedReader(isr);
		String line="";
	    if (input != null) {                            
            while ((line=reader.readLine())!=null) {    
            	if(line.length()>0 && line.charAt(0)!='#') // String annotations off of Annotated Lists
    				list.add(line);
            }                
        }
	    if(reader!=null) reader.close();
	    if(isr!=null) isr.close();
	}
	
	public static void readList(String fullPath, List<String> list) throws IOException{
		File file=null;
		FileReader in=null;
		LineNumberReader lin=null;
		file=new File(fullPath);
		in=new FileReader(file);
		lin=new LineNumberReader(in);
		String line="";
		while((line=lin.readLine())!=null){
			if(line.length()>0 && line.charAt(0)!='#') // String annotations off of Annotated Lists
				list.add(line);
		}
		if(lin!=null) lin.close();
		if(in!=null) in.close();
	}
	
	public static void writeList(String name, String path, Iterator<String> iterator) throws IOException {
		String fullPath=path+"/"+name;
		writeList(fullPath,iterator);
	}
	
	public static void writeList(String fullPath, Iterator<String> iterator) throws IOException{
		FileWriter fstream=null;
		BufferedWriter out=null;
		fstream= new FileWriter(fullPath);
		out = new BufferedWriter(fstream);

		while(iterator.hasNext()){
			out.write(iterator.next());
			out.newLine();
		}		
		out.close();
	}
	
	public static void writeList(String fullPath,List<String> list) throws IOException{
		FileWriter fstream=null;
		BufferedWriter out=null;
		fstream= new FileWriter(fullPath);
		out = new BufferedWriter(fstream);

		for(String line: list){
			out.write(line);
			out.newLine();
		}		
		out.close();
	}
	
	public static void lineReplace(String inFile, String outFile, String find, String replace) throws IOException{
		List<String> in=new ArrayList<String>();
		ListUtils.readList(inFile, in);
		List<String> out=new ArrayList<String>();
		
		for (String line: in){
			if (line.trim().equalsIgnoreCase(find.trim())){
				out.add(replace);
			} else {
				out.add(line);
			}
		}
		ListUtils.writeList(outFile, out.iterator());
	}
	
}
