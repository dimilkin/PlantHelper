package com.m.plantkeeper.ui;

import static com.m.plantkeeper.Constants.EXTRA_PLANT_INFO_BODY;
import static com.m.plantkeeper.Constants.EXTRA_PLANT_INFO_TITLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.m.plantkeeper.R;

public class ShortInfoFragment extends Fragment {

    private ImageButton closeButton;
    private TextView titleText, contentText;

    public ShortInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_short_info, container, false);
        initializeView(view);
        view.setBackground(getResources().getDrawable(R.drawable.background_plant, getActivity().getTheme()));
        Bundle bundle = getArguments();
        if (bundle != null) {
            titleText.setText(bundle.getString(EXTRA_PLANT_INFO_TITLE));
            contentText.setText(bundle.getString(EXTRA_PLANT_INFO_BODY));
        }
        closeButton.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        titleText.clearComposingText();
        contentText.clearComposingText();
    }

    private void initializeView(View view) {
        closeButton = view.findViewById(R.id.shortInfoCloseButton);
        titleText = view.findViewById(R.id.shortInfoTitle);
        contentText = view.findViewById(R.id.shortInfoContent);
    }
}