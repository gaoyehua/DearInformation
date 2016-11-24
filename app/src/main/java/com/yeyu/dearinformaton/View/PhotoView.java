package com.yeyu.dearinformaton.View;

import com.yeyu.dearinformaton.Constant.LoadNewsType;
import com.yeyu.dearinformaton.View.BaseView.BaseView;
import com.yeyu.dearinformaton.entity.PhotoGirl;

import java.util.List;

/**
 * Created by gaoyehua on 2016/9/11.
 */
public interface PhotoView extends BaseView {
    void setPhotoList(List<PhotoGirl> photoGirls, @LoadNewsType.checker int loadType);
}
