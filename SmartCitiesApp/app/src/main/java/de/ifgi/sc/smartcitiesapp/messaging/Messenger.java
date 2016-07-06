package de.ifgi.sc.smartcitiesapp.messaging;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import de.ifgi.sc.smartcitiesapp.zone.Zone;

/**
 * Created by SAAD on 5/11/2016.
 */
public class Messenger implements de.ifgi.sc.smartcitiesapp.interfaces.Messenger {


    private final Context ourContext;


    /** This method will receive messages from Server and PeerConnection separately
     * and write them in database
     * Conditions:
     * User need to be in at least one zone inorder to store messages locally
     * Only messages from one particular zones will be received
     * Messages need to have unique MessageID, otherwise they won't get stored.
     * @param msgs Raw Array list of Messages received from Server or Peers
     */

    @Override
    public void updateMessengerFromConnect(ArrayList<Message> msgs){
        //Checking size of Arraylist
        int size;
        size= msgs.size();
        Message t_msg;

        DatabaseHelper db = new DatabaseHelper(ourContext);
        db.open();
        String userZoneID=currentUserZone();
        for(int i=0;i<size;i++){
            Log.i("This is Msg "+i," Number");
            t_msg= msgs.get(i);
            //will change to this once implemented by ZoneManager
            //db.messageAlreadyExist(t_msg) == false && userZoneID!=null && userZoneID.equals(t_msg.getZone_ID())
            if(db.messageAlreadyExist(t_msg) == false ){
                createMessageEntry(db,t_msg);

            }

        }

        Log.i("Messages "," Fetched");
        db.close();
    }

    //This function will get user zoneID from ZoneManager Class and return it
    private String currentUserZone() {
        return null;
    }

    /**This method will be called by main Interface to write and store message locally
     *
     * @param msgs New message written by user
     */
    @Override
    public void updateMessengerFromUI(ArrayList<Message> msgs) {
        //Checking size of Arraylist
        int size;
        size= msgs.size();
        Message t_msg;

        DatabaseHelper db = new DatabaseHelper(ourContext);
        db.open();

        for(int i=0;i<size;i++){
            Log.i("This is Msg "+i," Number");
            t_msg= msgs.get(i);
            if(db.messageAlreadyExist(t_msg) == false){
                    createMessageEntry(db,t_msg);
            }

        }

        Log.i("Messages "," Fetched");
        db.close();

    }

    /**
     * This method will create a Message entry in database
     * @param db
     * @param t_msg
     */
    private void createMessageEntry(DatabaseHelper db,Message t_msg){
        db.createEntry(t_msg.getMessage_ID(),t_msg.getZone_ID(), t_msg.getCreated_At(),t_msg.getLatitude(),
                t_msg.getLongitude(), t_msg.getExpired_At(),t_msg.getTopic(),
                t_msg.getTitle(),t_msg.getMsg());
    }
    public Messenger(Context C){
            ourContext = C;
    }

    /**
     * This methods will read all Messages from local database and return them in the form of
     * Message array
     * @return return all Messages stored locally in database
     */
    public ArrayList<Message> getAllMessages(){
        DatabaseHelper db = new DatabaseHelper(ourContext);
        db.open();
        ArrayList<Message> msgs= db.getAllMessages();
        db.close();

        return msgs;
    }

    //This methods will store all zones in database having unique Zone_IDs.
    //Zone with prematching zoneID will simply be ignored
    public void updateZonesInDatabase(ArrayList<Zone> zones){
        //Checking size of Arraylist
        int size;
        size= zones.size();
        Zone zn;

        DatabaseHelper db = new DatabaseHelper(ourContext);
        db.open();

        for(int i=0;i<size;i++){
            Log.i("This is Zone  "+i," Number");
            zn= zones.get(i);
            if(db.zoneAlreadyExist(zn) == false){
                db.createZoneEntry(zn.getName(),zn.getZoneID(),zn.getExpiredAt(),zn.getTopics(),zn.getPolygon());

            }

        }
        // db.getAllMessages();
        Log.i("Zones  "," stored");
        db.close();

    }

    //This method will return all zones stored in Database
    public ArrayList<Zone> getAllZonesfromDatabase(){
        DatabaseHelper db = new DatabaseHelper(ourContext);
        db.open();
        ArrayList<Zone> zones= db.getAllZones_DB();
        db.close();

        return zones;
    }


    public void deleteZoneMessage()
    {
        DatabaseHelper db = new DatabaseHelper(ourContext);
        db.open();
        db.deleteMessageZoneWhenExpire();
        db.close();


    }
}
