package fi.jyu.ohj2.rikantos.kirjasto.controller;

import fi.jyu.ohj2.rikantos.kirjasto.model.Kirja;
import fi.jyu.ohj2.rikantos.kirjasto.model.Kirjakokoelma;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class KirjalistaController implements Initializable {

    @FXML
    private TableColumn<Kirja, String> isbnColumn;

    @FXML
    private TextField isbnTxt;

    @FXML
    private TableView<Kirja> kirjaTaulu;

    @FXML
    private Button lisaaKirja;

    @FXML
    private TextField nimiTxt;

    @FXML
    private Button poistaKirja;

    @FXML
    private Button siirryLainaamaan;

    @FXML
    private TextField tekijaTxt;

    @FXML
    void handleLisaaKirja(MouseEvent event) {
        lisaaKirja();
    }

    @FXML
    void handlePoistaKirja(MouseEvent event) {

    }

    @FXML
    void handleSiirryLainauksiin(MouseEvent event) {
         sulje();
    }

    private Kirjakokoelma kirjakokoelma = new Kirjakokoelma();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SortedList<Kirja> kirjatLajiteltu = kirjakokoelma.getKirjat().sorted(Comparator.comparing(Kirja::getTekija));
        kirjaTaulu.setItems(kirjatLajiteltu);
        kirjaTaulu.setEditable(true);
        
        TableColumn<Kirja, String> nimiSarake = new TableColumn<>("Nimi");
        nimiSarake.setCellValueFactory(cd -> cd.getValue().nimiProperty());
        kirjaTaulu.getColumns().add(nimiSarake);

        TableColumn<Kirja, String> tekijaSarake = new TableColumn<>("Tekija");
        tekijaSarake.setCellValueFactory(cd -> cd.getValue().tekijaProperty());
        kirjaTaulu.getColumns().add(tekijaSarake);

        TableColumn<Kirja, String> isbnSarake = new TableColumn<>("ISBN");
        isbnSarake.setCellValueFactory(cd -> cd.getValue().isbnProperty());
        kirjaTaulu.getColumns().add(isbnSarake);

        TableColumn<Kirja, Boolean> lainattuSarake = new TableColumn<>("Lainattu");
        lainattuSarake.setCellValueFactory(cd -> cd.getValue().lainattuProperty());
        kirjaTaulu.getColumns().add(lainattuSarake);

        kirjaTaulu.setRowFactory(kirja -> {
            TableRow<Kirja> row = new TableRow<>();

            return row;
        });

        kirjakokoelma.lataa();

        lisaaKirja.setOnAction(event -> lisaaKirja());
    }

    private void lisaaKirja() {
        String nimi = nimiTxt.getText();
        String tekija = tekijaTxt.getText();
        String isbn = isbnTxt.getText();

        if (nimi == null || nimi.isBlank()) {
            nimiTxt.requestFocus();
            return;
        }
        if (tekija == null || tekija.isBlank()) {
            tekijaTxt.requestFocus();
            return;
        }
        if (isbn == null || isbn.isBlank()) {
            isbnTxt.requestFocus();
            return;
        }

        kirjakokoelma.lisaaKirja(nimi, tekija, isbn);

        nimiTxt.clear();
        tekijaTxt.clear();
        isbnTxt.clear();
    }

    private void sulje() {
        // Haetaan Scene-olio kirjalista komponentista
        Scene scene = isbnTxt.getScene();
        Stage ikkuna = (Stage) scene.getWindow();
        ikkuna.close();
    }
}
