package fyp.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseController {

    public static void reserveSpace(ParkReservation reservation, String emailAddress) {
        DatabaseReference reservationDb = (FirebaseDatabase.getInstance()).getReference("reservation").child(emailAddress);
        reservationDb.setValue(reservation);
    }

    public static FirebaseUser getLoggedUser() {
        return (FirebaseAuth.getInstance()).getCurrentUser();
    }
}
