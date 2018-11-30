package idas22018;

import static idas22018.IDAS22018.prihlaseno;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class FXMLPrihlaseniController implements Initializable {

    @FXML
    private Button prihlasitButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prihlaseno = true;
    }

    @FXML
    private void administratorClick(ActionEvent event) {
        zavrit(IDAS22018.RezimProhlizeni.ADMINISTRATOR); //TODO - po debugu skrýt
    }

    @FXML
    private void registrovanyClick(ActionEvent event) {
        zavrit(IDAS22018.RezimProhlizeni.REGISTROVANY); //TODO - po debugu skrýt
    }

    @FXML
    private void neregistrovanyClick(ActionEvent event) {
        zavrit(IDAS22018.RezimProhlizeni.NEREGISTROVANY);
    }
    
    private void zavrit(IDAS22018.RezimProhlizeni druhProhlizeni){
        IDAS22018.druhProhlizeni = druhProhlizeni;
        IDAS22018.mainController.panelProRegistrovaneStatus(druhProhlizeni);
        IDAS22018.mainController.controlsProVyucujici("VYUČUJÍCÍ"); //TODO
        Stage stage = (Stage) prihlasitButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    @FXML
    private void prihlasitClick(ActionEvent event) {
        //TODO - vyhledání uživatelského jména v údajích a kontrola správnosti hesla
        //TODO - předání id přihlášeného zaměstnance do proměnné pro další práci (úprava sebe sama, úprava svých RA, ...)
        //TODO z id zamestnance zjistit opravneni zamestnance (registrovany/administrator)
    }

}
