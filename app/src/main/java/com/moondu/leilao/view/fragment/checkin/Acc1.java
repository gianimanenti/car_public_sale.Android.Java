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

public class Acc1 extends BaseFragment {

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
    private RadioButton rbAcessorio4_S;
    private RadioButton rbAcessorio4_N;
    private RadioButton rbAcessorio4_I;
    private RadioButton rbAcessorio5_S;
    private RadioButton rbAcessorio5_N;
    private RadioButton rbAcessorio5_I;
    private RadioButton rbAcessorio6_S;
    private RadioButton rbAcessorio6_N;
    private RadioButton rbAcessorio6_I;

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
        View v = inflater.inflate(R.layout.accessories_1, container, false);

        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.menu_acessory_1);

        ((Home) getActivity()).showBackButton(true);

        init(v);
        fillForm();

        return v;
    }

    private void fillForm() {
        if (item.getAccessory1() != null){
            rbAcessorio1_S.setChecked(item.getAccessory1().equals("S"));
            rbAcessorio1_N.setChecked(item.getAccessory1().equals("N"));
            rbAcessorio1_I.setChecked(item.getAccessory1().equals("I"));
        }
        if (item.getAccessory2() != null){
            rbAcessorio2_S.setChecked(item.getAccessory2().equals("S"));
            rbAcessorio2_N.setChecked(item.getAccessory2().equals("N"));
            rbAcessorio2_I.setChecked(item.getAccessory2().equals("I"));
        }
        if (item.getAccessory3() != null){
            rbAcessorio3_S.setChecked(item.getAccessory3().equals("S"));
            rbAcessorio3_N.setChecked(item.getAccessory3().equals("N"));
            rbAcessorio3_I.setChecked(item.getAccessory3().equals("I"));
        }
        if (item.getAccessory4() != null){
            rbAcessorio4_S.setChecked(item.getAccessory4().equals("S"));
            rbAcessorio4_N.setChecked(item.getAccessory4().equals("N"));
            rbAcessorio4_I.setChecked(item.getAccessory4().equals("I"));
        }
        if (item.getAccessory5() != null){
            rbAcessorio5_S.setChecked(item.getAccessory5().equals("S"));
            rbAcessorio5_N.setChecked(item.getAccessory5().equals("N"));
            rbAcessorio5_I.setChecked(item.getAccessory5().equals("I"));
        }
        if (item.getAccessory6() != null){
            rbAcessorio6_S.setChecked(item.getAccessory6().equals("S"));
            rbAcessorio6_N.setChecked(item.getAccessory6().equals("N"));
            rbAcessorio6_I.setChecked(item.getAccessory6().equals("I"));
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
        rbAcessorio4_S = v.findViewById(R.id.rbAcessorio4_S);
        rbAcessorio4_N = v.findViewById(R.id.rbAcessorio4_N);
        rbAcessorio4_I = v.findViewById(R.id.rbAcessorio4_I);
        rbAcessorio5_S = v.findViewById(R.id.rbAcessorio5_S);
        rbAcessorio5_N = v.findViewById(R.id.rbAcessorio5_N);
        rbAcessorio5_I = v.findViewById(R.id.rbAcessorio5_I);
        rbAcessorio6_S = v.findViewById(R.id.rbAcessorio6_S);
        rbAcessorio6_N = v.findViewById(R.id.rbAcessorio6_N);
        rbAcessorio6_I = v.findViewById(R.id.rbAcessorio6_I);
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ServiceOrder.class.getSimpleName(), item);
        bundle.putInt(BaseFragment.INDEX, position);

        CarDamage fragment = new CarDamage();
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
            item.setAccessory1(rbAcessorio1_S.isChecked() ? "S" : (rbAcessorio1_N.isChecked() ? "N" : "I"));
            item.setAccessory2(rbAcessorio2_S.isChecked() ? "S" : (rbAcessorio2_N.isChecked() ? "N" : "I"));
            item.setAccessory3(rbAcessorio3_S.isChecked() ? "S" : (rbAcessorio3_N.isChecked() ? "N" : "I"));
            item.setAccessory4(rbAcessorio4_S.isChecked() ? "S" : (rbAcessorio4_N.isChecked() ? "N" : "I"));
            item.setAccessory5(rbAcessorio5_S.isChecked() ? "S" : (rbAcessorio5_N.isChecked() ? "N" : "I"));
            item.setAccessory6(rbAcessorio6_S.isChecked() ? "S" : (rbAcessorio6_N.isChecked() ? "N" : "I"));

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

                    Acc2 fragment = new Acc2();
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
        boolean bAcessorio4 = false;
        boolean bAcessorio5 = false;
        boolean bAcessorio6 = false;

        if (rbAcessorio1_S.isChecked() || rbAcessorio1_N.isChecked() || rbAcessorio1_I.isChecked()) {
            bAcessorio1 = true;
        }

        if (rbAcessorio2_S.isChecked() || rbAcessorio2_N.isChecked() || rbAcessorio2_I.isChecked()) {
            bAcessorio2 = true;
        }

        if (rbAcessorio3_S.isChecked() || rbAcessorio3_N.isChecked() || rbAcessorio3_I.isChecked()) {
            bAcessorio3 = true;
        }

        if (rbAcessorio4_S.isChecked() || rbAcessorio4_N.isChecked() || rbAcessorio4_I.isChecked()) {
            bAcessorio4 = true;
        }

        if (rbAcessorio5_S.isChecked() || rbAcessorio5_N.isChecked() || rbAcessorio5_I.isChecked()) {
            bAcessorio5 = true;
        }

        if (rbAcessorio6_S.isChecked() || rbAcessorio6_N.isChecked() || rbAcessorio6_I.isChecked()) {
            bAcessorio6 = true;
        }

        return bAcessorio1 && bAcessorio2 && bAcessorio3 && bAcessorio4 && bAcessorio5 && bAcessorio6;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}