package org.Jv5;

/**
 * Реализация интерфейса SomeOtherInterface, которая выводит "C".
 */
public class SODoer implements SomeOtherInterface {
    @Override
    public void doSomeOther() {
        System.out.println("C");
    }
}