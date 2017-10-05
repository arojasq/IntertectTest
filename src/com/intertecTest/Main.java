package com.intertecTest;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter String");
        try {
            String s = br.readLine();
            System.out.print("Enter Integer:");
            int i = Integer.parseInt(br.readLine());
            System.out.print("Integer is: " + i);
        }
        catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
        }
        catch(Exception e){
            System.err.println("ERROR!");
        }
    }
}
