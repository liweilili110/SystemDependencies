package com.salesforce.rpt.dependency;

import com.salesforce.rpt.dependency.command.CommandException;

public class InputParser {

    public String[] parse(String commandString) throws CommandException {
        return commandString.split("\\s+");
    }

}
