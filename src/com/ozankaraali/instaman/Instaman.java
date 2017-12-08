package com.ozankaraali.instaman;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.params.ConnRoutePNames;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.*;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetUserFollowersResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUser;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Instaman {
    private String username;
    private String password;
    private String sneakUsername;
    private Instagram4j instagram;
    private InstagramSearchUsernameResult userResult;
    private InstagramUser userasd;
    private List<InstagramUserSummary> users;
    private List<InstagramUserSummary> usera;
    private ArrayList followersList;
    private ArrayList followingList;

    private boolean proxyEnabled = false;

    public boolean isProxyEnabled() {
        return proxyEnabled;
    }

    public void setProxyEnabled(boolean proxyEnabled) {
        this.proxyEnabled = proxyEnabled;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public String getNetUser() {
        return netUser;
    }

    public void setNetUser(String netUser) {
        this.netUser = netUser;
    }

    public String getNetPass() {
        return netPass;
    }

    public void setNetPass(String netPass) {
        this.netPass = netPass;
    }

    private String serverIp;
    private int portNumber;
    private String netUser;
    private String netPass;

    public Instaman(){

    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setSneakUsername(String sneakUsername) {
        this.sneakUsername = sneakUsername;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void builder(){
        instagram = Instagram4j.builder().username(username).password(password).build();
        instagram.setup();
        if(sneakUsername.equals("")){
            sneakUsername = username;
        }

        if (proxyEnabled){
        HttpHost proxy = new HttpHost(serverIp, portNumber, "http");
        instagram.getClient().getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        instagram.getClient().getParams().setIntParameter("http.connection.timeout", 600000);

        instagram.getClient().getCredentialsProvider().setCredentials(
                new AuthScope(serverIp, portNumber),
                new UsernamePasswordCredentials(netUser, netPass));
        }

        try {
            instagram.login();
            refreshResult();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshResult(){
        try {
            userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(sneakUsername));
            userasd = userResult.getUser();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList getFollowersList() throws NullPointerException{
        InstagramGetUserFollowersResult fr = followersRequest();
        users = new ArrayList<>();
        String nextMaxId = fr.getNext_max_id() ;
        while (true) {
            users.addAll(fr.getUsers());
            fr = followersRequest(nextMaxId);

            if (nextMaxId == null) {
                break;
            }
            else{
                //users.addAll(fr.getUsers());
                nextMaxId = fr.getNext_max_id();
                //fr = followersRequest(nextMaxId);
            }
        }

        followersList = new ArrayList<>();
        for (InstagramUserSummary user : users) {
            followersList.add(user.getUsername());
        }
        return followersList;
    }

    public ArrayList getFollowingList() throws NullPointerException{
        InstagramGetUserFollowersResult fr = followingRequest();
        usera = new ArrayList<>();
        String nextMaxId = fr.getNext_max_id() ;
        while (true) {
            usera.addAll(fr.getUsers());
            fr = followingRequest(nextMaxId);
            if (nextMaxId == null) {
                break;
            }
            else{
                //usera.addAll(fr.getUsers());
                nextMaxId = fr.getNext_max_id();
                //fr = followingRequest(nextMaxId);
            }
        }

        followingList = new ArrayList<>();
        for (InstagramUserSummary user : usera) {
            followingList.add(user.getUsername());
        }
        return followingList;
    }

    public ArrayList getFansList(){
        if(followersList==null||followingList==null){
            getFollowingList();
            getFollowersList();
        }
        ArrayList fansList = new ArrayList();
        for (InstagramUserSummary user : users) {
            fansList.add(user.getUsername());
        }
        fansList.removeAll(followingList);
        return fansList;
    }

    public ArrayList getUnfList(){
        if(followersList==null||followingList==null){
            getFollowingList();
            getFollowersList();
        }
        ArrayList unfList = new ArrayList();
        for (InstagramUserSummary user : usera) {
            unfList.add(user.getUsername());
        }
        unfList.removeAll(followersList);
        return unfList;
    }

    public int getFollowersNum(){try{
        userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(sneakUsername));
        userasd = userResult.getUser();}catch (Exception e){}
        return userasd.follower_count;
        //return followersList.size();
    }

    public int getFollowingNum(){try {
        userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(sneakUsername));
        userasd = userResult.getUser();}catch (Exception e){}
        return userasd.following_count;
        //return followingList.size();
    }

    public void unfollowRequest(String username){try{
        userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(username));
        userasd = userResult.getUser();
        instagram.sendRequest(new InstagramUnfollowRequest(userasd.getPk()));}catch (Exception e){}
    }

    public void followRequest(String username){try{
        userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(username));
        userasd = userResult.getUser();
        instagram.sendRequest(new InstagramFollowRequest(userasd.getPk()));}catch (Exception e){}
    }

    public String getProfilePic(String username){
        java.lang.System.setProperty("java.net.preferIPv4Stack", "true");
        java.lang.System.setProperty("java.net.preferIPv6Addresses", "false");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://instagram.com/"+username)
                .build();
        Response response = null;
        String out="";
        try {
            response = client.newCall(request).execute();
            String mahmut = response.body().string();
            int b = mahmut.indexOf("og:image");
            int c = mahmut.indexOf("og:title");
            String url = mahmut.subSequence(b+19,c-33).toString();
            int s = url.indexOf("s150x150");
            out = url.subSequence(0,s).toString()+url.subSequence(s+9,url.length()).toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return out;
    }

    public InstagramGetUserFollowersResult followingRequest() throws NullPointerException{
        try {
            return instagram.sendRequest(new InstagramGetUserFollowingRequest(userResult.getUser().getPk()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public InstagramGetUserFollowersResult followingRequest(String nextMaxId) throws NullPointerException{
        try {
            return instagram.sendRequest(new InstagramGetUserFollowingRequest(userResult.getUser().getPk(), nextMaxId));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InstagramGetUserFollowersResult followersRequest() throws NullPointerException{
        try {
            return instagram.sendRequest(new InstagramGetUserFollowersRequest(userResult.getUser().getPk()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public InstagramGetUserFollowersResult followersRequest(String nextMaxId) throws NullPointerException{
        try {
            return instagram.sendRequest(new InstagramGetUserFollowersRequest(userResult.getUser().getPk(), nextMaxId));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isLoggedin(){
        return instagram.isLoggedIn();
    }
}
