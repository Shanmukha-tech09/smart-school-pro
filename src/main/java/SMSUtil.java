package com.school.util;

import java.net.HttpURLConnection;
import java.net.URL;

public class SMSUtil {
    public static void sendSMS(String parentMobile, String studentName) {
        try {
            System.out.println("SMS Sent to Parent (" + parentMobile + "): Your ward " + studentName + " entered school.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
