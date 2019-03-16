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
    <version>2.0</version>
</dependency>
```
## Using the bot
### Starting the bot
* An example to start the bot is below
```java
public class main {
    public static void main(String[] args) {
        IDiscordBot bot = new DiscordBotBuilder(args[0]).setCommandPrefix("!").setCommandChannel("bot").channelCommands(new ChannelCommands()).start();
    }
}
```
* the only required parameter for the bot is a valid discord token
* setCommandPrefix: is the prefix for commands
* setCommandChannel: is the channel where the bot commands are valid
* channelCommands: is a CommandsClass interface accessible via the channel specified
* dmCommands: is a CommandsClass interface for commands accessible via dms
* registerListeners: any custom event listener you want to implement, pass it there
* setEmbedFooter: when sending an embed message, an image url to end the footer with
* start: the method to call once all settings have been set and you wish to start the bot

### CommandsClasses
* An example of a CommandsClass is below
```java
public class ChannelCommands implements CommandsClass {
    @Executable
    public String ping(final IDiscordBot bot) {
        return "pong";
    }

    @Executable
    public String pong(final IMessage message) {
        return "ping";
    }

    @Executable
    public String test1(final IMessage message, final String content) {
        return content;
    }

    @Executable
    public String test2(final IDiscordBot bot, final String content) {
        return content;
    }

    @Executable
    public String test3(final IDiscordBot bot, final IMessage message, final String content) {
        return content;
    }

    @Executable
    public String test4(final IMessage message, final IDiscordBot bot, final String content) {
        return content;
    }
}
```
* When starting the bot all you have to do is add an instance of this in the method channelCommands or dmCommands
* Every method that you want to be a command needs to be annotated with the annotation @Executable
* Every method can have 2 optional parameters those are an IDiscordBot param and or IMessage param these params just allow you to write more personalized responses to commands
* Return type must be string
* You can have input params from the user. These must be primitive types or String. If you include an object that isn't a primitive type there will be unexpected behaviour

* An example of return embed messages is below
```java
public class DmCommands implements CommandsClass {

    @Executable
    public String DmMailCheck(final IMessage message, final String string) {
        return "<N>Info</N>" + "<V>Name: " + message.getUser().getUsername() +
                "\nNickName: " + message.getUser().getNickName() + "\nContent: " + string +
                "</V>";
    }
}
```
* When the user messages the bot with the command !DmMailCheck ${message} it will create an embed message with the header 'Info' and the value 'Name: $name\nNickname: $nickname\nContent: ${message}'
* This will create a embed message. Good for display purchases