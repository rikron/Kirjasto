package fi.jyu.ohj2.rikantos.kirjasto.model;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class KirjakokoelmaModel {

    // ObservableList, joka ottaa kirjojen tiedot vastaan ja tallentaa ne JavaFX:lle sopivalla tavalla
    private final ObservableList<KirjaModel> kirjat = FXCollections.observableArrayList(
            kirja -> new Observable[]{
                    kirja.nimiProperty(),
                    kirja.tekijaProperty(),
                    kirja.observableLainauksetProperty(),
                    kirja.isbnProperty(),
                    kirja.lainattuProperty()}
    );

    private final Path tiedostoPolku = Path.of("kirjat.json");
    private final ObjectMapper mapper = new ObjectMapper();

    public KirjakokoelmaModel() {
        // Kuuntelija muutoksille, joka tallentaa ne tiedostoon
        kirjat.addListener((ListChangeListener<KirjaModel>) change -> {
            tallenna();
        });
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
    public int getTiettyKirja(KirjaModel tavoiteltuKirja) {
        int koko = kirjat.size();

        for(int i = 0; i<koko; i++){
            if (tavoiteltuKirja.equals(kirjat.get(i))){
                return i;
            }
        }
        return -1;
    }

    /**
     * Tallentaa kirjakokoelmaan lisätyt kirjat tiedostoon
     */
    public void tallenna() {
        mapper.writeValue(tiedostoPolku, kirjat);
    }

    /**
     * Lataa kirjakokoelman tiedot tiedostosta, jos se on olemassa
     */
    public void lataa() {
        if (Files.notExists(tiedostoPolku)) {
            return;
        }
        try {
            // Luetaan tiedostosta kirjat
            List<KirjaModel> kaikkiKirjat = mapper.readValue(tiedostoPolku, new TypeReference<>() {});
            // Tyhjennetään taulukko
            //kirjat.clear();
            // Lisätään kirjat taulukkoon
            kirjat.addAll(kaikkiKirjat);
            // Lisätään vielä observableLainaukset listaan kaikki kirjat,
            // sillä ne eivät automaattisesti sinne lisäänny
            for (KirjaModel kirja : kirjat) {
                kirja.asetaObservableLainaukset();
            }
        } catch (JacksonException je) {
            IO.println("Kirjakokoelma - JSONin lukeminen epäonnistui: " + je.getMessage());
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
    public void paivitaKirja(KirjaModel vanhaKirja, KirjaModel uusiKirja) {
        if (vanhaKirja == null) return;

        // Etsitään kirjan indeksi kokoelmasta, jos ei löydy, palautetaan -1 ja palataan
        int listanKirjaIndeksi = getTiettyKirja(vanhaKirja);
        if (listanKirjaIndeksi == -1) return;

        // Asetetaan lainaukset ja lainattu tila uusiksi. Fyysisten kirjojen lainaustila ei muuttuisi
        uusiKirja.setLainaukset(vanhaKirja.getLainaukset());
        uusiKirja.setLainattu(vanhaKirja.getLainattu());

        // Asetetaan kokoelmaan uusi kirja vanhan kirjan indeksiin.
        // Näkyvissä taulukoissa toki järjestys menee muiden asioiden mukaisesti
        kirjat.set(listanKirjaIndeksi, uusiKirja);
        // Tallennetaan taulukon muutokset
        tallenna();
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
        tallenna();
    }
}
