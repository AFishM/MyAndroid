package com.xuzixu.myandroid;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xuzixu.myandroid.img.ImgActivity;
import com.xuzixu.myandroid.view.ViewActivity;

public class MainActivity extends AppCompatActivity implements ComponentCallbacks2, View.OnClickListener {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
        findViewById(R.id.to_img_activity_view).setOnClickListener(this);
        findViewById(R.id.to_view_activity_view).setOnClickListener(this);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    /**
     * Release memory when the UI becomes hidden or when system resources become low.
     *
     * @param level the memory-related event that was raised.
     */
    // TODO: 2019/2/15 查看系统怎么调用这个方法，即调用栈
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // Determine which lifecycle or system event was raised.
        switch (level) {
            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:
                /*
                   Release any UI objects that currently hold memory.

                   The user interface has moved to the background.
                */
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
                /*
                   Release any memory that your app doesn't need to run.

                   The device is running low on memory while the app is running.
                   The event raised indicates the severity of the memory-related event.
                   If the event is TRIM_MEMORY_RUNNING_CRITICAL, then the system will
                   begin killing background processes.
                */
                break;
            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                /*
                   Release as much memory as the process can.

                   The app is on the LRU list and the system is running low on memory.
                   The event raised indicates where the app sits within the LRU list.
                   If the event is TRIM_MEMORY_COMPLETE, the process will be one of
                   the first to be terminated.
                */
                break;
            default:
                /*
                  Release any non-critical data structures.

                  The app received an unrecognized memory level value
                  from the system. Treat this as a generic low-memory message.
                */
                break;
        }
    }

    public void doSomethingMemoryIntensive() {
        // Before doing something that requires a lot of memory,
        // check to see whether the device is in a low memory state.
        ActivityManager.MemoryInfo memoryInfo = getMemoryInfo();
        if (memoryInfo.lowMemory) {
            // Do memory intensive work ...
        }
    }

    private ActivityManager.MemoryInfo getMemoryInfo() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        assert activityManager != null;
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_img_activity_view:
                startActivity(new Intent(MainActivity.this, ImgActivity.class));
                break;
            case R.id.to_view_activity_view:
                startActivity(new Intent(MainActivity.this, ViewActivity.class));
                break;
        }
    }

    /*
    service容易一直运行不停止，占用内存，可以考虑用JobScheduler或者IntentService代替

    使用高效的数据结构，例如用SparseArray等替代hashMap可以避免自动装箱，HashMap

    抽象可以使代码灵活和可维护，但需要许多不必要的代码，因此也需要更多时间和更多内存去执行代码，所以避免不必要的抽象

    protobufs用于序列化数据，更小更快，但也会生成冗长代码

    Avoid memory churn：垃圾回收花费的时间越多，做别的事情例如渲染画面的时间就越少，在循环中或在onDraw方法中创建临时对象会快速消耗初生代的内存空间，迫使系统进行垃圾回收

    资源和第三方库会侵占可用的内存，通过减小apk体积可以显著减少内存使用

    使用dagger2做依赖注入，dagger2不使用反射，static, compile-time implementation意味着没有不必要的时间和内存运行成本，其他使用反射的依赖注入框架初始化进程时会扫描代码注解，这会在启动APP带来显著的滞后

    谨慎使用第三方库，通常这是基于非移动设备环境开发，使用前需仔细评估其代码体积和内存使用情况，也许需要针对移动设备环境进行优化
    即使是基于移动设备开发或者优化过，也可能由于两个不同库的不同实现例如序列化、缓存等有不同实现而带来诸多问题
    即使ProGuard可以移除多余的API和资源，但它无法移除第三方库的内部依赖，你可能需要花费很多时间手动调整ProGuard来使其生效
    避免因为一两个小功能引入一个有许多功能的库，这会带来许多不必要的代码，使用第三方库前寻找一些与你的需求非常匹配的，否则考虑自己实现一下
    */
}
