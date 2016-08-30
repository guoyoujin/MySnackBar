package com.trycatch.mycustomsnackbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.trycatch.mysnackbar.TSnackbar;

public class MainActivity extends AppCompatActivity {
    private TSnackbar snackBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.success:
                if (snackBar != null && snackBar.isShown()) {
                    snackBar.dismiss();
                } else {
                    final ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content).getRootView();
                    snackBar = TSnackbar.make(viewGroup, "Loading...", TSnackbar.LENGTH_LONG, TSnackbar.APPEAR_FROM_TOP_TO_DOWN);
                    snackBar.addIcon(R.mipmap.ic_launcher,200,200);
                    snackBar.setAction("adasdasd", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    snackBar.show();
                }
                break;
            case R.id.error:
                break;
            case R.id.warning:
                break;
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        
    }
}
