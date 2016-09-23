package com.uploader;

import java.util.*;
import java.util.regex.*;
import java.io.File;
import java.io.IOException;
import java.lang.*;
import java.net.*;

// import org.jsoup.Jsoup;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Node;
// import org.apache.commons.io.*;
// import org.jsoup.nodes.Elements;

// import org.jsoup.nodes.;

public class OneLinkUpload  {

    // public static String WHITE_COLOR = "\u001B[0m";
    // public static String GREEN_COLOR = "\u001B[32m";
    // public static String RED_COLOR = "\u001B[31m";

    public static String WHITE_COLOR = "";
    public static String GREEN_COLOR = "";
    public static String RED_COLOR = "";

    private static String REF_PART_OF_LINK = "?ref=";
    private static boolean DELAY_FOR_UPLOAD = true;
    
    private static String name_of_profile;    
    private static String link_to_project;
    private static String referal_name;
    private static boolean public_upload;

    private static boolean YOUTUBE_UPLOAD = false;
    private static boolean VIMEO_UPLOAD = false;

    public static boolean STOP = false;

    public OneLinkUpload(String _link_to_project, String _referal_name, String _upload_resource, boolean _public_upload)  {

        // name_of_profile = new String();

        name_of_profile = "Test_String";
        // delay_in_min = _delay_in_min;
        link_to_project = _link_to_project;
        referal_name = _referal_name;
        public_upload = _public_upload;

        if(!(Objects.equals(_link_to_project, "")) || (!Objects.equals(_link_to_project, null)))    {

            if(_upload_resource == "vimeo") VIMEO_UPLOAD = true;
            if(_upload_resource == "youtube") YOUTUBE_UPLOAD = true;

        }   else    {

            System.out.println("Set Link to project!");

        }

        STOP = false;

    }

