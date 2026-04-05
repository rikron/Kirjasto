package fi.jyu.ohj2.rikantos.kirjasto.model;

import fi.jyu.ohj2.rikantos.kirjasto.persistence.KirjaRepository;
import fi.jyu.ohj2.rikantos.kirjasto.persistence.RepositoryException;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.List;

public class KirjakokoelmaModel {
    private final KirjaRepository repository;

    // ObservableList, joka ottaa kirjojen tiedot vastaan ja tallentaa ne JavaFX:lle sopivalla tavalla
    private final ObservableList<KirjaModel> kirjat = FXCollections.observableArrayList(
            kirja -> new Observable[]{
                    kirja.nimiProperty(),
                    kirja.tekijaProperty(),
                    kirja.observableLainauksetProperty(),
                    kirja.isbnProperty(),
                    kirja.lainattuProperty()}
    );

    public KirjakokoelmaModel(KirjaRepository repository) {
        this.repository = repository;
        // Kuuntelija muutoksille, joka tallentaa ne tiedostoon
        kirjat.addListener((ListChangeListener<KirjaModel>) _ -> tallenna());
    }

    public ObservableList<KirjaModel> getKirjat() {
        return kirjat;
    }

    public ObservableList<KirjaModel> getLainaamattomatKirjat() {
        return new FilteredList<>(this.getKirjat(), kirja -> !kirja.getLainattu());
    }

    /**
     * Hakee tietyn kirjan listasta syötetyn kirjan perusteella
     * @param tavoiteltuKirja - Kirja jonka sijainti halutaan löytää listasta
     * @return Kirjan indeksi
     */
    public int getTiettyKirja(KirjaModel tavoiteltuKirja, String nimi, String tekija) {
        tallenna();
        lataa();

        for(KirjaModel kirja : kirjat){
            //IO.println("Tavoitellaan: " + tavoiteltuKirja);
            //IO.println("Käsitellään: " + kirja);
            if ((tavoiteltuKirja.getNimi().equals(kirja.getNimi()) && tavoiteltuKirja.getTekija().equals(kirja.getTekija()))
                || (nimi.equals(kirja.getNimi()) && tekija.equals(kirja.getTekija()))){
                //IO.println("Indeksi on: " +kirjat.indexOf(kirja));
                return kirjat.indexOf(kirja);
            }
        }
        return -1;
    }

    /**
     * Tallentaa kirjakokoelmaan lisätyt kirjat tiedostoon
     */
    public void tallenna() {
        try {
            repository.tallenna(kirjat);
        } catch (RepositoryException e) {
            IO.println(e.getMessage());
        }
    }

    /**
     * Lataa kirjakokoelman tiedot tiedostosta, jos se on olemassa
     */
    public void lataa() {
        try {
            // Luetaan tiedostosta kirjat
            List<KirjaModel> kaikkiKirjat = repository.lataa();
            // Tyhjennetään taulukko
            kirjat.clear();
            // Lisätään kirjat taulukkoon
            kirjat.addAll(kaikkiKirjat);
            // Lisätään vielä observableLainaukset listaan kaikki kirjat,
            // sillä ne eivät automaattisesti sinne lisäänny
            for (KirjaModel kirja : kirjat) {
                kirja.asetaObservableLainaukset();
            }
        } catch (RepositoryException e) {
            IO.println(e.getMessage());
        }
    }

    /**
     * Lisätään kirja kirjakokoelmaan. Tarkastetaan myös syötteet tässä vaiheessa
     * @param nimi - Kirjan nimi
     * @param tekija - Kirjan tekijä
     * @param isbn - ISBN
     */
    public void lisaaKirja(String nimi, String tekija, String isbn) {
        if (nimi == null || nimi.isBlank() || tekija == null || tekija.isBlank() || isbn == null || isbn.isBlank()) {
            return;
        }

        nimi = nimi.trim();
        tekija = tekija.trim();
        isbn = isbn.trim();

        kirjat.add(new KirjaModel(nimi, tekija, isbn));
    }

    /**
     * Otaa vastaan vanha kirja, jota päivitetään, sekä uusilla tiedoin varustettu
     * teos, joka sijoitetaan kokoelmaan vanhan sijalle. Lainahistoria ja lainattu-status säilyy.
     * @param vanhaKirja - Kirja, jota halutaan muokata
     * @param uusiKirja - Kirja, joka asetetaan vanhan kirjan tilalle
     */
    public void paivitaKirja(KirjaModel vanhaKirja, KirjaModel uusiKirja, String nimi, String tekija) {
        if (vanhaKirja == null) return;
        // Etsitään kirjan indeksi kokoelmasta, jos ei löydy, palautetaan -1 ja palataan
        int listanKirjaIndeksi = getTiettyKirja(vanhaKirja, nimi, tekija);
        //IO.println("Päivitetään: " + listanKirjaIndeksi);
        if (listanKirjaIndeksi == -1) {
            //IO.println("Listasta ei löydetty kirjaa!");
            return;
        }

        // Asetetaan lainaukset ja lainattu tila uusiksi. Fyysisten kirjojen lainaustila ei muuttuisi
        uusiKirja.setLainaukset(vanhaKirja.getLainaukset());
        uusiKirja.setLainattu(vanhaKirja.getLainattu());

        // Asetetaan kokoelmaan uusi kirja vanhan kirjan indeksiin.
        // Näkyvissä taulukoissa toki järjestys menee muiden asioiden mukaisesti
        kirjat.set(listanKirjaIndeksi, uusiKirja);
    }

    /**
     * Poistaa kirjan kokoelmasta
     * @param kirja - KirjaModel kirja
     */
    public void poistaKirja(KirjaModel kirja) {
        if (kirja == null) {
            return;
        }
        kirjat.remove(kirja);
    }
}
