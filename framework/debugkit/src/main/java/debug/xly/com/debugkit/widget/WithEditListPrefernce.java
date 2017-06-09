package debug.xly.com.debugkit.widget;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.DialogPreference;
import android.preference.ListPreference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import debug.xly.com.debugkit.R;

/**
 * Created by zhangyang131 on 2017/6/1.
 */

public class WithEditListPrefernce extends DialogPreference {
  ListPreference l;
  private DebugConfigListAdapter mListAdapter;
  int nowIndex;
  private boolean mValueSet;
  View foot;
  View root;
  EditText editText;
  ListView list;
  private CharSequence[] mEntries;
  private CharSequence[] mEntryValues;
  private String mValue;
  private String mSummary;

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public WithEditListPrefernce(Context context) {
    super(context);
  }

  public WithEditListPrefernce(Context context, AttributeSet attrs) {
    super(context, attrs);
    getData(context, attrs);
  }

  public WithEditListPrefernce(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    getData(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public WithEditListPrefernce(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    getData(context, attrs);
  }

  private void getData(Context context, AttributeSet attrs) {
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditListPrefernce);
    mEntries = a.getTextArray(R.styleable.EditListPrefernce_edit_list_entries);
    mEntryValues = a.getTextArray(R.styleable.EditListPrefernce_edit_list_entryValues);
    mSummary = a.getString(R.styleable.EditListPrefernce_edit_list_summary);
    a.recycle();
  }

  private AdapterView.OnItemClickListener mItemClickListener;

  private int getValueIndex() {
    return findIndexOfValue(getValue());
  }

  public CharSequence[] getEntryValues() {
    return mEntryValues;
  }

  public CharSequence[] getEntries() {
    return mEntries;
  }

  /**
   * Returns the value of the key. This should be one of the entries in
   * {@link #getEntryValues()}.
   *
   * @return The value of the key.
   */
  public String getValue() {
    return mValue;
  }

  /**
   * Returns the index of the given value (in the entry values array).
   *
   * @param value The value whose index should be returned.
   * @return The index of the value, or -1 if not found.
   */
  public int findIndexOfValue(String value) {
    if (value != null && mEntryValues != null) {
      for (int i = mEntryValues.length - 1; i >= 0; i--) {
        if (mEntryValues[i].equals(value)) {
          return i;
        }
      }
    }
    return -1;
  }

  private View getCustomView() {
    LayoutInflater inflater = LayoutInflater.from(getContext());
    mListAdapter = new DebugConfigListAdapter(getContext(), getEntryValues(), getEntries());
    root = inflater.inflate(R.layout.edit_single_choose_dialog_layout, null, false);
    foot = inflater.inflate(R.layout.debug_custom_view, null, false);
    editText = (EditText) foot.findViewById(R.id.debug_custome_info);
    list = (ListView) root.findViewById(R.id.debug_info_listview);
    list.addFooterView(foot);
    int i = getValueIndex();
    if(!TextUtils.isEmpty(getValue()) && i == -1){
      ((RadioButton) foot.findViewById(R.id.debug_custom_radiobtn))
              .setChecked(true);
      editText.setText(getValue());
    }else{
      mListAdapter.setSelectedIndex(i);
    }
    foot.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        nowIndex = -1;
        ((RadioButton) foot.findViewById(R.id.debug_custom_radiobtn))
            .setChecked(true);
        if (mListAdapter != null ) {
          mListAdapter.clear();
          mListAdapter.notifyDataSetChanged();
        }

        RadioButton rb = (RadioButton) foot
            .findViewById(R.id.debug_custom_radiobtn);
        rb.setChecked(true);
        editText.requestFocus();
        getDialog().getWindow().clearFlags(//不去掉焦点不能正常弹出软件键盘
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        InputMethodManager imm =
                (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        foot.invalidate();
      }
    });
    mItemClickListener = new AdapterView.OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        nowIndex = position;
        setApiEnvironment(nowIndex);
        mListAdapter.setSelectedIndex(nowIndex);
        ((RadioButton) foot.findViewById(R.id.debug_custom_radiobtn)).setChecked(false);
        foot.invalidate();
        mListAdapter.notifyDataSetChanged();
        getDialog().dismiss();
      }

    };

    list.setOnItemClickListener(mItemClickListener);
    list.setAdapter(mListAdapter);
    return root;
  }

  /**
   * 配置完Api地址后，需要设置全局环境变量
   */
  private void setApiEnvironment(int index) {
    // AppEnvironment.setServerEnvironment("http://.......");
  }

  @Override
  protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
    if (getEntries() == null || getEntryValues() == null) {
      throw new IllegalStateException(
          "ListPreference requires an entries array and an entryValues array.");
    }

    nowIndex = getValueIndex();

    View customView = getCustomView();
    builder.setView(customView);
    builder.setPositiveButton("确定", null);
    builder.setNegativeButton(null, null);
  }


  @Override
  protected void onDialogClosed(boolean positiveResult) {
    super.onDialogClosed(positiveResult);
    String value = null;
    if (nowIndex == -1) {
      value = editText.getText().toString();
    }else if(nowIndex>=0 && nowIndex<getEntryValues().length){
      value = getEntryValues()[nowIndex].toString();
    }
    if (!TextUtils.isEmpty(value) && callChangeListener(value)) {
      setValue(value);
    }
  }

  public void setValue(String value) {
    Log.i("setvalue", "value:"+value);
    // Always persist/notify the first time.
    setSummary(value);
    final boolean changed = !TextUtils.equals(mValue, value);
    if (changed || !mValueSet) {
      mValue = value;
      mValueSet = true;
      persistString(value);
      if (changed) {
        notifyChanged();
      }
    }
  }

  @Override
  protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
    setValue(restoreValue ? getPersistedString(mValue) : (String) defaultValue);
  }
}
