package com.salesforce.rpt.dependency;

import com.salesforce.rpt.dependency.command.CommandException;

public interface Command {
     String execute(String[] args) throws CommandException;
}
