package fi.jyu.ohj2.rikantos.kirjasto.controller;

import fi.jyu.ohj2.rikantos.kirjasto.App;
import fi.jyu.ohj2.rikantos.kirjasto.model.KirjaModel;
import fi.jyu.ohj2.rikantos.kirjasto.model.KirjakokoelmaModel;
import fi.jyu.ohj2.rikantos.kirjasto.model.LainakokoelmaModel;
import fi.jyu.ohj2.rikantos.kirjasto.model.LainausModel;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Objects;
import java.util.ResourceBundle;

public class KirjalistaController implements Initializable {

    @FXML
    private TableColumn<KirjaModel, String> isbnColumn;

    @FXML
    private TextField isbnTxt;

    @FXML
    private TableView<KirjaModel> kirjaTaulu;

    @FXML
    private Button lisaaKirja;

    @FXML
    private TextField nimiTxt;

    @FXML
    private Button poistaKirja;

    @FXML
    private TextField tekijaTxt;

    @FXML
    void handleLisaaKirja(MouseEvent event) {
        lisaaKirja();
    }

    @FXML
    void handlePoistaKirja(MouseEvent event) {
        poistaValittu();
    }

    private KirjakokoelmaModel kirjakokoelmaModel = new KirjakokoelmaModel();
    private LainakokoelmaModel lainakokoelmaModel = new LainakokoelmaModel();

    private ObservableList<LainausModel> lainaukset = lainakokoelmaModel.getLainaukset();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SortedList<KirjaModel> kirjatLajiteltu = kirjakokoelmaModel.getKirjat().sorted(Comparator.comparing(KirjaModel::getTekija));
        kirjaTaulu.setItems(kirjatLajiteltu);
        kirjaTaulu.setEditable(true);
        
        TableColumn<KirjaModel, String> nimiSarake = new TableColumn<>("Nimi");
        nimiSarake.setCellValueFactory(cd -> cd.getValue().nimiProperty());
        kirjaTaulu.getColumns().add(nimiSarake);

        TableColumn<KirjaModel, String> tekijaSarake = new TableColumn<>("Tekija");
        tekijaSarake.setCellValueFactory(cd -> cd.getValue().tekijaProperty());
        kirjaTaulu.getColumns().add(tekijaSarake);

        TableColumn<KirjaModel, String> isbnSarake = new TableColumn<>("ISBN");
        isbnSarake.setCellValueFactory(cd -> cd.getValue().isbnProperty());
        kirjaTaulu.getColumns().add(isbnSarake);

        TableColumn<KirjaModel, Boolean> lainattuSarake = new TableColumn<>("Lainattu");
        lainattuSarake.setCellValueFactory(cd -> cd.getValue().lainattuProperty());
        kirjaTaulu.getColumns().add(lainattuSarake);



        kirjaTaulu.setRowFactory(kirja -> {
            TableRow<KirjaModel> row = new TableRow<>();

            // Lisätään uudelle riville tapahtumakäsittelijä klikkauksille
            row.setOnMouseClicked(event -> {
                // Jos oli hiiren ykkösnapin tuplaklikkaus,
                // eikä tyhjän rivialueen klikkaus, niin käsitellään tapahtuma
                if (event.getButton().equals(MouseButton.PRIMARY) &&
                        event.getClickCount() == 2 && !row.isEmpty()) {
                    // Haetaan riviä vastaava Tehtava-olio
                    KirjaModel valittuKirja = row.getItem();
                    // Avataan muokkausdialogi
                    avaaLainaHistoria(valittuKirja);
                }
            });

            return row;
        });

        kirjakokoelmaModel.lataa();

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

        kirjakokoelmaModel.lisaaKirja(nimi, tekija, isbn);

        nimiTxt.clear();
        tekijaTxt.clear();
        isbnTxt.clear();
    }

    /**
     * Poistetaan hiirellä kirjaTaulusta valittu kirja painamalla poista painiketta
     */
    private void poistaValittu() {
        KirjaModel valittuKirja = kirjaTaulu.getSelectionModel().getSelectedItem();
        lainaukset.removeIf(lainaus -> Objects.equals(lainaus.getKirjaNimi(), valittuKirja.getNimi()));
        kirjakokoelmaModel.poistaKirja(valittuKirja);
    }

    private void avaaLainaHistoria(KirjaModel valittuKirja) {
        try {
            /* 1 */ FXMLLoader loader = new FXMLLoader(App.class.getResource("lainaushistoria.fxml"));
            /* 1 */ Parent root = loader.load();
            /* 1 */ Scene scene = new Scene(root);

            LainaHistoriaController controller = loader.getController();
            controller.setKirja(valittuKirja);

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
