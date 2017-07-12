package ntut.csie.service;

import ntut.csie.model.TokenModel;
import ntut.csie.repository.TokenRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TokenServiceTest {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private TokenRepository tokenRepository;

    @Before
    public void setup(){
        TokenModel tokenModel = new TokenModel();
        tokenModel.setId(Integer.toUnsignedLong(1));
        tokenModel.setToken("token_1");
        tokenRepository.save(tokenModel);
    }

    @After
    public void teardown(){
        tokenRepository.deleteAll();
    }

    @Test
    public void TestGetTokenById(){
        TokenModel tokenModel = tokenService.getTokenByTokenString("token_1");
        Assert.assertNotNull(tokenModel);
        TokenModel _tokenModel = tokenService.getTokenById(tokenModel.getId());
        Assert.assertNotNull(_tokenModel);
        Assert.assertEquals("token_1", _tokenModel.getToken());
    }

    @Test
    public void TestGetTokenByTokenString(){
        TokenModel tokenModel = tokenService.getTokenByTokenString("token_1");
        Assert.assertNotNull(tokenModel);
        Assert.assertEquals("token_1", tokenModel.getToken());
    }

    @Test
    public void TestDelete(){
        TokenModel tokenModel = tokenService.getTokenByTokenString("token_1");
        Assert.assertNotNull(tokenModel);
        tokenService.delete(tokenModel.getId());
        tokenModel = tokenService.getTokenByTokenString("token_1");
        Assert.assertNull(tokenModel);
    }

    @Test
    public void TestSave(){
        TokenModel tokenModel = new TokenModel();
        tokenModel.setToken("token_2");
        tokenService.save(tokenModel);
        TokenModel _tokenModel = tokenService.getTokenByTokenString("token_2");
        Assert.assertNotNull(_tokenModel);
        Assert.assertEquals("token_2", _tokenModel.getToken());
    }
}