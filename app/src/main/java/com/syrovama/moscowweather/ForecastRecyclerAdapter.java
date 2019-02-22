package com.syrovama.moscowweather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class ForecastRecyclerAdapter extends RecyclerView.Adapter<ForecastRecyclerAdapter.Holder>{
    private static final String TAG = "MyRecyclerAdapter";
    private ClickListener mClickListener;
    private ForecastPresenter mPresenter;

    public interface ClickListener {
        void onItemClick(int id);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    ForecastRecyclerAdapter(ForecastPresenter presenter) {
        mPresenter = presenter;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup recycler, int i) {
        LayoutInflater inflater = LayoutInflater.from(recycler.getContext());
        return new Holder(inflater.inflate(R.layout.recycler_item, recycler, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        mPresenter.onBindRowViewAtPosition(i, holder);
    }

    @Override
    public int getItemCount() {
        return mPresenter.getRowsCount();
    }

    public class Holder extends RecyclerView.ViewHolder
            implements View.OnClickListener, ForecastPresenter.ForecastRowView {
        private TextView mDateTextView;
        private TextView mTempTextView;


        Holder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mDateTextView = itemView.findViewById(R.id.date_text);
            mTempTextView = itemView.findViewById(R.id.temp_text);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onItemClick(mPresenter.getId(getAdapterPosition()));
        }

        @Override
        public void setWeather(String date, String temp) {
            mDateTextView.setText(date);
            mTempTextView.setText(temp);
        }
    }
}
