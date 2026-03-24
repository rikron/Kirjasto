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

public class Lainakokoelma {
    Kirjakokoelma kirjakokoelma = new Kirjakokoelma();

    private final ObservableList<Lainaus> lainaukset = FXCollections.observableArrayList(
            lainaus -> new Observable[]{
                    lainaus.kirjaProperty(),
                    lainaus.lainaajaNimiProperty(),
                    lainaus.lainattuPvmProperty(),
                    lainaus.palautusPvmProperty(),
                    lainaus.palautettuPvmProperty()}
    );

    private final Path tiedostoPolku = Path.of("lainaukset.json");
    private final ObjectMapper mapper = new ObjectMapper();

    public Lainakokoelma() {
        lainaukset.addListener((ListChangeListener<Lainaus>) change -> {
            tallenna();
        });
    }

    public ObservableList<Lainaus> getLainaukset() {
        return lainaukset;
    }

    public void tallenna() {
        mapper.writeValue(tiedostoPolku, lainaukset);
    }

    public void lataa() {
        if (Files.notExists(tiedostoPolku)) {
            return;
        }
        try {
            List<Lainaus> kaikkiLainaukset = mapper.readValue(tiedostoPolku, new TypeReference<>() {});
            lainaukset.addAll(kaikkiLainaukset);
        } catch (JacksonException je) {
            IO.println("JSONin lukeminen epäonnistui: " + je.getMessage());
        }
    }

    public void lisaaLainaus(Kirja kirja, String lainaajaNimi) {
        if (lainaajaNimi == null || lainaajaNimi.isBlank() || kirja == null) {
            return;
        }

        lainaajaNimi = lainaajaNimi.trim();

        Lainaus lainaus = new Lainaus(kirja, lainaajaNimi);
        // Lisätään kirjan historiaan merkintä lainauksesta
        kirja.lisaaLainaus(lainaus);
        lainaukset.add(lainaus);
    }

    public void poistaLainaus(Lainaus lainaus) {
        if (lainaus == null) {
            return;
        }
        lainaukset.remove(lainaus);
    }
}
