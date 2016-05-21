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

public class HiveToResource  {

    // public static String WHITE_COLOR = "\u001B[0m";
    // public static String GREEN_COLOR = "\u001B[32m";
    // public static String RED_COLOR = "\u001B[31m";

    public static String WHITE_COLOR = "";
    public static String GREEN_COLOR = "";
    public static String RED_COLOR = "";

    private static String REF_PART_OF_LINK = "?ref=";
    private static boolean DELAY_FOR_UPLOAD = true;
    
    private static String link_to_profile;
    private static String name_of_profile;
    private static int number_of_pages;
    private static int current_page;
    private static double delay_in_min;
    private static String referal_name;
    private static boolean public_upload;

    private static boolean YOUTUBE_UPLOAD = false;
    private static boolean VIMEO_UPLOAD = false;

    public static boolean STOP = false;

    public HiveToResource(String _name_of_profile, double _delay_in_min, String _referal_name, String _upload_resource, boolean _public_upload)  {

        name_of_profile = new String();

        name_of_profile = _name_of_profile;
        delay_in_min = _delay_in_min;
        referal_name = _referal_name;
        public_upload = _public_upload;

        if(!(Objects.equals(_name_of_profile, "")) || (!Objects.equals(_name_of_profile, null)))    {

            if(_upload_resource == "vimeo") VIMEO_UPLOAD = true;
            if(_upload_resource == "youtube") YOUTUBE_UPLOAD = true;

        }   else    {

            System.out.println("Set Profile Name!");

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
        Elements userTextElements;
        Elements aTagsElements;

        Elements spanNumOfPagesElement;
        
        // Element imgElement;
        // Elements links;

        current_page = 0;

        try {

            // name_of_profile = "karasyamba";
            // System.in.read();
            // System.out.print("Enter name of videohive.net profile (press Enter) (Введи имя профиля): ");

            name_of_profile = name_of_profile.toLowerCase();

            System.out.println("Name of videohive.net profile: " + GREEN_COLOR + name_of_profile + WHITE_COLOR);
            System.out.println("Delay in minutes: " + GREEN_COLOR + delay_in_min + WHITE_COLOR);
            System.out.println("Referal Link: " + GREEN_COLOR + referal_name + WHITE_COLOR);
            
            // Scanner in_str = new Scanner(System.in);

            // name_of_profile = new String();
            // name_of_profile = in_str.nextLine();

            // System.out.print("Enter number_of_pages (Введи количество страниц профиля на videohive): ");

            // Scanner in_number_of_pages = new Scanner(System.in);

            // number_of_pages = in_number_of_pages.nextInt();

            // System.out.print("Enter delay_in_min (Введи задержку в минутах ): ");

            // Scanner in_delay_in_min = new Scanner(System.in);

            // delay_in_min = in_delay_in_min.nextInt();
            
            // System.in.read(data);

        } catch(Exception e)    {

            e.printStackTrace();

        }

        // for(int i = 1; i < number_of_pages; i++)    {
        do {

        current_page++;

        try {

            // name_of_profile = "karasyamba";

            // String link_to_profile = "http://videohive.net/user/" + name_of_profile + "/portfolio\n";

            StringBuilder sb = new StringBuilder();

            sb.append("http://videohive.net/user/").append(name_of_profile).append("/portfolio?page=").append(current_page);

            link_to_profile = sb.toString();

            // link_to_profile += "/portfolio";

            System.out.println("Your link: " + link_to_profile);

            if(name_of_profile == null) {

                System.out.println(RED_COLOR + "Program get out!");
                System.exit(1);

            }   

            // else {

            //     // System.out.println(link_to_profile);
            //     if(Objects.equals(link_to_profile, "http://videohive.net/user/karasyamba/portfolio"))    {

            //         System.out.println("cool!");
            //         System.exit(0);

            //     }

            // }
            
            html = Jsoup.connect(link_to_profile).timeout(0).get();

            // int count = 0;

            /* Хитрый ход. Каждый раз обновляем сколько всего страниц, ибо иначе делать не удобно. */

            String spanNumOfPages = new String();

            // int spanCount = 0;
            // spanNumOfPagesElement = html.getElementsByAttributeValue("class","pagination__summary");
            spanNumOfPagesElement = html.select("span:contains(Page)");

            for(Element number_of_pages_element: spanNumOfPagesElement)    {

                // spanCount++;
                spanNumOfPages = number_of_pages_element.text();

            }

            // System.out.println( RED_COLOR + "spanCount: " + WHITE_COLOR + spanCount);
            // System.out.println( RED_COLOR + "Element_size: " + WHITE_COLOR + spanNumOfPagesElement.size());

            if(spanNumOfPagesElement.size() == 0)   {

                number_of_pages = 1;

            }   else {

                String buffer[] = spanNumOfPages.split("of ", 2);

                // System.out.println( RED_COLOR + "spanNumOfPages: " + WHITE_COLOR + buffer[1]);
                number_of_pages = Integer.parseInt(buffer[1]);

            }


            System.out.println( WHITE_COLOR + "Number of Pages is " + GREEN_COLOR + number_of_pages);

            // ref_link = ;
            // File f_in = new File("./portfolio.html");
            // html = Jsoup.parse(f_in, "UTF-8", "");
            // html = Jsoup.connect("http://videohive.net/user/karasyamba/portfolio").get();

        }   catch(Exception e) {

            // e.printStackTrace();
            System.out.println("Cannot connect! Profile Name is not correct or internet connection is lost.");

        }

        try {

            int count = 0;

            imgElements = html.select("img[data-video-file-url$=.mp4]");

            String imgAttrAuthor[] = new String[imgElements.size()+1];
            String imgAttrName[] = new String[imgElements.size()+1];
            String imgAttrTags[] = new String[imgElements.size()+1];
            String imgAttrVideo[] = new String[imgElements.size()+1];
            String imgAttrPreview[] = new String[imgElements.size()+1];

            String buffer[] = new String[2];

            try {

                Thread.sleep(1000);

            }   catch(InterruptedException e) {   

                System.out.println("Interrapted Thread. Error in \"sleep\" function. ");
                Thread.currentThread().interrupt();

            }

            aElements = html.select("a[class=js-google-analytics__list-event-trigger");

            String aAttrLink[] = new String[aElements.size()+1];
            String aTagsAttrNames[] = new String[100];
            String aItemDescription[] = new String[100];

            // String buffer[] = new String[2];
            // for(int i = 0; i < imgElement.size())

            for(Element el: aElements)  {

                if(STOP) {

                    System.out.println("Stop Parsing Pages.");
                    break;

                }

                count++;

                aAttrLink[count] = "http://videohive.net" + el.attr("href");

                buffer = aAttrLink[count].split("\\?", 2);

                if((Objects.equals(referal_name, "")) || (Objects.equals(referal_name, null))) {

                    aAttrLink[count] = buffer[0];

                }   else aAttrLink[count] = buffer[0] + REF_PART_OF_LINK + referal_name;

                System.out.println(RED_COLOR + count + ":" + WHITE_COLOR + aAttrLink[count]);

                project_page = Jsoup.connect(aAttrLink[count]).timeout(0).get();
                aTagsElements = project_page.select("a[href^=/tags/]");

                try {

                    Thread.sleep(100);

                }   catch(InterruptedException e) {   

                    System.out.println("Interrapted Thread. Error in \"sleep\" function (Parse elements section).");
                    Thread.currentThread().interrupt();

                }

                userTextElements = project_page.select("[class=user-html]");

                // System.out.println("Size: " + aTagsElements.size() + ":" + userTextElements.size());
                System.out.println(GREEN_COLOR + "item-description:" + WHITE_COLOR);

                for(Element descrel: userTextElements)    {

                    // descrel.removeAttr("a");
                    aItemDescription[count] = descrel.text();

                    // String buffer[] = new String[2];

                    if(aItemDescription[count].toLowerCase().contains("Project features".toLowerCase()))    {

                        buffer = aItemDescription[count].split("Project features", 2);
                        aItemDescription[count] = buffer[0];

                    }

                    if(aItemDescription[count].toLowerCase().contains("Projects features".toLowerCase()))    {

                        buffer = aItemDescription[count].split("Projects features", 2);
                        aItemDescription[count] = buffer[0];
                    }

                    
                    aItemDescription[count] = aItemDescription[count].replace(">"," ");
                    // aItemDescription[count].replace(">>>"," ");

                    System.out.println(aItemDescription[count]);
                        
                }

                aTagsAttrNames[count] = "";

                for(Element subel: aTagsElements)   {

                    aTagsAttrNames[count] += subel.attr("title") + " ";
                    // System.out.println(aTagsAttrNames[count]);

                }

            }

            // for(int i = 1; i < count; i++)  System.out.println(aTagsAttrNames[count]);

            // System.exit(0);

            count = 0;

            for(Element el: imgElements)  {

                if(STOP) {

                    System.out.println("Stop Uploading to Resource.");
                    break;

                }

                count++;
                imgAttrAuthor[count] = el.attr("data-item-author");
                imgAttrName[count] = el.attr("data-item-name");
                // imgAttrTags[count] = el.attr("data-item-category");
                imgAttrVideo[count] = el.attr("data-video-file-url");
                imgAttrPreview[count] = el.attr("data-preview-url");

                String title = html.title();

                System.out.println(GREEN_COLOR + "\nTitle: " + WHITE_COLOR + title);

                System.out.println(GREEN_COLOR + "Author: " + WHITE_COLOR + imgAttrAuthor[count]);
                System.out.println(GREEN_COLOR + "Name: " + WHITE_COLOR + imgAttrName[count]);
                // System.out.println("\u001b[32mTags: " + imgAttrTags[count]);
                System.out.println(GREEN_COLOR + "Tags: " + WHITE_COLOR + aTagsAttrNames[count]);
                System.out.println(GREEN_COLOR + "Video: " + WHITE_COLOR + imgAttrVideo[count]);
                System.out.println(GREEN_COLOR + "Preview: " + WHITE_COLOR + imgAttrPreview[count]);
                // System.out.println(GREEN_COLOR + "Preview: " + WHITE_COLOR + imgAttrPreview[count]);
                System.out.println(GREEN_COLOR + "RefLink: " + WHITE_COLOR + aAttrLink[count]);
                System.out.println(GREEN_COLOR + "Description: " + WHITE_COLOR + aItemDescription[count]);
                System.out.print("\n");

                try {

                    System.out.println("Downloading video: " + imgAttrVideo[count] + " -> ./tmp/" + imgAttrAuthor[count] + "/" + imgAttrName[count].replace(" ", "_") + ".mp4");
                    VideoFromURL video_url = new VideoFromURL();
                    video_url.getVideoFromURL(imgAttrVideo[count], imgAttrName[count], imgAttrAuthor[count]);

                }   catch(Exception e)  {

                    e.printStackTrace();

                }

                if(DELAY_FOR_UPLOAD)    {

                    System.out.println("Delay (in minutes) = " + delay_in_min);

                }

                try {

                    String local_filename = ("/tmp/" + imgAttrAuthor[count] + "/" + imgAttrName[count].replace(" ", "_") + ".mp4");

                    if(YOUTUBE_UPLOAD)  {

                        VideoToYoutube video_to_upload  = new VideoToYoutube();
                        video_to_upload.AuthAndUpload(local_filename, imgAttrName[count], imgAttrAuthor[count], link_to_profile, aTagsAttrNames[count], aAttrLink[count], aItemDescription[count], public_upload);

                    }
                    if(VIMEO_UPLOAD)    {

                        System.out.println("Start Vimeo Uploading");

                        VideoToVimeo video_to_upload = new VideoToVimeo();
                        video_to_upload.AuthAndUpload(local_filename, imgAttrName[count], imgAttrAuthor[count], link_to_profile, aTagsAttrNames[count], aAttrLink[count], aItemDescription[count]);

                    }

                }   catch(Exception e)   {

                    e.printStackTrace();

                }

                if(DELAY_FOR_UPLOAD)    {

                    System.out.println("Waiting " + delay_in_min + " min (" + delay_in_min*60 + " sec) for upload.");

                    try {

                    int delay_in_milliseconds = (int)(delay_in_min*1000*60);
                    Thread.sleep(delay_in_milliseconds);

                    }   catch(InterruptedException e) {   

                        System.out.println("Interrapted Thread. Error in \"sleep\" function (Parse elements section).");
                        Thread.currentThread().interrupt();

                    }

                }

                // String imgAttr = imgElement1.text();
            
                // break;
            }

        }   catch(Exception e)   {

            // e.printStackTrace();
            System.out.println("Check Profile name please or internet connection.");

        }

    }   while(current_page < number_of_pages);

    System.out.println(GREEN_COLOR + "The End of Uploading!\n");
    try {
        System.in.read();
    }   catch(Exception e)  {
        e.printStackTrace();
    }

    }
}
