package fi.jyu.ohj2.rikantos.kirjasto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.Observable;
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
    @JsonIgnore
    private ListProperty<LainausModel> observableLainaukset = new SimpleListProperty<>(FXCollections.observableArrayList());
    private BooleanProperty lainattu = new SimpleBooleanProperty(false);

    @SuppressWarnings("unused")
    public KirjaModel() {
    }

    public KirjaModel(String nimi, String tekija, String isbn) {
        setNimi(nimi);
        setTekija(tekija);
        setIsbn(isbn);
        setLainattu(false);
    }

    public String getNimi() {
        return this.nimi.get();
    }

    public void setNimi(String nimi) {
        this.nimi.set(nimi);
    }

    public StringProperty nimiProperty() {
        return this.nimi;
    }

    public String getTekija() {
        return this.tekija.get();
    }

    public void setTekija(String tekija) {
        this.tekija.set(tekija);
    }

    public StringProperty tekijaProperty() {
        return this.tekija;
    }

    public String getIsbn() {
        return this.isbn.get();
    }

    public void setIsbn(String isbn) {
        this.isbn.set(isbn);
    }

    public StringProperty isbnProperty() {
        return this.isbn;
    }

    public Boolean getLainattu() {
        return this.lainattu.get();
    }

    public void setLainattu(boolean lainattu) {
        this.lainattu.set(lainattu);
    }

    public BooleanProperty lainattuProperty() {
        return this.lainattu;
    }

    // Call this after deserialization
    public void initObservableList() {
        observableLainaukset.setAll(lainaukset);
    }

    // JSON getters/setters
    public List<LainausModel> getLainaukset() { return lainaukset; }

    public void setLainaukset(List<LainausModel> lainaukset) {
        this.lainaukset = lainaukset;
        initObservableList();
    }

    public void setObservableLainaukset(ObservableList<LainausModel> observableLainaukset) {
        this.observableLainaukset.set(observableLainaukset);
    }

    // JavaFX property getter
    public ListProperty<LainausModel> observableLainauksetProperty() {
        return observableLainaukset;
    }

    public ObservableList<LainausModel> getObservableLainaukset() {
        IO.println(observableLainaukset);
        IO.println(lainaukset);
        return observableLainaukset.get();
    }

    /**
     * Lisää merkinnän kirjan lainauksia seuraavaan listaan Lainus objektista
     * @param lainaus
     */
    public void lisaaLainauksiin(LainausModel lainaus) {
        if (lainaus == null) return;
        IO.println(lainaus);
        IO.println(lainaukset);
        lainaukset.add(lainaus);
        IO.println(lainaukset);
        initObservableList();
    }

    @Override
    public String toString() {
        return getNimi() + " : " + getTekija() + " : " + getIsbn();
    }
}
