package com.caobo.app;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.caobo.app.adpter.GankIoWelfareAdapter;
import com.caobo.app.db.DBOpenHelper;
import com.caobo.app.db.manager.BaseModelManager;
import com.caobo.app.model.FileBean;
import com.caobo.app.utils.PreferenceUtil;
import com.caobo.app.utils.RxHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements BaseQuickAdapter.RequestLoadMoreListener{

    private int page =0;
    private int id =0;
    private RecyclerView rvGankIoWelfare;
    private GankIoWelfareAdapter mGankIoWelfareAdapter;
    private View loadingView;
    private FileBean fileBean = new FileBean();
    private DBOpenHelper dbOpenHelper;
    private MyRunable myRunable;
    ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(3));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getDatabaseHelper(this);
        myRunable = new MyRunable();
        loadingView = findViewById(R.id.layout);
        rvGankIoWelfare = findViewById(R.id.rv_gankio_welfare);

        mGankIoWelfareAdapter = new GankIoWelfareAdapter(R.layout.item_gank_io_welfare);
//        rvGankIoWelfare.setAdapter(mGankIoWelfareAdapter);
        if(!PreferenceUtil.readBoolean(MainActivity.this, PreferenceUtil.PRE_SHARE_APP)){
            poolExecutor.execute(myRunable);
        }else {
            setDataForView();
        }
    }

    private void setDataForView() {
        Observable.create(new ObservableOnSubscribe<List<FileBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<FileBean>> emitter) throws Exception {
                List<FileBean> fileBeans = BaseModelManager.getInstance().getPageModels(FileBean.class, DBOpenHelper.class,
                        MainActivity.this, page, 20, "time", true);
                emitter.onNext(fileBeans);
                emitter.onComplete();
            }
        }).compose(RxHelper.<List<FileBean>>rxSchedulerHelper())
                .subscribe(new Consumer<List<FileBean>>() {
            @Override
            public void accept(List<FileBean> fileBeans) throws Exception {
                loadingView.setVisibility(View.GONE);
                if(fileBeans.size()>0){
                    page++;
                }else {
                    mGankIoWelfareAdapter.loadMoreEnd(false);
                }
                if (mGankIoWelfareAdapter.getData().size() == 0) {
                    initRecycleView(fileBeans);
                } else {
                    mGankIoWelfareAdapter.addData(fileBeans);
                }


            }
        });
    }

    private void initRecycleView(List<FileBean> list) {
        mGankIoWelfareAdapter = new GankIoWelfareAdapter(R.layout.item_gank_io_welfare, list);
        mGankIoWelfareAdapter.setOnLoadMoreListener(this, rvGankIoWelfare);
        rvGankIoWelfare.setAdapter(mGankIoWelfareAdapter);
        rvGankIoWelfare.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("keydown","onKeyDown");
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("keydown","onTouchEvent");
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e("keydown","dispatchKeyEvent");
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    class MyRunable implements Runnable{
            @Override
            public void run() {
                getAllCursor();
            }
    }


    private void getAllCursor() {
        List<FileBean> fileBeans = new ArrayList<>();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Images.Media.DATE_MODIFIED);
        assert cursor != null;
        while (cursor.moveToNext()) {
            //获取图片的名称
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            //获取图片的生成日期
            byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            String date_modified = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
            fileBean.setId(id++);
            fileBean.setUrl(new String(data, 0, data.length - 1));
            fileBean.setTime(date_modified);
            fileBeans.add(fileBean);
            BaseModelManager.getInstance().saveOrUpdateModel(FileBean.class,MainActivity.this,fileBean,
                    DBOpenHelper.class);
        }
        PreferenceUtil.write(MainActivity.this,PreferenceUtil.PRE_SHARE_APP,true);
        setDataForView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        poolExecutor.remove(myRunable);
        poolExecutor.shutdownNow();
    }


    public DBOpenHelper getDatabaseHelper(Context context) {
        if (dbOpenHelper == null) {
            dbOpenHelper = new DBOpenHelper(context);
        }
        return dbOpenHelper;
    }


    @Override
    public void onLoadMoreRequested() {
        loadingView.setVisibility(View.GONE);
        mGankIoWelfareAdapter.loadMoreComplete();
        setDataForView();
    }
}