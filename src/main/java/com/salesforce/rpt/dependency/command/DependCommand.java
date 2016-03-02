package com.salesforce.rpt.dependency.command;

import com.salesforce.rpt.dependency.model.Program;
import com.salesforce.rpt.dependency.service.ServiceException;

import java.util.Arrays;

public class DependCommand extends AbstractCommand {
    @Override
    public String executeInternal(String[] args) throws CommandException, ServiceException {
        verifyHasMinimumNumberOfArguments(args, 3);

        dependencyManager.depend(new Program(args[1]),
                Program.toProgram(Arrays.copyOfRange(args, 2, args.length)));

        return null;
    }
}
