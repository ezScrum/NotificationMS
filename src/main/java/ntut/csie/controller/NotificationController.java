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

    @RequestMapping(method = RequestMethod.POST, path ="/subscript", produces = MediaType.APPLICATION_JSON_VALUE)
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
        }
        return "Success";
    }

    @RequestMapping(method = RequestMethod.POST, path ="/unSubscript", produces = MediaType.APPLICATION_JSON_VALUE)
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
            return "fail";
        }
        else{
            return "fail";
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
                return "Subscript";
            }
            return "UnSubscript";
        }
        else{
            return "UnSubscript";
        }
    }

    @RequestMapping(method = RequestMethod.POST, path ="/notifyLogOut", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String NotifyLogOut(@RequestBody Map<String, String> payload) throws JSONException {
        String username = payload.get("username");
        String tokenString = payload.get("token");
        System.out.println(username);
        System.out.println(tokenString);
        Subscriber subscriber = subscriberService.findSubscriberByUsername(username);
        Long subscriberId;
        TokenModel _token = tokenService.getTokenByTokenString(tokenString);
        Long tokenId;
        if(subscriber != null && _token != null){
            subscriberId = subscriber.getId();
            tokenId = _token.getId();

            //ToDo getUsernamesByProjectId
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
        String sender = payload.get("sender");
        String projectId = payload.get("projectId");
        String messageTitle = payload.get("messageTitle");
        String messageBody = payload.get("messageBody");
        String fromURL = payload.get("fromURL");
        FCMSenderModel sm = new FCMSenderModel();
        sm.setTitle(messageTitle);
        sm.setBody(messageBody);
        sm.setUrl(fromURL);
        boolean allSuccess = true;
        //Todo get username by projectId from account management service.
        Subscriber subscriber = subscriberService.findSubscriberByUsername(sender);
        Long subscriberId;

        List<Long> tokenIds = new ArrayList<Long>();

        if(subscriber != null){
            subscriberId = subscriber.getId();
            List<TokenRelationModel> tokenRelationModel = tokenRelationService.getRelationsBySubscriberId(subscriberId);
            for (TokenRelationModel trm : tokenRelationModel){
                if(trm.getLogon() && !tokenIds.contains(trm.getTokenId())){
                    tokenIds.add(trm.getTokenId());
                }
            }

            for (Long tokenId : tokenIds){
                TokenModel _token = tokenService.getTokenById(tokenId);
                sm.setToken(_token.getToken());
                String s = sm.send();
                if(s == "Success")
                    allSuccess &= true;
                else
                    allSuccess &= false;
            }
        }
        if(allSuccess)
            return "Success";
        else
            return "fail";
    }

    private List<String> getUserNames(Long projectId){
        List<String> usernames = new ArrayList<String>();
        return usernames;
    }
}
