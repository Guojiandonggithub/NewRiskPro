package com.example.administrator.riskprojects.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.administrator.riskprojects.Adpter.ListBigPicAdapter;
import com.example.administrator.riskprojects.Adpter.PicAdapter;
import com.example.administrator.riskprojects.BaseActivity;
import com.example.administrator.riskprojects.R;
import com.example.administrator.riskprojects.bean.HiddenDangerRecord;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class HiddenDangerDetailManagementActivity extends BaseActivity {
    private static final String TAG = "HiddenDangerDetailManag";
    protected NetClient netClient;
    private TextView txtLeft;
    private ImageView imgLeft;
    private TextView txtTitle;
    private ImageView imgRight;
    private TextView txtRight;
    private TextView tvHiddenUnits;
    private TextView tvTimeOrOrder;
    private TextView tvHiddenContent;
    private TextView tvHiddenDangerBelongs;
    private LinearLayoutCompat expand;
    private TextView tvProfessional;
    private TextView tvArea;
    private TextView tvClasses;
    private TextView tvOversee;
    private ImageView ivStatusSecond;
    private LinearLayoutCompat clickMore;
    private LinearLayoutCompat rectificationPlan;
    private ImageView ivStatus;
    private LinearLayoutCompat llBottom;
    private TextView tvDelete;
    private TextView tvAdd;
    private TextView tvChange;
    private HiddenDangerRecord record;
    private String hiddenrecordjson;
    private TextView tvHiddenBelong;
    private TextView tvIsHang;
    private TextView tvDiscoveryTime;
    private TextView tvCheckTheContent;
    private TextView tvStatus;
    private TextView tvIsHandle;
    private TextView tvHiddenDangerLogger;
    private TextView tvFinishTime;
    private TextView tvPrincipal;
    private TextView tvMeasure;
    private TextView tvCapital;
    private TextView tvRecyclerView;
    private TextView tvTheNumberOfProcessing;
    private TextView tvTrackingUnit;
    private TextView tvTrackPeople;
    private TextView tvAcceptanceOfThePeople;
    private LinearLayoutCompat llacceptance;
    private LinearLayoutCompat llAcceptanceOfTheResults;
    private TextView tvAcceptanceOfTheResults;
    private RecyclerView recyclerView;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_danger_detail_management_new);
        netClient = new NetClient(HiddenDangerDetailManagementActivity.this);
        initView();
        setView();
    }

    private void setView() {
        final Intent intent = getIntent();
        String statistics = intent.getStringExtra("statistics");
        String id = intent.getStringExtra("id");
        String hiddenriskrecorddetail = intent.getStringExtra("hiddenriskrecorddetail");
        if(!TextUtils.isEmpty(statistics)){
            txtTitle.setText(R.string.hazard_query_statistics);
            llBottom.setVisibility(View.GONE);
            getHiddenRecordById(id);
        }else if(!TextUtils.isEmpty(hiddenriskrecorddetail)){
            txtTitle.setText("隐患详情");
            tvDelete.setVisibility(View.GONE);
            tvChange.setVisibility(View.GONE);
            tvAdd.setVisibility(View.GONE);
            getHiddenRecordById(id);
        }else{
            txtTitle.setText(R.string.hidden_danger_record_management);
            Bundle bundle = intent.getBundleExtra("recordBund");
            record = (HiddenDangerRecord) bundle.getSerializable("hiddenDangerRecord");
            initdata();
            timer1();
        }
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAlertDialog alertDialog = new MyAlertDialog(HiddenDangerDetailManagementActivity.this
                        , new MyAlertDialog.DialogListener() {
                    @Override
                    public void affirm() {
                        String id = intent.getStringExtra("id");
                        if (checkparam(intent)) {
                            deleteHiddenRecord(id);
                        }
                    }

                    @Override
                    public void cancel() {

                    }
                }, "你确定要删除选中的数据么？");
                alertDialog.show();
            }
        });

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "是否隐患督办: "+ record.getIsupervision());
                if("0".equals(record.getIsupervision())){
                    Intent intents = new Intent(HiddenDangerDetailManagementActivity.this, AddHangRecordActivity.class);
                    intents.putExtra("id",record.getId());
                    startActivityForResult(intents, Integer.parseInt(Constants.PAGE));
                }else{
                    Utils.showLongToast(HiddenDangerDetailManagementActivity.this, "该隐患已经挂牌!");
                }
            }
        });

        tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intents = new Intent(HiddenDangerDetailManagementActivity.this, HiddenRiskRecordAddEditActivity.class);
                //Log.e(TAG, "onClick: hiddenrecordjson=============" + hiddenrecordjson);
                Bundle bundle = new Bundle();
                bundle.putSerializable("hiddenDangerRecord", record);
                intents.putExtra("recordBund", bundle);
                //intents.putExtra("hiddenrecordjson", hiddenrecordjson);
                intents.putExtra("id", record.getId());
                intents.putExtra("offlineStatus", record.getOfflineDataStatus());
                startActivityForResult(intents,RESULT_FIRST_USER);
            }
        });

        findViewById(R.id.btn_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HiddenDangerDetailManagementActivity.this, HiddenDangeTrackingDetailListActivity.class);
                intent.putExtra("threeFixId",record.getId());
                intent.putExtra("name",record.getTeamGroupName());
                intent.putExtra("content",record.getContent());
                intent.putExtra("tracker",record.getFollingPersonName());
                intent.putExtra("trackeram",record.getFollingTeamName());
                System.out.print("threeFixId222222222222====="+record.getId());
                startActivity(intent);
            }
        });



    }

    private boolean checkparam(Intent intent) {
        String flag = intent.getStringExtra("flag");
        Log.e(TAG, "flag================: " + flag);
        String employeeId = intent.getStringExtra("employeeId");
        String role = UserUtils.getUserRoleids(HiddenDangerDetailManagementActivity.this);
        String userid = UserUtils.getUserID(HiddenDangerDetailManagementActivity.this);
        if (!"1".equals(role) && !userid.equals(employeeId)) {
            Utils.showLongToast(HiddenDangerDetailManagementActivity.this, "您不是管理员或该隐患不是您上报的,不能进行删除!");
            return false;
        }
        if (null != flag) {
            if (Integer.parseInt(flag) >= 2) {
                Utils.showLongToast(HiddenDangerDetailManagementActivity.this, "该隐患已经下达不能修改!");
            }
        }
        return true;
    }

    private void initView() {
        txtLeft = findViewById(R.id.txt_left);
        imgLeft = findViewById(R.id.img_left);
        txtTitle = findViewById(R.id.txt_title);
        imgRight = findViewById(R.id.img_right);
        txtRight = findViewById(R.id.txt_right);
        tvHiddenUnits = findViewById(R.id.tv_hidden_units);
        rectificationPlan = findViewById(R.id.ll_rectification_plan);
        tvTimeOrOrder = findViewById(R.id.tv_time_or_order);
        tvHiddenContent = findViewById(R.id.tv_hidden_content);
        tvHiddenDangerBelongs = findViewById(R.id.tv_hidden_danger_belongs);
        expand = findViewById(R.id.expand);
        tvProfessional = findViewById(R.id.tv_professional);
        tvArea = findViewById(R.id.tv_area);
        tvClasses = findViewById(R.id.tv_classes);
        tvOversee = findViewById(R.id.tv_oversee);
//        ivStatusSecond = findViewById(R.id.iv_status_second);
        clickMore = findViewById(R.id.click_more);
        ivStatus = findViewById(R.id.iv_status);
        llBottom = findViewById(R.id.ll_bottom);
        tvAdd = findViewById(R.id.tv_add);
        tvDelete = findViewById(R.id.tv_delete);
        tvChange = findViewById(R.id.tv_change);
        tvHiddenBelong = findViewById(R.id.tv_hidden_belong);
        tvIsHang = findViewById(R.id.tv_is_hang);
        tvDiscoveryTime = findViewById(R.id.tv_discovery_time);
        tvCheckTheContent = findViewById(R.id.tv_check_the_content);
        tvStatus = findViewById(R.id.tv_status);
        tvIsHandle = findViewById(R.id.tv_is_handle);
        tvHiddenDangerLogger = findViewById(R.id.tv_hidden_danger_logger);
        tvFinishTime = findViewById(R.id.tv_finish_time);
        tvPrincipal = findViewById(R.id.tv_principal);
        tvMeasure = findViewById(R.id.tv_measure);
        tvCapital = findViewById(R.id.tv_capital);
        tvRecyclerView = findViewById(R.id.tv_department);
        tvTheNumberOfProcessing = findViewById(R.id.tv_the_number_of_processing);
        tvTrackingUnit = findViewById(R.id.tv_tracking_unit);
        tvTrackPeople = findViewById(R.id.tv_track_people);
        tvAcceptanceOfThePeople = findViewById(R.id.tv_acceptance_of_the_people);
        llacceptance = findViewById(R.id.ll_acceptance);
        tvAcceptanceOfTheResults = findViewById(R.id.tv_acceptance_of_the_results);
        llAcceptanceOfTheResults = findViewById(R.id.ll_acceptance_of_the_results);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.addItemDecoration(new MyDecoration(
               this
                , MyDecoration.HORIZONTAL_LIST, R.color.tranparent, DensityUtil.dip2px(this
                ,8)));
    }

    private void initdata() {
        getHiddenRecord();
    }

    private void getHiddenRecordById(String id){
        RequestParams params = new RequestParams();
        params.put("hiddenDangerRecordId", id);
        netClient.post(Data.getInstance().getIp() + Constants.HIDDENDANGERRECORD, params, new BaseJsonRes() {

            @Override
            public void onMySuccess(String data) {
                Log.i(TAG, "隐患数据返回数据：" + data);
                if (!TextUtils.isEmpty(data)) {
                    JSONObject returndata = JSON.parseObject(data);
                    hiddenrecordjson = data;
                    record = JSONArray.parseObject(data, HiddenDangerRecord.class);
                    initdata();
                }
            }

            @Override
            public void onMyFailure(String content) {
                Log.e(TAG, "隐患数据返回错误信息：" + content);
                Utils.showLongToast(HiddenDangerDetailManagementActivity.this, content);
                return;
            }
        });
    }

    private void getHiddenRecord() {//隐患id String id
        if(record.getFlag()==null){
            record.setFlag("0");
        }
        if(record.getFlag().equals("0")||record.getFlag().equals("4")){
            tvAdd.setVisibility(View.GONE);
        }
        tvHiddenUnits.setText(record.getTeamGroupName().trim());
        String findTimeStr = record.getFindTime();
        String findTime = findTimeStr.substring(0,10);
        tvTimeOrOrder.setText(findTimeStr);
        ((TextView) findViewById(R.id.time)).setText(record.getClassName().replace("点班", ""));
        tvHiddenContent.setText(record.getContent());
        tvHiddenDangerBelongs.setText(record.getHiddenBelong());
        tvProfessional.setText(record.getSname()+"专业");
        tvProfessional.setVisibility(View.VISIBLE);
        tvArea.setText(record.getAreaName());
        tvClasses.setText(record.getJbName()+"级");
        tvClasses.setVisibility(View.VISIBLE);
        ivStatus.setImageResource(getImageResourceByFlag(record.getFlag(), record.getOutTimeFlag()));
        String isuper = record.getIsupervision();
        String guapai = "未挂牌";
        if (TextUtils.isEmpty(isuper) || TextUtils.equals(isuper, "0")) {
            isuper = "未挂牌";
        } else {
            isuper = "已挂牌";
            guapai = "已挂牌";
        }
        String ishandle = record.getIshandle();
        if(TextUtils.isEmpty(ishandle)||"0".equals(ishandle)){
            ishandle = "未处理";
        }else{
            ishandle = "已处理";
        }
        tvDiscoveryTime.setText(record.getFindTime());
        tvOversee.setText(isuper);
        tvIsHang.setText(guapai);
        String status = getStatusByFlag(record.getFlag(),record.getOutTimeFlag());
        tvStatus.setText(status);
        tvIsHandle.setText(ishandle);
        tvHiddenDangerLogger.setText(record.getRealName());
        tvFinishTime.setText(record.getFixTime());
        tvPrincipal.setText(record.getThreeFixRealName());
        tvMeasure.setText(record.getMeasure());
        tvCapital.setText(record.getMoney());
        tvRecyclerView.setText(record.getTeamName());
        tvTheNumberOfProcessing.setText(record.getPersonNum());
        tvTrackingUnit.setText(record.getFollingTeamName());
        tvTrackPeople.setText(record.getFollingPersonName());
        tvAcceptanceOfThePeople.setText(record.getRecheckPersonName());
        String recheckresult = record.getRecheckResult();
        if(TextUtils.isEmpty(recheckresult)){
            recheckresult = "";
        }else if(TextUtils.equals(recheckresult,"0")){
            recheckresult = "通过";
        }else{
            recheckresult = "未通过";
        }
        tvAcceptanceOfTheResults.setText(recheckresult);
        tvOversee.setText(isuper);
        tvCheckTheContent.setText(record.getHiddenCheckContent());
        if(!TextUtils.isEmpty(record.getOfflineDataStatus())){
            txtRight.setText("未上报");
        }
        if(record.getPicList()!=null){
            recyclerView.setAdapter(new ListBigPicAdapter(record.getPicList()));
        }else{
            getPicList(record.getImageGroup());
        }
    }

    //删除隐患
    private void deleteHiddenRecord(String id) {//隐患id
        RequestParams params = new RequestParams();
        if(!TextUtils.isEmpty(record.getOfflineDataStatus())){
            params.put("offlineDataStatus", record.getOfflineDataStatus());
        }
        params.put("hiddenDangerRecordId", id);
        netClient.post(Data.getInstance().getIp() + Constants.DELETE_HIDDEN, params, new BaseJsonRes() {

            @Override
            public void onMySuccess(String data) {
                Log.i(TAG, "删除隐患返回数据：" + data);
                if (!TextUtils.isEmpty(data)) {
                    Utils.showLongToast(HiddenDangerDetailManagementActivity.this, "删除成功");
                }
                finish();
            }

            @Override
            public void onMyFailure(String content) {
                Log.e(TAG, "删除隐患返回错误信息：" + content);
                Utils.showLongToast(HiddenDangerDetailManagementActivity.this, content);
                return;
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
            case "5":
                return R.mipmap.ic_status_release;
            default:
                return R.mipmap.ic_status_overdue;
        }
    }

    private String getStatusByFlag(String flag, String outTimeFlag) {
        if ("1".equals(outTimeFlag)) {
            return "逾期";
        }
        switch (flag) {
            case "0":
                rectificationPlan.setVisibility(View.GONE);
                llacceptance.setVisibility(View.GONE);
                return "筛选";
            case "1":
                llacceptance.setVisibility(View.GONE);
                return "五定中";
            case "2":
                llacceptance.setVisibility(View.GONE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    finish();
                }
                break;
            default:
        }
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
                            List<String> paths =new ArrayList<>();
                            for(int i=0;i<jsonArray.size();i++){
                                JSONObject job = jsonArray.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                                paths.add(Constants.MAIN_ENGINE+"file/"+job.get("imagePath"));
                            }
                            Log.e(TAG, "paths================: "+paths);
                            recyclerView.setAdapter(new PicAdapter(paths));
                        }
                    }

                    @Override
                    public void onMyFailure(String content) {
                        Log.e(TAG, "查询图片组返回错误信息：" + content);
                        Utils.showLongToast(HiddenDangerDetailManagementActivity.this, content);
                    }
                });
            }
        }catch (Exception e) {
            Utils.showLongToast(HiddenDangerDetailManagementActivity.this, e.toString());
        }
    }

    private void timer1(){
        timer=new Timer();
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addRecordWatch();
                    }
                });
            }
        };
        timer.schedule(task,4000);
    }

    private void addRecordWatch(){
       String recordId = record.getId();
       String userId = UserUtils.getUserID(HiddenDangerDetailManagementActivity.this);
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("recordId",recordId);
        paramsMap.put("userId",userId);
        RequestParams params = new RequestParams();
        String jsonString = JSON.toJSONString(paramsMap);
        params.put("recordWatchJsonData", jsonString);
        netClient.post(Data.getInstance().getIp() + Constants.ADD_RECORDWATCH, params, new BaseJsonRes() {

            @Override
            public void onMySuccess(String data) {
                Log.i(TAG, "查询添加记录：" + data);
                if (!TextUtils.isEmpty(data)) {
                    Log.e(TAG, "data111================: "+data);
                }else{
                    Log.e(TAG, "data222================: "+data);
                }
                timer.cancel();
            }

            @Override
            public void onMyFailure(String content) {
                Log.e(TAG, "查询图片组返回错误信息：" + content);
                Utils.showLongToast(HiddenDangerDetailManagementActivity.this, content);
            }
        });
    }
}
