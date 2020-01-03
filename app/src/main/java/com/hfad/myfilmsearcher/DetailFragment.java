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

import com.bumptech.glide.Glide;

public class DetailFragment extends Fragment {

    public static final String TAG = DetailFragment.class.getSimpleName();

    private static String EXTRTA_DESCRIPTION = "description";
    private static String EXTRA_IMAGEID = "imageId";

    private String description;
    private String imageUrl;

    public static DetailFragment newInstance(String imageUrl, String description) {
        DetailFragment fragment = new DetailFragment();

        Bundle bundle = new Bundle();
        bundle.putString(EXTRTA_DESCRIPTION, description);
        bundle.putString(EXTRA_IMAGEID, imageUrl);
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
            imageUrl = getArguments().getString(EXTRA_IMAGEID);
        }

        ((TextView) view.findViewById(R.id.textView2)).setText(description);

        ImageView imageView = (ImageView) view.findViewById(R.id.img_detail_photo);
        Glide.with(getContext())
                .load(imageUrl)
                .into(imageView);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).switchFabButton();
    }

}
