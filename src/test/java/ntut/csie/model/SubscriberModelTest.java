package ntut.csie.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscriberModelTest {
    private SubscriberModel subscriberModel = new SubscriberModel();

    @After
    public void teardown(){
        subscriberModel = new SubscriberModel();
    }

    @Test
    public void TestSubscriber(){
        subscriberModel.setUsername("user_1");
        Assert.assertEquals("user_1", subscriberModel.getUsername());
        Long id = Integer.toUnsignedLong(1);
        subscriberModel.setId(id);
        Assert.assertEquals(Integer.toUnsignedLong(1), subscriberModel.getId().longValue());
    }
}