package com.moondu.leilao.view.fragment.checkin;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moondu.leilao.R;
import com.moondu.leilao.model.adapter.AvariaAdapter;
import com.moondu.leilao.model.entity.ServiceOrder;
import com.moondu.leilao.view.activity.Home;
import com.moondu.leilao.view.fragment.base.BaseFragment;

import java.util.ArrayList;
import java.util.Objects;

import static com.moondu.leilao.model.firebase.FirebaseHelper.getAuth;
import static com.moondu.leilao.model.firebase.FirebaseHelper.getFirebase;

public class CarDamage extends BaseFragment {

    private ServiceOrder item;
    private Integer position;

    private Button addAvaria;
    private RecyclerView rbAvaria;
    private TextView rbAvariaNotFound;

    private RadioButton rbPneu1_Estado_B;
    private RadioButton rbPneu1_Estado_M;
    private RadioButton rbPneu1_Estado_R;
    private RadioButton rbPneu2_Estado_B;
    private RadioButton rbPneu2_Estado_M;
    private RadioButton rbPneu2_Estado_R;
    private RadioButton rbPneu3_Estado_B;
    private RadioButton rbPneu3_Estado_M;
    private RadioButton rbPneu3_Estado_R;
    private RadioButton rbPneu4_Estado_B;
    private RadioButton rbPneu4_Estado_M;
    private RadioButton rbPneu4_Estado_R;
    private RadioButton rbPneu5_Estado_B;
    private RadioButton rbPneu5_Estado_M;
    private RadioButton rbPneu5_Estado_R;

    private RadioButton rbPneu1_Tipo_C;
    private RadioButton rbPneu1_Tipo_L;
    private RadioButton rbPneu1_Tipo_I;
    private RadioButton rbPneu2_Tipo_C;
    private RadioButton rbPneu2_Tipo_L;
    private RadioButton rbPneu2_Tipo_I;
    private RadioButton rbPneu3_Tipo_C;
    private RadioButton rbPneu3_Tipo_L;
    private RadioButton rbPneu3_Tipo_I;
    private RadioButton rbPneu4_Tipo_C;
    private RadioButton rbPneu4_Tipo_L;
    private RadioButton rbPneu4_Tipo_I;
    private RadioButton rbPneu5_Tipo_C;
    private RadioButton rbPneu5_Tipo_L;
    private RadioButton rbPneu5_Tipo_I;

    private EditText ipPneu1_Marca;
    private EditText ipPneu2_Marca;
    private EditText ipPneu3_Marca;
    private EditText ipPneu4_Marca;
    private EditText ipPneu5_Marca;

