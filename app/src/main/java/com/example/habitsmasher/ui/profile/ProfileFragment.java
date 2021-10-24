package com.example.habitsmasher.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.habitsmasher.R;

public class ProfileFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView username = view.findViewById(R.id.username);
        TextView followers = view.findViewById(R.id.number_followers);
        TextView following = view.findViewById(R.id.number_following);

        username.setText("@yourUsername");
        followers.setText("0");
        following.setText("0");

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}