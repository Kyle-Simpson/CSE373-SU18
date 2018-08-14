package datastructures;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;

/**
 * This file should contain any tests that check and make sure your delete
 * method is efficient.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDeleteStress extends TestDoubleLinkedList {

    private static final int STRESS_CAP = 200000;
    private static final int STRESS_CAP2 = 2000000;
    
    private IList<Integer> makeStressList() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < STRESS_CAP; i++) {
            list.add(i);
        }
        return list;
    }
    
    private IList<Integer> makeStressList2() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < STRESS_CAP2; i++) {
            list.add(i);
        }
        return list;
    }

    @Test(timeout = 15 * SECOND)
    public void testDeleteFrontIsEfficient() {
        IList<Integer> list = makeStressList2();
        for (int i = 0; i < STRESS_CAP2; i++) {
            list.delete(0);
        }
        assertEquals(0, list.size());
    }

    @Test(timeout = 15 * SECOND)
    public void testDeleteBackIsEfficient() {
        IList<Integer> list = makeStressList2();
        for (int i = 0; i < STRESS_CAP2; i++) {
            list.delete(list.size() - 1);
        }
        assertEquals(0, list.size());
    }

    @Test(timeout = 15 * SECOND)
    public void testDeleteIncrementIndexIsEfficient() {
        IList<Integer> list = makeStressList();
        for (int i = 0; i < STRESS_CAP / 2; i++) {
            list.delete(i);
        }
        
        assertEquals(100000, list.size());
    }

    @Test(timeout = 15 * SECOND)
    public void testDeleteNearBackIsEfficient() {
        IList<Integer> list = makeStressList();
        for (int i = 0; i < STRESS_CAP / 2; i++) {
            list.delete(list.size() * 3 / 4);
        }
        assertEquals(100000, list.size());
    }

    @Test(timeout = 15 * SECOND)
    public void testDeleteNearFrontIsEfficient() {
        IList<Integer> list = makeStressList();
        for (int i = 0; i < STRESS_CAP / 2; i++) {
            list.delete(list.size() * 1 / 4);
        }
        assertEquals(100000, list.size());
    }
}
