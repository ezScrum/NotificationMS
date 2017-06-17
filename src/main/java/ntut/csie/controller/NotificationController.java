package ntut.csie.controller;

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
import java.util.Map;
import ntut.csie.model.Subscriber;
import ntut.csie.model.TokenModel;
import ntut.csie.model.TokenRelationModel;
import ntut.csie.service.SubscriberService;
import ntut.csie.service.TokenService;
import ntut.csie.service.TokenRelationService;

@RestController
@RequestMapping(path = "notify")
public class NotificationController {
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
        if(subscriber == null){
            return "Fail";
        }
        else{
            subscriberId = subscriber.getId();
        }
        TokenModel _token = tokenService.getTokenByTokenString(tokenString);
        Long tokenId;
        if(_token == null){
            return "Fail";
        }
        else{
            tokenId = _token.getId();
        }

        TokenRelationModel tokenRelationModel = tokenRelationService.getRelation(subscriberId,tokenId);
        tokenRelationService.delete(tokenRelationModel.getId());
        return "Success";
    }

    
}
