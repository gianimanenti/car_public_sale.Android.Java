package com.moondu.leilao.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;
import com.moondu.leilao.R;
import com.moondu.leilao.model.entity.User;
import com.moondu.leilao.view.fragment.checkin.ServiceOrders;
import com.moondu.leilao.view.fragment.checkout.CheckOut;

import static com.moondu.leilao.model.firebase.FirebaseHelper.getAuth;
import static com.moondu.leilao.view.activity.UserAuth.getUser;
import static com.moondu.leilao.view.activity.UserAuth.setUser;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private boolean mToolBarNavigationListenerIsRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(this.getString(R.string.app_name));

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView nav = drawerLayout.findViewById(R.id.nav_view);

        TextView userName = nav.getHeaderView(0).findViewById(R.id.nameUser);
        TextView userDesc = nav.getHeaderView(0).findViewById(R.id.typeUser);

        userName.setText(getUser(this).getName());
        userDesc.setText(getUser(this).getProfile());

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ServiceOrders fragment = new ServiceOrders();

//        if (BaseFragment.lastFragment != null){
//            try {
//                fragment = (CheckOut) BaseFragment.lastFragment.newInstance();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            }
//
//            Bundle bundle = new Bundle();
//            bundle.putSerializable(ServiceOrder.class.getName(), BaseFragment.serviceOrder);
//            bundle.putInt(BaseFragment.INDEX, BaseFragment.position);
//
//            fragment.setArguments(bundle);
//        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();


        TextView llCheckIn = drawerLayout.findViewById(R.id.nav_checkin);

        llCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new ServiceOrders()).commit();
            }
        });


        TextView llCheckOut = drawerLayout.findViewById(R.id.nav_checkout);

        llCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new CheckOut()).commit();
            }
        });

        /*
        LinearLayout llFaleConosco = drawerLayout.findViewById(R.id.nav_fale_conosco);

        llFaleConosco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new FragmentFaleConosco()).commit();
            }
        });
        */


        LinearLayout llSairApp = drawerLayout.findViewById(R.id.nav_sair);
        llSairApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);

                getAuth().signOut();

                User item = getUser(Home.this);

                item.setUnlogged(true);

                setUser(item, Home.this);

                SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(Home.this);
                mPrefs.edit().clear().commit();

                Intent intent = new Intent(Home.this, UserAuth.class);
                finish();
                startActivity(intent);
            }
        });
    }

    public void showBackButton(boolean show) {
        if (show) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (!mToolBarNavigationListenerIsRegistered) {
                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

                mToolBarNavigationListenerIsRegistered = true;
            }

        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            toggle.setDrawerIndicatorEnabled(true);
            toggle.setToolbarNavigationClickListener(null);

            mToolBarNavigationListenerIsRegistered = false;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    /*
    private Context context = this;

    private BaseFragment fragment;

    // GPS
    protected LocationManager locationManager;
    private List<Posicao> navegacao;
    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters
    public static final long MIN_TIME_BW_UPDATES = 0; // 1 minute

    public static boolean EM_ATENDIMENTO = false;
    public static Snackbar SB_MODE_VIEW;

    private Fragment mContent;

    public static Agenda lastAgenda;
    public static int idxAgenda;

    private GPSService service;

    private Dialog popupGps;
    private Location location;
    private boolean inLocation;
    private String address;
    private Handler gpsHandler;

    public static Posicao lastPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(this.getString(R.string.title_activity_home));

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView nav = drawerLayout.findViewById(R.id.nav_view);
        TextView userName = nav.getHeaderView(0).findViewById(R.id.nameUser);
        TextView userDesc = nav.getHeaderView(0).findViewById(R.id.typeUser);

        SB_MODE_VIEW = Snackbar.make(findViewById(android.R.id.content), "Modo visualização",
                Snackbar.LENGTH_INDEFINITE);

        userName.setText(getUser(context).getNome());
        userDesc.setText(getUser(context).getPerfil());

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .replace(R.id.frame_container, new AgendamentosList());
        fragmentTransaction.commit();


        TextView llCaderno = drawerLayout.findViewById(R.id.nav_checkin);

        llCaderno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new AgendamentosList()).commit();
            }
        });


        TextView llEstadio = drawerLayout.findViewById(R.id.nav_estadio);

        llEstadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new FragmentEstadiosList()).commit();
            }
        });

        TextView llDicas = drawerLayout.findViewById(R.id.nav_dicas);

        llDicas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new DicasInstrucoesList()).commit();
            }
        });

        TextView llDoencas = drawerLayout.findViewById(R.id.nav_bug);

        llDoencas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new DoencaPragaList()).commit();
            }
        });

        LinearLayout llFaleConosco = drawerLayout.findViewById(R.id.nav_fale_conosco);

        llFaleConosco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new FragmentFaleConosco()).commit();
            }
        });
        LinearLayout llSairApp = drawerLayout.findViewById(R.id.nav_sair);
        llSairApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);

                getAuth().signOut();

                Usuario item  = getUser(context);

                item.setUnlogged(true);

                setUser(item, context);

//                SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
//                mPrefs.edit().clear().commit();

                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });

        if (!isServiceRunning(DadosService.class)){
            getApplicationContext().startService(new Intent(getApplicationContext(), DadosService.class));
        }
    }

    public void iniciaTerminaAtendimento(final BaseFragment fragment) {
        if (EM_ATENDIMENTO) {
            terminaAtendimento(fragment);
        } else {
            iniciaAtendimento(fragment);
        }
    }

    private void terminaAtendimento(BaseFragment fragment) {
        EM_ATENDIMENTO = false;

        if (isServiceRunning(GPSService.class)) {
            context.stopService(new Intent(getApplicationContext(), GPSService.class));
        }

        getFirebase().getDatabase().goOnline();

        fragment.modoVisualizacao();
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void iniciaAtendimento(final BaseFragment fragment) {
        this.fragment = fragment;

        if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 999);
        } else {
            requestLastIndex();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 999) {
            requestLastIndex();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestLastIndex() {
        popupGps = new Dialog(HomeActivity.this,
                R.style.DialogNoActionBarMinWidth);
        popupGps.setContentView(R.layout.dialog_inicio_atendimento);
        popupGps.setCancelable(false);

        TextView lnNoGps = popupGps.findViewById(R.id.lbGps);
        TextView tvPosition = popupGps.findViewById(R.id.position);
        TextView tvAddress = popupGps.findViewById(R.id.address);
        TextView tvInLocation = popupGps.findViewById(R.id.inLocation);
        TextView tvOutLocation = popupGps.findViewById(R.id.outLocation);
        TextView tvNoLocation = popupGps.findViewById(R.id.noLocation);
        TextView lnJustification = popupGps.findViewById(R.id.lbJust);
        ProgressBar progressBar = popupGps.findViewById(R.id.progressBar);
        final EditText justification = popupGps.findViewById(R.id.justification);
        TextView tvRecLocation = popupGps.findViewById(R.id.recLocation);

        Button cancel = popupGps.findViewById(R.id.btCancelar);
        Button confirm = popupGps.findViewById(R.id.btIniciar);
        Button gps = popupGps.findViewById(R.id.btGps);

        gps.setVisibility(View.GONE);
        lnNoGps.setVisibility(View.GONE);
        tvAddress.setVisibility(View.GONE);
        tvPosition.setVisibility(View.GONE);
        tvInLocation.setVisibility(View.GONE);
        tvOutLocation.setVisibility(View.GONE);
        tvNoLocation.setVisibility(View.GONE);
        lnJustification.setVisibility(View.GONE);
        justification.setVisibility(View.GONE);

        confirm.setVisibility(View.INVISIBLE);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupGps.dismiss();

                if (locationManager != null)
                    locationManager.removeUpdates(HomeActivity.this);

                if (fragment != null)
                    fragment.modoVisualizacao();
            }
        });

        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String just = justification.getText().toString();

                if (!inLocation && just.trim().equals("")) {
                    Toast.makeText(HomeActivity.this, "É obrigatório informar uma Justificativa", Toast.LENGTH_LONG).show();
                } else {
                    lastAgenda.setJustificativa(just);

                    Posicao p = new Posicao();
                    p.setDescricao(address);
                    p.setLatitude(location.getLatitude());
                    p.setLongitude(location.getLongitude());

                    lastAgenda.setPosicao(p);

                    getFirebase()
                            .child("agendas")
                            .child(FirebaseHelper.getCurrentUID(context))
                            .child(convertDateToFarmDate(lastAgenda.getData()))
                            .child(String.valueOf(idxAgenda))
                            .setValue(lastAgenda);

                    popupGps.dismiss();

                    Intent intent = new Intent(getApplicationContext(), GPSService.class);
                    intent.putExtra(BaseFragment.ARG_PARAM_AGENDA, lastAgenda);
                    intent.putExtra(BaseFragment.ARG_PARAM_USER, FirebaseHelper.getCurrentUID(context));
                    intent.putExtra(BaseFragment.ARG_PARAM_IDX_ATTENDANCE, "n" + new Date().getTime());

                    getApplicationContext().startService(intent);

                    locationManager.removeUpdates(HomeActivity.this);

                    fragment.modoEdicao();

                    getFirebase().getDatabase().goOffline();
                }
            }
        });

        popupGps.show();

        new AtendimentoComplResult(this, lastAgenda.getChave()) {
            @Override
            public void onResult(Atendimento item) {
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

        if (!isGPSEnabled) {
            notifyNoGps();
        } else {
            requestActualPostion();
        }
    }

    private void notifyNoGps() {
        TextView lnNoGps = popupGps.findViewById(R.id.lbGps);
        TextView tvPosition = popupGps.findViewById(R.id.position);
        TextView tvAddress = popupGps.findViewById(R.id.address);
        TextView tvInLocation = popupGps.findViewById(R.id.inLocation);
        TextView tvOutLocation = popupGps.findViewById(R.id.outLocation);
        TextView tvNoLocation = popupGps.findViewById(R.id.noLocation);
        TextView lnJustification = popupGps.findViewById(R.id.lbJust);
        ProgressBar progressBar = popupGps.findViewById(R.id.progressBar);
        final EditText justification = popupGps.findViewById(R.id.justification);
        TextView tvRecLocation = popupGps.findViewById(R.id.recLocation);

        Button cancel = popupGps.findViewById(R.id.btCancelar);
        Button confirm = popupGps.findViewById(R.id.btIniciar);
        Button gps = popupGps.findViewById(R.id.btGps);

        gps.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.VISIBLE);
        confirm.setVisibility(View.GONE);
        tvAddress.setVisibility(View.GONE);
        tvPosition.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        tvInLocation.setVisibility(View.GONE);
        tvRecLocation.setVisibility(View.GONE);
        tvOutLocation.setVisibility(View.GONE);
        justification.setVisibility(View.GONE);
        tvNoLocation.setVisibility(View.VISIBLE);
        lnNoGps.setVisibility(View.VISIBLE);
    }

    private void requestActualPostion() {
        location = getActualPostion();

        if (location != null) {
            updatePosition(location);
        } else {
            gpsHandler = new Handler();

            gpsHandler.postDelayed(new Runnable() {
                @SuppressLint("MissingPermission")
                @Override
                public void run() {
                    location = getActualPostion();

                    if (location != null) {
                        updatePosition(location);
                    } else {
                        gpsHandler.postDelayed(this, 1000);
                    }
                }
            }, 1000);
        }
    }

    private Location getActualPostion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    private void updatePosition(Location location) {
        this.location = location;

        String address = null;

        if (location != null) {
            try {
                Geocoder gCoder = new Geocoder(HomeActivity.this);

                List<Address> addresses = gCoder.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);

                if (addresses != null && addresses.size() > 0) {
                    address = addresses.get(0).getAddressLine(0);
                }
            } catch (Exception ex) {
                address = null;
            }
        }
        TextView lnNoGps = popupGps.findViewById(R.id.lbGps);
        TextView tvPosition = popupGps.findViewById(R.id.position);
        TextView tvAddress = popupGps.findViewById(R.id.address);
        TextView tvInLocation = popupGps.findViewById(R.id.inLocation);
        TextView tvOutLocation = popupGps.findViewById(R.id.outLocation);
        TextView tvNoLocation = popupGps.findViewById(R.id.noLocation);
        TextView lnJustification = popupGps.findViewById(R.id.lbJust);
        ProgressBar progressBar = popupGps.findViewById(R.id.progressBar);
        final EditText justification = popupGps.findViewById(R.id.justification);
        TextView tvRecLocation = popupGps.findViewById(R.id.recLocation);

        Button confirm = popupGps.findViewById(R.id.btIniciar);
        Button gps = popupGps.findViewById(R.id.btGps);

        boolean noPosition = location == null || (!(location.getLatitude() != 0d && location.getLongitude() != 0d));
        inLocation = location == null ? false : isInLocation(location.getLatitude(),
                location.getLongitude(), location.getAccuracy());

        gps.setVisibility(View.GONE);
        lnNoGps.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        tvRecLocation.setVisibility(View.GONE);
        confirm.setVisibility(View.VISIBLE);

        if (noPosition) {
            tvPosition.setVisibility(View.GONE);
            tvAddress.setVisibility(View.GONE);
            tvInLocation.setVisibility(View.GONE);
            tvOutLocation.setVisibility(View.GONE);
            tvNoLocation.setVisibility(View.VISIBLE);
            lnJustification.setVisibility(View.VISIBLE);
            justification.setVisibility(View.VISIBLE);
        } else {
            tvPosition.setVisibility(View.VISIBLE);
            tvAddress.setVisibility(View.VISIBLE);
            tvNoLocation.setVisibility(View.GONE);
            tvInLocation.setVisibility(View.GONE);
            tvOutLocation.setVisibility(View.GONE);
            lnJustification.setVisibility(View.GONE);
            justification.setVisibility(View.GONE);

            tvAddress.setText(address);
            tvPosition.setText("Lat.:" + location.getLatitude() + "\r\nLng: " + location.getLongitude());

            if (inLocation) {
                tvInLocation.setVisibility(View.VISIBLE);
            } else {
                lnJustification.setVisibility(View.VISIBLE);
                tvOutLocation.setVisibility(View.VISIBLE);
                justification.setVisibility(View.VISIBLE);
            }
        }
    }

    private boolean isInLocation(double lat, double lng, double accuracy) {
        double latP = lastAgenda.getLocal().getLatitude();
        double lngP = lastAgenda.getLocal().getLongitude();
        double dist = lastAgenda.getLocal().getDistancia();

        double distance = distance(lat, lng, latP, lngP) * 1000;

        return distance <= dist;
    }

    private double distance(double latA, double lngA, double latB, double lngB) {
        if ((latA == latB) && (lngA == lngB)) {
            return 0;
        } else {
            double theta = lngA - lngB;
            double dist = Math.sin(Math.toRadians(latA)) * Math.sin(Math.toRadians(latB)) +
                    Math.cos(Math.toRadians(latA)) * Math.cos(Math.toRadians(latB)) * Math.cos(Math.toRadians(theta));

            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;

            return dist * 1.609344;
        }
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
        requestActualPostion();
    }

    @Override
    public void onProviderDisabled(String s) {
        notifyNoGps();
    }

    public void showBackButton(boolean show) {

        if (show) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (!mToolBarNavigationListenerIsRegistered) {
                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
                mToolBarNavigationListenerIsRegistered = true;
            }

        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.setToolbarNavigationClickListener(null);
            mToolBarNavigationListenerIsRegistered = false;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_checkin) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, new AgendamentosList()).commit();

        } else if (id == R.id.nav_estadio) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, new FragmentEstadiosList()).commit();

        } else if (id == R.id.nav_dicas) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, new DicasInstrucoesList()).commit();

        } else if (id == R.id.nav_bug) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, new DoencaPragaList()).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            tellFragments();
        }
    }

    private void tellFragments() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f : fragments) {
            if (f != null && f instanceof BaseFragment)
                ((BaseFragment) f).onBackPressed();
        }
    }
    */
}
