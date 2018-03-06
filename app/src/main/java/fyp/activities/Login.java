package fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {


    private EditText email, password;
    private FirebaseAuth mAuth;
    private Button login, signUp;
    private LinearLayout emailBox, passwordBox;
    private TextView title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        email.setText("daniel.deloughry@mycit.ie");
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signUp);
        emailBox = findViewById(R.id.emailBox);
        passwordBox = findViewById(R.id.passwordBox);
        title = findViewById(R.id.title);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            login.setText(R.string.sign_out);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signOut();
                }
            });
            signUp.setVisibility(View.GONE);
            emailBox.setVisibility(View.GONE);
            passwordBox.setVisibility(View.GONE);
            title.setText((mAuth.getCurrentUser()).getEmail());
            title.setTextSize(26);
            title.setAllCaps(false);
            if (!mAuth.getCurrentUser().isEmailVerified()) {
                Toast.makeText(this, "Please verify email address.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void signOut() {
        mAuth.signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
//        updateUI(null);
    }

    private void sendEmailVerification(final String s) {
        if (mAuth.getCurrentUser() != null) {
            (mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Login.this, "Verification email sent to: " + s, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Login.this, "Verification email not sent.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean validateForm() {
        if (email.getText().toString().equals("")) {
            email.setError(getString(R.string.required));
            return false;
        } else {
            email.setError(null);
        }
        if (password.getText().toString().equals("")) {
            password.setError(getString(R.string.required));
            return false;
        } else {
            password.setError(null);
        }
        return true;
    }

    //   private void updateUI(FirebaseUser user) {
//        hideProgressDialog();
    //      if (user != null) {
//            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,                    user.getEmail(), user.isEmailVerified()));
//            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

//            findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
//            findViewById(R.id.email_password_fields).setVisibility(View.GONE);
//            findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);

    //       } else {
//            mStatusTextView.setText(R.string.signed_out);
//            mDetailTextView.setText(null);
//
//            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
//            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
//            findViewById(R.id.signed_in_buttonsuttons).setVisibility(View.GONE);
//        }
//    }


    public void signUp(View v) {
        if (!validateForm()) {
            return;
        }
//        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(String.valueOf(email.getText()), String.valueOf(password.getText()))
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendEmailVerification(String.valueOf(email.getText()));
                            signOut();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                            finish();
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);

                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.authentication_failed), Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
//                        hideProgressDialog();
                    }
                });
    }

    public void login(View v) {
        if (!validateForm()) {
            return;
        }
//        showProgressDialog();
        mAuth.signInWithEmailAndPassword(String.valueOf(email.getText()), String.valueOf(password.getText()))
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                            Intent refresh = new Intent(getApplicationContext(), Login.class);
                            startActivity(refresh);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.authentication_failed), Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
//                        hideProgressDialog();
                    }
                });
    }


    public void backToMenu(View view) {
        onBackPressed();
    }

    public void clearEmail(View view) {
        email.setText("");
    }

    public void clearPassword(View view) {
        password.setText("");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }
}