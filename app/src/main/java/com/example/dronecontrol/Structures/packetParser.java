package com.example.dronecontrol.Structures;

import java.nio.ByteBuffer;

public class packetParser {
    public static double getElevation(byte[]message)
    {
        return extractDouble(message,16);
    }
    public static double getLongtatiude(byte[]message)
    {
        return extractDouble(message,8);
    }
    public static double getLatatiude(byte[] message)
    {
        return extractDouble(message,0);
    }
    private static double extractDouble(byte[] message, int index)
    {
        byte[] temp = new byte[8];
        int end_index = index + 8;
        for(int i = index; i < end_index;i++)
        {
            temp[i-index] = message[i];
        }
        return ByteBuffer.wrap(temp).getDouble();
    }
}
