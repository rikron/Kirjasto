package fi.jyu.ohj2.rikantos.kirjasto.controller;

import fi.jyu.ohj2.rikantos.kirjasto.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class KirjalistaController {

    @FXML
    private TableColumn<?, ?> isbnColumn;

    @FXML
    private TextField isbnTxt;

    @FXML
    private TableView<?> kirjaLista;

    @FXML
    private TableColumn<?, ?> lainattuColumn;

    @FXML
    private TableColumn<?, ?> lainausmaaraColumn;

    @FXML
    private Button lisaaKirja;

    @FXML
    private TableColumn<?, ?> nimiColumn;

    @FXML
    private TextField nimiTxt;

    @FXML
    private Button poistaKirja;

    @FXML
    private Button siirryLainaamaan;

    @FXML
    private TableColumn<?, ?> tekijaColumn;

    @FXML
    private TextField tekijaTxt;

    @FXML
    void handleLisaaKirja(MouseEvent event) {

    }

    @FXML
    void handlePoistaKirja(MouseEvent event) {

    }

    @FXML
    void handleSiirryLainauksiin(MouseEvent event) {
         sulje();
    }

    private void sulje() {
        // Haetaan Scene-olio kirjalista komponentista
        Scene scene = isbnTxt.getScene();
        Stage ikkuna = (Stage) scene.getWindow();
        ikkuna.close();
    }
}
