package ntut.csie.service;

import ntut.csie.model.TokenRelationModel;
import java.util.List;

public interface TokenRelationService {
    TokenRelationModel getRelationById(Long id);

    List<TokenRelationModel> getRelationsBySubscriberId(Long subscriberId);

    TokenRelationModel getRelation(Long subscriberId, Long tokenId);

    void delete(Long id);

    TokenRelationModel save(TokenRelationModel tr);
}
