/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.falafel.controller;

import com.falafel.Exception.Exceptions;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import com.falafel.view.animation.PathTransition;
import com.falafel.model.Narrative;
import static com.falafel.role.PathRole.ICON_PATH;
import static com.falafel.role.PathRole.ON_PRESENTATION_TITLE;
import static com.falafel.role.PathRole.PRESENTATION_PATH;
import com.falafel.util.RGB;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

/**
 * FXML Controller class
 *
 * @author rizal
 */
public class MainController implements Initializable {

    private final Exceptions exceptions = new Exceptions(getClass());
    
    private boolean is_out;
    private GraphicsContext graphicsContext;
    private double lastX, lastY, oldX, oldY;
    private boolean is_drawing;
    private String colorName;
    private int colorState;
    @FXML
    private AnchorPane ap;
    @FXML
    private Button bMenu;
    @FXML
    private Canvas canvas;
    @FXML
    private Pane paneButtons;
    @FXML
    private Button bBoard;
    @FXML
    private Button bPencil;
    @FXML
    private Button bClear;
    @FXML
    private Button bPpt;
    @FXML
    private Button bColor;
    @FXML
    private ColorPicker cpPencil;
    @FXML
    private FontAwesomeIconView viewPencil;
    @FXML
    private FontAwesomeIconView viewColor;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        nodeInitiate();
        canvasProperties();
        buttonProperties();
        Platform.runLater(() -> {
            initDimension();
        });
    }
    
    /**
     * First node initiate
     */
    private void nodeInitiate(){
        ap.setBackground(Background.EMPTY);
        paneButtons.setVisible(false);
    }
    
    /**
     * set dimension first run app
     */
    private void initDimension() {
        Dimension scSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = scSize.getWidth();
        double height = scSize.getHeight();
        paneButtons.setLayoutX(-70);
        paneButtons.setLayoutY(height-175);
        paneButtons.setVisible(true);
    }
    
    /**
     * Button properties
     */
    private void buttonProperties(){
        buttonParentProperties();
        buttonChildProperties();
    }
    private boolean isWindows(){
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }
    private void buttonParentProperties(){
        if (isWindows()) {
            paneButtons.setOnMouseExited((e) -> {
                buttonChildAnimate(false);
            });
            bMenu.setOnMouseDragged((e) -> {
                if (bPpt.isVisible()) { bPpt.setVisible(false); }
                paneButtons.setLayoutX(e.getScreenX()-110);
                paneButtons.setLayoutY(e.getScreenY()-110);
            });
            bMenu.setOnMousePressed((e) -> {
                if (bPpt.isVisible()) { bPpt.setVisible(false); }
            });
            bMenu.setOnMouseEntered((e) -> {
                if (!bPpt.isVisible()) { buttonChildAnimate(true); }
            });
            
            bMenu.setOnMouseReleased((e) -> {
                buttonChildAnimate(true);
                checkButtonsPosition(e.getScreenX(), e.getScreenY());
            });
        }else{
            bMenu.setOnMouseDragged((e) -> {
                paneButtons.setLayoutX(e.getScreenX()-110);
                paneButtons.setLayoutY(e.getScreenY()-110);
            });
            bMenu.setOnMouseReleased((e) -> {
                if (is_out) {
                    is_out = false;
                    buttonChildAnimate(false);
                }else{
                    is_out = true;
                    buttonChildAnimate(true);
                }
                checkButtonsPosition(e.getScreenX(), e.getScreenY());
            });
        }
    }
    private void buttonChildProperties(){
//        bMenu.setTooltip(new Tooltip("Falafel ready to serve"));
        bPpt.setTooltip(new Tooltip("Open .pptx file"));
        bBoard.setTooltip(new Tooltip("Choose board"));
        bPencil.setTooltip(new Tooltip("Start drawing"));
        cpPencil.setTooltip(new Tooltip("Choose pencil color"));
        bClear.setTooltip(new Tooltip("Clear your monitor"));
        bPencil.visibleProperty().bind(bPpt.visibleProperty());
        bColor.visibleProperty().bind(bPpt.visibleProperty());
        bClear.visibleProperty().bind(bPpt.visibleProperty());
        bBoard.visibleProperty().bind(bPpt.visibleProperty());
        bPpt.setVisible(false);
        bPptProperties();
        bBoardProperties();
        bPencilProperties();
        bClearProperties();
    }
    
    private void bPptProperties(){
        bPpt.setOnAction((e) -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Choose presentation file");
            List<String> ext = new ArrayList<>(
                    Arrays.asList(
                            "*.ppt", 
                            "*.pptx"
                    )
            );
            FileChooser.ExtensionFilter extFilter = 
                    new FileChooser.ExtensionFilter("Presentation format", ext);
            fc.getExtensionFilters().add(extFilter);
            Stage thisStage = (Stage) ap.getScene().getWindow();
            File file = fc.showOpenDialog(thisStage);
            if (file != null) {
                Narrative p = processPresentation(file);
                if (p != null) {
                    openPresentation(p);
                }
            }
        });
    }
    private Narrative processPresentation(File file){
        Narrative narrative = null;
        try {
            //check .ppt
            HSLFSlideShow ppt = new HSLFSlideShow(new FileInputStream(file));
            narrative = new Narrative(true, false, ppt, null);
            System.out.println("file is .ppt");
        } catch (Exception e) {
            try {
                //check .pptx
                XMLSlideShow pptx = new XMLSlideShow(new FileInputStream(file));
                narrative = new Narrative(false, true, null, pptx);
                System.out.println("file is .pptx");
            } catch (Exception ex) {
                exceptions.log(ex);
            }
        }
        return narrative;
    }
    private void openPresentation(Narrative presentation){
        Timeline tl = new Timeline(new KeyFrame(Duration.ONE, (ActionEvent e) -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader()
                    .getResource(PRESENTATION_PATH));
            Parent root1 = null;
            try {
                root1 = (Parent) fxmlLoader.load();
            } catch (IOException ex) {
                exceptions.log(ex);
            }
            Stage thisStage = (Stage) ap.getScene().getWindow();
            PresentationController presentationController = fxmlLoader.getController();
            presentationController.setPresentation(presentation);
            if (is_drawing) { unusedState(); }
            Stage stage = new Stage();
            stage.setOnCloseRequest((WindowEvent ev) -> {
                stage.close();
                thisStage.show();
            });
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle(ON_PRESENTATION_TITLE);
            stage.getIcons().add(new Image(getClass().getClassLoader()
                    .getResourceAsStream(ICON_PATH)));
            Scene scene = new Scene(root1);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);  
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setFullScreen(true);
            stage.show();
            thisStage.close();
        }));
        tl.setCycleCount(1);
        tl.play();
    }
    
    private void bBoardProperties(){
        bBoard.setOnAction((e) -> {
            switch(colorState){
                case 1: //colorName = "#000000";
                        ap.setId("blackBoard");
                        colorState++;break;
                case 2: //colorName = "#ffffff";
                        ap.setId("whiteBoard");
                        colorState++;break;
                case 3: if (isWindows()) 
                        { colorName = "#ffffff01";ap.setId("trans"); }
                        else{ 
                            colorName = "#ffffff21"; 
                            ap.setId("transBoard");
                        }
                        ap.setStyle(""
                                + "-fx-background-color:"+colorName+";");
                        colorState = 1;break;
            }
            viewPencil.setIcon(FontAwesomeIcon.SQUARE);
            is_drawing = true;
            bPencil.setId("bPencilInactive");
            bPencil.setTooltip(new Tooltip("End drawing"));
        });
    }
    private void bPencilProperties(){
        bPencil.setOnAction((e) -> {
            if (is_drawing) {
                unusedState();
            }else{
                viewPencil.setIcon(FontAwesomeIcon.SQUARE);
                if (isWindows()) { ap.setId("trans");
                    ap.setStyle("-fx-background-color:#ffffff01;"); }
                else{ 
                    ap.setStyle("-fx-background-color:#ffffff21;");
                    ap.setId("transBoard"); 
                }
                is_drawing = true;
                bPencil.setId("bPencilInactive");
                bPencil.setTooltip(new Tooltip("End drawing"));
            }
        });
        cpPencil.setOnAction((e) -> {
            graphicsContext.setStroke(cpPencil.getValue());
            String rgb = new RGB().toString(cpPencil.getValue());
            viewColor.setFill((rgb.equals("#000000")? Paint.valueOf("#ffffff"): Paint.valueOf("#000000")));
            bColor.setStyle("-fx-background-color:"+rgb+";");
        });
    }
    private void unusedState(){
        viewPencil.setIcon(FontAwesomeIcon.PENCIL);
        ap.setId("trans");
        ap.setStyle("-fx-background-color:transparent;");
        colorState = 1;
        is_drawing = false;
        bPencil.setId("bPencilActive");
        bPencil.setTooltip(new Tooltip("Start drawing"));
    }
    private void bClearProperties(){
        bClear.setOnAction((e) -> {
            graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        });
    }
    
    /* Legacy*/
    private void buttonChildAnimate(boolean out){
        double c_point = 95;
        if (out) {
            bPpt.setVisible(out);
            new PathTransition(bPpt, c_point-88, c_point-20, 0, 0, out).play();
            new PathTransition(bBoard, c_point-126, c_point-33, 0, 0, out).play();
            new PathTransition(bPencil, c_point-155, c_point-62, 0, 0, out).play();
            new PathTransition(bColor, c_point-191, c_point-45, 0, 0, out).play();
            new PathTransition(bClear, c_point-164, c_point-102, 0, 0, out).play();
        }else{
            PathTransition path = 
                    new PathTransition(bPpt, 0, 0,c_point-88, c_point-20, out);
            path.setOnFinished((e) -> { bPpt.setVisible(false); });
            path.play();
            new PathTransition(bBoard, 0, 0, c_point-126, c_point-33, out).play();
            new PathTransition(bPencil, 0, 0, c_point-155, c_point-62, out).play();
            new PathTransition(bColor, 0, 0, c_point-191, c_point-45, out).play();
            new PathTransition(bClear, 0, 0, c_point-164, c_point-102, out).play();
        }
        
    }
    private void checkButtonsPosition(double posX, double posY){
        Dimension scSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = scSize.getWidth()/2;
        double height = scSize.getHeight()/2;
        double rotate = 0;
        if (posX < width && posY < height) {
            rotate = 90;
        }else if (posX < width && posY > height) {
            rotate = 0;
        }else if (posX > width && posY < height) {
            rotate = 180;
        }else if (posX > width && posY > height) {
            rotate = 270;
        }
        paneButtons.setRotate(rotate);
        bMenu.setRotate(360-rotate);
        bPpt.setRotate(360-rotate);
        bBoard.setRotate(360-rotate);
        bPencil.setRotate(360-rotate);
        bColor.setRotate(360-rotate);
        bClear.setRotate(360-rotate);
    }
    private void canvasProperties(){
        is_drawing = false;
        is_out = false;
        colorState =1;
        graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setStroke(cpPencil.getValue());
        canvas.widthProperty().bind(ap.widthProperty());
        canvas.heightProperty().bind(ap.heightProperty());
        canvas.setOnMouseDragged((e) -> {
            if (is_drawing) {
                lastX = e.getX();
                lastY = e.getY();
                graphicsContext.setLineWidth(4);
                graphicsContext.strokeLine(oldX, oldY, lastX, lastY);
                oldX = lastX;
                oldY = lastY;
            }
        });
        canvas.setOnMousePressed((e) -> {
            if (is_drawing) { oldX = e.getX(); oldY = e.getY(); }
        });
    }

    
    
}
