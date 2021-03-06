package idas22018;

import idas22018.dialogy.DialogChyba;
import idas22018.dialogy.DialogCommitRollback;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class IDAS22018 extends Application {

    /**
     *verze programu
     */
    public static final String VERSION = "2.0.0";
    public static final String PROGRAMNAME = "Database Editor - v." + VERSION;
    public static Stage stageP;
    public static Scene mainScene;
    public static GuiFXMLController mainController;
    public static ResultSet dataset;
    public static boolean prihlaseno = false;
    public static RezimProhlizeni druhProhlizeni = RezimProhlizeni.NEREGISTROVANY;
    public static int idPrihlasenehoZamestnance;
    
    public static boolean prejdiZOknaBezCommitu(){
        boolean ukoncit = true;
        Connection conn = GuiFXMLController.getDataLayer().getConnect();
        
        DialogCommitRollback dialog = new DialogCommitRollback(null);
        dialog.showAndWait();
       
        try {
            switch(dialog.getVysl()){
                case COMMIT:
                    conn.commit();
                    break;
                case ROLLBACK:
                    conn.rollback();
                    break;
                case CANCEL:
                    ukoncit = false;
                    break;
            }
        } catch (SQLException ex) {
            ukoncit = false;
            DialogChyba dialog2 = new DialogChyba(null, ex.getMessage());
            dialog2.showAndWait();
        }
        return ukoncit;
    }
    
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
    
    public enum RezimProhlizeni{
        ADMINISTRATOR("Administrátor"), REGISTROVANY("Registrovaný"), NEREGISTROVANY("Neregistrovaný");
        
        private final String nazev;
        
        private RezimProhlizeni(String nazev){
            this.nazev = nazev;
        }

        @Override
        public String toString() {
            return nazev;
        }
    }
    
}
