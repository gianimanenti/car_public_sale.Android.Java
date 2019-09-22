package com.moondu.leilao.view.fragment.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.moondu.leilao.model.entity.ServiceOrder;

public abstract class BaseFragment extends Fragment implements OnBackPressed {

    public static final String INDEX = "index";

    private boolean inflateMenu;

    public static ServiceOrder serviceOrder;
    public static Class<Fragment> lastFragment;
    public static Integer position;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void onBackPressed() {
    }
}
