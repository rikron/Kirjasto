package fi.jyu.ohj2.rikantos.kirjasto.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

public class Lainaus {
    private ObjectProperty<Kirja> kirja = new SimpleObjectProperty<>();;
    private final StringProperty lainaajaNimi = new SimpleStringProperty("");
    private ObjectProperty<LocalDateTime> lainattuPvm = new SimpleObjectProperty<>();;
    private ObjectProperty<LocalDateTime> palautusPvm = new SimpleObjectProperty<>();;
    private ObjectProperty<LocalDateTime> palautettuPvm = new SimpleObjectProperty<>();;

    @SuppressWarnings("unused")
    public Lainaus() {
    }

    public Lainaus(Kirja kirja, String lainaajaNimi) {
        setKirja(kirja);
        setLainaajaNimi(lainaajaNimi);
        setLainattuPvm(now());
        int lainaViikot = 4;
        setPalautusPvm(now().plusWeeks(lainaViikot));
    }

    public Kirja getKirja() {return kirja.get();}
    public String getLainaajaNimi() {return lainaajaNimi.get();}
    public LocalDateTime getLainattuPvm() {return lainattuPvm.get();}
    public LocalDateTime getPalautettuPvm() {return palautettuPvm.get();}
    public LocalDateTime getPalautusPvm() {return palautusPvm.get();}

    public void setKirja(Kirja kirja) {this.kirja.set(kirja);}
    public void setLainaajaNimi(String lainaajaNimi) {this.lainaajaNimi.set(lainaajaNimi);}
    public void setLainattuPvm(LocalDateTime lainattuPvm) {this.lainattuPvm.set(lainattuPvm);}
    public void setPalautettuPvm(LocalDateTime palautettuPvm) {this.palautettuPvm.set(palautettuPvm);}
    public void setPalautusPvm(LocalDateTime palautusPvm) {this.palautusPvm.set(palautusPvm);}

    public ObjectProperty<Kirja> kirjaProperty() {
        return kirja;
    }

    public StringProperty lainaajaNimiProperty() {
        return lainaajaNimi;
    }

    public ObjectProperty<LocalDateTime> lainattuPvmProperty() {
        return lainattuPvm;
    }

    public ObjectProperty<LocalDateTime> palautettuPvmProperty() {
        return palautettuPvm;
    }

    public ObjectProperty<LocalDateTime> palautusPvmProperty() {
        return palautusPvm;
    }

    public void poistaLainaus() {
        setKirja(null);
        setLainaajaNimi(null);
        setLainattuPvm(null);
        setPalautusPvm(null);
        setPalautettuPvm(null);
    }
}
