package com.bravelocation.yeltzlandnew.tweet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.net.URL;
import java.util.List;

public class Media implements TweetEntity {
    @SerializedName("id_str")
    @Expose
    public String id;

    @SerializedName("display_url")
    @Expose
    public String displayUrl;

    @SerializedName("expanded_url")
    @Expose
    public String expandedUrl;

    @SerializedName("media_url_https")
    @Expose
    public String mediaUrl;

    @SerializedName("sizes")
    @Expose
    public MediaSizes sizes = null;

    @SerializedName("indices")
    @Expose
    public List<Integer> indices = null;

    public List<Integer> getIndices() { return this.indices; }

    public String displayText() {
        return "";
    }

    public String linkUrl() {
        return expandedUrl;
    }

    public String smallMediaUrl() {
        if (this.mediaUrl != null) {
            try {
                URL fullUrl = new URL(this.mediaUrl);
                String host = fullUrl.getHost();
                String fullPath = fullUrl.getPath();

                int slashIndex = fullPath.lastIndexOf('/');
                if (slashIndex > 0 && slashIndex < fullPath.length() - 2) {
                    String pathUpToFileName = fullPath.substring(0, slashIndex + 1);
                    String fileName = fullPath.substring(slashIndex + 1);

                    int dotPosition = fileName.lastIndexOf('.');
                    if (dotPosition > 0 && dotPosition < fileName.length() - 1) {
                        String fileNameWithoutExtension = fileName.substring(0, dotPosition);
                        String extension = fileName.substring(dotPosition + 1);

                        if (extension.equals("jpg") || extension.equals("png"))  {
                            return "https://" + host + pathUpToFileName + fileNameWithoutExtension + "?name=small&format=" + extension;
                        }
                    }


                }


            } catch (Exception e) {

            }
        }

        return null;
    }
}