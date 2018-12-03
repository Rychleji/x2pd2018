package idas22018;

import static idas22018.IDAS22018.prihlaseno;
import idas22018.dialogy.DialogChyba;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLPrihlaseniController implements Initializable {

    @FXML
    private Button prihlasitButton;
    @FXML
    private TextField jmenoTextField;
    @FXML
    private TextField hesloTextField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prihlaseno = true;
    }

    private void administratorClick(ActionEvent event) {//debug metoda - v db je uživatel s přihlašováním admin admin
        zavrit(IDAS22018.RezimProhlizeni.ADMINISTRATOR); 
    }

    private void registrovanyClick(ActionEvent event) {//debug metoda - v db je uživatel s přihlašováním reg reg
        zavrit(IDAS22018.RezimProhlizeni.REGISTROVANY); 
    }

    @FXML
    private void neregistrovanyClick(ActionEvent event) {//prohlížení
        IDAS22018.mainController.controlsProVyucujici("neregistrovaný");
        zavrit(IDAS22018.RezimProhlizeni.NEREGISTROVANY);
    }
    
    private void zavrit(IDAS22018.RezimProhlizeni druhProhlizeni){
        IDAS22018.druhProhlizeni = druhProhlizeni;
        IDAS22018.mainController.panelProRegistrovaneStatus(druhProhlizeni);
        
        Stage stage = (Stage) prihlasitButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    @FXML
    private void prihlasitClick(ActionEvent event) {
        try {
            //CallableStatement vStatement = vDatabaseConnection.prepareCall( "begin ? := javatest( ?, ? ); end;" );
            Connection conn = GuiFXMLController.getDataLayer().getConnect();
            PreparedStatement pstmt = conn.prepareCall("select vratIdPrihlaseni(?, ?) as IDZAM from dual");
            pstmt.setString(1, jmenoTextField.getText().trim());
            pstmt.setString(2, hesloTextField.getText().trim());
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            IDAS22018.idPrihlasenehoZamestnance = rs.getInt("IDZAM");
            
            Statement statement = conn.createStatement();

            rs = statement.executeQuery("select OPRAVNENI, TYPROLE from ZAM_VIEW where ID_ZAMESTNANEC = " + String.valueOf(IDAS22018.idPrihlasenehoZamestnance));
            rs.next();
            
            IDAS22018.RezimProhlizeni rezim = IDAS22018.RezimProhlizeni.NEREGISTROVANY;
            
            for(IDAS22018.RezimProhlizeni hod: IDAS22018.RezimProhlizeni.values()){
                if(hod.toString().equalsIgnoreCase(rs.getString("OPRAVNENI")))
                    rezim = hod;
            }
            
            IDAS22018.mainController.controlsProVyucujici(rs.getString("TYPROLE"));
            
            zavrit(rezim);
        } catch (SQLException ex) {
            DialogChyba dialog = new DialogChyba(null, ex.getMessage());
            dialog.showAndWait();
        }
    }

}