    private RadioButton rbCombustivel1;
    private RadioButton rbCombustivel2;
    private RadioButton rbCombustivel3;
    private RadioButton rbCombustivel4;

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
        View v = inflater.inflate(R.layout.car_damage, container, false);

        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.menu_survey);

        ((Home) getActivity()).showBackButton(true);

        init(v);
        fillForm();

        return v;
    }

    private void addAvaria() {
        final Dialog dialog = new Dialog(getContext(), R.style.DialogNoActionBarMinWidth);
        dialog.setContentView(R.layout.popup_add_avaria);
        dialog.setCancelable(true);

        EditText input = dialog.findViewById(R.id.ipDescricao);
        Button button = dialog.findViewById(R.id.btnAddAvaria);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = input.getText().toString();

                if (value.trim().length() > 0){
                    if (item.getDamage() == null){
                        item.setDamage(new ArrayList<String>());
                    }

                    item.getDamage().add(value);
                }

                reload();

                dialog.hide();
            }
        });

        dialog.show();
    }

    public void removeItem(int index) {
        if (item.getDamage() == null){
            item.setDamage(new ArrayList<String>());
        }

        item.getDamage().remove(index);

        reload();
    }

    private void reload() {
        if (item.getDamage() == null){
            item.setDamage(new ArrayList<String>());
        }

        if (item.getDamage().size() == 0){
            rbAvariaNotFound.setVisibility(View.VISIBLE);
        }else{
            rbAvariaNotFound.setVisibility(View.GONE);
        }

        AvariaAdapter tAdapter = new AvariaAdapter(this, item.getDamage());
        rbAvaria.setAdapter(tAdapter);
    }

    private void fillForm() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rbAvaria.setLayoutManager(llm);

        reload();

        if (item.getTireCondition1() != null){
            rbPneu1_Estado_B.setChecked(item.getTireCondition1().equals("B"));
            rbPneu1_Estado_M.setChecked(item.getTireCondition1().equals("M"));
            rbPneu1_Estado_R.setChecked(item.getTireCondition1().equals("R"));
        }

        if (item.getTireCondition2() != null){
            rbPneu2_Estado_B.setChecked(item.getTireCondition2().equals("B"));
            rbPneu2_Estado_M.setChecked(item.getTireCondition2().equals("M"));
            rbPneu2_Estado_R.setChecked(item.getTireCondition2().equals("R"));
        }

        if (item.getTireCondition3() != null){
            rbPneu3_Estado_B.setChecked(item.getTireCondition3().equals("B"));
            rbPneu3_Estado_M.setChecked(item.getTireCondition3().equals("M"));
            rbPneu3_Estado_R.setChecked(item.getTireCondition3().equals("R"));
        }

        if (item.getTireCondition4() != null){
            rbPneu4_Estado_B.setChecked(item.getTireCondition4().equals("B"));
            rbPneu4_Estado_M.setChecked(item.getTireCondition4().equals("M"));
            rbPneu4_Estado_R.setChecked(item.getTireCondition4().equals("R"));
        }

        if (item.getTireCondition5() != null){
            rbPneu5_Estado_B.setChecked(item.getTireCondition5().equals("B"));
            rbPneu5_Estado_M.setChecked(item.getTireCondition5().equals("M"));
            rbPneu5_Estado_R.setChecked(item.getTireCondition5().equals("R"));
        }

        if (item.getTireType1() != null){
            rbPneu1_Tipo_C.setChecked(item.getTireType1().equals("C"));
            rbPneu1_Tipo_I.setChecked(item.getTireType1().equals("I"));
            rbPneu1_Tipo_L.setChecked(item.getTireType1().equals("L"));
        }

        if (item.getTireType2() != null){
            rbPneu2_Tipo_C.setChecked(item.getTireType2().equals("C"));
            rbPneu2_Tipo_I.setChecked(item.getTireType2().equals("I"));
            rbPneu2_Tipo_L.setChecked(item.getTireType2().equals("L"));
        }

        if (item.getTireType3() != null){
            rbPneu3_Tipo_C.setChecked(item.getTireType3().equals("C"));
            rbPneu3_Tipo_I.setChecked(item.getTireType3().equals("I"));
            rbPneu3_Tipo_L.setChecked(item.getTireType3().equals("L"));
        }

        if (item.getTireType4() != null){
            rbPneu4_Tipo_C.setChecked(item.getTireType4().equals("C"));
            rbPneu4_Tipo_I.setChecked(item.getTireType4().equals("I"));
            rbPneu4_Tipo_L.setChecked(item.getTireType4().equals("L"));
        }

        if (item.getTireType5() != null){
            rbPneu5_Tipo_C.setChecked(item.getTireType5().equals("C"));
            rbPneu5_Tipo_I.setChecked(item.getTireType5().equals("I"));
            rbPneu5_Tipo_L.setChecked(item.getTireType5().equals("L"));
        }

        if (item.getTireBrand1() != null)
            ipPneu1_Marca.setText(item.getTireBrand1());

        if (item.getTireBrand2() != null)
            ipPneu2_Marca.setText(item.getTireBrand2());

        if (item.getTireBrand3() != null)
            ipPneu3_Marca.setText(item.getTireBrand3());

        if (item.getTireBrand4() != null)
            ipPneu4_Marca.setText(item.getTireBrand4());

        if (item.getTireBrand5() != null)
            ipPneu5_Marca.setText(item.getTireBrand5());

        if (item.getFuelLevel() != null){
            rbCombustivel1.setChecked(item.getFuelLevel().equals(1));
            rbCombustivel2.setChecked(item.getFuelLevel().equals(2));
            rbCombustivel3.setChecked(item.getFuelLevel().equals(3));
            rbCombustivel4.setChecked(item.getFuelLevel().equals(4));
        }

        addAvaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAvaria();
            }
        });
    }

    private void init(View v) {
        addAvaria = v.findViewById(R.id.addAvaria);
        rbAvaria = v.findViewById(R.id.rbAvaria);
        rbAvariaNotFound = v.findViewById(R.id.rbAvariaNotFound);

        rbPneu1_Estado_B = v.findViewById(R.id.rbPneu1_Estado_B);
        rbPneu1_Estado_M = v.findViewById(R.id.rbPneu1_Estado_M);
        rbPneu1_Estado_R = v.findViewById(R.id.rbPneu1_Estado_R);
        rbPneu2_Estado_B = v.findViewById(R.id.rbPneu2_Estado_B);
        rbPneu2_Estado_M = v.findViewById(R.id.rbPneu2_Estado_M);
        rbPneu2_Estado_R = v.findViewById(R.id.rbPneu2_Estado_R);
        rbPneu3_Estado_B = v.findViewById(R.id.rbPneu3_Estado_B);
        rbPneu3_Estado_M = v.findViewById(R.id.rbPneu3_Estado_M);
        rbPneu3_Estado_R = v.findViewById(R.id.rbPneu3_Estado_R);
        rbPneu4_Estado_B = v.findViewById(R.id.rbPneu4_Estado_B);
        rbPneu4_Estado_M = v.findViewById(R.id.rbPneu4_Estado_M);
        rbPneu4_Estado_R = v.findViewById(R.id.rbPneu4_Estado_R);
        rbPneu5_Estado_B = v.findViewById(R.id.rbEstepe_Estado_B);
        rbPneu5_Estado_M = v.findViewById(R.id.rbEstepe_Estado_M);
        rbPneu5_Estado_R = v.findViewById(R.id.rbEstepe_Estado_R);

        rbPneu1_Tipo_C = v.findViewById(R.id.rbPneu1_Tipo_C);
        rbPneu1_Tipo_L = v.findViewById(R.id.rbPneu1_Tipo_L);
        rbPneu1_Tipo_I = v.findViewById(R.id.rbPneu1_Tipo_I);
        rbPneu2_Tipo_C = v.findViewById(R.id.rbPneu2_Tipo_C);
        rbPneu2_Tipo_L = v.findViewById(R.id.rbPneu2_Tipo_L);
        rbPneu2_Tipo_I = v.findViewById(R.id.rbPneu2_Tipo_I);
        rbPneu3_Tipo_C = v.findViewById(R.id.rbPneu3_Tipo_C);
        rbPneu3_Tipo_L = v.findViewById(R.id.rbPneu3_Tipo_L);
        rbPneu3_Tipo_I = v.findViewById(R.id.rbPneu3_Tipo_I);
        rbPneu4_Tipo_C = v.findViewById(R.id.rbPneu4_Tipo_C);
        rbPneu4_Tipo_L = v.findViewById(R.id.rbPneu4_Tipo_L);
        rbPneu4_Tipo_I = v.findViewById(R.id.rbPneu4_Tipo_I);
        rbPneu5_Tipo_C = v.findViewById(R.id.rbEstepe_Tipo_C);
        rbPneu5_Tipo_L = v.findViewById(R.id.rbEstepe_Tipo_L);
        rbPneu5_Tipo_I = v.findViewById(R.id.rbEstepe_Tipo_I);

        ipPneu1_Marca = v.findViewById(R.id.ipPneu1_Marca);
        ipPneu2_Marca = v.findViewById(R.id.ipPneu2_Marca);
        ipPneu3_Marca = v.findViewById(R.id.ipPneu3_Marca);
        ipPneu4_Marca = v.findViewById(R.id.ipPneu4_Marca);
        ipPneu5_Marca = v.findViewById(R.id.ipEstepe_Marca);

        rbCombustivel1 = v.findViewById(R.id.rbCombustivel_1);
        rbCombustivel2 = v.findViewById(R.id.rbCombustivel_2);
        rbCombustivel3 = v.findViewById(R.id.rbCombustivel_3);
        rbCombustivel4 = v.findViewById(R.id.rbCombustivel_4);
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ServiceOrder.class.getSimpleName(), item);
        bundle.putInt(BaseFragment.INDEX, position);

        CarInspection fragment = new CarInspection();
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
            item.setTireCondition1(rbPneu1_Estado_B.isChecked() ? "B" : (rbPneu1_Estado_M.isChecked() ? "M" : "R"));
            item.setTireCondition2(rbPneu2_Estado_B.isChecked() ? "B" : (rbPneu2_Estado_M.isChecked() ? "M" : "R"));
            item.setTireCondition3(rbPneu3_Estado_B.isChecked() ? "B" : (rbPneu3_Estado_M.isChecked() ? "M" : "R"));
            item.setTireCondition4(rbPneu4_Estado_B.isChecked() ? "B" : (rbPneu4_Estado_M.isChecked() ? "M" : "R"));
            item.setTireCondition5(rbPneu5_Estado_B.isChecked() ? "B" : (rbPneu5_Estado_M.isChecked() ? "M" : "R"));

            item.setTireType1(rbPneu1_Tipo_C.isChecked() ? "C" : (rbPneu1_Tipo_L.isChecked() ? "L" : "I"));
            item.setTireType2(rbPneu2_Tipo_C.isChecked() ? "C" : (rbPneu2_Tipo_L.isChecked() ? "L" : "I"));
            item.setTireType3(rbPneu3_Tipo_C.isChecked() ? "C" : (rbPneu3_Tipo_L.isChecked() ? "L" : "I"));
            item.setTireType4(rbPneu4_Tipo_C.isChecked() ? "C" : (rbPneu4_Tipo_L.isChecked() ? "L" : "I"));
            item.setTireType5(rbPneu5_Tipo_C.isChecked() ? "C" : (rbPneu5_Tipo_L.isChecked() ? "L" : "I"));

            item.setTireBrand1(ipPneu1_Marca.getText().toString());
            item.setTireBrand2(ipPneu2_Marca.getText().toString());
            item.setTireBrand3(ipPneu3_Marca.getText().toString());
            item.setTireBrand4(ipPneu4_Marca.getText().toString());
            item.setTireBrand5(ipPneu5_Marca.getText().toString());

            if (rbCombustivel1.isChecked()){
                item.setFuelLevel(1);
            } else if (rbCombustivel2.isChecked()){
                item.setFuelLevel(2);
            } else if (rbCombustivel3.isChecked()){
                item.setFuelLevel(3);
            } else if (rbCombustivel4.isChecked()){
                item.setFuelLevel(4);
            }

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

                    Acc1 fragment = new Acc1();
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
        boolean bPneuEstado1 = false;
        boolean bPneuEstado2 = false;
        boolean bPneuEstado3 = false;
        boolean bPneuEstado4 = false;
        boolean bPneuEstado5 = false;
        boolean bPneuTipo1 = false;
        boolean bPneuTipo2 = false;
        boolean bPneuTipo3 = false;
        boolean bPneuTipo4 = false;
        boolean bPneuTipo5 = false;
        boolean bPneuMarca1 = false;
        boolean bPneuMarca2 = false;
        boolean bPneuMarca3 = false;
        boolean bPneuMarca4 = false;
        boolean bPneuMarca5 = false;
        boolean bCombustivel = false;

        if (rbPneu1_Tipo_I.isChecked()) {
            bPneuEstado1 = true;
            bPneuTipo1 = true;
            bPneuMarca1 = true;
        }else {
            if (rbPneu1_Estado_B.isChecked() || rbPneu1_Estado_M.isChecked() || rbPneu1_Estado_R.isChecked()) {
                bPneuEstado1 = true;
            }

            if (rbPneu1_Tipo_C.isChecked() || rbPneu1_Tipo_L.isChecked() || rbPneu1_Tipo_I.isChecked()) {
                bPneuTipo1 = true;
            }

            if (ipPneu1_Marca.getText() != null && ipPneu1_Marca.getText().toString().length() > 0) {
                bPneuMarca1 = true;
            } else {
                ipPneu1_Marca.setError("Campo obrigatório!");
                bPneuMarca1 = false;
            }
        }

        if (rbPneu2_Tipo_I.isChecked()) {
            bPneuEstado2 = true;
            bPneuTipo2 = true;
            bPneuMarca2 = true;
        }else {
            if (rbPneu2_Estado_B.isChecked() || rbPneu2_Estado_M.isChecked() || rbPneu2_Estado_R.isChecked()) {
                bPneuEstado2 = true;
            }

            if (rbPneu2_Tipo_C.isChecked() || rbPneu2_Tipo_L.isChecked() || rbPneu2_Tipo_I.isChecked()) {
                bPneuTipo2 = true;
            }

            if (ipPneu2_Marca.getText() != null && ipPneu2_Marca.getText().toString().length() > 0) {
                bPneuMarca2 = true;
            } else {
                ipPneu2_Marca.setError("Campo obrigatório!");
                bPneuMarca2 = false;
            }
        }

        if (rbPneu3_Tipo_I.isChecked()) {
            bPneuEstado3 = true;
            bPneuTipo3 = true;
            bPneuMarca3 = true;
        }else {
            if (rbPneu3_Estado_B.isChecked() || rbPneu3_Estado_M.isChecked() || rbPneu3_Estado_R.isChecked()) {
                bPneuEstado3 = true;
            }

            if (rbPneu3_Tipo_C.isChecked() || rbPneu3_Tipo_L.isChecked() || rbPneu3_Tipo_I.isChecked()) {
                bPneuTipo3 = true;
            }

            if (ipPneu3_Marca.getText() != null && ipPneu3_Marca.getText().toString().length() > 0) {
                bPneuMarca3 = true;
            } else {
                ipPneu3_Marca.setError("Campo obrigatório!");
                bPneuMarca3 = false;
            }
        }

        if (rbPneu4_Tipo_I.isChecked()) {
            bPneuEstado4 = true;
            bPneuTipo4 = true;
            bPneuMarca4 = true;
        }else {
            if (rbPneu4_Estado_B.isChecked() || rbPneu4_Estado_M.isChecked() || rbPneu4_Estado_R.isChecked()) {
                bPneuEstado4 = true;
            }

            if (rbPneu4_Tipo_C.isChecked() || rbPneu4_Tipo_L.isChecked() || rbPneu4_Tipo_I.isChecked()) {
                bPneuTipo4 = true;
            }

            if (ipPneu4_Marca.getText() != null && ipPneu4_Marca.getText().toString().length() > 0) {
                bPneuMarca4 = true;
            } else {
                ipPneu4_Marca.setError("Campo obrigatório!");
                bPneuMarca4 = false;
            }
        }

        if (rbPneu5_Tipo_I.isChecked()) {
            bPneuEstado5 = true;
            bPneuTipo5 = true;
            bPneuMarca5 = true;
        }else {
            if (rbPneu5_Estado_B.isChecked() || rbPneu5_Estado_M.isChecked() || rbPneu5_Estado_R.isChecked()) {
                bPneuEstado5 = true;
            }

            if (rbPneu5_Tipo_C.isChecked() || rbPneu5_Tipo_L.isChecked() || rbPneu5_Tipo_I.isChecked()) {
                bPneuTipo5 = true;
            }

            if (ipPneu5_Marca.getText() != null && ipPneu5_Marca.getText().toString().length() > 0) {
                bPneuMarca5 = true;
            } else {
                ipPneu5_Marca.setError("Campo obrigatório!");
                bPneuMarca5 = false;
            }
        }

        if (rbCombustivel1.isChecked() || rbCombustivel2.isChecked() ||
                rbCombustivel3.isChecked() || rbCombustivel4.isChecked()) {
            bCombustivel = true;
        }

        return bPneuEstado1 && bPneuEstado2 && bPneuEstado3 && bPneuEstado4 && bPneuEstado5 &&
                bPneuTipo1 && bPneuTipo2 && bPneuTipo3 && bPneuTipo4 && bPneuTipo5 && bCombustivel &&
                bPneuMarca1 && bPneuMarca2 && bPneuMarca3 && bPneuMarca4 && bPneuMarca5;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}