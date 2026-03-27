package fi.jyu.ohj2.rikantos.kirjasto.controller;

import fi.jyu.ohj2.rikantos.kirjasto.model.KirjaModel;
import fi.jyu.ohj2.rikantos.kirjasto.model.KirjakokoelmaModel;
import fi.jyu.ohj2.rikantos.kirjasto.model.LainausModel;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class LainaHistoriaController implements Initializable {

    @FXML
    private TableView<LainausModel> lainausHistoriaTable;

    KirjakokoelmaModel kirjakokoelma = new KirjakokoelmaModel();

    private KirjaModel tarkasteltavaKirja;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Asettaa kontrollerin käsittelemän kirjan ja kutsuu taytaTaulukko metodia
     * @param kirja Tarkasteltava kirja
     */
    public void setKirja(KirjaModel kirja) {
        if (kirja == null) return;
        this.tarkasteltavaKirja = kirja;
        taytaTaulukko();
    }

    /**
     * Kontrollerille tulee syöttää kirja, jonka historiaa tarkastellaan
     * ennen tiedon tulostamista. Tämä metodi täyttää taulukon asetetun kirjan
     * pohjalta
     */
    public void taytaTaulukko() {
        tarkasteltavaKirja.asetaObservableLainaukset();
        ObservableList<LainausModel> lainauksetLajiteltu = tarkasteltavaKirja.getObservableLainaukset();

        lainausHistoriaTable.setItems(lainauksetLajiteltu);
        lainausHistoriaTable.setEditable(true);

        TableColumn<LainausModel, LocalDateTime> lainattuPvmSarake = new TableColumn<>("Lainattu");
        lainattuPvmSarake.setCellValueFactory(cd -> cd.getValue().lainattuPvmProperty());
        lainausHistoriaTable.getColumns().add(lainattuPvmSarake);

        TableColumn<LainausModel, LocalDateTime> palautusSarake = new TableColumn<>("Palautus");
        palautusSarake.setCellValueFactory(cd -> cd.getValue().palautusPvmProperty());
        lainausHistoriaTable.getColumns().add(palautusSarake);

        TableColumn<LainausModel, LocalDateTime> palautettuSarake = new TableColumn<>("Palautettu");
        palautettuSarake.setCellValueFactory(cd -> cd.getValue().palautettuPvmProperty());
        lainausHistoriaTable.getColumns().add(palautettuSarake);

        lainausHistoriaTable.setRowFactory(lainaus -> {
            TableRow<LainausModel> row = new TableRow<>();

            return row;
        });

        kirjakokoelma.lataa();
    }
}

