package com.endava.smartdesk.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.endava.smartdesk.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FormsAdapter extends RecyclerView.Adapter<FormsAdapter.ViewHolder> {

    private final List<String> mFormsList;

    private OnMyItemClickListener mItemListener;

    public FormsAdapter() {
        mFormsList = new ArrayList<>();
    }

    @NonNull
    @Override
    public FormsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.form_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String formName = mFormsList.get(position);
        viewHolder.bind(formName);
    }

    @Override
    public int getItemCount() {
        return mFormsList.size();
    }

    public void setData(List<String> mData) {
        mFormsList.clear();
        mFormsList.addAll(mData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_view)
        ConstraintLayout mView;

        @BindView(R.id.form_name)
        TextView mTitle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(String formName) {
            mTitle.setText(formName);
            mView.setOnClickListener(v -> {
                mItemListener.onMyItemClickListener(false, formName);
            });
        }
    }

    public interface OnMyItemClickListener {
        void onMyItemClickListener(boolean displayFormList, String formName);
    }

    public void setOnMyItemClickListener(OnMyItemClickListener listener) {
        mItemListener = listener;
    }
}
