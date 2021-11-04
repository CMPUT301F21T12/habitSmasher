package com.example.habitsmasher.ui.dashboard;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.ListView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Listener class that handles touch interactions with a RecyclerView
 * Implementation of this class came from this source:
 * Name: Velmurugan
 * Date: March 4, 2021
 * URL: https://howtodoandroid.com/android-recyclerview-swipe-menu
 */
public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener, OnActivityTouchListener {

    // defined constants
    private static final String TAG = "RecyclerTouchListener";
    private final long ANIMATION_STANDARD = 300;
    private final long ANIMATION_CLOSE = 150;

    // parent activity of RecyclerView
    Activity _activity;

    /*
     * Option views are the views on the background/swipe menu
     * layer which acts as the different clickable options
     * when swipe menu spawned
     */
    List<Integer> _swipeMenuOptions;

    // amount finger must move in units before swipe is detected
    private int _touchSlop;

    // correction factor used in calculate translation from swipes
    private int _swipingSlop;

    // minimum and maximum velocity for proper swiping action
    private int _minSwipeVelocity;
    private int _maxSwipeVelocity;

    // velocity tracker used calculate velocity of swipe
    private VelocityTracker _swipeVelocityTracker;

    // recycler view that this TouchListener is listening for
    private RecyclerView _recyclerView;

    // x and y coordinate of the current touch
    private float _touchedX;
    private float _touchedY;

    // view and position of the view touched at the moment
    private int _touchedPosition;
    private View _touchedView;

    // flag variable that indicates that row currently in the act of being swiped
    private boolean _foregroundSwiping;

    // flag variable that indicates that all interactions are to be ignored
    private boolean _interactionPaused;

    // flag variable indicating if RecyclerView is being scrolled through
    private boolean _recyclerViewScrolling;

    // flag variable indicting whether swipe menu spawned and visible
    private boolean _backgroundVisible;

    // position of row in RecyclerView with visible swipe menu
    private int _backgroundVisiblePosition;

    // view holding current spawned visible swipe menu
    private View _backgroundVisibleView;

    // flag variable indicating that the foreground view which is partially on the screen is clicked
    private boolean _foregroundPartialViewClicked;

    // size properties
    private int _heightOutsideView;
    private int _heightOfScreen;
    private int _backgroundWidth = 1;

    // Foreground view (to be swiped), Background view (to show)
    private View _foregroundView;
    private View _backgroundView;

    //view IDs of foreground and background/swipe menu
    private int _foregroundID;
    private int _backgroundID;

    //listeners for clicking a row and clicking an option in swipe menu
    private OnRowClickListener _RowClickListener;
    private OnSwipeOptionsClickListener _swipeMenuOptionsClickListener;

    //flag variables indicating that RecyclerView is clickable or swipeable
    private boolean _clickable = false;
    private boolean _swipeable = false;

    /**
     * Interface used for listener handling clicking of row
     */
    public interface OnRowClickListener {
        /**
         * Action carried out when row at the specified position is clicked in Recycler View
         * @param position position of row
         */
        void onRowClicked(int position);
    }

    /**
     * Listener used to handle the clicking of the specific clickable
     * option views in swipe menu
     */
    public interface OnSwipeOptionsClickListener {

        /**
         * Actions carried out when option with the id viewID is clicked
         * when the row at the specified position is clicked
         * @param viewID view ID of option view
         * @param position position of row
         */
        void onSwipeOptionClicked(int viewID, int position);
    }

    /**
     * Listener used to handle when a row is swiped to spawn/despawn
     * the swipe menu, spawning clickable options
     */
    public interface OnSwipeListener {

        /**
         * Action carried out when swipe menu is closed
         */
        void onSwipeOptionsClosed();

        /**
         * Action carried out when swipe menu is open
         */
        void onSwipeOptionsOpened();
    }

    /**
     * Enumeration of open and close animations
     */
    private enum Animation {
        OPEN, CLOSE
    }

    /**
     * Constructs the TouchListener for a certain recyclerView
     * @param activity activity where recycler view resides
     * @param recyclerView recycler to be connected to listener
     */
    public RecyclerTouchListener(Activity activity, RecyclerView recyclerView) {
        _activity = activity;
        _recyclerView = recyclerView;
        _swipeMenuOptions = new ArrayList<>();

        ViewConfiguration vc = ViewConfiguration.get(recyclerView.getContext());
        _touchSlop = vc.getScaledTouchSlop();

        _minSwipeVelocity = vc.getScaledMinimumFlingVelocity() * 16;
        _maxSwipeVelocity = vc.getScaledMaximumFlingVelocity();

        _backgroundVisible = false;
        _foregroundPartialViewClicked = false;
        _recyclerViewScrolling = false;

        _backgroundVisibleView = null;
        _backgroundVisiblePosition = -1;

        _recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                /**
                 * This will ensure that this RecyclerTouchListener is paused during recycler view scrolling.
                 * If a scroll listener is already assigned, the caller should still pass scroll changes through
                 * to this listener.
                 */
                setEnabled(newState != RecyclerView.SCROLL_STATE_DRAGGING);

                /**
                 * This is used so that clicking a row cannot be done while scrolling
                 */
                _recyclerViewScrolling = newState != RecyclerView.SCROLL_STATE_IDLE;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }
        });
    }

    /**
     * Enables or disables (pauses or resumes) watching for swipe-to-dismiss gestures.
     * @param enabled Whether or not to watch for gestures.
     */
    public void setEnabled(boolean enabled) {
        _interactionPaused = !enabled;
    }

    /**
     * Allows elements of the recycler view to be clicked and
     * sets the listener used to handle row clicking
     * @param listener row click listener
     * @return itself
     */
    public RecyclerTouchListener setClickable(OnRowClickListener listener) {
        _clickable = true;
        this._RowClickListener = listener;
        return this;
    }

    /**
     * Allows elements of the recycler view to be swiped and
     * sets the listener used to handle clicking of options in swipe menu
     * @param listener row swipe options listener
     * @return itself
     */
    public RecyclerTouchListener setSwipeable(int foregroundID, int backgroundID, OnSwipeOptionsClickListener listener) {
        this._swipeable = true;
        if (_foregroundID != 0 && foregroundID != _foregroundID)
            throw new IllegalArgumentException("foregroundID does not match previously set ID");
        _foregroundID = foregroundID;
        _backgroundID = backgroundID;
        this._swipeMenuOptionsClickListener = listener;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        _activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        _heightOfScreen = displaymetrics.heightPixels;

        return this;
    }

    /**
     * Sets the clickable options in the swipe menu
     * @param viewIds set of view IDs of the clickable options
     * @return itself
     */
    public RecyclerTouchListener setSwipeOptionViews(Integer... viewIds) {
        this._swipeMenuOptions = new ArrayList<>(Arrays.asList(viewIds));
        return this;
    }

    /**
     * Returns the view ID of the clickable option that is clicked in an interaction
     * @param motionEvent motion event representing that interaction
     * @return view ID of clicked option, -1 if no option was clicked
     */
    private int getOptionViewID(MotionEvent motionEvent) {
        for (int i = 0; i < _swipeMenuOptions.size(); i++) {
            if (_touchedView != null) {
                Rect rect = new Rect();
                int x = (int) motionEvent.getRawX();
                int y = (int) motionEvent.getRawY();
                _touchedView.findViewById(_swipeMenuOptions.get(i)).getGlobalVisibleRect(rect);
                if (rect.contains(x, y)) {
                    return _swipeMenuOptions.get(i);
                }
            }
        }
        return -1;
    }

    /**
     * Closes the swipe menu
     * @param mSwipeCloseListener listener handling swipes
     */
    public void closeVisibleBG(final OnSwipeListener mSwipeCloseListener) {
        if (_backgroundVisibleView == null) {
            Log.e(TAG, "No rows found for which background options are visible");
            return;
        }
        final ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(_backgroundVisibleView,
                View.TRANSLATION_X, 0f);
        translateAnimator.setDuration(ANIMATION_CLOSE);
        translateAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mSwipeCloseListener != null)
                    mSwipeCloseListener.onSwipeOptionsClosed();
                translateAnimator.removeAllListeners();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        translateAnimator.start();

        _backgroundVisible = false;
        _backgroundVisibleView = null;
        _backgroundVisiblePosition = -1;
    }

    /**
     * Animates the foreground during a swipe animation
     * @param animateType type of animation
     * @param duration duration of animation
     */
    private void animateFG(Animation animateType, long duration) {

        // move foreground so options menu is OPEN
        if (animateType == Animation.OPEN) {
            ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(
                    _foregroundView, View.TRANSLATION_X, -_backgroundWidth);
            translateAnimator.setDuration(duration);
            translateAnimator.setInterpolator(new DecelerateInterpolator(1.5f));
            translateAnimator.start();

            // move foreground so options menu is CLOSED
        } else if (animateType == Animation.CLOSE) {
            ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(
                    _foregroundView, View.TRANSLATION_X, 0f);
            translateAnimator.setDuration(duration);
            translateAnimator.setInterpolator(new DecelerateInterpolator(1.5f));
            translateAnimator.start();
        }
    }

    /**
     * Animates the foreground during a swipe animation
     * @param animateType type of animation
     * @param duration duration of animation
     * @param mSwipeCloseListener listener used to handle swipes
     */
    private void animateFG(final Animation animateType, long duration,
                           final OnSwipeListener mSwipeCloseListener) {
        final ObjectAnimator translateAnimator;

        // move foreground so options menu is OPEN
        if (animateType == Animation.OPEN) {
            translateAnimator = ObjectAnimator.ofFloat(_foregroundView, View.TRANSLATION_X, -_backgroundWidth);
            translateAnimator.setDuration(duration);
            translateAnimator.setInterpolator(new DecelerateInterpolator(1.5f));
            translateAnimator.start();

            // move foreground so options menu is CLOSED
        } else {
            translateAnimator = ObjectAnimator.ofFloat(_foregroundView, View.TRANSLATION_X, 0f);
            translateAnimator.setDuration(duration);
            translateAnimator.setInterpolator(new DecelerateInterpolator(1.5f));
            translateAnimator.start();
        }

        translateAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mSwipeCloseListener != null) {
                    if (animateType == Animation.OPEN)
                        mSwipeCloseListener.onSwipeOptionsOpened();
                    else if (animateType == Animation.CLOSE)
                        mSwipeCloseListener.onSwipeOptionsClosed();
                }
                translateAnimator.removeAllListeners();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    /**
     * Checks if the touch screen interaction should be ignored or not
     * @return true if it should be ignored
     */
    private boolean shouldIgnoreAction() {
        return _recyclerView == null;
    }

    /**
     * Handles a touch interaction with the screen
     * @param motionEvent motion event representing the touch interaction with the screen
     * @return
     */
    private boolean handleTouchEvent(MotionEvent motionEvent) {

        if (_swipeable && _backgroundWidth < 2) {
            if (_activity.findViewById(_backgroundID) != null)
                _backgroundWidth = _activity.findViewById(_backgroundID).getWidth();
            _heightOutsideView = _heightOfScreen - _recyclerView.getHeight();
        }

        switch (motionEvent.getActionMasked()) {
            // when finger touches screen initially
            case MotionEvent.ACTION_DOWN: {

                // stop interaction if paused
                if (_interactionPaused) {
                    break;
                }

                Rect rect = new Rect();
                int childCount = _recyclerView.getChildCount();
                int[] listViewCoords = new int[2];
                _recyclerView.getLocationOnScreen(listViewCoords);

                int xOfRecyclerView = (int) motionEvent.getRawX() - listViewCoords[0];
                int yOfRecyclerView = (int) motionEvent.getRawY() - listViewCoords[1];
                View rowView;

                /*
                 * check every row in the recycler view whether the touched co-ordinates belong to that
                 * respective row and if it does, register that row as the touched view
                 */
                for (int i = 0; i < childCount; i++) {
                    rowView = _recyclerView.getChildAt(i);
                    rowView.getHitRect(rect);
                    if (rect.contains(xOfRecyclerView, yOfRecyclerView)) {
                        _touchedView = rowView;
                        break;
                    }
                }

                // if a row was touched
                if (_touchedView != null) {
                    _touchedX = motionEvent.getRawX();
                    _touchedY = motionEvent.getRawY();
                    _touchedPosition = _recyclerView.getChildAdapterPosition(_touchedView);

                    if (shouldIgnoreAction()) {
                        _touchedPosition = ListView.INVALID_POSITION;
                        return false;
                    }

                    // if swipes are allowed
                    if (_swipeable) {
                        _swipeVelocityTracker = VelocityTracker.obtain();
                        _swipeVelocityTracker.addMovement(motionEvent);
                        _foregroundView = _touchedView.findViewById(_foregroundID);
                        _backgroundView = _touchedView.findViewById(_backgroundID);
                        _backgroundView.setMinimumHeight(_foregroundView.getHeight());

                        /*
                         * backgroundVisible is true when the options menu is opened
                         * This block is to register foregroundPartialViewClicked status
                         *
                         * Partial view is the view that is still
                         * shown on the screen if the options width is < device width
                         */
                        if (_backgroundVisible && _foregroundView != null) {
                            int x = (int) motionEvent.getRawX();
                            int y = (int) motionEvent.getRawY();
                            _foregroundView.getGlobalVisibleRect(rect);
                            _foregroundPartialViewClicked = rect.contains(x, y);
                        } else {
                            _foregroundPartialViewClicked = false;
                        }
                    }
                }

                /*
                 * If options menu is shown and the touched position is not the same as the row for which the
                 * options is displayed - close the options menu for the row which is displaying it
                 * (backgroundVisibleView and backgroundVisiblePosition is used for this purpose which registers which view and
                 * which position has it's options menu opened)
                 */
                _recyclerView.getHitRect(rect);
                if (_swipeable && _backgroundVisible && _touchedPosition != _backgroundVisiblePosition) {
                    closeVisibleBG(null);
                }
                break;
            }

            // touch interaction is aborted
            case MotionEvent.ACTION_CANCEL: {
                if (_swipeVelocityTracker == null) {
                    break;
                }
                if (_swipeable) {

                    // when in middle of swiping
                    if (_touchedView != null && _foregroundSwiping) {
                        // cancel animation
                        animateFG(Animation.CLOSE, ANIMATION_STANDARD);
                    }
                    _swipeVelocityTracker.recycle();
                    _swipeVelocityTracker = null;
                    _foregroundSwiping = false;
                    _backgroundView = null;
                }
                _touchedX = 0;
                _touchedY = 0;
                _touchedView = null;
                _touchedPosition = ListView.INVALID_POSITION;
                break;
            }

            // When finger is lifted off the screen (after clicking, flinging, swiping, etc..)
            case MotionEvent.ACTION_UP: {

                if (_swipeVelocityTracker == null && _swipeable) {
                    break;
                }
                if (_touchedPosition < 0)
                    break;

                // swipedLeft and swipedRight are true if the user swipes in the respective direction (no conditions)
                boolean swipedLeft = false;
                boolean swipedRight = false;

                /*
                 * swipedLeftProper and swipedRightProper are true if user swipes in the respective direction
                 * and if certain conditions are satisfied (given some few lines below)
                 */
                boolean swipedLeftProper = false;
                boolean swipedRightProper = false;

                // final translation in x direction of movement
                float mFinalDelta = motionEvent.getRawX() - _touchedX;

                // if swiped in a direction, make that respective variable true
                if (_foregroundSwiping) {
                    swipedLeft = mFinalDelta < 0;
                    swipedRight = mFinalDelta > 0;
                }

                /*
                 * If the user has swiped more than half of the width of the options menu, or if the
                 * velocity of swiping is between min and max fling values
                 * "proper" variable are set true
                 */
                if (Math.abs(mFinalDelta) > _backgroundWidth / 2 && _foregroundSwiping) {
                    swipedLeftProper = mFinalDelta < 0;
                    swipedRightProper = mFinalDelta > 0;
                } else if (_swipeable) {
                    _swipeVelocityTracker.addMovement(motionEvent);
                    _swipeVelocityTracker.computeCurrentVelocity(1000);
                    float velocityX = _swipeVelocityTracker.getXVelocity();
                    float absVelocityX = Math.abs(velocityX);
                    float absVelocityY = Math.abs(_swipeVelocityTracker.getYVelocity());
                    if (_minSwipeVelocity <= absVelocityX && absVelocityX <= _maxSwipeVelocity
                            && absVelocityY < absVelocityX && _foregroundSwiping) {
                        // dismiss only if flinging in the same direction as dragging
                        swipedLeftProper = (velocityX < 0) == (mFinalDelta < 0);
                        swipedRightProper = (velocityX > 0) == (mFinalDelta > 0);
                    }
                }


                // if swiped left properly and options menu isn't already visible, animate the foreground to the left
                if (_swipeable && !swipedRight && swipedLeftProper && _touchedPosition != RecyclerView.NO_POSITION
                        && !_backgroundVisible) {

                    // open swipe menu
                    animateFG(Animation.OPEN, ANIMATION_STANDARD);
                    _backgroundVisible = true;
                    _backgroundVisibleView = _foregroundView;
                    _backgroundVisiblePosition = _touchedPosition;;
                }

                /*
                 * if swiped right properly when options menu is visible, close the menu and
                 * bring the foreground to it's original position
                 */
                else if (_swipeable && !swipedLeft && swipedRightProper && _touchedPosition != RecyclerView.NO_POSITION
                         && _backgroundVisible) {

                    // close swipe menu
                    animateFG(Animation.CLOSE, ANIMATION_STANDARD);
                    _backgroundVisible = false;
                    _backgroundVisibleView = null;
                    _backgroundVisiblePosition = -1;
                }

                /*
                 * if swiped left incorrectly (not satisfying the above conditions),
                 * animate the foreground back to it's original position (spring effect)
                 */
                else if (_swipeable && swipedLeft && !_backgroundVisible) {

                    // cancel swipe animation
                    final View tempBgView = _backgroundView;
                    animateFG(Animation.CLOSE, ANIMATION_STANDARD, new OnSwipeListener() {
                        @Override
                        public void onSwipeOptionsClosed() {
                            if (tempBgView != null)
                                tempBgView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onSwipeOptionsOpened() {

                        }
                    });

                    _backgroundVisible = false;
                    _backgroundVisibleView = null;
                    _backgroundVisiblePosition = -1;
                }

                /*
                 * if swiped right incorrectly (not satisfying the above conditions),
                 * animate the foreground to it's open position (spring effect)
                 */
                else if (_swipeable && swipedRight && _backgroundVisible) {
                    // cancel animation
                    animateFG(Animation.OPEN, ANIMATION_STANDARD);
                    _backgroundVisible = true;
                    _backgroundVisibleView = _foregroundView;
                    _backgroundVisiblePosition = _touchedPosition;
                }

                /*
                 * This case deals with an error where the user can swipe left, then right
                 * really fast and the swipe menu is stuck open - so in that case
                 * we close the swipe menu
                 */
                else if (_swipeable && swipedRight && !_backgroundVisible) {
                    // close menu
                    animateFG(Animation.CLOSE, ANIMATION_STANDARD);
                    _backgroundVisible = false;
                    _backgroundVisibleView = null;
                    _backgroundVisiblePosition = -1;
                }

                /*
                 * This case deals with an error where the user can swipe right, then left
                 * really fast and the swipe menu is stuck open - so in that case
                 * we open the swipe menu
                 */
                else if (_swipeable && swipedLeft && _backgroundVisible) {
                    // open menu
                    animateFG(Animation.OPEN, ANIMATION_STANDARD);
                    _backgroundVisible = true;
                    _backgroundVisibleView = _foregroundView;
                    _backgroundVisiblePosition = _touchedPosition;
                }

                // if the row is clicked
                else if (!swipedRight && !swipedLeft) {
                    /*
                     * if partial foreground view is clicked, bring foreground back to
                     * original position
                     */
                    if (_swipeable && _foregroundPartialViewClicked) {
                        animateFG(Animation.CLOSE, ANIMATION_STANDARD);
                        _backgroundVisible = false;
                        _backgroundVisibleView = null;
                        _backgroundVisiblePosition = -1;
                    }

                    // handling clicking of row
                    else if (_clickable && !_backgroundVisible && _touchedPosition >= 0
                            && !_recyclerViewScrolling) {
                        _RowClickListener.onRowClicked(_touchedPosition);
                    }

                    // handling clicking of options in swipe menu
                    else if (_swipeable && _backgroundVisible && !_foregroundPartialViewClicked) {
                        final int optionID = getOptionViewID(motionEvent);
                        if (optionID >= 0 && _touchedPosition >= 0) {
                            final int downPosition = _touchedPosition;

                            // close swipe menu once option is chosen
                            closeVisibleBG(new OnSwipeListener() {
                                @Override
                                public void onSwipeOptionsClosed() {
                                    _swipeMenuOptionsClickListener.onSwipeOptionClicked(optionID,
                                                                                        downPosition);
                                }

                                @Override
                                public void onSwipeOptionsOpened() {

                                }
                            });
                        }
                    }
                }
            }

            // if clicked and not swiped
            if (_swipeable) {
                _swipeVelocityTracker.recycle();
                _swipeVelocityTracker = null;
            }
            _touchedX = 0;
            _touchedY = 0;
            _touchedView = null;
            _touchedPosition = ListView.INVALID_POSITION;
            _foregroundSwiping = false;
            _backgroundView = null;
            break;

            // when finger is moving across the screen (and not yet lifted)
            case MotionEvent.ACTION_MOVE: {

                // cancels gesture if paused, swiping is disallowed or fields are not set
                if (_swipeVelocityTracker == null || _interactionPaused || !_swipeable) {
                    break;
                }

                _swipeVelocityTracker.addMovement(motionEvent);
                float deltaX = motionEvent.getRawX() - _touchedX;
                float deltaY = motionEvent.getRawY() - _touchedY;

                /*
                 * foregroundSwiping variable which is set to true here is used to alter the swipedLeft,
                 * swipedRightProper variables in "ACTION_UP" block by checking if user is actually swiping at present or not
                 */
                if (!_foregroundSwiping && Math.abs(deltaX) > _touchSlop && Math.abs(deltaY) < Math.abs(deltaX) / 2) {
                    _foregroundSwiping = true;
                    _swipingSlop = (deltaX > 0 ? _touchSlop : -_touchSlop);
                }

                // This block moves the foreground along with the finger when swiping
                if (_swipeable && _foregroundSwiping) {
                    if (_backgroundView == null) {
                        _backgroundView = _touchedView.findViewById(_backgroundID);
                        _backgroundView.setVisibility(View.VISIBLE);
                    }
                    // if swiping left
                    if (deltaX < _touchSlop && !_backgroundVisible) {
                        float translateAmount = deltaX - _swipingSlop;
                        {

                        /*
                         * Swipe foreground till width of background. If swiped further,
                         * nothing happens (stalls at width of background)
                         */
                        _foregroundView.setTranslationX(Math.abs(translateAmount) > _backgroundWidth ? -_backgroundWidth : translateAmount);
                        if (_foregroundView.getTranslationX() > 0) _foregroundView.setTranslationX(0);
                        }

                    }
                    // if swiping right
                    else if (deltaX > 0 && _backgroundVisible) {

                        // for closing rightOptions
                        if (_backgroundVisible) {
                            float translateAmount = (deltaX - _swipingSlop) - _backgroundWidth;

                            /*
                             * swipe foreground till it reaches original position. If swiped further,
                             * nothing happens (stalls at 0)
                             */
                            _foregroundView.setTranslationX(translateAmount > 0 ? 0 : translateAmount);
                        }
                        // for opening leftOptions
                        else {
                            float translateAmount = (deltaX - _swipingSlop) - _backgroundWidth;

                            /*
                             * swipe foreground till it reaches original position. If swiped further,
                             * nothing happens (stalls at 0)
                             */
                            _foregroundView.setTranslationX(translateAmount > 0 ? 0 : translateAmount);
                        }
                    }
                    return true;
                }
                // when swiped, but not beyond threshold needed for detectable swipe action
                else if (_swipeable && _foregroundSwiping) {
                    if (deltaX < _touchSlop && !_backgroundVisible) {
                        float translateAmount = deltaX - _swipingSlop;
                        if (_backgroundView == null)
                            _backgroundView = _touchedView.findViewById(_backgroundID);

                        if (_backgroundView != null)
                            _backgroundView.setVisibility(View.GONE);

                        /*
                         * swipe foreground till it reaches original position. If swiped further,
                         * nothing happens (stalls at 0)
                         */
                        _foregroundView.setTranslationX(translateAmount / 5);
                        if (_foregroundView.getTranslationX() > 0) _foregroundView.setTranslationX(0);
                    }
                    return true;
                }
                break;
            }
        }
        return false;
    }

    /**
     * Gets coordinates from Activity and closes any
     * swiped rows if touch happens outside the recycler view
     */
    public void getTouchCoordinates(MotionEvent ev) {
        int y = (int) ev.getRawY();
        if (_swipeable && _backgroundVisible && ev.getActionMasked() == MotionEvent.ACTION_DOWN
                && y < _heightOutsideView) closeVisibleBG(null);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent motionEvent) {
        return handleTouchEvent(motionEvent);
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent motionEvent) {
        handleTouchEvent(motionEvent);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

}