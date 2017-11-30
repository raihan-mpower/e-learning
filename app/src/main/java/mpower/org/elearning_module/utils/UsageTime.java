package mpower.org.elearning_module.utils;

/**
 * Created by sabbir on 11/30/17.
 *
 * @author sabbir (sabbir@mpowe-social.com)
 */

public final class UsageTime {

    private long startTime;
    private long lastRecordedTime;
    private static UsageTime instance;

    private UsageTime(){
        instance=this;
        startTime=0;
        lastRecordedTime=0;
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

    public long getUsageTime(){
        lastRecordedTime=startTime-System.currentTimeMillis();
        return lastRecordedTime;
    }

    public long getLastRecordedTime(){
        return lastRecordedTime;
    }
}
