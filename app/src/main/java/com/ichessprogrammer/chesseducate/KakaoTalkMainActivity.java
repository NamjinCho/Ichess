package com.ichessprogrammer.chesseducate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class KakaoTalkMainActivity extends Activity {
    private SessionCallback callback;
    Button buttonLogin;

    /**
     * 로그인 버튼을 클릭 했을시 access token을 요청하도록 설정한다.
     *
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_common_kakao_login);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        RelativeLayout root = (RelativeLayout)findViewById(R.id.RootView);
        ImageView img = (ImageView) findViewById(R.id.background);
        img.setImageBitmap(ImageSampling.decodeSampledBitmapFromResource(getResources(),R.drawable.bg_basic,
                width,height));
        sharedPreferences = getSharedPreferences("Ichess", Activity.MODE_WORLD_WRITEABLE);
        //buttonLogin = (Button)findViewById(R.id.buttonLogin);
        String ID = sharedPreferences.getString("ID","null");
        if(!ID.equals("null"))
        {
            Log.d("?","?");
            Intent intent = new Intent(getApplicationContext(),FirstLoadingActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        Log.d("디벅",ID);
        Log.d("해쉬키",getKeyHash(getApplicationContext()));
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }
    public void loginWithKakao(View v)
    {
        Session.getCurrentSession().open(AuthType.KAKAO_TALK, this);
    }
    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w("Hash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }


    public void showLoginDialog(View v)
    {
        startActivityForResult(new Intent(KakaoTalkMainActivity.this,OtherLoginActivity.class),5028);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==5028)
        {
            if(resultCode==RESULT_OK)
            {
                startActivity(new Intent(KakaoTalkMainActivity.this,FirstLoadingActivity.class));
                finish();
            }
            return;
        }
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /*

    * */
    @Override
    protected void onDestroy() {
         ImageView imageView = (ImageView) findViewById(R.id.background);
        ((BitmapDrawable) imageView.getDrawable()).getBitmap().recycle();
        imageView.setImageBitmap(null);
        System.gc();
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            redirectSignupActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception);
            }
        }
    }
    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }


    protected void redirectSignupActivity() {
        Log.d("로그인 시도","로그인");
        final Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
    }
}