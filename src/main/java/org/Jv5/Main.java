package org.Jv5;

public class Main {
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

        // Вывод информации о внедрении зависимостей после основного вывода
        System.out.println("\nИнформация о внедрении зависимостей для AC:");
        injectorAC.printInjectionDetails();

        System.out.println("\nИнформация о внедрении зависимостей для BC:");
        injectorBC.printInjectionDetails();
    }
}

