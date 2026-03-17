package fi.jyu.ohj2.rikantos.kirjasto.controller;

import fi.jyu.ohj2.rikantos.kirjasto.App;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements Initializable {
        @FXML
        private Button lainaaKirjaBtn;

        @FXML
        private TableView<?> lainaamatTable;

        @FXML
        private TableView<?> lainattavissaTable;

        @FXML
        private Button palautaKirjaBtn;

        @FXML
        private Button siirryKirjaListaanBtn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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

                KirjalistaController controller = loader.getController();

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