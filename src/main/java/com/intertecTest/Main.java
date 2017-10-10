package com.intertecTest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        IntertecTestUtil util = new IntertecTestUtil();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Type in your username: ");
            String userName = br.readLine().toLowerCase();
            Map result = util.checkUserName(userName);
            Boolean success = (Boolean)result.keySet().toArray()[0];
            List<String> suggestions = (List)result.get(success);
            if (success != null) {
                if (success)
                    System.out.println("Your username has been created!");
                else {
                    System.out.println("The username you typed in is already in use. I suggest using one of the following:");
                    for (String s : suggestions) {
                        System.out.println(s.toString());
                    }
                }
            }
            System.out.print("Do you want to try again? Y/N ");
            String input = br.readLine();
            if(input.toLowerCase().equals("y"))
                restart(args);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void restart(String[] strArr) {
        main(strArr);
    }
}
