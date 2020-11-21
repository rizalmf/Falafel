/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.falafel.model;

import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

/**
 *
 * @author rizal
 */
public class Narrative {
    private boolean is_ppt;
    private boolean is_pptx;
    private HSLFSlideShow pptSlideShow;
    private XMLSlideShow pptxSlideShow;

    public Narrative(boolean is_ppt, boolean is_pptx, HSLFSlideShow pptSlideShow, XMLSlideShow pptxSlideShow) {
        this.is_ppt = is_ppt;
        this.is_pptx = is_pptx;
        this.pptSlideShow = pptSlideShow;
        this.pptxSlideShow = pptxSlideShow;
    }

    public Narrative() {
    }

    public boolean is_ppt() {
        return is_ppt;
    }

    public void setIs_ppt(boolean is_ppt) {
        this.is_ppt = is_ppt;
    }

    public boolean is_pptx() {
        return is_pptx;
    }

    public void setIs_pptx(boolean is_pptx) {
        this.is_pptx = is_pptx;
    }

    public HSLFSlideShow getPptSlideShow() {
        return pptSlideShow;
    }

    public void setPptSlideShow(HSLFSlideShow pptSlideShow) {
        this.pptSlideShow = pptSlideShow;
    }

    public XMLSlideShow getPptxSlideShow() {
        return pptxSlideShow;
    }

    public void setPptxSlideShow(XMLSlideShow pptxSlideShow) {
        this.pptxSlideShow = pptxSlideShow;
    }
    
}
