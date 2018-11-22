/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas22018;

import static idas22018.IDAS22018.prihlaseno;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author David
 */
public class FXMLPrihlaseniController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
               prihlaseno = true;
    }

    @FXML
    private void administratorClick(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GuiFXML.fxml"));
        KnihovnaZobrazovani.zobrazHlavniMenu(loader);
        IDAS22018.druhProhlizeni = "administrator";
   
    }

    @FXML
    private void registrovanyClick(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GuiFXML.fxml"));
        KnihovnaZobrazovani.zobrazHlavniMenu(loader);
        IDAS22018.druhProhlizeni = "registrovany";
    }

    @FXML
    private void neregistrovanyClick(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GuiFXML.fxml"));
        KnihovnaZobrazovani.zobrazHlavniMenu(loader);
        IDAS22018.druhProhlizeni = "neregistrovany";
    }

}
