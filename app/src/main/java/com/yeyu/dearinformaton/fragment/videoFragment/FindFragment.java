package com.yeyu.dearinformaton.fragment.videoFragment;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yeyu.dearinformaton.MyApplication;
import com.yeyu.dearinformaton.R;
import com.yeyu.dearinformaton.activity.video.FinddetailActivity;
import com.yeyu.dearinformaton.entity.video.FindMoreEntity;
import com.yeyu.dearinformaton.utils.video.CommonAdapter;
import com.yeyu.dearinformaton.utils.video.HttpAdress;
import com.yeyu.dearinformaton.utils.video.JsonParseUtils;
import com.yeyu.dearinformaton.utils.video.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gaoyehua on 2016/9/19.
 */
public class FindFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.find_grid)
    GridView findGrid;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private List<FindMoreEntity> dataEntities=new ArrayList<>();
    private View view;
    public FindFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_find, container, false);

        ButterKnife.bind(this, view);
        initSwipeRefreshLayout();
        initData();
        setListener();
        return view;
    }
    //设置事件监听
    private void setListener() {
        findGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FindMoreEntity entity = dataEntities.get(position);
                Intent intent=new Intent(MyApplication.getAppContext(),FinddetailActivity.class);
                intent.putExtra("name",entity.getName());
                startActivity(intent);
            }
        });
    }

    /*
    初始化刷新控件
     */
    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.gplus_colors));
    }

    //初始化数据
    private void initData() {
        RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
        //下载json数据
        StringRequest request = new StringRequest(HttpAdress.FIND_MORE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    //设置适配器
    private void setAdapter(List<FindMoreEntity> dataEntities) {
        findGrid.setAdapter(new CommonAdapter<FindMoreEntity>(MyApplication.getAppContext(),dataEntities,R.layout.grid_item) {
            @Override
            public void convert(ViewHolder viewHolder, FindMoreEntity dataEntity) {
                viewHolder.setText(R.id.grid_tv, dataEntity.getName());
                viewHolder.setImageResourcewithFresco(R.id.grid_iv, Uri.parse(dataEntity.getBgPicture()));
            }
        });
    }
    //解析json数据
    private void parseJson(String jsonData){
        List<FindMoreEntity> entities = JsonParseUtils.parseFromJson(jsonData);
        dataEntities.addAll(entities);
        //给适配器设置数据
        setAdapter(dataEntities);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.bind(this,view).unbind();
    }

    @Override
    public void onRefresh() {
        initData();
    }
}
