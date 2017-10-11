package com.intertecTest;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.util.*;
import com.intertecTest.IntertecTestStrings;

/**
 * Created by andreyrojas on 10/10/17.
 */
public class IntertecTestUtil {

    public List<Integer> randomNumbers = new ArrayList<Integer>();

    public List<String> getListFromFile(String fileName, String nodeName) {
        List<String> theList = new ArrayList<String>();
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream(fileName);
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject)parser.parse(new InputStreamReader(is, "UTF-8"));
            String content = (String)obj.get(nodeName);
            theList = new ArrayList<String>(Arrays.asList(content.split(",")));
            is.close();
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
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream(fileName);
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject)parser.parse(new InputStreamReader(is, "UTF-8"));
            JSONObject settings = (JSONObject)obj.get(IntertecTestStrings.SETTINGS_PARENT_NODE_NAME);
            userNamesLength = (Long)settings.get(IntertecTestStrings.SETTINGS_CHILD_NODE_NAME);
            is.close();
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
        Boolean userNameExists = false;
        Boolean hasRestrictedWord = false;
        Map result = new HashMap();
        List<String> userNameSuggestions = new ArrayList<String>();

        try {
            List<String> userList = getListFromFile(IntertecTestStrings.USER_LIST_FILE_NAME, IntertecTestStrings.USER_LIST_NODE_NAME);
            List<String> wordsList = getListFromFile(IntertecTestStrings.RESTRICTED_WORDS_FILE_NAME, IntertecTestStrings.RESTRICTED_WORDS_NODE_NAME);
            Long randomNamesLength = getSettingsFile(IntertecTestStrings.SETTINGS_FILE_NAME);
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
                        FileWriter file = new FileWriter(IntertecTestStrings.USER_LIST_FILE_NAME);
                        JSONObject obj = new JSONObject();
                        userList.add(theUserName);
                        obj.put(IntertecTestStrings.USER_LIST_NODE_NAME, String.join(",", userList));
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
