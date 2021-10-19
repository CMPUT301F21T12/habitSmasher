package com.example.habitsmasher.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class HabitItemAdapter extends RecyclerView.Adapter<HabitItemAdapter.HabitViewHolder> {
    private static final String DATE_PATTERN = "EEE, MMM d, yyyy";
    private static final Locale LOCALE = Locale.CANADA;

    private final Context _context;
    private static ArrayList<Habit> _habits;
    private final FragmentActivity _activity;

    public HabitItemAdapter(Context context, ArrayList<Habit> habits, FragmentActivity activity) {
        _context = context;
        _habits = habits;
        _activity = activity;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_context).inflate(R.layout.habit_row, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit currentHabit = _habits.get(position);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN, LOCALE);

        holder._habitTitle.setText(currentHabit.getTitle());
        holder._habitReason.setText(currentHabit.getReason());
        holder._habitDate.setText(simpleDateFormat.format(currentHabit.getDate()));
        holder._habitImage.setImageResource(R.drawable.habit_temp_img);

        setOnClickListenerForHabit(holder);
    }

    private void setOnClickListenerForHabit(@NonNull HabitViewHolder holder) {
        holder._habitRows.setOnClickListener(view -> openHabitView(holder));
    }

    /**
     * This function opens the habit view
     * @param holder
     * This holds the values for the selected habit item
     */
    private void openHabitView(HabitViewHolder holder) {
        // Create Habit View Fragment with all required parameters passed in
        HabitViewFragment fragment = HabitViewFragment.newInstance(holder._habitTitle.getText().toString(), holder._habitReason.getText().toString(), holder._habitDate.getText().toString());
        // Replace the current fragment with the habit view
        FragmentTransaction transaction = _activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, fragment);
        transaction.addToBackStack(null);
        // Load new fragment
        transaction.commit();
    }

    @Override
    public int getItemCount() {
        return _habits.size();
    }

    public static class HabitViewHolder extends RecyclerView.ViewHolder {
        private final TextView _habitTitle;
        private final TextView _habitReason;
        private final TextView _habitDate;
        private final ImageView _habitImage;
        private final ConstraintLayout _habitRows;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);

            _habitRows = itemView.findViewById(R.id.habit_rows);
            _habitTitle = itemView.findViewById(R.id.habit_title);
            _habitReason = itemView.findViewById(R.id.habit_reason);
            _habitDate = itemView.findViewById(R.id.habit_date);
            _habitImage = itemView.findViewById(R.id.habit_image);
        }
    }
}
