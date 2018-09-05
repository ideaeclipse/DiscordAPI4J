package DiscordBot.Data.Methods.proof;

import DiscordAPI.Terminal.Interfaces.CustomTerminal;
import DiscordAPI.listener.terminalListener.listenerTypes.CustomAnnotation;

public class Calculator implements CustomTerminal {
    public Calculator(){

    }
    @CustomAnnotation(description="Addition of 2 floating point numbers")
    public float add(float a, float b){
        return a + b;
    }
    @CustomAnnotation(description="Subtraction of 2 floating point numbers")
    public float subtract(float a,float b){
        return a-b;
    }
    @CustomAnnotation(description="Multiplication of 2 floating point numbers")
    public float multiply(float a, float b){
        return a*b;
    }
    @CustomAnnotation(description="Division of 2 floating point numbers")
    public float divide(float a, float b){
        return a/b;
    }

    @Override
    public void done() {

    }
}
