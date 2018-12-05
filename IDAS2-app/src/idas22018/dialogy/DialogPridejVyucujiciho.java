/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas22018.dialogy;

import idas22018.GuiFXMLController.HelpClass;
import idas22018.IDAS22018;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 *
 * @author Radim Nyč
 */
public class DialogPridejVyucujiciho extends Stage {

    private boolean buttonPressed = false;

    private String jmeno, prijmeni, titulP, titulZ, email, zkratkaKatedry, uJmeno, uHeslo;
    
    private int role, opravneni, telefon, mobil;

    public boolean isButtonPressed() {
        return buttonPressed;
    }

    public String getJmeno() {
        return jmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public String getTitulP() {
        return titulP;
    }

    public String getTitulZ() {
        return titulZ;
    }

    public int getTelefon() {
        return telefon;
    }

    public int getMobil() {
        return mobil;
    }

    public String getEmail() {
        return email;
    }

    public String getZkratkaKatedry() {
        return zkratkaKatedry;
    }

    public String getuJmeno() {
        return uJmeno;
    }

    public String getuHeslo() {
        return uHeslo;
    }

    public int getRole() {
        return role;
    }

    public int getOpravneni() {
        return opravneni;
    }
    
    public DialogPridejVyucujiciho(Window okno, boolean jeVyuc, Connection conn) {
        setTitle("Přidej zaměstnance");

        initStyle(StageStyle.UTILITY);
        initModality(Modality.WINDOW_MODAL);
        initOwner(okno);

        setScene(vytvorScenu(jeVyuc, 0, false, conn, false));
    }
    
    public DialogPridejVyucujiciho(Window okno, boolean jeVyuc, int idZam, Connection conn, boolean self) {
        setTitle("Uprav zaměstnance");

        initStyle(StageStyle.UTILITY);
        initModality(Modality.WINDOW_MODAL);
        initOwner(okno);

        setScene(vytvorScenu(jeVyuc, idZam, true, conn, self));
    }

    private Scene vytvorScenu(boolean jeVyuc, int idZam, boolean edit, Connection conn, boolean self) {
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(20);
        
        if(edit){
            try {
                Statement stmt2 = conn.createStatement();
                
                ResultSet rs2 = stmt2.executeQuery("select * from ZAM_VIEW where ID_ZAMESTNANEC = "+idZam);
                rs2.next();
                
                jmeno = rs2.getString("JMENO");
                prijmeni = rs2.getString("PRIJMENI");
                titulP = rs2.getString("TITUL_PRED");
                titulZ = rs2.getString("TITUL_ZA");
                telefon = rs2.getInt("TELEFON");
                mobil = rs2.getInt("MOBIL");
                email = rs2.getString("EMAIL");
                zkratkaKatedry = rs2.getString("KATEDRA_ZKRATKA_KATEDRY");
                
                rs2=stmt2.executeQuery("SELECT * from ZAMESTNANEC WHERE ID_ZAMESTNANEC = "+idZam);
                rs2.next();
                opravneni = rs2.getInt("ID_OPRAVNENI");
                role = rs2.getInt("ID_ROLE");               
            } catch (SQLException ex) {
                Logger.getLogger(DialogPridejVyucujiciho.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Mřížka s TextFieldy a Labely
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        GridPane grid2 = new GridPane();
        grid2.setAlignment(Pos.BOTTOM_CENTER);
        grid2.setPadding(new Insets(10));

        // Komponenty
        TextField jmenoTF = new TextField(jmeno);//textfield
        TextField prijmeniTF = new TextField(prijmeni);//textfield
        TextField titulPTF = new TextField(titulP);//textfield
        TextField titulZTF = new TextField(titulZ);//textfield
        TextField telefonTF = new TextField(String.valueOf(telefon));//textfield
        TextField mobilTF = new TextField(String.valueOf(mobil));//textfield
        TextField emailTF = new TextField(email);//textfield
        ComboBox<String> zkratKatCb = new ComboBox<>();//combobox
        try {
            Statement stmt = conn.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM KATEDRA");
            
            while(rs.next()){
                zkratKatCb.getItems().add(rs.getString("ZKRATKA_KATEDRY"));
            }
            
            if (!edit){
                zkratKatCb.getSelectionModel().selectFirst();
            }else{
                zkratKatCb.getSelectionModel().select(zkratkaKatedry);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DialogPridejVyucujiciho.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        zkratKatCb.setDisable(self);
        ComboBox<HelpClass> opravneniCb = new ComboBox<>(FXCollections.observableArrayList(IDAS22018.mainController.getCiselnikOpravneni().values()));
        opravneniCb.setDisable(self);
        ComboBox<HelpClass> roleCb = new ComboBox<>(FXCollections.observableArrayList(IDAS22018.mainController.getCiselnikRole().values()));
        roleCb.setDisable(self);
        
        if(!edit){
            opravneniCb.getSelectionModel().selectFirst();
            roleCb.getSelectionModel().selectFirst();
        }else{
            opravneniCb.getSelectionModel().select(IDAS22018.mainController.getCiselnikOpravneni().get(opravneni));
            roleCb.getSelectionModel().select(IDAS22018.mainController.getCiselnikRole().get(role));
        }
        
        TextField uJmenoTF = new TextField();
        uJmenoTF.setDisable(self);
        PasswordField uHesloPF = new PasswordField();
        uHesloPF.setDisable(self);
        opravneniCb.setOnAction((event) -> {
            uJmenoTF.setDisable(self || opravneniCb.getValue().getString().equalsIgnoreCase(idas22018.IDAS22018.RezimProhlizeni.NEREGISTROVANY.toString()));
            uHesloPF.setDisable(self || uJmenoTF.disableProperty().getValue());
        });
        
        Button buttPhoto = new Button("Fotka");
        buttPhoto.setOnAction((event) -> {
            DialogFotka dialog = new DialogFotka(this, idZam);
            dialog.showAndWait();
        });
        buttPhoto.setDisable(!edit);

        grid.add(new Label("Jméno:"), 0, 0);
        grid.add(jmenoTF, 1, 0);
        grid.add(new Label("Příjmení:"), 0, 1);
        grid.add(prijmeniTF, 1, 1);
        grid.add(new Label("Titul před:"), 0, 2);
        grid.add(titulPTF, 1, 2);
        grid.add(new Label("Titul za:"), 0, 3);
        grid.add(titulZTF, 1, 3);
        grid.add(new Label("Telefon:"), 0, 4);
        grid.add(telefonTF, 1, 4);
        grid.add(new Label("Mobil:"), 0, 5);
        grid.add(mobilTF, 1, 5);
        grid.add(new Label("e-mail:"), 0, 6);
        grid.add(emailTF, 1, 6);
        grid.add(new Label("Zkratka katedry:"), 0, 7);
        grid.add(zkratKatCb, 1, 7);
        grid.add(new Label("Oprávnění:"), 0, 8);
        grid.add(opravneniCb, 1, 8);
        grid.add(new Label("Role:"), 0, 9);
        grid.add(roleCb, 1, 9);
        grid.add(new Label("Uživatelské jméno:"), 0, 10);
        grid.add(uJmenoTF, 1, 10);
        grid.add(new Label("Heslo:"), 0, 11);
        grid.add(uHesloPF, 1, 11);
        grid.add(new Label("Foto:"),0, 12);
        grid.add(buttPhoto, 1, 12);

        // Tlačítko
        Button tlacitko1 = new Button("Vlož");

        grid2.add(tlacitko1, 0, 0);

        tlacitko1.setOnAction((ActionEvent e) -> {
            try {
                jmeno = jmenoTF.getText();
                prijmeni = prijmeniTF.getText();
                titulP = titulPTF.getText();
                titulZ = titulZTF.getText();
                telefon = Integer.parseInt(telefonTF.getText());
                mobil = Integer.parseInt(mobilTF.getText());
                email = emailTF.getText();
                zkratkaKatedry = zkratKatCb.getValue();
                uJmeno = uJmenoTF.getText().trim();
                uHeslo = uHesloPF.getText().trim();
                role = roleCb.getValue().getId();
                opravneni = opravneniCb.getValue().getId();

                buttonPressed = true;
                hide();
            } catch (IllegalArgumentException ex) {
                DialogChyba dialog3 = new DialogChyba(null, "Špatný formát");
                dialog3 = (DialogChyba) dialog3.getScene().getWindow();
                dialog3.showAndWait();
            }
        });

        box.getChildren().addAll(grid, grid2);
        return new Scene(box);
    }
}
