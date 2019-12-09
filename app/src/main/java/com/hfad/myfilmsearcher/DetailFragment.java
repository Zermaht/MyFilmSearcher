package com.hfad.myfilmsearcher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetailFragment extends Fragment {

    public static final String TAG = DetailFragment.class.getSimpleName();

    private static String EXTRTA_DESCRIPTION = "description";
    private static String EXTRA_IMAGEID = "imageId";

    private String description;
    private int imageId;

    public static DetailFragment newInstance(int imageId, String description) {
        DetailFragment fragment = new DetailFragment();

        Bundle bundle = new Bundle();
        bundle.putString(EXTRTA_DESCRIPTION, description);
        bundle.putInt(EXTRA_IMAGEID, imageId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            description = getArguments().getString(EXTRTA_DESCRIPTION);
            imageId = getArguments().getInt(EXTRA_IMAGEID);
        }

        ((TextView) view.findViewById(R.id.textView2)).setText(description);
        ((ImageView) view.findViewById(R.id.img_detail_photo)).setImageResource(imageId);

    }

}
