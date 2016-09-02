package com.trycatch.mycustomsnackbar;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.trycatch.mysnackbar.Prompt;
import com.trycatch.mysnackbar.TSnackbar;

public class MainActivity extends BaseActivity {
    private TSnackbar snackBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClick(View v) throws InterruptedException {
        final ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content).getRootView();
        switch (v.getId()) {
            case R.id.success:
                snackBar = TSnackbar.make(viewGroup, "正在加载中...", TSnackbar.LENGTH_INDEFINITE, TSnackbar.APPEAR_FROM_TOP_TO_DOWN);
                snackBar.setAction("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                snackBar.setPromptThemBackground(Prompt.SUCCESS);
                snackBar.addIconProgressLoading(0,true,false);
                snackBar.show();
                break;
            case R.id.error:
                snackBar = TSnackbar.make(viewGroup, "错误...", TSnackbar.LENGTH_LONG, TSnackbar.APPEAR_FROM_TOP_TO_DOWN);
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
                snackBar = TSnackbar.make(viewGroup, "警告...", TSnackbar.LENGTH_LONG, TSnackbar.APPEAR_FROM_TOP_TO_DOWN);
                snackBar.addIcon(R.mipmap.ic_launcher,100,100);
                snackBar.setAction("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                snackBar.setPromptThemBackground(Prompt.WARNING);
                snackBar.show();
                break;
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        
    }
}
