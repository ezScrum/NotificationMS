package ntut.csie.controller;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class AccMsController {
    private String accToken;
    private JSONArray accounts_id;

    public void setAccountId(JSONArray accounts_id){
        try {
            this.accounts_id= accounts_id;
        }catch (Exception e)
        {}
    }

    public void setAccToken(String accToken){
        this.accToken = accToken;
    }

    public ArrayList<String> getReceiversName(){
        ArrayList<String> receiversName = new ArrayList<String>();
        HttpURLConnection connection = null;
        try{
            JSONObject json = new JSONObject();
            json.put("accounts_id", accounts_id);
            URL url = new URL("http://localhost:8088/accounts/getAccountList");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true );
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestProperty("Authorization",accToken);
            OutputStream wr = connection.getOutputStream();
            wr.write(json.toString().getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            int HttpResult = connection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                JSONObject result = new JSONObject(sb.toString());
                for (int index = 0; index< accounts_id.length(); index++){
                    String name = result.getString(Long.valueOf(accounts_id.getLong(index)).toString());
                    receiversName.add(name);
                }
                br.close();
                return receiversName;
            }
        }catch(Exception e){
            System.out.println(e);
            return null;
        }
        return null;
    }
}
