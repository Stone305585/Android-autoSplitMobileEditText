package com.stone.autosplitephoneedittext;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Stone on 2017/5/6.
 * @author Stone
 * @email Stone305585@live.com
 *
 * 自定义切割手机号 EditText
 * 实现效果：输入手机号，实现按位置切分。
 */
public class SplitPhoneEditText extends EditText {

    /**
     * 默认中国手机号码长度
     */
    private int phoneLength = 11;
    /**
     * 定义手机号码分割的位置，A为第一个空格分隔位置，B为第二个，C...依此类推
     *
     * 当前为 国内手机号 187 6543 2100
     */
    private int splitA = 3;
    private int splitB = 4;
    private int splitC = 0;

    /**
     * 当前号码
     */
    private String phone = "";

    CurrentPhone currentPhoneListener;


    public SplitPhoneEditText(Context context) {
        this(context, null);
    }

    public SplitPhoneEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSplitListener();
    }


    public void setCurrentPhoneListener(CurrentPhone currentPhoneListener) {
        this.currentPhoneListener = currentPhoneListener;
    }

    /**
     * 设置监听器，实时改变字符间隔
     */
    private void initSplitListener() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {

                //这里不要随便写一些代码，因为这里会有一些进程和线程之间的初始化和交互，开始
                //这里写了一些东西，调试发现的，最后就去了Handler和Looper，尤其是使用return就出错了。
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s) || s.length() == 0) return;
                //----------防止手动输入空格越界---------------
                if (s.toString().substring(s.length() - 1).equals(" ")) {
                    setText(s.toString().trim());
                    setSelection(s.toString().trim().length());
                    return;
                }

                String ns = s.toString().replace(" ", "");

                //已经输入设置长度的手机号
                if (ns.length() == phoneLength) {
                    return;
                } else {
                    //这里测试过，当在输入第spliteA+1 = a个数字和第spliteA + spliteB+2 = b个数字时，count瞬间变为a,b
                    //下面的ns.length() + count会大于phoneLength，其实并没有，下面手动改变count数值。
                    count = 1;
                }

                //应对粘贴情况
                if (ns.length() + count > phoneLength) {

                    AlertDialog errorDialog = new AlertDialog.Builder(getContext()).create();
                    errorDialog.setTitle("提示");
                    errorDialog.setMessage("您粘贴的号码超出当前区号的位数限制，请手动输入正确的号码");
                    errorDialog.show();

                    setText("");
                    return;
                }

                //下面是切割主要逻辑
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (splitC == 0) {
                        splitC = -1;
                    }
                    if (splitB == 0) {
                        splitB = -1;
                    }

                    //当前处于不应该切分的空格处，即应删除空格
                    if (i != splitA && i != (splitA + splitB + 1) && i != (splitA + splitB + splitC + 2) && s.charAt(i) == ' ') {
                        continue;
                    } else {

                        //不应该删除的位置，添加上字符，不论该字符是不是空格，这里出现的空格是该循环中添加上的空格，
                        //用户如果手动输入空格，在上面通过trim()已经删掉了
                        sb.append(s.charAt(i));

                        //如果添加至此字符长度等于该分段长度，分段长度：187 6543 2100该手机号分为前4位(187+" ")一段，前9位(187+" "+6543+" ")一段
                        //最后一个非空格字符前插入空格进行分割，所有空格分隔都由此处插入
                        if ((sb.length() == (splitA + 1) || sb.length() == (splitA + splitB + 2) || sb.length() == (splitA + splitB + splitC + 3)) && sb.charAt(sb.length() - 1) != ' ') {
                            sb.insert(sb.length() - 1, ' ');
                        }
                    }
                }
                try {
                    /**
                     * before为1是粘贴替换的情况
                     */
                    if (!sb.toString().equals(s.toString())) {

                        int index = start + 1;

                        if (sb.charAt(start) == ' ') {
                            if (before == 0) {
                                index++;
                            } else {
                                index--;
                            }
                        } else {
                            if (before == 1) {
                                index--;
                            }
                        }
                        setText(sb.toString());
                        setSelection(index);
                    }
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                phone = getText().toString().replace(" ", "");

                if(currentPhoneListener != null) {
                    currentPhoneListener.getCurrentPhone(phone);
                }
            }
        });
    }

    //回调结果接口
    interface CurrentPhone {
        void getCurrentPhone(String phone);
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPhoneLength() {
        return phoneLength;
    }

    public void setPhoneLength(int phoneLength) {
        this.phoneLength = phoneLength;
    }

    public int getSplitA() {
        return splitA;
    }

    public void setSplitA(int splitA) {
        this.splitA = splitA;
    }

    public int getSplitB() {
        return splitB;
    }

    public void setSplitB(int splitB) {
        this.splitB = splitB;
    }

    public int getSplitC() {
        return splitC;
    }

    public void setSplitC(int splitC) {
        this.splitC = splitC;
    }
}
