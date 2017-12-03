package mpower.org.elearning_module.utils;



import android.provider.ContactsContract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sabbir on 11/30/17.
 *
 * @author sabbir (sabbir@mpowe-social.com)
 */

public final class UsageTime {

    private long startTime;
    private long lastRecordedTime;
    private static UsageTime instance;
    private String startDateTime;
    private String endDateTime;
    private DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private Date mDate;

    private UsageTime(){
        instance=this;
        startTime=0;
        lastRecordedTime=0;
        startDateTime="";
        endDateTime="";
        dateFormatter.setLenient(false);
    }

    public String getStartDateTime(){
        mDate= new Date();
        return dateFormatter.format(mDate);
    }

    public String getEndDateTime(){
        mDate=new Date();
        return dateFormatter.format(mDate);
    }

    public void reset(){
        startTime=0;
        lastRecordedTime=0;
    }

    public static synchronized UsageTime getInstance(){
        if (instance==null){
            instance=new UsageTime();
        }
        return instance;
    }

    public void start(){
        reset();
        startTime=System.currentTimeMillis();
    }

    public long getStartTime(){
        return startTime;
    }

    public long getEndTime(){
        return System.currentTimeMillis();
    }

    public long getUsageTime(){
        lastRecordedTime=startTime-System.currentTimeMillis();
        return lastRecordedTime;
    }

    public long getLastRecordedTime(){
        return lastRecordedTime;
    }
}
