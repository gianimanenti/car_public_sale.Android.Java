package com.moondu.leilao.model.firebase;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import static com.moondu.leilao.view.activity.UserAuth.getUser;

public class FirebaseHelper {

    public static final long MB = 1048576l;

    private static FirebaseDatabase mDatabase;
    private static DatabaseReference databaseReference;
    private static FirebaseAuth mAuth;

    public static DatabaseReference getFirebase() {
        if (databaseReference == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
            mDatabase.setPersistenceCacheSizeBytes(100 * MB);

            databaseReference = mDatabase.getReference();
        }

        return databaseReference;
    }

    public static FirebaseAuth getAuth() {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }

        return mAuth;
    }

    public static void firebaseToggleConnection(Context context) {
        if (isConnected(context)) {
            getFirebase().getDatabase().goOnline();
        } else {
            getFirebase().getDatabase().goOffline();
        }
    }


    private static boolean isConnected(Context context) {
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            return true;
        }

        return false;
    }

    public static abstract class ListResult<T> {

        private Context context;

        public ListResult(Context context) {
            this.context = context;
        }

        public abstract void onResult(List<T> itens);

        public Context getContext() {
            return context;
        }
    }


    public static abstract class UniqueResult<T> {

        private Context context;

        public UniqueResult(Context context) {
            this.context = context;
        }

        public abstract void onResult(T item);

        public Context getContext() {
            return context;
        }
    }

    public static abstract class QueryListResult<T> implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<T> itens = new ArrayList<T>();

            for (DataSnapshot item : dataSnapshot.getChildren()) {
                try {
                    T result = (T) item.getValue(getTypeClass());

                    itens.add(result);
                } catch (Exception ex) {
                    Log.e("Erro:", ex.getMessage());
                }
            }

            onResultList(itens);
        }

        public abstract void onResultList(List<T> itens);

        private Class<?> getTypeClass() {
            return (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    public static String getCurrentUID(Context context) {
        return getUser(context).getKey();
    }
}
