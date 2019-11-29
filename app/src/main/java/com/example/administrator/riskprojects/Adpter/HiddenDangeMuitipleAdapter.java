package com.example.administrator.riskprojects.Adpter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.administrator.riskprojects.OnItemClickListener;
import com.example.administrator.riskprojects.R;
import com.example.administrator.riskprojects.activity.Data;
import com.example.administrator.riskprojects.activity.FiveDecisionsActivity;
import com.example.administrator.riskprojects.activity.HiddenDangerOverdueManagementActivity;
import com.example.administrator.riskprojects.activity.HiddenDangerRectificationManagementActivity;
import com.example.administrator.riskprojects.activity.HiddenDangerReviewManagementActivity;
import com.example.administrator.riskprojects.bean.ThreeFix;
import com.example.administrator.riskprojects.common.NetUtil;
import com.example.administrator.riskprojects.dialog.FlippingLoadingDialog;
import com.example.administrator.riskprojects.net.BaseJsonRes;
import com.example.administrator.riskprojects.net.NetClient;
import com.example.administrator.riskprojects.tools.Constants;
import com.example.administrator.riskprojects.tools.UserUtils;
import com.example.administrator.riskprojects.tools.Utils;
import com.example.administrator.riskprojects.util.DensityUtil;
import com.example.administrator.riskprojects.view.MyAlertDialog;
import com.example.administrator.riskprojects.view.MyDecoration;
import com.juns.health.net.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

//隐患跟踪路由
public class HiddenDangeMuitipleAdapter extends RecyclerView.Adapter {
    private static final String TAG = "HiddenDangeMuitipleAdap";
    public static final int FLAG_OVERDUE = 0;
    public static final int FLAG_REALEASE = 1;
    public static final int FLAG_REVIEW = 2;
    public static final int FLAG_RECTIFICATION = 3;
    private int flag = FLAG_OVERDUE;
    protected FlippingLoadingDialog mLoadingDialog;
    List<String> paths =new ArrayList<>();

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    List<ThreeFix> threeFixList;
    private OnItemClickListener itemClickListener;
    protected NetClient netClient;
    Context context;

