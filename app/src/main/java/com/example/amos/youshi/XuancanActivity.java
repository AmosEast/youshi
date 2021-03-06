package com.example.amos.youshi;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 此界面为加餐界面
 */

public class XuancanActivity extends BaseActivity {

    private List<XItem> xItemList = new ArrayList<>();
    private List<XOpt> xOptList = new ArrayList<>();
    private XuanchanAdapter adapter;
    private static final String TAG = "XuancanActivity";
    public float cur_calorie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuancan);

        //初始化选项列表
        String url = base_url + "/youshi/GetFoodController.php" ;

        String req = "type=肉蛋类" ;
        post_test(url, req, 1);
        initXOpts();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        final XuancanTypeAdapter xuancanTypeAdapter = new XuancanTypeAdapter(xOptList);
        recyclerView.setAdapter(xuancanTypeAdapter);

        //初始化选餐链表
        //initXItems("");
        adapter = new XuanchanAdapter(XuancanActivity.this, R.layout.xuancan_item, xItemList);
        ListView listView = (ListView) findViewById(R.id.view_list);
        listView.setAdapter(adapter);
        //监听点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                XItem xItem = xItemList.get(position);
                cur_calorie = Integer.parseInt(xItem.getSec_item()) ;
                customView();
            }
        });

        //recyclerView的点击事件

        xuancanTypeAdapter.setOnItemClickListener(new XuancanTypeAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                //adapter.clear();
//                initXItems(data);
                //adapter.notifyDataSetChanged();
                String url = base_url + "/youshi/GetFoodController.php" ;

                String req = "type=" + data ;
                post_test(url, req, 1);

            }
        });

        //设置返回点击监听事件
        super.ret_clicked(this);

    }

    /**
     * 初始化选项链表
     */
    private void initXOpts() {

        XOpt xOpt = new XOpt("肉蛋类", R.drawable.pic_1);
        xOptList.add(xOpt);
        xOpt = new XOpt("蔬菜", R.drawable.pic_2);
        xOptList.add(xOpt);
        xOpt = new XOpt("水果", R.drawable.pic_3);
        xOptList.add(xOpt);
        xOpt = new XOpt("零食", R.drawable.pic_4);
        xOptList.add(xOpt);
    }

    /**
     * 初始化选项链表数据
     */
    private void initXItems(String item) {
//        xItemList.clear();
        for(int i = 0; i < 20; i++) {
            XItem xItem = new XItem(item + "itme_" , item + "item_8" );
            xItemList.add(xItem);
        }
    }

    /**
     * numberPicker
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void customView() {
        NumberPickerDialog numberPickerDialog = new NumberPickerDialog(this,
                new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                        Log.e("ard", "所选值：" + picker.getValue() + "，原值：" + oldVal + "，新值：" + newVal); // 新值即所选值
                    }
                },
                3000, // 最大值
                20, // 最小值
                40,
                cur_calorie); // 默认值
        numberPickerDialog.setCurrentValue(55).show();
        //Toast.makeText(XuancanActivity.this, numberPickerDialog.getNewVal(), Toast.LENGTH_SHORT).show();
    }

    protected void success_1(android.os.Message msg) {
        try {

            adapter.clear();
            //Toast.makeText(XuancanActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
            JSONArray jsonArray = new JSONArray(msg.obj.toString());
            xItemList.clear();
            //ArrayList<XItem> itemArr = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String calorie = jsonObject.getString("calorie");
                XItem xItem = new XItem(name, calorie);
                xItemList.add(xItem);

//                initXItems(data);
                adapter.notifyDataSetChanged();
            }


        }catch (JSONException e) {
            Toast.makeText(XuancanActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            Log.d(TAG,e.toString());

        }
    }

    public float getCur_calorie() {
        return cur_calorie;
    }
}
