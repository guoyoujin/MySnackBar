## MySnackBar
从顶部弹出的Snackbar,用于提示用户操作信息.
提供三种信息: SUCCESS,WARNING,ERROR


## 特点
1. 状态栏颜色处理兼容到4.4;在4.4上需要添加values-v19
2. 和Snackbar的调用方法一样,同时支持duration和手势移除,移除时状态栏颜色随手势移除而变化.
3. 提供LUtils用于操作状态栏,支持属性动画

## Example
 first add dependences
```
  dependencies {
    compile 'com.trycatch.android:mysnackbar:1.0.0'
  }
```

## 使用
```
    public class MainActivity extends AppCompatActivity {
        private TSnackbar snackBar;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && android.os.Build.VERSION.SDK_INT<=Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
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
                    snackBar.setThemBackground(Prompt.SUCCESS);
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
                    snackBar.setThemBackground(Prompt.ERROR);
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
                    snackBar.setThemBackground(Prompt.WARNING);
                    snackBar.show();
                    break;
            }
        }
    
        @Override protected void onDestroy() {
            super.onDestroy();
            
        }
    }
    
```


## ScreenShot
5.0之上<br>
![TSnackbar](images/SnackbarL.gif "5.0 sample")
<br>4.4<br>
![TSnackbar](images/SnackbarK.gif "4.4 sample")
<br>4.4以下<br>
![TSnackbar](images/Snackbar.gif "4.4 sample")

## Thanks To
<a href="https://github.com/hongyangAndroid/ColorfulStatusBar" target="_blank">ColorfulStatusBar</a>
<br>
<a href="https://github.com/google/iosched" target="_blank">iosched</a>
<a href="https://github.com/baiiu/TSnackbar" target="_blank">TSnackbar</a>

## License

```
Copyright 2015 baiiu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```