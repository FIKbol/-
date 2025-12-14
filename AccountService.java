package auth;

import utils.FilePaths;
import java.io.*;
import java.util.*;

public class AccountService {
    private List<Account> accounts = new ArrayList<>();

    public AccountService() {
        loadAccounts();
    }

    public List<Account> getAccounts() { return accounts; }

    public void loadAccounts() {
        accounts.clear();
        File file = new File(FilePaths.ACCOUNTS_FILE);

        try {
            if (!file.exists()) {
                FileWriter fw = new FileWriter(file);
                fw.write("admin;admin;1\n");
                fw.close();
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                accounts.add(Account.fromString(line));
            }

            br.close();

        } catch (IOException e) {
            System.out.println(FilePaths.MSG_ERROR + e.getMessage());
        }
    }

    public void saveAccounts() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FilePaths.ACCOUNTS_FILE))) {
            for (Account a : accounts) {
                bw.write(a.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println(FilePaths.MSG_ERROR + e.getMessage());
        }
    }
    
    public void save() {
        saveAccounts();
    }

    public boolean isLoginUnique(String login) {
        return accounts.stream().noneMatch(a -> a.getLogin().equalsIgnoreCase(login));
    }

    public void addAccount(Account account) {
        accounts.add(account);
        saveAccounts();
    }

    public void deleteAccount(int index) {
        accounts.remove(index);
        saveAccounts();
    }
}
