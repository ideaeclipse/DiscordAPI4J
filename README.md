# DiscordAPI4J
Custom discordapi interpreter  <br />

Example of Creating the bot
```java

import DiscordAPI.IDiscordBot;
import DiscordAPI.objects.DiscordBotBuilder;

public class Main {
    public static IDiscordBot bot;

    private Main(String token, Long guildId) {
        bot = new DiscordBotBuilder(token, new EventClass(), guildId).login();
    }

    public static void main(String[] args) {
        new Main(args[0], Long.parseUnsignedLong(args[1]));
    }
}
```
Message Listener example

```java
import DiscordAPI.listener.discordApiListener.listenerEvents.Message_Create;
import ideaeclipse.reflectionListener.EventHandler;
import ideaeclipse.reflectionListener.Listener;

public class EventClass implements Listener {
    @EventHandler
    public void event1(Message_Create create) {
        System.out.println(create.getMessage().getContent());
    }
}
```

# Configuration
 * adminGroup: the group you want to give admin privilleges to
 * debug: if you want to log debug messages
 * adminUser: your user name
 * useTerminal: if you want to use the custom terminal
 * genericDirectory: where your code is stored for the generic terminal commands
 * adminFileDir: admin commands
 * commandsDirectory: where your code is stored for the custom terminal commands
 * defaultFileDirectory: default commands(anyone can use them)
 
# Api Logic(If you plan on forking this project)
 * Once the user runs the code with a valid token and guild id all the users/roles/channels get loaded in for the server
 * Then a websocket connection is started where an identity token is sent over the websocket, after this point the bot while show up in the server
 * Next all events will go through the 'Dispatch' case in the switch statement in the textWss file.
 * From here everything is event driven and in order to write code for each event use the IDiscordBot method (addListener)
# Async
 * There is a custom Async library that will return data about completion of each method in a synced order
 * Make an AsyncList<?> and add new Async.IU for each index.
 * Finally use the static method Async.Execute("Your async list here") and this will return a List<?>
# ObjectMapper
 * There is also a custom object mapper/builder if you are doing anything related to sending json data to the webapi or over the socket refer to https://discordapp.com/developers/docs/intro for proper formatting
 * The object mapper is in a class called Parser (see method convertToPayload(final Json object, final Class<?> c)) if you pass the json object and a class with accessable variables (same name as each key value) and it will map each key to the corrosponding variable and will return an instance of that class
 * The builder class works in the inverse way the convertToPayload method works. If you want to build a json string you need to first create a new instance of a class with public variables and set their values then pass that instance to the buildData method and if you're building it into a payload set that instance into buildPayload with an appropriate opCode
