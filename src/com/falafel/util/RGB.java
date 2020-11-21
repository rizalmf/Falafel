/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.falafel.util;

import javafx.scene.paint.Color;

/**
 *
 * @author rizal
 */
public class RGB {
    
    public String toString(Color color){
        return String.format("#%02X%02X%02X", 
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue()* 255)
                );
    }
}
