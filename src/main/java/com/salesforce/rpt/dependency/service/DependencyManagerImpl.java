package com.salesforce.rpt.dependency.service;

import com.salesforce.rpt.dependency.model.Program;

import java.util.*;

public class DependencyManagerImpl implements DependencyManager {
    private Map<Program, Set<Program>> incomingDependencies;
    private Set<Program> installed;

    public DependencyManagerImpl() {
        incomingDependencies = new HashMap<>();
        installed = new HashSet<>();
    }

    public Set<Program> getPrograms() {
        return Collections.unmodifiableSet(incomingDependencies.keySet());
    }

    public Program getProgram(Program program) {
        return incomingDependencies.keySet().stream().filter(p -> p.equals(program)).findFirst().orElse(program);
    }

    @Override
    public Set<Program> list() throws ServiceException {
        return Collections.unmodifiableSet(installed);
    }

    public boolean isInstalled(Program program) {
        return installed.contains(program);
    }

    @Override
    public void depend(Program source, Set<Program> dest) throws ServiceException {
        checkProgram(source);
        for (Program program : dest) {
            checkProgram(program);
        }
        checkNewDependency(source);

        addToIncomingDependencyIfMissing(source);
        for (Program program : dest) {
            addToIncomingDependencyIfMissing(program);
            source.addDependency(program);
            incomingDependencies.get(program).add(source);
        }
    }

    protected void addToIncomingDependencyIfMissing(Program program) {
        if (!incomingDependencies.containsKey(program)) {
            incomingDependencies.put(program, new HashSet<>());
        }
    }

    protected void checkNewDependency(Program program) throws ServiceException {
        if (installed.contains(program)) {
            throw new ServiceException("Cannot add a new dependency to a program that is installed.");
        }
    }

    protected void checkProgram(Program Program) throws ServiceException {
        if (Program == null) {
            throw new ServiceException("Program cannot be null!");
        }
    }

    @Override
    public Set<Program> add(Program program) throws ServiceException {
        checkProgram(program);
        checkReinstall(program);
        Set<Program> installable = new HashSet<>();
        addRecursive(getProgram(program), installable);
        return installable;
    }

    protected void checkReinstall(Program program) throws ServiceException {
        if (isInstalled(program)) {
            throw new ServiceException(program.getName() + " is already installed.");
        }
    }

    protected void addRecursive(Program program, Set<Program> installable) throws ServiceException {
        installable.add(program);

        for (Program dependency : program.getDependencies()) {
            if (!installed.contains(dependency)) {
                if (installable.contains(dependency)) {
                    throw new ServiceException("Discovered loop in dependency when installing program.");
                }
                addRecursive(dependency, installable);
            }
        }

        installed.add(program);
    }

    @Override
    public Set<Program> remove(Program program) throws ServiceException {
        checkIsExisting(program);
        checkRemovable(program);
        checkIsInstalled(program);
        Set<Program> removable = new HashSet<>();
        removeRecursive(getProgram(program), removable);
        return removable;
    }

    protected void checkIsInstalled(Program program) throws ServiceException {
        if (!installed.contains(program)) {
            throw new ServiceException(program.getName() + " is not installed.");
        }
    }

    private void checkIsExisting(Program program) throws ServiceException {
        if (!incomingDependencies.keySet().contains(program)) {
            throw new ServiceException("Unknown Program: " + program.getName());
        }
    }

    protected void checkRemovable(Program program) throws ServiceException {
        Set<Program> incoming = new HashSet<>(incomingDependencies.get(program));
        incoming.retainAll(installed);
        if (!incoming.isEmpty()) {
            throw new ServiceException(program.getName() + " is still needed.");
        }
    }

    protected void removeRecursive(Program program, Set<Program> removable) {
        removable.add(program);
        installed.remove(program);

        for (Program dependency : program.getDependencies()) {
            Set<Program> incoming = new HashSet<>(incomingDependencies.get(dependency));
            incoming.removeAll(removable);
            incoming.retainAll(installed);
            if (incoming.isEmpty()) {
                removeRecursive(dependency, removable);
            }
        }
    }

}
