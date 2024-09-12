package com.example.rutinkofffintech.TASK_3;

import java.util.List;
import java.util.NoSuchElementException;

public class CustomLinkedList<E> {
    private Node<E> head;
    private Node<E> tail;
    private int size;

    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
        }
    }

    public CustomLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void add(E element) {
        Node<E> newNode = new Node<>(element);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public E get(int index) {
        checkIndex(index);
        Node<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    public void remove(int index) {
        checkIndex(index);
        if (index == 0) {
            head = head.next;
            if (head == null) {
                tail = null;
            }
        } else {
            Node<E> previous = head;
            for (int i = 0; i < index - 1; i++) {
                previous = previous.next;
            }
            previous.next = previous.next.next;
            if (previous.next == null) {
                tail = previous;
            }
        }
        size--;
    }

    public boolean contains(E element) {
        Node<E> current = head;
        while (current != null) {
            if (current.data.equals(element)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public void addAll(List<E> elements) {
        for (E element : elements) {
            add(element);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<E> current = head;
        while (current != null) {
            sb.append(current.data).append(" -> ");
            current = current.next;
        }
        return sb.append("null").toString();
    }
}
