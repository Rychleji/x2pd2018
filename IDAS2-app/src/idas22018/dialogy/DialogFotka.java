package idas22018.dialogy;

import idas22018.GuiFXMLController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class DialogFotka extends Stage {
      public DialogFotka(Window okno, int idZam) {
        setTitle("Foto");

        initStyle(StageStyle.UTILITY);
        initModality(Modality.WINDOW_MODAL);
        initOwner(okno);

        setScene(vytvorScenu(idZam));

    }

    private Scene vytvorScenu(int idZam) {
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
        Button nahrajBtn = new Button("Nahrát fotku");
        Button smazBtn = new Button("Smazat fotku");
        
        nahrajBtn.setOnAction((event) -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showOpenDialog(null);

            int id = idZam;
            if (file != null) {
                try {
                    InputStream is = new FileInputStream(file);
                    GuiFXMLController.getDataLayer().addPicture(is, id);
                } catch (FileNotFoundException | SQLException ex) {
                    DialogChyba dialog2 = new DialogChyba(null, ex.getMessage());
                    dialog2.showAndWait();
                }
            }
        });
        
        smazBtn.setOnAction((event) -> {
            int id = idZam;

            try {
                GuiFXMLController.getDataLayer().deletePicture(id);
            } catch (SQLException ex) {
                DialogChyba dialog2 = new DialogChyba(null, ex.getMessage());
                dialog2.showAndWait();
            }
        });
        
        grid.add(nahrajBtn, 0, 0);
        grid.add(smazBtn, 0, 1);

        // Tlačítko
        Button tlacitko1 = new Button("OK");

        grid2.add(tlacitko1, 0, 0);

        tlacitko1.setOnAction((ActionEvent e) -> {
            try {
                hide();
            } catch (IllegalArgumentException ex) {
                System.out.println("Chyba: " + ex.getMessage());
            }
        });

        box.getChildren().addAll(grid, grid2);
        return new Scene(box);
    }

}
