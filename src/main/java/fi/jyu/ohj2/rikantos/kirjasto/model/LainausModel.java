package fi.jyu.ohj2.rikantos.kirjasto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.*;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

public class LainausModel {
    // Ei anneta kirjaa Jacksonille hämmästeltäväksi
    @JsonIgnore
    private KirjaModel kirja = new KirjaModel();

    private final StringProperty tekija = new SimpleStringProperty("");
    private final StringProperty kirjaNimi = new SimpleStringProperty("");
    private final BooleanProperty lainattu = new SimpleBooleanProperty(false);
    private final StringProperty lainaajaNimi = new SimpleStringProperty("");
    private final StringProperty isbn = new SimpleStringProperty("");
    private final ObjectProperty<LocalDateTime> lainattuPvm = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> palautusPvm = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> palautettuPvm = new SimpleObjectProperty<>();

    // Jacksonille tyhjä konstruktori
    @SuppressWarnings("unused")
    public LainausModel() {
    }

    public LainausModel(KirjaModel kirja, String lainaajaNimi) {
        setKirja(kirja);
        setKirjaNimi(kirja.getNimi());
        setTekija(kirja.getTekija());
        setIsbn(kirja.getIsbn());
        setLainattu(kirja.getLainattu());
        setLainaajaNimi(lainaajaNimi);
        setLainattuPvm(now());
        int lainaViikot = 4;
        setPalautusPvm(now().plusWeeks(lainaViikot));
    }

    // Getterit/setterit
    public KirjaModel getKirja() {return kirja;}
    public void setKirja(KirjaModel kirja) {this.kirja = kirja;}

    public String getKirjaNimi() {return kirjaNimi.get();}
    public void setKirjaNimi(String kirjaNimi) {this.kirjaNimi.set(kirjaNimi);}
    public StringProperty kirjaNimiProperty() {return kirjaNimi;}

    public String getTekija() {return tekija.get();}
    public void setTekija(String tekija) {this.tekija.set(tekija);}
    public StringProperty tekijaProperty() {return tekija;}

    @SuppressWarnings({"unused"})
    public boolean getLainattu() {return lainattu.get();}
    public void setLainattu(boolean lainattu) {this.lainattu.set(lainattu);}
    @SuppressWarnings({"unused"})
    public BooleanProperty lainattuProperty() {return lainattu;}

    public String getLainaajaNimi() {return lainaajaNimi.get();}
    public void setLainaajaNimi(String lainaajaNimi) {this.lainaajaNimi.set(lainaajaNimi);}
    public StringProperty lainaajaNimiProperty() {return lainaajaNimi;}

    @SuppressWarnings({"unused"})
    public LocalDateTime getLainattuPvm() {return lainattuPvm.get();}
    public void setLainattuPvm(LocalDateTime lainattuPvm) {this.lainattuPvm.set(lainattuPvm);}
    public ObjectProperty<LocalDateTime> lainattuPvmProperty() {return lainattuPvm;}

    @SuppressWarnings({"unused"})
    public LocalDateTime getPalautettuPvm() {return palautettuPvm.get();}
    public void setPalautettuPvm(LocalDateTime palautettuPvm) {this.palautettuPvm.set(palautettuPvm);}
    public ObjectProperty<LocalDateTime> palautettuPvmProperty() {return palautettuPvm;}

    public LocalDateTime getPalautusPvm() {return palautusPvm.get();}
    public void setPalautusPvm(LocalDateTime palautusPvm) {this.palautusPvm.set(palautusPvm);}
    public ObjectProperty<LocalDateTime> palautusPvmProperty() {return palautusPvm;}

    public String getIsbn() {return isbn.get();}
    public void setIsbn(String isbn) {this.isbn.set(isbn);}
    public StringProperty isbnProperty() {return isbn;}
}
