/*
 * Timer.java
 *
 * Created on May 26, 2006, 8:52 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.chrisargenta.utils;

/**
 *
 * @author Chris Argenta
 */
public class TimerUtil {
    
    long startTime=0;
    long stopTime=0;
   
    public void start(){
        startTime=System.currentTimeMillis();
    }
    
    public void stop(){
        stopTime=System.currentTimeMillis();
    }
    
    public long get(){
        if (stopTime!=0) return stopTime-startTime;
        else return System.currentTimeMillis()-startTime;
    }
    
    public void reset(){
        startTime=0;
        stopTime=0;
    }
}
