package com.cxt.gps.UI.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.acker.simplezxing.activity.CaptureActivity;
import com.cxt.gps.R;
import com.cxt.gps.presenter.DeviceInstallPresenter;
import com.cxt.gps.util.ImageUtils;
import com.cxt.gps.util.LowToUpper;
import com.cxt.gps.util.ShowUtils;
import com.cxt.gps.util.UploadImageAdapter;
import com.cxt.gps.view.IDeviceInstallView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import timber.log.Timber;

public class DeviceInstallActivity extends SuperActivity implements View.OnClickListener,IDeviceInstallView {
    private TextView tv_title;
    private ImageView iv_back;
    private EditText et_vin;
    private EditText et_carNum;
    private ImageView iv_scanner;
    private Button bt_next;
    private GridView gv_image_carNum;
    private GridView gv_image_carBody;
    private SharedPreferences sharedPreferences;
    private String account;
    private DeviceInstallPresenter presenter = new DeviceInstallPresenter(this);
    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;
    /**
     * 选择图片的返回码
     */
    public final static int SELECT_IMAGE_RESULT_CODE = 200;
    public final static int SELECT_IMAGE_RESULT_CODE_BODY= 222;
    public String mImagePath;
    String[] proj = {MediaStore.MediaColumns.DATA};
    private LinkedList<String> dataList = new LinkedList<String>();
    private LinkedList<String> dataList_body = new LinkedList<String>();
    private UploadImageAdapter adapter;
    private UploadImageAdapter adapter_body;
    private String vin;
    private boolean flag = false;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_device_install);
        initView();
        init();
        initEvents();
    }
    public void initView(){
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        et_carNum = findViewById(R.id.et_carNum);
        et_vin = findViewById(R.id.et_vin);
        iv_scanner = findViewById(R.id.iv_scanner);
        gv_image_carBody = findViewById(R.id.gv_image_carBody);
        gv_image_carNum = findViewById(R.id.gv_image_carNum);
        bt_next = findViewById(R.id.bt_next);
    }
    public void init(){
        sharedPreferences = getSharedPreferences("first_pref", Context.MODE_PRIVATE);
        account = sharedPreferences.getString("account","");
        tv_title.setText(getResources().getString(R.string.device_install));
        et_carNum.setTransformationMethod(new LowToUpper());
    }
    public void initEvents(){
        iv_back.setOnClickListener(this);
        bt_next.setOnClickListener(this);
        iv_scanner.setOnClickListener(this);
        et_vin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    String regex = "[0-9A-Za-z]{17}";
                    if (!hasFocus) {
                        vin = et_vin.getText().toString();
                        if (vin.isEmpty()) {
                            ShowUtils.showCustomToast(getApplicationContext(), "车架号不能为空!");
                        } else {
                            if (vin.matches(regex)) {
                                ShowUtils.showCustomToast(getApplicationContext(), "上传前请仔细核对车架号！");
                                flag = true;
                            } else {
                                ShowUtils.showCustomToast(getApplicationContext(), "车架号格式不对！");
                            }
                        }
                    }
                }
            });
            gv_image_carNum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if (parent.getItemAtPosition(position) == null) { // 添加图片
                            if (Build.VERSION.SDK_INT > 22) {
                                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    //先判断有没有权限 ，没有就在这里进行权限的申请
                                    ActivityCompat.requestPermissions(getParent(),
                                            new String[]{Manifest.permission.CAMERA}, SELECT_IMAGE_RESULT_CODE);
                                } else {
                                    //说明已经获取到摄像头权限了 想干嘛干嘛
                                    showPictureDailog(SELECT_IMAGE_RESULT_CODE);//Dialog形式
                                    //showPicturePopupWindow();// PopupWindow形式
                                }
                            } else {
                                //这个说明系统版本在6.0之下，不需要动态获取权限。
                                showPictureDailog(SELECT_IMAGE_RESULT_CODE);//Dialog形式
                                //showPicturePopupWindow();// PopupWindow形式
                            }
                        }
                    } catch (Exception e) {
                        Timber.d(e.getMessage());
                    }

                }
            });
            gv_image_carNum.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getItemAtPosition(position) != null) { // 长按删除
                        dataList.remove(parent.getItemAtPosition(position));
                        adapter.update(dataList);
                    }
                    return true;
                }
            });
            gv_image_carBody.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if (parent.getItemAtPosition(position) == null) { // 添加图片
                            if (Build.VERSION.SDK_INT > 22) {
                                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    //先判断有没有权限 ，没有就在这里进行权限的申请
                                    ActivityCompat.requestPermissions(getParent(),
                                            new String[]{Manifest.permission.CAMERA}, SELECT_IMAGE_RESULT_CODE_BODY);
                                } else {
                                    //说明已经获取到摄像头权限了 想干嘛干嘛
                                    showPictureDailog(SELECT_IMAGE_RESULT_CODE_BODY);//Dialog形式
                                    //showPicturePopupWindow();// PopupWindow形式
                                }
                            } else {
                                //这个说明系统版本在6.0之下，不需要动态获取权限。
                                showPictureDailog(SELECT_IMAGE_RESULT_CODE_BODY);//Dialog形式
                                //showPicturePopupWindow();// PopupWindow形式
                            }
                        }
                    } catch (Exception e) {
                        Timber.d(e.getMessage());
                    }
                }
            });
            gv_image_carBody.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getItemAtPosition(position) != null) { // 长按删除
                        dataList_body.remove(parent.getItemAtPosition(position));
                        adapter_body.update(dataList_body);
                    }
                    return true;
                }
            });

        dataList.addLast(null);// 初始化第一个添加按钮数据
        adapter = new UploadImageAdapter(this, dataList);
        gv_image_carNum.setAdapter(adapter);
        dataList_body.addLast(null);
        adapter_body = new UploadImageAdapter(this, dataList_body);
        gv_image_carBody.setAdapter(adapter_body);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_scanner:
                scanner();
                break;
            case R.id.bt_next:
                et_vin.clearFocus();
                if (flag) {
                    vin = et_vin.getText().toString().trim();
                    String carNum = et_carNum.getText().toString().trim();
                    presenter.bindData("REQUEST_UPLOAD_CAR_INFO", account, vin, carNum);
                }
                break;
        }
    }
    public void scanner(){
        if (Build.VERSION.SDK_INT > 22){
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                //先判断有没有权限 ，没有就在这里进行权限的申请
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},REQUEST_CODE);

            }else {
                //说明已经获取到摄像头权限了 想干嘛干嘛
                startCaptureActivityForResult();
            }
        }else {
            //这个说明系统版本在6.0之下，不需要动态获取权限。
           startCaptureActivityForResult();
        }


    }

    /**
     * 拍照或从图库选择图片(Dialog形式)
     */
    public void showPictureDailog(final int CODE) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(new String[] { "拍摄照片", "选择照片", "取消" },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0://拍照
                                take_photo(CODE);
                                break;
                            case 1://相册选择图片
                                pickPhoto(CODE);
                                break;
                            case 2://取消
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    /**
     * 拍照获取图片
     */
    public void take_photo(int CODE) {
        File outputImagepath;
        //获取系統版本
        int currentapiVersion = Build.VERSION.SDK_INT;
        // 激活相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                    "yyyy_MM_dd_HH_mm_ss");
            String filename = timeStampFormat.format(new Date());
            outputImagepath = new File(Environment.getExternalStorageDirectory(),
                    filename + ".jpg");
            mImagePath = outputImagepath.getAbsolutePath();
            if (currentapiVersion < 24) {
                // 从文件中创建uri
                Uri uri = Uri.fromFile(outputImagepath);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, CODE);
            } else {
                //兼容android7.0 使用共享文件的形式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, outputImagepath.getAbsolutePath());
                Uri uri = getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, CODE);
            }
            // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA

        }else {
            ShowUtils.showCustomToast(this, "内存卡不存在！");
        }

    }
    /***
     * 从相册中取图片
     */
    private void pickPhoto(int CODE) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, CODE);
    }


    private void startCaptureActivityForResult() {
        Intent intent = new Intent(this, CaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(CaptureActivity.KEY_NEED_BEEP, CaptureActivity.VALUE_BEEP);
        bundle.putBoolean(CaptureActivity.KEY_NEED_VIBRATION, CaptureActivity.VALUE_VIBRATION);
        bundle.putBoolean(CaptureActivity.KEY_NEED_EXPOSURE, CaptureActivity.VALUE_NO_EXPOSURE);
        bundle.putByte(CaptureActivity.KEY_FLASHLIGHT_MODE, CaptureActivity.VALUE_FLASHLIGHT_OFF);
        bundle.putByte(CaptureActivity.KEY_ORIENTATION_MODE, CaptureActivity.VALUE_ORIENTATION_AUTO);
        bundle.putBoolean(CaptureActivity.KEY_SCAN_AREA_FULL_SCREEN, CaptureActivity.VALUE_SCAN_AREA_FULL_SCREEN);
        bundle.putBoolean(CaptureActivity.KEY_NEED_SCAN_HINT_TEXT, CaptureActivity.VALUE_SCAN_HINT_TEXT);
        intent.putExtra(CaptureActivity.EXTRA_SETTING_BUNDLE, bundle);
        startActivityForResult(intent, CaptureActivity.REQ_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            ShowUtils.showCustomToast(getApplicationContext(), "需要相机权限才能使用拍照！");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //图片地址
        Cursor cursor=null;
        Uri uri = null;
        String imagePath="";
        if (requestCode == SELECT_IMAGE_RESULT_CODE && resultCode == RESULT_OK) {

            if (data != null && data.getData() != null) {// 有数据返回直接使用返回的图片地址
                uri = data.getData();
                cursor = getContentResolver().query(uri, proj, null,
                        null, null);
                if (cursor == null) {
                    uri = ImageUtils.getUri(this, data);
                }
                imagePath = ImageUtils.getFilePathByFileUri(this, uri);
            } else {// 无数据使用指定的图片路径
                imagePath = mImagePath;
            }
            dataList.addFirst(imagePath);
            adapter.update(dataList); // 刷新图片
            if (flag) {
                presenter.uploadImage(this, dataList,"REQUEST_UPLOAD_IMAGE2", account, vin, 1);
            }
            //直接写这个会出错，找了半天才发现
            //cursor.close();
            if (cursor!=null){
                cursor.close();
            }
        }
        if (requestCode == SELECT_IMAGE_RESULT_CODE_BODY && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {// 有数据返回直接使用返回的图片地址
                uri = data.getData();
                cursor = getContentResolver().query(uri, proj, null,
                        null, null);
                if (cursor == null) {
                    uri = ImageUtils.getUri(this, data);
                }
                imagePath = ImageUtils.getFilePathByFileUri(this, uri);
            } else {// 无数据使用指定的图片路径
                imagePath = mImagePath;
            }
            dataList_body.addFirst(imagePath);
            adapter_body.update(dataList_body); // 刷新图片
            for(int i = 0; i < dataList.size();i++){
                    presenter.uploadImage(this, dataList_body,"REQUEST_UPLOAD_IMAGE2", account, vin, 2+i);
            }
            //直接写这个会出错，找了半天才发现
            //cursor.close();
            if (cursor!=null){
                cursor.close();
            }
        }
        if (requestCode == CaptureActivity.REQ_CODE){
            if (resultCode == RESULT_OK){
                et_vin.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
            }else if (resultCode == RESULT_CANCELED){
                et_vin.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
            }
        }
    }

    @Override
    public void toMainActivity(String result) {
        try{
            if (result.equals("0")){
                ShowUtils.showCustomToast(this,"车辆信息上传失败！");
            }else if(result.equals("1")){
                ShowUtils.showCustomToast(this,"信息上传成功！");
                Intent intent = new Intent(this,AddDeviceActivity.class);
                intent.putExtra("vin",et_vin.getText().toString());
                intent.putExtra("carNum",et_carNum.getText().toString());
                startActivity(intent);
            }else if (result.equals("01")){
                ShowUtils.showCustomToast(this,"车架号已存在！");
                Intent intent = new Intent(this,AddDeviceActivity.class);
                intent.putExtra("vin",et_vin.getText().toString());
                intent.putExtra("carNum",et_carNum.getText().toString());
                startActivity(intent);
            }

        }catch (Exception e){
            Timber.d(e.getMessage());
        }
    }

    @Override
    public void showFailedError() {

    }

    @Override
    public void toShowUploadActivity(String result) {
        try{
            if (result.equals("0")){
                ShowUtils.showCustomToast(this,"图片上传失败！");
            }else if(result.equals("1")){
                ShowUtils.showCustomToast(this,"图片上传成功！");

            }else if (result.equals("2")){
                ShowUtils.showCustomToast(this,"图片保存不成功！");
            }

        }catch (Exception e){
            Timber.d(e.getMessage());
        }
    }
}


