package fi.jyu.ohj2.rikantos.kirjasto.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

public class Kirja {
    private final StringProperty nimi = new SimpleStringProperty("");
    private final StringProperty tekija = new SimpleStringProperty("");
    private final StringProperty isbn = new SimpleStringProperty("");
    private final ListProperty<Lainaus> lainaukset = new SimpleListProperty<>();

    @SuppressWarnings("unused")
    public Kirja() {}

    public Kirja(String nimi, String tekija, String isbn) {
        setNimi(nimi);
        setTekija(tekija);
        setIsbn(isbn);
    }

    public String getNimi() {return this.nimi.get();}
    public void setNimi(String nimi) {this.nimi.set(nimi);}
    public StringProperty nimiProperty() {return this.nimi;}

    public String getTekija() {return this.tekija.get();}
    public void setTekija(String tekija) {this.tekija.set(tekija);}
    public StringProperty tekijaProperty() {return this.tekija;}

    public String getIsbn() {return this.isbn.get();}
    public void setIsbn(String isbn) {this.isbn.set(isbn);}
    public StringProperty isbnProperty() {return this.isbn;}

    //public List<Lainaus> getLainaukset() {return lainaukset;}
    //public void setLainaukset(List<Lainaus> lainaukset) {this.lainaukset = lainaukset;}

    @Override
    public String toString() {
        return getNimi() + " : " + getTekija() + " : " + getIsbn();
    }
}
