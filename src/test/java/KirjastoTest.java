import fi.jyu.ohj2.rikantos.kirjasto.model.KirjaModel;
import fi.jyu.ohj2.rikantos.kirjasto.model.KirjakokoelmaModel;
import fi.jyu.ohj2.rikantos.kirjasto.model.LainakokoelmaModel;
import fi.jyu.ohj2.rikantos.kirjasto.model.LainausModel;
import fi.jyu.ohj2.rikantos.kirjasto.persistence.JsonKirjaRepository;
import fi.jyu.ohj2.rikantos.kirjasto.persistence.JsonLainausRepository;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class KirjastoTest {

    @Test
    void lisaaKirja_lisaaKirjanListaan() {
        KirjakokoelmaModel kokoelma = new KirjakokoelmaModel(new JsonKirjaRepository(Path.of("testikirjat.json")));
        kokoelma.lisaaKirja("Nimi", "Tekijä", "1234");
        assertEquals(1, kokoelma.getKirjat().size());
        assertEquals("Nimi", kokoelma.getKirjat().getFirst().getNimi());
        assertEquals("Tekijä", kokoelma.getKirjat().getFirst().getTekija());
        assertEquals("1234", kokoelma.getKirjat().getFirst().getIsbn());
    }

    @Test
    void poistaKirja_poistaaKirjanListaan() {
        KirjakokoelmaModel kokoelma = new KirjakokoelmaModel(new JsonKirjaRepository(Path.of("testikirjat.json")));
        kokoelma.lisaaKirja("Nimi", "Tekijä", "1234");
        KirjaModel kirja = kokoelma.getKirjat().getFirst();
        kokoelma.poistaKirja(kirja);
        assertEquals(0, kokoelma.getKirjat().size());
    }

    @Test
    void lisaaKirja_eiLisaaTyhjaaKirjaa() {
        KirjakokoelmaModel kokoelma = new KirjakokoelmaModel(new JsonKirjaRepository(Path.of("testikirjat.json")));
        kokoelma.lisaaKirja(" ", " ", " ");
        assertEquals(0, kokoelma.getKirjat().size());
    }

    @Test
    void lisaaLainaus_lisaaLainauksenListaan() {
        LainakokoelmaModel kokoelma = new LainakokoelmaModel(new JsonLainausRepository(Path.of("testilainaukset.json")));
        kokoelma.lisaaLainaus(new KirjaModel("nimi", "tekijä", "1234"), "lainaaja");
        assertEquals(1, kokoelma.getLainaukset().size());
        assertEquals("lainaaja", kokoelma.getLainaukset().getFirst().getLainaajaNimi());
        assertEquals("tekijä", kokoelma.getLainaukset().getFirst().getTekija());
        assertEquals("nimi", kokoelma.getLainaukset().getFirst().getKirjaNimi());
    }

    @Test
    void poistaLainaus_poistaaLainauksenListaan() {
        LainakokoelmaModel kokoelma = new LainakokoelmaModel(new JsonLainausRepository(Path.of("testilainaukset.json")));
        kokoelma.lisaaLainaus(new KirjaModel("nimi", "tekijä", "1234"), "lainaaja");
        LainausModel kirja = kokoelma.getLainaukset().getFirst();
        kokoelma.poistaLainaus(kirja);
        assertEquals(0, kokoelma.getLainaukset().size());
    }

    @Test
    void lisaaLainaus_eiLisaaTyhjaaLainausta() {
        LainakokoelmaModel kokoelma = new LainakokoelmaModel(new JsonLainausRepository(Path.of("testilainaukset.json")));
        kokoelma.lisaaLainaus(new KirjaModel("", "", ""), "");
        assertEquals(0, kokoelma.getLainaukset().size());
    }
}
