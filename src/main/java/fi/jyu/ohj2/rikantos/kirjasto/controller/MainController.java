package fi.jyu.ohj2.rikantos.kirjasto.controller;

import fi.jyu.ohj2.rikantos.kirjasto.App;
import fi.jyu.ohj2.rikantos.kirjasto.model.Kirja;
import fi.jyu.ohj2.rikantos.kirjasto.model.Kirjakokoelma;
import fi.jyu.ohj2.rikantos.kirjasto.model.Lainakokoelma;
import fi.jyu.ohj2.rikantos.kirjasto.model.Lainaus;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements Initializable {
    private Kirjakokoelma kirjakokoelma = new Kirjakokoelma();
    private Lainakokoelma lainakokoelma = new Lainakokoelma();

    @FXML
    private Button lainaaKirjaBtn;

    @FXML
    private TableView<Lainaus> lainaamatTable;

    @FXML
    private TableView<Kirja> lainattavissaTable;

    @FXML
    private Button palautaKirjaBtn;

    @FXML
    private Button siirryKirjaListaanBtn;

    @FXML
    private TextField lainaajanNimiTxt;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Lajitellaan kirjat aina tekijän nimen mukaisesti
        FilteredList<Kirja> kirjatSuodatettu = new FilteredList<>(kirjakokoelma.getKirjat(), kirja -> !kirja.getLainattu());
        SortedList<Kirja> kirjatLajiteltu = kirjatSuodatettu.sorted(Comparator.comparing(Kirja::getTekija));
        lainattavissaTable.setItems(kirjatLajiteltu);
        lainattavissaTable.setEditable(true);

        TableColumn<Kirja, String> nimiSarake = new TableColumn<>("Nimi");
        nimiSarake.setCellValueFactory(cd -> cd.getValue().nimiProperty());
        lainattavissaTable.getColumns().add(nimiSarake);

        TableColumn<Kirja, String> tekijaSarake = new TableColumn<>("Tekija");
        tekijaSarake.setCellValueFactory(cd -> cd.getValue().tekijaProperty());
        lainattavissaTable.getColumns().add(tekijaSarake);

        TableColumn<Kirja, String> isbnSarake = new TableColumn<>("ISBN");
        isbnSarake.setCellValueFactory(cd -> cd.getValue().isbnProperty());
        lainattavissaTable.getColumns().add(isbnSarake);

        TableColumn<Kirja, String> lainattuSarake = new TableColumn<>("Lainattu");
        lainattuSarake.setCellValueFactory(cd -> cd.getValue().lainattuProperty().asString());
        lainattavissaTable.getColumns().add(lainattuSarake);

        lainattavissaTable.setRowFactory(kirja -> {
            TableRow<Kirja> row = new TableRow<>();

            return row;
        });

        // Lajitellaan tässä palautusPvm mukaan
        SortedList<Lainaus> lainauksetLajiteltu = lainakokoelma.getLainaukset().sorted(Comparator.comparing(Lainaus::getPalautusPvm));
        lainaamatTable.setItems(lainauksetLajiteltu);
        lainaamatTable.setEditable(true);

        TableColumn<Lainaus, String> lainattuNimiSarake = new TableColumn<>("Nimi");
        lainattuNimiSarake.setCellValueFactory(cd -> cd.getValue().kirjaProperty().get().nimiProperty());
        lainaamatTable.getColumns().add(lainattuNimiSarake);

        TableColumn<Lainaus, String> lainattuTekijaSarake = new TableColumn<>("Tekija");
        lainattuTekijaSarake.setCellValueFactory(cd -> cd.getValue().kirjaProperty().get().tekijaProperty());
        lainaamatTable.getColumns().add(lainattuTekijaSarake);

        TableColumn<Lainaus, String> lainattuIsbnSarake = new TableColumn<>("ISBN");
        lainattuIsbnSarake.setCellValueFactory(cd -> cd.getValue().kirjaProperty().get().isbnProperty());
        lainaamatTable.getColumns().add(lainattuIsbnSarake);

        TableColumn<Lainaus, LocalDateTime> lainattuPvmSarake = new TableColumn<>("Lainattu");
        lainattuPvmSarake.setCellValueFactory(cd -> cd.getValue().lainattuPvmProperty());
        lainaamatTable.getColumns().add(lainattuPvmSarake);

        TableColumn<Lainaus, LocalDateTime> palautusSarake = new TableColumn<>("Palautus");
        palautusSarake.setCellValueFactory(cd -> cd.getValue().palautusPvmProperty());
        lainaamatTable.getColumns().add(palautusSarake);

        lainaamatTable.setRowFactory(lainaus -> {
            TableRow<Lainaus> row = new TableRow<>();

            return row;
        });

        lainakokoelma.lataa();
        kirjakokoelma.lataa();
    }

    @FXML
    void handleLainaaKirja(MouseEvent event) {
        lainaaKirja();
    }

    @FXML
    void handlePalautaKirja(MouseEvent event) {
        palautaKirja();
    }

    @FXML
    void handleSiirryKirjalistaan(MouseEvent event) {
        try {
            /* 1 */ FXMLLoader loader = new FXMLLoader(App.class.getResource("kirjalista.fxml"));
            /* 1 */ Parent root = loader.load();
            /* 1 */ Scene scene = new Scene(root);

            /* 2 */ Stage dialogi = new Stage();
            /* 2 */ dialogi.setScene(scene);

            /* 3 */ dialogi.setTitle("Kirjalista");
            /* 3 */ dialogi.setMinWidth(400);
            /* 3 */ dialogi.setMinHeight(300);
            /* 3 */ dialogi.initModality(Modality.APPLICATION_MODAL);

            /* 4 */ dialogi.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void poistaValittu() {
        Lainaus valittuLainaus = lainaamatTable.getSelectionModel().getSelectedItem();
        lainakokoelma.poistaLainaus(valittuLainaus);
    }

    private void lainaaKirja() {
        String nimi = lainaajanNimiTxt.getText();
        Kirja valittuKirja = lainattavissaTable.getSelectionModel().getSelectedItem();

        if (valittuKirja == null) {
            return;
        }

        if (nimi == null || nimi.isBlank()) {
            lainaajanNimiTxt.requestFocus();
            return;
        }

        // Asetetaan valittu kirja lainatuksi
        valittuKirja.setLainattu(true);

        // Lisätään valittu kirja ja lainaajan nimi lainakokoelmaan
        lainakokoelma.lisaaLainaus(valittuKirja, nimi);
    }

    private void palautaKirja() {
        Lainaus valittuLainaus = lainaamatTable.getSelectionModel().getSelectedItem();

        if (valittuLainaus == null) {
            return;
        }

        lainakokoelma.poistaLainaus(valittuLainaus);

        // Asetetaan valittu kirja vapaaksi
        valittuLainaus.getKirja().setLainattu(false);
    }
}
