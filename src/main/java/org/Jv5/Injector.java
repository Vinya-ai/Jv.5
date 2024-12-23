package org.Jv5;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Класс, осуществляющий внедрение зависимостей через аннотацию {@link AutoInjectable}.
 */
public class Injector {

    private Properties properties;

    /**
     * Конструктор, который загружает файл свойств по умолчанию.
     */
    public Injector() {
        this("config.properties");
    }

    /**
     * Конструктор, который загружает файл свойств по указанному пути.
     *
     * @param propertiesFile Путь к файлу свойств.
     */
    public Injector(String propertiesFile) {
        properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(propertiesFile)) {
            if (input == null) {
                System.out.println("Property file " + propertiesFile + " not found in the classpath.");
                return;
            }
            properties.load(input);
        } catch (IOException e) {
            System.out.println("Failed to load properties file: " + propertiesFile);
            e.printStackTrace();
        }
    }

    /**
     * Метод для внедрения зависимостей в объект, помеченный аннотацией {@link AutoInjectable}.
     *
     * @param obj Объект, в который будут внедрены зависимости.
     * @param <T> Тип объекта.
     * @return Объект с внедренными зависимостями.
     */
    public <T> T inject(T obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(AutoInjectable.class)) {
                Class<?> fieldType = field.getType();
                String interfaceName = fieldType.getCanonicalName();
                String implClassName = properties.getProperty(interfaceName);

                if (implClassName != null) {
                    try {
                        Class<?> implClass = Class.forName(implClassName);
                        Object implObject = implClass.getDeclaredConstructor().newInstance();
                        field.setAccessible(true);
                        field.set(obj, implObject);
                    } catch (Exception e) {
                        System.out.println("Failed to inject dependency for field: " + field.getName());
                    }
                } else {
                    System.out.println("No implementation found for interface: " + interfaceName);
                }
            }
        }
        return obj;
    }

    /**
     * Метод для печати информации о внедренных зависимостях.
     * Выводит, какие реализации были внедрены для каких интерфейсов.
     */
    public void printInjectionDetails() {
        for (Object entry : properties.entrySet()) {
            String key = (String) ((java.util.Map.Entry<?, ?>) entry).getKey();
            String value = (String) ((java.util.Map.Entry<?, ?>) entry).getValue();
            System.out.println("Injected " + value + " for interface: " + key);
        }
    }
}
