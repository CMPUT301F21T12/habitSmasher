package com.example.habitsmasher.ui.home;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.R;
import com.example.habitsmasher.databinding.FragmentHomeBinding;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        // This is the example habit to be displayed
        Habit testHabit = new Habit("This is a title", "This is my reason", new Date());

        // Getting the corresponding textviews in the fragment_home.xml
        TextView titleView = binding.habitTitle;
        TextView reasonView = binding.habitReason;
        TextView dateView = binding.habitDate;

        // Setting the pattern for the date
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.CANADA);

        // Updating the text fields of the textviews
        titleView.setText(testHabit.get_title());
        reasonView.setText(testHabit.get_reason());
        dateView.setText(simpleDateFormat.format(testHabit.get_date()));


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}