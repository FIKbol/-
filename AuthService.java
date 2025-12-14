package auth;

import java.util.Scanner;

public class AuthService {

    public Account authorize(AccountService accService) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Логин: ");
        String login = sc.nextLine();
        System.out.print("Пароль: ");
        String pass = sc.nextLine();

        for (Account a : accService.getAccounts()) {
            if (a.getLogin().equals(login) && a.getPassword().equals(pass)) {
                return a;
            }
        }
        return null;
    }
}
