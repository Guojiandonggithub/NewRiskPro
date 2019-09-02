package com.example.administrator.riskprojects.Adpter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.riskprojects.OnItemClickListener;
import com.example.administrator.riskprojects.R;
import com.example.administrator.riskprojects.bean.HiddenFollingRecord;

import java.util.List;

public class HomeHiddenDangerdetailListAdapter extends RecyclerView.Adapter {
    List<HiddenFollingRecord> hiddenFollingRecordList;
    public static final int FLAG_CHANGE = 0;
    public static final int FLAG_DELETE = 1;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public HomeHiddenDangerdetailListAdapter(List<HiddenFollingRecord> hiddenFollingRecordList) {
        this.hiddenFollingRecordList = hiddenFollingRecordList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hidden_dange_tracking_detail_list_new, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((ViewHolder) holder).mTvman.setText(hiddenFollingRecordList.get(position).getFollingPersonName());
        ((ViewHolder) holder).mTvDate.setText(hiddenFollingRecordList.get(position).getFollingRecordTime());
        ((ViewHolder) holder).mTvContent.setText(hiddenFollingRecordList.get(position).getFollingRecord());
//
//        ((ViewHolder) holder).mAvChange.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onItemClickListener != null) {
//                    onItemClickListener.onItemClick(v, position, FLAG_CHANGE);
//                }
//            }
//        });
//
//        ((ViewHolder) holder).mAvDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onItemClickListener != null) {
//                    onItemClickListener.onItemClick(v, position, FLAG_DELETE);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return hiddenFollingRecordList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvDate;
        private TextView mTvman;
        private TextView mTvContent;

        ViewHolder(View view) {
            super(view);
            mTvDate = view.findViewById(R.id.tv_date);
            mTvman = view.findViewById(R.id.tv_man);
            mTvContent = view.findViewById(R.id.tv_content);

        }
    }

}
