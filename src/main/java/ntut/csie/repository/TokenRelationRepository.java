package ntut.csie.repository;

import ntut.csie.model.TokenRelationModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRelationRepository extends JpaRepository<TokenRelationModel, Long>{
    List<TokenRelationModel> findAllBySubscriberid(Long subscriberid);
    TokenRelationModel findBySubscriberidAndTokenid(Long subscriberid, Long tokenid);
}
