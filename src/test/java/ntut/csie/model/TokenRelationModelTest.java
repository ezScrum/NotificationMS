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
public class TokenRelationModelTest {
    private TokenRelationModel tokenRelationModel = new TokenRelationModel();

    @After
    public void teardown(){
        tokenRelationModel = new TokenRelationModel();
    }

    @Test
    public void TestTokenRelationModel(){
        tokenRelationModel.setSubscriberId(Integer.toUnsignedLong(1));
        tokenRelationModel.setTokenId(Integer.toUnsignedLong(1));
        tokenRelationModel.setId(Integer.toUnsignedLong(1));
        tokenRelationModel.setLogon(true);
        Assert.assertEquals(Integer.toUnsignedLong(1), tokenRelationModel.getSubscriberId().longValue());
        Assert.assertEquals(Integer.toUnsignedLong(1), tokenRelationModel.getTokenId().longValue());
        Assert.assertEquals(Integer.toUnsignedLong(1), tokenRelationModel.getId().longValue());
        Assert.assertTrue(tokenRelationModel.getLogon());
    }
}