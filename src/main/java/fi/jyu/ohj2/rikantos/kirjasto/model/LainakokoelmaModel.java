package fi.jyu.ohj2.rikantos.kirjasto.model;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LainakokoelmaModel {

    private final ObservableList<LainausModel> lainaukset = FXCollections.observableArrayList(
            lainaus -> new Observable[]{
                    lainaus.tekijaProperty(),
                    lainaus.kirjaNimiProperty(),
                    lainaus.lainaajaNimiProperty(),
                    lainaus.lainattuPvmProperty(),
                    lainaus.palautusPvmProperty(),
                    lainaus.palautettuPvmProperty()}
    );

    private final Path tiedostoPolku = Path.of("lainaukset.json");
    private final ObjectMapper mapper = new ObjectMapper();

    public LainakokoelmaModel() {
        lainaukset.addListener((ListChangeListener<LainausModel>) change -> {
            tallenna();
        });
    }

    public ObservableList<LainausModel> getLainaukset() {
        return lainaukset;
    }

    /**
     * Tallennetaan lainaukset tiedostoon
     */
    public void tallenna() {
        mapper.writeValue(tiedostoPolku, lainaukset);
    }

    /**
     * Ladataan lainaukset tiedostosta, jos se on olemassa
     */
    public void lataa() {
        if (Files.notExists(tiedostoPolku)) {
            return;
        }
        try {
            List<LainausModel> kaikkiLainaukset = mapper.readValue(tiedostoPolku, new TypeReference<>() {});
            lainaukset.addAll(kaikkiLainaukset);
        } catch (JacksonException je) {
            IO.println("Lainakokoelma - JSONin lukeminen epäonnistui: " + je.getMessage());
        }
    }

    /**
     * Lisää lainauksen lainakokoelmaan. Tarkastaa myös syötteiden eheyden
     * @param kirja - KirjaModel tyypin kirja
     * @param lainaajaNimi - Syötetty nimi
     */
    public void lisaaLainaus(KirjaModel kirja, String lainaajaNimi) {
        if (lainaajaNimi == null || lainaajaNimi.isBlank() || kirja == null) {
            return;
        }

        lainaajaNimi = lainaajaNimi.trim();

        LainausModel lainaus = new LainausModel(kirja, lainaajaNimi);
        // Lisätään kirjan historiaan merkintä lainauksesta
        kirja.lisaaLainauksiin(lainaus);
        lainaukset.add(lainaus);
    }

    /**
     * Poistaa lainauksen kokoelmasta
     * @param lainaus - LainausModel tyypin lainaus
     */
    public void poistaLainaus(LainausModel lainaus) {
        if (lainaus == null) {
            return;
        }
        lainaukset.remove(lainaus);
    }
}