    public static void GrabAndLoad() {

        // ----------------------GUI----------------------

        // System.out.println("Creating GUI...");

        // try {

        //     AppGui MainGui = new AppGui();
        //     MainGui.setVisible(true);

        // }   catch(Exception e)   {

        //     e.printStackTrace();

        // }

        // ----------------------GUI----------------------


        System.out.println("Begin...");
        System.out.println("Vimeo-Trigger:" + VIMEO_UPLOAD);

        Document html = null;
        Document project_page = null;
        // Element imgElement[] = new Element [10];
        Elements imgElements;
        Elements aElements;
        Elements metaElementWithLink;
        Elements metaElementWithName;
        Elements linkElementWithOriginalLink;
        Elements userTextElements;
        Elements aTagsElements;

        Elements spanNumOfPagesElement;

        try {

            name_of_profile = name_of_profile.toLowerCase();

            System.out.println("Link to upload: " + GREEN_COLOR + link_to_project + WHITE_COLOR);
            System.out.println("Referal Link: " + GREEN_COLOR + referal_name + WHITE_COLOR);
            
        } catch(Exception e)    {

            e.printStackTrace();

        }

        try {

            System.out.println("Your link: " + link_to_project);

            if(link_to_project == null) {

                System.out.println(RED_COLOR + "Program get out!");
                System.exit(1);

            }   

            html = Jsoup.connect(link_to_project).timeout(0).get();

            /* Хитрый ход. Каждый раз обновляем сколько всего страниц, ибо иначе делать не удобно. */

        }   catch(Exception e) {

            // System.out.println("Cannot connect! Link is not correct or internet connection is lost.");
            e.printStackTrace();

        }

        try {

            imgElements = html.select("img[data-video-file-url$=.mp4]");

            String imgAttrAuthor = new String();
            String imgAttrName = new String();
            String imgAttrTags = new String();
            String imgAttrPreview = new String();

            String metaLinkToVideo = new String();
            String metaNameOfVideo = new String();

            String linkOriginalLink = new String();

            String buffer[] = new String[2];

            aElements = html.select("a[class=js-google-analytics__list-event-trigger");

            String linkAttrLink = new String();

            String aTagsAttrNames = new String();
            String aItemDescription = new String();

            Element el = imgElements.last();

            metaElementWithLink = html.select("meta[itemprop=contentURL]");

            Element elLink = metaElementWithLink.last();

            metaElementWithName = html.select("meta[itemprop=name]");

            Element elName = metaElementWithName.last();

            linkElementWithOriginalLink = html.select("link[rel$=canonical]");

            Element elOriginalLink = linkElementWithOriginalLink.last();

            // for(Element el: aElements)  {

                buffer = link_to_project.split("\\?", 2);

                if((Objects.equals(referal_name, "")) || (Objects.equals(referal_name, null))) {

                    link_to_project = buffer[0];

                }   else link_to_project = buffer[0] + REF_PART_OF_LINK + referal_name;

                System.out.println(RED_COLOR + "1" + ":" + WHITE_COLOR + link_to_project);

                project_page = Jsoup.connect(link_to_project).timeout(0).get();
                aTagsElements = project_page.select("a[href^=/tags/]");

                try {

                    Thread.sleep(100);

                }   catch(InterruptedException e) {   

                    System.out.println("Interrapted Thread. Error in \"sleep\" function (Parse elements section).");
                    Thread.currentThread().interrupt();

                }

                userTextElements = project_page.select("[class=user-html]");

                System.out.println("Size: " + aTagsElements.size() + ":" + userTextElements.size());
                System.out.println(GREEN_COLOR + "item-description:" + WHITE_COLOR);

                for(Element descrel: userTextElements)    {

                    // descrel.removeAttr("a");
                    aItemDescription = descrel.text();

                    // String buffer[] = new String[2];

                    if(aItemDescription.toLowerCase().contains("Project features".toLowerCase()))    {

                        buffer = aItemDescription.split("Project features", 2);
                        aItemDescription = buffer[0];

                    }

                    if(aItemDescription.toLowerCase().contains("Projects features".toLowerCase()))    {

                        buffer = aItemDescription.split("Projects features", 2);
                        aItemDescription = buffer[0];
                    }

                    
                    aItemDescription = aItemDescription.replace(">"," ");
                    // aItemDescription[count].replace(">>>"," ");

                    System.out.println(aItemDescription);
                        
                }

                aTagsAttrNames = "";

                for(Element subel: aTagsElements)   {

                    aTagsAttrNames += subel.attr("title") + " ";
                    // System.out.println(aTagsAttrNames[count]);

                }

                aAttrLink = link_to_project;

                imgAttrAuthor = el.attr("data-item-author");
                imgAttrPreview = el.attr("data-preview-url");

                try {

                    metaLinkToVideo = elLink.attr("content");

                } catch (Exception e)   {

                    e.printStackTrace();

                }

                try {

                    metaNameOfVideo = elName.attr("content");

                } catch (Exception e)  {

                    e.printStackTrace();

                }

                try {

                    linkOriginalLink = elOriginalLink.attr("href");

                } catch (Exception e)  {

                    e.printStackTrace();

                }

                linkOriginalLink += ("?ref=" + referal_name); 
                link_to_project += ("?ref=" + referal_name); 

                String title = html.title();

                System.out.println(GREEN_COLOR + "\nTitle: " + WHITE_COLOR + title);

                System.out.println(GREEN_COLOR + "Author: " + WHITE_COLOR + imgAttrAuthor);
                System.out.println(GREEN_COLOR + "Name: " + WHITE_COLOR + metaNameOfVideo);
                System.out.println(GREEN_COLOR + "Tags: " + WHITE_COLOR + aTagsAttrNames);
                System.out.println(GREEN_COLOR + "Video: " + WHITE_COLOR + metaLinkToVideo);
                System.out.println(GREEN_COLOR + "Preview: " + WHITE_COLOR + imgAttrPreview);
                System.out.println(GREEN_COLOR + "RefLink: " + WHITE_COLOR + linkOriginalLink);
                System.out.println(GREEN_COLOR + "Description: " + WHITE_COLOR + aItemDescription);
                System.out.print("\n");

                try {

                    System.out.println("Downloading video: " + metaLinkToVideo + " -> ./tmp/" + imgAttrAuthor + "/" + metaNameOfVideo.replace(" ", "_") + ".mp4");
                    VideoFromURL video_url = new VideoFromURL();
                    video_url.getVideoFromURL(metaLinkToVideo, metaNameOfVideo, imgAttrAuthor);

                }   catch(Exception e)  {

                    e.printStackTrace();

                }

                try {

                    String local_filename = ("/tmp/" + imgAttrAuthor + "/" + metaNameOfVideo.replace(" ", "_") + ".mp4");

                    if(YOUTUBE_UPLOAD)  {

                        VideoToYoutube video_to_upload  = new VideoToYoutube();
                        video_to_upload.AuthAndUpload(local_filename, metaNameOfVideo, imgAttrAuthor, link_to_project, aTagsAttrNames, linkOriginalLink, aItemDescription, public_upload);

                    }

                    if(VIMEO_UPLOAD)    {

                        System.out.println("Start Vimeo Uploading");

                        VideoToVimeo video_to_upload = new VideoToVimeo();
                        video_to_upload.AuthAndUpload(local_filename, metaNameOfVideo
                            , imgAttrAuthor, link_to_project, aTagsAttrNames, linkOriginalLink, aItemDescription);

                    }

                }   catch(Exception e)   {

                    e.printStackTrace();

                }

                // if(DELAY_FOR_UPLOAD)    {

                //     System.out.println("Waiting " + delay_in_min + " min (" + delay_in_min*60 + " sec) for upload.");

                //     try {

                //     int delay_in_milliseconds = (int)(delay_in_min*1000*60);
                //     Thread.sleep(delay_in_milliseconds);

                //     }   catch(InterruptedException e) {   

                //         System.out.println("Interrapted Thread. Error in \"sleep\" function (Parse elements section).");
                //         Thread.currentThread().interrupt();

                //     }

                // }

                // String imgAttr = imgElement1.text();
            
                // break;
            // }

        }   catch(Exception e)   {

            // e.printStackTrace();
            System.out.println("Check URL address or internet connection.");

        }

    // }   while(current_page < number_of_pages);

    System.out.println(GREEN_COLOR + "The End of Uploading!\n");
    try {
        System.in.read();
    }   catch(Exception e)  {
        e.printStackTrace();
    }

    }
}