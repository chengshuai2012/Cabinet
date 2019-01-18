package com.link.cloud;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.link.cloud.bean.CabinetUserDatail;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by 49488 on 2019/1/18.
 */

public class UserCabinetDetails extends Activity{

    private RealmResults<CabinetUserDatail> cabinetUserDatails;
    ArrayList<CabinetUserDatail> cabinetUserDatailArrayList = new ArrayList<>();
    private EditText no;
    private Realm realm;
    private MyBaseAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_detail);
        no = findViewById(R.id.cabinetNo);
        findViewById(R.id.open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        realm = Realm.getDefaultInstance();
        findViewById(R.id.query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = no.getText().toString();
                if(TextUtils.isEmpty(text)){
                    Toast.makeText(UserCabinetDetails.this,"请输入柜号",Toast.LENGTH_LONG).show();
                    return;
                };
                cabinetUserDatails = realm.where(CabinetUserDatail.class).equalTo("cabinetNo",text).findAll().sort("creatTime", Sort.DESCENDING);
                cabinetUserDatailArrayList.clear();
                cabinetUserDatailArrayList.addAll(realm.copyFromRealm(cabinetUserDatails));
                adapter.notifyDataSetChanged();
            }
        });


        ListView viewById = findViewById(R.id.lv);
        adapter = new MyBaseAdapter();
        viewById.setAdapter(adapter);
    }
    class MyBaseAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return cabinetUserDatailArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 =null;
            if(view==null){
                view1 = View.inflate(UserCabinetDetails.this,R.layout.user_detail,null);
            }else {
                view1= view;
            }
            TextView name = view1.findViewById(R.id.name);
            TextView phone = view1.findViewById(R.id.phone);
            TextView time = view1.findViewById(R.id.time);
            TextView open_o = view1.findViewById(R.id.open_o);
            name.setText(cabinetUserDatailArrayList.get(i).getUserName());
            phone.setText(cabinetUserDatailArrayList.get(i).getPhoneNum());
            time.setText(cabinetUserDatailArrayList.get(i).getUserTime());
            open_o.setText(cabinetUserDatailArrayList.get(i).getOpenOrClose());
            return view1;
        }
    }
}
