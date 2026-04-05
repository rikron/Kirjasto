package fi.jyu.ohj2.rikantos.kirjasto.persistence;

import fi.jyu.ohj2.rikantos.kirjasto.model.KirjaModel;
import java.util.List;

public interface KirjaRepository {
    List<KirjaModel> lataa() throws RepositoryException;
    void tallenna(List<KirjaModel> kirjat) throws RepositoryException;
}
