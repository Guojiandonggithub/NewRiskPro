package com.example.administrator.riskprojects.Adpter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.riskprojects.R;
import com.example.administrator.riskprojects.activity.HiddenDangeTrackingManagementActivity;
import com.example.administrator.riskprojects.bean.ThreeFix;

import java.util.List;

public class HiddenDangeTrackingAdapter extends RecyclerView.Adapter {
    private List<ThreeFix> threeFixList;



    public HiddenDangeTrackingAdapter(List<ThreeFix> threeFixList) {
        this.threeFixList = threeFixList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rist_new_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(threeFixList.size()>0){
            String findTimeStr = threeFixList.get(position).getFindTime();
            String findTime = findTimeStr.substring(0, 10);
            ((ViewHolder) holder).name.setText(threeFixList.get(position).getTeamGroupName().trim());
            ((ViewHolder) holder).time.setText(threeFixList.get(position).getClassName().replace("点班", ""));
            ((ViewHolder) holder).date.setText(findTime);
            ((ViewHolder) holder).content.setText(threeFixList.get(position).getContent());
            ((ViewHolder) holder).status.setText("跟踪状态未定");
//            ((ViewHolder) holder).tvHiddenContent.setText(threeFixList.get(position).getContent());
//            ((ViewHolder) holder).tvArea.setText(threeFixList.get(position).getAreaName());
//            ((ViewHolder) holder).tvSpecialty.setText(threeFixList.get(position).getSname());
//            String findTimeStr = threeFixList.get(position).getFindTime();
//            String findTime = findTimeStr.substring(0,10);
//            ((ViewHolder) holder).tvTimeOrOrder.setText(findTime+"/"+threeFixList.get(position).getClassName());
//            ((ViewHolder) holder).tvCategory.setText(threeFixList.get(position).getJbName());
//            String isuper = threeFixList.get(position).getIsupervision();
//            if(TextUtils.isEmpty(isuper)||TextUtils.equals(isuper,"0")){
//                isuper = "未挂牌";
//            }else{
//                isuper = "已挂牌";
//            }
//
//            ((ViewHolder) holder).tvSupervise.setText(isuper);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=  new Intent( holder.itemView.getContext(),
                        HiddenDangeTrackingManagementActivity.class);
                ThreeFix threeFix = threeFixList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("threeFix",threeFix);
                intent.putExtra("threeBund",bundle);
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return threeFixList.size();
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
}
