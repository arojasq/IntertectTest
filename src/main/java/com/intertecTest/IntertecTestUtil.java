package com.intertecTest;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by andreyrojas on 10/10/17.
 */
public class IntertecTestUtil {

    public List<Integer> randomNumbers = new ArrayList<Integer>();

    public List<String> getListFromFile(String fileName, String nodeName) {
        List<String> theList = new ArrayList<String>();
        try {
            File theFile = new File(fileName);
            JSONParser parser = new JSONParser();
            Object jsonFile = parser.parse(new FileReader(theFile));
            JSONObject obj = (JSONObject)jsonFile;
            String users = (String)obj.get(nodeName);
            theList = new ArrayList<String>(Arrays.asList(users.split(",")));
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
        return theList;
    }

    private Long getSettingsFile(String fileName) {
        Long userNamesLength = 0L;
        try {
            File theFile = new File(fileName);
            JSONParser parser = new JSONParser();
            Object jsonFile = parser.parse(new FileReader(theFile));
            JSONObject obj = (JSONObject)jsonFile;
            JSONObject settings = (JSONObject)obj.get("settings");
            userNamesLength = (Long)settings.get("randomUserNames");
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
        return userNamesLength;
    }

    public Map checkUserName(String theUserName) {
        String path = "./src/main/resources/";
        Boolean userNameExists = false;
        Boolean hasRestrictedWord = false;
        Map result = new HashMap();
        List<String> userNameSuggestions = new ArrayList<String>();

        try {
            List<String> userList = getListFromFile(path + "userList.json", "users");
            List<String> wordsList = getListFromFile(path + "restrictedWords.json", "words");
            Long randomNamesLength = getSettingsFile(path + "settings.json");
            for (Integer j = 0; j < wordsList.size(); j++) {
                if (theUserName.toLowerCase().contains(wordsList.get(j).toLowerCase())) {
                    System.out.println("The username you are tyring to create contains one or more restricted words.");
                    hasRestrictedWord = true;
                    break;
                }
            }

            if (!hasRestrictedWord) {
                for (Integer i = 0; i < userList.size(); i++) {
                    if (theUserName.toLowerCase().equals(userList.get(i).toLowerCase())) {
                        userNameExists = true;
                        break;
                    }
                }

                if (userNameExists)
                    userNameSuggestions = getRandomUserNames(theUserName,randomNamesLength);
                else {
                    try {
                        FileWriter file = new FileWriter(path + "userList.json");
                        JSONObject obj = new JSONObject();
                        userList.add(theUserName);
                        obj.put("users", String.join(",", userList));
                        file.write(obj.toJSONString());
                        file.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                result.put(!userNameExists, userNameSuggestions);
            }
            else
                result.put(null, null);
        }
        catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private List<String> getRandomUserNames(String theUserName, Long randomNamesLength) {
        Random r = new Random();
        String rndName = "";
        List<String> names = new ArrayList<String>();
        Integer t = 0;
        while (t < randomNamesLength) {
            Integer nextRand = r.nextInt(9999);
            if (!randomNumbers.contains(nextRand)) {
                if (nextRand % 2 == 0)
                    rndName = theUserName + "_@" + nextRand;
                else if (nextRand % 3 == 0)
                    rndName = theUserName + "XyZ" + nextRand;
                else if (nextRand % 5 == 0)
                    rndName = "!" + theUserName + "_" + nextRand;
                else
                    rndName = theUserName + nextRand + "?";

                names.add(rndName);
                randomNumbers.add(nextRand);
                t++;
            }
        }

        return names;
    }
}
