package com.uploader;

import java.util.*;
import java.io.File;
import java.io.IOException;
// import java.nio.*;
import java.lang.*;
import java.net.URL;

import org.apache.commons.io.*;


public class VideoFromURL    {

    private URL weblink;
    private String name_of_video_with_extension;
    private File f_out;
    private FileUtils file_utils;

    // VideoFromURL()  {
    //     f_out = null;
    //     name_of_video_with_extension = "";
    // }
    
    int getVideoFromURL(String link_buffer, String name_of_video, String name_of_profile) {

        // System.out.println("-----------------------------------");
        // System.out.println("Link: " + link_buffer);
        // System.out.println("Name: " + name_of_video);
        // System.out.println("\n");

        try {

            name_of_video_with_extension = "./tmp/" + name_of_profile + "/" + name_of_video.replace(" ", "_") + ".mp4";

            f_out = new File(name_of_video_with_extension);

            weblink = new URL(link_buffer);
            
        }   catch(Exception e) {

            e.printStackTrace();

        }

        try {
            
            // if(!f_out.exists())
                file_utils.copyURLToFile(weblink, f_out);
            // else
                // System.out.println("File: " + name_of_video_with_extension + " is exist in filesystem.\n");

        }   catch(Exception e)    {

            e.printStackTrace();

        }

        return 0;
    };

}
