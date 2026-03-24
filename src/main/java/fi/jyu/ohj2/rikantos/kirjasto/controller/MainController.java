package fi.jyu.ohj2.rikantos.kirjasto.controller;

import fi.jyu.ohj2.rikantos.kirjasto.App;
import fi.jyu.ohj2.rikantos.kirjasto.model.Kirja;
import fi.jyu.ohj2.rikantos.kirjasto.model.Kirjakokoelma;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements Initializable {
        @FXML
        private Button lainaaKirjaBtn;

        @FXML
        private TableView<Kirja> lainaamatTable;

        @FXML
        private TableView<Kirja> lainattavissaTable;

        @FXML
        private Button palautaKirjaBtn;

        @FXML
        private Button siirryKirjaListaanBtn;

    private Kirjakokoelma kirjakokoelma = new Kirjakokoelma();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SortedList<Kirja> kirjatLajiteltu = kirjakokoelma.getKirjat().sorted(Comparator.comparing(Kirja::getTekija));
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

        TableColumn<Kirja, Boolean> lainattuSarake = new TableColumn<>("Lainattu");
        lainattuSarake.setCellValueFactory(cd -> cd.getValue().lainattuProperty());
        lainattavissaTable.getColumns().add(lainattuSarake);

        lainattavissaTable.setRowFactory(kirja -> {
            TableRow<Kirja> row = new TableRow<>();

            return row;
        });

        kirjakokoelma.lataa();

        //lisaaKirja.setOnAction(event -> lisaaKirja());
    }

    @FXML
    void handleLainaaKirja(MouseEvent event) {

    }

    @FXML
    void handlePalautaKirja(MouseEvent event) {

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
}