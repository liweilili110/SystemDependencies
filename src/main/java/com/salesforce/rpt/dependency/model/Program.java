package com.salesforce.rpt.dependency.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Program {
    private String name;
    private Set<Program> dependencies;

    public Program(String name) {
        this.name = name;
        dependencies = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public Set<Program> getDependencies() {
        return Collections.unmodifiableSet(dependencies);
    }

    public boolean addDependency(Program program) {
        return dependencies.add(program);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        Program program = (Program) other;

        return name.equals(program.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public static Set<Program> toProgram(String[] programs) {
        Set<Program> set = new HashSet<>(programs.length);
        for (String programName : programs) {
            set.add(new Program(programName));
        }
        return set;
    }
}
