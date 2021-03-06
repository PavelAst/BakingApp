package com.world.jst.android.bakingapp.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashChunkSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.world.jst.android.bakingapp.R;
import com.world.jst.android.bakingapp.player.ComponentListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StepDetailsFragment extends Fragment {

    @BindView(R.id.playerView)
    PlayerView playerView;
    @BindView(R.id.guideline_player)
    Guideline mGuideline;
    @BindView(R.id.thumbnail_iv)
    ImageView mThumbnailImageView;
    @BindView(R.id.step_short_description_tv)
    TextView mShortDescriptionTextView;
    @BindView(R.id.step_description_tv)
    TextView mDescriptionTextView;
    private Unbinder mUnbinder;

    // Turn logging on or off
    private static final boolean L = false;
    private static final String TAG = "StepDetailsFragment";
    private static final String PLAYBACK_POSITION = "playback_position";
    private static final String PLAY_WHEN_READY = "play_when_ready";
    private static final String CURRENT_WINDOW = "current_window";
    // bandwidth meter to measure and estimate bandwidth
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final String STEP_SHORT_DESCRIPTION = "step_short_description";
    private static final String STEP_DESCRIPTION = "step_description";
    private static final String STEP_VIDEO_URL = "step_video_url";
    private static final String STEP_THUMBNAIL_URL = "step_thumbnail_url";
    private static final String TWO_PANE = "two_pane";
    private String mShortDescription;
    private String mDescription;
    private String mVideoUrl;
    private String mThumbnailUrl;
    private boolean mTwoPane;

    private SimpleExoPlayer mPlayer;
    private ComponentListener mComponentListener;

    private long mPlaybackPosition;
    private int mCurrentWindow;
    private boolean mPlayWhenReady = false;

    public static StepDetailsFragment newInstance(String shortDescription, String description,
                                                  String videoUrl, String thumbnailUrl,
                                                  boolean twoPane) {
        StepDetailsFragment fragment = new StepDetailsFragment();
        Bundle args = new Bundle();
        args.putString(STEP_SHORT_DESCRIPTION, shortDescription);
        args.putString(STEP_DESCRIPTION, description);
        args.putString(STEP_VIDEO_URL, videoUrl);
        args.putString(STEP_THUMBNAIL_URL, thumbnailUrl);
        args.putBoolean(TWO_PANE, twoPane);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (L) Log.d(TAG, "*** StepDetailsFragment - onCreate ***");
        super.onCreate(savedInstanceState);
        mShortDescription = getArguments().getString(STEP_SHORT_DESCRIPTION);
        mDescription = getArguments().getString(STEP_DESCRIPTION);
        mVideoUrl = getArguments().getString(STEP_VIDEO_URL);
        mThumbnailUrl = getArguments().getString(STEP_THUMBNAIL_URL);
        mTwoPane = getArguments().getBoolean(TWO_PANE, true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        int fragment_step_details = (mTwoPane) ?
                R.layout.fragment_step_details_tablet :
                R.layout.fragment_step_details;
        View view = inflater.inflate(fragment_step_details, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        if (savedInstanceState != null) {
            if (L) Log.d(TAG, "*** onCreateView - savedInstanceState != null ***");
            mPlaybackPosition = savedInstanceState.getLong(PLAYBACK_POSITION);
            mCurrentWindow = savedInstanceState.getInt(CURRENT_WINDOW);
            mPlayWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
        }

        mComponentListener = new ComponentListener();

        mShortDescriptionTextView.setText(mShortDescription);
        mDescriptionTextView.setText(mDescription);

        return view;
    }

    @Override
    public void onStart() {
        if (L) Log.d(TAG, "***  - onStart ***");
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        if (L) Log.d(TAG, "*** StepDetailsFragment - onResume ***");
        super.onResume();
        if ((Util.SDK_INT <= 23 || mPlayer == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        if (L) Log.d(TAG, "*** StepDetailsFragment - onPause ***");
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        if (L) Log.d(TAG, "*** StepDetailsFragment - onStop ***");
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        if (L) Log.d(TAG, "*** StepDetailsFragment-onDestroyView ***");
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private void initializePlayer() {
        if (TextUtils.isEmpty(mVideoUrl)) {
            showThumbnailImageView();
            displayThumbnailImage();
            return;
        }
        if (mPlayer == null) {
            if (L) Log.d(TAG, "*** StepDetailsFragment-initializePlayer ***");
            mPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getActivity()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            mPlayer.addListener(mComponentListener);
            mPlayer.addVideoDebugListener(mComponentListener);
            mPlayer.addAudioDebugListener(mComponentListener);
            playerView.setPlayer(mPlayer);
            mPlayer.setPlayWhenReady(mPlayWhenReady);
            mPlayer.seekTo(mCurrentWindow, mPlaybackPosition);
        }
        MediaSource mediaSource = buildMediaSource(Uri.parse(mVideoUrl));
        mPlayer.prepare(mediaSource, false, false);
    }

    private void showThumbnailImageView() {
        mThumbnailImageView.setVisibility(View.VISIBLE);
        playerView.setVisibility(View.INVISIBLE);
    }

    private void displayThumbnailImage() {
        if (!TextUtils.isEmpty(mThumbnailUrl)) {
            Picasso.get()
                    .load(mThumbnailUrl)
                    .placeholder(R.drawable.bakery_new)
                    .error(R.drawable.bakery_new)
                    .into(mThumbnailImageView);
        } else {
            Picasso.get()
                    .load(R.drawable.bakery_new)
                    .into(mThumbnailImageView);
        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            if (L) Log.d(TAG, "*** StepDetailsFragment-releasePlayer ***");
            mPlaybackPosition = mPlayer.getCurrentPosition();
            mCurrentWindow = mPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mPlayer.getPlayWhenReady();
            mPlayer.removeListener(mComponentListener);
            mPlayer.removeVideoDebugListener(mComponentListener);
            mPlayer.removeAudioDebugListener(mComponentListener);
            mPlayer.release();
            mPlayer = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        String userAgent = "exoplayer-codelab";

        if (uri.getLastPathSegment().contains("mp3") || uri.getLastPathSegment().contains("mp4")) {
            return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(uri);
        } else if (uri.getLastPathSegment().contains("m3u8")) {
            return new HlsMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(uri);
        } else {
            DashChunkSource.Factory dashChunkSourceFactory = new DefaultDashChunkSource.Factory(
                    new DefaultHttpDataSourceFactory("ua", BANDWIDTH_METER));
            DataSource.Factory manifestDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent);
            return new DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory)
                    .createMediaSource(uri);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && mPlayer != null) {
            mPlayWhenReady = false;
            mPlayer.setPlayWhenReady(mPlayWhenReady);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (L) Log.d(TAG, "**** StepDetailsFragment-onSaveInstanceState ***");
        if (mPlayer != null) {
            mPlaybackPosition = mPlayer.getCurrentPosition();
            mCurrentWindow = mPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mPlayer.getPlayWhenReady();
        }
        outState.putLong(PLAYBACK_POSITION, mPlaybackPosition);
        outState.putInt(CURRENT_WINDOW, mCurrentWindow);
        outState.putBoolean(PLAY_WHEN_READY, mPlayWhenReady);
    }

}
