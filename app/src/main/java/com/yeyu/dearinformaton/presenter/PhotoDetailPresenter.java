package com.yeyu.dearinformaton.presenter;

import com.yeyu.dearinformaton.Constant.PhotoRequestType;
import com.yeyu.dearinformaton.presenter.BasePresenter.BasePresenter;

/**
 * Created by gaoyehua on 2016/9/11.
 */
public interface PhotoDetailPresenter extends BasePresenter {
    void handlePicture(String imageUrl, @PhotoRequestType.PhotoRequestTypeChecker int type);
}
