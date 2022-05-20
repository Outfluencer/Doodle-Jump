package xyz.doodlejump.mysql;

public class Communication {


    public static boolean tryLogin(String username, String password) {
        if(username.isEmpty() || password.isEmpty()) return false;
        return true;
    }


    public static boolean tryRegister(String username, String password) {
        if(username.isEmpty() || password.isEmpty()) return false;
        return true;
    }

}
