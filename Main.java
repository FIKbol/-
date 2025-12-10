package Main;

import auth.*;
import data.*;
import utils.InputUtils;

public class Main {

    public static void main(String[] args) {

        AccountService accService = new AccountService();
        ProductionService prodService = new ProductionService();
        AuthService auth = new AuthService();

        while (true) {
            System.out.println("\n--- Авторизация ---");
            Account current = auth.authorize(accService);

            if (current == null) {
                System.out.println("Неверный логин или пароль!");
                continue;
            }

            if (current.getRole() == 1) adminMenu(accService, prodService);
            else userMenu(prodService);
        }
    }

    // Меню администратора
    public static void adminMenu(AccountService accService, ProductionService ps) {
        while (true) {
            System.out.println("\n== Меню администратора ==");
            System.out.println("1. Просмотр всех записей");
            System.out.println("2. Добавить запись");
            System.out.println("3. Удалить запись");
            System.out.println("4. Редактировать запись");
            System.out.println("5. Управление пользователями");
            System.out.println("0. Назад");

            int n = InputUtils.nextInt();
            switch (n) {

                case 1 -> printRecords(ps);

                case 2 -> addRecord(ps);

                case 3 -> deleteRecord(ps);

                case 4 -> editRecord(ps);

                case 5 -> userManagement(accService);

                case 0 -> { return; }

                default -> System.out.println("Ошибка!");
            }
        }
    }

    // Меню пользователя
    public static void userMenu(ProductionService ps) {
        while (true) {
            System.out.println("\n== Меню пользователя ==");
            System.out.println("1. Просмотр всех записей");
            System.out.println("2. Поиск");
            System.out.println("3. Сортировка");
            System.out.println("4. Выпуск по цеху за период");
            System.out.println("0. Назад");

            int n = InputUtils.nextInt();

            switch (n) {

                case 1 -> printRecords(ps);

                case 2 -> searchMenu(ps);

                case 3 -> sortMenu(ps);

                case 4 -> productionByWorkshopAndPeriod(ps);

                case 0 -> { return; }

                default -> System.out.println("Ошибка!");
            }
        }
    }

    // Вывод списка
    public static void printRecords(ProductionService ps) {
        if (ps.getRecords().isEmpty()) {
            System.out.println("Нет записей.");
            return;
        }

        for (int i = 0; i < ps.getRecords().size(); i++) {
            System.out.println((i + 1) + ". " + ps.getRecords().get(i));
        }
    }

