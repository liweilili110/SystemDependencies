package com.salesforce.rpt.dependency;

import com.salesforce.rpt.dependency.command.CommandException;

import java.io.File;
import java.util.Scanner;

public class Controller {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new Exception("Expected Syntax: Controller <input file>");
        }

        InputParser parser = new InputParser();
        CommandFactory cf = CommandFactory.init();
        Scanner scanner = new Scanner(new File(args[0]));

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] arguments = parser.parse(line);
            try {
                String response = cf.execute(arguments);
                if (response != null) {
                    System.out.print(response);
                }
            } catch (CommandException e) {
                System.out.println(e.getMessage());
            }
        }

        scanner.close();
    }

}
