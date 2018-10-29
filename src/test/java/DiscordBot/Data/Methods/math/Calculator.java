package DiscordBot.Data.Methods.math;

import ideaeclipse.DiscordAPI.terminal.CustomTerminal;

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
