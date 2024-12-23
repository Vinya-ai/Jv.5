package org.Jv5;

/**
 * Реализация интерфейса SomeInterface, которая выводит "B".
 */
public class OtherImpl implements SomeInterface {
    @Override
    public void doSomething() {
        System.out.println("B");
    }
}
