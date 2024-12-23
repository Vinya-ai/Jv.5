package org.Jv5;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;  // <-- добавьте этот импорт
import java.util.Properties;

/**
 * Класс, осуществляющий внедрение зависимостей через аннотацию {@link AutoInjectable}.
 * Загружает конфигурацию из файла свойств и инжектирует зависимости в объекты.
 */
public class Injector {

    private Properties properties;

    /**
     * Конструктор, который загружает файл свойств по умолчанию ("config.properties").
     * Если файл не найден, выбрасывается исключение.
     */
    public Injector() {
        this("config.properties");
    }

    /**
     * Конструктор, который загружает файл свойств по указанному пути.
     *
     * @param propertiesFile Путь к файлу свойств.
     * @throws RuntimeException Если файл свойств не найден или не может быть загружен.
     */
    public Injector(String propertiesFile) {
        properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(propertiesFile)) {
            if (input == null) {
                System.err.println("Ошибка: Файл конфигурации " + propertiesFile + " не найден в classpath.");
                throw new RuntimeException("Не удалось найти файл конфигурации: " + propertiesFile);
            }
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Ошибка: Не удалось загрузить файл конфигурации " + propertiesFile);
            e.printStackTrace();
            throw new RuntimeException("Ошибка при загрузке файла конфигурации: " + propertiesFile, e);
        }
    }

    /**
     * Метод для внедрения зависимостей в объект, помеченный аннотацией {@link AutoInjectable}.
     * Для каждого поля с аннотацией {@link AutoInjectable} проверяется соответствующая реализация в файле свойств,
     * и зависимость инжектируется в объект.
     *
     * @param obj Объект, в который будут внедрены зависимости.
     * @param <T> Тип объекта.
     * @return Объект с внедренными зависимостями.
     * @throws RuntimeException Если не удается создать зависимость или найти подходящую реализацию.
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
                        throw new RuntimeException("Не удалось внедрить зависимость для поля: " + field.getName(), e);
                    }
                } else {
                    throw new RuntimeException("Не найдена реализация для интерфейса: " + interfaceName);
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
            System.out.println("Внедрена реализация " + value + " для интерфейса: " + key);
        }
    }
}
