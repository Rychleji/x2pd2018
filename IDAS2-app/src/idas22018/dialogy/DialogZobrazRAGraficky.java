package idas22018.dialogy;

import java.sql.Connection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 *
 * @author David
 */
public class DialogZobrazRAGraficky extends Stage{

    public DialogZobrazRAGraficky(Window okno) {
      

        setTitle("Rozvrhová akce");

        initStyle(StageStyle.UTILITY);
        initModality(Modality.WINDOW_MODAL);
        initOwner(okno);

        setScene(vytvorScenu());
        this.setResizable(false);
    }

    private Scene vytvorScenu() {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(20);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        
        return new Scene(box);
    }
}
