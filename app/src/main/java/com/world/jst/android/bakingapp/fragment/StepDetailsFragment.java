package com.world.jst.android.bakingapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.world.jst.android.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StepDetailsFragment extends Fragment {

    @BindView(R.id.step_short_description_tv)
    TextView mShortDescriptionTextView;
    @BindView(R.id.step_description_tv)
    TextView mDescriptionTextView;
    @BindView(R.id.step_video_url_tv)
    TextView mVideoUrlTextView;
    private Unbinder mUnbinder;
    private static final String STEP_SHORT_DESCRIPTION = "step_short_description";
    private static final String STEP_DESCRIPTION = "step_description";
    private static final String STEP_VIDEO_URL = "step_video_url";
    private static final String STEP_THUMBNAIL_URL = "step_thumbnail_url";
    private String mShortDescription;
    private String mDescription;
    private String mVideoUrl;
    private String mThumbnailUrl;

    public static StepDetailsFragment newInstance(String shortDescription, String description,
                                                  String videoUrl, String thumbnailUrl) {
        StepDetailsFragment fragment = new StepDetailsFragment();
        Bundle args = new Bundle();
        args.putString(STEP_SHORT_DESCRIPTION, shortDescription);
        args.putString(STEP_DESCRIPTION, description);
        args.putString(STEP_VIDEO_URL, videoUrl);
        args.putString(STEP_THUMBNAIL_URL, thumbnailUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShortDescription = getArguments().getString(STEP_SHORT_DESCRIPTION);
        mDescription = getArguments().getString(STEP_DESCRIPTION);
        mVideoUrl = getArguments().getString(STEP_VIDEO_URL);
        mThumbnailUrl = getArguments().getString(STEP_THUMBNAIL_URL);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_details, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mShortDescriptionTextView.setText(mShortDescription);
        mDescriptionTextView.setText(mDescription);
        mVideoUrlTextView.setText(mVideoUrl);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}
