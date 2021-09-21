package com.chrisargenta.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.chrisargenta.utils.AttributedObject;


public class AnnotatedList extends AttributedObject<String,String>{

	public List<String> list=new ArrayList<String>();
	
	public AnnotatedList(String path, String filename) {
		super("_NONE");
		setAttribute("_PATH",path);
		setAttribute("_FILENAME",filename);
	}
	
	// getAttributre(String) /setAttribute(String, String)
	
	public AnnotatedList(String fullpath) {
		super("_NONE");
		File file=new File(fullpath);
		setAttribute("_PATH",file.getPath().substring(0,file.getPath().lastIndexOf(File.separator)));
		setAttribute("_FILENAME",file.getName());
	}

	public boolean exists(){
		File file=new File(getAttribute("_PATH")+"/"+getAttribute("_FILENAME"));
		return file.exists();
	}
	
	public void read() throws IOException{
		File file=new File(getAttribute("_PATH")+"/"+getAttribute("_FILENAME"));
		FileReader in=new FileReader(file);
		LineNumberReader lin=new LineNumberReader(in);
		String line="";
		while((line=lin.readLine())!=null){
			if(!line.isEmpty()){
				if(line.charAt(0)=='#'){ // Annotation
					int ifp=line.lastIndexOf('\t');
					setAttribute(line.substring(1,ifp).trim(),line.substring(ifp).trim());
				} else {
					list.add(line);
				}
			}
		}
		if(lin!=null) lin.close();
		if(in!=null) in.close();

	}
	
	public void write() throws IOException {
		FileWriter fstream=null;
		BufferedWriter out=null;
		fstream= new FileWriter(getAttribute("_PATH")+"/"+getAttribute("_FILENAME"));
		out = new BufferedWriter(fstream);

		Iterator<String> kIter= keySet().iterator();
		while (kIter.hasNext()){
			String k=kIter.next();
			if (k.charAt(0)!='_'){ // do not write out internal attributes
				out.write("# "+k+"\t"+getAttribute(k));
				out.newLine();
			}
		}
		
		Iterator<String> lIter= list.iterator();
		while(lIter.hasNext()){
			out.write(lIter.next());
			out.newLine();
		}		
		out.close();

	}
	
	public Iterator<String> iterator(){
		return list.iterator();
	}
	
	public void add(String line){
		list.add(line);
	}
	
	public void add(int index, String line){
		list.add(index, line);
	}
	
	public void addAll(Collection<String> c){
		list.addAll(c);
	}

	public int size(){
		return list.size();
	}
	
	public String attributesToCsv(String header){
		List<String> headers = Arrays.asList(header.split(","));
		String out="";
		Iterator<String> kIter= headers.iterator();
		while (kIter.hasNext()){
			String k=kIter.next();
			out+="\""+getAttribute(k)+"\"";
			if(kIter.hasNext())
				out+=",";
		}
		return out;
	}
	
	public String attributeHeadersToCsv(){
		String out="";
		Iterator<String> kIter= keySet().iterator();
		while (kIter.hasNext()){
			String k=kIter.next();
			if (k.charAt(0)!='_'){ // do not write out internal attributes
				out+=k;
				if(kIter.hasNext())
					out+=",";
			}
		}
		return out;
	}
}
