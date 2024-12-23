package org.Jv5;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс для тестирования внедрения зависимостей с использованием класса Injector.
 */
public class InjectorTest {

    /**
     * Тест для проверки внедрения зависимостей с использованием конфигурации "config_AC.properties".
     * Проверяется, что поля были инжектированы и метод foo() работает корректно.
     */
    @Test
    public void testInjectionForAC() throws NoSuchFieldException, IllegalAccessException {
        String configFileAC = "config_AC.properties";
        Injector injectorAC = new Injector(configFileAC);

        SomeBean someBeanAC = injectorAC.inject(new SomeBean());

        Field field1 = SomeBean.class.getDeclaredField("field1");
        field1.setAccessible(true);
        Field field2 = SomeBean.class.getDeclaredField("field2");
        field2.setAccessible(true);

        assertNotNull(field1.get(someBeanAC), "field1 should be injected");
        assertNotNull(field2.get(someBeanAC), "field2 should be injected");

        someBeanAC.foo();
    }

    /**
     * Тест для проверки внедрения зависимостей с использованием конфигурации "config_BC.properties".
     * Проверяется, что поля были инжектированы и метод foo() работает корректно.
     */
    @Test
    public void testInjectionForBC() throws NoSuchFieldException, IllegalAccessException {
        String configFileBC = "config_BC.properties";
        Injector injectorBC = new Injector(configFileBC);

        SomeBean someBeanBC = injectorBC.inject(new SomeBean());

        Field field1 = SomeBean.class.getDeclaredField("field1");
        field1.setAccessible(true);
        Field field2 = SomeBean.class.getDeclaredField("field2");
        field2.setAccessible(true);

        assertNotNull(field1.get(someBeanBC), "field1 should be injected");
        assertNotNull(field2.get(someBeanBC), "field2 should be injected");

        someBeanBC.foo();
    }

    /**
     * Тест для проверки ситуации, когда для интерфейса не найдена реализация в файле свойств.
     * Ожидается, что поле не будет инжектировано.
     */
    @Test
    public void testInjectionWithMissingImplementation() {
        String configFile = "config_missing.properties";
        Injector injector = new Injector(configFile);

        assertThrows(RuntimeException.class, () -> {
            injector.inject(new SomeBean());
        }, "Expected exception when property file is missing");
    }

    /**
     * Тест для проверки ситуации, когда внедряемая зависимость не может быть создана.
     * Например, если класс не существует или не имеет конструктора по умолчанию.
     */
    @Test
    public void testInjectionWithCreationFailure() {
        String configFile = "config_invalid.properties";
        Injector injector = new Injector(configFile);

        assertThrows(RuntimeException.class, () -> {
            injector.inject(new SomeBean());
        }, "Expected exception when dependency cannot be created");
    }


    /**
     * Тест для проверки, что происходит, если поле не аннотировано аннотацией @AutoInjectable.
     */
    @Test
    public void testInjectionWithNoAutoInjectable() throws NoSuchFieldException, IllegalAccessException {
        String configFile = "config_AC.properties";
        Injector injector = new Injector(configFile);

        SomeBeanWithoutInjection someBean = new SomeBeanWithoutInjection();
        SomeBeanWithoutInjection injectedBean = injector.inject(someBean);

        Field field = SomeBeanWithoutInjection.class.getDeclaredField("field");
        field.setAccessible(true);

        assertNull(field.get(injectedBean), "field should not be injected if not annotated with @AutoInjectable");
    }

    /**
     * Класс для тестирования, не аннотированный аннотацией @AutoInjectable.
     */
    public static class SomeBeanWithoutInjection {
        private String field;

        public String getField() {
            return field;
        }
    }
}
