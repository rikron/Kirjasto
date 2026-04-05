package fi.jyu.ohj2.rikantos.kirjasto.persistence;

import fi.jyu.ohj2.rikantos.kirjasto.model.LainausModel;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JsonLainausRepository implements LainausRepository{
    private final Path tallennustiedosto;
    private final ObjectMapper mapper = new ObjectMapper();

    public JsonLainausRepository(Path tallennustiedosto) {
        this.tallennustiedosto = tallennustiedosto;
    }

    @Override
    public List<LainausModel> lataa() throws JacksonException {
        if (Files.notExists(tallennustiedosto)) {
            return List.of();
        }
        return mapper.readValue(tallennustiedosto.toFile(), new TypeReference<>() {});
    }

    @Override
    public void tallenna(List<LainausModel> lainaukset) throws JacksonException {
        mapper.writeValue(tallennustiedosto.toFile(), lainaukset);
    }
}
