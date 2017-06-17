package ntut.csie.service;

import ntut.csie.model.TokenRelationModel;
import ntut.csie.repository.TokenRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenRelationServiceImpl implements TokenRelationService{
    @Autowired
    private TokenRelationRepository tokenRelationRepository;

    @Override
    public TokenRelationModel getRelationById(Long id){return tokenRelationRepository.findOne(id);}

    @Override
    public List<TokenRelationModel> getRelationsBySubscriberId(Long subscriberId){
        return tokenRelationRepository.findAllBySubscriberid(subscriberId);
    }

    @Override
    public TokenRelationModel getRelation(Long subscriberId, Long tokenId){
        return  tokenRelationRepository.findBySubscriberidAndTokenid(subscriberId, tokenId);
    }

    @Override
    public void delete(Long id){tokenRelationRepository.delete(id);}

    @Override
    public TokenRelationModel save(TokenRelationModel tokenRelationModel){
        TokenRelationModel trm = new TokenRelationModel();
        trm.setSubscriberId(tokenRelationModel.getSubscriberId());
        trm.setTokenId(tokenRelationModel.getTokenId());
        trm.setLogon(tokenRelationModel.getLogon());
        return tokenRelationRepository.save(trm);
    }
}
