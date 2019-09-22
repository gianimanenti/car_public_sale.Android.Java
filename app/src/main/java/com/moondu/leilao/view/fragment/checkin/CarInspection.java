package com.moondu.leilao.view.fragment.checkin;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.Nullable;

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

public class CarInspection extends BaseFragment {

    private ServiceOrder item;
    private Integer position;

    private RadioButton rbRemBasePrestador1;
    private RadioButton rbRemBasePrestador2;
    private RadioButton rbRemBasePrestador3;
    private RadioButton rbLocalEscuro1;
    private RadioButton rbLocalEscuro2;
    private RadioButton rbLocalEscuro3;
    private RadioButton rbAcompanhouTransp1;
    private RadioButton rbAcompanhouTransp2;
    private RadioButton rbAcompanhouTransp3;
    private RadioButton rbReboquistaVeic1;
    private RadioButton rbReboquistaVeic2;
    private RadioButton rbReboquistaVeic3;
    private RadioButton rbPertencesPessoais1;
    private RadioButton rbPertencesPessoais2;
    private RadioButton rbPertencesPessoais3;

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
        View v = inflater.inflate(R.layout.car_inspection, container, false);

        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.menu_survey);

        ((Home) getActivity()).showBackButton(true);

        init(v);
        fillForm();

        return v;
    }

    private void fillForm() {
        if (item.getRemovedToBase() != null) {
            rbRemBasePrestador1.setChecked(item.getRemovedToBase().equals(1));
            rbRemBasePrestador2.setChecked(item.getRemovedToBase().equals(2));
            rbRemBasePrestador3.setChecked(item.getRemovedToBase().equals(3));
        }

        if (item.getTrackShipping() != null) {
            rbAcompanhouTransp1.setChecked(item.getTrackShipping().equals(1));
            rbAcompanhouTransp2.setChecked(item.getTrackShipping().equals(2));
            rbAcompanhouTransp3.setChecked(item.getRemovedToBase().equals(3));
        }

        if (item.getDarkPlace() != null) {
            rbLocalEscuro1.setChecked(item.getDarkPlace().equals(1));
            rbLocalEscuro2.setChecked(item.getDarkPlace().equals(2));
            rbLocalEscuro3.setChecked(item.getRemovedToBase().equals(3));
        }

        if (item.getInteriorVehicle() != null) {
            rbReboquistaVeic1.setChecked(item.getInteriorVehicle().equals(1));
            rbReboquistaVeic2.setChecked(item.getInteriorVehicle().equals(2));
            rbReboquistaVeic3.setChecked(item.getRemovedToBase().equals(3));
        }

        if (item.getRemovedBelongings() != null) {
            rbPertencesPessoais1.setChecked(item.getRemovedBelongings().equals(1));
            rbPertencesPessoais2.setChecked(item.getRemovedBelongings().equals(2));
            rbPertencesPessoais3.setChecked(item.getRemovedToBase().equals(3));
        }
    }

    private void init(View v) {
        rbRemBasePrestador1 = v.findViewById(R.id.rbRemBasePrestador_1);
        rbRemBasePrestador2 = v.findViewById(R.id.rbRemBasePrestador_2);
        rbRemBasePrestador3 = v.findViewById(R.id.rbRemBasePrestador_3);
        rbLocalEscuro1 = v.findViewById(R.id.rbLocalEscuro_1);
        rbLocalEscuro2 = v.findViewById(R.id.rbLocalEscuro_2);
        rbLocalEscuro3 = v.findViewById(R.id.rbLocalEscuro_3);
        rbAcompanhouTransp1 = v.findViewById(R.id.rbAcompanhouTransp_1);
        rbAcompanhouTransp2 = v.findViewById(R.id.rbAcompanhouTransp_2);
        rbAcompanhouTransp3 = v.findViewById(R.id.rbAcompanhouTransp_3);
        rbReboquistaVeic1 = v.findViewById(R.id.rbReboquistaVeic_1);
        rbReboquistaVeic2 = v.findViewById(R.id.rbReboquistaVeic_2);
        rbReboquistaVeic3 = v.findViewById(R.id.rbReboquistaVeic_3);
        rbPertencesPessoais1 = v.findViewById(R.id.rbPertencesPessoais_1);
        rbPertencesPessoais2 = v.findViewById(R.id.rbPertencesPessoais_2);
        rbPertencesPessoais3 = v.findViewById(R.id.rbPertencesPessoais_3);
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ServiceOrder.class.getSimpleName(), item);
        bundle.putInt(BaseFragment.INDEX, position);

        CarEvent fragment = new CarEvent();
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
            item.setRemovedToBase(rbRemBasePrestador1.isChecked() ? 1 : (rbRemBasePrestador2.isChecked() ? 2 : 3));
            item.setDarkPlace(rbLocalEscuro1.isChecked() ? 1 : (rbLocalEscuro2.isChecked() ? 2 : 3));
            item.setTrackShipping(rbAcompanhouTransp1.isChecked() ? 1 : (rbAcompanhouTransp2.isChecked() ? 2 : 3));
            item.setInteriorVehicle(rbReboquistaVeic1.isChecked() ? 1 : (rbReboquistaVeic2.isChecked() ? 2 : 3));
            item.setRemovedBelongings(rbPertencesPessoais1.isChecked() ? 1 : (rbPertencesPessoais2.isChecked() ? 2 : 3));

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

                    CarDamage fragment = new CarDamage();
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
        boolean bRemBasePrestador = false;
        boolean bLocalEscuro = false;
        boolean bAcompanhouTransp = false;
        boolean bReboquistaVeic = false;
        boolean rbPertencesPessoais = false;

        if (rbRemBasePrestador1.isChecked() || rbRemBasePrestador2.isChecked() || rbRemBasePrestador3.isChecked()) {
            bRemBasePrestador = true;
        }

        if (rbLocalEscuro1.isChecked() || rbLocalEscuro2.isChecked() || rbLocalEscuro3.isChecked()) {
            bLocalEscuro = true;
        }

        if (rbAcompanhouTransp1.isChecked() || rbAcompanhouTransp2.isChecked() || rbAcompanhouTransp3.isChecked()) {
            bAcompanhouTransp = true;
        }

        if (rbReboquistaVeic1.isChecked() || rbReboquistaVeic2.isChecked() || rbReboquistaVeic3.isChecked()) {
            bReboquistaVeic = true;
        }

        if (rbPertencesPessoais1.isChecked() || rbPertencesPessoais2.isChecked() || rbPertencesPessoais3.isChecked()) {
            rbPertencesPessoais = true;
        }

        return bRemBasePrestador && bLocalEscuro && bAcompanhouTransp && bReboquistaVeic && rbPertencesPessoais;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}