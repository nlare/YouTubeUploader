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

    private static String REF_PART_OF_LINK1 = "?rid=";
    private static String REF_PART_OF_LINK2 = "&rid=";
    private static boolean DELAY_FOR_UPLOAD = true;
    
    private static String link_to_profile;    
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
        referal_name = (REF_PART_OF_LINK2 + _referal_name);
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

                    hrefToCurrentClipOnPage = link_to_project;

                    // threadSleepMillisec(1000);

                    clip_page = Jsoup.connect(hrefToCurrentClipOnPage).timeout(0).get();

                    System.out.println("\n---------------------------------------------------------");

                    itemContributorNameElement = clip_page.select("a[id=contributor-name]").first();
                    itemContributorName        = itemContributorNameElement.text();

                    link_to_profile            = "http://shutterstock.com/video/gallery?contributor=" + itemContributorName + referal_name;

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

                        VideoToYoutube video_to_upload = new VideoToYoutube();
                        video_to_upload.AuthAndUpload(fullPathToDownloadedVideo, itemName, itemContributorName, link_to_profile, itemTags, (hrefToCurrentClipOnPage +  referal_name), itemDescription, public_upload);

                    }


        }   catch(Exception e) {

            // e.printStackTrace();
            System.out.println("Connection or tag parsing problem.");

        }

    System.out.println(GREEN_COLOR + "The End of Uploading!\n");
    try {
        System.in.read();
    }   catch(Exception e)  {
        e.printStackTrace();
    }

    }
}
