package com.moondu.leilao.view.fragment.checkin;

import androidx.annotation.Nullable;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moondu.leilao.R;
import com.moondu.leilao.model.entity.ServiceOrder;
import com.moondu.leilao.view.activity.Home;
import com.moondu.leilao.view.fragment.base.BaseFragment;

import java.util.Objects;

import static com.moondu.leilao.model.firebase.FirebaseHelper.getAuth;
import static com.moondu.leilao.model.firebase.FirebaseHelper.getFirebase;

public class Acc5 extends BaseFragment {

    private ServiceOrder item;
    private Integer position;

    private RadioButton rbAcessorio1_S;
    private RadioButton rbAcessorio1_N;
    private RadioButton rbAcessorio1_I;
    private RadioButton rbAcessorio2_S;
    private RadioButton rbAcessorio2_N;
    private RadioButton rbAcessorio2_I;
    private RadioButton rbAcessorio3_S;
    private RadioButton rbAcessorio3_N;
    private RadioButton rbAcessorio3_I;

    private EditText ipOutros;

    @Override
    public void onCreate(@Nullable Bundle instance) {
        super.onCreate(instance);

        if (getArguments() != null) {
            position = getArguments().getInt(BaseFragment.INDEX);
            item = (ServiceOrder) getArguments().getSerializable(ServiceOrder.class.getName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.accessories_5, container, false);

        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.menu_acessory_5);

        ((Home) getActivity()).showBackButton(true);

        init(v);
        fillForm();

        return v;
    }

    private void fillForm() {
        if (item.getAccessory25() != null) {
            rbAcessorio1_S.setChecked(item.getAccessory25().equals("S"));
            rbAcessorio1_N.setChecked(item.getAccessory25().equals("N"));
            rbAcessorio1_I.setChecked(item.getAccessory25().equals("I"));
        }
        if (item.getAccessory26() != null) {
            rbAcessorio2_S.setChecked(item.getAccessory26().equals("S"));
            rbAcessorio2_N.setChecked(item.getAccessory26().equals("N"));
            rbAcessorio2_I.setChecked(item.getAccessory26().equals("I"));
        }
        if (item.getAccessory27() != null) {
            rbAcessorio3_S.setChecked(item.getAccessory27().equals("S"));
            rbAcessorio3_N.setChecked(item.getAccessory27().equals("N"));
            rbAcessorio3_I.setChecked(item.getAccessory27().equals("I"));
        }
        if (item.getOtherAccessories() != null) {
            ipOutros.setText(item.getOtherAccessories());
        }
    }

    private void init(View v) {
        rbAcessorio1_S = v.findViewById(R.id.rbAcessorio1_S);
        rbAcessorio1_N = v.findViewById(R.id.rbAcessorio1_N);
        rbAcessorio1_I = v.findViewById(R.id.rbAcessorio1_I);
        rbAcessorio2_S = v.findViewById(R.id.rbAcessorio2_S);
        rbAcessorio2_N = v.findViewById(R.id.rbAcessorio2_N);
        rbAcessorio2_I = v.findViewById(R.id.rbAcessorio2_I);
        rbAcessorio3_S = v.findViewById(R.id.rbAcessorio3_S);
        rbAcessorio3_N = v.findViewById(R.id.rbAcessorio3_N);
        rbAcessorio3_I = v.findViewById(R.id.rbAcessorio3_I);

        ipOutros = v.findViewById(R.id.ipOutros);
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ServiceOrder.class.getSimpleName(), item);
        bundle.putInt(BaseFragment.INDEX, position);

        Acc4 fragment = new Acc4();
        fragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, new CarID()).addToBackStack(null).commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!menu.hasVisibleItems()) {
            inflater.inflate(R.menu.menu_next_item, menu);

            View view = Home.toolbar.getMenu().findItem(R.id.nextItem).getActionView();

            view.findViewById(R.id.nextItemBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    save();
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    save();
                }
            });
        }
    }

    private void save() {
        if (isValid()) {
            item.setAccessory25(rbAcessorio1_S.isChecked() ? "S" : (rbAcessorio1_N.isChecked() ? "N" : "I"));
            item.setAccessory26(rbAcessorio2_S.isChecked() ? "S" : (rbAcessorio2_N.isChecked() ? "N" : "I"));
            item.setAccessory27(rbAcessorio3_S.isChecked() ? "S" : (rbAcessorio3_N.isChecked() ? "N" : "I"));

            item.setOtherAccessories(ipOutros.getText().toString());

            final String uid = Objects.requireNonNull(getAuth().getCurrentUser()).getUid();
            Query query = getFirebase().child("service_orders").child(uid);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long index = dataSnapshot.getChildrenCount();

                    if (position != null && position != -1)
                        index = position;

                    getFirebase().child("service_orders").child(uid).
                            child(String.valueOf(index)).setValue(item);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ServiceOrder.class.getName(), item);

                    BaseFragment fragment = null;

                    if (item.getHasDriver() == 1){
                        fragment = new SignatureDriver();
                    }else{
                        fragment = new SignatureOperator();
                    }

                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("DadoUsuario", databaseError.getMessage().toString());
                }
            });
        } else {
            Snackbar.make(getActivity().findViewById(android.R.id.content),
                    "Todos os campos são obrigatórios", Snackbar.LENGTH_SHORT).show();
        }
    }

    private boolean isValid() {
        boolean bAcessorio1 = false;
        boolean bAcessorio2 = false;
        boolean bAcessorio3 = false;

        if (rbAcessorio1_S.isChecked() || rbAcessorio1_N.isChecked() || rbAcessorio1_I.isChecked()) {
            bAcessorio1 = true;
        }

        if (rbAcessorio2_S.isChecked() || rbAcessorio2_N.isChecked() || rbAcessorio2_I.isChecked()) {
            bAcessorio2 = true;
        }

        if (rbAcessorio3_S.isChecked() || rbAcessorio3_N.isChecked() || rbAcessorio3_I.isChecked()) {
            bAcessorio3 = true;
        }

        return bAcessorio1 && bAcessorio2 && bAcessorio3;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}