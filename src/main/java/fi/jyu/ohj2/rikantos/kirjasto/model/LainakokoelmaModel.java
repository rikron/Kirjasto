package fi.jyu.ohj2.rikantos.kirjasto.model;

import fi.jyu.ohj2.rikantos.kirjasto.persistence.LainausRepository;
import fi.jyu.ohj2.rikantos.kirjasto.persistence.RepositoryException;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import java.util.List;

public class LainakokoelmaModel {
    private final LainausRepository repository;

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

    public LainakokoelmaModel(LainausRepository repository) {
        this.repository = repository;

        lainaukset.addListener((ListChangeListener<LainausModel>) _ -> tallenna());
    }

    public ObservableList<LainausModel> getLainaukset() {
        return lainaukset;
    }

    /**
     * Hakee tietyn kirjan listasta syötetyn kirjan perusteella
     * @param tavoiteltuKirja Kirja jonka sijainti halutaan löytää listasta
     */
    public void paivitaTietynKirjanLainaukset(KirjaModel tavoiteltuKirja, KirjaModel uudetTiedot) {
        lataa();
        for (LainausModel lainausModel : lainaukset) {
            //IO.println("Lainaus " + lainaukset.indexOf(lainausModel));
            if (tavoiteltuKirja.getNimi().equals(lainausModel.getKirjaNimi())
                    && tavoiteltuKirja.getTekija().equals(lainausModel.getTekija())
                    && tavoiteltuKirja.getIsbn().equals(lainausModel.getIsbn())) {
                //IO.println("Kirja löytyi!");
                lainausModel.setKirjaNimi(uudetTiedot.getNimi());
                lainausModel.setTekija(uudetTiedot.getTekija());
                lainausModel.setIsbn(uudetTiedot.getIsbn());
            }
        }
        tallenna();
    }

    /**
     * Tallennetaan lainaukset tiedostoon
     */
    public void tallenna() {
        try {
            repository.tallenna(lainaukset);
        } catch (RepositoryException e) {
            IO.println(e.getMessage());
        }
    }

    /**
     * Ladataan lainaukset tiedostosta, jos se on olemassa
     */
    public void lataa() {
        try {
            // Haetaan tiedostosta lainaukset
            List<LainausModel> kaikkiLainaukset = repository.lataa();
            // Tyhjennetään vanha taulukko
            lainaukset.clear();
            // Kirjoitetaan tiedostosta lainaukset listaan
            lainaukset.addAll(kaikkiLainaukset);
        } catch (RepositoryException e) {
            IO.println(e.getMessage());
        }
    }

    /**
     * Lisää lainauksen lainakokoelmaan. Tarkastaa myös syötteiden eheyden
     * @param kirja KirjaModel tyypin kirja
     * @param lainaajaNimi Syötetty nimi
     */
    public void lisaaLainaus(KirjaModel kirja, String lainaajaNimi) {
        if (lainaajaNimi == null || lainaajaNimi.isBlank() || kirja == null) {
            return;
        }

        //IO.println("lisaaLainaus kirja" + kirja);

        lainaajaNimi = lainaajaNimi.trim();

        LainausModel lainaus = new LainausModel(kirja, lainaajaNimi);
        // Lisätään kirjan historiaan merkintä lainauksesta
        kirja.lisaaLainauksiin(lainaus);
        lainaukset.add(lainaus);
    }

    /**
     * Poistaa lainauksen kokoelmasta
     * @param lainaus LainausModel tyypin lainaus
     */
    public void poistaLainaus(LainausModel lainaus) {
        if (lainaus == null) {
            return;
        }
        lainaukset.remove(lainaus);
    }
}
