package com.eukon05.dilanbot.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RedditSubmission {

    private boolean over_18;
    private boolean is_video;
    private String permalink;
    private String url;
    private String selftext;
    private String author;
    private String title;
    private String subreddit_name_prefixed;

}
