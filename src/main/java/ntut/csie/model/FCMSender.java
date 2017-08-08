package ntut.csie.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class FCMSender {

    public FCMSender(){
    }

    private String title;
    private String body;
    private String jumpUrl;
    private String token;
    private String response;

    public void setToken(String token){this.token = token;}
    public String getToken(){return token;}
    public void setTitle(String title){this.title = title;}
    public String getTitle(){return title;}
    public void setBody(String body){this.body = body;}
    public String getBody(){return body;}
    public void setUrl(String jumpUrl){this.jumpUrl = jumpUrl;}
    public String getUrl(){return jumpUrl;}
    public String getResponse(){return response;}

    public String send(){
        boolean isSuccess = false;
        Properties properties = new Properties();
        try{
            properties.load(new FileInputStream("./FCMService.ini"));
        }catch (IOException e){
            System.out.println(e);
        }
        String serverKey = "key=" + properties.getProperty("serverKey");
        HttpURLConnection connection = null;
        try{
            JSONObject parent = buildMessage();

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true );
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestProperty("Authorization",serverKey);
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
                response = sb.toString();
                JSONObject result = new JSONObject(sb.toString());
                if(result.getInt("success") == 1){
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
