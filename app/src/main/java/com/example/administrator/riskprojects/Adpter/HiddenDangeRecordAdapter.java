package com.example.administrator.riskprojects.Adpter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.riskprojects.R;
import com.example.administrator.riskprojects.activity.HiddenDangerDetailManagementActivity;
import com.example.administrator.riskprojects.bean.HiddenDangerRecord;
import com.example.administrator.riskprojects.util.DensityUtil;
import com.example.administrator.riskprojects.view.MyDecoration;

import java.util.List;

public class HiddenDangeRecordAdapter extends RecyclerView.Adapter {
    private static final String TAG = "HiddenDangeRecordAdapte";

    List<HiddenDangerRecord> recordList;
    Context context;


    public HiddenDangeRecordAdapter(List<HiddenDangerRecord> recordList, Context context) {
        this.recordList = recordList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rist_new_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
//        ((ViewHolder) holder).tvHiddenUnits.setText(recordList.get(position).getTeamGroupName().trim());

//        ((ViewHolder) holder).tvTimeOrOrder.setText(findTime + "/" + recordList.get(position).getClassName());
//        ((ViewHolder) holder).tvHiddenContent.setText(recordList.get(position).getContent());
//        ((ViewHolder) holder).tvHiddenDangerBelongs.setText(recordList.get(position).getHiddenBelong());
//        ((ViewHolder) holder).tvProfessional.setText(recordList.get(position).getSname());
//        ((ViewHolder) holder).tvArea.setText(recordList.get(position).getAreaName());
//        ((ViewHolder) holder).tvClasses.setText(recordList.get(position).getJbName());
//        ((ViewHolder) holder).ivStatus.setImageResource(getImageResourceByFlag(recordList.get(position).getFlag() == null ? "0" : recordList.get(position).getFlag(), recordList.get(position).getOutTimeFlag()));
//        ((ViewHolder) holder).ivStatusSecond.setImageResource(getImageResourceByFlag(recordList.get(position).getFlag() == null ? "0" : recordList.get(position).getFlag(), recordList.get(position).getOutTimeFlag()));
//        String isuper = recordList.get(position).getIsupervision();
//        if (TextUtils.isEmpty(isuper) || TextUtils.equals(isuper, "0")) {
//            isuper = "未挂牌";
//        } else {
//            isuper = "已挂牌";
//        }
        String findTimeStr = recordList.get(position).getFindTime();
        String findTime = findTimeStr.substring(0, 10);
        ((ViewHolder) holder).name.setText(recordList.get(position).getTeamGroupName().trim());
        ((ViewHolder) holder).time.setText(recordList.get(position).getClassName().replace("点班", ""));
        ((ViewHolder) holder).date.setText(findTime);
        ((ViewHolder) holder).status.setText(getStatusByFlag(recordList.get(position).getFlag(),recordList.get(position).getOutTimeFlag()));
        ((ViewHolder) holder).content.setText(recordList.get(position).getContent());
        ((ViewHolder) holder).pics.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        ((ViewHolder) holder).pics.setAdapter(new ListPicAdapter(recordList.get(position).getPicList()));
        ((ViewHolder) holder).pics.addItemDecoration(new MyDecoration(
                holder.itemView.getContext()
                , MyDecoration.HORIZONTAL_LIST, R.color.tranparent, DensityUtil.dip2px(holder.itemView.getContext(),8)));


        ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (NetUtil.checkNetWork(context)) {
                Intent intent = new Intent(holder.itemView.getContext(),
                        HiddenDangerDetailManagementActivity.class);
                final HiddenDangerRecord hiddenDangerRecord = recordList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("hiddenDangerRecord", hiddenDangerRecord);
                intent.putExtra("recordBund", bundle);
                String employeeId = recordList.get(position).getEmployeeId();
                String id = recordList.get(position).getId();
                String flag = recordList.get(position).getFlag();
                String isupervision = recordList.get(position).getIsupervision();
                intent.putExtra("id", id);
                intent.putExtra("employeeId", employeeId);
                intent.putExtra("flag", flag);
                intent.putExtra("isupervision", isupervision);
                holder.itemView.getContext().startActivity(intent);
                //}else{
                //Utils.showShortToast(context, "当前没有网络，无法获取信息");
                //}
            }
        });

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
            /*case "5":
                return R.mipmap.ic_status_overdue;*/
            default:
                return R.mipmap.ic_status_overdue;
        }


    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    private void initView() {

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

    private String getStatusByFlag(String flag,String outTimeFlag) {
        if ("1".equals(outTimeFlag)) {
            return "逾期";
        }
        switch (flag) {
            case "0":
                return "筛选";
            case "1":
                return "五定中";
            case "2":
                return "整改中";
            case "3":
                return "验收中";
            case "4":
                return "销项";
            case "5":
                return "跟踪";
            default:
                return "未知";
        }
    }
}