    public HiddenDangeMuitipleAdapter(int flag, List<ThreeFix> threeFixList, Context context) {
        this.flag = flag;
        this.threeFixList = threeFixList;
        this.context = context;
        netClient = new NetClient(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rist_new_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        String findTimeStr = threeFixList.get(position).getFindTime();
        String findTime = findTimeStr.substring(0, 10);
        ((ViewHolder) holder).name.setText(threeFixList.get(position).getTeamGroupName().trim());
        ((ViewHolder) holder).time.setText(threeFixList.get(position).getClassName().replace("点班", ""));
        ((ViewHolder) holder).date.setText(findTime);
        ((ViewHolder) holder).content.setText(threeFixList.get(position).getContent());
        ((ViewHolder) holder).pics.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        String imagegroup = threeFixList.get(position).getImage();
        List<String> imageList = new ArrayList<String>();
        if(null!=imagegroup&&!imagegroup.equals("")){
            imagegroup = imagegroup.substring(0,imagegroup.length()-1);
            String[] imageStr = imagegroup.split(",");
            for (int i = 0; i < imageStr.length; i++) {
                imageList.add(Constants.MAIN_ENGINE+"file/"+imageStr[i]);
            }
        }
        ((ViewHolder) holder).pics.setAdapter(new ListPicAdapter(imageList));
        ((ViewHolder) holder).pics.addItemDecoration(new MyDecoration(
                holder.itemView.getContext()
                , MyDecoration.HORIZONTAL_LIST, R.color.tranparent, DensityUtil.dip2px(holder.itemView.getContext(),8)));


//        ((ViewHolder) holder).tvHiddenContent.setText(threeFixList.get(position).getContent());
//        ((ViewHolder) holder).tvArea.setText(threeFixList.get(position).getAreaName());
//        ((ViewHolder) holder).tvSpecialty.setText(threeFixList.get(position).getSname());
//        ((ViewHolder) holder).tvTimeOrOrder.setText(threeFixList.get(position).getClassName());
//        ((ViewHolder) holder).tvCategory.setText(threeFixList.get(position).getJbName());
//        String isuper = threeFixList.get(position).getIsupervision();
//        if (TextUtils.isEmpty(isuper) || TextUtils.equals(isuper, "0")) {
//            isuper = "未挂牌";
//        } else {
//            isuper = "已挂牌";
//        }
//        ((ViewHolder) holder).tvSupervise.setText(isuper);
        switch (flag) {
            case FLAG_OVERDUE:
                ((ViewHolder) holder).status.setText("重新下达");
                ((ViewHolder) holder).status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ThreeFix threeFix = threeFixList.get(position);
                        /*Intent intent = new Intent(holder.itemView.getContext(),
                                HiddenDangerOverdueManagementActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("threeFix",threeFix);
                        intent.putExtra("threeBund",bundle);
                        holder.itemView.getContext().startActivity(intent);
                        */
                        MyAlertDialog myAlertDialog = new MyAlertDialog(context,
                                new MyAlertDialog.DialogListener() {
                                    @Override
                                    public void affirm() {
                                        Log.e(TAG, "position======================: "+position);
                                        handleOutTime(threeFix.getId(), position);
                                    }

                                    @Override
                                    public void cancel() {

                                    }
                                }, "你确定要重新下达吗？");
                        myAlertDialog.show();
                    }
                });
                ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ThreeFix threeFix = threeFixList.get(position);
                        Intent intent = new Intent(holder.itemView.getContext(),
                                HiddenDangerOverdueManagementActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("threeFix", threeFix);
                        intent.putExtra("threeBund", bundle);
                        holder.itemView.getContext().startActivity(intent);


                    }
                });
                break;
            case FLAG_REALEASE:
                String userRole = UserUtils.getUserRoleids(context);
                if("1".equals(userRole)){
                    ((ViewHolder) holder).status.setText("五定");
                    ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ThreeFix threeFix = threeFixList.get(position);
                       /* Intent intent = new Intent(holder.itemView.getContext(),
                                HiddenDangerReleaseManagementActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("threeFix",threeFix);
                        intent.putExtra("threeBund",bundle);
                        holder.itemView.getContext().startActivity(intent);*/

                            Intent intent = new Intent(
                                    holder.itemView.getContext(),
                                    FiveDecisionsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("threeFix", threeFix);
                            intent.putExtra("threeBund", bundle);
                            holder.itemView.getContext().startActivity(intent);
                        }
                    });
                }else{
                    ((ViewHolder) holder).status.setVisibility(View.GONE);
                }
                break;

            case FLAG_REVIEW:
                String userRoles = UserUtils.getUserRoleids(context);
                String userid = UserUtils.getUserID(context);
                String employeeId = threeFixList.get(position).getRecheckPersonId();
                if("1".equals(userRoles)||userid.equals(employeeId)){
                    ((ViewHolder) holder).status.setText("验收");
                    ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ThreeFix threeFix = threeFixList.get(position);
                        Intent intent = new Intent(holder.itemView.getContext(),
                                HiddenDangerReviewManagementActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("threeFix",threeFix);
                        intent.putExtra("threeBund",bundle);
                        holder.itemView.getContext().startActivity(intent);
                            /*Intent intent = new Intent(holder.itemView.getContext(), HiddenDangerAcceptanceActivity.class);
                            intent.putExtra("threeFixId", threeFix.getId());
                            intent.putExtra("recheckresult", threeFix.getRecheckResult());
                            intent.putExtra("description", threeFix.getDescription());
                            intent.putExtra("recheckPersonId", threeFix.getRecheckPersonId());
                            intent.putExtra("recheckPersonName", threeFix.getRecheckPersonName());
                            holder.itemView.getContext().startActivity(intent);*/
                        }
                    });
                }else{
                    ((ViewHolder) holder).status.setVisibility(View.GONE);
                }
                break;

            case FLAG_RECTIFICATION:
                ((ViewHolder) holder).status.setText("完成整改");
                ((ViewHolder) holder).status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (itemClickListener != null) {
                            itemClickListener.onItemClick(view, position, flag);
                        }
                    }
                });
                ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(holder.itemView.getContext(), HiddenDangerRectificationManagementActivity.class);
                        final ThreeFix threeFix = threeFixList.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("threeFix", threeFix);
                        intent.putExtra("threeBund", bundle);
                        holder.itemView.getContext().startActivity(intent);

