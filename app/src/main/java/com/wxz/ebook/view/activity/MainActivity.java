package com.wxz.ebook.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.wxz.ebook.R;
import com.wxz.ebook.view.fragment.BookMarketFragment;
import com.wxz.ebook.view.fragment.BookMineFragment;
import com.wxz.ebook.view.fragment.BookShelfFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private BookMarketFragment bookMarketFragment;
    private BookShelfFragment bookShelfFragment;
    private BookMineFragment bookMineFragment;
    private Fragment[] fragments;
    private int lastfragment;//用于记录上个选择的Fragment
    private BottomNavigationView navigation;
    private String allGrantedStr[] = { Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int MY_PERMISSION_REQUEST_CODE = 10000;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if(lastfragment!=0)
                    {
                        toolbar.setTitle(R.string.title_activity_main_read_book_shelf);
                        switchFragment(lastfragment,0);
                        lastfragment=0;
                    }
                    return true;
                case R.id.navigation_dashboard:
                    if(lastfragment!=1)
                    {
                        toolbar.setTitle(R.string.title_activity_main_read_book_market);
                        switchFragment(lastfragment,1);
                        lastfragment=1;
                    }
                    return true;
                case R.id.navigation_notifications:
                    if(lastfragment!=2)
                    {
                        toolbar.setTitle(R.string.title_activity_main_read_my);
                        switchFragment(lastfragment,2);
                        lastfragment=2;
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        boolean isAllGranted = checkPermissionAllGranted(allGrantedStr);
        // 如果这3个权限全都拥有, 则直接执行备份代码
        if (!isAllGranted) {
            ActivityCompat.requestPermissions(this, allGrantedStr, MY_PERMISSION_REQUEST_CODE);
        }
        initFragment();
    }

    private void initFragment()
    {
        bookShelfFragment = new BookShelfFragment();
        bookMarketFragment = new BookMarketFragment();
        bookMineFragment = new BookMineFragment();
        fragments = new Fragment[]{bookShelfFragment,bookMarketFragment,bookMineFragment};
        lastfragment=0;
        getSupportFragmentManager().beginTransaction().replace(R.id.main_read_view,bookShelfFragment).show(bookShelfFragment).commit();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void switchFragment(int lastfragment,int index)
    {
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastfragment]);//隐藏上个Fragment
        if(!fragments[index].isAdded())
        {
            transaction.add(R.id.main_read_view,fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent intent = new Intent(MainActivity.this, SearchFileActivity.class);
            startActivityForResult(intent,1);
            return true;
        }else if (id == R.id.action_search) {
            Intent intent = new Intent(MainActivity.this, BookSearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

}
