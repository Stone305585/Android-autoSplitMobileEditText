# Android-EditText

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)

一个简单的自定义EditText，通过设置分割点，即可自动分割号码长度，欢迎提Issue。

![image](https://github.com/Stone305585/Android-autoSplitMobileEditText/blob/master/phonenumber.gif)

* maven

```
<dependency>
  <groupId>com.stone.splitphone</groupId>
  <artifactId>splitphone</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

* gradle

```
compile 'com.stone.splitphone:splitphone:1.0.0'
```

### 使用方式：

xml中：
```
<com.stone.splitphone.SplitPhoneEditText
        android:id="@+id/my_split_phone"
        android:layout_marginTop="100dp"
        android:layout_width="200dp"
        android:layout_height="50dp"
        android:textSize="14sp"
        android:layout_gravity="center_horizontal"
        android:textColor="@color/colorAccent"
        />
```
java中：
```
        splitePhoneEditText = (SplitPhoneEditText) findViewById(R.id.my_split_phone);
        splitePhoneEditText.setSplitA(3);
        splitePhoneEditText.setSplitB(4);
        
        splitePhoneEditText.setCurrentPhoneListener(new SplitPhoneEditText.CurrentPhone() {
            @Override
            public void getCurrentPhone(String phone) {
                //get the phone number
            }
        });
```
