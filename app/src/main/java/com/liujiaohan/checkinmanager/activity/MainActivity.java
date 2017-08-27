package com.liujiaohan.checkinmanager.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.liujiaohan.checkinmanager.db.MessageDao;
import com.liujiaohan.checkinmanager.location.GaodeLocation;
import com.liujiaohan.checkinmanager.Message;
import com.liujiaohan.checkinmanager.network.NetManager;
import com.liujiaohan.checkinmanager.R;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.internal.Util;

public class MainActivity extends AppCompatActivity implements AMapLocationListener{

    List<String> permissionList=new ArrayList<>();
    TextView mNumberingText;
    TextView mLocationText;
    TextView mForestText;
    TextView mTimeText;
    TextView mLongitudeText;
    TextView mLatidudeText;
    EditText mAltitudeText;
    EditText mFemaleText;
    EditText mMaleText;
    EditText mAmountText;
    Button mCompleteBtn;
    Button mScanBtn;
    Button mSendFailBtn;
    GaodeLocation gaodeLocation;
    MessageDao mMessageDao;
    int mFemaleCount,mMaleCount,mTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this,"ed60276ea184d21b992ff897da7dd665");
        mMessageDao=new MessageDao(this);
        requestPermission();
        initUI();
        Log.i("TAG", "onCreate: "+sHA1(this));
        gaodeLocation=new GaodeLocation(getApplicationContext(),this);
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(this,  Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (permissionList.size()>0){
            String[] permission=new String[permissionList.size()];
            for (int i=0;i<permissionList.size();i++){
                permission[i]=permissionList.get(i);
            }
            ActivityCompat.requestPermissions(this,permission,1);
        }
    }

    private void initUI() {

        mSendFailBtn= (Button) findViewById(R.id.btn_send_fail);
        mScanBtn=(Button) findViewById(R.id.scanBtn);
        mNumberingText= (TextView) findViewById(R.id.Numbering);
        mLocationText= (TextView) findViewById(R.id.place);
        mForestText= (TextView) findViewById(R.id.forest);
        mTimeText= (TextView) findViewById(R.id.time);
        mLongitudeText= (TextView) findViewById(R.id.longitude);
        mLatidudeText= (TextView) findViewById(R.id.latitude);
        mAltitudeText= (EditText) findViewById(R.id.altitude);
        mFemaleText= (EditText) findViewById(R.id.female);
        mMaleText= (EditText) findViewById(R.id.male);
        mAmountText= (EditText) findViewById(R.id.count);
        mCompleteBtn= (Button) findViewById(R.id.done);

        mSendFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
            }
        });

        mFemaleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String content=s.toString();
                Log.i("TAG", "afterTextChanged: "+content);
                if (!content.equals("")){
                    mFemaleCount=Integer.parseInt(content);
                    if (!mMaleText.getText().toString().equals("")){
                        mTotal=mFemaleCount+mMaleCount;
                        mAmountText.setText(mTotal+"");
                    }
                }
            }
        });
        mMaleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String content=s.toString();
                Log.i("TAG", "afterTextChanged: "+content);
                if (!content.equals("")){
                    mMaleCount=Integer.parseInt(content);
                    if (!mFemaleText.getText().toString().equals("")){
                        mTotal=mFemaleCount+mMaleCount;
                        mAmountText.setText(mTotal+"");
                    }
                }
            }
        });

        mScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 0);

            }
        });

        mCompleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Message message=getMessage();
                final ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("发送中...");
                progressDialog.setCancelable(true);
                progressDialog.show();


                message.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e==null){
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this,"添加成功",Toast.LENGTH_LONG).show();
                            reset();
                        }else{
                            progressDialog.dismiss();
                            mMessageDao.addMessage(message);
                            Toast.makeText(MainActivity.this,"添加失败",Toast.LENGTH_LONG).show();
                        }
                    }
                });
//                NetManager.sendMessage(message, new NetManager.SendMessageCallback() {
//                    @Override
//                    public void onSended() {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                progressDialog.dismiss();
//                                reset();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onError() {
//                        Toast.makeText(MainActivity.this,"发送失败",Toast.LENGTH_LONG).show();
//
//                    }
//                });
            }
        });

    }

    private void reset(){
        mNumberingText.setText("");
        mLocationText.setText("");
        mForestText.setText("");
        mTimeText.setText("");
        mLongitudeText.setText("");
        mLatidudeText.setText("");
        mAltitudeText.setText("");
        mFemaleText.setText("");
        mMaleText.setText("");
        mAmountText.setText("");
    }

    private Message getMessage() {
        String numbering=mNumberingText.getText().toString();
        String location=mLocationText.getText().toString();
        String forest=mForestText.getText().toString();
        String time=mTimeText.getText().toString();
        String longitude=mLongitudeText.getText().toString();
        String latitude=mLatidudeText.getText().toString();
        String altitude=mAltitudeText.getText().toString();
        String femaleCount=mFemaleText.getText().toString();
        String maleCount=mMaleText.getText().toString();
        String total=mAmountText.getText().toString();
        Message message=new Message(numbering,location,forest,time,longitude,latitude,altitude,femaleCount,
                maleCount,total);
        return message;
    };


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (!isMatch(result)){
                        Toast.makeText(MainActivity.this,"二维码有误",Toast.LENGTH_LONG).show();
                        return;
                    }
                    String[] s=result.split("%");

                    mNumberingText.setText(s[0]);
                    mLocationText.setText(s[1]);
                    mForestText.setText(s[2]);
                    AMapLocation location=gaodeLocation.getLastKnowLocation();
                  //  Location location=LocationUtil.getLocation(getApplicationContext());
                    if (location!=null){
                        mLongitudeText.setText(location.getLongitude()+" ");
                        mLatidudeText.setText(location.getLatitude()+" ");
                    }
                    else {
                        mLongitudeText.setText("获取不到");
                        mLatidudeText.setText("获取不到");
                        Log.i("TAG", "onActivityResult: "+location.getErrorInfo());
                    }
                    SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
                    Date date=new Date(System.currentTimeMillis());
                    String time=format.format(date);
                    mTimeText.setText(time);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private boolean isMatch(String result) {
        String regex=".*%.*%.*";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(result);
        if (matcher.matches()){
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length>0)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted

            } else {
                // Permission Denied
                //  displayFrameworkBugMessageAndExit();
                Toast.makeText(this, "请在应用管理中打开“相机”访问权限！", Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == 2) {
            if (grantResults.length>0)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted

            } else {
                // Permission Denied
                //  displayFrameworkBugMessageAndExit();
                Toast.makeText(this, "请在应用管理中打开“GPS”访问权限！", Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == 3) {
            if (grantResults.length>0)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted

            } else {
                // Permission Denied
                //  displayFrameworkBugMessageAndExit();[
                Toast.makeText(this, "请在应用管理中打开“GPS”访问权限！", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation!=null){
            if (aMapLocation.getErrorCode()!=0){
                Log.e("错误", "onLocationChanged: "+"错误代码为"+
                        aMapLocation.getErrorCode()+"错误信息为"+aMapLocation.getErrorInfo());
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"获取不到了",Toast.LENGTH_LONG);
        }
    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result=hexString.toString();
            return result.substring(0, result.length()-1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
