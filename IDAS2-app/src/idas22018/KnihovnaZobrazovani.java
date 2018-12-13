package idas22018;

import static idas22018.IDAS22018.stageP;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class KnihovnaZobrazovani {
    static KnihovnaZobrazovani kZ = null; 
    private KnihovnaZobrazovani(){}
    
    /**
     * 
     * @return 
     */
    public static synchronized KnihovnaZobrazovani getKnihovnaZobrazovani(){
        if(kZ == null){
            kZ = new KnihovnaZobrazovani();
        }
        return kZ;
    }
    
    /**
     * 
     */
    public void zobrazPrehledZamestnancu() {
        zobrazPrehledZamestnancu(IDAS22018.mainScene);
    }
    
    /**
     * 
     * @param predchoziScena 
     */
    public void zobrazPrehledZamestnancu(Scene predchoziScena) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLVyucujici.fxml"));
            root = loader.load();
            FXMLVyucujiciController controller = loader.<FXMLVyucujiciController>getController();
            controller.setSkrytVeci(true);
            Scene scena = new Scene(root);
            controller.setScenes(predchoziScena, scena);
            stageP.setScene(scena);
            stageP.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     */
    public void zobrazPrihlaseni(){
        Stage stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPrihlaseni.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Přihlášení");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(IDAS22018.mainScene.getWindow());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     */
    public void zobrazHlavniMenu(){
        stageP.setScene(IDAS22018.mainScene);
        stageP.show();
    }
    
    /**
     * 
     */
    public void zobrazPrehledUcitelu(){
        KnihovnaZobrazovani.this.zobrazPrehledUcitelu(null, null, IDAS22018.mainScene);
    }
    
    /**
     * 
     * @param predchoziScena 
     */
    public void zobrazPrehledUcitelu(Scene predchoziScena){
        KnihovnaZobrazovani.this.zobrazPrehledUcitelu(null, null, predchoziScena);
    }
    
    /**
     * 
     * @param katedraFiltr
     * @param predmetFiltr
     * @param predchoziScena 
     */
    public void zobrazPrehledUcitelu(String katedraFiltr, String predmetFiltr, Scene predchoziScena){
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLVyucujici.fxml"));
            root = loader.load();
            FXMLVyucujiciController controller = loader.<FXMLVyucujiciController>getController();
            controller.setKatedraFiltr(katedraFiltr);
            controller.setPredmetFiltr(predmetFiltr);
            Scene scena = new Scene(root);
            controller.setScenes(predchoziScena, scena);
            stageP.setScene(scena);
            stageP.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     */
    public void zobrazPrehledPredmetu(){
        zobrazPrehledPredmetu(null, IDAS22018.mainScene);
    }
    
    /**
     * 
     * @param predchoziScena 
     */
    public void zobrazPrehledPredmetu(Scene predchoziScena){
        zobrazPrehledPredmetu(null, predchoziScena);
    }
    
    /**
     * 
     * @param iDVyucujiciho 
     */
    public void zobrazPrehledPredmetu(String iDVyucujiciho){
        zobrazPrehledPredmetu(iDVyucujiciho, IDAS22018.mainScene);
    }
    
    /**
     * 
     * @param iDVyucujiciho
     * @param aktScena 
     */
    public void zobrazPrehledPredmetu(String iDVyucujiciho, Scene aktScena){
        Parent root;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPredmety.fxml"));
                root = loader.load();
                FXMLPredmetyController controller = loader.<FXMLPredmetyController>getController();
                controller.setVyucId(iDVyucujiciho);
                Scene scena = new Scene(root);
                controller.setScenes(aktScena, scena);
                stageP.setScene(scena);
                stageP.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    
    /**
     * 
     */
    public void zobrazPrehledPracovist(){
        zobrazPrehledPracovist(IDAS22018.mainScene);
    }
    
    /**
     * 
     * @param predchoziScena 
     */
    public void zobrazPrehledPracovist(Scene predchoziScena){
       Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPracoviste.fxml"));
            root = loader.load();
            FXMLPracovisteController controller = loader.<FXMLPracovisteController>getController();

            Scene scena = new Scene(root);
            controller.setScenes(predchoziScena, scena);
            stageP.setScene(scena);
            stageP.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     */
    public void zobrazPrehledOboru(){
       zobrazPrehledOboru(IDAS22018.mainScene);
    }
    
    /**
     * 
     * @param predchoziScena 
     */
    public void zobrazPrehledOboru(Scene predchoziScena){
       Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLObory.fxml"));
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
    
    /**
     * 
     */
    public void zobrazRozvrhoveAkce(){
        zobrazRozvrhoveAkce(null, null, null, IDAS22018.mainScene);
    }
    
    /**
     * 
     * @param predchoziScena 
     */
    public void zobrazRozvrhoveAkce(Scene predchoziScena){
        zobrazRozvrhoveAkce(null, null, null, predchoziScena);
    }
    
    /**
     * 
     * @param vyucID
     * @param subjID
     * @param roomID
     * @param predchoziScena 
     */
    public void zobrazRozvrhoveAkce(String vyucID, String subjID, String roomID, Scene predchoziScena){
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLRozvrhoveAkce.fxml"));
            root = fxmlLoader.load();
            FXMLRozvrhoveAkceController controller = fxmlLoader.<FXMLRozvrhoveAkceController>getController();
            controller.setVyucId(vyucID);
            controller.setSubjId(subjID);
            controller.setRoomId(roomID);
            Scene scena = new Scene(root);
            controller.setScenes(predchoziScena, scena);
            stageP.setScene(scena);
            stageP.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @param vyucID
     * @param predchoziScena 
     */
    public void zobrazRozvrhoveAkceVlastni(String vyucID, Scene predchoziScena){
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLRozvrhoveAkce.fxml"));
            root = fxmlLoader.load();
            FXMLRozvrhoveAkceController controller = fxmlLoader.<FXMLRozvrhoveAkceController>getController();
            controller.setVlastni(true);
            controller.setVyucId(vyucID);
            Scene scena = new Scene(root);
            controller.setScenes(predchoziScena, scena);
            stageP.setScene(scena);
            stageP.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /*public void zobrazVazbyOborPredmet(String oborID){
        zobrazVazbyOborPredmet(oborID, null, IDAS22018.mainScene);
    }*/
    
    /**
     * 
     * @param oborID
     * @param predmetID
     * @param predchoziScena
     */
    public void zobrazVazbyOborPredmet(String oborID, String predmetID, Scene predchoziScena){
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLOborPredmet.fxml"));
            root = fxmlLoader.load();
            FXMLOborPredmetController controller = fxmlLoader.<FXMLOborPredmetController>getController();

            controller.setFilterId(oborID);
            controller.setSubjId(predmetID);

            Scene scena = new Scene(root);
            controller.setScenes(predchoziScena, scena);
            stageP.setScene(scena);
            stageP.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void zobrazPrehledUceben() {
        zobrazPrehledUceben(IDAS22018.mainScene);
    }
    
    public void zobrazPrehledUceben(Scene predchoziScena){
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLUcebna.fxml"));
            root = loader.load();
            FXMLUcebnaController controller = loader.<FXMLUcebnaController>getController();
            Scene scena = new Scene(root);
            controller.setScenes(IDAS22018.mainScene, scena);
            stageP.setScene(scena);
            stageP.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void zobrazImport() {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLImport.fxml"));
            root = loader.load();
            FXMLImportController controller = loader.<FXMLImportController>getController();
            Scene scena = new Scene(root);
            controller.setScenes(IDAS22018.mainScene, scena);
            stageP.setScene(scena);
            stageP.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
