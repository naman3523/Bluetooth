package com.example.myapplication65;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
Button scanbutton;
ListView scanlistview;
public ArrayList<String> stringArrayList=new ArrayList<>();
ArrayAdapter<String> arrayAdapter;
BluetoothAdapter myAdapter=BluetoothAdapter.getDefaultAdapter();

private final int REQUEST_PERMISSION_ACCESS_COARSE_LOCATION =1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanbutton=(Button) findViewById(R.id.scanbutton);
        scanlistview=(ListView) findViewById(R.id.scanlistview);
        Button btnONOFF = (Button) findViewById(R.id.btnONOFF);


        scanbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAdapter.startDiscovery();
                enableOFF();


            }
        });
        btnONOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableDisableBT();
            }

        });


        IntentFilter intentFilter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(myReceiever,intentFilter);
        arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,stringArrayList);
        scanlistview.setAdapter(arrayAdapter);
        showExplanation("Warning", "ask for permission", Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_PERMISSION_ACCESS_COARSE_LOCATION);
        IntentFilter filter=new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(myReceiever1,filter);

    }
    public void enableOFF(){
        if(!myAdapter.isEnabled()){
            Toast.makeText(MainActivity.this, "Enabling BT!", Toast.LENGTH_SHORT).show();
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(myReceiever,BTIntent);
        }

    }
    public void enableDisableBT(){
        if(myAdapter == null){
            Toast.makeText(MainActivity.this, "Does not have capabilities ", Toast.LENGTH_SHORT).show();
        }
        if(!myAdapter.isEnabled()){
            Toast.makeText(MainActivity.this, "Enabling BT!", Toast.LENGTH_SHORT).show();
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(myReceiever,BTIntent);
        }
        if(myAdapter.isEnabled()){Toast.makeText(MainActivity.this, "Disable!", Toast.LENGTH_SHORT).show();
            myAdapter.disable();


            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(myReceiever,BTIntent);

        }

    }

    BroadcastReceiver myReceiever=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();

            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                stringArrayList.add(device.getName());
                arrayAdapter.notifyDataSetChanged();
            }
        }
    };
    BroadcastReceiver myReceiever1=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();

            if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){
                BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //case1: Already bonded
                if(device.getBondState()==BluetoothDevice.BOND_BONDED){

                }
                //case2: Creating a bond
                if(device.getBondState()==BluetoothDevice.BOND_BONDING){

                }
                //case3: breaking a bond
                if(device.getBondState()==BluetoothDevice.BOND_NONE){

                }

            }
        }
    };
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }
    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        myAdapter.cancelDiscovery();

        Toast.makeText(MainActivity.this, "You Clicked On A Device!", Toast.LENGTH_SHORT).show();
        String deviceName=stringArrayList.get(i);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2)
        {



        }
    }
}
