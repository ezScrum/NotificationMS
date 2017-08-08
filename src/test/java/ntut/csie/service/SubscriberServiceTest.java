package ntut.csie.service;

import ntut.csie.model.SubscriberModel;
import ntut.csie.repository.SubscriberRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscriberServiceTest {
    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Before
    public void setup(){
        SubscriberModel subscriberModel = new SubscriberModel();
        subscriberModel.setId(Integer.toUnsignedLong(1));
        subscriberModel.setUsername("user_1");
        subscriberRepository.save(subscriberModel);
    }

    @After
    public void teardown(){
        subscriberRepository.deleteAll();
    }

    @Test
    public void TestFindSubscriberByUsername(){
        SubscriberModel subscriberModel = subscriberService.findSubscriberByUsername("user_1");
        Assert.assertNotNull(subscriberModel);
        Assert.assertEquals("user_1", subscriberModel.getUsername());
    }

    @Test
    public void TestGetSubscriberList(){
        List<SubscriberModel> subscriberModels = subscriberService.getSubscriberList();
        Assert.assertEquals(1, subscriberModels.size());
        Assert.assertEquals("user_1", subscriberModels.get(0).getUsername());
    }

    @Test
    public void TestDelete(){
        SubscriberModel subscriberModel = subscriberService.findSubscriberByUsername("user_1");
        Assert.assertNotNull(subscriberModel);
        subscriberService.delete(subscriberModel.getId());
        subscriberModel = subscriberService.findSubscriberByUsername("user_1");
        Assert.assertNull(subscriberModel);
    }

    @Test
    public void TestSave(){
        SubscriberModel subscriberModel = new SubscriberModel();
        subscriberModel.setUsername("user_2");
        subscriberService.save(subscriberModel);
        SubscriberModel _subscriberModel = subscriberService.findSubscriberByUsername("user_2");
        Assert.assertNotNull(_subscriberModel);
        Assert.assertEquals("user_2", _subscriberModel.getUsername());
    }

    @Test
    public void TestFindUserById(){
        SubscriberModel subscriber_Model_1 = subscriberService.findSubscriberByUsername("user_1");
        Assert.assertNotNull(subscriber_Model_1);

        SubscriberModel subscriber_Model_2 = subscriberService.findUserById(subscriber_Model_1.getId());
        Assert.assertNotNull(subscriber_Model_2);
        Assert.assertEquals(subscriber_Model_1.getUsername(), subscriber_Model_2.getUsername());
        Assert.assertEquals(subscriber_Model_1.getId(), subscriber_Model_2.getId());
    }

}