    // Добавления новой записи
    public static void addRecord(ProductionService ps) {

        // дата 
        String d;
        while (true) {
            System.out.print("Дата (YYYY-MM-DD): ");
            d = InputUtils.nextLine();

            if (d.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")) break;

            System.out.println("Ошибка формата даты!");
        }

        // цех
        int w;
        while (true) {
            System.out.print("Цех (только цифры): ");
            String input = InputUtils.nextLine();

            if (input.matches("\\d+")) {
                w = Integer.parseInt(input);
                if (w > 0) break;
            }
            System.out.println("Ошибка: неверный номер цеха!");
        }

        // наименование
        String n;
        while (true) {
            System.out.print("Наименование продукции: ");
            n = InputUtils.nextLine();

            if (n.matches("[A-Za-zА-Яа-яЁё\\-\\s]+") && n.trim().length() >= 2) break;

            System.out.println("Ошибка: только буквы, пробелы и дефисы!");
        }

        // количество
        int q;
        while (true) {
            System.out.print("Количество (только цифры): ");
            String input = InputUtils.nextLine();

            if (input.matches("\\d+")) {
                q = Integer.parseInt(input);
                if (q > 0) break;
            }

            System.out.println("Ошибка: количество должно быть > 0!");
        }

        // ФИО
        String r;
        while (true) {
            System.out.print("ФИО ответственного: ");
            r = InputUtils.nextLine();

            if (r.matches("[A-Za-zА-Яа-яЁё\\-\\s]+") && r.trim().length() >= 3) break;

            System.out.println("Ошибка: только буквы, пробелы и дефисы!");
        }

        ps.addRecord(new ProductionRecord(d, w, n, q, r));
        System.out.println("Запись добавлена.");
    }

    // Удаление
    public static void deleteRecord(ProductionService ps) {

        printRecords(ps);

        System.out.print("Введите номер записи (1...N): ");
        int index = InputUtils.nextInt() - 1;

        if (index < 0 || index >= ps.getRecords().size()) {
            System.out.println("Ошибка: неверный номер!");
            return;
        }

        ps.deleteRecord(index);
        System.out.println("Запись удалена.");
    }

    // Редактирование
    public static void editRecord(ProductionService ps) {

        printRecords(ps);

        System.out.print("Введите номер записи (1...N): ");
        int index = InputUtils.nextInt() - 1;

        if (index < 0 || index >= ps.getRecords().size()) {
            System.out.println("Ошибка: неверный номер!");
            return;
        }

        ProductionRecord old = ps.getRecords().get(index);

        System.out.println("Редактирование записи №" + (index + 1));
        System.out.println("Старая запись: " + old);

        // дата
        String date;
        while (true) {
            System.out.print("Новая дата (или Enter): ");
            date = InputUtils.nextLine();
            if (date.isEmpty()) { date = old.getDate(); break; }
            if (date.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")) break;
            System.out.println("Ошибка!");
        }

        // цех
        int workshop;
        while (true) {
            System.out.print("Новый цех (или Enter): ");
            String w = InputUtils.nextLine();
            if (w.isEmpty()) { workshop = old.getWorkshop(); break; }
            if (w.matches("\\d+")) {
                workshop = Integer.parseInt(w);
                if (workshop > 0) break;
            }
            System.out.println("Ошибка!");
        }

        // наименование
        String name;
        while (true) {
            System.out.print("Новое наименование (или Enter): ");
            name = InputUtils.nextLine();
            if (name.isEmpty()) { name = old.getProductName(); break; }
            if (name.matches("[A-Za-zА-Яа-яЁё\\-\\s]+")) break;
            System.out.println("Ошибка!");
        }

        // количество
        int qty;
        while (true) {
            System.out.print("Новое количество (или Enter): ");
            String q = InputUtils.nextLine();
            if (q.isEmpty()) { qty = old.getQuantity(); break; }
            if (q.matches("\\d+")) {
                qty = Integer.parseInt(q);
                if (qty > 0) break;
            }
            System.out.println("Ошибка!");
        }

        // ответственный
        String resp;
        while (true) {
            System.out.print("Новый ответственный (или Enter): ");
            resp = InputUtils.nextLine();
            if (resp.isEmpty()) { resp = old.getResponsible(); break; }
            if (resp.matches("[A-Za-zА-Яа-яЁё\\-\\s]+") && resp.trim().length() >= 3) break;
            System.out.println("Ошибка!");
        }

        ps.getRecords().set(index,
                new ProductionRecord(date, workshop, name, qty, resp));

        ps.save();
        System.out.println("Запись обновлена.");
    }

    // выпуск по цеху за период
    public static void productionByWorkshopAndPeriod(ProductionService ps) {

        int w;
        while (true) {
            System.out.print("Введите номер цеха: ");
            String input = InputUtils.nextLine();

            if (input.matches("\\d+")) {
                w = Integer.parseInt(input);
                if (w > 0) break;
            }
            System.out.println("Ошибка!");
        }

        String df;
        while (true) {
            System.out.print("Дата от (YYYY-MM-DD): ");
            df = InputUtils.nextLine();
            if (df.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")) break;
            System.out.println("Ошибка!");
        }

        String dt;
        while (true) {
            System.out.print("Дата до (YYYY-MM-DD): ");
            dt = InputUtils.nextLine();
            if (dt.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")) break;
            System.out.println("Ошибка!");
        }

        ps.showProductionByPeriod(w, df, dt);
    }

    // Управление пользователями
    public static void userManagement(AccountService accService) {
        while (true) {
            System.out.println("\n== Управление пользователями ==");
            System.out.println("1. Просмотр пользователей");
            System.out.println("2. Добавить пользователя");
            System.out.println("3. Удалить пользователя");
            System.out.println("0. Назад");

            int n = InputUtils.nextInt();

            switch (n) {

                case 1 -> accService.getAccounts().forEach(a ->
                        System.out.println(a.getLogin() + " | role=" + a.getRole()));

                case 2 -> {
                    System.out.print("Логин: ");
                    String login = InputUtils.nextLine();

                    if (!accService.isLoginUnique(login)) {
                        System.out.println("Ошибка: логин уже существует!");
                        break;
                    }

                    System.out.print("Пароль: ");
                    String pass = InputUtils.nextLine();

                    System.out.print("Роль (0=user, 1=admin): ");
                    int role = InputUtils.nextInt();

                    accService.addAccount(new Account(login, pass, role));
                }

                case 3 -> {
                    System.out.print("Введите номер пользователя (1...N): ");
                    int idx = InputUtils.nextInt() - 1;

                    if (idx < 0 || idx >= accService.getAccounts().size()) {
                        System.out.println("Ошибка: неверный номер!");
                        break;
                    }

                    accService.deleteAccount(idx);
                }

                case 0 -> { return; }

                default -> System.out.println("Ошибка!");
            }
        }
    }

    // Поиск и сортировака
    public static void searchMenu(ProductionService ps) {
        System.out.println("\n== Поиск ==");
        System.out.println("1. По наименованию");
        System.out.println("2. По дате");
        System.out.println("3. По цеху");
        System.out.println("0. Назад");

        int n = InputUtils.nextInt();

        switch (n) {

            case 1 -> {
                System.out.print("Введите наименование: ");
                String name = InputUtils.nextLine();
                ps.getRecords().stream()
                        .filter(r -> r.getProductName().equalsIgnoreCase(name))
                        .forEach(System.out::println);
            }

            case 2 -> {
                System.out.print("Введите дату (YYYY-MM-DD): ");
                String d = InputUtils.nextLine();
                ps.getRecords().stream()
                        .filter(r -> r.getDate().equals(d))
                        .forEach(System.out::println);
            }

            case 3 -> {
                System.out.print("Введите номер цеха: ");
                int w = InputUtils.nextInt();
                ps.getRecords().stream()
                        .filter(r -> r.getWorkshop() == w)
                        .forEach(System.out::println);
            }

            case 0 -> { return; }

            default -> System.out.println("Ошибка!");
        }
    }

    public static void sortMenu(ProductionService ps) {
        System.out.println("\n== Сортировка ==");
        System.out.println("1. По дате");
        System.out.println("2. По наименованию");
        System.out.println("3. По цеху");
        System.out.println("0. Назад");

        int n = InputUtils.nextInt();

        switch (n) {

            case 1 -> ps.getRecords().stream()
                    .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                    .forEach(System.out::println);

            case 2 -> ps.getRecords().stream()
                    .sorted((a, b) -> a.getProductName().compareToIgnoreCase(b.getProductName()))
                    .forEach(System.out::println);

            case 3 -> ps.getRecords().stream()
                    .sorted((a, b) -> Integer.compare(a.getWorkshop(), b.getWorkshop()))
                    .forEach(System.out::println);

            case 0 -> { return; }

            default -> System.out.println("Ошибка!");
        }
    }
}
