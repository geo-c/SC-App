package de.ifgi.sc.smartcitiesapp.server;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.ifgi.sc.smartcitiesapp.messaging.Message;
import de.ifgi.sc.smartcitiesapp.zone.Zone;



public class JSONParser {

    private ArrayList<Message> msglist = new ArrayList<Message>();
    private JSONObject jsonObject = new JSONObject();
    private JSONArray jsonArray = new JSONArray();
    private JSONObject jsonMsg = new JSONObject();
    private JSONObject jsonLoc = new JSONObject();
    private JSONArray jsonCoords = new JSONArray();
    private String messageID = new String();
    private String zoneID = new String();
    private String crDt = new String();
    private String exDt = new String();
    private Date creationDate = new Date();
    private Date expiredDate = new Date();
    private String topic = new String();
    private String title = new String();
    private Double latitude;
    private Double longitude;
    private String msg = new String();
    private Message message;
    private String string;
    private String[] splittetCoords;

    private ArrayList<Zone> zonelist = new ArrayList<>();
    private String name;
    private String[] zoneTopics;


    /**
     * Constructor
     */
    public JSONParser(){

    }

    /**
     * parses a ArrayList of Messages to an JSONObject, containing a JSONArray with messages
     * @param //ArrayList<Message>
     * @return JSONObject
     */
    public JSONObject parseMessagetoJSON(ArrayList<Message> msglist){

        this.msglist = msglist;

        //clear the jsonObject and the jsonArrays
        this.jsonObject.remove("Messages");
        for(int j = 0; j< jsonArray.length(); j++) {
            this.jsonArray.remove(j);
        }
        this.jsonMsg.remove("Client-id");
        this.jsonMsg.remove("Message-id");
        this.jsonMsg.remove("Zone-id");
        this.jsonMsg.remove("Expired-at");
        this.jsonMsg.remove("Topic");
        this.jsonMsg.remove("Title");
        this.jsonMsg.remove("Message");
        this.jsonLoc.remove("Coordinate");

        // get Messages from msglist and write them into the jsonArray
        //for(int i = 0; i < msglist.size(); i++){
        for (Message m : msglist){

            message = m;
            messageID = message.getMessage_ID();
            zoneID = message.getZone_ID();
             crDt = message.getCreated_At();
            topic = message.getTopic();
            title = message.getTitle();
            exDt = message.getExpired_At();
            msg = message.getMsg();
            longitude = message.getLongitude();
            Log.i("Parser","Longitude "+longitude);
            latitude = message.getLatitude();
            Log.i("Parser","MessageInLoop: "+ message);

            try {
                JSONObject obj = new JSONObject();
                obj.put("Message-id", messageID);
                obj.put("Zone-id", zoneID);
                obj.put("Created-at",crDt);
                obj.put("Expired-at", exDt);
                obj.put("Topic", topic);
                obj.put("Title", title);
                obj.put("Message", msg);


                JSONObject Loc = new JSONObject();
                if(latitude != null & longitude != null){
                    JSONArray Coords = new JSONArray();
                    Coords.put(latitude+","+longitude);
                    Loc.put("Coordinate",Coords);
                    latitude = null;
                    longitude = null;
                }

                obj.put("Location",Loc);
                Log.i("Parser","JSONMsg: "+obj);
                this.jsonArray.put(obj);
                Log.i("Parser","JSONghjzfzzjcfzj "+jsonArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        try {
            jsonObject.put("Messages", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("MsgToJSON","putting jsonArray into jsonObject didn't work");
        }
        return jsonObject;
    }

    /**
     * parses a jsonObject, containing a JsonArray, to an ArrayList of messages
     * @param jsonObject
     * @return ArrayList<Message>
     */
    public ArrayList<Message> parseJSONtoMessage(JSONObject jsonObject) {

        this.jsonObject = jsonObject;
        this.msglist.clear();

        try {
            jsonArray = jsonObject.getJSONArray("Messages");
            for(int i = 0; i < jsonArray.length(); i++){
                jsonMsg = jsonArray.getJSONObject(i);
                messageID = (String) jsonMsg.get("Message-id");
                zoneID = (String) jsonMsg.get("Zone-id");
                crDt = (String) jsonMsg.get("Created-at");
                creationDate = parseStringToDate(crDt);
                exDt = (String) jsonMsg.get("Expired-at");
                expiredDate = parseStringToDate(exDt);
                topic = (String) jsonMsg.get("Topic");
                title = (String) jsonMsg.get("Title");
                msg = (String) jsonMsg.get("Message");
                jsonLoc = jsonMsg.getJSONObject("Location");
                if(jsonLoc.isNull("Coordinate")){
                    latitude = null;
                    longitude = null;
                }
                else{
                    jsonCoords = jsonLoc.getJSONArray("Coordinate");
                    string = (String) jsonCoords.get(0);
                    splittetCoords = string.split(",");
                    latitude = Double.parseDouble(splittetCoords[0]);
                    longitude = Double.parseDouble(splittetCoords[1]);
                }
                message = new Message(messageID,zoneID,creationDate,latitude,longitude,expiredDate,topic,title,msg);
                this.msglist.add(message);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return this.msglist;
    }

    public Date parseStringToDate(String string){

        SimpleDateFormat D_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        try {
            return D_format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Converts a JSON file into a ArrayList of Zones
     * @param jsonObject
     * @return ArrayList of Zone
     */
    public ArrayList<Zone> parseJSONtoZone(JSONObject jsonObject){

        this.jsonObject = jsonObject;
        this.zonelist.clear();

        try {
            jsonArray = this.jsonObject.getJSONArray("Zones");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonZone = jsonArray.getJSONObject(i);
                String name = (String) jsonZone.get("Name");
                String zoneID = (String) jsonZone.get("Zone-id");
                String exDt = (String) jsonZone.get("Expired-at");
                JSONObject geom = jsonZone.getJSONObject("Geometry");
                JSONArray jsonCoords = geom.getJSONArray("Coordinates");
                ArrayList<LatLng> polygon = new ArrayList<LatLng>();
                for(int k = 0; k < jsonCoords.length(); k++){
                    String string = (String)jsonCoords.get(k);
                    String[] splittetCoords = string.split(",");
                    Double latitude = Double.parseDouble(splittetCoords[0]);
                    Double longitude = Double.parseDouble(splittetCoords[1]);
                    polygon.add(new LatLng(latitude,longitude));
                }
                JSONArray jsonTopics = jsonZone.getJSONArray("Topics");
                zoneTopics = new String[jsonTopics.length()];
                for (int m = 0; m< jsonTopics.length(); m++) {
                    zoneTopics[m] = (String) jsonTopics.get(m);
                }
                Zone zone = new Zone(name, zoneID, exDt,zoneTopics, polygon);
                zonelist.add(zone);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return zonelist;
    }
}