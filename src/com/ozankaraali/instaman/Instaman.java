package com.ozankaraali.instaman;

import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowersRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowingRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUnfollowRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetUserFollowersResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUser;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;

import java.io.IOException;
import java.lang.reflect.Array;
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
        try {
            instagram.login();
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

    public String getProfilePic(String username){try{
        userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(username));
        userasd = userResult.getUser();}catch (Exception e){}
        return userasd.hd_profile_pic_url_info.url;
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

}
