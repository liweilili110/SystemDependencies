package com.salesforce.rpt.dependency.service;

import com.salesforce.rpt.dependency.model.Program;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class DependencyManagerTest {
    private DependencyManagerImpl manager;

    private Program a;
    private Program b;
    private Program c;
    private Program d;

    @Before
    public void setUp() {
        a = new Program("A");
        b = new Program("B");
        c = new Program("C");
        d = new Program("D");

        manager = new DependencyManagerImpl();
    }

    @Test
    public void install() throws ServiceException {
        manager.depend(a, toSet(b));
        manager.depend(b, toSet(c));

        TestCase.assertEquals(3, manager.getPrograms().size());
        TestCase.assertEquals(0, manager.list().size());

        manager.add(a);

        TestCase.assertEquals(3, manager.list().size());
        assertTrue(manager.isInstalled(a));
        assertTrue(manager.isInstalled(b));
        assertTrue(manager.isInstalled(c));
    }

    @Test(expected = ServiceException.class)
    public void cannotInstall() throws ServiceException {
        manager.depend(a, toSet(b));

        TestCase.assertEquals(2, manager.getPrograms().size());
        TestCase.assertEquals(0, manager.list().size());

        manager.add(a);
        manager.depend(b, toSet(c));
    }

    @Test(expected = ServiceException.class)
    public void loop() throws ServiceException {
        manager.depend(a, toSet(b));
        manager.depend(b, toSet(c));
        manager.depend(c, toSet(d));
        manager.depend(d, toSet(b));

        TestCase.assertEquals(4, manager.getPrograms().size());
        TestCase.assertEquals(0, manager.list().size());

        manager.add(a);
    }

    @Test
    public void removeSubSet() throws ServiceException {
        manager.depend(a, toSet(b));
        manager.depend(b, toSet(c));

        TestCase.assertEquals(3, manager.getPrograms().size());
        TestCase.assertEquals(0, manager.list().size());

        manager.add(a);
        TestCase.assertEquals(3, manager.list().size());

        manager.remove(a);
        TestCase.assertEquals(0, manager.list().size());
    }

    @Test
    public void removeLinkList() throws ServiceException {
        manager.depend(a, toSet(b));
        manager.depend(b, toSet(c));
        manager.depend(b, toSet(d));
        manager.depend(d, toSet(c));

        TestCase.assertEquals(4, manager.getPrograms().size());
        TestCase.assertEquals(0, manager.list().size());

        manager.add(a);
        TestCase.assertEquals(4, manager.list().size());

        manager.remove(a);
        TestCase.assertEquals(0, manager.list().size());
    }

    @Test
    public void removeKeepNeededPrograms() throws ServiceException {
        manager.depend(a, toSet(b));
        manager.depend(a, toSet(c));
        manager.depend(d, toSet(b));

        TestCase.assertEquals(4, manager.getPrograms().size());
        TestCase.assertEquals(0, manager.list().size());

        manager.add(a);
        TestCase.assertEquals(3, manager.list().size());

        manager.add(d);
        TestCase.assertEquals(4, manager.list().size());

        manager.remove(a);
        TestCase.assertEquals(2, manager.list().size());
        assertTrue(manager.list().contains(b));
        assertTrue(manager.list().contains(d));
    }

    @Test(expected = ServiceException.class)
    public void notRemovable() throws ServiceException {
        manager.depend(a, toSet(b));
        manager.depend(b, toSet(c));

        TestCase.assertEquals(3, manager.getPrograms().size());
        TestCase.assertEquals(0, manager.list().size());

        manager.add(a);
        TestCase.assertEquals(3, manager.list().size());

        manager.remove(b);
    }

    private Set<Program> toSet(Program... programs) {
        Set<Program> set = new HashSet<>(programs.length);
        Collections.addAll(set, programs);
        return set;
    }
}
