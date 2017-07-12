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
public class SubscriberTest {
    private Subscriber subscriber = new Subscriber();

    @After
    public void teardown(){
        subscriber = new Subscriber();
    }

    @Test
    public void TestSubscriber(){
        subscriber.setUsername("user_1");
        Assert.assertEquals("user_1", subscriber.getUsername());
        Long id = Integer.toUnsignedLong(1);
        subscriber.setId(id);
        Assert.assertEquals(Integer.toUnsignedLong(1), subscriber.getId().longValue());
    }
}