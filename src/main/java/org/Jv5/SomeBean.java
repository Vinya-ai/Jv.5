package org.Jv5;

/**
 * Класс SomeBean с двумя полями, аннотированными {@link AutoInjectable},
 * которые будут инициализированы классом {@link Injector}.
 */
public class SomeBean {

    @AutoInjectable
    private SomeInterface field1;

    @AutoInjectable
    private SomeOtherInterface field2;

    /**
     * Метод для вызова методов doSomething и doSomeOther из внедренных объектов.
     */
    public void foo() {
        field1.doSomething();
        field2.doSomeOther();
    }
}