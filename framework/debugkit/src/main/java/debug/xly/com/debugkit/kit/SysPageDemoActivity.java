package debug.xly.com.debugkit.kit;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import debug.xly.com.debugkit.R;
import debug.xly.com.debugkit.util.Spanny;

/**
 * Created by zhangyang131 on 2017/6/8.
 */

public class SysPageDemoActivity extends ListActivity implements AdapterView.OnItemLongClickListener {
    private static final String SETTING_NAME = "setting name";
    private static final String SETTING_ACTION = "setting action";

    private SimpleAdapter adapter;
    String[] settingName;
    String[] settingAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAdapter();
        setListAdapter(adapter);
    }

    private void initAdapter() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        settingName = getResources().getStringArray(R.array.setting_name);
        settingAction = getResources().getStringArray(R.array.setting_action);
        for (int i = 0; i < settingAction.length; i++) {
            Map<String, Object> data = new HashMap<>();
            data.put(SETTING_NAME, settingName[i]);
            data.put(SETTING_ACTION, settingAction[i]);
            list.add(data);
        }
        getListView().setOnItemLongClickListener(this);
        adapter = new SimpleAdapter(this, list, android.R.layout.simple_list_item_2, new String[]{SETTING_NAME, SETTING_ACTION}, new int[]{android.R.id.text1, android.R.id.text2});
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        startSettingActivity(settingAction[position]);
    }


    private void startSettingActivity(String action_) {
        try {
            String action = action_.replace("Settings.", "");
            Class c = Class.forName("android.provider.Settings");
            Field f = c.getField(action);
            Intent intent = new Intent(f.get(null).toString());
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("实现代码");
        String action = settingAction[position].replace("Settings.", "");
        Spanny sp = new Spanny("Intent intent = ");
        sp.append("new ", new ForegroundColorSpan(0xffca783a));
        sp.append("Intent(Settings.");
        sp.append(action, new ForegroundColorSpan(0xff9676a8));
        sp.append(");\n");
        sp.append("startActivity(intent);");
        builder.setMessage(sp);
        builder.create().show();
        return true;
    }
}
