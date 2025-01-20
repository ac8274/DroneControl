package com.example.dronecontrol.Structures;

import android.util.Log;


import com.example.dronecontrol.Drone_Control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class HotSpot extends Thread{
    private ServerSocket serverSocket;
    private String HOTSPOT_IP; // Default IP for hotspot gateway
    private int PORT; // Port to listen on
    private static final String TAG = "ServerThread";
    private GlobalFileHolder fileHolder;
    //private Context context;

    public HotSpot(String ip, int port)//, Context context
    {
        this.HOTSPOT_IP = ip;
        this.PORT = port;
        this.fileHolder = GlobalFileHolder.getInstance();
        //this.context = context;
    }

    @Override
    public void run()
    {
        try
        {
            startServer();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            stopServer();
        }
    }

    public void startServer() {
        try {
            // Bind to the Hotspot IP and a specific port
            InetAddress inetAddress = InetAddress.getByName(HOTSPOT_IP);
            serverSocket = new ServerSocket(PORT, 50, inetAddress);
            System.out.println("Server started on " + HOTSPOT_IP + ":" + PORT);

            // Listen for incoming connections
            Socket clientSocket = serverSocket.accept();
            serverSocket.close(); // the client was accepted no more need for socket
            System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
            handleClient(clientSocket);
            clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception in ServerThread: " + e.getMessage());
        }
        finally {
            stopServer();
        }
    }

    private void handleClient(Socket clientSocket) {
        Log.println(Log.INFO,"Connection","Succefully connected");
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            inputStream = clientSocket.getInputStream();
            outputStream = clientSocket.getOutputStream();
            while(!fileHolder.stopWriting) {
                byte[] buffer = new byte[4096];
                int bytesRead = inputStream.read(buffer);

                if(!fileHolder.stopWriting)
                {
                    parsePacket(buffer);
                    writeToClient(outputStream, this.createResponse());
                }
            }
            fileHolder.endFileWriting();

        } catch (IOException e) {
            Log.println(Log.INFO,"Reader","Reader failed to read from buffer");
            throw new RuntimeException(e);
        }
    }

    private void parsePacket(byte[] message)
    {
        double elevation = packetParser.getElevation(message);
        double latitude = packetParser.getLatatiude(message);
        double longitude = packetParser.getLongtatiude(message);

        this.fileHolder.writeToFile(latitude, longitude, elevation);
        Drone_Control.setPosition(latitude,longitude);
    }

    private byte[] createResponse()
    {
        byte[] message = new byte[24];

        double latitude = Drone_Control.getLatitude();
        double longitude = Drone_Control.getLongitude();
        double elevation = Drone_Control.getElevation();

       ByteBuffer.wrap(message).putDouble(latitude);
       ByteBuffer.wrap(message).putDouble(8,longitude);
       ByteBuffer.wrap(message).putDouble(16,elevation);

        return message;
    }

    private void writeToClient(OutputStream out, byte[] message)
    {
        try {
            out.write(message);
            out.flush();
        }
        catch (IOException e)
        {
            Log.println(Log.INFO,"Writing to Stream","Failed to wright to stream");
        }
    }
    public void stopServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
