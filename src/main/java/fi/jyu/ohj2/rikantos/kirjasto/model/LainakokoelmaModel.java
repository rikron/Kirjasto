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
                    lainaus.getKirja().lainattuProperty(),
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
     * Hakee tietyn kirjan listasta syötetyn kirjan perusteella
     * @param tavoiteltuKirja - Kirja jonka sijainti halutaan löytää listasta
     * @return Kirjan indeksi
     */
    public void paivitaTietynKirjanLainaukset(KirjaModel tavoiteltuKirja) {
        int koko = lainaukset.size();

        for (LainausModel lainausModel : lainaukset) {
            if (tavoiteltuKirja.getNimi().equals(lainausModel.getKirjaNimi())
                    && tavoiteltuKirja.getTekija().equals(lainausModel.getTekija())) {
                lainausModel.setKirjaNimi(tavoiteltuKirja.getNimi());
                lainausModel.setTekija(tavoiteltuKirja.getTekija());
            }
        }
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
            // Haetaan tiedostosta lainaukset
            List<LainausModel> kaikkiLainaukset = mapper.readValue(tiedostoPolku, new TypeReference<>() {});
            // Tyhjennetään vanha taulukko
            lainaukset.clear();
            // Kirjoitetaan tiedostosta lainaukset listaan
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
