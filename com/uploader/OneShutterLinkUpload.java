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

public class OneShutterLinkUpload  {

    // public static String WHITE_COLOR = "\u001B[0m";
    // public static String GREEN_COLOR = "\u001B[32m";
    // public static String RED_COLOR = "\u001B[31m";

    public static String WHITE_COLOR = "";
    public static String GREEN_COLOR = "";
    public static String RED_COLOR = "";

    private static String REF_PART_OF_LINK = "?ref=";
    private static boolean DELAY_FOR_UPLOAD = true;
    
    // private static String name_of_profile;    
    private static String link_to_project;
    private static String referal_name;
    private static boolean public_upload;

    private static boolean YOUTUBE_UPLOAD = false;
    private static boolean VIMEO_UPLOAD = false;

    public static boolean STOP = false;

    public OneShutterLinkUpload(String _link_to_project, String _referal_name, String _upload_resource, boolean _public_upload)  {

        // name_of_profile = new String();

        // name_of_profile = _name_of_profile;
        // delay_in_min = _delay_in_min;
        link_to_project = _link_to_project;
        referal_name = _referal_name;
        public_upload = _public_upload;

        if(!(Objects.equals(_link_to_project, "")) || (!Objects.equals(_link_to_project, null)))    {

            if(_upload_resource == "vimeo") VIMEO_UPLOAD = true;
            if(_upload_resource == "youtube") YOUTUBE_UPLOAD = true;

        }   else    {

            System.out.println("Set link to shutterstock project!");

        }

        File tempdir = new File("./tmp/");

        if(tempdir.mkdir()) {

            System.out.println("Temp Directory Created Successfully");

        }   else    {

            System.out.println("Cannot create Temp Directory! Directory already exists or check permisssions! Run as Admin, for example.");

        }

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


        System.out.println("Begin shutterstock.com parsing ...");

        Document clip_page = null;

        String hrefToCurrentClipOnPage = new String();

        String itemName;
        String itemDescription;
        String itemContentUrl;
        String itemContributorName;
        String itemTags = new String();

        Element itemNameElement;
        Element itemDescriptionElement;
        Element itemContentUrlElement;
        Element itemContributorNameElement;
        Elements itemTagsElements;

        try {

            link_to_project = link_to_project.toLowerCase();

            System.out.println("Link to upload: " + GREEN_COLOR + link_to_project + WHITE_COLOR);
            System.out.println("Referal Link: "   + GREEN_COLOR + referal_name + WHITE_COLOR);

        } catch(Exception e)    {

            e.printStackTrace();

        }

        // for(int i = 1; i < number_of_pages; i++)    {
        // do {

        // current_page++;
        // Counting number of pages
        try {

            // name_of_profile = "karasyamba";

            // String link_to_profile = "http://videohive.net/user/" + name_of_profile + "/portfolio\n";

            if(link_to_project == null) {

                System.out.println(RED_COLOR + "Set Link to upload!");
                System.exit(1);

            }

            // if(hrefToCurrentClipOnPageElements != null) {

                // for(Element hrefToCurrentClipOnPageElement: hrefToCurrentClipOnPageElements) {

                    hrefToCurrentClipOnPage = link_to_project;

                    // System.out.println("Href To Clip #" + (all_clips_count) + ":" + hrefToCurrentClipOnPage[clips_per_page_count]);

                    // threadSleepMillisec(1000);

                    clip_page = Jsoup.connect(hrefToCurrentClipOnPage).timeout(0).get();

                    System.out.println("\n---------------------------------------------------------");

                    itemContributorNameElement = clip_page.select("a[id=contributor-name]").first();
                    itemContributorName        = itemContributorNameElement.text();

                    System.out.println("Contributor Name: " + itemContributorName);

                    itemNameElement        = clip_page.select("meta[itemprop=name]").first();
                    itemName               = itemNameElement.attr("content");

                    System.out.println("Name: " + itemName);

                    itemDescriptionElement = clip_page.select("meta[itemprop=description]").first();
                    itemDescription        = itemDescriptionElement.attr("content");

                    System.out.println("Description: " + itemDescription);

                    itemContentUrlElement  = clip_page.select("meta[itemprop=contentUrl]").first();
                    itemContentUrl         = "http:" + itemContentUrlElement.attr("content");

                    System.out.println("Content Link: " + itemContentUrl);

                    itemTagsElements       = clip_page.select("a[href$=ref_context=keyword]");

                    for(Element itemTagsElement: itemTagsElements)  {

                        itemTags += itemTagsElement.text() + " ";

                    }

                    System.out.println("Tags: " + itemTags);

                    System.out.println("---------------------------------------------------------\n");

                    String fullPathToDownloadedVideo = "/tmp/" + itemContributorName + "/" + itemName.replace(" ", "_") + ".mp4";

                    System.out.println("Downloading video: " + itemContentUrl + " -> " + fullPathToDownloadedVideo);
                    // Download video from URL
                    VideoFromURL video_url = new VideoFromURL();

                    video_url.getVideoFromURL(itemContentUrl, itemName, itemContributorName);

                    if(YOUTUBE_UPLOAD)  {

                        // System.out.println("Uploading video to YouTube: " + fullPathToDownloadedVideo);

                        VideoToYoutube video_to_upload = new VideoToYoutube();
                        video_to_upload.AuthAndUpload(fullPathToDownloadedVideo, itemName, itemContributorName, "", itemTags, hrefToCurrentClipOnPage, itemDescription, public_upload);

                    }

                // }

            // }   else    {

                // System.out.println("Cannot find any Elements on page! hrefToCurrentClipOnPageElements is NULL!");

            // }

        }   catch(Exception e) {

            // e.printStackTrace();
            System.out.println("Connection or tag parsing problem.");

        }

        // try {

        //     imgElements = html.select("img[data-video-file-url$=.mp4]");

        //     String imgAttrAuthor[] = new String[imgElements.size()+1];
        //     String imgAttrName[] = new String[imgElements.size()+1];
        //     String imgAttrTags[] = new String[imgElements.size()+1];
        //     String imgAttrVideo[] = new String[imgElements.size()+1];
        //     String imgAttrPreview[] = new String[imgElements.size()+1];

        //     String buffer[] = new String[2];

        //     try {

        //         Thread.sleep(1000);

        //     }   catch(InterruptedException e) {   

        //         System.out.println("Interrapted Thread. Error in \"sleep\" function. ");
        //         Thread.currentThread().interrupt();

        //     }

        //     aElements = html.select("a[class=js-google-analytics__list-event-trigger");

        //     String aAttrLink[] = new String[aElements.size()+1];
        //     String aTagsAttrNames[] = new String[100];
        //     String aItemDescription[] = new String[100];

        //     // String buffer[] = new String[2];
        //     // for(int i = 0; i < imgElement.size())

        //     for(Element el: aElements)  {

        //         if(STOP) {

        //             System.out.println("Stop Parsing Pages.");
        //             break;

        //         }

        //         count++;

        //         aAttrLink[count] = "http://videohive.net" + el.attr("href");

        //         buffer = aAttrLink[count].split("\\?", 2);

        //         if((Objects.equals(referal_name, "")) || (Objects.equals(referal_name, null))) {

        //             aAttrLink[count] = buffer[0];

        //         }   else aAttrLink[count] = buffer[0] + REF_PART_OF_LINK + referal_name;

        //         System.out.println(RED_COLOR + count + ":" + WHITE_COLOR + aAttrLink[count]);

        //         project_page = Jsoup.connect(aAttrLink[count]).timeout(0).get();
        //         aTagsElements = project_page.select("a[href^=/tags/]");

        //         try {

        //             Thread.sleep(100);

        //         }   catch(InterruptedException e) {   

        //             System.out.println("Interrapted Thread. Error in \"sleep\" function (Parse elements section).");
        //             Thread.currentThread().interrupt();

        //         }

        //         userTextElements = project_page.select("[class=user-html]");

        //         // System.out.println("Size: " + aTagsElements.size() + ":" + userTextElements.size());
        //         System.out.println(GREEN_COLOR + "item-description:" + WHITE_COLOR);

        //         for(Element descrel: userTextElements)    {

        //             // descrel.removeAttr("a");
        //             aItemDescription[count] = descrel.text();

        //             // String buffer[] = new String[2];

        //             if(aItemDescription[count].toLowerCase().contains("Project features".toLowerCase()))    {

        //                 buffer = aItemDescription[count].split("Project features", 2);
        //                 aItemDescription[count] = buffer[0];

        //             }

        //             if(aItemDescription[count].toLowerCase().contains("Projects features".toLowerCase()))    {

        //                 buffer = aItemDescription[count].split("Projects features", 2);
        //                 aItemDescription[count] = buffer[0];
        //             }

                    
        //             aItemDescription[count] = aItemDescription[count].replace(">"," ");
        //             // aItemDescription[count].replace(">>>"," ");

        //             System.out.println(aItemDescription[count]);
                        
        //         }

        //         aTagsAttrNames[count] = "";

        //         for(Element subel: aTagsElements)   {

        //             aTagsAttrNames[count] += subel.attr("title") + " ";
        //             // System.out.println(aTagsAttrNames[count]);

        //         }

        //     }

        //     // for(int i = 1; i < count; i++)  System.out.println(aTagsAttrNames[count]);

        //     // System.exit(0);

        //     count = 0;

        //     for(Element el: imgElements)  {

        //         if(STOP) {

        //             System.out.println("Stop Uploading to Resource.");
        //             break;

        //         }

        //         count++;
        //         imgAttrAuthor[count] = el.attr("data-item-author");
        //         imgAttrName[count] = el.attr("data-item-name");
        //         // imgAttrTags[count] = el.attr("data-item-category");
        //         imgAttrVideo[count] = el.attr("data-video-file-url");
        //         imgAttrPreview[count] = el.attr("data-preview-url");

        //         String title = html.title();

        //         System.out.println(GREEN_COLOR + "\nTitle: " + WHITE_COLOR + title);

        //         System.out.println(GREEN_COLOR + "Author: " + WHITE_COLOR + imgAttrAuthor[count]);
        //         System.out.println(GREEN_COLOR + "Name: " + WHITE_COLOR + imgAttrName[count]);
        //         // System.out.println("\u001b[32mTags: " + imgAttrTags[count]);
        //         System.out.println(GREEN_COLOR + "Tags: " + WHITE_COLOR + aTagsAttrNames[count]);
        //         System.out.println(GREEN_COLOR + "Video: " + WHITE_COLOR + imgAttrVideo[count]);
        //         System.out.println(GREEN_COLOR + "Preview: " + WHITE_COLOR + imgAttrPreview[count]);
        //         // System.out.println(GREEN_COLOR + "Preview: " + WHITE_COLOR + imgAttrPreview[count]);
        //         System.out.println(GREEN_COLOR + "RefLink: " + WHITE_COLOR + aAttrLink[count]);
        //         System.out.println(GREEN_COLOR + "Description: " + WHITE_COLOR + aItemDescription[count]);
        //         System.out.print("\n");

        //         try {

        //             System.out.println("Downloading video: " + imgAttrVideo[count] + " -> ./tmp/" + imgAttrAuthor[count] + "/" + imgAttrName[count].replace(" ", "_") + ".mp4");
        //             VideoFromURL video_url = new VideoFromURL();
        //             video_url.getVideoFromURL(imgAttrVideo[count], imgAttrName[count], imgAttrAuthor[count]);

        //         }   catch(Exception e)  {

        //             e.printStackTrace();

        //         }

        //         if(DELAY_FOR_UPLOAD)    {

        //             System.out.println("DelayMin (in minutes) = " + delay_min_in_min);
        //             System.out.println("DelayMax (in minutes) = " + delay_max_in_min);

        //         }

        //         try {

        //             String local_filename = ("/tmp/" + imgAttrAuthor[count] + "/" + imgAttrName[count].replace(" ", "_") + ".mp4");

        //             if(YOUTUBE_UPLOAD)  {

        //                 VideoToYoutube video_to_upload  = new VideoToYoutube();
        //                 video_to_upload.AuthAndUpload(local_filename, imgAttrName[count], imgAttrAuthor[count], link_to_profile, aTagsAttrNames[count], aAttrLink[count], aItemDescription[count], public_upload);

        //             }
        //             if(VIMEO_UPLOAD)    {

        //                 System.out.println("Start Vimeo Uploading");

        //                 VideoToVimeo video_to_upload = new VideoToVimeo();
        //                 video_to_upload.AuthAndUpload(local_filename, imgAttrName[count], imgAttrAuthor[count], link_to_profile, aTagsAttrNames[count], aAttrLink[count], aItemDescription[count]);

        //             }

        //         }   catch(Exception e)   {

        //             e.printStackTrace();

        //         }

        //         if(DELAY_FOR_UPLOAD)    {

        //             //System.out.println("Waiting " + delay_in_min + " min (" + delay_in_min*60 + " sec) for upload.");

        //             try {

        //                 int random_delay_in_min;

        //                 Random rand = new Random();
        //                 // delay [from,to] [delay_min, delay_max]
        //                 // rand.nextInt(int _arg) generates begun of 0 to _arg

        //                 random_delay_in_min = rand.nextInt((int)delay_max_in_min - (int)delay_min_in_min + 1) + (int)delay_min_in_min;

        //                 if(delay_max_in_min >= delay_min_in_min) {

        //                     int delay_in_milliseconds = (int)(random_delay_in_min*1000*60);

        //                     System.out.println("You're set delay, then wait a " + random_delay_in_min + " minute please ...");

        //                     Thread.sleep(delay_in_milliseconds);

        //                 }   else    {

        //                     System.out.println("delayMax MUST be higher than delayMin! Set no delay.");

        //                 }


        //             }   catch(InterruptedException e) {   

        //                 System.out.println("Interrapted Thread. Error in \"sleep\" function (Parse elements section).");
        //                 Thread.currentThread().interrupt();

        //             }

        //         }

        //         // String imgAttr = imgElement1.text();
            
        //         // break;
        //     }

        // }   catch(Exception e)   {

        //     e.printStackTrace();
        // //     System.out.println("Check Profile name please or internet connection.");

        // }

    // }   while(current_page <= number_of_pages);

    System.out.println(GREEN_COLOR + "The End of Uploading!\n");
    try {
        System.in.read();
    }   catch(Exception e)  {
        e.printStackTrace();
    }

    }
}
