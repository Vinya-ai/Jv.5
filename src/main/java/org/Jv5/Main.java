package org.Jv5;

/**
 * Главный класс для демонстрации работы с инжектором зависимостей.
 * Этот класс выполняет инжектирование зависимостей в объект {@link SomeBean} с использованием различных конфигурационных файлов.
 */
public class Main {

    /**
     * Основной метод программы.
     * В этом методе выполняется внедрение зависимостей для двух конфигурационных файлов:
     * {@code config_AC.properties} и {@code config_BC.properties}.
     * Результаты внедрения зависимостей выводятся в консоль.
     *
     * @param args Аргументы командной строки (не используются в данном примере).
     */
    public static void main(String[] args) {
        String configFileAC = "config_AC.properties";
        String configFileBC = "config_BC.properties";

        System.out.println("Результат для AC:");
        Injector injectorAC = new Injector(configFileAC);
        SomeBean someBeanAC = injectorAC.inject(new SomeBean());

        if (someBeanAC != null) {
            someBeanAC.foo();
        } else {
            System.out.println("Не удалось внедрить зависимости для AC.");
        }

        System.out.println("\nРезультат для BC:");
        Injector injectorBC = new Injector(configFileBC);
        SomeBean someBeanBC = injectorBC.inject(new SomeBean());

        if (someBeanBC != null) {
            someBeanBC.foo();
        } else {
            System.out.println("Не удалось внедрить зависимости для BC.");
        }

        System.out.println("\nИнформация о внедрении зависимостей для AC:");
        injectorAC.printInjectionDetails();

        System.out.println("\nИнформация о внедрении зависимостей для BC:");
        injectorBC.printInjectionDetails();
    }
}
