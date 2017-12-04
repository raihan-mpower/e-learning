package mpower.org.elearning_module.utils;

/**
 * Created by sabbir on 10/12/17.
 */

public enum UserType {

    DOT("DOT"),
    PUBLIC("");

    private String userType;


    UserType(String userType){
        this.userType=userType;
    }

    public String getUserType(){
        return userType;
    }

    public static UserType fromString(String userType){
        for (UserType user:UserType.values()){
            if (user.userType.equalsIgnoreCase(userType)){
                return user;
            }
        }
        return null;
    }

    public static String getStringUserType(UserType userType){
        return userType.userType;
    }

    @Override
    public String toString() {
        return userType;
    }
}
