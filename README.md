# DiscordAPI4J
* Custom command Discord bot library

## Maven
* Add Repository
```xml
<repository>
    <id>ideaeclipse Repository Server</id>
    <url>https://repository.thiessem.ca/repository/maven-releases/</url>
</repository>
```
* Add dependency
```xml
<dependency>
    <groupId>com.ideaeclipse</groupId>
    <artifactId>DiscordAPI</artifactId>
    <version>1.9</version>
</dependency>
```

## Need to know
* This bot uses two different utilities you have to interact with.
* Custom Properties, and Custom Terminal

### Custom Properties
* There are 7 properties that need to be set in order for this bot to start
    * LoadChannelMessages
        * Boolean value
        * true, loads all channels messages
        * false, doesn't load channel message
    * CommandPrefix
        * String
        * Used to start each command, i.e. \\! would be work like !help
    * Debug
        * Boolean value
        * Whether to show debug messages in console
    * DmCommands
        * String
        * Null = no dm specific commands
        * Populated string means that is the package directory for where your class files are located for the dm commands. There should be a class titled Commands in this package
    * InstanceCommands
        * String
        * Cannot be null
        * Same as DmCommands
    * EmbedFooterImage
        * String
        * Null = not footer
        * String, url to image for embeded footer
    * CommandChannel
        * String
        * Cannot be Null
        * Says which channel bot channels are permissable in
* An example file is
```text
#Thu Feb 14 16:32:37 EST 2019
LoadChannelMessages=false
CommandPrefix=\!
debug=true
DmCommands=DiscordBotNew.dm
InstanceCommands=DiscordBotNew.generic
embedFooterImage=null
CommandChannel=bot
```

### Custom Terminal
* There are two separate methods of commands. Through channel communication and through direct message
* Make two separate packages for each like the image below
* ![alt text](https://i.imgur.com/sRSIC5V.png)
* Make sure these packages are documented in your properties file
#### Making commands
* All these commands are executable in the channel specified in the properties file
* You can have the IDiscordBot parameter in the constructor. This is optional but no other parameter is allowed.
* Command methods must be annotated with @Executable
* All command methods must have 1 or 2 parameters, 1 being a primitive type and an option parameter of an IMessage object. This object will contain info on the message, like user, channel, etc.
* This is an example of a channel commands class
```java
package DiscordBotNew.generic;

import ideaeclipse.DiscordAPI.bot.IDiscordBot;
import ideaeclipse.DiscordAPI.bot.objects.message.IMessage;
import ideaeclipse.DiscordAPI.bot.objects.role.IRole;
import ideaeclipse.customTerminal.Executable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Commands {
    private final IDiscordBot bot;

    public Commands(final IDiscordBot bot) {
        this.bot = bot;
    }
    @Executable
    public String ping(final String message) {
        if (message.toLowerCase().equals("ping"))
            return "pong";
        return null;
    }
    @Executable
    public String pong(final String message) {
        if (message.toLowerCase().equals("pong"))
            return "ping";
        return null;
    }
}
```
* for example if you say !ping, the bot will message back with pong, or if you message with !pong the bot will respond with ping
* An example of sending an embedded messages is below
```java
package DiscordBotNew.dm;

import ideaeclipse.DiscordAPI.bot.IDiscordBot;
import ideaeclipse.DiscordAPI.bot.objects.channel.Field;
import ideaeclipse.DiscordAPI.bot.objects.message.IMessage;
import ideaeclipse.DiscordAPI.bot.objects.role.IRole;
import ideaeclipse.DiscordAPI.bot.objects.user.IDiscordUser;
import ideaeclipse.customTerminal.Executable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Commands {
    private final IDiscordBot bot;

    public Commands(final IDiscordBot bot) {
        this.bot = bot;
    }

    @Executable
    public String DmMailCheck(final IMessage message, final String string) {
        return "<N>Info</N>" + "<V>Name: " + message.getUser().getUsername() +
                "\nNickName: " + message.getUser().getNickName() + "\nContent: " + string +
                "</V>";
    }
}
```
* When sending an embedded message you must have a \<N>\</N> followed by a \<V>\</V> these do not need to be separate by a \n. 
* \<N>\</N> cases are used as names. or "Headers" You cannot put \n in these.
* \<V>\</V> cases are used as values. You can put \n in these.
## Hot to use
* All you need to do to use this bot is to create the required commands in classes. Setup the properties file as you please and register a bot on the [discord apps webpage](https://discordapp.com/developers/applications/)
* An example to start this is bot is
```java
package DiscordBotNew;

import ideaeclipse.DiscordAPI.bot.DiscordBot;

public class main {
    public static void main(String[] arg) {
        new DiscordBot(arg[0]);
    }
}
```
* Arg[0] being a passed parameter, this is the token for your bot