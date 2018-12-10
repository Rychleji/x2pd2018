/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas22018;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

/**
 *
 * @author David
 */
public class CsvReader {

    private String line = "";
    private String cvsSplitBy = ";";
    private ObservableList<String[]> list = FXCollections.observableArrayList();

    public ObservableList<String[]> importuj() throws FileNotFoundException, UnsupportedEncodingException {

        FileChooser fileChoicer = new FileChooser();
        fileChoicer.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("csv", "*.csv"),
                new FileChooser.ExtensionFilter("xls(x)", ".xlsx", ".xls"),
                new FileChooser.ExtensionFilter("json", "*.json")
        );
        fileChoicer.setTitle("Zvol soubor");
        File file = fileChoicer.showOpenDialog(null);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "windows-1250"))) {

            while ((line = br.readLine()) != null) {
                String[] pracoviste = line.split(cvsSplitBy);

                list.add(pracoviste);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
