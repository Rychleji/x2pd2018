/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas22018;

import datovavrstva.CsvReader;
import datovavrstva.ISkolniDB;
import datovavrstva.XMLLoader;
import static idas22018.IDAS22018.close;
import idas22018.dialogy.DialogChyba;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import javax.xml.bind.JAXBException;

/**
 * FXML Controller class
 *
 * @author Radim
 */
public class FXMLImportController implements Initializable {

    Scene predScena, aktScena;
    @FXML
    private ComboBox<String> priponaCB;
    @FXML
    private ComboBox<String> tabulkaCB;
    @FXML
    private Button souborButton;
    @FXML
    private Button commitButton;
    @FXML
    private Button rollbackButton;
    private ISkolniDB dataLayer;
    private Consumer<String> xmlImport;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataLayer = GuiFXMLController.getDataLayer();

        priponaCB.getItems().addAll("CSV", "XML");
        priponaCB.getSelectionModel().selectFirst();
        priponaCBChange(new ActionEvent(priponaCB, null));
    }

    public void setScenes(Scene mainScene, Scene scena) {
        predScena = mainScene;
        aktScena = scena;
    }

    @FXML
    private void priponaCBChange(ActionEvent event) {
        ComboBox<String> cb = ((ComboBox<String>) event.getSource());
        if (cb.getValue().equalsIgnoreCase("CSV")) {
            tabulkaCB.getItems().clear();
            tabulkaCB.getItems().addAll("Pracoviště");
        } else if (cb.getValue().equalsIgnoreCase("XML")) {
            tabulkaCB.getItems().clear();
            tabulkaCB.getItems().addAll("Předměty");
        }
        tabulkaCB.getSelectionModel().selectFirst();
    }

    @FXML
    private void souborButtonClick(ActionEvent event) {
        if (priponaCB.getValue().equalsIgnoreCase("CSV")) {
            CsvReader s = new CsvReader();
            ObservableList<String[]> list = null;
            try {
                list = s.importuj();
            } catch (UnsupportedEncodingException | FileNotFoundException ex) {
                DialogChyba dialog = new DialogChyba(null, ex.getMessage());
                Logger.getLogger(FXMLImportController.class.getName()).log(Level.SEVERE, null, ex);
                dialog.showAndWait();
                return;
            }

            list.remove(0);
            for (String[] pole : list) {
                String zkratka = pole[2];
                String nazev = pole[3];
                String zkratkaFakulty = pole[5];
                zkratka = zkratka.replace("\"", ""); //zbavim se uvozovek v csv
                nazev = nazev.replace("\"", "");
                zkratkaFakulty = zkratkaFakulty.replace("\"", "");
                try {
                    dataLayer.addDepartment(zkratka, nazev, zkratkaFakulty);
                } catch (SQLException ex) {
                    Logger.getLogger(FXMLImportController.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                }
            }
        } else if (priponaCB.getValue().equalsIgnoreCase("XML")) {
            FileChooser fileChoicer = new FileChooser();
            fileChoicer.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter(priponaCB.getValue(), "*." + priponaCB.getValue().toLowerCase())
            );
            fileChoicer.setTitle("Zvol soubor");
            File file = fileChoicer.showOpenDialog(priponaCB.getParent().getScene().getWindow());

            xmlImport.accept(file.getAbsolutePath());
        }
    }

    @FXML
    private void commitButtonClick(ActionEvent event) {
        dataLayer.commit();
        close(predScena);
    }

    @FXML
    private void rollbackButtonClick(ActionEvent event) {
        dataLayer.rollback();
        close(predScena);
    }

    @FXML
    private void tabulkaOnChange(ActionEvent event) {
        ComboBox<String> cb = (ComboBox<String>)event.getSource();
        String value = cb.getValue();
        
        if (priponaCB.getValue().equalsIgnoreCase("XML") && value!=null){
            switch(value){
                case "Předměty":
                    xmlImport = (t) -> {
                        try {
                            XMLLoader.importPredmetu(t);
                        } catch (JAXBException | FileNotFoundException ex) {
                            DialogChyba dialog = new DialogChyba(null, ex.getMessage());
                            Logger.getLogger(FXMLImportController.class.getName()).log(Level.SEVERE, null, ex);
                            dialog.showAndWait();
                            return;
                        }
                    };
                    break;
            }
        }
    }

}
