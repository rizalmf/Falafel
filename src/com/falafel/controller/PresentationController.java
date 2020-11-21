/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.falafel.controller;

import com.jfoenix.controls.JFXButton;
import com.falafel.model.Narrative;
import com.falafel.util.SlideSession;
import com.falafel.util.Converter;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author rizal
 */
public class PresentationController implements Initializable {
    
    private GraphicsContext graphicsContext;
    private double lastX, lastY, oldX, oldY;
    @FXML
    private AnchorPane ap;
    @FXML
    private HBox slidePane1;
    @FXML
    private HBox slidePane2;
    @FXML
    private Canvas canvas;
    @FXML
    private Label lMsg;
    @FXML
    private HBox hbButtons;
    @FXML
    private Button bExit;
    @FXML
    private HBox hbTop;
    @FXML
    private JFXButton bMini;
    @FXML
    private JFXButton bWindow;
    @FXML
    private JFXButton bHome;
    @FXML
    private JFXButton bPrev;
    @FXML
    private JFXButton bNext;
    private Narrative narrative;
    @FXML
    private ColorPicker cpPencil;
    @FXML
    private JFXButton bClear;
    public void setPresentation(Narrative narrative){
        this.narrative = narrative;
    }
    private Converter converter;
    private Image firstImage;
    private int page;
    private int page_size;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        converter = new Converter();
        canvasProperties();
        hbButtonsProperties();
        hbTopProperties();
        Platform.runLater(() -> {
            lMsg.setText((narrative.is_ppt())? "Reading ppt file ..": "Reading pptx file ..");
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (narrative.is_ppt()) {
                        firstImage = converter.pptToImage(
                                narrative.getPptSlideShow().getPageSize(), 
                                narrative.getPptSlideShow().getSlides());
                    }else{
                        firstImage = converter.pptxToImage(
                                narrative.getPptxSlideShow().getPageSize(), 
                                narrative.getPptxSlideShow().getSlides());
                    }
                    runPresentation();
                    page = 0;
                    page_size = SlideSession.getImages().size();
                }
            }, 0);
        });
    } 
    private void hbButtonsProperties(){
        bHome.setTooltip(new Tooltip("Back first page"));
        bPrev.setTooltip(new Tooltip("Previous page"));
        bNext.setTooltip(new Tooltip("Next page"));
        cpPencil.setTooltip(new Tooltip("Choose pencil color"));
        bHome.setOnAction((e) -> {
            page =0;
            ImageView view = new ImageView(SlideSession.getImages().get(page));
            Dimension scSize = Toolkit.getDefaultToolkit().getScreenSize();
            view.setPreserveRatio(true);
            view.setFitHeight(scSize.getHeight());
            slidePane1.getChildren().clear();
            slidePane1.getChildren().add(view);
        });
        bPrev.setOnAction((e) -> {
            if (page <= 0) {
                page =1;
            }
            if ((page-- - 1) > -1 ) {
                ImageView view = new ImageView(SlideSession.getImages().get(page));
                view.fitHeightProperty().bind(ap.heightProperty());
                view.setPreserveRatio(true);
                slidePane1.getChildren().clear();
                slidePane1.getChildren().add(view);
            }
        });
        bNext.setOnAction((e) -> {
            if (page >= page_size) {
                page = page_size -1;
                return;
            }
            if ((page_size - page++) > 1 ) {
                ImageView view = new ImageView(SlideSession.getImages().get(page));
                view.fitHeightProperty().bind(ap.heightProperty());
                view.setPreserveRatio(true);
                slidePane1.getChildren().clear();
                slidePane1.getChildren().add(view);
            }
        });
        cpPencil.setOnAction((e) -> {
            graphicsContext.setStroke(cpPencil.getValue());
        });
        bClear.setOnAction((e) -> {
            graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        });
    }
    double xOffset = 0;
    double yOffset = 0;
    private void hbTopProperties(){
        bExit.setTooltip(new Tooltip("Exit"));
        bWindow.setTooltip(new Tooltip("Togle fullscreen"));
        bMini.setTooltip(new Tooltip("Minimize"));
        bExit.setOnAction((e) -> {
            Stage stage = (Stage) ap.getScene().getWindow();
            stage.getOnCloseRequest().handle(null);
            slidePane1.getChildren().clear();
            slidePane2.getChildren().clear();
        });
        bWindow.setOnAction((e) -> {
            Stage stage = (Stage) ap.getScene().getWindow();
            stage.setFullScreen(!stage.isFullScreen());
        });
        bMini.setOnAction((e) -> {
            Stage stage = (Stage) ap.getScene().getWindow();
            stage.setIconified(true);
        });
        hbTop.setOnMouseDragged((event) -> {
            Stage stage = (Stage) ap.getScene().getWindow();
            if (!stage.isFullScreen()) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
        hbTop.setOnMousePressed((event) -> {
            Stage stage = (Stage) ap.getScene().getWindow();
            if (!stage.isFullScreen()) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
    }
    private void runPresentation(){
        Timeline tl = new Timeline(new KeyFrame(Duration.ONE, (e) -> {
            if (firstImage != null) {
                lMsg.setText("");
                ImageView view = new ImageView(firstImage);
                view.fitHeightProperty().bind(ap.heightProperty());
                view.setPreserveRatio(true);
                slidePane1.getChildren().clear();
                slidePane1.getChildren().add(view);
            }else{
                lMsg.setText("Problem occur. Cant load file");
            }
        }));
        tl.play();
    }
    private void canvasProperties(){
        graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setStroke(cpPencil.getValue());
        canvas.widthProperty().bind(ap.widthProperty());
        canvas.heightProperty().bind(ap.heightProperty());
        canvas.setOnMouseDragged((e) -> {
            lastX = e.getX();
            lastY = e.getY();
            graphicsContext.setLineWidth(4);
            graphicsContext.strokeLine(oldX, oldY, lastX, lastY);
            oldX = lastX;
            oldY = lastY;
        });
        canvas.setOnMousePressed((e) -> {
            oldX = e.getX(); oldY = e.getY(); 
        });
    }
}
