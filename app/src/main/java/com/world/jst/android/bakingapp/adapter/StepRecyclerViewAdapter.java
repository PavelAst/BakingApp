package com.world.jst.android.bakingapp.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.world.jst.android.bakingapp.R;
import com.world.jst.android.bakingapp.fragment.RecipeStepsFragment;
import com.world.jst.android.bakingapp.model.Step;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class StepRecyclerViewAdapter extends
        RealmRecyclerViewAdapter<Step, StepRecyclerViewAdapter.StepViewHolder> {

    private final RecipeStepsFragment.RecipeStepsOnClickHandler mClickHandler;

    public StepRecyclerViewAdapter(@Nullable OrderedRealmCollection<Step> data,
                                   RecipeStepsFragment.RecipeStepsOnClickHandler clickHandler) {
        super(data, true);
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_step_item, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        Step step = getData().get(position);
        holder.bindTo(step);
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.step_number_tv)
        TextView mStepNumberTV;
        @BindView(R.id.description_tv)
        TextView mDescriptionTV;

        public StepViewHolder(View stepView) {
            super(stepView);
            ButterKnife.bind(this, stepView);
            stepView.setOnClickListener(this);
        }

        public void bindTo(Step step) {
            int adapterPosition = getAdapterPosition();
            mStepNumberTV.setText(String.valueOf(adapterPosition + 1));
            mDescriptionTV.setText(step.mShortDescription);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Step step = getData().get(adapterPosition);
            if (mClickHandler != null) {
                mClickHandler.onOptionClick(step, adapterPosition);
            }
        }

    }
}
