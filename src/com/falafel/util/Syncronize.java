/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.falafel.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.TimerTask;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlide;

/**
 * sync
 * @author rizal
 */
public class Syncronize extends TimerTask{
    private final int index;
    private final XSLFSlide pptxSlide;
    private final HSLFSlide pptSlide;
    private final Dimension size;
    
    public Syncronize(int index, HSLFSlide pptSlide, XSLFSlide pptxSlide, Dimension size){
        this.index = index;
        this.pptSlide = pptSlide;
        this.pptxSlide = pptxSlide;
        this.size = size;
    }
    
    @Override
    public void run() {
        SlideSession.getImages().set(index, addImageAsync(size, pptSlide, pptxSlide));
        System.out.println("add index "+index+" done");
    }
    
    private Image addImageAsync(Dimension size, HSLFSlide pptSlide, XSLFSlide pptxSlide){
        BufferedImage img = new BufferedImage(size.width, size.height,BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = img.createGraphics();
        graphics.setPaint(Color.white);
        graphics.fill(new Rectangle2D.Float(0, 0, size.width, size.height));
        if (pptxSlide == null) { pptSlide.draw(graphics); }
        else{ pptxSlide.draw(graphics); }
        return SwingFXUtils.toFXImage(img, null);
    }
}
