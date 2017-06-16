package ntut.csie.repository;

import ntut.csie.model.TokenModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<TokenModel, Long> {
    TokenModel findTokenByToken(String token);
}
