package com.example.habitsmasher.ui.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitsmasher.HabitEvent;
import com.example.habitsmasher.HabitEventList;
import com.example.habitsmasher.R;

public class HabitEventItemAdapter extends RecyclerView.Adapter<HabitEventItemAdapter.HabitEventViewHolder> {
    private final Context _context;
    private static HabitEventList _habitEvents;

    /**
     * Default constructor
     * @param context (Context): The context of the parent initializing the adapter
     * @param habitEvents (HabitEventList): The list of habit events
     */
    public HabitEventItemAdapter(Context context, HabitEventList habitEvents) {
        _context = context;
        _habitEvents = habitEvents;
    }

    @NonNull
    @Override
    public HabitEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_context).inflate(R.layout.habit_event_row, parent, false);
        return new HabitEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitEventViewHolder holder, int position) {
        // Get current habit event
        HabitEvent currentHabitEvent = _habitEvents.getHabitEvents().get(position);

        // Set UI elements of habit event
        holder._habitEventDate.setText(currentHabitEvent.getStartDate().toString());
        holder._habitEventComment.setText(currentHabitEvent.getComment());

        // TODO: Implement image setting too
        // TODO: Set on click listener
    }

    @Override
    public int getItemCount() { return _habitEvents.getHabitEvents().size(); }

    /**
     * This class holds any necessary elements of the habit event on the front-end
     */
    public static class HabitEventViewHolder extends RecyclerView.ViewHolder {
        // List of UI elements
        private final TextView _habitEventDate;
        private final TextView _habitEventComment;
        // private final ImageView _habitEventImage;
        // private final ConstraintLayout _habitEventRow;

        /**
         * Default constructor
         * @param itemView (View): The habit event view
         */
        public HabitEventViewHolder(@NonNull View itemView) {
            super(itemView);

            _habitEventDate = itemView.findViewById(R.id.habit_event_date);
            _habitEventComment = itemView.findViewById(R.id.habit_event_comment);
            // _habitEventImage = itemView.findViewById(R.id.habit_event_image);
            // _habitEventRow = itemView.findViewById(R.id.habit_event_row);
        }
    }

}
