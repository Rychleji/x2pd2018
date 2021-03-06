package idas22018.dialogy;

import idas22018.GuiFXMLController;
import idas22018.IDAS22018;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class DialogPridejPredmet extends Stage {

    private boolean buttonPressed = false;

    int rocnik = 1, semestr, zpusobZak, forma;
    
    private String zkratka = "", nazev = "";

    public boolean isButtonPressed() {
        return buttonPressed;
    }

    public int getRocnik() {
        return rocnik;
    }

    public int getSemestr() {
        return semestr;
    }

    public int getZpusobZak() {
        return zpusobZak;
    }

    public int getForma() {
        return forma;
    }

    public String getZkratka() {
        return zkratka;
    }

    public String getNazev() {
        return nazev;
    }

    public DialogPridejPredmet(Window okno, Connection conn, String zkr) {
        setTitle("Předměty");

        initStyle(StageStyle.UTILITY);
        initModality(Modality.WINDOW_MODAL);
        initOwner(okno);

        setScene(vytvorScenu(conn, zkr));
    }

    private Scene vytvorScenu(Connection conn, String zkr) {
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(20);

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
        
        ObservableList<GuiFXMLController.HelpClass> list1 = FXCollections.observableArrayList(IDAS22018.mainController.getCiselnikFormaVyuky().values());
        ObservableList<GuiFXMLController.HelpClass> list2 = FXCollections.observableArrayList(IDAS22018.mainController.getCiselnikZpusobZak().values());  
        ObservableList<GuiFXMLController.HelpClass> list3 = FXCollections.observableArrayList(IDAS22018.mainController.getCiselnikSemestr().values());
               
        ComboBox<GuiFXMLController.HelpClass> formaCB = new ComboBox<>(list1);
        ComboBox<GuiFXMLController.HelpClass> zpusobCB = new ComboBox<>(list2);
        ComboBox<GuiFXMLController.HelpClass> semestrCB = new ComboBox<>(list3);
        
        formaCB.getSelectionModel().selectFirst();
        zpusobCB.getSelectionModel().selectFirst();
        semestrCB.getSelectionModel().selectFirst();
        
        if(conn!=null){
            try{
                zkratka = zkr;
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * from PREDMET_EXT_VIEW where ZKRATKA_PREDMETU = '" + zkr+"'");
                
                rs.next();
                
                nazev = rs.getString("NAZEV_PREDMETU");
                rocnik = rs.getInt("DOPORUCENY_ROCNIK");
                
                formaCB.getSelectionModel().select(IDAS22018.mainController.getCiselnikFormaVyuky().get(rs.getInt("FORMA_VYUKY_ID_FV")));
                zpusobCB.getSelectionModel().select(IDAS22018.mainController.getCiselnikZpusobZak().get(rs.getInt("ZPUSOB_ZAKONCENI_ID_ZZ")));
                semestrCB.getSelectionModel().select(IDAS22018.mainController.getCiselnikSemestr().get(rs.getInt("SEMESTR_ID_SEMESTR")));
            }catch (Exception e){
                
            }
        }
        
        TextField zkratkaTF = new TextField(zkratka);
        TextField nazevTF = new TextField(nazev);
        Spinner<Integer> rocnikSn = new Spinner<>(1, 3, rocnik);
        
        

        grid.add(new Label("Zkratka předmětu:"), 0, 0);
        grid.add(zkratkaTF, 1, 0);
        grid.add(new Label("Název předmětu:"), 0, 1);
        grid.add(nazevTF, 1, 1);
        grid.add(new Label("Doporučený ročník:"), 0, 2);
        grid.add(rocnikSn, 1, 2);
        grid.add(new Label("Forma:"), 0, 3);
        grid.add(formaCB, 1, 3);
        grid.add(new Label("Zakončení:"), 0, 4);
        grid.add(zpusobCB, 1, 4);
        grid.add(new Label("Semestr:"), 0, 5);
        grid.add(semestrCB, 1, 5);

        // Tlačítko
        Button tlacitko1 = new Button("Vlož");

        grid2.add(tlacitko1, 0, 0);

        tlacitko1.setOnAction((ActionEvent e) -> {
            try {
                zkratka = zkratkaTF.getText();
                nazev = nazevTF.getText();
                rocnik = rocnikSn.getValue();
                forma = formaCB.getValue().getId();
                semestr = semestrCB.getValue().getId();
                zpusobZak =  zpusobCB.getValue().getId();

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
