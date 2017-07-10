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

public class ShutterToResource  {

    // public static String WHITE_COLOR = "\u001B[0m";
    // public static String GREEN_COLOR = "\u001B[32m";
    // public static String RED_COLOR = "\u001B[31m";

    public static String WHITE_COLOR = "";
    public static String GREEN_COLOR = "";
    public static String RED_COLOR = "";

    private static String  REF_PART_OF_LINK1 = "?rid=";
    private static String  REF_PART_OF_LINK2 = "&rid=";
    private static boolean DELAY_FOR_UPLOAD = true;
    
    private static String link_to_profile;
    private static String name_of_profile;
    private static int number_of_pages;
    private static int current_page;
    private static int clips_per_page;
    private static int all_clips_count;
    private static double delay_min_in_min;
    private static double delay_max_in_min;
    private static String referal_name;
    private static boolean public_upload;
    private static String footageClipsInProfile;

    private static boolean YOUTUBE_UPLOAD = false;
    private static boolean VIMEO_UPLOAD = false;

    public static boolean STOP = false;

    public ShutterToResource(String _name_of_profile, double _delay_min_in_min, double _delay_max_in_min, String _referal_name, String _upload_resource, boolean _public_upload)  {

        name_of_profile = new String();

        name_of_profile = _name_of_profile;
        delay_min_in_min = _delay_min_in_min;
        delay_max_in_min = _delay_max_in_min;
        public_upload = _public_upload;

        referal_name = REF_PART_OF_LINK2 + _referal_name;

        clips_per_page = 100;
        all_clips_count = 0;

        if(!(Objects.equals(_name_of_profile, "")) || (!Objects.equals(_name_of_profile, null)))    {

            if(_upload_resource == "vimeo") VIMEO_UPLOAD = true;
            if(_upload_resource == "youtube") YOUTUBE_UPLOAD = true;

        }   else    {

            System.out.println("Set Profile Name!");

        }

        File tempdir = new File("./tmp/");

        if(tempdir.mkdir()) {

            System.out.println("Temp Directory Created Successfully or Already Exists");

        }   else    {

            System.out.println("Cannot create Temp Directory! Check permisssions! Run as Admin, for example.")            ;

        }

        STOP = false;

    }

    private static void threadSleepMillisec(int milliseconds_to_sleep) {

         try {

             Thread.sleep(milliseconds_to_sleep);

         }   catch(InterruptedException e) {  

             // System.out.println("Interrapted Thread. Error in \"sleep\" function. ");
             // Thread.currentThread().interrupt();
            e.printStackTrace();

         }

    }

