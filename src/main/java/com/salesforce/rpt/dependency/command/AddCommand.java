package com.salesforce.rpt.dependency.command;

import com.salesforce.rpt.dependency.model.Program;
import com.salesforce.rpt.dependency.service.ServiceException;

import java.util.Set;

public class AddCommand extends AbstractCommand {
    @Override
    public String executeInternal(String[] args) throws CommandException, ServiceException {
        verifyHasExactNumberOfArguments(args, 2);

        Set<Program> installed = dependencyManager.add(new Program(args[1]));

        StringBuffer response = new StringBuffer();
        for (Program program : installed) {
            response.append("\tInstalling ").append(program.getName()).append("\n");
        }

        return response.toString();
    }
}
