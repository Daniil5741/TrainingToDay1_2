package com.example.danya.trainingtoday1_2;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.preference.PreferenceScreen;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button button_add, btnLoad;
    TextView textView;
    EditText edexersise, edapproaches, edquantity;
    Dialog dialog;
    static String exercise = null;
    static String quantity = null;
    static int approaches = 0;

    final String ATTR_PHONE_NAME = "phoneName";
    final String ATTR_GROUP_NAME = "groupName";

    final String LOG_TAG = "MyLogs";
    int clickCounter = 0;
    Map<String, String> map;
    LinearLayout linearLayout;
    View item;
    ExpandableListView expandableListView;
    ArrayList<String> ExerciseList;
    ArrayList<Map<String, String>> groupDataList;
    String groupFrom[];
    int groupTo[];
    ArrayList<ArrayList<Map<String, String>>> сhildDataList;
    ArrayList<Map<String, String>> сhildDataItemList;
    String childFrom[];
    int childTo[];
    private SimpleExpandableListAdapter adapter;
    SharedPreferences sPref;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((SimpleExpandableListAdapter)expandableListView.getAdapter()).onSaveInstanceState(outState);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_add = (Button) findViewById(R.id.btnADD);
        button_add.setOnClickListener(this);

        dialog = new Dialog(MainActivity.this);

        // Установите заголовок
        // Передайте ссылку на разметку
        dialog.setContentView(R.layout.dialog_view);

        edexersise = (EditText) dialog.findViewById(R.id.Exercise);
        edapproaches = (EditText) dialog.findViewById(R.id.Approaches);
        edquantity = (EditText) dialog.findViewById(R.id.Quantity);
        btnLoad = (Button) dialog.findViewById(R.id.btnLoad);
        expandableListView = (ExpandableListView) findViewById(R.id.elvMain);

        groupDataList = new ArrayList<>();
        groupFrom = new String[]{"groupName"};
        groupTo = new int[]{R.id.text_item};
        сhildDataList = new ArrayList<>();

        childFrom = new String[]{"NumberApproach"};
        childTo = new int[]{R.id.text_item_approaches};
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exercise = edexersise.getText().toString();
                quantity = edquantity.getText().toString();
                approaches = Integer.parseInt(edapproaches.getText().toString());
                int[] arr_quantity = new int[approaches];
                Arrays.fill(arr_quantity, 0);
                int j = 0;
                for (String retval : quantity.split(" ")) {
                    arr_quantity[j] = Integer.parseInt(retval);
                    j++;
                }


                map = new HashMap<>();
                map.put("groupName", exercise); // время года
                groupDataList.add(map);
                сhildDataItemList = new ArrayList<>();
                int l = 0;
                for (int i = 1; i < (approaches + 1); i++) {
                    map = new HashMap<>();
                    map.put("NumberApproach", "Approaches " + i + (" (") + arr_quantity[l] + ")");
                    сhildDataItemList.add(map);
                    l++;
                }
                сhildDataList.add(сhildDataItemList);


                SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                        getApplicationContext(), groupDataList,
                        R.layout.item_main_exercsize, groupFrom,
                        groupTo, сhildDataList, R.layout.item_approaches,
                        childFrom, childTo);

                expandableListView.setAdapter(adapter);

                adapter.notifyDataSetChanged();

                loadText();
                ;

                String map1 = getGroupChildText(0, 0, adapter);
                //  Toast.makeText(MainActivity.this,  Integeradapter.getGroupId(0), Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Log.d(LOG_TAG, "onChildClick groupPosition = " + groupPosition +
                        " childPosition = " + childPosition +
                        " id = " + id);

                return false;
            }
        });
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                Log.d(LOG_TAG, "onGroupClick groupPosition = " + groupPosition +
                        " id = " + id);
                // v.setBackgroundColor(R.color.colorAccent);
                // блокируем дальнейшую обработку события для группы с позицией 1
//                if (groupPosition == 1) return true;
//
                return false;
            }
        });
        saveText();;

    }

    String getGroupText(int groupPos, SimpleExpandableListAdapter adapter) {
        return ((Map<String, String>) (adapter.getGroup(groupPos))).get(ATTR_GROUP_NAME);
    }

    String getChildText(int groupPos, int childPos, SimpleExpandableListAdapter adapter) {
        return ((Map<String, String>) (adapter.getChild(groupPos, childPos))).get(ATTR_PHONE_NAME);
    }

    String getGroupChildText(int groupPos, int childPos, SimpleExpandableListAdapter adapter) {
        return getGroupText(groupPos, adapter) + " " + getChildText(groupPos, childPos, adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dialog.getWindow().setLayout(getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight());


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnADD:
                dialog.show();
                break;
//            case R.id.btnLoad:
//                exercise = edexersise.getText().toString();
//                approaches = Integer.parseInt(edapproaches.getText().toString());
//
//                dialog.cancel();
//                break;
        }
    }

    void saveText() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("E", "Put");
        ed.putString("E", "Put1");
        ed.commit();
        //Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
    }

    void loadText() {
        sPref = getPreferences(MODE_PRIVATE);
        String savedText = sPref.getString("E", "");

        Toast.makeText(this,
                savedText, Toast.LENGTH_LONG).show();
    }


}