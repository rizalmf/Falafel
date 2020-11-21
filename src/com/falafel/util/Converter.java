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
import java.util.List;
import java.util.Timer;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlide;

/**
 *
 * @author rizal
 */
public class Converter {
    public Image pptToImage(Dimension size, List<HSLFSlide> list){
        Image image = null;
        SlideSession.setImage(list.size());
        SlideSession.flushImage();
        if (list.size() >= 1) {
            image = addImageAsync(size, list.get(0), null);
            SlideSession.getImages().set(0, image);
            for (int i = 1; i < list.size(); i++) {
                new Timer().schedule(new Syncronize(i, list.get(i), null, size), 0);
            }
        }
        return image;
    }
    public Image pptxToImage(Dimension size, List<XSLFSlide> list){
        Image image = null;
        SlideSession.setImage(list.size());
        SlideSession.flushImage();
        if (list.size() >= 1) {
            image = addImageAsync(size, null, list.get(0));
            SlideSession.getImages().set(0, image);
            for (int i = 1; i < list.size(); i++) {
                new Timer().schedule(new Syncronize(i, null, list.get(i), size), 0);
            }
        }
        return image;
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
