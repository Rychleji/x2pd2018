/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas22018;

import static idas22018.IDAS22018.stageP;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 *
 * @author David
 */
public final class KnihovnaZobrazovani {
    
    private KnihovnaZobrazovani(){
        
    }
    
    static void zobrazPrehledZamestnancu(FXMLLoader loader) {
        Parent root;
        try {
            root = loader.load();
            FXMLZamestnanciController controller = loader.<FXMLZamestnanciController>getController();
            Scene scena = new Scene(root);
            controller.setScenes(IDAS22018.mainScene, scena);
            stageP.setScene(scena);
            stageP.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    static void zobrazPrihlaseni(FXMLLoader loader){
        Parent root;
        try {
            root = loader.load();
            FXMLPrihlaseniController controller = loader.<FXMLPrihlaseniController>getController();
            Scene scena = new Scene(root);
            stageP.setScene(scena);
            stageP.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    static void zobrazHlavniMenu(FXMLLoader loader){
        Parent root;
        try {
            root = loader.load();
            GuiFXMLController controller = loader.<GuiFXMLController>getController();
            Scene scena = new Scene(root);
            stageP.setScene(scena);
            stageP.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void zobrazPredhledUcitelu(FXMLLoader loader){
        Parent root;
        try {
            root = loader.load();
            FXMLVyucujiciController controller = loader.<FXMLVyucujiciController>getController();
            Scene scena = new Scene(root);
            controller.setScenes(IDAS22018.mainScene, scena);
            stageP.setScene(scena);
            stageP.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void zobrazPrehledPredmetuUcitele(String origID, FXMLLoader loader, Scene aktScena){
        Parent root;
            try {
                root = loader.load();
                FXMLPredmetyController controller = loader.<FXMLPredmetyController>getController();
                controller.setVyucId(origID);
                controller.initialize(null, null);
                Scene scena = new Scene(root);
                controller.setScenes(aktScena, scena);
                stageP.setScene(scena);
                stageP.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    
    public static void zobrazPrehledPracovistBezVyberu(FXMLLoader loader){
       Parent root;
        try {
            root = loader.load();
            FXMLPracovisteController controller = loader.<FXMLPracovisteController>getController();

            Scene scena = new Scene(root);
            controller.setScenes(IDAS22018.mainScene, scena);
            stageP.setScene(scena);
            stageP.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     
    public static void zobrazPrehledPredmetuBezVyberu(FXMLLoader loader){
        Parent root;
        try {
            root = loader.load();
            FXMLPredmetyController controller = loader.<FXMLPredmetyController>getController();

            Scene scena = new Scene(root);
            controller.setScenes(IDAS22018.mainScene, scena);
            stageP.setScene(scena);
            stageP.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void zobrazPrehledOboruBezVyberu(FXMLLoader loader){
       Parent root;
        try {
            root = loader.load();
            FXMLOboryController controller = loader.<FXMLOboryController>getController();
            Scene scena = new Scene(root);
            controller.setScenes(IDAS22018.mainScene, scena);
            stageP.setScene(scena);
            stageP.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
