package com.uploader;

import java.io.File;

public class VideoToVimeo   {
        
    private static String videoEndPoint = new String();
    private static String name = new String();
    private static String desc = new String();
    private static String license = new String(); //see Vimeo API Documentation
    private static String privacyView = new String(); //see Vimeo API Documentation
    private static String privacyEmbed = new String(); //see Vimeo API Documentation
    private static boolean reviewLink;

    public static void AuthAndUpload(String parsed_filename, String parsed_name_of_video, String parsed_name_of_author, String link_to_videohive, String parsed_tags, String parsed_ref_link, String parsed_description) throws Exception {

        System.out.println("Starting VideoToVimeo...");

        Vimeo vimeo = new Vimeo("29e5ba56e9be5d615ab20ae0c03b8ee3");

        System.out.println(parsed_filename);

        boolean upgradeTo1080 = false;
        videoEndPoint = vimeo.addVideo(new File("." + parsed_filename), upgradeTo1080);

        // VimeoResponse info = vimeo.getVideoInfo(videoEndPoint);
        // System.out.println(info);

        name = parsed_name_of_video;
        desc = parsed_ref_link + "\nAuthor: " + parsed_name_of_author + " " + link_to_videohive + "\n" + parsed_description + "\nTo see all project features go to this link:\n" + parsed_ref_link;
        // desc = "Some description";
        license = ""; //see Vimeo API Documentation
        privacyView = "nobody"; //see Vimeo API Documentation
        privacyEmbed = "whitelist"; //see Vimeo API Documentation
        reviewLink = false;

        // System.out.println("!!----------------- updateVideoMetadata ------------------!!");
        vimeo.updateVideoMetadata(videoEndPoint, name, desc, license, privacyView, privacyEmbed, reviewLink);

        System.out.println("End of uploading.");
    }

}

