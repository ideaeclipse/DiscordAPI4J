package DiscordBot.Data.Methods.proof;

import DiscordAPI.Terminal.CustomTerminal;
import DiscordAPI.listener.terminalListener.listenerTypes.CustomAnnotation;

public class Dog implements CustomTerminal {
    private String name;
    private int age;
    public Dog() {

    }
    public Dog(String name, int age){
        this.name = name;
        this.age = age;
    }
    @CustomAnnotation(description = "Get Name of your Dog")
    public String getName() {
        return name;
    }
    @CustomAnnotation(description = "Set the Name of your dog")
    public void setName(String name) {
        this.name = name;
    }
    @CustomAnnotation(description = "Get the age of your dog")
    public int getAge() {
        return age;
    }
    @CustomAnnotation(description = "Set the age of your dog")
    public void setAge(int age) {
        this.age = age;
    }
    @CustomAnnotation(description = "Get the info you set about your dog")
    public String printDog() {
        return this.name + " is " + this.age + " year(s) old.";
    }
    @CustomAnnotation(description = "Done")
    public void done(){
        System.out.println("***Exiting dog***");
    }
}
