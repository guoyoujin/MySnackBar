package com.trycatch.mycustomsnackbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.trycatch.mysnackbar.Prompt;
import com.trycatch.mysnackbar.TSnackbar;

public class MainActivity extends BaseActivity {
    private TSnackbar snackBar;
    private int APP_DOWn = TSnackbar.APPEAR_FROM_TOP_TO_DOWN;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if(snackBar!=null){
                        snackBar.setPromptThemBackground(Prompt.SUCCESS).setText("登录成功").setDuration(TSnackbar.LENGTH_LONG).show();

                    }
                    break;
                case 1:
                    if(snackBar!=null) {
                        snackBar.setPromptThemBackground(Prompt.ERROR).setText("登录失败").setDuration(TSnackbar.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };;
    Runnable gotoLoginActSuccess = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            msg.what = 0;
            mHandler.sendMessage(msg);
        }
    };
    Runnable gotoLoginActFail = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            msg.what = 1;
            mHandler.sendMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClick(View v) throws InterruptedException {
        final ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content).getRootView();
        switch (v.getId()) {
            case R.id.success:
                
                snackBar = TSnackbar.make(viewGroup, "成功...", TSnackbar.LENGTH_SHORT, APP_DOWn);
                snackBar.setAction("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                snackBar.setPromptThemBackground(Prompt.SUCCESS);
                snackBar.show();
                break;
            case R.id.error:
                snackBar = TSnackbar.make(viewGroup, "错误...", TSnackbar.LENGTH_LONG, APP_DOWn);
                snackBar.addIcon(R.mipmap.ic_launcher,100,100);
                snackBar.setAction("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                snackBar.setPromptThemBackground(Prompt.ERROR);
                snackBar.show();
                break;
            case R.id.warning:
                snackBar = TSnackbar.make(viewGroup, "警告...", TSnackbar.LENGTH_LONG, APP_DOWn);
                snackBar.addIcon(R.mipmap.ic_launcher,100,100);
                snackBar.setAction("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                snackBar.setPromptThemBackground(Prompt.WARNING);
                snackBar.show();
                break;
            case R.id.loginSuccess:
                snackBar = TSnackbar.make(viewGroup, "正在登录，请稍后...", TSnackbar.LENGTH_INDEFINITE, APP_DOWn);
                snackBar.setAction("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                snackBar.setPromptThemBackground(Prompt.SUCCESS);
                snackBar.addIconProgressLoading(0,true,false);
                snackBar.show();
                mHandler.postDelayed(gotoLoginActSuccess, 5000);
                break;
            case R.id.loginFail:
                snackBar = TSnackbar.make(viewGroup, "正在登录，请稍后...", TSnackbar.LENGTH_INDEFINITE, APP_DOWn);
                snackBar.setAction("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                snackBar.setPromptThemBackground(Prompt.SUCCESS);
                snackBar.addIconProgressLoading(0,true,false);
                snackBar.show();
                mHandler.postDelayed(gotoLoginActFail, 5000);
                break;
            default:
                
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.style0:
                APP_DOWn = TSnackbar.APPEAR_FROM_TOP_TO_DOWN;
                return true;
            case R.id.style1:
                APP_DOWn = TSnackbar.APPEAR_FROM_BOTTOM_TO_TOP;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
}
