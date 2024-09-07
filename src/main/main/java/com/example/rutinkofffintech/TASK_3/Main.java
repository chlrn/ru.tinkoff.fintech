package com.example.rutinkofffintech.TASK_3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();

        // Добавляем элементы
        list.add(1);
        list.add(2);
        list.add(3);

        // Получаем элемент
        System.out.println("Element at index 0: " + list.get(0));

        // Удаляем элемент
        list.remove(1);
        System.out.println("List after removing element at index 1: " + list);

        // Проверяем, содержится ли элемент
        System.out.println("List contains 2: " + list.contains(2));
        System.out.println("List contains 3: " + list.contains(3));

        // Добавляем несколько элементов
        list.addAll(Arrays.asList(4, 5, 6));
        System.out.println("List after adding elements 4, 5, 6: " + list);

        // Преобразуем Stream в CustomLinkedList
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
        System.out.println("List created from stream: " + listFromStream);
    }
}
