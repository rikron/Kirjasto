package fi.jyu.ohj2.rikantos.kirjasto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class KirjaModel {
    private final StringProperty nimi = new SimpleStringProperty("");
    private final StringProperty tekija = new SimpleStringProperty("");
    private final StringProperty isbn = new SimpleStringProperty("");
    private List<LainausModel> lainaukset = new ArrayList<>();
    private final BooleanProperty lainattu = new SimpleBooleanProperty(false);

    // Jätetään observableLainaukset Jacksonin ulkopuolelle, sillä se ei ymmärrä sen tyyppiä
    @JsonIgnore
    private final ListProperty<LainausModel> observableLainaukset = new SimpleListProperty<>(FXCollections.observableArrayList());

    // Jackson vaattii myös tyhjän version mallista
    @SuppressWarnings("unused")
    public KirjaModel() {
    }

    public KirjaModel(String nimi, String tekija, String isbn) {
        setNimi(nimi);
        setTekija(tekija);
        setIsbn(isbn);
        setLainattu(false);
    }

    // Getterit/setterit
    public String getNimi() {return this.nimi.get();}
    public void setNimi(String nimi) {this.nimi.set(nimi);}
    public StringProperty nimiProperty() {return this.nimi;}

    public String getTekija() {return this.tekija.get();}
    public void setTekija(String tekija) {this.tekija.set(tekija);}
    public StringProperty tekijaProperty() {return this.tekija;}

    public String getIsbn() {return this.isbn.get();}
    public void setIsbn(String isbn) {this.isbn.set(isbn);}
    public StringProperty isbnProperty() {return this.isbn;}

    public Boolean getLainattu() {return this.lainattu.get();}
    public void setLainattu(boolean lainattu) {this.lainattu.set(lainattu);}
    public BooleanProperty lainattuProperty() {return this.lainattu;}

    public ObservableList<LainausModel> getObservableLainaukset() {return observableLainaukset.get();}
    public ListProperty<LainausModel> observableLainauksetProperty() {return this.observableLainaukset;}

    public List<LainausModel> getLainaukset() { return lainaukset; }

    public void setLainaukset(List<LainausModel> lainaukset) {
        this.lainaukset = lainaukset;
        asetaObservableLainaukset();
    }
    /**
     * Asetetaan observableListaan lainaukset, sillä ne eivät
     * päivity automaattisesti Jacksonin toimesta sinne
     */
    public void asetaObservableLainaukset() {
        observableLainaukset.setAll(lainaukset);
    }

    /**
     * Lisää tämän kirjan lainaukset listaan merkinnän uudesta lainauksesta ja
     * samalla kirjoittaa ObservableList tyyppiseen ListProperty listaan
     * @param lainaus LainausModel objekti
     */
    public void lisaaLainauksiin(LainausModel lainaus) {
        if (lainaus == null) return;
        lainaukset.add(lainaus);
        asetaObservableLainaukset();
    }

    /**
     * toString metodi kirjalle, debuggaukseen kätevä.
     * @return Palauttaa nimen, tekijän ja ISBN
     */
    @Override
    public String toString() {
        return getNimi() + " : " + getTekija() + " : " + getIsbn();
    }
}
