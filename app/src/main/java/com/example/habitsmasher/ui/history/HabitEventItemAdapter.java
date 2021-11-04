package com.example.habitsmasher.ui.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitEvent;
import com.example.habitsmasher.HabitEventList;
import com.example.habitsmasher.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;

import java.text.SimpleDateFormat;

/**
 * The HabitEventItemAdapter class
 * Based on HabitItemAdapter, extension of FirestoreRecycler that shows habit events list with live updates
 */
public class HabitEventItemAdapter extends FirestoreRecyclerAdapter<HabitEvent, HabitEventItemAdapter.HabitEventViewHolder> {
    // Initialize variables
    private Context _context;
    private final String DATE_FORMAT = "dd/MM/yyyy";
    public final ObservableSnapshotArray<HabitEvent> _snapshots;
    private static Habit _parentHabit;
    private static String _username;
    private static HabitEventList _habitEvents;
    private static HabitEventListFragment _habitEventListFragment;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options the firestore entities
     */
    public HabitEventItemAdapter(@NonNull FirestoreRecyclerOptions<HabitEvent> options,
                                 Habit parentHabit,
                                 String username,
                                 HabitEventList habitEvents,
                                 HabitEventListFragment fragment) {
        super(options);
        _snapshots = options.getSnapshots();
        _parentHabit = parentHabit;
        _username = username;
        _habitEvents = habitEvents;
        _habitEventListFragment = fragment;
    }

    @NonNull
    @Override
    public HabitEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate view based on HabitEventViewHolder
        _context = parent.getContext();
        View view = LayoutInflater.from(_context).inflate(R.layout.habit_event_row, parent, false);
        return new HabitEventViewHolder(view, _snapshots,_username, _parentHabit, _context);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitEventViewHolder holder, int position, @NonNull HabitEvent habitEvent) {
        // Set UI elements of habit event
        holder._habitEventDate.setText(new SimpleDateFormat(DATE_FORMAT).format(habitEvent.getDate()));
        holder._habitEventComment.setText(habitEvent.getComment());
        holder._habitEventDateWithButtons.setText(new SimpleDateFormat(DATE_FORMAT).format(habitEvent.getDate()));
        holder._get_habitEventCommentWithButtons.setText(habitEvent.getComment());
        // TODO: Implement image setting too
    }

    /**
     * Necessary function for firestore recycler view
     * @return number of elements in snapshots
     */
    @Override
    public int getItemCount() { return _snapshots.size(); }

    /**
     * This class holds any necessary elements of the habit event on the front-end
     */
    public static class HabitEventViewHolder extends RecyclerView.ViewHolder {
        // List of UI elements
        private final TextView _habitEventDate;
        private final TextView _habitEventComment;
        private final TextView _habitEventDateWithButtons;
        private final TextView _get_habitEventCommentWithButtons;
        private final Button _editHabitEventButton;
        private final Button _deleteHabitEventButton;
        private final ConstraintLayout _layoutWithoutButtons;
        private final ConstraintLayout _layoutWithButtons;
        private HabitEventViewHolder _habitEventViewHolder = this;

        // private final ImageView _habitEventImage;

        /**
         * Default constructor
         * @param itemView (View): The habit event view
         */
        public HabitEventViewHolder(@NonNull View itemView,
                                    ObservableSnapshotArray<HabitEvent> _snapshots,
                                    String username,
                                    Habit parentHabit,
                                    Context context) {
            super(itemView);

            // Connect UI elements
            _habitEventDate = itemView.findViewById(R.id.habit_event_date);
            _habitEventComment = itemView.findViewById(R.id.habit_event_comment);
            _habitEventDateWithButtons = itemView.findViewById(R.id.habit_event_date_with_buttons);
            _get_habitEventCommentWithButtons = itemView.findViewById(R.id.habit_event_comment_with_buttons);
            _editHabitEventButton = itemView.findViewById(R.id.edit_habit_event_button);
            _deleteHabitEventButton = itemView.findViewById(R.id.delete_habit_event_button);
            _layoutWithButtons = itemView.findViewById(R.id.habit_event_row_button_view);
            _layoutWithoutButtons = itemView.findViewById(R.id.habit_event_row_normal_view);
            // _habitEventImage = itemView.findViewById(R.id.habit_event_image);

            // Add listener to delete button
            _deleteHabitEventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get habit event for which button was clicked
                    int eventPosition = getAdapterPosition();
                    HabitEvent toDelete = _snapshots.get(eventPosition);

                    // Delete the habit event
                    _habitEvents.deleteHabitEvent(context, username, parentHabit, toDelete);
                }
            });

            // Add listener to edit button
            _editHabitEventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get position of click, and corresponding habit event
                    int eventPosition = getAdapterPosition();
                    HabitEvent toEdit = _snapshots.get(eventPosition);

                    // Open edit habit event dialog
                    EditHabitEventFragment editHabitEventFragment = new EditHabitEventFragment(eventPosition,
                                                                            toEdit,
                                                                            _habitEventListFragment,
                                                                            _habitEventViewHolder);
                    editHabitEventFragment.show(_habitEventListFragment.getFragmentManager(), "Edit Habit Event");
                }
            });
        }

        /**
         * Sets view with buttons as visible
         */
        public void setButtonView() {
            _layoutWithoutButtons.setVisibility(View.INVISIBLE);
            _layoutWithButtons.setVisibility(View.VISIBLE);
        }

        /**
         * Sets view without buttons as visible
         */
        public void setNoButtonView() {
            _layoutWithButtons.setVisibility(View.INVISIBLE);
            _layoutWithoutButtons.setVisibility(View.VISIBLE);
        }
    }

}
