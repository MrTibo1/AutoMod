package be.mrtibo.automod;

import com.google.gson.*;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

public class Moderate {

    String message;

    public Moderate(String message){
        this.message = message;
    }

    public ModerateResult getResult() {

        JsonObject obj = new JsonObject();
        obj.addProperty("input", message);
        String inputJson = String.valueOf(obj);

        try {
            URL apiUrl = new URL("https://api.openai.com/v1/moderations");

            HttpsURLConnection con = (HttpsURLConnection) apiUrl.openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + Automod.getInstance().getConfig().getString("authkey"));
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.connect();
            OutputStream outputStream = con.getOutputStream();
            outputStream.write(inputJson.getBytes());
            outputStream.flush();
            outputStream.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            con.disconnect();
            JsonElement json = JsonParser.parseString(response.toString());
            JsonObject result = json.getAsJsonObject().get("results").getAsJsonArray().get(0).getAsJsonObject();
            JsonObject categories = result.get("categories").getAsJsonObject();
            boolean flagged = result.get("flagged").getAsBoolean();
            if(flagged){
                String flagReason = "";
                for(String key : categories.keySet()){
                    if(categories.get(key).getAsBoolean()){
                        flagReason = key;
                        break;
                    }
                }
                return new ModerateResult(Result.FLAGGED, flagReason);
            } else {
                return new ModerateResult(Result.OK, "Not Flagged");
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
