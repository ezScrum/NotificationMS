package ntut.csie.service;

import ntut.csie.model.Subscriber;
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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscriberServiceTest {
    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Before
    public void setup(){
        Subscriber subscriber = new Subscriber();
        subscriber.setId(Integer.toUnsignedLong(1));
        subscriber.setUsername("user_1");
        subscriberRepository.save(subscriber);
    }

    @After
    public void teardown(){
        subscriberRepository.deleteAll();
    }

    @Test
    public void TestFindSubscriberByUsername(){
        Subscriber subscriber = subscriberService.findSubscriberByUsername("user_1");
        Assert.assertNotNull(subscriber);
        Assert.assertEquals("user_1", subscriber.getUsername());
    }

    @Test
    public void TestGetSubscriberList(){
        List<Subscriber> subscribers = subscriberService.getSubscriberList();
        Assert.assertEquals(1, subscribers.size());
        Assert.assertEquals("user_1", subscribers.get(0).getUsername());
    }

    @Test
    public void TestDelete(){
        Subscriber subscriber = subscriberService.findSubscriberByUsername("user_1");
        Assert.assertNotNull(subscriber);
        subscriberService.delete(subscriber.getId());
        subscriber = subscriberService.findSubscriberByUsername("user_1");
        Assert.assertNull(subscriber);
    }

    @Test
    public void TestSave(){
        Subscriber subscriber = new Subscriber();
        subscriber.setUsername("user_2");
        subscriberService.save(subscriber);
        Subscriber _subscriber = subscriberService.findSubscriberByUsername("user_2");
        Assert.assertNotNull(_subscriber);
        Assert.assertEquals("user_2", _subscriber.getUsername());
    }

    @Test
    public void TestFindUserById(){
        Subscriber subscriber_1 = subscriberService.findSubscriberByUsername("user_1");
        Assert.assertNotNull(subscriber_1);

        Subscriber subscriber_2 = subscriberService.findUserById(subscriber_1.getId());
        Assert.assertNotNull(subscriber_2);
        Assert.assertEquals(subscriber_1.getUsername(), subscriber_2.getUsername());
        Assert.assertEquals(subscriber_1.getId(), subscriber_2.getId());
    }

}