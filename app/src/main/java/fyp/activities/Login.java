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
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LinearLayout emailBox = findViewById(R.id.emailBox);
        LinearLayout passwordBox = findViewById(R.id.passwordBox);
        TextView loginTextview = findViewById(R.id.loginTextview);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        Button signOut = findViewById(R.id.signOut);
        Button signUp = findViewById(R.id.signUp);
        Button login = findViewById(R.id.login);
        TextView viewEmail = findViewById(R.id.viewEmail);
        if (mAuth.getCurrentUser() != null) {
            signOut.setVisibility(View.VISIBLE);
            signUp.setVisibility(View.GONE);
            login.setVisibility(View.GONE);
            emailBox.setVisibility(View.GONE);
            passwordBox.setVisibility(View.GONE);
            loginTextview.setText(mAuth.getCurrentUser().getEmail());
            loginTextview.setAllCaps(false);
            loginTextview.setTextSize(24);
        } else {
            signUp.setVisibility(View.VISIBLE);
            login.setVisibility(View.VISIBLE);
            signOut.setVisibility(View.GONE);
            emailBox.setVisibility(View.VISIBLE);
            passwordBox.setVisibility(View.VISIBLE);
            loginTextview.setText(getString(R.string.login));
        }
    }

    public void signUp(View view) {
        if (!String.valueOf(email.getText()).equals("") && !String.valueOf(password.getText()).equals("")) {
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    Toast.makeText(Login.this, (user).getEmail() + " signed up!", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(getIntent());
                                }
                            } else {
                                Toast.makeText(Login.this, "Sign-up failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void login(View view) {
        if (!String.valueOf(email.getText()).equals("") && !String.valueOf(password.getText()).equals("")) {
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    Toast.makeText(Login.this, "Welcome back " + (user).getEmail() + "!", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(getIntent());
                                }
                            } else {
                                Toast.makeText(Login.this, "Login failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void clearEmail(View view) {
        email.setText("");
    }

    public void clearPassword(View view) {
        password.setText("");
    }

    public void signOut(View view) {
        mAuth.signOut();
        finish();
        startActivity(getIntent());
    }

    public void backToMenu(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Main.class));
    }
}
