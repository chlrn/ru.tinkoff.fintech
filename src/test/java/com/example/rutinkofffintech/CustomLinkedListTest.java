package com.example.rutinkofffintech;

import com.example.rutinkofffintech.TASK_3.CustomLinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class CustomLinkedListTest {

    private CustomLinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new CustomLinkedList<>();
    }

    @Test
    void testAdd() {
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    void testGet() {
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(3));
    }

    @Test
    void testRemove() {
        list.add(1);
        list.add(2);
        list.add(3);
        list.remove(1);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(2));
    }

    @Test
    void testContains() {
        list.add(1);
        list.add(2);
        list.add(3);
        assertTrue(list.contains(2));
        assertFalse(list.contains(4));
    }

    @Test
    void testAddAll() {
        list.addAll(Arrays.asList(1, 2, 3, 4));
        assertEquals(4, list.size());
        assertEquals(1, list.get(0));
        assertEquals(4, list.get(3));
    }

    @Test
    void testToString() {
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals("1 -> 2 -> 3 -> null", list.toString());
    }

    @Test
    void testStreamToCustomLinkedList() {
        java.util.stream.Stream<Integer> stream = java.util.stream.Stream.of(7, 8, 9);
        CustomLinkedList<Integer> listFromStream = stream.reduce(
                new CustomLinkedList<>(),
                (linkedList, element) -> {
                    linkedList.add(element);
                    return linkedList;
                },
                (left, right) -> {
                    throw new UnsupportedOperationException("Combining two lists is not supported");
                }
        );
        assertEquals(3, listFromStream.size());
        assertEquals(7, listFromStream.get(0));
        assertEquals(8, listFromStream.get(1));
        assertEquals(9, listFromStream.get(2));
    }
}
