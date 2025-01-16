package com.example.dronecontrol.CustomExceptions;

public class StreamInUseException extends Exception
{
    public StreamInUseException(){}
    public StreamInUseException(String message) {super(message);}
}
