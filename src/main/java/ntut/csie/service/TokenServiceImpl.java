package ntut.csie.service;

import ntut.csie.model.TokenModel;
import ntut.csie.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public TokenModel getTokenById(Long id) {
        return tokenRepository.findOne(id);
    }

    @Override
    public TokenModel getTokenByTokenString(String tokenString) {
        return tokenRepository.findTokenByToken(tokenString);
    }

    @Override
    public void delete(Long id) {
        tokenRepository.delete(id);
    }

    @Override
    public TokenModel save(TokenModel token) {
        TokenModel tkn = new TokenModel();
        tkn.setToken(token.getToken());

        return tokenRepository.save(tkn);
    }
}
