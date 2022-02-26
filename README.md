<img src="dilan-banner.png" style="margin-left: auto; margin-right: auto" alt="banner">

<h3>Backstory</h3>
After Google shut Rythm and Groovy down, me and my friends were looking for a way to once again hear the famous song "Never Gonna Give You Up" on our Discord server. <br>
We have tried out many other projects, but weren't satisfied with the results, as many of the bots started using SoundCloud as their source of audio, in fear of being shut down by Google, or their functionality was very different from what we had grown accustomed to. <br> <br>
That's how Dilan was born; <b>A bot that is easy to use and uses Youtube as it's audio source!</b> <br> How neat is that! <br>
We build it on top of Spring Framework and Javacord, popular and stable libraries, widely used in the Java community.
The audio is being sourced using an updated fork of Lavaplayer, so Youtube compatibility is almost guaranteed at all times.

<h3>Features and usage</h3>

The bot's default prefix is "dilan", every command uses this pattern: <br>
<b>[prefix] [command] [arguments]</b>

<ul>
<li>
    Play music from Youtube by using the "play" command and either typing in the title of the song, or passing a URL. <br>
    Control the playback by using the "skip", "pause", "loop", "clear" or "stop" commands. <br>
    The "play" command without any arguments is used to resume the track after it has been paused.
</li>
<br>
<li>
    The bot automatically leaves the VC after everyone else has left it; <br>
    You can also force it to leave by using the "disconnect" command
</li>
<br>
<li>
    Fetching random posts from subreddits! You can use it for posting memes, copypastas or anything you like! <br>
    Use the command "reddit" followed by the name of the subreddit
</li>
<br>
<li>
    Have a tough decision to make? Use the "8ball" command followed by your question and let the fortune decide for you!
</li>
<br>
<li>
    You want to have a karaoke party on your server? Use the "lyrics" command to get lyrics for the song that is currently playing, or type "lyrics" followed by the name of the song to get precisely what you want
</li>
<br>
<li>
    Don't like the default prefix of the bot? Change it with the "prefix" command, followed by your desired prefix
</li>

</ul>

<h3>
<a href="https://discord.com/api/oauth2/authorize?client_id=913511878523752519&permissions=8&scope=bot">Add the bot to your server by clicking this link!</a>
</h3>

<h3>FAQ</h3>
<ul>

<li>
Why does the bot require admin privileges?! This is a scam!<br><br>
No, this is not a scam. You need to grant Dilan admin access, because I was too lazy to implement permission checks.<br>
If you are still unsure about the bot's safety, feel free to look through the code, I assure you that it's 100% safe<br>

</li>
<br>

<li>
Does the bot store my information?<br><br>
No, we don't store any user-related data.<br>
The server's ID, however, goes into our database, to enable our users to change the prefix of the bot. That's all.<br>
</li>
<br>

<li>
I kicked the bot from my server and added it back, but it won't react to any commands!<br><br>
There's a high chance that you've changed the bot's prefix! You can change it back, using the "dilan prefix" command.<br>
If the issue still persists, feel free to open an issue on this Github project
</li>


</ul>

<h3>Contributing</h3>
If you have an idea for a new feature or a bugfix, feel free to open an issue or a pull request!

<h3>Technical stuff</h3>
The bot was made using the following libraries:
<ul>

<li>
<a href="https://spring.io">Spring Framework</a>
</li>

<li>
<a href="https://javacord.org/">Javacord</a>
</li>

<li>
<a href="https://github.com/Walkyst/lavaplayer-fork">Lavaplayer Fork</a> by Walkyst (and original Lavaplayer by Sedmelluq)
</li>

<li>
<a href="https://github.com/LowLevelSubmarine/GeniusLyricsAPI">GeniusLyricsAPI</a> by LowLevelSubmarine
</li>

<li>
<a href="https://github.com/google/gson">Gson</a>
</li>

<li>
<a href="http://kong.github.io/unirest-java/">Unirest For Java</a> by Kong
</li>

<li>
<a href="https://github.com/mattbdean/JRAW">JRAW</a> by mattdean
</li>

<li>
<a href="https://flywaydb.org/">Flyway</a>
</li>

<li>
<a href="https://www.postgresql.org/">PostgreSQL</a>
</li>

</ul>

Big shoutout to everyone who has contributed to the projects listed above, without you, Dilan would not be possible!<br><br>

You can host the bot on your own if you'd like to, it's heroku-ready so all you have to do is clone this repository, modify the application.properties file with your access tokens and login details and then create a new Heroku app with the cloned repo.<br>
A detailed instruction on how to do this will be here in the future
