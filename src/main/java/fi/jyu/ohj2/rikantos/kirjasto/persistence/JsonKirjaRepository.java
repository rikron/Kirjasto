package fi.jyu.ohj2.rikantos.kirjasto.persistence;

import fi.jyu.ohj2.rikantos.kirjasto.model.KirjaModel;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JsonKirjaRepository implements KirjaRepository{
    private final Path tallennustiedosto;
    private final ObjectMapper mapper = new ObjectMapper();

    public JsonKirjaRepository(Path tallennustiedosto) {
        this.tallennustiedosto = tallennustiedosto;
    }

    @Override
    public List<KirjaModel> lataa() throws JacksonException {
        if (Files.notExists(tallennustiedosto)) {
            return List.of();
        }
        return mapper.readValue(tallennustiedosto.toFile(), new TypeReference<>() {});
    }

    @Override
    public void tallenna(List<KirjaModel> kirjat) throws JacksonException {
        mapper.writeValue(tallennustiedosto.toFile(), kirjat);
    }
}