//                        MyAlertDialog myAlertDialog = new MyAlertDialog(context,
//                                new MyAlertDialog.DialogListener() {
//                                    @Override
//                                    public void affirm() {
//                                        //确定入口
//                                        getHiddenRecord(threeFix,position);
//                                    }
//
//                                    @Override
//                                    public void cancel() {
//
//                                    }
//                                },"你确定要整改此隐患吗？" );
//                        myAlertDialog.show();
//
                    }
                });
                break;
        }

    }

        //隐逾期患重新下达
        private void handleOutTime(String id, final int position) {//隐患id
            RequestParams params = new RequestParams();
            params.put("ids", id);
            if (!NetUtil.checkNetWork(context)) {
                List<String> handleoutOverduelist = new ArrayList();
                String handleoutOverdueListStr = Utils.getValue(context,Constants.HANDLEOUT_OVERDUELIST);
                if(!TextUtils.isEmpty(handleoutOverdueListStr)){
                    handleoutOverduelist = JSONArray.parseArray(handleoutOverdueListStr, String.class);
                }
                handleoutOverduelist.add(id);
                String listStr = JSONArray.toJSONString(handleoutOverduelist);
                Log.e(TAG, "隐患重新下达没网时: listStr============"+listStr);
                Utils.putValue(context,Constants.HANDLEOUT_OVERDUELIST,listStr);
                Utils.showShortToast(context, Constants.SAVE_DATA);
                notifyItemRemoved(position);
            }else{
                netClient.post(Data.getInstance().getIp() + Constants.HANDLEOUT_OVERDUELIST, params, new BaseJsonRes() {
                    @Override
                    public void onMySuccess(String data) {
                        Log.i(TAG, "隐逾期患重新下达返回数据：" + data);
                        if (!TextUtils.isEmpty(data)) {
                            Utils.showShortToast(context, "重新下达成功");
                            threeFixList.remove(position);
                            notifyItemRemoved(position);
                        }
                    }

                    @Override
                    public void onMyFailure(String content) {
                        Log.e(TAG, "隐逾期患重新下达返回错误信息：" + content);
                        Utils.showShortToast(context, content);
                        return;
                    }
                });
            }
    }

    @Override
    public int getItemCount() {
        return threeFixList.size();
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

    //完成整改
    private void getHiddenRecord(final ThreeFix threeFix, final int position) {
        RequestParams params = new RequestParams();
        params.put("ids", threeFix.getId());
        netClient.post(Data.getInstance().getIp() + Constants.COMPLETERECTIFY, params, new BaseJsonRes() {

            @Override
            public void onMySuccess(String data) {
                Log.i(TAG, "完成整改返回数据：" + data);
                if (!TextUtils.isEmpty(data)) {
                    Utils.showShortToast(context, "隐患整改成功！");
                    threeFixList.remove(position);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onMyFailure(String content) {
                Log.e(TAG, "完成整改返回错误信息：" + content);
                Utils.showShortToast(context, content);
            }
        });
    }

    public FlippingLoadingDialog getLoadingDialog(String msg) {
        if (mLoadingDialog == null)
            mLoadingDialog = new FlippingLoadingDialog(context, msg);
        return mLoadingDialog;
    }

    //查询图片列表
    private void getPicList(String imageGroup) {
        Log.i(TAG, "imageGroup: 图片imageGroup=========" + imageGroup);
        try {
            if(!TextUtils.isEmpty(imageGroup)){
                RequestParams params = new RequestParams();
                params.put("imageGroup",imageGroup);
                netClient.post(Data.getInstance().getIp() + Constants.GET_PICLIST, params, new BaseJsonRes() {

                    @Override
                    public void onMySuccess(String data) {
                        Log.i(TAG, "查询图片组返回数据：" + data);
                        if (!TextUtils.isEmpty(data)) {
                            JSONArray jsonArray = JSONArray.parseArray(data);

                            for(int i=0;i<jsonArray.size();i++){
                                JSONObject job = jsonArray.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                                paths.add(Constants.MAIN_ENGINE+"file/"+job.get("imagePath"));
                            }
                            Log.e(TAG, "paths================: "+paths);
                        }
                    }

                    @Override
                    public void onMyFailure(String content) {
                        Log.e(TAG, "查询图片组返回错误信息：" + content);
                        Utils.showLongToast(context, content);
                    }
                });
            }
        }catch (Exception e) {
            Utils.showLongToast(context, e.toString());
        }
    }

}
