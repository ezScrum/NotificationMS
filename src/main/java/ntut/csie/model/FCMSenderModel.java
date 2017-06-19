package ntut.csie.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FCMSenderModel {
    public FCMSenderModel(String token, String title, String body, String jumpUrl){
        this.token = token;
        this.title = title;
        this.body = body;
        this.jumpUrl = jumpUrl;
    }

    public FCMSenderModel(){
    }

    private String title;
    private String body;
    private String jumpUrl;
    private String token;

    public void setToken(String token){this.token = token;}
    public void setTitle(String title){this.title = title;}
    public void setBody(String body){this.body = body;}
    public void setUrl(String jumpUrl){this.jumpUrl = jumpUrl;}

    public String send(){
        boolean isSuccess = false;
        HttpURLConnection connection = null;
        try{
            JSONObject parent = buildMessage();

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true );
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestProperty("Authorization","key=AAAALeDdRM4:APA91bHDg36wwcKhafIlP4A6eRpzFtqTL0MvjOQPYicW1wnoVClieKmGZeRNKABkqG3e4We0cGXnDBJ_zD2lWMI6BhFLL_NRlXmpYm8oZux5IoKiGauN7K9YJhc__xI4cW-BXl6AKlwd");
            OutputStream wr = connection.getOutputStream();
            wr.write(parent.toString().getBytes("UTF-8"));
            wr.close();

            StringBuilder sb = new StringBuilder();
            int HttpResult = connection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                JSONObject result = new JSONObject(sb.toString());
                if(result.get("success") == "1"){
                    isSuccess = true;
                }
            } else {
                System.out.println(connection.getResponseMessage());
                isSuccess = false;
            }

        }catch (Exception e){
            System.out.println(e);
            isSuccess = false;
        }
        if(isSuccess){
            return "Success";
        }
        else{
            return "Fail";
        }
    }

    private JSONObject buildMessage() throws JSONException{
        JSONObject parent = new JSONObject();
        parent.put("to",token);
        JSONObject message = new JSONObject();
        message.put("title",title);
        message.put("body",body);
        message.put("click_action",jumpUrl);
        parent.put("notification",message);

        return parent;
    }
}
