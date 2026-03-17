package fi.jyu.ohj2.rikantos.kirjasto.controller;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class MainController implements Initializable {
    @FXML
    private TableView<?> kirjaLista;

    @FXML
    private Button lisaaKirja;

    @FXML
    private Button poistaKirja;

    @FXML
    private Button siirryLainaamaan;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    void handleLisaaKirja(MouseEvent event) {
    }

    @FXML
    void handlePoistaKirja(MouseEvent event) {

    }

    @FXML
    void handleSiirryLainauksiin(MouseEvent event) {

    }
}
