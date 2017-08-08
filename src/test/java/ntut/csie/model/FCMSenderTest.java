package ntut.csie.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FCMSenderTest {
    private FCMSender fcmSender = new FCMSender();

    @After
    public void teardown(){
        fcmSender = new FCMSender();
    }

    @Test
    public void TestFCMSenderModel(){
        fcmSender.setTitle("title");
        fcmSender.setBody("body");
        fcmSender.setUrl("url");
        fcmSender.setToken("token");

        Assert.assertEquals("title", fcmSender.getTitle());
        Assert.assertEquals("body", fcmSender.getBody());
        Assert.assertEquals("url", fcmSender.getUrl());
        Assert.assertEquals("token", fcmSender.getToken());

        String s = fcmSender.send();
        Assert.assertEquals("Fail", s);
        String response = fcmSender.getResponse();
        try{
            JSONObject result = new JSONObject(response.toString());
            Assert.assertEquals(false, result.getBoolean("success"));
            Assert.assertEquals(true, result.getBoolean("failure"));
            Assert.assertEquals(0, result.getInt("canonical_ids"));
            JSONArray array = result.getJSONArray("results");
            Assert.assertEquals("InvalidRegistration", array.getJSONObject(0).getString("error"));
        }catch (JSONException e){}
    }
}