package com.example.habitsmasher.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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

    public HabitItemAdapter(Context context, ArrayList<Habit> habits) {
        _context = context;
        _habits = habits;
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

        setOnClickListenerForHabit(holder, position);
    }

    private void setOnClickListenerForHabit(@NonNull HabitViewHolder holder, int position) {
        holder._habitRows.setOnClickListener(view -> {
            // placeholder, just displays a message to indicate the habit has been clicked
            Toast.makeText(_context, _habits.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        });
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
        private final Button _editButton;
        private final Button _deleteButton;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);

            _habitRows = itemView.findViewById(R.id.habit_rows);
            _habitTitle = itemView.findViewById(R.id.habit_title);
            _habitReason = itemView.findViewById(R.id.habit_reason);
            _habitDate = itemView.findViewById(R.id.habit_date);
            _habitImage = itemView.findViewById(R.id.habit_image);
            _editButton = itemView.findViewById(R.id.edit_button);
            _deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
