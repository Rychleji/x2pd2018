package idas22018.dialogy;

import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 *
 * @author David
 */
public class DialogZobrazRAGraficky extends Stage {

    public DialogZobrazRAGraficky(Window okno, List<String> seznamPredmetu) {

        setTitle("Rozvrhová akce");

        initStyle(StageStyle.UTILITY);
        initModality(Modality.WINDOW_MODAL);
        initOwner(okno);
        setWidth(1650);
        setHeight(540);
        setScene(vytvorScenu(seznamPredmetu));
        this.setResizable(false);
    }

    private Scene vytvorScenu(List<String> seznamPredmetu) {
        HBox box = new HBox();
        box.setAlignment(Pos.TOP_CENTER);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setGridLinesVisible(true);

        int zac = 7;
        int kon = 8;
        for (int i = 0; i < 13; i++) {
            Pane pane = new Pane();
            pane.setPrefWidth(140);
            pane.setPrefHeight(60);
            pane.setBackground(new Background(new BackgroundFill(Color.GREY, null, null)));
            if (i >= 1) {
                Label label = new Label((zac + i) + " - " + (kon + i));
                label.setFont(new Font("Serif", 25));
                pane.getChildren().add(label);
            }
            grid.add(pane, i, 0);
        }

        for (int i = 1; i < 8; i++) {
            Pane pane = new Pane();
            pane.setPrefWidth(140);
            pane.setPrefHeight(60);
            pane.setBackground(new Background(new BackgroundFill(Color.GREY, null, null)));
            grid.add(pane, 0, i);
            switch (i) {
                case 1:
                    pane.getChildren().add(vratFormat("Pondělí", 25));
                    break;
                case 2:
                    pane.getChildren().add(vratFormat("Úterý", 25));
                    break;
                case 3:
                    pane.getChildren().add(vratFormat("Středa", 25));
                    break;
                case 4:
                    pane.getChildren().add(vratFormat("Čtvrtek", 25));
                    break;
                case 5:
                    pane.getChildren().add(vratFormat("Pátek", 25));
                    break;
                case 6:
                    pane.getChildren().add(vratFormat("Sobota", 25));
                    break;
                case 7:
                    pane.getChildren().add(vratFormat("Neděle", 25));
                    break;
            }

            vytvorLegendu(grid);
        }

        int pocetRA = seznamPredmetu.size() / 6;
        for (int i = 0; i < pocetRA; i++) {

            String denVTydnu = seznamPredmetu.remove(5);
            switch (denVTydnu) {
                case "Pondělí":
                    zobrazVgridu(seznamPredmetu, grid, 1);
                    break;
                case "Úterý":
                    zobrazVgridu(seznamPredmetu, grid, 2);
                    break;
                case "Středa":
                    zobrazVgridu(seznamPredmetu, grid, 3);
                    break;
                case "Čtvrtek":
                    zobrazVgridu(seznamPredmetu, grid, 4);
                    break;
                case "Pátek":
                    zobrazVgridu(seznamPredmetu, grid, 5);
                    break;
                case "Sobota":
                    zobrazVgridu(seznamPredmetu, grid, 6);
                    break;
                case "Neděle":
                    zobrazVgridu(seznamPredmetu, grid, 7);
                    break;
            }
        }

        box.getChildren().add(grid);
        return new Scene(box);
    }

    private void vytvorLegendu(GridPane grid) {
        Pane infoPane = new Pane();
        infoPane.setBackground(new Background(new BackgroundFill(Color.BLUEVIOLET, null, null)));
        infoPane.getChildren().add(new Label("Přednáška"));
        infoPane.setPrefWidth(140);
        infoPane.setPrefHeight(60);
        grid.add(infoPane, 0, 8);
        Pane infoPane1 = new Pane();
        infoPane1.setBackground(new Background(new BackgroundFill(Color.DARKGREEN, null, null)));
        infoPane1.getChildren().add(new Label("Cvičení"));
        infoPane1.setPrefWidth(140);
        infoPane1.setPrefHeight(60);
        grid.add(infoPane1, 1, 8);
        Pane infoPane2 = new Pane();
        infoPane2.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        infoPane2.getChildren().add(new Label("Seminář"));
        infoPane2.setPrefWidth(140);
        infoPane2.setPrefHeight(60);
        grid.add(infoPane2, 2, 8);
        Pane infoPane3 = new Pane();
        infoPane3.setBackground(new Background(new BackgroundFill(Color.DARKGOLDENROD, null, null)));
        infoPane3.getChildren().add(new Label("Jiné"));
        infoPane3.setPrefWidth(140);
        infoPane3.setPrefHeight(60);
        grid.add(infoPane3, 3, 8);
    }

    private Label vratFormat(String text, int velikost) {
        Label label = new Label(text);
        label.setFont(new Font("Serif", velikost));

        return label;
    }

    private void zobrazVgridu(List<String> seznamPredmetu, GridPane grid, int radek) {
        Pane pane = new Pane();

        String nazevPredmetu = seznamPredmetu.remove(0);
        int sloupec = Integer.parseInt(seznamPredmetu.remove(0)) - 7;
        int pocetHodin = Integer.parseInt(seznamPredmetu.remove(0));
        String zpusob = seznamPredmetu.remove(0);
        String ucebna = seznamPredmetu.remove(0);

        pane.setBackground(new Background(new BackgroundFill(vratBarvuPodleDruhuPredmetu(zpusob), null, null)));
        pane.setPrefWidth(140);
        pane.setPrefHeight(60);
        pane.getChildren().add(vratFormat(nazevPredmetu + "\n" + ucebna, 16));
        grid.add(pane, sloupec, radek);
        if (pocetHodin > 1) {
            for (int j = 1; j <= pocetHodin - 1; j++) {
                Pane p = new Pane();
                p.setPrefWidth(140);
                p.setPrefHeight(60);
                p.setBackground(new Background(new BackgroundFill(vratBarvuPodleDruhuPredmetu(zpusob), null, null)));
                int poz = sloupec + j;
                grid.add(p, poz, radek);
            }
        }
    }

    private Color vratBarvuPodleDruhuPredmetu(String druh) {
        switch (druh) {
            case "Přednáška":
                return Color.BLUEVIOLET;
            case "Cvičení":
                return Color.DARKGREEN;
            case "Seminář":
                return Color.BLUE;
            case "Jiné":
                return Color.DARKGOLDENROD;
        }
        return null;
    }
}
