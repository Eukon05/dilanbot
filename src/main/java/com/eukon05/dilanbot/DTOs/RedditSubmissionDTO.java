package com.eukon05.dilanbot.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RedditSubmissionDTO {

    private boolean over_18;
    private boolean is_video;
    private String permalink;
    private String url;
    private String selfText;
    private String author;
    private String title;
    private String subreddit_name_prefixed;

}
