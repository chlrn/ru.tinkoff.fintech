package com.example.rutinkofffintech.task_11_1;

public class Main {
    public static void main(String[] args) {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();

        // Добавляем элементы
        list.add(1);
        list.add(2);
        list.add(3);

        // Выводим элементы с помощью итератора
        CustomIterator<Integer> iterator = list.iterator();
        System.out.println("List elements using iterator:");
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }

        // Используем forEachRemaining
        System.out.println("\nList elements using forEachRemaining:");
        iterator = list.iterator();
        iterator.forEachRemaining(System.out::println);
    }
}
