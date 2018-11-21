/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas22018;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author David
 */
public class FXMLZamestnanciController implements Initializable {

    @FXML
    private ImageView imageView;
    @FXML
    private TableView<?> tableView;
    @FXML
    private TableColumn<?, ?> idCol;
    @FXML
    private TableColumn<?, ?> jmenoCol;
    @FXML
    private TableColumn<?, ?> prijmeniCol;
    @FXML
    private TableColumn<?, ?> titulPredCol;
    @FXML
    private TableColumn<?, ?> titulZaCol;
    @FXML
    private TableColumn<?, ?> telefonCol;
    @FXML
    private TableColumn<?, ?> mobilCol;
    @FXML
    private TableColumn<?, ?> emailCol;
    @FXML
    private TableColumn<?, ?> katedraCol;
    @FXML
    private TableColumn<?, ?> fakultaCol;
    @FXML
    private TableColumn<?, ?> role;
    @FXML
    private TableColumn<?, ?> typUzivatele;

    private Scene predScena;
    private Scene aktScena;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void okButtonClick(ActionEvent event) {
    }

    @FXML
    private void cancelButtonClick(ActionEvent event) {
    }

    @FXML
    private void pridejButtonClick(ActionEvent event) {
    }

    @FXML
    private void upravButtonClick(ActionEvent event) {
    }

    @FXML
    private void odeberButtonClick(ActionEvent event) {
    }

    @FXML
    private void pracovisteButtonClick(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLPracoviste.fxml"));
        KnihovnaZobrazovani.zobrazPrehledPracovistBezVyberu(fxmlLoader);
    }

    @FXML
    private void prehledPredmetuButtonClick(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLPredmety.fxml")); //nemuze byt staticke
        KnihovnaZobrazovani.zobrazPrehledPredmetuBezVyberu(fxmlLoader);
    }

    @FXML
    private void oboryButttonClick(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLObory.fxml"));
        KnihovnaZobrazovani.zobrazPrehledOboruBezVyberu(fxmlLoader);
    }

    void setScenes(Scene predScena, Scene aktScena) {
        this.predScena = predScena;
        this.aktScena = aktScena;
    }

    @FXML
    private void vyucujiciButtonClick(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLVyucujici.fxml"));
        KnihovnaZobrazovani.zobrazPredhledUcitelu(fxmlLoader);
    }

    @FXML
    private void rozvrhAkceButtonClick(ActionEvent event) {
    }

}
