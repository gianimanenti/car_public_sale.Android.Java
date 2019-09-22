package com.moondu.leilao.view.fragment.checkin;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moondu.leilao.R;
import com.moondu.leilao.commons.watcher.CPFWatcher;
import com.moondu.leilao.commons.watcher.PhoneWhatcher;
import com.moondu.leilao.model.entity.ServiceOrder;
import com.moondu.leilao.model.ws.Cep;
import com.moondu.leilao.model.ws.services.Config;
import com.moondu.leilao.view.activity.Home;
import com.moondu.leilao.view.fragment.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.moondu.leilao.model.firebase.FirebaseHelper.getAuth;
import static com.moondu.leilao.model.firebase.FirebaseHelper.getFirebase;

public class DriverID extends BaseFragment {

    private ServiceOrder item;
    private Integer position;

    private EditText ipNomeCondutor;
    private EditText ipRG;
    private EditText ipCPF;
    private EditText ipCNH;
    private EditText ipEndereco;
    private EditText ipEmail;
    private EditText ipTelefone;

    private ImageView ivEndereco;

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
        View v = inflater.inflate(R.layout.driver_id, container, false);

        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.menu_driver);

        ((Home) getActivity()).showBackButton(true);

        init(v);
        fillForm();

        return v;
    }

    private void searchAddress() {
        final Dialog dialog = new Dialog(getContext(), R.style.DialogNoActionBarMinWidth);
        dialog.setContentView(R.layout.popup_search_address);
        dialog.setCancelable(true);

        SearchView searchView = dialog.findViewById(R.id.searchView);
        final ListView listView = dialog.findViewById(R.id.listView);

        searchView.setIconified(false);
        searchView.setQuery("", true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String filter = formatFilter(query);

                if (filter != null && filter.length() > 3) {
                    searchItem(filter, listView);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String filter = formatFilter(newText);

                if (filter != null && filter.length() > 3) {
                    searchItem(filter, listView);
                }

                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != -1) {
                    ipEndereco.setText((String) adapterView.getItemAtPosition(i));

                    dialog.hide();
                }
            }
        });

        dialog.show();
    }

    private void searchItem(String filter, final ListView listView) {
        new Config().cepService().list(filter).enqueue(new Callback<List<Cep>>() {
            @Override
            public void onResponse(Call<List<Cep>> call, Response<List<Cep>> response) {
                List<String> itens = new ArrayList<String>();

                if (response.body() != null) {
                    for (Cep cep : response.body()) {
                        itens.add(format(cep));
                    }
                }

                listView.setAdapter(new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, itens));
            }

            @Override
            public void onFailure(Call<List<Cep>> call, Throwable t) {
                System.out.println("");
            }
        });
    }

    private boolean verifyIsConnected() {
        ConnectivityManager conectivtyManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }

    private String formatFilter(String value) {
        if (value != null) {
            value = value.trim();
            value = value.replace(" ", "%20");

            return value;
        }

        return "";
    }

    private String format(Cep cep) {
        StringBuilder sb = new StringBuilder();

        if (cep.getLogradouro() != null) {
            sb.append(cep.getLogradouro());

            if (cep.getNumero() != null) {
                sb.append(", ");
                sb.append(cep.getNumero());
            }
        }

        if (cep.getBairro() != null) {
            sb.append(", ");
            sb.append(cep.getBairro());
        }

        if (cep.getLocalidade() != null) {
            sb.append(" - ");
            sb.append(cep.getLocalidade());

            if (cep.getUf() != null) {
                sb.append("/");
                sb.append(cep.getUf());
            }
        }

        return sb.toString();
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ServiceOrder.class.getSimpleName(), item);
        bundle.putInt(BaseFragment.INDEX, position);

        CarID fragment = new CarID();
        fragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, new CarID()).addToBackStack(null).commit();
    }

    private void fillForm() {
        ipNomeCondutor.setText(item.getDriver() != null ? item.getDriver() : "");
        ipRG.setText(item.getRg() != null ? item.getRg() : "");
        ipCPF.setText(item.getCpf() != null ? item.getCpf() : "");
        ipCNH.setText(item.getCnh() != null ? item.getCnh() : "");
        ipEndereco.setText(item.getDriverAddress() != null ? item.getDriverAddress() : "");
        ipEmail.setText(item.getMail() != null ? item.getMail() : "");
        ipTelefone.setText(item.getPhone() != null ? item.getPhone() : "");

        ipEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchAddress();
            }
        });

        ivEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchAddress();
            }
        });

        if (verifyIsConnected()) {
            ivEndereco.setVisibility(View.VISIBLE);
        } else {
            ivEndereco.setVisibility(View.GONE);
        }

        ipCNH.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        ipCPF.addTextChangedListener(new CPFWatcher(ipCPF));
        ipTelefone.addTextChangedListener(new PhoneWhatcher(ipTelefone));
    }

    private void init(View v) {
        ipNomeCondutor = v.findViewById(R.id.ipNomeCondutor);
        ipRG = v.findViewById(R.id.ipRG);
        ipCPF = v.findViewById(R.id.ipCPF);
        ipCNH = v.findViewById(R.id.ipCNH);
        ipEndereco = v.findViewById(R.id.ipEndereco);
        ivEndereco = v.findViewById(R.id.ivEndereco);
        ipEmail = v.findViewById(R.id.ipEmail);
        ipTelefone = v.findViewById(R.id.ipTelefone);
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
            item.setDriver(ipNomeCondutor.getText().toString());
            item.setRg(ipRG.getText().toString());
            item.setCpf(ipCPF.getText().toString());
            item.setCnh(ipCNH.getText().toString());
            item.setDriverAddress(ipEndereco.getText().toString());
            item.setMail(ipEmail.getText().toString());
            item.setPhone(ipTelefone.getText().toString());

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

                    CarEvent fragment = new CarEvent();
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

    private boolean isValid() {
        boolean bCondutor = false;
        boolean bRg = false;
        boolean bCpf = false;
        boolean bCnh = false;
        boolean bEndereco = false;
        boolean bEmail = false;
        boolean bTelefone = false;

        if (ipNomeCondutor.getText() != null && ipNomeCondutor.getText().toString().length() > 0) {
            bCondutor = true;
        } else {
            ipNomeCondutor.setError("Campo obrigatório!");
            bCondutor = false;
        }

        if (ipRG.getText() != null && ipRG.getText().toString().length() > 0) {
            bRg = true;
        } else {
            ipRG.setError("Campo obrigatório!");
            bRg = false;
        }

        if (ipCPF.getText() != null && ipCPF.getText().toString().length() > 0) {
            if (isValidCPF(ipCPF.getText().toString())) {
                bCpf = true;
            } else {
                ipCPF.setError("CPF Inválido!");
                bCpf = false;
            }
        } else {
            ipCPF.setError("Campo obrigatório!");
            bCpf = false;
        }

        if (ipCNH.getText() != null && ipCNH.getText().toString().length() > 0) {
            bCnh = true;
        } else {
            ipCNH.setError("Campo obrigatório!");
            bCnh = false;
        }

        if (ipEndereco.getText() != null && ipEndereco.getText().toString().length() > 0) {
            bEndereco = true;
        } else {
            ipEndereco.setError("Campo obrigatório!");
            bEndereco = false;
        }

        if (ipEmail.getText() != null && ipEmail.getText().toString().length() > 0) {
            if (isValidMail(ipEmail.getText().toString())) {
                bEmail = true;
            } else {
                ipEmail.setError("E-mail Inválido!");
                bEmail = false;
            }
        } else {
            ipEmail.setError("Campo obrigatório!");
            bEmail = false;
        }

        if (ipTelefone.getText() != null && ipTelefone.getText().toString().length() > 0) {
            bTelefone = true;
        } else {
            ipTelefone.setError("Campo obrigatório!");
            bTelefone = false;
        }

        return bCondutor && bRg && bCpf && bCnh && bEndereco && bTelefone && bEmail;
    }

    private boolean isValidMail(String value) {
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    private boolean isValidCPF(String value) {
        int d1, d2;
        int digito1, digito2, resto;
        int digitoCPF;
        String nDigResult;

        value = value.replaceAll("[^0-9]*", "");

        d1 = d2 = 0;
        digito1 = digito2 = resto = 0;

        for (int nCount = 1; nCount < value.length() - 1; nCount++) {
            digitoCPF = Integer.valueOf(value.substring(nCount - 1, nCount)).intValue();

            //multiplique a ultima casa por 2 a seguinte por 3 a seguinte por 4 e assim por diante.
            d1 = d1 + (11 - nCount) * digitoCPF;

            //para o segundo digito repita o procedimento incluindo o primeiro digito calculado no passo anterior.
            d2 = d2 + (12 - nCount) * digitoCPF;
        }
        ;

        //Primeiro resto da divisão por 11.
        resto = (d1 % 11);

        //Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.
        if (resto < 2)
            digito1 = 0;
        else
            digito1 = 11 - resto;

        d2 += 2 * digito1;

        //Segundo resto da divisão por 11.
        resto = (d2 % 11);

        //Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.
        if (resto < 2)
            digito2 = 0;
        else
            digito2 = 11 - resto;

        //Digito verificador do CPF que está sendo validado.
        String nDigVerific = value.substring(value.length() - 2, value.length());

        //Concatenando o primeiro resto com o segundo.
        nDigResult = String.valueOf(digito1) + String.valueOf(digito2);

        //comparar o digito verificador do cpf com o primeiro resto + o segundo resto.
        return nDigVerific.equals(nDigResult);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