    public static void GrabAndLoad() {

        System.out.println("Begin shutterstock.com parsing ...");

        Document html      = null;
        Document clip_page = null;
        
        Elements imgElements;
        Elements aElements;
        Elements userTextElements;
        Elements aTagsElements;

        // Find number of clips of current profile
        Elements footageClipsInProfileElements;

        Elements hrefToCurrentClipOnPageElements;

        String hrefToCurrentClipOnPage[] = new String[clips_per_page];

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

        current_page = 0;

        try {

            name_of_profile = name_of_profile.toLowerCase();

            System.out.println("Name of videohive.net/shutterstock.com profile: " + GREEN_COLOR + name_of_profile + WHITE_COLOR);
            System.out.println("DelayMin in minutes: " + GREEN_COLOR + delay_min_in_min + WHITE_COLOR);
            System.out.println("DelayMax in minutes: " + GREEN_COLOR + delay_max_in_min + WHITE_COLOR);
            System.out.println("Referal Link: " + GREEN_COLOR + referal_name + WHITE_COLOR);
            System.out.println("Upload from video #: " + GREEN_COLOR + AppGui.uploadFromVideoNumber + WHITE_COLOR);

        } catch(Exception e)    {

            e.printStackTrace();

        }

        // for(int i = 1; i < number_of_pages; i++)    {
        do {

        current_page++;
        // Counting number of pages
        try {

            // name_of_profile = "karasyamba";

            // String link_to_profile = "http://videohive.net/user/" + name_of_profile + "/portfolio\n";

            if(name_of_profile == null) {

                System.out.println(RED_COLOR + "Set Name of Profile to upload!");
                System.exit(1);

            }

            StringBuilder sb = new StringBuilder();

            sb.append("http://shutterstock.com/video/gallery?contributor=").append(name_of_profile).append("&page=").append(current_page).append("&perpage=").append(clips_per_page);

            link_to_profile = sb.toString();

            System.out.println("Your link: " + link_to_profile);
            
            html = Jsoup.connect(link_to_profile).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko").timeout(0).get();

            /* Хитрый ход. Каждый раз обновляем сколько всего страниц, ибо иначе делать не удобно. */

            footageClipsInProfileElements = html.select("span[class=\"total minor\"]");

            if(footageClipsInProfileElements != null)   {

                for(Element footageClipsInProfileElement: footageClipsInProfileElements)    {

                    // spanCount++;
                    footageClipsInProfile = footageClipsInProfileElement.text();
                    System.out.println("Span with num of videos: " + footageClipsInProfile);

                }

            }   else    {

                System.out.println("footageClipsInProfileElements is NULL!");

            }

            if(footageClipsInProfile != null)   {

                footageClipsInProfile = footageClipsInProfile.replace("(", "").replace(")", "").replace(",", "");

                number_of_pages = Integer.parseInt(footageClipsInProfile) / clips_per_page + 1;                

            }

            System.out.println("Formatted string (from Span): " + footageClipsInProfile);
            System.out.println("Number Of Pages: " + number_of_pages);

            hrefToCurrentClipOnPageElements = html.select("a[class=\"clip trackit\"]");

            int clips_per_page_count = 0; 

            if(hrefToCurrentClipOnPageElements != null) {

                for(Element hrefToCurrentClipOnPageElement: hrefToCurrentClipOnPageElements) {

                    all_clips_count++;

                    if(all_clips_count >= AppGui.uploadFromVideoNumber)     {

                    hrefToCurrentClipOnPage[clips_per_page_count] = hrefToCurrentClipOnPageElement.attr("abs:href");

                    System.out.println("Href To Clip #" + (all_clips_count) + ":" + hrefToCurrentClipOnPage[clips_per_page_count]);

                    threadSleepMillisec(1000);

                    clip_page = Jsoup.connect(hrefToCurrentClipOnPage[clips_per_page_count]).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko").timeout(0).get();

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

                    itemTags = "";

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
                        video_to_upload.AuthAndUpload(fullPathToDownloadedVideo, itemName, itemContributorName, (link_to_profile + referal_name), itemTags, (hrefToCurrentClipOnPage[clips_per_page_count] + referal_name), itemDescription, public_upload);

                    }

                    clips_per_page_count++;

                    // Thread.currentThread().interrupt();

                    // break;

                    if(DELAY_FOR_UPLOAD)    {

                    //System.out.println("Waiting " + delay_in_min + " min (" + delay_in_min*60 + " sec) for upload.");

                        try {

                            int random_delay_in_min;

                            Random rand = new Random();
                        // delay [from,to] [delay_min, delay_max]
                        // rand.nextInt(int _arg) generates begun of 0 to _arg

                            random_delay_in_min = rand.nextInt((int)delay_max_in_min - (int)delay_min_in_min + 1) + (int)delay_min_in_min;

                            if(delay_max_in_min >= delay_min_in_min) {

                                int delay_in_milliseconds = (int)(random_delay_in_min*1000*60);

                                System.out.println("You're set delay, then wait a " + random_delay_in_min + " minute please ...");

                                Thread.sleep(delay_in_milliseconds);

                            }   else    {

                                System.out.println("delayMax MUST be higher than delayMin!");

                            }


                        }   catch(InterruptedException e) {   

                            System.out.println("Interrapted Thread. Error in \"sleep\" function (Parse elements section).");
                            Thread.currentThread().interrupt();

                        }

                    }

                    }

                    if(STOP) {

                        System.out.println("Stop Parsing Pages.");
                        break;

                    }

                }

            }   else    {

                System.out.println("Cannot find any Elements on page! hrefToCurrentClipOnPageElements is NULL!");

            }

        }   catch(Exception e) {

            // e.printStackTrace();
            System.out.println("Connection or tag parsing problem.");

        }

        if(STOP) {

            System.out.println("Stop Page Iterations.");
            break;

        }

    }   while(current_page <= number_of_pages);

    System.out.println(GREEN_COLOR + "The End of Uploading!\n");
    try {
        System.in.read();
    }   catch(Exception e)  {
        e.printStackTrace();
    }

    }
}
