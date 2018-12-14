package com.syrovama.moscowweather;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.Holder> {
    private static final String TAG = "WeatherAdapter";
    private List<Weather> mWeatherList;
    private ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    WeatherAdapter(List<Weather> dataset) {
        mWeatherList = dataset;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup recycler, int i) {
        LayoutInflater inflater = LayoutInflater.from(recycler.getContext());
        return new Holder(inflater.inflate(R.layout.recycler_item, recycler, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.bind(mWeatherList.get(i));
    }

    @Override
    public int getItemCount() {
        return mWeatherList.size();
    }


    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mDateTextView;
        private TextView mTempTextView;
        private TextView mConditionTextView;


        Holder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mDateTextView = itemView.findViewById(R.id.date_text);
            mTempTextView = itemView.findViewById(R.id.temp_text);
            mConditionTextView = itemView.findViewById(R.id.condition_text);
        }
        void bind(Weather weather) {
            mDateTextView.setText(weather.getDate());
            mTempTextView.setText(weather.getTemp()+"°C");
            mConditionTextView.setText(weather.getCondition());
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "Holder got the click event");
            mClickListener.onItemClick(getAdapterPosition(), view);
        }

    }
}
