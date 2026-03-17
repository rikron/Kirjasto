package fi.jyu.ohj2.rikantos.kirjasto.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

public class Lainaus {
    private final StringProperty lainaajaNimi = new SimpleStringProperty("");
    LocalDateTime lainattuPvm;
    LocalDateTime palautusPvm;
    LocalDateTime palautettuPvm;

    public Lainaus(String lainaajaNimi) {
        setLainaajaNimi(lainaajaNimi);
    }

    public String getLainaajaNimi() {return lainaajaNimi.get();}
    public LocalDateTime getLainattuPvm() {return lainattuPvm;}
    public LocalDateTime getPalautettuPvm() {return palautettuPvm;}
    public LocalDateTime getPalautusPvm() {return palautusPvm;}

    public void setLainaajaNimi(String lainaajaNimi) {this.lainaajaNimi.set(lainaajaNimi);}
    public void setLainattuPvm(LocalDateTime lainattuPvm) {this.lainattuPvm = lainattuPvm;}
    public void setPalautettuPvm(LocalDateTime palautettuPvm) {this.palautettuPvm = palautettuPvm;}
    public void setPalautusPvm(LocalDateTime palautusPvm) {this.palautusPvm = palautusPvm;}
}
