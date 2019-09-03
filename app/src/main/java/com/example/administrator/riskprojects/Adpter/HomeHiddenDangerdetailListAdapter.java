package com.example.administrator.riskprojects.Adpter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private String title;
    private String content;
    private String man;
    private String apt;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public HomeHiddenDangerdetailListAdapter(List<HiddenFollingRecord> hiddenFollingRecordList, String title, String content, String man, String apt) {
        this.hiddenFollingRecordList = hiddenFollingRecordList;
        this.title = title;
        this.content = content;
        this.man = man;
        this.apt = apt;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hidden_dange_tracking_detail_list_new, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((ViewHolder) holder).tvMan.setText(hiddenFollingRecordList.get(position).getFollingPersonName());
        ((ViewHolder) holder).tvDate.setText(hiddenFollingRecordList.get(position).getFollingRecordTime());
        ((ViewHolder) holder).tvContent.setText(hiddenFollingRecordList.get(position).getFollingRecord());

        ((ViewHolder) holder).tvTitle.setText(title);
        ((ViewHolder) holder).tvTitleContent.setText(content);
        ((ViewHolder) holder).tvTitleMan.setText(man);
        ((ViewHolder) holder).tvTitleApt.setText(apt);

        ((ViewHolder) holder).llTitle.setVisibility(holder.getAdapterPosition()==0?View.VISIBLE:View.GONE);

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

    private void initView() {

    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView llTitle;
        private TextView tvTitle;
        private TextView tvTitleContent;
        private TextView tvTitleMan;
        private TextView tvTitleApt;
        private TextView tvDate;
        private TextView tvContent;
        private TextView tvMan;

        ViewHolder(View convertView) {
            super(convertView);
            llTitle = convertView.findViewById(R.id.ll_title);
            tvTitle = convertView.findViewById(R.id.tv_title);
            tvTitleContent = convertView.findViewById(R.id.tv_title_content);
            tvTitleMan = convertView.findViewById(R.id.tv_title_man);
            tvTitleApt = convertView.findViewById(R.id.tv_title_apt);
            tvDate = convertView.findViewById(R.id.tv_date);
            tvContent = convertView.findViewById(R.id.tv_content);
            tvMan = convertView.findViewById(R.id.tv_man);

        }
    }

}
