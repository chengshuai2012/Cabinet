package com.link.cloud;


import com.zitech.framework.SP;

/**
 * Created by ymh on 2016/7/1 0001.
 */
public class User {

    private static final String TOKEN = "token";
    private static final String PASSWORD="passWord";

    private SP sp;

    private String token;
    private int position;
    private String passWord;


    public static User get() {
        return CabinetApplication.getInstance().getUser();
    }

    public User() {
        super();
        sp = new SP("USER_DATA");
        token = sp.getString(TOKEN, "");
        passWord=sp.getString(PASSWORD,"");
    }


    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
        sp.putString(PASSWORD,passWord);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        sp.putString(TOKEN, token);
    }


    public void clear() {

        sp.remove(TOKEN);
        token = "";


    }
}
