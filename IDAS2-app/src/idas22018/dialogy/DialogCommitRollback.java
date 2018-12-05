/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas22018.dialogy;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 *
 * @author Radim
 */
public class DialogCommitRollback extends Stage {
    public enum CommitRollbackVysledek{
        COMMIT, ROLLBACK, CANCEL;
    }
    private CommitRollbackVysledek vysl = CommitRollbackVysledek.CANCEL;

    public CommitRollbackVysledek getVysl() {
        return vysl;
    }
    
    public DialogCommitRollback(Window okno){
        setTitle("Pozor!");
        setWidth(500);
        setHeight(200);

        initStyle(StageStyle.UTILITY);
        initModality(Modality.APPLICATION_MODAL);
        initOwner(okno);

        setScene(vytvorScenu());

    }

    private Scene vytvorScenu() {
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
        grid2.setHgap(5);
        grid2.setVgap(5);

        // Komponenty
        grid.add(new Label("Na této stránce jsou neuložené změny. Jak si přejete pokračovat?"), 0, 0);

        // Tlačítko
        Button commit = new Button("Commit");
        Button rollback = new Button("Rollback");
        Button cancel = new Button("Cancel");
        
        grid2.add(commit, 0, 0);
        grid2.add(rollback, 1, 0);
        grid2.add(cancel, 2, 0);

        commit.setOnAction((ActionEvent e) -> {
            try {
                vysl = CommitRollbackVysledek.COMMIT;
                hide();
            } catch (IllegalArgumentException ex) {
                System.out.println("Chyba: " + ex.getMessage());
            }
        });
        
        rollback.setOnAction((ActionEvent e) -> {
            try {
                vysl = CommitRollbackVysledek.ROLLBACK;
                hide();
            } catch (IllegalArgumentException ex) {
                System.out.println("Chyba: " + ex.getMessage());
            }
        });
        
        cancel.setOnAction((ActionEvent e) -> {
            try {
                vysl = CommitRollbackVysledek.CANCEL;
                hide();
            } catch (IllegalArgumentException ex) {
                System.out.println("Chyba: " + ex.getMessage());
            }
        });

        box.getChildren().addAll(grid, grid2);
        return new Scene(box);
    }
}
