package com.eukon05.dilanbot.reddit;

record RedditSubmission(boolean over_18, boolean is_video, String permalink, String url, String selftext,
                        String author, String title, String subreddit_name_prefixed) {
}
