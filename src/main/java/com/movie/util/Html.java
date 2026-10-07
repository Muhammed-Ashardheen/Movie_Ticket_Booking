package com.movie.util;

public class Html {
    public static String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    public static String page(String title, String body) {
        return "<!DOCTYPE html><html><head><meta charset='UTF-8'><title>" + esc(title) + "</title>"
             + "<link rel='stylesheet' href='style.css'></head><body>"
             + "<nav><a href='index.html'>Home</a> <a href='movies'>Movies</a> <a href='bookings'>My Bookings</a></nav>"
             + "<div class='box'><h2>" + esc(title) + "</h2>" + body + "</div></body></html>";
    }
}
