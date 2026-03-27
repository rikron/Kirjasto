package fi.jyu.ohj2.rikantos.kirjasto.controller;

import fi.jyu.ohj2.rikantos.kirjasto.App;
import fi.jyu.ohj2.rikantos.kirjasto.model.KirjaModel;
import fi.jyu.ohj2.rikantos.kirjasto.model.KirjakokoelmaModel;
import fi.jyu.ohj2.rikantos.kirjasto.model.LainakokoelmaModel;
import fi.jyu.ohj2.rikantos.kirjasto.model.LainausModel;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements Initializable {
    private KirjakokoelmaModel kirjakokoelmaModel = new KirjakokoelmaModel();
    private LainakokoelmaModel lainakokoelmaModel = new LainakokoelmaModel();

    @FXML
    private Button lainaaKirjaBtn;

    @FXML
    private TableView<LainausModel> lainaamatTable;

    @FXML
    private TableView<KirjaModel> lainattavissaTable;

    @FXML
    private Button palautaKirjaBtn;

    @FXML
    private Button siirryKirjaListaanBtn;

    @FXML
    private TextField lainaajanNimiTxt;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        taytaTaulukot();
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

    private void taytaTaulukot() {
        // Lajitellaan kirjat aina tekijän nimen mukaisesti, haetaan vain lainaamattomat kirjat
        SortedList<KirjaModel> kirjatLajiteltu = kirjakokoelmaModel.getLainaamattomatKirjat().sorted(Comparator.comparing(KirjaModel::getTekija));
        lainattavissaTable.setItems(kirjatLajiteltu);
        lainattavissaTable.setEditable(true);

        TableColumn<KirjaModel, String> nimiSarake = new TableColumn<>("Nimi");
        nimiSarake.setCellValueFactory(cd -> cd.getValue().nimiProperty());
        lainattavissaTable.getColumns().add(nimiSarake);

        TableColumn<KirjaModel, String> tekijaSarake = new TableColumn<>("Tekija");
        tekijaSarake.setCellValueFactory(cd -> cd.getValue().tekijaProperty());
        lainattavissaTable.getColumns().add(tekijaSarake);

        TableColumn<KirjaModel, String> isbnSarake = new TableColumn<>("ISBN");
        isbnSarake.setCellValueFactory(cd -> cd.getValue().isbnProperty());
        lainattavissaTable.getColumns().add(isbnSarake);

        TableColumn<KirjaModel, String> lainattuSarake = new TableColumn<>("Lainattu");
        lainattuSarake.setCellValueFactory(cd -> cd.getValue().lainattuProperty().asString());
        lainattavissaTable.getColumns().add(lainattuSarake);

        lainattavissaTable.setRowFactory(kirja -> {
            TableRow<KirjaModel> row = new TableRow<>();

            // Lisätään uudelle riville tapahtumakäsittelijä klikkauksille
            row.setOnMouseClicked(event -> {
                // Jos oli hiiren ykkösnapin tuplaklikkaus,
                // eikä tyhjän rivialueen klikkaus, niin käsitellään tapahtuma
                if (event.getButton().equals(MouseButton.PRIMARY) &&
                        event.getClickCount() == 2 && !row.isEmpty()) {
                    tyhjennaTaulukot();
                    taytaTaulukot();
                    // Haetaan riviä vastaava kirja
                    KirjaModel valittuKirja = row.getItem();
                    // Avataan lainahistoria dialogi, jolle syötetään valittu kirja
                    avaaLainaHistoria(valittuKirja);
                }
            });

            return row;
        });

        // Lajitellaan tässä palautusPvm mukaan
        SortedList<LainausModel> lainauksetLajiteltu = lainakokoelmaModel.getLainaukset().sorted(Comparator.comparing(LainausModel::getPalautusPvm));
        lainaamatTable.setItems(lainauksetLajiteltu);
        lainaamatTable.setEditable(true);

        TableColumn<LainausModel, String> lainattuNimiSarake = new TableColumn<>("Nimi");
        lainattuNimiSarake.setCellValueFactory(cd -> cd.getValue().kirjaNimiProperty());
        lainaamatTable.getColumns().add(lainattuNimiSarake);

        TableColumn<LainausModel, String> lainattuTekijaSarake = new TableColumn<>("Tekija");
        lainattuTekijaSarake.setCellValueFactory(cd -> cd.getValue().tekijaProperty());
        lainaamatTable.getColumns().add(lainattuTekijaSarake);

        TableColumn<LainausModel, LocalDateTime> lainattuPvmSarake = new TableColumn<>("Lainattu");
        lainattuPvmSarake.setCellValueFactory(cd -> cd.getValue().lainattuPvmProperty());
        lainaamatTable.getColumns().add(lainattuPvmSarake);

        TableColumn<LainausModel, LocalDateTime> palautusSarake = new TableColumn<>("Palautus");
        palautusSarake.setCellValueFactory(cd -> cd.getValue().palautusPvmProperty());
        lainaamatTable.getColumns().add(palautusSarake);

        lainaamatTable.setRowFactory(lainaus -> {
            TableRow<LainausModel> row = new TableRow<>();

            return row;
        });

        lainakokoelmaModel.lataa();
        kirjakokoelmaModel.lataa();
    }

    private void tyhjennaTaulukot() {
        lainaamatTable.getColumns().clear();
        lainattavissaTable.getColumns().clear();
    }

    /**
     * Poistaa valitun lainauksen lainakokoelmasta
     */
    private void poistaValittu() {
        LainausModel valittuLainaus = lainaamatTable.getSelectionModel().getSelectedItem();
        lainakokoelmaModel.poistaLainaus(valittuLainaus);
    }

    /**
     * Siirretään lainattu kirja ylemmästä taulusta alempaan.
     * Astetaan kirja KirjaModelissa lainautksi.
     */
    private void lainaaKirja() {
        String nimi = lainaajanNimiTxt.getText();
        KirjaModel valittuKirja = lainattavissaTable.getSelectionModel().getSelectedItem();

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
        lainakokoelmaModel.lisaaLainaus(valittuKirja, nimi);
    }

    /**
     * Palauttaa kirjan. Asettaa KirjaModelissa lainattu totuusarvon falseksi
     */
    private void palautaKirja() {
        LainausModel valittuLainaus = lainaamatTable.getSelectionModel().getSelectedItem();

        if (valittuLainaus == null) {
            return;
        }

        valittuLainaus.getKirja().setLainattu(false);
        valittuLainaus.setPalautettuPvm(LocalDateTime.now());

        lainakokoelmaModel.poistaLainaus(valittuLainaus);
    }

    /**
     * Avataan lainaushistoria näkymä
     * @param valittuKirja - Kontrollerille syötetty valittu kirja
     */
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
