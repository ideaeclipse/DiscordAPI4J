package DiscordBot.Data.Methods.proof;

import DiscordAPI.Terminal.CustomTerminal;

public class Dog implements CustomTerminal {
    private String name;
    private int age;
    public Dog() {

    }
    public Dog(String name, int age){
        this.name = name;
        this.age = age;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String printDog() {
        return this.name + " is " + this.age + " year(s) old.";
    }
    public void done(){
        System.out.println("***Exiting dog***");
    }
}
