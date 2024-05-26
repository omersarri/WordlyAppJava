package com.omersari.wordlyjavafinal.model;

import java.io.Serializable;

public class ApiResponseStory implements Serializable {

    private String storyTitle;

    private String story;
    public ApiResponseStory() {

    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }


}
