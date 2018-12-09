/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas22018;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

/**
 *
 * @author David
 */
public class CsvReader {

    String csvFile = "seznamPracovist.csv";
    String csvFile2 = "seznamPracovist2.csv";
    String line = "";
    String cvsSplitBy = ";";
    int delka = 0;

    public void importuj() throws FileNotFoundException, UnsupportedEncodingException {
        Writer out = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(
                        "seznamPracovist2.csv"), "UTF-8"));

///////////////////////////////////////////new InputStreamReader(
  //  new FileInputStream("DirectionResponse.xml"), "UTF-8")
        ObservableList<String[]> list = FXCollections.observableArrayList();
      try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile),"windows-1250"))){
        //try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        

            while ((line = br.readLine()) != null) {
                out.write(line);
                out.write("\n");
            }

            br.close();
            out.close();

//            BufferedReader c = new BufferedReader(new FileReader(csvFile2));
//
//            while ((line = br.readLine()) != null) {
////
//                String[] country = line.split(cvsSplitBy);
//                delka = country.length;
//
//                list.add(country);
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
//
        for (String[] s : list) {
            for (int i = 0; i < delka; i++) {
                System.out.print(s[i]);
            }
            System.out.println("");
        }
    }
}
