package com.hfad.myfilmsearcher;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

public class BottomNavigationDrawerFragment extends BottomSheetDialogFragment {
    public static final String TAG = BottomNavigationDrawerFragment.class.getSimpleName();

    private static BottomNavigationDrawerFragment bottomNavigationDrawerFragment = null;

    static BottomNavigationDrawerFragment newInstance() {
        bottomNavigationDrawerFragment = new BottomNavigationDrawerFragment();
        return bottomNavigationDrawerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.navigation_drawer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView closeView = view.findViewById(R.id.navigation_close_view);
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationDrawerFragment.dismiss();
            }
        });

        final NavigationView navigationView = view.findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new FilmsFragment(), FilmsFragment.TAG)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();
                        bottomNavigationDrawerFragment.dismiss();
                        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() != 0) {
                            MainActivity activity = (MainActivity) getActivity();
                            activity.switchFabButton();
                        }
                        break;
                    case R.id.nav_exit:
                        new AlertDialog.Builder(getContext())
                                .setTitle("Выход")
                                .setMessage("Вы действительно хотите выйти из приложения?")
                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getActivity().finish();
                                    }
                                })
                                .setNegativeButton("Нет", null)
                                .show();
                }
                return true;
            }
        });
    }
}
