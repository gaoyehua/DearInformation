package com.yeyu.dearinformaton.fragment.videoFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.yeyu.dearinformaton.R;
import com.yeyu.dearinformaton.activity.video.VideoDetailActivity;
import com.yeyu.dearinformaton.entity.video.HotStraetgyEntity;
import com.yeyu.dearinformaton.utils.video.CommonAdapter;
import com.yeyu.dearinformaton.utils.video.HttpAdress;
import com.yeyu.dearinformaton.utils.video.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gaoyehua on 2016/9/19.
 */
public class CommonHotFragment extends Fragment {

    private List<HotStraetgyEntity.ItemListEntity> itemListEntities = new ArrayList<>();
    @BindView(R.id.hot_listview)
    ListView hotListview;
    //排行 周排行 月排行 总排行
    private static final String[] STRATEGY = new String[]{
            "weekly", "monthly", "historical"
    };
    private View inflate;

    public CommonHotFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_common_hot_fragment, container, false);
        //获取控件
        ButterKnife.bind(this, inflate);

        initView();
        initData();
        setListener();
        return inflate;
    }

    //初始化控件
    private void initView() {
        //添加底部布局
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_footer, null);
        hotListview.addFooterView(view,null,false);
    }


    //初始化数据
    private void initData() {

        //获取排行
        int position = FragmentPagerItem.getPosition(getArguments());
        Log.i("---->position",position+"");
        String stretary = STRATEGY[position];
        //获取到排行请求地址
        String url = String.format(HttpAdress.HOT_STRATEGY, stretary);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pareJson(response);
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
    private void setAdapter(final List<HotStraetgyEntity.ItemListEntity> dataEntity) {
        final int[] i = {0};//设置数据的序号
        hotListview.setAdapter(new CommonAdapter<HotStraetgyEntity.ItemListEntity>(getContext(), dataEntity, R.layout.list_hot_item) {
            @Override
            public void convert(ViewHolder viewHolder, HotStraetgyEntity.ItemListEntity itemListEntity) {
                viewHolder.setText(R.id.tv_title, itemListEntity.getData().getTitle());
                //获取时间
                int duration = itemListEntity.getData().getDuration();
                int mm = duration / 60;//分
                int ss = duration % 60;//秒
                String second = "";//秒
                String minute = "";//分
                if (ss < 10) {
                    second = "0" + String.valueOf(ss);
                } else {
                    second = String.valueOf(ss);
                }
                if (mm < 10) {
                    minute = "0" + String.valueOf(mm);
                } else {
                    minute = String.valueOf(mm);//分钟
                }
                viewHolder.setText(R.id.tv_time, "#" + itemListEntity.getData().getCategory() + " / " + minute + "'" + second + '"');
                viewHolder.setImageResourcewithFresco(R.id.iv, Uri.parse(itemListEntity.getData().getCover().getFeed()));

                if (i[0]<dataEntity.size()) {
                    viewHolder.setText(R.id.hot_tv_textnumber, ++i[0] +".");
                }
            }
        });

    }

    //解析json数据
    private void pareJson(String jsonData) {
        HotStraetgyEntity hotStraetgyEntity = new Gson().fromJson(jsonData, HotStraetgyEntity.class);
        itemListEntities.addAll(hotStraetgyEntity.getItemList());
        //设置适配器
        setAdapter(itemListEntities);
    }

    //设置事件监听
    private void setListener() {
        //listview点击事件
        hotListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "单击事件", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getContext(), VideoDetailActivity.class);
                Bundle bundle=new Bundle();
                Log.i("--->",position+"");
                HotStraetgyEntity.ItemListEntity.DataEntity data = itemListEntities.get(position).getData();
                bundle.putString("title", data.getTitle());
                //获取到时间
                int duration = data.getDuration();
                int mm = duration / 60;//分
                int ss = duration % 60;//秒
                String second = "";//秒
                String minute = "";//分
                if (ss < 10) {
                    second = "0" + String.valueOf(ss);
                } else {
                    second = String.valueOf(ss);
                }
                if (mm < 10) {
                    minute = "0" + String.valueOf(mm);
                } else {
                    minute = String.valueOf(mm);//分钟
                }
                bundle.putString("time", "#" + data.getCategory()+ " / " + minute + "'" + second + '"');
                bundle.putString("desc",data.getDescription());//视频描述
                bundle.putString("blurred",data.getCover().getBlurred());//模糊图片地址
                bundle.putString("feed",data.getCover().getFeed());//图片地址
                bundle.putString("video",data.getPlayUrl());//视频播放地址
                bundle.putInt("collect",data.getConsumption().getCollectionCount());//收藏量
                bundle.putInt("share",data.getConsumption().getShareCount());//分享量
                bundle.putInt("reply",data.getConsumption().getReplyCount());//回复数量
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

       /* hotListview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LinearLayout ll_moban = (LinearLayout) v.findViewById(R.id.ll_moban);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://手指抬起
                        ll_moban.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_DOWN://手指按下
                        ll_moban.setVisibility(View.GONE);
                        break;
                    case MotionEvent.ACTION_MOVE://手指离开
                        ll_moban.setVisibility(View.VISIBLE);
                    default:
                        ll_moban.setVisibility(View.VISIBLE);
                        break;
                }
                return false;
            }
        });*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.bind(this,inflate).unbind();
    }
}

