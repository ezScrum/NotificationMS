package ntut.csie.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TokenModelTest {
    private TokenModel tokenModel = new TokenModel();

    @After
    public void teardown(){
        tokenModel = new TokenModel();
    }

    @Test
    public void TestTokenModel(){
        tokenModel.setToken("testToken");
        tokenModel.setId(Integer.toUnsignedLong(1));
        Assert.assertEquals(Integer.toUnsignedLong(1), tokenModel.getId().longValue());
        Assert.assertEquals("testToken", tokenModel.getToken());
    }
}