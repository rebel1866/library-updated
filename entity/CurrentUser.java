package by.epamtc.stanislavmelnikov.entity;

public class CurrentUser {
    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        CurrentUser.currentUser = currentUser;
    }

    private static User currentUser;
}
