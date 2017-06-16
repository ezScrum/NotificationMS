package ntut.csie.service;

import ntut.csie.model.TokenModel;

public interface TokenService {
    TokenModel getTokenById(Long id);

    TokenModel getTokenByTokenString(String tokenString);

    void delete(Long id);

    TokenModel save(TokenModel token);
}
