package com.example.lance.myapplication;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mCostRecyclerView;
    private static final String TAG = "MainActivity";
    private Mydatabase mMydatabase;
    private List<CostItem> mCItems = new ArrayList<>();
    private CostAdaper costAdaper;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mMydatabase = new Mydatabase(MainActivity.this);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View viewDialog = inflater.inflate(R.layout.cost,null);
                final EditText mEtTitle = (EditText) viewDialog.findViewById(R.id.et_title);
                final EditText mEtMoney = (EditText) viewDialog.findViewById(R.id.et_money);
                final DatePicker mDpDate = (DatePicker) viewDialog.findViewById(R.id.dp_date);

                dialog.setTitle("新建记账");
                dialog.setView(viewDialog);

                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        CostItem costItem = new CostItem();
                        String title = mEtTitle.getText().toString();
                        String money = mEtMoney.getText().toString();
                        String date = mDpDate.getYear()+"-"+(mDpDate.getMonth()+1)+"-"+
                               mDpDate.getDayOfMonth();
                        costItem.setCostTitle(title);
                        costItem.setCostMoney(money);
                        costItem.setCostDate(date);
                        mMydatabase.insertData(costItem);
                        mCItems.add(costItem);
                        costAdaper.notifyDataSetChanged();

                    }
                });
                dialog.setNegativeButton("取消",null);

                dialog.create().show();
            }
        });
        
        mCostRecyclerView = (RecyclerView) findViewById(R.id.cost_recycler_view);
        mCostRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        initCostData();
        costAdaper = new CostAdaper(mCItems);
        mCostRecyclerView.setAdapter(costAdaper);


    }

    private void initCostData() {
        Cursor cursor = mMydatabase.selectData();
       if(cursor != null){
           while (cursor.moveToNext()){
               CostItem costItem = new CostItem();
               String title = cursor.getString(cursor.getColumnIndex("title"));
               String money = cursor.getString(cursor.getColumnIndex("money"));
               String date = cursor.getString(cursor.getColumnIndex("date"));
                costItem.setCostTitle(title);
                costItem.setCostMoney(money);
                costItem.setCostDate(date);
               mCItems.add(costItem);

            }
        }
    }


    public class CostHolder extends RecyclerView.ViewHolder{
        private TextView costTitle;
        private TextView costDate;
        private TextView costMoney;

        public CostHolder(View itemView) {
            super(itemView);
            costTitle = (TextView) itemView.findViewById(R.id.cost_title);
            costDate = (TextView) itemView.findViewById(R.id.cost_date);
            costMoney = (TextView) itemView.findViewById(R.id.cost_money);
        }

    }

    public class CostAdaper extends RecyclerView.Adapter<CostHolder>{
        private List<CostItem> mCostItems;

        public CostAdaper(List<CostItem> costItems){
            mCostItems = costItems;
        }

        @Override
        public CostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            View view = layoutInflater.inflate(R.layout.cost_item,null);
            return new CostHolder(view);
        }

        @Override
        public void onBindViewHolder(CostHolder holder, int position) {
            CostItem costItem = mCostItems.get(position);
            Log.i(TAG, "onBindViewHolder: "+costItem);

            holder.costTitle.setText(costItem.getCostTitle());
            holder.costDate.setText(costItem.getCostDate());
            holder.costMoney.setText(""+costItem.getCostMoney());
        }

        @Override
        public int getItemCount() {
            return mCostItems.size();
        }
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
