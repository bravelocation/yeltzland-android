package com.bravelocation.yeltzlandnew.tweet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExtendedEntities {
    @SerializedName("media")
    @Expose
    public List<Media> media = null;
}