package com.moondu.leilao.view.fragment.checkin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moondu.leilao.R;
import com.moondu.leilao.model.entity.ServiceOrder;
import com.moondu.leilao.view.activity.Home;
import com.moondu.leilao.view.fragment.base.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.content.Context.LOCATION_SERVICE;
import static com.moondu.leilao.model.firebase.FirebaseHelper.getAuth;
import static com.moondu.leilao.model.firebase.FirebaseHelper.getFirebase;

public class CarID extends BaseFragment implements LocationListener {

    private ServiceOrder item;
    private Integer position;

    private TextView tvCondutor;

    private RadioGroup rbPossuiCond;
    private RadioButton rbPossuiCond1;
    private RadioButton rbPossuiCond2;

    private RadioButton rbTipoIdent1;
    private RadioButton rbTipoIdent2;
    private RadioButton rbTipoIdent3;

    private EditText ipIdentification;
    private EditText ipEndereco;
    private EditText ipTipoVeiculo;
    private EditText ipModelo;
    private EditText ipCor;

    private LocationManager locationManager;

    private Location location;

    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    public static final long MIN_TIME_BW_UPDATES = 0;

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
        View v = inflater.inflate(R.layout.car_id, container, false);

        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.menu_identification);

        ((Home) getActivity()).showBackButton(true);

        if (item == null) {
            item = new ServiceOrder();
            item.setDate(new Date());

            requestLocation();
        }

        init(v);
        fillForm();

        return v;
    }

    @SuppressLint("MissingPermission")
    private void requestLocation() {
        locationManager = (LocationManager) getActivity().getApplicationContext().
                getSystemService(LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);


        location = getActualPostion();

        if (location != null) {
            updatePosition(location);
        }
    }

    private void updatePosition(Location location) {
        this.location = location;

        String address = null;

        if (location != null) {
            try {
                Geocoder gCoder = new Geocoder(getActivity());

                List<Address> addresses = gCoder.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);

                if (addresses != null && addresses.size() > 0) {
                    address = addresses.get(0).getAddressLine(0);
                }
            } catch (Exception ex) {
                address = null;
            }
        }

        if (ipEndereco != null)
            ipEndereco.setText(address);
    }

    private Location getActualPostion() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        List<String> providers = locationManager.getProviders(true);

        for (String provider : providers) {
            locationManager.requestLocationUpdates(provider, MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            Location l = locationManager.getLastKnownLocation(provider);

            if (l == null) {
                continue;
            }

            if (location == null || l.getAccuracy() < location.getAccuracy()) {
                location = l;
            }
        }

        return location;
    }

    private void fillForm() {
        if (item.getIdType() == null)
            item.setIdType(1);

        if (item.getHasDriver() == null)
            item.setHasDriver(1);

        rbTipoIdent1.setChecked(item.getIdType().equals(1));
        rbTipoIdent2.setChecked(item.getIdType().equals(2));
        rbTipoIdent2.setChecked(item.getIdType().equals(3));

        ipIdentification.setText(item.getIdentification() != null ? item.getIdentification() : "");
        ipEndereco.setText(item.getLocation() != null ? item.getLocation() : "");
        ipTipoVeiculo.setText(item.getVehicleType() != null ? item.getVehicleType() : "");
        ipModelo.setText(item.getModel() != null ? item.getModel() : "");
        ipCor.setText(item.getColor() != null ? item.getColor() : "");

        rbPossuiCond1.setChecked(item.getHasDriver().equals(1));
        rbPossuiCond2.setChecked(item.getHasDriver().equals(2));

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (rbTipoIdent3.isChecked()) {
                    tvCondutor.setVisibility(View.GONE);
                    rbPossuiCond.setVisibility(View.GONE);
                } else {
                    tvCondutor.setVisibility(View.VISIBLE);
                    rbPossuiCond.setVisibility(View.VISIBLE);
                }
            }
        };

        rbTipoIdent1.setOnCheckedChangeListener(listener);
        rbTipoIdent2.setOnCheckedChangeListener(listener);
        rbTipoIdent3.setOnCheckedChangeListener(listener);

        ipIdentification.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
    }

    private void init(View v) {
        tvCondutor = v.findViewById(R.id.tvCondutor);

        rbTipoIdent1 = v.findViewById(R.id.rbTipoIdent_1);
        rbTipoIdent2 = v.findViewById(R.id.rbTipoIdent_2);
        rbTipoIdent3 = v.findViewById(R.id.rbTipoIdent_3);

        ipIdentification = v.findViewById(R.id.ipIdentification);
        ipEndereco = v.findViewById(R.id.ipEndereco);
        ipTipoVeiculo = v.findViewById(R.id.ipTipoVeiculo);
        ipModelo = v.findViewById(R.id.ipModelo);
        ipCor = v.findViewById(R.id.ipCor);

        rbPossuiCond = v.findViewById(R.id.rbPossuiCond);
        rbPossuiCond1 = v.findViewById(R.id.rbPossuiCond_1);
        rbPossuiCond2 = v.findViewById(R.id.rbPossuiCond_2);
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
            item.setIdType(rbTipoIdent1.isChecked() ? 1 : (rbTipoIdent1.isChecked() ? 2 : 3));
            item.setIdentification(ipIdentification.getText().toString());
            item.setLocation(ipEndereco.getText().toString());
            item.setVehicleType(ipTipoVeiculo.getText().toString());
            item.setModel(ipModelo.getText().toString());
            item.setColor(ipCor.getText().toString());
            item.setHasDriver(rbPossuiCond1.isChecked() ? 1 : 2);

            if (item.getId() == null) {
                item.setId("OS-" + new SimpleDateFormat("yyMMdd-HHmmss").format(item.getDate()));
            }

            if (locationManager != null)
            locationManager.removeUpdates(this);

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
                        fragment = new DriverID();
                    }else{
                        fragment = new CarEvent();
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
                    "Há campos a serem preenchidos", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, new ServiceOrders()).addToBackStack(null).commit();
    }

    private boolean isValid() {
        boolean bIdentificacao = false;
        boolean bLocalizacao = false;
        boolean bTipoVeiculo = false;
        boolean bModelo = false;
        boolean bCor = false;

        if (ipIdentification.getText() != null && ipIdentification.getText().toString().length() > 0) {
            bIdentificacao = true;
        } else {
            ipIdentification.setError("Campo obrigatório!");
            bIdentificacao = false;
        }

        if (ipEndereco.getText() != null && ipEndereco.getText().toString().length() > 0) {
            bLocalizacao = true;
        } else {
            ipEndereco.setError("Campo obrigatório!");
            bLocalizacao = false;
        }

        if (ipTipoVeiculo.getText() != null && ipTipoVeiculo.getText().toString().length() > 0) {
            bTipoVeiculo = true;
        } else {
            ipTipoVeiculo.setError("Campo obrigatório!");
            bTipoVeiculo = false;
        }

        if (ipModelo.getText() != null && ipModelo.getText().toString().length() > 0) {
            bModelo = true;
        } else {
            ipModelo.setError("Campo obrigatório!");
            bModelo = false;
        }

        if (ipCor.getText() != null && ipCor.getText().toString().length() > 0) {
            bCor = true;
        } else {
            ipCor.setError("Campo obrigatório!");
            bCor = false;
        }

        return bIdentificacao && bLocalizacao && bTipoVeiculo && bModelo && bCor;
    }

    @Override
    public void onLocationChanged(Location location) {
        updatePosition(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}