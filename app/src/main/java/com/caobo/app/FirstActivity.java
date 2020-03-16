package com.caobo.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.caobo.app.utils.AndroidUtil;
import com.caobo.app.utils.RxHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by caobo
 */
public class FirstActivity extends AppCompatActivity {

    private List<ResolveInfo> apps;

    private GridView apps_list;
    // master分支
    private String master = "master";
    // feature1分支
    private String feature1 = "feature1";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        apps_list = findViewById(R.id.apps_list);
        loadApps();
//        requestPermissions();
    }

    private void loadApps() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        apps = getPackageManager().queryIntentActivities(mainIntent, 0);

        apps_list.setAdapter(new AppsAdapter());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(FirstActivity.this);
        rxPermission.request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (!granted) {
                        }
                        initCountDown();
                    }
                });
    }

    private void initCountDown() {
        Observable.interval(1, TimeUnit.SECONDS)
                .take(2)//计时次数
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return 2 - aLong;// 3-0 3-2 3-1
                    }
                })
                .compose(RxHelper.<Long>rxSchedulerHelper())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Long value) {
                        //                        Logger.e("value = " + value);
                        String s = String.valueOf(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        startActivity(new Intent(FirstActivity.this, MainActivity.class));
                        finish();
                    }
                });
    }


    public class AppsAdapter extends BaseAdapter {

        public AppsAdapter(){
        }

        @Override
        public int getCount() {
            return apps.size();
        }

        @Override
        public Object getItem(int i) {
            return apps.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (view == null) {
                view = LayoutInflater.from(FirstActivity.this).inflate(R.layout.layout_grid_item, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.itemImg = (ImageView) view.findViewById(R.id.iv_head);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.itemImg.setLayoutParams(new RelativeLayout.LayoutParams(
                    (AndroidUtil.getDisplayW(FirstActivity.this)-30)/4,
                    (AndroidUtil.getDisplayW(FirstActivity.this)-30)/4));
            ResolveInfo info = apps.get(i);
            viewHolder.itemImg.setImageDrawable(info.activityInfo.loadIcon(getPackageManager()));
//            Glide.with(FirstActivity.this)
//                    .load(setByte(info.activityInfo.loadIcon(getPackageManager())))
//                    .crossFade(500)
//                    .placeholder(R.mipmap.img_default_meizi)
//                    .into(viewHolder.itemImg);
            return view;
        }

        class ViewHolder {
            ImageView itemImg;
        }
    }


    public byte [] setByte(Drawable icon){
        Bitmap bitmap = ((BitmapDrawable)icon).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}



