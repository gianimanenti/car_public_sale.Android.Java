package com.moondu.leilao.view.fragment.checkout;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moondu.leilao.R;
import com.moondu.leilao.commons.utils.RecyclerItemListener;
import com.moondu.leilao.model.adapter.ServiceOrderAdapter;
import com.moondu.leilao.model.entity.ServiceOrder;
import com.moondu.leilao.view.activity.Home;
import com.moondu.leilao.view.fragment.base.BaseFragment;
import com.moondu.leilao.view.fragment.checkin.CarID;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.moondu.leilao.model.firebase.FirebaseHelper.getAuth;
import static com.moondu.leilao.model.firebase.FirebaseHelper.getFirebase;

public class CheckOut extends BaseFragment {

    private RecyclerView rv;
    private LinearLayout llNotFound;
    private RelativeLayout rlItensListagem;
    private SwipeRefreshLayout swRefreshLayoutAll;

    private List<ServiceOrder> itens;

    private ServiceOrderAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.service_orders, container, false);

        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.menu_checkout);

        ((Home) getActivity()).showBackButton(false);

        BaseFragment.lastFragment = null;
        BaseFragment.position = null;
        BaseFragment.serviceOrder = null;

        rv = v.findViewById(R.id.rv_itens);
        llNotFound = v.findViewById(R.id.ln_not_fount_data);
        rlItensListagem = v.findViewById(R.id.rl_vr_itens);
        swRefreshLayoutAll = v.findViewById(R.id.swipeRefresh);

        LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        rv.setLongClickable(false);

        rv.addOnItemTouchListener(new RecyclerItemListener(v.getContext(), rv,
                new RecyclerItemListener.RecyclerTouchListener() {

                    public void onClickItem(View v, int position) {
                        final ServiceOrder item = itens.get(position);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ServiceOrder.class.getName(), item);
                        bundle.putInt(BaseFragment.INDEX, position);

                        Summary fragment = new Summary();
                        fragment.setArguments(bundle);

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                    }

                    @Override
                    public void onLongClickItem(View v, int position) {
                        //nothing
                    }
                }));

        swRefreshLayoutAll.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();

                swRefreshLayoutAll.setRefreshing(false);
            }
        });

        swRefreshLayoutAll.setRefreshing(false);

        refresh();

        return v;
    }

    private void refresh() {
        final String uid = Objects.requireNonNull(getAuth().getCurrentUser()).getUid();

        Query query = getFirebase().child("service_orders").child(uid);

        itens = new ArrayList<>();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        itens.add(d.getValue(ServiceOrder.class));
                    }
                }

                reload();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DadoUsuario", databaseError.getMessage().toString());
            }
        });
    }

    public void reload() {
        if (itens != null && itens.size() > 0) {
            rlItensListagem.setVisibility(View.VISIBLE);
            llNotFound.setVisibility(View.GONE);

            if (adapter == null)
                adapter = new ServiceOrderAdapter(itens);

            rv.setAdapter(adapter);
        } else {
            rlItensListagem.setVisibility(View.GONE);
            llNotFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!menu.hasVisibleItems()) {
            inflater.inflate(R.menu.menu_new_service_order, menu);

            View view = Home.toolbar.getMenu().findItem(R.id.newOrder).getActionView();

            view.findViewById(R.id.newOrderBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Home) getActivity()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_container,
                                    new CarID()).addToBackStack(null).commit();
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
