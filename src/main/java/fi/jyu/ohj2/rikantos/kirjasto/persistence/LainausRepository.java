package fi.jyu.ohj2.rikantos.kirjasto.persistence;

import fi.jyu.ohj2.rikantos.kirjasto.model.LainausModel;
import java.util.List;

public interface LainausRepository {
    List<LainausModel> lataa() throws RepositoryException;
    void tallenna(List<LainausModel> lainaukset) throws RepositoryException;
}
