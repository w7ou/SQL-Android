package com.example.myapplication5;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button add_button;
    Button del_button;
    EditText name_editText;
    EditText id_editText;
    EditText email_editText;
    ListView listView;
    ArrayList<String> dataSource;
    ArrayAdapter<String> arrayAdapter;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        id_editText=(EditText) findViewById(R.id.id_editText);
        name_editText=(EditText) findViewById(R.id.name_editText);
        email_editText=(EditText) findViewById(R.id.email_editText);
        add_button=(Button) findViewById(R.id.add_button);
        del_button=(Button) findViewById(R.id.del_button);
        listView = (ListView) findViewById(R.id.listView);
        addButtonHandler addhandler = new addButtonHandler();
        add_button.setOnClickListener(addhandler);

        DButtonHandler delhandler = new DButtonHandler();
        del_button.setOnClickListener(delhandler);

        dataSource = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.list_item,dataSource);
        listView.setAdapter(arrayAdapter);
        OnItemClickHandler itemClickListener = new OnItemClickHandler();
        listView.setOnItemClickListener(itemClickListener);

        builder = new AlertDialog.Builder(MainActivity.this);
    }


    private class addButtonHandler implements View.OnClickListener{
        public void onClick(View view){
        builder.setTitle("Confirm Message");
        builder.setMessage("are you sure to add a person ?");
            if(name_editText.getText().toString().equals("")) {
                Toast toast = Toast.makeText(MainActivity.this , "姓名欄不能空白", Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (id_editText.getText().toString().equals("")) {
                Toast toast = Toast.makeText(MainActivity.this , "學號欄不能空白", Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (email_editText.getText().toString().equals("")) {
                Toast toast = Toast.makeText(MainActivity.this , "電子郵件不能空白", Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                DelButtonHandler Dehandler = new DelButtonHandler();
                builder.setPositiveButton("ok", Dehandler);
                builder.setNegativeButton("cancel", null);
                builder.show();
            }
        }
        private class DelButtonHandler implements DialogInterface.OnClickListener {
            public void onClick(DialogInterface dialog,int which){
                Connection connection = connectionclass();
                try {
                    if (connection != null) {
                        String sqlinsert = "Insert into AndroidFile values ('" + id_editText.getText().toString() + "','" + name_editText.getText().toString() + "','" + email_editText.getText().toString() + "')";
                        Statement st = connection.createStatement();
                        int rowsAffected = st.executeUpdate(sqlinsert);
                        dataSource.clear();
                        ResultSet rs = st.executeQuery("SELECT*FROM AndroidFile");
                        while(rs.next()){
                            String number = rs.getString("學號");
                            String name = rs.getString("姓名");
                            String email = rs.getString("電子郵件");
                            dataSource.add("學號: " + number + " 姓名: " + name + " 電子郵件: " + email);
                        }
                        arrayAdapter.notifyDataSetChanged();
                    }
                } catch (Exception exception) {
                    Log.e("Error", exception.getMessage());
                }
            }
        }
    }

    private class DButtonHandler implements View.OnClickListener{
        public void onClick(View view){
            builder.setTitle("Confirm Message");
            builder.setMessage("are you sure to del a person ?");
            addButtonHandler.DelButtonHandler Dehandler = new addButtonHandler().new DelButtonHandler() {
                public void onClick(DialogInterface dialog, int which) {
                    Connection connection = connectionclass();
                    try{
                        if(connection != null){
                            String idValue = id_editText.getText().toString();
                            String nameValue = name_editText.getText().toString();
                            String emailValue = email_editText.getText().toString();
                            String IDsqldelete="DELETE FROM AndroidFile WHERE 學號 = '" + idValue + "'";
                            Statement IDst= connection.createStatement();
                            int IDrowsAffected = IDst.executeUpdate(IDsqldelete);

                            String NAMEsqldelete="DELETE FROM AndroidFile WHERE 姓名 = '" + nameValue + "'";
                            Statement NAMEst= connection.createStatement();
                            int NAMErowsAffected = NAMEst.executeUpdate(NAMEsqldelete);

                            String EMAILsqldelete="DELETE FROM AndroidFile WHERE 電子郵件 = '" + emailValue + "'";
                            Statement EMAILst= connection.createStatement();
                            int EMAILrowsAffected = EMAILst.executeUpdate(EMAILsqldelete);

                            dataSource.clear();
                            ResultSet rs = IDst.executeQuery("SELECT*FROM AndroidFile");
                            while (rs.next()) {
                                String name = rs.getString("姓名");
                                String number = rs.getString("學號");
                                String email = rs.getString("電子郵件");
                                dataSource.add("學號: " + number + " 姓名: " + name + " 電子郵件: " + email);
                            }

                            dataSource.clear();
                            ResultSet NAMErs = NAMEst.executeQuery("SELECT*FROM AndroidFile");
                            while (NAMErs.next()) {
                                String name = NAMErs.getString("姓名");
                                String number = NAMErs.getString("學號");
                                String email = NAMErs.getString("電子郵件");
                                dataSource.add("學號: " + number + " 姓名: " + name + " 電子郵件: " + email);
                            }

                            dataSource.clear();
                            ResultSet Ers = EMAILst.executeQuery("SELECT*FROM AndroidFile");
                            while (Ers.next()) {
                                String name = Ers.getString("姓名");
                                String number = Ers.getString("學號");
                                String email = Ers.getString("電子郵件");
                                dataSource.add("學號: " + number + " 姓名: " + name + " 電子郵件: " + email);
                            }
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                    catch (Exception exception){
                        Log.e("Error",exception.getMessage());
                    }
                }
            };
            builder.setPositiveButton("ok", Dehandler);
            builder.setNegativeButton("cancel", null);
            builder.show();
        }
    }

    public class OnItemClickHandler implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            String clickedItem = dataSource.get(position);
            String[] parts = clickedItem.split("學號:|姓名:|電子郵件:");
            if (parts.length >= 3) {
                String number = parts[1].trim();
                String name = parts[2].trim();
                String email = parts[3].trim();

                id_editText.setText(number);
                name_editText.setText(name);
                email_editText.setText(email);
            }
        }
    }


    @SuppressLint("NewApi")
    public Connection connectionclass(){
        Connection con=null;
        String ip="192.168.1.116",port="1433",username="sa",password="a15a15a15", databasename="first";
        StrictMode.ThreadPolicy tp=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(tp);
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String connectionUrl="jdbc:jtds:sqlserver://" + ip + ":" + port + ";databasename=" + databasename + ";user=" + username + ";password=" + password + ";";
            con = DriverManager.getConnection(connectionUrl);
        }
        catch (Exception exception) {
            Log.e("Error" ,exception.getMessage());
        }
        return con;
    }
}