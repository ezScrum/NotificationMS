package ntut.csie.service;

import ntut.csie.model.TokenRelationModel;
import ntut.csie.repository.TokenRelationRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TokenRelationServiceTest {
    @Autowired
    private TokenRelationService tokenRelationService;

    @Autowired
    private TokenRelationRepository tokenRelationRepository;

    @Before
    public void setup(){
        TokenRelationModel tokenRelationModel = new TokenRelationModel();
        tokenRelationModel.setId(Integer.toUnsignedLong(1));
        tokenRelationModel.setTokenId(Integer.toUnsignedLong(1));
        tokenRelationModel.setSubscriberId(Integer.toUnsignedLong(1));
        tokenRelationModel.setLogon(true);
        tokenRelationRepository.save(tokenRelationModel);
    }

    @After
    public void teardown(){
        tokenRelationRepository.deleteAll();
    }

    @Test
    public void TestGetRelationById(){
        TokenRelationModel tokenRelationModel = tokenRelationService.getRelationById(Integer.toUnsignedLong(1));
        Assert.assertNotNull(tokenRelationModel);
        Assert.assertEquals(Integer.toUnsignedLong(1), tokenRelationModel.getSubscriberId().longValue());
        Assert.assertEquals(Integer.toUnsignedLong(1), tokenRelationModel.getId().longValue());
        Assert.assertEquals(Integer.toUnsignedLong(1), tokenRelationModel.getTokenId().longValue());
        Assert.assertTrue(tokenRelationModel.getLogon());
    }

    @Test
    public void TestGetRelationsBySubscriberId(){
        List<TokenRelationModel> tokenRelationsModel = tokenRelationService
                .getRelationsBySubscriberId(Integer.toUnsignedLong(1));
        Assert.assertNotNull(tokenRelationsModel);
        Assert.assertEquals(1, tokenRelationsModel.size());
        Assert.assertEquals(Integer.toUnsignedLong(1), tokenRelationsModel.get(0).getSubscriberId().longValue());
        Assert.assertEquals(Integer.toUnsignedLong(1), tokenRelationsModel.get(0).getTokenId().longValue());
        Assert.assertTrue(tokenRelationsModel.get(0).getLogon());
    }

    @Test
    public void TestGetRelation(){
        Long tokenId = Integer.toUnsignedLong(1);
        Long subscriberId = Integer.toUnsignedLong(1);
        TokenRelationModel tokenRelationModel = tokenRelationService.getRelation(subscriberId, tokenId);
        Assert.assertNotNull(tokenRelationModel);
        Assert.assertEquals(Integer.toUnsignedLong(1), tokenRelationModel.getSubscriberId().longValue());
        Assert.assertEquals(Integer.toUnsignedLong(1), tokenRelationModel.getTokenId().longValue());
        Assert.assertTrue(tokenRelationModel.getLogon());
    }

    @Test
    public void TestDelete(){
        Long tokenId = Integer.toUnsignedLong(1);
        Long subscriberId = Integer.toUnsignedLong(1);
        TokenRelationModel tokenRelationModel = tokenRelationService.getRelation(subscriberId, tokenId);
        Assert.assertNotNull(tokenRelationModel);
        tokenRelationService.delete(tokenRelationModel.getId());
        tokenRelationModel = tokenRelationService.getRelation(subscriberId, tokenId);
        Assert.assertNull(tokenRelationModel);
    }

    @Test
    public void TestSave(){
        TokenRelationModel tokenRelationModel = new TokenRelationModel();
        tokenRelationModel.setTokenId(Integer.toUnsignedLong(2));
        tokenRelationModel.setSubscriberId(Integer.toUnsignedLong(2));
        tokenRelationModel.setLogon(true);
        tokenRelationService.save(tokenRelationModel);
    }
}