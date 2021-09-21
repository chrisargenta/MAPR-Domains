package com.chrisargenta.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileUtils {
	
	public static void stringToFile(String fullpath,String content) throws IOException{
		byte[] encoded=content.getBytes(StandardCharsets.UTF_8);
		Files.write(Paths.get(fullpath), encoded,StandardOpenOption.CREATE);
	}
	
	public static String fileToString(String fullpath) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(fullpath));
		return new String(encoded, StandardCharsets.UTF_8);
	}
	
	public static String streamToString(InputStream input) throws IOException{
		String out="";
		InputStreamReader isr=new InputStreamReader(input);
	    BufferedReader reader = new BufferedReader(isr);
		String line="";
	    if (input != null) {                            
            while ((line=reader.readLine())!=null) {    
            	if(line.length()>0 && line.charAt(0)!='#') // String annotations off of Annotated Lists
    				out+=line+"\n";
            }                
        }
	    if(reader!=null) reader.close();
	    if(isr!=null) isr.close();
	    return out;
	}

	
}
