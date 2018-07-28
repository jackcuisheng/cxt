package com.cxt.gps.presenter;

import android.os.Handler;

import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.model.MessageModel;
import com.cxt.gps.model.impl.MessageModelImpl;
import com.cxt.gps.view.IMessageView;

import java.util.HashMap;
import java.util.List;

public class MessageManagePresenter {
    private MessageModel messageModel;
    private IMessageView iMessageView;
    private Handler mHandler = new Handler() {
    };
    public MessageManagePresenter(IMessageView iMessageView) {
        this.iMessageView = iMessageView;
        this.messageModel = new MessageModelImpl();
    }

    public void loadDataList(String account,String requestType,int pageNum,int num) {
        iMessageView.showLoading();
        messageModel.loadDataList(account,requestType,pageNum,num, new OnListCarListener() {
            @Override
            public void successed(final List<HashMap<String, String>> list) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iMessageView.toShowListActivity(list);
                        iMessageView.hideLoading();
                    }
                });

            }

            @Override
            public void failed() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iMessageView.showFailedError();
                        iMessageView.hideLoading();
                    }
                });
            }
        });
    }

    public void query(String msgType,String account,String query) {
        messageModel.query(msgType,account,query, new OnListCarListener() {
            @Override
            public void successed(final List<HashMap<String, String>> list) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iMessageView.toShowQueryListActivity(list);
                    }
                });

            }

            @Override
            public void failed() {

            }
        });
    }
}
