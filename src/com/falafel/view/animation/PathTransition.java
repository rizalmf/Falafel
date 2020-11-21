/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.falafel.view.animation;

import com.fxexperience.javafx.animation.CachedTimelineTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.TimelineBuilder;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 *
 * @author RIZAL
 */
public class PathTransition extends CachedTimelineTransition{
    private double startX, startY, endX, endY;
    
    public PathTransition(final Node node, double startX, double startY, 
            double endX, double endY, boolean out) {
        super(node, null);
        if (out) {
            node.setOpacity(0);
        }
        setCycleDuration(Duration.millis(800));
        setDelay(Duration.seconds(0));
        this.startX =startX;
        this.startY =startY;
        this.endX =endX;
        this.endY =endY;
    }

    @Override 
    protected void starting() {
        timeline = TimelineBuilder.create()
                .keyFrames(
                    new KeyFrame(Duration.millis(0),    
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(node.translateXProperty(), startX, WEB_EASE),
                        new KeyValue(node.translateYProperty(), startY, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(800),    
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(node.translateXProperty(), endX, WEB_EASE),
                        new KeyValue(node.translateYProperty(), endY, WEB_EASE)
                    )
                )
                .build();
        super.starting();
    }
}
