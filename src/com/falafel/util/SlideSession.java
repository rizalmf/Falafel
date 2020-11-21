/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.falafel.util;

import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

/**
 *
 * @author rizal
 */
public class SlideSession {
    private static ObservableList<Image> listImage;
    private static Image[] images;
    public static ObservableList<Image> getImages(){
        if (listImage == null) {
            listImage = FXCollections.observableArrayList(Arrays.asList(images));
        }
        return listImage;
    }
    public static void setImage(int size){
        images = new Image[size];
    }
    public static void flushImage(){
        listImage = FXCollections.observableArrayList(Arrays.asList(images));
    }
}
