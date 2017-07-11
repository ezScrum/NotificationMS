package ntut.csie.controller;

import ntut.csie.service.*;
import ntut.csie.model.Subscriber;
import ntut.csie.model.TokenModel;
import ntut.csie.model.TokenRelationModel;
import ntut.csie.repository.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import static com.jayway.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NotificationControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TokenRelationService tokenRelationService;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private  TokenRelationRepository tokenRelationRepository;

    @Before
    public void setup(){
        Subscriber subscriber = new Subscriber();
        subscriber.setUsername("Test_1");
        subscriberService.save(subscriber);

        TokenModel tokenModel = new TokenModel();
        tokenModel.setToken("testToken_1");
        tokenService.save(tokenModel);

        TokenRelationModel trm = new TokenRelationModel();
        Subscriber s = subscriberService.findSubscriberByUsername("Test_1");
        TokenModel tm = tokenService.getTokenByTokenString("testToken_1");
        trm.setSubscriberId(s.getId());
        trm.setTokenId(tm.getId());
        trm.setLogon(false);
        tokenRelationService.save(trm);
    }

    @After
    public void teardown(){
        subscriberRepository.deleteAll();
        tokenRepository.deleteAll();
        tokenRelationRepository.deleteAll();
    }

    @Test
    public void TestSubcribe_newSubscriber(){
        JSONObject json = new JSONObject();
        try{
            json.put("username","Test_2");
            json.put("token","testToken_2");
        }catch (JSONException e){
        }

        String response = given().port(port).contentType("application/json")
                .body(json.toString())
                .when().post("/notify/subscribe")
                .andReturn().asString();

        Subscriber subscriber = subscriberService.findSubscriberByUsername("Test_2");
        TokenModel tokenModel = tokenService.getTokenByTokenString("testToken_2");

        Assert.assertNotNull(subscriber);
        Assert.assertNotNull(tokenModel);

        TokenRelationModel trm = tokenRelationService.getRelation(subscriber.getId(), tokenModel.getId());
        Assert.assertNotNull(trm);
        Assert.assertTrue(trm.getLogon());
        Assert.assertEquals("Success",response);
    }

    @Test
    public void TestSubcribe_sameSubscriber(){
        JSONObject json = new JSONObject();
        try{
            json.put("username","Test_1");
            json.put("token","testToken_2");
        }catch (JSONException e){
        }

        String response = given().port(port).contentType("application/json")
                .body(json.toString())
                .when().post("/notify/subscribe")
                .andReturn().asString();

        Assert.assertEquals("Success",response);

        Subscriber subscriber = subscriberService.findSubscriberByUsername("Test_1");
        TokenModel tokenModel = tokenService.getTokenByTokenString("testToken_2");
        Assert.assertNotNull(subscriber);
        Assert.assertNotNull(tokenModel);

        TokenRelationModel trm = tokenRelationService.getRelation(subscriber.getId(), tokenModel.getId());
        Assert.assertNotNull(trm);
        Assert.assertTrue(trm.getLogon());

        tokenModel = tokenService.getTokenByTokenString("testToken_1");
        Assert.assertNotNull(tokenModel);
        trm = tokenRelationService.getRelation(subscriber.getId(), tokenModel.getId());
        Assert.assertNotNull(trm);
        Assert.assertFalse(trm.getLogon());
    }

    @Test
    public void TestSubcribe_sameToken(){
        JSONObject json = new JSONObject();
        try{
            json.put("username","Test_2");
            json.put("token","testToken_1");
        }catch (JSONException e){
        }

        String response = given().port(port).contentType("application/json")
                .body(json.toString())
                .when().post("/notify/subscribe")
                .andReturn().asString();

        Assert.assertEquals("Success",response);

        Subscriber subscriber = subscriberService.findSubscriberByUsername("Test_2");
        TokenModel tokenModel = tokenService.getTokenByTokenString("testToken_1");
        Assert.assertNotNull(subscriber);
        Assert.assertNotNull(tokenModel);

        TokenRelationModel trm = tokenRelationService.getRelation(subscriber.getId(), tokenModel.getId());
        Assert.assertNotNull(trm);
        Assert.assertTrue(trm.getLogon());

        subscriber = subscriberService.findSubscriberByUsername("Test_1");
        Assert.assertNotNull(subscriber);
        trm = tokenRelationService.getRelation(subscriber.getId(), tokenModel.getId());
        Assert.assertNotNull(trm);
        Assert.assertFalse(trm.getLogon());
    }

    @Test
    public void TestSubcribe_SubscriptIsExist(){
        JSONObject json = new JSONObject();
        try{
            json.put("username","Test_1");
            json.put("token","testToken_1");
        }catch (JSONException e){
        }

        String response = given().port(port).contentType("application/json")
                .body(json.toString())
                .when().post("/notify/subscribe")
                .andReturn().asString();

        Assert.assertEquals("The subscript is exist.",response);
    }

    @Test
    public void TestCancelSubscribe(){
        JSONObject json = new JSONObject();
        try{
            json.put("username","Test_1");
            json.put("token","testToken_1");
        }catch (JSONException e){
        }

        String response = given().port(port).contentType("application/json")
                .body(json.toString())
                .when().post("/notify/cancelSubscribe")
                .andReturn().asString();

        Subscriber subscriber = subscriberService.findSubscriberByUsername("Test_1");
        TokenModel tokenModel = tokenService.getTokenByTokenString("testToken_1");

        Assert.assertNotNull(subscriber);
        Assert.assertNotNull(tokenModel);

        TokenRelationModel trm = tokenRelationService.getRelation(subscriber.getId(), tokenModel.getId());
        Assert.assertNull(trm);
        Assert.assertEquals("Success",response);
    }

    @Test
    public void TestCancelSubscribe_error(){
        JSONObject ckeckJson1 = new JSONObject();
        try{
            ckeckJson1.put("username","Test_2");
            ckeckJson1.put("token","testToken_2");
        }catch (JSONException e){
        }

        String response = given().port(port).contentType("application/json")
                .body(ckeckJson1.toString())
                .when().post("/notify/cancelSubscribe")
                .andReturn().asString();

        Assert.assertEquals("Cancel subscribe fail.",response);

        JSONObject subscribeJson = new JSONObject();
        try{
            subscribeJson.put("username","Test_2");
            subscribeJson.put("token","testToken_2");
        }catch (JSONException e){
        }

        response = given().port(port).contentType("application/json")
                .body(subscribeJson.toString())
                .when().post("/notify/subscribe")
                .andReturn().asString();

        Assert.assertEquals("Success",response);

        Subscriber subscriber = subscriberService.findSubscriberByUsername("Test_1");
        TokenModel tokenModel = tokenService.getTokenByTokenString("testToken_2");
        Assert.assertNotNull(subscriber);
        Assert.assertNotNull(tokenModel);
        TokenRelationModel trm = tokenRelationService.getRelation(subscriber.getId(), tokenModel.getId());
        Assert.assertNull(trm);
        subscriber = subscriberService.findSubscriberByUsername("Test_2");
        tokenModel = tokenService.getTokenByTokenString("testToken_1");
        Assert.assertNotNull(subscriber);
        Assert.assertNotNull(tokenModel);
        trm = tokenRelationService.getRelation(subscriber.getId(), tokenModel.getId());
        Assert.assertNull(trm);

        JSONObject checkJson2 = new JSONObject();
        try{
            checkJson2.put("username","Test_2");
            checkJson2.put("token","testToken_1");
        }catch (JSONException e){
        }

        response = given().port(port).contentType("application/json")
                .body(checkJson2.toString())
                .when().post("/notify/cancelSubscribe")
                .andReturn().asString();
        Assert.assertEquals("Cancel subscribe fail.",response);
    }

    @Test
    public void TestNotifyLogin(){
        JSONObject json = new JSONObject();
        try{
            json.put("username","Test_1");
            json.put("token","testToken_1");
        }catch (JSONException e){
        }

        String response = given().port(port).contentType("application/json")
                .body(json.toString())
                .when().post("/notify/notifyLogon")
                .andReturn().asString();

        Subscriber subscriber = subscriberService.findSubscriberByUsername("Test_1");
        TokenModel tokenModel = tokenService.getTokenByTokenString("testToken_1");

        Assert.assertNotNull(subscriber);
        Assert.assertNotNull(tokenModel);
        TokenRelationModel trm = tokenRelationService.getRelation(subscriber.getId(), tokenModel.getId());
        Assert.assertNotNull(trm);
        Assert.assertTrue(trm.getLogon());
        Assert.assertEquals("Subscription",response);
    }

    @Test
    public void TestNotifyLogin_noSubscription_NotHaveSubscriberOrToken(){
        JSONObject json = new JSONObject();
        try{
            json.put("username","Test_2");
            json.put("token","testToken_2");
        }catch (JSONException e){
        }

        String response = given().port(port).contentType("application/json")
                .body(json.toString())
                .when().post("/notify/notifyLogon")
                .andReturn().asString();

        Assert.assertEquals("No-Subscription",response);
        Subscriber subscriber = subscriberService.findSubscriberByUsername("Test_2");
        TokenModel tokenModel = tokenService.getTokenByTokenString("testToken_2");

        Assert.assertNull(subscriber);
        Assert.assertNull(tokenModel);
    }

    @Test
    public void TestNotifyLogin_noSubscription_HaveSubscriberOrToken(){
        JSONObject subscribeJson = new JSONObject();
        try{
            subscribeJson.put("username","Test_2");
            subscribeJson.put("token","testToken_2");
        }catch (JSONException e){
        }

        String response = given().port(port).contentType("application/json")
                .body(subscribeJson.toString())
                .when().post("/notify/subscribe")
                .andReturn().asString();
        Assert.assertEquals("Success",response);
        Subscriber subscriber = subscriberService.findSubscriberByUsername("Test_2");
        TokenModel tokenModel = tokenService.getTokenByTokenString("testToken_2");
        Assert.assertNotNull(subscriber);
        Assert.assertNotNull(tokenModel);
        TokenRelationModel trm = tokenRelationService.getRelation(subscriber.getId(), tokenModel.getId());
        Assert.assertNotNull(trm);
        Assert.assertTrue(trm.getLogon());

        JSONObject ckeckJson1 = new JSONObject();
        try{
            ckeckJson1.put("username","Test_2");
            ckeckJson1.put("token","testToken_1");
        }catch (JSONException e){
        }
        response = given().port(port).contentType("application/json")
                .body(ckeckJson1.toString())
                .when().post("/notify/notifyLogon")
                .andReturn().asString();
        Assert.assertEquals("No-Subscription",response);

        JSONObject ckeckJson2 = new JSONObject();
        try{
            ckeckJson2.put("username","Test_1");
            ckeckJson2.put("token","testToken_2");
        }catch (JSONException e){
        }
        response = given().port(port).contentType("application/json")
                .body(ckeckJson2.toString())
                .when().post("/notify/notifyLogon")
                .andReturn().asString();
        Assert.assertEquals("No-Subscription",response);
    }

    @Test
    public void TestNotifyLogout(){
        JSONObject json = new JSONObject();
        try{
            json.put("username","Test_1");
            json.put("token","testToken_1");
        }catch (JSONException e){
        }

        given().port(port).contentType("application/json")
                .body(json.toString())
                .when().post("/notify/notifyLogon");

        Subscriber subscriber = subscriberService.findSubscriberByUsername("Test_1");
        TokenModel tokenModel = tokenService.getTokenByTokenString("testToken_1");
        TokenRelationModel trm = tokenRelationService.getRelation(subscriber.getId(), tokenModel.getId());

        Assert.assertNotNull(trm);
        Assert.assertTrue(trm.getLogon());

        given().port(port).contentType("application/json")
                .body(json.toString())
                .when().post("/notify/notifyLogout");

        trm = tokenRelationService.getRelation(subscriber.getId(), tokenModel.getId());
        Assert.assertNotNull(trm);
        Assert.assertFalse(trm.getLogon());
    }

    @Test
    public void TestSendMessage(){
         JSONObject message =  new JSONObject();
        try{
            JSONArray array = new JSONArray();
            array.put("Test_1");
            message.put("receivers",array.toString());
            message.put("tittle","title");
            message.put("body","body");
            message.put("eventSource","eventSource");
        }catch (JSONException e){
        }

        String response = given().port(port).contentType("application/json")
                .body(message.toString())
                .when().post("/notify/send")
                .andReturn().asString();

        Assert.assertEquals("Success",response);
    }

    @Test
    public void TestSendMessage_Fail(){
        JSONObject json = new JSONObject();
        try{
            json.put("username","Test_2");
            json.put("token","TestTest");
        }catch (JSONException e){
        }

        given().port(port).contentType("application/json")
                .body(json.toString())
                .when().post("/notify/subscribe")
                .then().statusCode(200);

        JSONObject message =  new JSONObject();
        try{
            JSONArray array = new JSONArray();
            array.put("Test_1");
            array.put("Test_2");
            message.put("receivers",array.toString());
            message.put("tittle","title");
            message.put("body","body");
            message.put("eventSource","eventSource");
        }catch (JSONException e){
        }

        String response = given().port(port).contentType("application/json")
                .body(message.toString())
                .when().post("/notify/send")
                .andReturn().asString();

        Assert.assertEquals("Send notification fail.",response);
    }

    @Test
    public void TestSendMessage_notHaveReceivers(){
        JSONObject message =  new JSONObject();
        try{
            JSONArray array = new JSONArray();
            message.put("receivers",array.toString());
            message.put("tittle","title");
            message.put("body","body");
            message.put("eventSource","eventSource");
        }catch (JSONException e){
        }

        String response = given().port(port).contentType("application/json")
                .body(message.toString())
                .when().post("/notify/send")
                .andReturn().asString();

        Assert.assertEquals("Send notification fail.",response);
    }
}
