package com.example.oAuth2Login;

public class Test {

    public static void main(String[] args) {
        String user = "\"ashish\"";
        String u = user.replace("\\", "").replace("\"", "");
        System.out.println(u);
        System.out.println("ashish");
    }
}
