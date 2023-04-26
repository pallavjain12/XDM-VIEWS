package org.example;

/*
    TODO:
        Current Implementation: For every view new connection is made.
        Appropriate Implementation: Create a table data structure for every source which stores connection request.
                                    Every View data structure reside inside that table
                                    For every view reuse the connection and close when finished.


 */

import org.example.Execute.ExecuteXML;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 0) {
            if (args[0].equals("1")) {
                ExecuteXML e = new ExecuteXML(args[1]);
                e.execute();
                e.displayAllViews();
            }
            else {
                System.out.println("JSON execution coming soon...");
            }
        }
        else {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Executable formats\n1.XML\n2.JSON\nPlease Enter file type : ");
            int n = Integer.parseInt(br.readLine());
            System.out.print("Enter path to file : ");
            String path = br.readLine();
            System.out.println("");
            if (n == 1) {
                ExecuteXML e = new ExecuteXML(path);
                e.execute();
                e.showAllViewName();
                System.out.println("");
                System.out.println("Enter view name to view its content\nEnter -1 to exit");
                String input = br.readLine();
                while(!input.equals("-1")) {
                    e.display(input);
                    System.out.println("Enter view name to view its content\nEnter -1 to exit");
                    input = br.readLine();
                }
            }
            else {
                System.out.println("JSON Execution coming soon.");
            }
        }
        System.out.println();
    }
}