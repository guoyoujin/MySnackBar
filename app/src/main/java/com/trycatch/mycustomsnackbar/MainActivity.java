package com.trycatch.mycustomsnackbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.trycatch.mysnackbar.Prompt;
import com.trycatch.mysnackbar.TSnackbar;

public class MainActivity extends AppCompatActivity {
    private TSnackbar snackBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 18) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_main);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.success:
                if (snackBar != null && snackBar.isShown()) {
                    snackBar.dismiss();
                } else {
                    final ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content).getRootView();
                    snackBar = TSnackbar.make(viewGroup, "正在加载中...", TSnackbar.LENGTH_INDEFINITE, TSnackbar.APPEAR_FROM_TOP_TO_DOWN);
                    snackBar.setAction("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    snackBar.setThemBackground(Prompt.SUCCESS);
                    snackBar.show();
                }
                break;
            case R.id.error:
                if (snackBar != null && snackBar.isShown()) {
                    snackBar.dismiss();
                } else {
                    final ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content).getRootView();
                    snackBar = TSnackbar.make(viewGroup, "error...", TSnackbar.LENGTH_LONG, TSnackbar.APPEAR_FROM_TOP_TO_DOWN);
                    snackBar.addIcon(R.mipmap.ic_launcher,100,100);
                    snackBar.setAction("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    snackBar.setThemBackground(Prompt.ERROR);
                    snackBar.show();
                }
                break;
            case R.id.warning:
                if (snackBar != null && snackBar.isShown()) {
                    snackBar.dismiss();
                } else {
                    final ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content).getRootView();
                    snackBar = TSnackbar.make(viewGroup, "waring...", TSnackbar.LENGTH_LONG, TSnackbar.APPEAR_FROM_TOP_TO_DOWN);
                    snackBar.addIcon(R.mipmap.ic_launcher,100,100);
                    snackBar.setAction("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    snackBar.setThemBackground(Prompt.WARNING);
                    snackBar.show();
                }
                break;
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        
    }
}
