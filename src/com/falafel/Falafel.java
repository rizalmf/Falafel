/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.falafel;

import static com.falafel.role.PathRole.APP_TITLE;
import static com.falafel.role.PathRole.ICON_PATH;
import static com.falafel.role.PathRole.MAIN_PATH;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author rizal
 */
public class Falafel extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(MAIN_PATH));
        stage.setOnCloseRequest((WindowEvent event) -> {
            System.exit(0);
        });
        stage.getIcons().add(new Image(getClass().getClassLoader()
                .getResourceAsStream(ICON_PATH)));
        stage.setTitle(APP_TITLE);
        stage.setAlwaysOnTop(true);
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setMaximized(true);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
