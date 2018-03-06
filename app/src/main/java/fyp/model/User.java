package fyp.model;

import com.google.firebase.auth.FirebaseAuth;

public class User {
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static String getLoggedEmail() {
        if ((mAuth.getCurrentUser()) != null) {
            return (mAuth.getCurrentUser()).getEmail();
        }
        return null;
    }

    public static boolean isLogged() {
        return mAuth.getCurrentUser() != null;
    }

    public static boolean emailVerified() {
        return (mAuth.getCurrentUser()) != null && mAuth.getCurrentUser().isEmailVerified();
    }
}
