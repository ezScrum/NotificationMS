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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FCMSenderModelTest {
    private FCMSenderModel fcmSenderModel = new FCMSenderModel();

    @After
    public void teardown(){
        fcmSenderModel = new FCMSenderModel();
    }

    @Test
    public void TestFCMSenderModel(){
        fcmSenderModel.setTitle("title");
        fcmSenderModel.setBody("body");
        fcmSenderModel.setUrl("url");
        fcmSenderModel.setToken("token");

        Assert.assertEquals("title", fcmSenderModel.getTitle());
        Assert.assertEquals("body", fcmSenderModel.getBody());
        Assert.assertEquals("url", fcmSenderModel.getUrl());
        Assert.assertEquals("token", fcmSenderModel.getToken());

        String s = fcmSenderModel.send();
        Assert.assertEquals("Fail", s);
        String response = fcmSenderModel.getResponse();
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