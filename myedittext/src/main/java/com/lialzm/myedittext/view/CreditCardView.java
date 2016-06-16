package com.lialzm.myedittext.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by lialzm on 2016/6/15.
 */
public class CreditCardView extends EditText {
    public CreditCardView(Context context) {
        super(context);
        init();
    }

    public CreditCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CreditCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.addTextChangedListener(setTextWatcher());
    }

    private TextWatcher setTextWatcher() {

        TextWatcher textWatcher = new TextWatcher() {
            //记录是否为删除
            boolean isDel = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("find", "beforeTextChangedlength=="+s.length() + ",start==" + start + ",count==" + count + ",after==" + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("find", "onTextChangedlength=="+s.length() + ",start==" + start + ",before==" + before + ",count==" + count);
                if (before > count) {//删除
                    isDel = true;
                } else {
                    isDel = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("find", "afterTextChangedlength=="+s.length());
                if (!isDel && s.length() > 0 &&s.length()>1&& (s.length()) % 5 == 0) {
                    //在指定位置之前插入
                    s.insert(s.length()-1,"-");
                }
                if (isDel && s.length() > 0&&s.length()>1 && (s.length()) % 5 == 0) {
                    //删除指定位置开区间[start,end)
                    s.delete(s.length() -1,s.length());
                }
            }
        };
        return textWatcher;
    }
}
