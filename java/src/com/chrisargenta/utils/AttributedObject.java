package com.chrisargenta.utils;

import java.util.HashMap;
import java.util.Set;

public class AttributedObject <Tkey, Tvalue> {
	public HashMap<Tkey,Tvalue> attributes = new HashMap<Tkey, Tvalue>();
	protected Tvalue def=null;
	
	protected AttributedObject(Tvalue def){
		this.def=def;
	}
	
	public void setAttribute(Tkey key, Tvalue value){
			attributes.put(key,value);
	}
	
	public Tvalue getAttribute(Tkey key){
		if (!attributes.containsKey(key)) return def;
		return attributes.get(key);
	}
	
	public Set<Tkey> keySet(){
		return attributes.keySet();
	}
	
	public boolean hasAttribute(Tkey key){
		return attributes.containsKey(key);
	}
	
	public void removeAttribute(Tkey key){
		if (!attributes.containsKey(key)) return;
		attributes.remove(key);
	}
	
}
