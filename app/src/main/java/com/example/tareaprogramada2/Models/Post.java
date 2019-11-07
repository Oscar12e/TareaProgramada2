package com.example.tareaprogramada2.Models;

import com.google.firebase.database.Exclude;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Post {
    public String postedBy;
    public String postedOn;

    public List<User> likedBy;
    public List<User> dislikedBy;

    public Content content;

    @Exclude
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

    public Post(){

    }

    public Post(String postedBy, String postedOn, List<User> likedBy, List<User> dislikedBy, Content content) {
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

        result.put("postedBy", postedBy);
        result.put("postedOn", postedOn);
        result.put("likedBy", likedBy);
        result.put("dislikedBy", dislikedBy);
        result.put("content", content.toMap());

        return result;
    }

    @Exclude
    public String dateDifference(){
        //Date postedDate = new Date(postedOn);
        //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date postedDate = null;
        Date now = new Date();
        try {
            postedDate = dateFormat.parse(postedOn);
            long diff = postedDate.getTime() - now.getTime();
            return parseTime(diff);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Exclude
    public String parseTime(long time){
        int daysDiff = (int) (time / (24 * 60 * 60 * 1000) );
        if (daysDiff > 0) return daysDiff + " dias.";

        int hoursDiff = (int) (time / (60 * 60 * 1000) );
        if (hoursDiff > 0) return daysDiff + " horas.";

        int minutesDiff = (int) (time / (60 * 1000) );
        if (minutesDiff > 0) return daysDiff + " min.";

        int secsDiff = (int) (time / ( 1000) );
        return secsDiff + " seconds.";

    }
}
