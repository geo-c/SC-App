package de.ifgi.sc.smartcitiesapp.messaging;


import android.util.Log;
import java.io.Serializable;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

    // Unique client ID that changes per session
    private String client_ID;

    // Unique message ID
    private String message_ID;

    // Unique zone ID
    private String zone_ID;

    // Date when message was created
    private String cr_Dt;

    // Date when message expires
    private String ex_Dt;

    // Topic of message
    private String topic;

    // Title of the message
    private String title;

    // Content of the message
    private String message;

    // Related coordinates
    private double latitude;
    private double longitude;

    private SimpleDateFormat D_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public Message(String c_id, String m_id, String z_id, Date crDt, double lat,double lon, Date exDt, String top, String tit, String msg){
        client_ID=c_id;
        message_ID=m_id;
        zone_ID=z_id;
        cr_Dt = changeDateFormat(crDt);
        latitude=lat;
        longitude=lon;
        ex_Dt=changeDateFormat(exDt);
        topic=top;
        title=tit;
        message=msg;
        Log.i("Msg "+c_id, " Created ");
    }

    public void setClient_ID(String id){
        client_ID=id;
    }
    public void setMessage_ID(String id){
        message_ID=id;
    }
    public void setZone_ID(String id){
        zone_ID=id;
    }
    public void setCreated_At(Date dt){cr_Dt=changeDateFormat(dt);}
    public void setLatitude(double lat)  { latitude=lat; };
    public void setLongitude(double lon) {longitude=lon; };
    public void setExpired_At(Date dt){
        ex_Dt=changeDateFormat(dt);
    }
    public void setCategory(String top){topic=top;}
    public void setTitle(String tit){   title=tit;}
    public void setMsg(String   m){message=m;}

    public String getClient_ID(){return client_ID;};
    public String getMessage_ID(){return message_ID;};
    public String getZone_ID(){return zone_ID;};
    public String getCreated_At(){return cr_Dt;};
    public double getLatitude(){return latitude;};
    public double getLongitude(){return longitude;};
    public String getExpired_At(){return ex_Dt;};
    public String getTopic(){return topic;};
    public String getTitle(){return title;};
    public String getMsg(){return message;};

    //This method takes Date as input and convert it in specific format
    // D_format= "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    //to store Expired date in this format in database.
    private String changeDateFormat(Date ex_time) {

        Log.i("Date format changed to ",D_format.format(ex_time));

        return D_format.format(ex_time);
//        }
    }

    public String toString() {
        return "client_ID," + this.client_ID +
                ", message_ID," + this.message_ID +
                ", zone_ID," + this.zone_ID +
                ", cr_Dt," + this.cr_Dt +
                ", ex_Dt," + this.ex_Dt +
                ", topic," + this.topic +
                ", title," + this.title +
                ", message," + this.message +
                ", latitude," + this.latitude +
                ", longitude," + this.longitude;


    }
}
