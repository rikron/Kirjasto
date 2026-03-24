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

public class Kirjakokoelma {
    private final ObservableList<Kirja> kirjat = FXCollections.observableArrayList(
            kirja -> new Observable[]{kirja.nimiProperty(), kirja.tekijaProperty(), kirja.isbnProperty(), kirja.lainattuProperty()}
    );

    private final Path tiedostoPolku = Path.of("kirjat.json");
    private final ObjectMapper mapper = new ObjectMapper();

    public Kirjakokoelma() {
        kirjat.addListener((ListChangeListener<Kirja>) change -> {
            tallenna();
        });
    }

    public ObservableList<Kirja> getKirjat() {
        return kirjat;
    }

    public void tallenna() {
        mapper.writeValue(tiedostoPolku, kirjat);
    }

    public void lataa() {
        if (Files.notExists(tiedostoPolku)) {
            return;
        }
        try {
            List<Kirja> kaikkiKirjat = mapper.readValue(tiedostoPolku, new TypeReference<>() {});
            kirjat.addAll(kaikkiKirjat);
        } catch (JacksonException je) {
            IO.println("JSONin lukeminen epäonnistui: " + je.getMessage());
        }
    }

    public void lisaaKirja(String nimi, String tekija, String isbn) {
        if (nimi == null || nimi.isBlank() || tekija == null || tekija.isBlank() || isbn == null || isbn.isBlank()) {
            return;
        }

        nimi = nimi.trim();
        tekija = tekija.trim();
        isbn = isbn.trim();

        kirjat.add(new Kirja(nimi, tekija, isbn));
    }

    public void poistaKirja(Kirja kirja) {
        if (kirja == null) {
            return;
        }
        kirjat.remove(kirja);
    }
}
