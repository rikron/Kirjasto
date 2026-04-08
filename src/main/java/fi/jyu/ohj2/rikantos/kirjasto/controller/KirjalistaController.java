package fi.jyu.ohj2.rikantos.kirjasto.controller;

import fi.jyu.ohj2.rikantos.kirjasto.App;
import fi.jyu.ohj2.rikantos.kirjasto.model.KirjaModel;
import fi.jyu.ohj2.rikantos.kirjasto.model.KirjakokoelmaModel;
import fi.jyu.ohj2.rikantos.kirjasto.model.LainakokoelmaModel;
import fi.jyu.ohj2.rikantos.kirjasto.model.LainausModel;
import fi.jyu.ohj2.rikantos.kirjasto.persistence.JsonKirjaRepository;
import fi.jyu.ohj2.rikantos.kirjasto.persistence.JsonLainausRepository;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Objects;
import java.util.ResourceBundle;

public class KirjalistaController implements Initializable {

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
    private Button muokkaaKirjaa;

    @FXML
    private TextField tekijaTxt;

    private final KirjakokoelmaModel kirjakokoelmaModel = new KirjakokoelmaModel(new JsonKirjaRepository(Path.of("kirjat.json")));
    private final LainakokoelmaModel lainakokoelmaModel = new LainakokoelmaModel(new JsonLainausRepository(Path.of("lainaukset.json")));

    private final ObservableList<LainausModel> lainaukset = lainakokoelmaModel.getLainaukset();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Lajitellaan kirjat tekijän nimen mukaan
        // TODO - Voisi jaotella nimet etunimi ja sukunimi ja lajitella sukunimen mukaan
        ObservableList<KirjaModel> kirjatLajiteltu = kirjakokoelmaModel.getKirjat().sorted(Comparator.comparing(KirjaModel::getTekija));
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

        TableColumn<KirjaModel, String> lainaajaSarake = new TableColumn<>("Viimeisin lainaaja");
        lainaajaSarake.setCellValueFactory(cd -> {
            if (!cd.getValue().getLainaukset().isEmpty()){
                return cd.getValue().getLainaukset().getLast().lainaajaNimiProperty();
            }
            return null;
        });
        kirjaTaulu.getColumns().add(lainaajaSarake);

        kirjaTaulu.setRowFactory(_ -> {
            TableRow<KirjaModel> row = new TableRow<>();

            // Lisätään uudelle riville tapahtumakäsittelijä klikkauksille
            row.setOnMouseClicked(event -> {
                // Jos oli hiiren ykkösnapin tuplaklikkaus,
                // eikä tyhjän rivialueen klikkaus, niin käsitellään tapahtuma
                if (event.getButton().equals(MouseButton.PRIMARY) &&
                        event.getClickCount() == 2 && !row.isEmpty()) {
                    // Haetaan riviä vastaava kirja
                    KirjaModel valittuKirja = row.getItem();
                    // Avataan lainahistoria dialogi, jolle syötetään valittu kirja
                    avaaLainaHistoria(valittuKirja);
                }
            });
            return row;
        });

        lisaaKirja.setOnAction(_ -> lisaaKirja());
        muokkaaKirjaa.setOnAction(_ -> muokkaaKirjaa());
        poistaKirja.setOnAction(_ -> poistaValittu());

        // Ladataan kirjakokoelman tiedot tiedostosta
        kirjakokoelmaModel.lataa();
    }

    /**
     * Lisätään kirja kirjakokoelmaan. Syötteet tarkastetaan tässä vaiheessa.
     * Syötekentät tyhjennetään
     */
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

        nimi = nimi.trim();
        tekija = tekija.trim();
        isbn = isbn.trim();

        kirjakokoelmaModel.lisaaKirja(nimi, tekija, isbn);

        nimiTxt.clear();
        tekijaTxt.clear();
        isbnTxt.clear();
    }

    private void muokkaaKirjaa() {
        KirjaModel valittuKirja = kirjaTaulu.getSelectionModel().getSelectedItem();

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

        if (valittuKirja == null) {
            //IO.println("Valittu kirja on NULL");
            return;
        }

        // Luodaan uusi kirja
        KirjaModel uudetTiedot = new KirjaModel(nimi, tekija, isbn);
        uudetTiedot.setLainaukset(valittuKirja.getLainaukset());
        uudetTiedot.setLainattu(valittuKirja.getLainattu());

        lainakokoelmaModel.paivitaTietynKirjanLainaukset(valittuKirja, uudetTiedot);
        kirjakokoelmaModel.paivitaKirja(valittuKirja, uudetTiedot, valittuKirja.getNimi(), valittuKirja.getTekija(), valittuKirja.getIsbn(), false);
    }

    /**
     * Poistetaan hiirellä kirjaTaulusta valittu kirja painamalla poista painiketta
     */
    private void poistaValittu() {
        KirjaModel valittuKirja = kirjaTaulu.getSelectionModel().getSelectedItem();

        if (valittuKirja == null) {
            return;
        }

        // Poistetaan vain jos valitun kirjan nimi on sama kuin lainauksissa olevan nimi
        // TODO - ISBN voisi olla parempi tarkastettava
        lainaukset.removeIf(lainaus -> Objects.equals(lainaus.getKirjaNimi(), valittuKirja.getNimi()));
        kirjakokoelmaModel.poistaKirja(valittuKirja);
    }

    /**
     * Metodi, joka käynnistää lainaushistoria näkymän
     * @param valittuKirja Kirja, joka välitetään kontrollerille
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
