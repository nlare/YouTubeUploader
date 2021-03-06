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
import java.io.InputStreamReader;
import java.util.ArrayList;
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

    // InputStreamContent mediaContent = null;
    // private static final String SAMPLE_VIDEO_FILENAME = "Flash Giant FX.mp4";

    public int AuthAndUpload(String parsed_filename, String parsed_name_of_video, String parsed_name_of_author, String link_to_videohive, String parsed_tags, String parsed_ref_link, String parsed_description, boolean public_upload)   {
        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.upload");

        try {

            Credential credential = Auth.authorize(scopes, "uploadvideo");

            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName("YoutubeUploaderApp").build();

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

            snippet.setTitle(parsed_name_of_video);
            // snippet.setDescription("Author: " + parsed_name_of_author + "\nLink: " + link_to_videohive);
            snippet.setDescription(parsed_ref_link + "\nAuthor: " + parsed_name_of_author + "\n" + parsed_description + "\nTo see all project features go to this link:\n" + parsed_ref_link);

            List<String> tags = new ArrayList<String>();

            int tag_count = 0;
            String[] devidedTagString = parsed_tags.split(" ");

            System.out.println("Tags:");
            System.out.println("-------------------------------");

            for(String el: devidedTagString)    {

                tag_count++;
                System.out.println(tag_count + ": " + el);
                tags.add(el);

            }

            // tags.add("after effects");
            // tags.add("after");
            // tags.add("effects");
            // tags.add("template");
            // tags.add("project");
            // tags.add("adobe");
            // tags.add("free");
            // tags.add("videohive");
            // tags.add("download");
            // tags.add("clean");
            // tags.add("corparate");
            // tags.add("wedding");
            // tags.add("logo");
            // tags.add("opener");
            // tags.add("titles");
            // tags.add("slideshow");
            // tags.add("xmas");
            // tags.add("Christmas");
            // tags.add("epic");
            // tags.add("cinematic");
            // tags.add("tv");
            // tags.add("video");
            // tags.add("dynamic");

            // 3d, animation, flash, tutorial, particular, element, flat, explainer, business, flares

            // tags.add("3d");
            // tags.add("animation");
            // tags.add("flash");
            // tags.add("tutorial");
            // tags.add("particular");
            // tags.add("element");
            // tags.add("flat");
            // tags.add("explainer");
            // tags.add("business");
            // tags.add("flares");

            video_count++;

            System.out.println("-------------------------------");

            System.out.println("Filename: " + parsed_filename);
            System.out.println("NameOfVideo: " + parsed_name_of_video);
            System.out.println("NameOfAuthor: " + parsed_name_of_author);
            System.out.println("Tags Count: " + RED_COLOR + tag_count + WHITE_COLOR);
            System.out.println("Video Count: " + RED_COLOR + video_count + WHITE_COLOR);

            snippet.setTags(tags);

            System.out.println("Test1");

            videoObjectDefiningMetadata.setSnippet(snippet);

            System.out.println("Test2");

            // InputStream content = .getContentAsStream();

            // try {

            // System.out.println("Filename: " + parsed_filename);

            Thread.sleep(1000);

            InputStreamContent mediaContent = new InputStreamContent("video/*", VideoToYoutube.class.getResourceAsStream("32k3j3h2j3h"));

            // }   catch(Exception e) {

                // e.printStackTrace();

            // }

            System.out.println("Test3");

            // try {

            YouTube.Videos.Insert videoInsert = youtube.videos().insert("snippet,statistics,status", videoObjectDefiningMetadata, mediaContent);

            // }   catch(Exception e) {

                // e.printStackTrace();

            // }

            System.out.println("Test4");

            MediaHttpUploader uploader = videoInsert.getMediaHttpUploader();

            System.out.println("Test5");

            uploader.setDirectUploadEnabled(false);

            System.out.println("Test6");

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