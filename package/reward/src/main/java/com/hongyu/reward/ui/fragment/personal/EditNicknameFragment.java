package com.hongyu.reward.ui.fragment.personal;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.fw.zycoder.http.callback.DataCallback;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.model.BaseModel;
import com.hongyu.reward.model.NoticeEvent;
import com.hongyu.reward.request.EditUserInfoRequestBuilder;
import com.hongyu.reward.utils.T;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangyang131 on 16/10/10.
 */
public class EditNicknameFragment extends BaseLoadFragment {
  EditText edit_text;
  private static InputFilter emojiFilter = new InputFilter() {
    Pattern emoji = Pattern.compile(
        "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
        Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
        int dstart,
        int dend) {
      Matcher emojiMatcher = emoji.matcher(source);
      if (emojiMatcher.find()) {
        return "";
      }
      return null;


    }
  };
  public static InputFilter[] emojiFilters = {emojiFilter};



  @Override
  protected void onStartLoading() {

  }



  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initView();
  }

  private void initView() {
    edit_text = (EditText) mContentView.findViewById(R.id.edit_text);


    edit_text.setFilters(emojiFilters);

    edit_text.setOnTouchListener(new View.OnTouchListener() {

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
        Drawable drawable = edit_text.getCompoundDrawables()[2];
        // 如果右边没有图片，不再处理
        if (drawable == null)
          return false;
        // 如果不是按下事件，不再处理
        if (event.getAction() != MotionEvent.ACTION_UP)
          return false;
        if (event.getX() > edit_text.getWidth()
            - edit_text.getPaddingRight()
            - drawable.getIntrinsicWidth()) {
          edit_text.setText("");
          return true;
        }
        return false;
      }
    });
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.edit_nickname_layout;
  }


  public void editFinish() {
    if (!isAdded()) {
      return;

    }
    String nickName = edit_text.getText().toString();
    if (TextUtils.isEmpty(nickName) || TextUtils.isEmpty(nickName.trim())) {
      edit_text.setError("昵称不能为空");
    } else {
      EditUserInfoRequestBuilder builder = new EditUserInfoRequestBuilder();
      builder.setNickname(nickName.trim());
      builder.setDataCallback(new DataCallback<BaseModel>() {
        @Override
        public void onDataCallback(BaseModel data) {
          if (ResponesUtil.checkModelCodeOK(data)) {
            EventBus.getDefault().post(new NoticeEvent(NoticeEvent.USER_NICKNAME_CHANGED));
          } else if (isAdded()) {
            T.show(ResponesUtil.getErrorMsg(data));
          }
          if (isAdded()) {
            dismissLoadingView();
            getActivity().finish();
          }
        }
      });
      builder.build().submit();

    }
  }
}
