package idas22018;

import static idas22018.IDAS22018.prihlaseno;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class FXMLPrihlaseniController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prihlaseno = true;
    }

    @FXML
    private void administratorClick(ActionEvent event) {
        zavrit(IDAS22018.RezimProhlizeni.ADMINISTRATOR);
    }

    @FXML
    private void registrovanyClick(ActionEvent event) {
        zavrit(IDAS22018.RezimProhlizeni.REGISTROVANY);
    }

    @FXML
    private void neregistrovanyClick(ActionEvent event) {
        zavrit(IDAS22018.RezimProhlizeni.NEREGISTROVANY);
    }
    
    private void zavrit(IDAS22018.RezimProhlizeni druhProhlizeni){
        KnihovnaZobrazovani.getKnihovnaZobrazovani().zobrazHlavniMenu(); // stejné jako cancel(IDAS22018.mainScene)
        IDAS22018.druhProhlizeni = druhProhlizeni;
    }

}
