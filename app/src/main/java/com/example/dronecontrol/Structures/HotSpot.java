package com.example.dronecontrol.Structures;

import android.app.Activity;
import android.util.Log;


import com.example.dronecontrol.Drone_Control;

import java.io.DataOutputStream;
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
    private Activity fatherActivity;
    private int PORT; // Port to listen on
    private static final String TAG = "ServerThread";
    private GlobalFileHolder fileHolder;
    //private Context context;

    public HotSpot(int port,Activity activity)//, Context context
    {
        this.fatherActivity = activity;
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
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started" + ":" + PORT);

            // Listen for incoming connections
            Socket clientSocket = serverSocket.accept();
            Log.println(Log.DEBUG,"COnnection", "connected");
            serverSocket.close(); // the client was accepted no more need for socket
            System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
            handleClient(clientSocket);
            Log.println(Log.DEBUG,"Client","Finished handling client");
            clientSocket.close();
            GlobalFileHolder.stopWriting = false;

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
        DataOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            inputStream = clientSocket.getInputStream();
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
            this.fileHolder.startWriting();
            while(!fileHolder.stopWriting) {
                Log.println(Log.DEBUG,"File Holder", "state: " + String.valueOf(fileHolder.stopWriting));
                byte[] buffer = new byte[4096];
                int bytesRead = inputStream.read(buffer);

                if(!fileHolder.stopWriting && bytesRead > 0)
                {
                    parsePacket(buffer);
                    writeToClient(outputStream);
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
        Log.println(Log.DEBUG,"Connection Issues","Connection");
        this.fileHolder.writeToFile(latitude, longitude, elevation);
        Log.println(Log.DEBUG,"File Writing", "Written to File Succesfully");
        this.fatherActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Drone_Control.setPosition(latitude,longitude);
            }
        });
    }

    private void writeToClient(DataOutputStream out)
    {
        try {
            Log.println(Log.DEBUG,"Degree",String.valueOf(Drone_Control.getCompassDegrees()));
            out.writeDouble(Drone_Control.getDistance()/10);
            out.writeDouble(Drone_Control.getCompassDegrees());
            out.writeDouble(Drone_Control.getElevation() / 10000.0);
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
