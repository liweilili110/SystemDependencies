package com.salesforce.rpt.dependency;

import com.salesforce.rpt.dependency.command.*;
import com.salesforce.rpt.dependency.service.DependencyManager;
import com.salesforce.rpt.dependency.service.DependencyManagerImpl;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    public static final String DEPEND_COMMAND = "DEPEND";
    public static final String ADD_COMMAND = "ADD";
    public static final String REMOVE_COMMAND = "REMOVE";
    public static final String LIST_COMMAND = "LIST";

    Map<String, Command> commands;

    private CommandFactory() {
        commands = new HashMap<>();
    }

    public void addCommand(String name, Command command) {
        commands.put(name, command);
    }

    public String execute(String[] args) throws CommandException {
        Command command = commands.get(args[0]);
        if (command != null) {
            return command.execute(args);
        }
        return "Command not found!";
    }

    public static CommandFactory init() {
        CommandFactory cf = new CommandFactory();
        DependencyManager dependencyManager = new DependencyManagerImpl();

        DependCommand dependCommand = new DependCommand();
        dependCommand.setDependencyManager(dependencyManager);
        cf.addCommand(DEPEND_COMMAND, dependCommand);

        AddCommand addCommand = new AddCommand();
        addCommand.setDependencyManager(dependencyManager);
        cf.addCommand(ADD_COMMAND, addCommand);

        RemoveCommand removeCommand = new RemoveCommand();
        removeCommand.setDependencyManager(dependencyManager);
        cf.addCommand(REMOVE_COMMAND, removeCommand);

        ListCommand listCommand = new ListCommand();
        listCommand.setDependencyManager(dependencyManager);
        cf.addCommand(LIST_COMMAND, listCommand);

        return cf;
    }
}
