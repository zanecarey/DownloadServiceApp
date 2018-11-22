package com.example.zane.downloadserviceapp;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class FileService extends IntentService {

    public static final String TRANSACTION_DONE = "com.example.TRANSACTION_DONE";

    public FileService() {
        super(FileService.class.getName());
    }

    public FileService(String name){
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.e("FileService", "Service Started");

        String passedUrl = intent.getStringExtra("url");

        downloadFile(passedUrl);

        Log.e("FileService", "Service Stopped");

        Intent i = new Intent(TRANSACTION_DONE);

        FileService.this.sendBroadcast(i);
    }

    //
    protected void downloadFile(String url){

        String fileName = "myFile";

        try{
            FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);

            // Get File
            URL fileURL = new URL(url);

            // Create a connection we can use to read data from a url
            HttpURLConnection urlConnection = (HttpURLConnection) fileURL.openConnection();

            // Set method to GET
            urlConnection.setRequestMethod("GET");

            // Set that we want to allow output for this connection
            urlConnection.setDoOutput(true);

            // Start connection
            urlConnection.connect();

            // Gets an input stream for reading data
            InputStream inputStream = urlConnection.getInputStream();

            // Define the size of the buffer
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            // read reads a byte of data from the stream until there is nothing more
            while ( (bufferLength = inputStream.read(buffer)) > 0 ){

                // Write the data received to our file
                outputStream.write(buffer, 0, bufferLength);

            }

            // Close our connection to our file
            outputStream.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
