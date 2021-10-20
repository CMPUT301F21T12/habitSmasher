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
import com.example.habitsmasher.HabitList;
import com.example.habitsmasher.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class HabitItemAdapter extends RecyclerView.Adapter<HabitItemAdapter.HabitViewHolder> {
    private static final String DATE_PATTERN = "dd-MM-yyyy";
    private static final Locale LOCALE = Locale.CANADA;

    private final Context _context;
    private static HabitList _habits;
    private static HabitListFragment _habitListFragment;
    public HabitItemAdapter(Context context, HabitList habits, HabitListFragment fragment) {
        _context = context;
        _habits = habits;
        _habitListFragment = fragment;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_context).inflate(R.layout.habit_row, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        //Habit currentHabit = _habits.get(position);
        Habit currentHabit = _habits.getHabitList().get(position);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN, LOCALE);

        holder._habitTitle.setText(currentHabit.getTitle());

        setOnClickListenerForHabit(holder, position);
    }

    private void setOnClickListenerForHabit(@NonNull HabitViewHolder holder, int position) {
        holder._habitRows.setOnClickListener(view -> {
            // placeholder, just displays a message to indicate the habit has been clicked
            Toast.makeText(_context, _habits.getHabitList().get(position).getTitle(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return _habits.getHabitList().size();
    }

    public static class HabitViewHolder extends RecyclerView.ViewHolder {
        private final TextView _habitTitle;
        private final ConstraintLayout _habitRows;
        private Button _editButton;
        private Button _deleteButton;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);

            _habitRows = itemView.findViewById(R.id.habit_rows);
            _habitTitle = itemView.findViewById(R.id.habit_title);
            _editButton = itemView.findViewById(R.id.edit_button);
            _deleteButton = itemView.findViewById(R.id.delete_button);

            _editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int buttonPos = getAdapterPosition();
                    EditHabitFragment editHabitFragment = new EditHabitFragment(buttonPos,
                            _habits.getHabitList().get(buttonPos), _habitListFragment);
                    editHabitFragment.show(_habitListFragment.getFragmentManager(), "Edit Habit");
                }
            });

            _deleteButton.setOnClickListener(new View.OnClickListener() {
                int buttonPos = getAdapterPosition();
                @Override
                public void onClick(View v) {
                    /*
                    Delete logic goes here
                     */
                }
            });

        }
    }
}
