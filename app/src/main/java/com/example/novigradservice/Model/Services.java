package com.example.novigradservice.Model;

public class Services {
    String name,start_time,end_time,rating;

    public Services(String name, String start_time, String end_time, String rating) {
        this.name = name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getRating() {
        return rating;
    }


}
