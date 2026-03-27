package fi.jyu.ohj2.rikantos.kirjasto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

public class LainausModel {
    // Ei anneta kirjaa Jacksonille hämmästeltäväksi
    @JsonIgnore
    private KirjaModel kirja = new KirjaModel();
    private final StringProperty tekija = new SimpleStringProperty("");
    private final StringProperty kirjaNimi = new SimpleStringProperty("");
    private final StringProperty lainaajaNimi = new SimpleStringProperty("");
    private ObjectProperty<LocalDateTime> lainattuPvm = new SimpleObjectProperty<>();;
    private ObjectProperty<LocalDateTime> palautusPvm = new SimpleObjectProperty<>();;
    private ObjectProperty<LocalDateTime> palautettuPvm = new SimpleObjectProperty<>();;

    // Jacksonille tyhjä kontstruktori
    @SuppressWarnings("unused")
    public LainausModel() {
    }

    public LainausModel(KirjaModel kirja, String lainaajaNimi) {
        setKirja(kirja);
        setKirjaNimi(kirja.getNimi());
        setTekija(kirja.getNimi());
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

    public String getLainaajaNimi() {return lainaajaNimi.get();}
    public void setLainaajaNimi(String lainaajaNimi) {this.lainaajaNimi.set(lainaajaNimi);}
    public StringProperty lainaajaNimiProperty() {return lainaajaNimi;}

    public LocalDateTime getLainattuPvm() {return lainattuPvm.get();}
    public void setLainattuPvm(LocalDateTime lainattuPvm) {this.lainattuPvm.set(lainattuPvm);}
    public ObjectProperty<LocalDateTime> lainattuPvmProperty() {return lainattuPvm;}

    public LocalDateTime getPalautettuPvm() {return palautettuPvm.get();}
    public void setPalautettuPvm(LocalDateTime palautettuPvm) {this.palautettuPvm.set(palautettuPvm);}
    public ObjectProperty<LocalDateTime> palautettuPvmProperty() {return palautettuPvm;}

    public LocalDateTime getPalautusPvm() {return palautusPvm.get();}
    public void setPalautusPvm(LocalDateTime palautusPvm) {this.palautusPvm.set(palautusPvm);}
    public ObjectProperty<LocalDateTime> palautusPvmProperty() {return palautusPvm;}

    /**
     * Poistetaan tiedot asettamalle niille null arvo
     */
    public void poistaLainaus() {
        setTekija(null);
        setKirjaNimi(null);
        setLainaajaNimi(null);
        setLainattuPvm(null);
        setPalautusPvm(null);
        setPalautettuPvm(null);
    }
}
