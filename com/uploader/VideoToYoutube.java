package com.uploader;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.InputStreamContent;
// import com.google.api.services.samples.youtube.cmdline.Auth;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.common.collect.Lists;
// import javax.servlet.http.*;

// import java.io.*;
// import java.lang.*;
// import java.util.List;
// import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader
;import java.util.ArrayList;
import java.util.List;
import java.lang.*;
import java.net.*;


public class VideoToYoutube {

    // public static String WHITE_COLOR = "\u001B[0m";
    // public static String GREEN_COLOR = "\u001B[32m";
    // public static String RED_COLOR = "\u001B[31m";

    public static String WHITE_COLOR = "";
    public static String GREEN_COLOR = "";
    public static String RED_COLOR = "";
    // public static boolean IS_PUBLIC = false;
    
    private static YouTube youtube;
    // private static 
    private static final String VIDEO_FILE_FORMAT = "video/*";

    private int video_count = 0;
    // private static final String SAMPLE_VIDEO_FILENAME = "Flash Giant FX.mp4";

    public int AuthAndUpload(String parsed_filename, String parsed_name_of_video, String parsed_name_of_author, String link_to_stock_profile, String parsed_tags, String parsed_ref_link, String parsed_description, boolean public_upload)   {
        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.upload");

        try {
            
            // For client_secrets_va.json
            Credential credential = Auth.authorize(scopes, "uploadvideo");
            // For client_secrets_vu.json
            // Credential credential = Auth.authorize(scopes, "videoupload");

            // For client_secrets_va.json
            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName("YoutubeUploaderApp").build();
            // For client_secrets_vu.json
            // youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName("YoutubeUploader").build();

            System.out.println("Uploading: " + parsed_filename);

            Video videoObjectDefiningMetadata = new Video();

            VideoStatus status = new VideoStatus();

            if(public_upload)   {

                status.setPrivacyStatus("public");

            }   else    {

                status.setPrivacyStatus("private");                

            }

            videoObjectDefiningMetadata.setStatus(status);

            VideoSnippet snippet = new VideoSnippet();

            // String result_name_of_video = parsed_name_of_video + " After Effects";
            String result_name_of_video = parsed_name_of_video;
            snippet.setTitle(result_name_of_video);
            // snippet.setDescription("Author: " + parsed_name_of_author + "\nLink: " + link_to_videohive);
            snippet.setDescription("\nAuthor: " + parsed_name_of_author + "\nProfile: " + link_to_stock_profile + "\n" + parsed_description + "\nTo see all project features go to this link:\n" + parsed_ref_link);

            List<String> tags = new ArrayList<String>();
            int tag_count = 0;

            if(parsed_tags != "")    {
                
                String[] devidedTagString = parsed_tags.split(" ");

                System.out.println("Tags:");
                System.out.println("-------------------------------");

                for(String el: devidedTagString)    {

                    tag_count++;
                    System.out.println(tag_count + ": " + el);
                    tags.add(el);

                }

                System.out.println("Tags Count: " + RED_COLOR + tag_count + WHITE_COLOR);

                snippet.setTags(tags);

            }

            // tags.add("after effects");
            // tags.add("after");
            // tags.add("effects");
            // tags.add("liquid");
            // tags.add("cinema4d");
            // tags.add("2d");
            // tags.add("2dFX");
            // tags.add("3d");
            // tags.add("FX");
            // tags.add("blender project");
            // tags.add("footage");
            // tags.add("video");
            // tags.add("videohive");
            // tags.add("free");
            // tags.add("slideshow");
            // tags.add("logo");
            // tags.add("logo reveal");
            // tags.add("reveal");
            // tags.add("houdini");
            // tags.add("motion");
            // tags.add("graphics");
            // tags.add("motion graphics");
            // tags.add("tutorial");
            // tags.add("training");
            // tags.add("trend");
            // tags.add("trending");

            video_count++;

            System.out.println("-------------------------------");

            System.out.println("Filename: "     + parsed_filename);
            System.out.println("NameOfVideo: "  + result_name_of_video);
            System.out.println("NameOfAuthor: " + parsed_name_of_author);
            
            //System.out.println("-------------------------------");       

            videoObjectDefiningMetadata.setSnippet(snippet);

            InputStreamContent mediaContent = new InputStreamContent(VIDEO_FILE_FORMAT, VideoToYoutube.class.getResourceAsStream(parsed_filename));

            YouTube.Videos.Insert videoInsert = youtube.videos().insert("snippet,statistics,status", videoObjectDefiningMetadata, mediaContent);

            MediaHttpUploader uploader = videoInsert.getMediaHttpUploader();

            uploader.setDirectUploadEnabled(false);

            MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {
                public void progressChanged(MediaHttpUploader uploader) throws IOException {
                    switch (uploader.getUploadState()) {
                        case INITIATION_STARTED:
                            System.out.println("Initiation Started");
                            break;
                        case INITIATION_COMPLETE:
                            System.out.println("Initiation Completed");
                            break;
                        case MEDIA_IN_PROGRESS:
                            System.out.println("Upload in progress");
                            // System.out.println("Upload percentage: " + uploader.getProgress());
                            break;
                        case MEDIA_COMPLETE:
                            System.out.println("Upload Completed!");
                            break;
                        case NOT_STARTED:
                            System.out.println("Upload Not Started!");
                            break;
                    }
                }
            };

            uploader.setProgressListener(progressListener);

            Video returnedVideo = videoInsert.execute();

            System.out.println("\n================== Returned Video ==================\n");
            System.out.println("  - Id: " + returnedVideo.getId());
            System.out.println("  - Title: " + returnedVideo.getSnippet().getTitle());
            System.out.println("  - Tags: " + returnedVideo.getSnippet().getTags());
            System.out.println("  - Privacy Status: " + returnedVideo.getStatus().getPrivacyStatus());
            System.out.println("  - Video Count: " + returnedVideo.getStatistics().getViewCount());
            System.out.println("\n================== ============== ==================\n");


        } catch (GoogleJsonResponseException e) {
            System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
            System.err.println("Throwable: " + t.getMessage());
            t.printStackTrace();
        }

        return 0;
    }

}