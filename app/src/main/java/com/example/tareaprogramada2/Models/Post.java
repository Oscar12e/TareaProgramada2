package com.example.tareaprogramada2.Models;

import com.google.firebase.database.Exclude;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Post {
    public String _key;
    public String postedBy;
    public String postedOn;

    public List<String> likedBy = new ArrayList<>();
    public List<String> dislikedBy = new ArrayList<>();

    public Content content;

    @Exclude
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Post(){

    }

    public Post(String postedBy, String postedOn, List<String> likedBy, List<String> dislikedBy, Content content) {
        this.postedBy = postedBy;
        this.postedOn = postedOn;
        this.likedBy = likedBy;
        this.dislikedBy = dislikedBy;
        this.content = content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("_key", _key);
        result.put("postedBy", postedBy);
        result.put("postedOn", postedOn);
        result.put("likedBy", likedBy);
        result.put("dislikedBy", dislikedBy);
        result.put("content", content.toMap());
        try {
            result.put("postedTime", 1-dateFormat.parse(postedOn).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Exclude
    public String dateDifference(){
        Date postedDate = null;
        Date now = new Date();
        try {
            postedDate = dateFormat.parse(postedOn);
            System.out.println(postedOn);
            long diff = now.getTime() - postedDate.getTime();
            return parseTime(diff);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Exclude
    private String parseTime(long time){
        int daysDiff = (int) (time / (24 * 60 * 60 * 1000) );
        if (daysDiff > 0) return daysDiff + " dias.";

        int hoursDiff = (int) (time / (60 * 60 * 1000) );
        if (hoursDiff > 0) return hoursDiff + " horas.";

        int minutesDiff = (int) (time / (60 * 1000) );
        if (minutesDiff > 0) return minutesDiff + " min.";

        int secsDiff = (int) (time / ( 1000) );
        return secsDiff + " seconds.";
    }

    @Exclude
    public void registerLike(String user_key){
        if (!removeVote(user_key, likedBy)){
            changeVote(user_key, dislikedBy);
            likedBy.add(user_key);
        }

    }
    @Exclude
    public void registerDislike(String user_key){
        if (!removeVote(user_key, dislikedBy)){
            changeVote(user_key, likedBy);
            dislikedBy.add(user_key);
        }
    }

    @Exclude
    //Return if removed
    private boolean removeVote(String user_key, List<String> votesList){
        if (!votesList.contains(user_key)) return false;

        votesList.remove(user_key);
        return true;
    }

    @Exclude
    //Return if removed
    private boolean changeVote(String user_key, List<String> votesList){
        if (!votesList.contains(user_key)) return false;

        votesList.remove(user_key);
        return true;
    }

}
