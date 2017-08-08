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
        //SubscriberModel
        SubscriberModel subscriberModel = subscriberService.findSubscriberByUsername(username);
        Long subscriberId;
        if(subscriberModel == null){
            subscriberModel = new SubscriberModel();
            subscriberModel.setUsername(username);
            subscriberId = subscriberService.save(subscriberModel).getId();
        }
        else{
            subscriberId = subscriberModel.getId();
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
        SubscriberModel subscriberModel = subscriberService.findSubscriberByUsername(username);
        Long subscriberId;
        TokenModel _token = tokenService.getTokenByTokenString(tokenString);
        Long tokenId;
        if(subscriberModel != null && _token != null){
            subscriberId = subscriberModel.getId();
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
        SubscriberModel subscriberModel = subscriberService.findSubscriberByUsername(username);
        Long subscriberId;
        TokenModel _token = tokenService.getTokenByTokenString(tokenString);
        Long tokenId;
        if(subscriberModel != null && _token != null){
            subscriberId = subscriberModel.getId();
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
        SubscriberModel subscriberModel = subscriberService.findSubscriberByUsername(username);
        Long subscriberId;
        TokenModel _token = tokenService.getTokenByTokenString(tokenString);
        Long tokenId;
        if(subscriberModel != null && _token != null){
            subscriberId = subscriberModel.getId();
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
        ArrayList<SubscriberModel> subscriberModels = getSubscriptReceivers(receivers);
        String messageTitle = payload.get("tittle");
        String messageBody = payload.get("body");
        String eventSource = payload.get("eventSource");
        FCMSender sm = new FCMSender();
        sm.setTitle(messageTitle);
        sm.setBody(messageBody);
        sm.setUrl(eventSource);

        //boolean allSuccess = SendTestBySender(sender,  sm);
        boolean allSuccess = SendMessage(subscriberModels,sm);
        if(allSuccess)
            return "Success";
        else
            return "Send notification fail.";
    }

    private ArrayList<SubscriberModel> getSubscriptReceivers(String receivers){
        ArrayList<SubscriberModel> subscriberModels = new ArrayList<SubscriberModel>();
        try{
            JSONArray jsonArray = new JSONArray(receivers);
            ArrayList<String> receiversName  = new ArrayList<String>();
            for (int index =0; index < jsonArray.length(); index++){
                receiversName.add(jsonArray.getString(index));
            }
            if(receiversName.isEmpty())
                return null;
            for(String receiverName:receiversName){
                SubscriberModel s = subscriberService.findSubscriberByUsername(receiverName);
                if(s != null)
                    subscriberModels.add(s);
            }
        }catch(JSONException e){
            System.out.println(e);
            return null;
        }
        return subscriberModels;
    }

    private boolean Send (List<Long> tokenIds, FCMSender sm){
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

    private boolean SendMessage(List<SubscriberModel> subscriberModels, FCMSender sm){
        List<Long> tokenIds = new ArrayList<Long>();
        if(subscriberModels == null || subscriberModels.isEmpty())
            return false;
        for(SubscriberModel subscriberModel : subscriberModels){
            Long subscriberId = subscriberModel.getId();
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
