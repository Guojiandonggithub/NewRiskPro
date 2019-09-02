package com.example.administrator.riskprojects.Adpter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.riskprojects.R;
import com.example.administrator.riskprojects.activity.HiddenDangerDetailManagementAddOrDetailActivity;
import com.example.administrator.riskprojects.bean.HiddenDangerRecord;
import com.example.administrator.riskprojects.util.DensityUtil;
import com.example.administrator.riskprojects.view.MyDecoration;

import java.util.List;

public class ListingSupervisionAdapter extends RecyclerView.Adapter {
    List<HiddenDangerRecord> recordList;


    public ListingSupervisionAdapter(List<HiddenDangerRecord> recordList) {
        this.recordList = recordList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rist_new_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        ((ViewHolder) holder).name.setText(recordList.get(position).getTeamGroupName().trim());
        ((ViewHolder) holder).time.setText(recordList.get(position).getClassName().replace("点班", ""));
        ((ViewHolder) holder).content.setText(recordList.get(position).getContent());
        ((ViewHolder) holder).llBottom.setVisibility(View.GONE);
        ((ViewHolder) holder).pics.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        ((ViewHolder) holder).pics.setAdapter(new ListPicAdapter(recordList.get(position).getPicList()));
        ((ViewHolder) holder).pics.addItemDecoration(new MyDecoration(
                holder.itemView.getContext()
        , MyDecoration.HORIZONTAL_LIST, R.color.tranparent, DensityUtil.dip2px(holder.itemView.getContext(),8)));
        ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(),
                        HiddenDangerDetailManagementAddOrDetailActivity.class);
                Bundle bundle = new Bundle();
                final HiddenDangerRecord hiddenDangerRecord = recordList.get(position);
                bundle.putSerializable("hiddenDangerRecord", hiddenDangerRecord);
                intent.putExtra("recordBund", bundle);
                String id = recordList.get(position).getId();
                intent.putExtra("id", id);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    private void initView(View view) {


    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView time;
        private TextView content;
        private RecyclerView pics;
        private LinearLayoutCompat llBottom;
        private TextView date;
        private TextView status;


        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            time = view.findViewById(R.id.time);
            content = view.findViewById(R.id.content);
            pics = view.findViewById(R.id.pics);
            llBottom = view.findViewById(R.id.ll_bottom);
            date = view.findViewById(R.id.date);
            status = view.findViewById(R.id.status);
        }
    }

    private int getImageResourceByFlag(String flag, String outTimeFlag) {
        if ("1".equals(outTimeFlag)) {
            return R.mipmap.ic_status_overdue;
        }
        switch (flag) {
            case "0":
                return R.mipmap.ic_status_shaixuan;
            case "1":
                return R.mipmap.ic_status_release;
            case "2":
                return R.mipmap.ic_status_rectificationg;
            case "3":
                return R.mipmap.ic_recheck;
            case "4":
                return R.mipmap.ic_status_dispelling;
            case "5":
                return R.mipmap.ic_status_release;
            default:
                return R.mipmap.ic_status_overdue;
        }
    }

}
