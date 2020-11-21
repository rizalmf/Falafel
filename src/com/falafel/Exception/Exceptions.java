/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.falafel.Exception;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rizal
 */
public class Exceptions {
    private Logger log;
    private final Class T;

    public Exceptions(Class T) {
        this.T = T;
    }
    
    //javaLogger
    public void log(Exception ex){
        log = (log == null) ? Logger.getLogger(T.getName()) : log;
        log.log(
                Level.SEVERE 
                , ex.getMessage()
                //, ex //uncomment this to read full logs
        );
    }
}
