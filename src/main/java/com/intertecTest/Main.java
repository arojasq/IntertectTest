package com.intertecTest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.File;
import org.json.simple.parser.ParseException;

public class Main {

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String path = "./src/main/resources/";
        try {
            JSONArray userList = getUsersList(path + "userList.json");
            String[] wordsList = getRestrictedWords(path + "restrictedWords.json");
            for (Integer i = 0; i < userList.size(); i++) {
                String userName = ((JSONObject) userList.get(i)).get("username").toString();
                String passWord = ((JSONObject) userList.get(i)).get("password").toString();
                System.out.println(userName);
                System.out.println(passWord);
            }
            for (Integer j = 0; j < wordsList.length; j++) {
                System.out.println(wordsList[j]);
            }
        }
        catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String[] getRestrictedWords(String fileName) {
        String[] wordsList = new String[] {};
        try {
            File theFile = new File(fileName);
            JSONParser parser = new JSONParser();
            Object restrictedWords = parser.parse(new FileReader(theFile));
            JSONObject wordsObj = (JSONObject)restrictedWords;
            String words = (String)wordsObj.get("words");
            wordsList = words.split(",");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return wordsList;
    }
    private static JSONArray getUsersList(String fileName) {
        JSONArray userList = new JSONArray();
        try {
            File theFile = new File(fileName);
            JSONParser parser = new JSONParser();
            Object usersFile = parser.parse(new FileReader(theFile.getAbsolutePath()));
            JSONObject usersObj = (JSONObject)usersFile;
            userList = (JSONArray)usersObj.get("users");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return userList;
    }
}
