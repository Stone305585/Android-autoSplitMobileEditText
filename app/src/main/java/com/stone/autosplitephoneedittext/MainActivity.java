package com.stone.autosplitephoneedittext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 自定义 切割 手机号 的展示界面
 */
public class MainActivity extends AppCompatActivity {

    //显示当前结果
    private TextView currentPhoneTv;
    //自定义view
    private SplitPhoneEditText splitePhoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setListener();
    }

    /**
     * 初始化view
     */
    private void initView() {

        currentPhoneTv = (TextView) findViewById(R.id.current_phone);
        splitePhoneEditText = (SplitPhoneEditText) findViewById(R.id.my_split_phone);
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        splitePhoneEditText.setCurrentPhoneListener(new SplitPhoneEditText.CurrentPhone() {
            @Override
            public void getCurrentPhone(String phone) {
                currentPhoneTv.setText(phone);
            }
        });
    }
}
