package com.moondu.leilao.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.moondu.leilao.R;
import com.moondu.leilao.commons.utils.AESUtils;
import com.moondu.leilao.model.entity.User;
import com.moondu.leilao.model.firebase.FirebaseHelper;

import java.util.Objects;

import dmax.dialog.SpotsDialog;

import static com.moondu.leilao.model.firebase.FirebaseHelper.getAuth;
import static com.moondu.leilao.model.firebase.FirebaseHelper.getFirebase;

public class UserAuth extends AppCompatActivity {

    private Button btnSignIn;

    private AlertDialog customProgressDialog;
    private EditText nmUsuario;
    private EditText password;

    private View viewRoot;
    private Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_auth);

        btnSignIn = findViewById(R.id.loginBtn);
        nmUsuario = findViewById(R.id.userName);
        password = findViewById(R.id.password);

        viewRoot = findViewById(android.R.id.content);

        customProgressDialog = new SpotsDialog(context, R.style.CustomProgressDialog);
        customProgressDialog.setCancelable(true);

        User item = getUser(context);

        if (item != null && item.mGetSavePassword() && !item.isUnlogged()) {
            if (item.isLoginInCache() ||
                    (getAuth().getCurrentUser() != null && FirebaseHelper.getCurrentUID(context).equals(item.getKey()))) {
                callMainActivity();
            }
        }

        /**
         * Keyboard Done
         */
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (validateForm()) {
                        signIn(nmUsuario.getText().toString(), password.getText().toString());
                        return true;
                    }
                }
                return false;
            }
        });

        /**
         * Buttom Login
         */
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!verifyIsConnected()) {
                    if (validateInCache()){
                        User item  = getUser(context);

                        item.setUnlogged(false);
                        item.setLoginInCache(true);

                        setUser(item, context);

                        callMainActivity();
                    }else{
                        showMessage(getString(R.string.no_internet));
                    }
                } else {
                    if (validateForm()) {
                        hideKeyboard(v);
                        signIn(nmUsuario.getText().toString(), password.getText().toString());
                    }
                }
            }
        });

    }

    private boolean validateInCache() {
        User item = getUser(this);
        String cachePassword = getLasPassword(this);

        String uCache = item.getMail();
        String uNovo = nmUsuario.getText().toString();

        String pCache = cachePassword;
        String pNovo = encrypt(password.getText().toString());

        String uuid = FirebaseHelper.getCurrentUID(context);

        return uCache.equals(uNovo) && pCache.equals(pNovo) &&
                uuid.equals(item.getKey());
    }

    private void signIn(String email, String password) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString("#lastPassword#", encrypt(password));
        prefsEditor.commit();

        customProgressDialog.show();
        hideKeyboard(viewRoot);

        getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUser();
                        } else {
                            showMessage(getResources().getString(R.string.failed_login));
                            customProgressDialog.dismiss();
                        }
                    }
                });
    }

    public String encrypt(String value) {
        return AESUtils.encrypt(value);
    }

    private void saveUser() {
        Query query = getFirebase().child("users")
                .child(Objects.requireNonNull(getAuth().getCurrentUser()).getUid());
        query.keepSynced(true);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User usuario = dataSnapshot.getValue(User.class);

                usuario.setUnlogged(false);
                usuario.setLoginInCache(false);

                if (usuario != null) {
                    if (usuario.getMail().contentEquals(getAuth().getCurrentUser().getEmail())) {
                        usuario.mSetSavePassword(true);

                        setUser(usuario, context);
                        callMainActivity();
                    } else {
                        showMessage(getResources().getString(R.string.failed_login_invalid_account));
                        customProgressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DadoUsuario", databaseError.getMessage().toString());
            }
        });
    }

    private void callMainActivity() {
        customProgressDialog.dismiss();
        Intent intent = new Intent(this, Home.class);
        finish();
        startActivity(intent);
    }

    protected static void setUser(Object u, Context context) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        String jsonUser = new Gson().toJson(u);
        prefsEditor.putString("Usuario", jsonUser);
        prefsEditor.commit();
    }

    public static synchronized User getUser(Context context) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = mPrefs.getString("Usuario", null);

        User item = new Gson().fromJson(json, User.class);

        if (item == null) {
            return new User();
        }

        return item;
    }

    public static synchronized String getLasPassword(Context context) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("#lastPassword#", null);
    }

    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean verifyIsConnected() {
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }

    private void showMessage(String msg) {
        Snackbar snackbar = Snackbar.make(viewRoot, msg, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(getResources().getColor(R.color.colorWarning));
        snackbar.show();
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = nmUsuario.getText().toString();
        if (TextUtils.isEmpty(email) || !email.contains("@")) {
            nmUsuario.setError(getString(R.string.message_required_login_email));
            valid = false;
        } else {
            nmUsuario.setError(null);
        }

        String pass = password.getText().toString();
        if (TextUtils.isEmpty(pass)) {
            password.setError(getString(R.string.message_required_login_password));
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }
}
