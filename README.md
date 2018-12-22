# DiscordAPI4J
* This is for the older version of the utility a new one will be posted after development of the refreshed utility is finished
* Custom discordapi interpreter

* Example of Creating the bot
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
* Message Listener example

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

* If you want to import this into your maven project use jitpack
* This project doesn't currently have a stable releases and is not posted on the maven repo
* If you would like to have direct access to the private maven repo this project is stored on please contact me or open a ticket with your intentions

* Example of command cm proof Calculator
* This file is in the commandsDirectory and its parent package is called 'proof'
```java
import DiscordAPI.Terminal.CustomTerminal;

public class Calculator implements CustomTerminal {
    public Calculator(){

    }
    public float add(float a, float b){
        return a + b;
    }
    public float subtract(float a,float b){
        return a-b;
    }
    public float multiply(float a, float b){
        return a*b;
    }
    public float divide(float a, float b){
        return a/b;
    }

    @Override
    public void done() {

    }
}
```
## Configuration
 * adminGroup: the group you want to give admin privilleges to
 * debug: if you want to log debug messages
 * adminUser: your user name
 * useTerminal: if you want to use the custom terminal
 * genericDirectory: where your code is stored for the generic terminal commands
 * adminFileDir: admin commands
 * commandsDirectory: where your code is stored for the custom terminal commands
 * defaultFileDirectory: default commands(anyone can use them)
## Custom Terminal Generic command reference
* To enter a function
    * cm $SubDirecotry $FunctionName
    * i.e. cm proof Calculator
* To execute a generic/admin command
    * cm $commandName
    * cm getcurrentfunction
* To see all possible functions
    * cm help
* To get commands from a function
    * cm help $SubDirecotry $FunctionName
    * i.e. cm help proof Calculator
* Import info
    * __You No longer need to use the prefix cm when inside a function__
    * You can execute the following 2 commands regardless of function
        * help, which displays the help menu
        * done, exits the function
    * help example
        * cm help proof Calculator is equivalent to entering the calculator function and typing the help command
## Custom Terminal
* The custom terminal functionality allows you to write "functions" and "commands" for your created functions
* A function is a collection of commands that are of the same type or relevance. For example my calculator example above is a function
* Functions are stored in subdriectorys of your specified commandsDirectory for example if your commands directory is DiscordBot.Data.Methods then you must create a folder where you want to place one or more function class files
* In my example the calculator class is stored in the proof directory
* Commands are the methods inside the function class. For example if you were inside the example Calculator function you could execute the add command
    * From the help menu we know that the add command requires two float parameters
    * example:
    ```text
    add 10 10
    
    Result: 20.0 (.0 due to the return type of float)
    ```
    * For example you inputted any other number of params that isn't 2 the return would look like this
    ```text
    add 10  
  
    Wrong Number of Args, you need to enter the following 
    [float, float]
    ```
* Once you have entered a function you can only execute commands that are specified inside the function file or generic/admin commands
* Generic/admin Commands
    * The direcotry needs to specified in the config.properties, genericDirectory
    * The admin commands are stored in a file titled "adminCommands"
    * The default "generic" commands are stored in a file titled "defaultCommands"
    * These commands can be called using cm $commandName when not connected to a function and $commandName when inside a function
* Extra function information
    * When you connect to a function, for example my Dog example, "cm proof Dog"
    * If you use the command setName, to dug
    * Then if you use the command getName
    ```text
    setName dug

    Result: void

    getName

    Result: dug
    ```
    * All data that you set is saved from command to command until you exit the function
* How to develop functions
    * Your function class must be public, and implement CustomTerminal
        * Private classes won't be added to the command directory
    * Your commands can have any primitive input type (int,float,double,character) and string
    * Your commands can also return any primitive type + strings

    
## Api Logic(If you plan on forking this project)
 * Once the user runs the code with a valid token and guild id all the users/roles/channels get loaded in for the server
 * Then a websocket connection is started where an identity token is sent over the websocket, after this point the bot while show up in the server
 * Next all events will go through the 'Dispatch' case in the switch statement in the textWss file.
 * From here everything is event driven and in order to write code for each event use the IDiscordBot method (getDispatcher)
## Async
 * See the [async project](https://github.com/ideaeclipse/AsyncUtility) 
## Json utilities
 * See the [Json utilities project](https://github.com/ideaeclipse/JsonUtilities)
## Custom Properties
* See the [Custom properties project](https://github.com/ideaeclipse/CustomProperties)
 