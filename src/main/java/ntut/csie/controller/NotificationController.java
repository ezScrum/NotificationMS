package ntut.csie.controller;

import ntut.csie.repository.TokenRelationRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import ntut.csie.model.*;
import ntut.csie.service.SubscriberService;
import ntut.csie.service.TokenService;
import ntut.csie.service.TokenRelationService;


@RestController
@RequestMapping(path = "notify")
public class NotificationController {
    @Autowired
    private TokenRelationRepository tokenRelationRepository;

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TokenRelationService tokenRelationService;

    @RequestMapping(method = RequestMethod.POST, path ="/subscribe", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String doSubscript(@RequestBody Map<String, String> payload) throws JSONException{
        String username = payload.get("username");
        String tokenString = payload.get("token");
        //Subscriber
        Subscriber subscriber = subscriberService.findSubscriberByUsername(username);
        Long subscriberId;
        if(subscriber == null){
            subscriber = new Subscriber();
            subscriber.setUsername(username);
            subscriberId = subscriberService.save(subscriber).getId();
        }
        else{
            subscriberId = subscriber.getId();
        }

        //Token
        TokenModel _token = tokenService.getTokenByTokenString(tokenString);
        Long tokenId;
        if(_token == null){
            _token = new TokenModel();
            _token.setToken(tokenString);
            tokenId = tokenService.save(_token).getId();
        }
        else{
            tokenId = _token.getId();
        }

        //Mapping
        TokenRelationModel tokenRelationModel = tokenRelationService.getRelation(subscriberId,tokenId);
        if(tokenRelationModel == null){
            tokenRelationModel = new TokenRelationModel();
            tokenRelationModel.setSubscriberId(subscriberId);
            tokenRelationModel.setTokenId(tokenId);
            tokenRelationModel.setLogon(true);
            tokenRelationService.save(tokenRelationModel);
            return "Success";
        }
        else{
            return "The subscript is exist.";
        }

    }

    @RequestMapping(method = RequestMethod.POST, path ="/cancelSubscribe", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String doUnSubscript(@RequestBody Map<String, String> payload) throws JSONException {
        String username = payload.get("username");
        String tokenString = payload.get("token");
        Subscriber subscriber = subscriberService.findSubscriberByUsername(username);
        Long subscriberId;
        TokenModel _token = tokenService.getTokenByTokenString(tokenString);
        Long tokenId;
        if(subscriber != null && _token != null){
            subscriberId = subscriber.getId();
            tokenId = _token.getId();
            TokenRelationModel tokenRelationModel = tokenRelationService.getRelation(subscriberId,tokenId);
            if(tokenRelationModel != null){
                tokenRelationService.delete(tokenRelationModel.getId());
                return "Success";
            }
            return "Cancel subscribe fail.";
        }
        else{
            return "Cancel subscribe fail.";
        }
    }

    @RequestMapping(method = RequestMethod.POST, path ="/notifyLogon", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String NotifyLogon(@RequestBody Map<String, String> payload) throws JSONException {
        String username = payload.get("username");
        String tokenString = payload.get("token");
        Subscriber subscriber = subscriberService.findSubscriberByUsername(username);
        Long subscriberId;
        TokenModel _token = tokenService.getTokenByTokenString(tokenString);
        Long tokenId;
        if(subscriber != null && _token != null){
            subscriberId = subscriber.getId();
            tokenId = _token.getId();
            TokenRelationModel tokenRelationModel = tokenRelationService.getRelation(subscriberId,tokenId);
            if(tokenRelationModel != null){
                tokenRelationModel.setLogon(true);
                tokenRelationRepository.save(tokenRelationModel);
                return "Subscription";
            }
            return "No-Subscription";
        }
        else{
            return "No-Subscription";
        }
    }

    @RequestMapping(method = RequestMethod.POST, path ="/notifyLogout", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String NotifyLogOut(@RequestBody Map<String, String> payload) throws JSONException {
        String username = payload.get("username");
        String tokenString = payload.get("token");
        Subscriber subscriber = subscriberService.findSubscriberByUsername(username);
        Long subscriberId;
        TokenModel _token = tokenService.getTokenByTokenString(tokenString);
        Long tokenId;
        if(subscriber != null && _token != null){
            subscriberId = subscriber.getId();
            tokenId = _token.getId();

            TokenRelationModel tokenRelationModel = tokenRelationService.getRelation(subscriberId,tokenId);
            if(tokenRelationModel != null){
                tokenRelationModel.setLogon(false);
                tokenRelationRepository.save(tokenRelationModel);
            }
        }
        return "Success";
    }

    @RequestMapping(method = RequestMethod.POST, path ="/send", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String SendNotification(@RequestBody Map<String, String> payload) throws JSONException {
        String receivers = payload.get("receivers");
        ArrayList<Subscriber> subscribers = getSubscriptReceivers(receivers);
        String messageTitle = payload.get("tittle");
        String messageBody = payload.get("body");
        String eventSource = payload.get("eventSource");
        FCMSenderModel sm = new FCMSenderModel();
        sm.setTitle(messageTitle);
        sm.setBody(messageBody);
        sm.setUrl(eventSource);

        //boolean allSuccess = SendTestBySender(sender,  sm);
        boolean allSuccess = SendMessage(subscribers,sm);
        if(allSuccess)
            return "Success";
        else
            return "Send notification fail.";
    }

    private ArrayList<Subscriber> getSubscriptReceivers(String receivers){
        ArrayList<Subscriber> subscribers = new ArrayList<Subscriber>();
        try{
            JSONArray jsonArray = new JSONArray(receivers);
            ArrayList<String> receiversName  = new ArrayList<String>();
            for (int index =0; index < jsonArray.length(); index++){
                receiversName.add(jsonArray.getString(index));
            }
            if(receiversName.isEmpty())
                return null;
            for(String receiverName:receiversName){
                Subscriber s = subscriberService.findSubscriberByUsername(receiverName);
                if(s != null)
                    subscribers.add(s);
            }
        }catch(JSONException e){
            System.out.println(e);
            return null;
        }
        return subscribers;
    }

    private boolean Send (List<Long> tokenIds, FCMSenderModel sm){
        boolean allSuccess = true;
        for (Long tokenId : tokenIds){
            TokenModel _token = tokenService.getTokenById(tokenId);

            if(_token.getToken().contains("TestToken")){
                //For test
                allSuccess &= true;
            }
            else{
                sm.setToken(_token.getToken());
                String s = sm.send();
                if(s.contains("Success"))
                    allSuccess &= true;
                else
                    allSuccess &= false;
            }
        }
        return allSuccess;
    }

    private boolean SendMessage(List<Subscriber> subscribers,  FCMSenderModel sm){
        List<Long> tokenIds = new ArrayList<Long>();
        if(subscribers == null || subscribers.isEmpty())
            return false;
        for(Subscriber subscriber : subscribers){
            Long subscriberId = subscriber.getId();
            List<TokenRelationModel> tokenRelationModel = tokenRelationService.getRelationsBySubscriberId(subscriberId);
            for (TokenRelationModel trm : tokenRelationModel){
                if(trm.getLogon() && !tokenIds.contains(trm.getTokenId())){
                    tokenIds.add(trm.getTokenId());
                }
            }
        }
        return Send(tokenIds, sm);
    }
}
