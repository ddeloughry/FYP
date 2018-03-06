package fyp.model;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static fyp.model.User.getLoggedEmail;

public class MyDao {
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static void putReservation(String email, ParkReservation reservation) {
        DatabaseReference reservationDb = (FirebaseDatabase.getInstance()).getReference("reservation").child(email.replace(".", "_"));
        reservationDb.setValue(reservation);
    }


    public static DatabaseReference getReservationDb() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (getLoggedEmail() != null) {
            if (getLoggedEmail().contains(".")) {
                return database.getReference("reservation").child(getLoggedEmail().replace(".", "_"));
            } else {
                return database.getReference("reservation").child(getLoggedEmail());
            }
        } else {
            return null;
        }
    }
}
