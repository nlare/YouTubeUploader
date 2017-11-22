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

public class OnePondLinkUpload  {

    // public static String WHITE_COLOR = "\u001B[0m";
    // public static String GREEN_COLOR = "\u001B[32m";
    // public static String RED_COLOR = "\u001B[31m";

    public static String WHITE_COLOR = "";
    public static String GREEN_COLOR = "";
    public static String RED_COLOR = "";

    private static String  REF_PART_OF_LINK1 = "?ref=";
    private static String  REF_PART_OF_LINK2 = "?ref=";
    private static boolean DELAY_FOR_UPLOAD = true;
    
    private static String  link_to_profile;    
    private static String  link_to_project;
    private static String  referal_name;
    private static boolean public_upload;

    private static boolean YOUTUBE_UPLOAD = false;
    private static boolean VIMEO_UPLOAD = false;

    public static boolean STOP = false;

    public OnePondLinkUpload(String _link_to_project, String _referal_name, String _upload_resource, boolean _public_upload)  {

        // name_of_profile = new String();

        // name_of_profile = _name_of_profile;
        // delay_in_min = _delay_in_min;
        link_to_project = _link_to_project;
        public_upload = _public_upload;

        referal_name = (REF_PART_OF_LINK1 + _referal_name);

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

        String itemName            = new String();
        String itemDescription     = new String();
        String itemContentUrl      = new String();
        String itemContributorName = new String();
        String itemTags            = new String();

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

                    // hrefToCurrentClipOnPage[clips_per_page_count] = hrefToCurrentClipOnPageElement.attr("abs:href");
                    hrefToCurrentClipOnPage = link_to_project;

                    System.out.println("\n---------------------------------------------------------");

                    System.out.println("Href To Clip: " + hrefToCurrentClipOnPage);

                    // userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").
                    clip_page = Jsoup.connect(hrefToCurrentClipOnPage).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko").timeout(0).maxBodySize(0).get();

                    Elements elementsWithItemInside = clip_page.select("div[class=\"u-paddingT10px u-paddingB10px u-colorDustyGray\"]");

                    for(Element element: elementsWithItemInside)    {
                        
                        itemContributorNameElement = clip_page.select("a[class=\"u-linkDodgerBlue\"]").first();
                        itemContributorName        = itemContributorNameElement.text();

                        link_to_profile            = itemContributorNameElement.attr("href");

                    }

                    System.out.println("Contributor Name: " + itemContributorName);

                    itemNameElement        = clip_page.select("span[class=\"u-paddingR10px u-padding0:35px\"]").first();
                    itemName               = itemNameElement.text();

                    System.out.println("Name: " + itemName);

                    itemDescriptionElement = clip_page.select("span[class=\"u-paddingR10px u-padding0:35px\"]").first();
                    itemDescription        = itemDescriptionElement.text();

                    System.out.println("Description: " + itemDescription);

                    // itemContentUrlElement  = clip_page.select("video[id=jp5player_video_0]").first();
                    // itemContentUrlElement  = clip_page.select("div[class=\"ItemDetailV3-section u-size9of10 u-paddingT40px u-paddingT60px:60em\"]").first();
                    itemContentUrlElement  = clip_page.select("div[id=\"mainJp5layer\"]").first();

                    String itemContentUrlBuffer = itemContentUrlElement.attr("data-jp5player");
                    // Select substring with link to video
                    itemContentUrl = itemContentUrlBuffer.substring(itemContentUrlBuffer.indexOf("http"), itemContentUrlBuffer.indexOf(".mp4") + 4);
                    // Replace \/ by /
                    itemContentUrl = itemContentUrl.replace("\\", "");

                    System.out.println("Content Link: " + itemContentUrl);

                    // PrintWriter writer = new PrintWriter("log.html", "UTF-8");
                    // writer.println(itemContentUrlElement);
                    // writer.close();

                    // writer = new PrintWriter("attr.html", "UTF-8");
                    // writer.println(itemContentUrl);
                    // writer.close();
                    // itemContentUrl         = itemContentUrlElement.attr("src");

                    // if(!itemContentUrl.equals(""))  {
                        
                    // }

                    itemTagsElements = clip_page.select("a[itemprop=keywords]");

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

                        System.out.println("Uploading video to YouTube: " + fullPathToDownloadedVideo);

                        VideoToYoutube video_to_upload = new VideoToYoutube();
                        video_to_upload.AuthAndUpload(fullPathToDownloadedVideo, itemName, itemContributorName, (link_to_profile + referal_name), itemTags, (hrefToCurrentClipOnPage + referal_name), itemDescription, public_upload);

                    }

                    // clips_per_page_count++;

                    // Thread.currentThread().interrupt();

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
