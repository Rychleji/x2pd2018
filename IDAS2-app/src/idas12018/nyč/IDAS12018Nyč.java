/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas12018.nyč;

import java.sql.ResultSet;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Radim
 */
public class IDAS12018Nyč extends Application {

    /**
     *verze programu
     */
    public static final String VERSION = "1.0.0";
    public static final String PROGRAMNAME = "Database Editor - v." + VERSION;
    public static Stage stageP;
    public static Scene mainScene;
    public static GuiFXMLController mainController;
    public static ResultSet dataset;
    
    public static void close(Scene predchoziScena) {
        stageP.setScene(predchoziScena);
        stageP.show();
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        stageP = stage;
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GuiFXML.fxml"));
        Parent root = fxmlLoader.load();
        mainController = fxmlLoader.<GuiFXMLController>getController();
                        
        mainScene = new Scene(root);
        
        stage.setTitle(PROGRAMNAME);
        
        stage.setScene(mainScene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
