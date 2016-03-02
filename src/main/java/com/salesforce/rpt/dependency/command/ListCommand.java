package com.salesforce.rpt.dependency.command;

import com.salesforce.rpt.dependency.model.Program;
import com.salesforce.rpt.dependency.service.ServiceException;

import java.util.Set;

public class ListCommand extends AbstractCommand {
    @Override
    public String executeInternal(String[] args) throws CommandException, ServiceException {
        verifyHasExactNumberOfArguments(args, 1);

        Set<Program> installed = dependencyManager.list();

        StringBuffer response = new StringBuffer();
        for (Program program : installed) {
            response.append("\t").append(program.getName()).append("\n");
        }

        return response.toString();
    }
}
