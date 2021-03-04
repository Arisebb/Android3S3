package com.example.bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHeadset
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Build
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_ENABLE_BT: Int= 1;
    private val REQUEST_CODE_DISCOVERABLE_BT: Int= 2;

    //bluetooth adapter
    lateinit var bAdapter:BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        

        //init bluetooth adapter
        bAdapter = BluetoothAdapter.getDefaultAdapter()

        val bluetoothStatusTv=findViewById<TextView>(R.id.bluetoothStatusTv)
        val bluetoothIv=findViewById<ImageView>(R.id.bluetoothIv)
        val turnOnBtn=findViewById<Button>(R.id.turnOnBtn)
        val turnOffBtn=findViewById<Button>(R.id.turnOffBtn)
        val discoverableBtn=findViewById<Button>(R.id.discoverableBtn)
        val pairedBtn=findViewById<Button>(R.id.pairedBtn)
        val pairedTv=findViewById<TextView>(R.id.pairedTv)

        //check if bluetooth is available or not
        if(bAdapter==null){
            bluetoothStatusTv.text="Bluetooth is not available"
        }
        else{
            bluetoothStatusTv.text = "Bluetooth is available"
        }
        //set image according to bluetooth status (on/off)
        if (bAdapter.isEnabled){
            //bluetooth is on
            bluetoothIv.setImageResource(R.drawable.ic_bluetooth_on)
        }
        else{
            //bluetooth is off
            bluetoothIv.setImageResource(R.drawable.ic_bluetooth_off)
        }

        //turn on bluetooth
        turnOnBtn.setOnClickListener{
            if(bAdapter.isEnabled){
                //already enabled
                Toast.makeText(this, "Already on", Toast.LENGTH_LONG).show()
            }
            else{
                //turn on bluetooth
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, REQUEST_CODE_ENABLE_BT);
            }
        }
        //turn off bluetooth
        turnOffBtn.setOnClickListener{
            if(bAdapter.isEnabled){
                //already disabled
                bAdapter.disable()
                bluetoothIv.setImageResource(R.drawable.ic_bluetooth_off)
                Toast.makeText(this, "Bluetooth turned off", Toast.LENGTH_LONG).show()

            }
            else{
                //turn off bluetooth
                Toast.makeText(this, "Already off", Toast.LENGTH_LONG).show()
            }
        }
        //discoverable the bluetooth
        discoverableBtn.setOnClickListener{
            if(!bAdapter.isDiscovering){
                Toast.makeText(this, "Making your device discoverable", Toast.LENGTH_LONG).show()
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
                startActivityForResult(intent, REQUEST_CODE_DISCOVERABLE_BT);
            }
        }
        //get list of paired devices
        pairedBtn.setOnClickListener{

            if(bAdapter.isEnabled){
                pairedTv.text = "Paired Devices"
                //get list of paired devices
                val devices = bAdapter.bondedDevices
                for(device in devices){
                    val deviceName = device.name
                    val deviceAddress= device.address
                    pairedTv.append("\nDevice: $deviceName, $device")
                }
            }
            else{
                Toast.makeText(this, "Turn on bluetooth first", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            REQUEST_CODE_ENABLE_BT ->
                if(resultCode == Activity.RESULT_OK){
                    bluetoothIv.setImageResource(R.drawable.ic_bluetooth_on)
                    Toast.makeText(this, "Bluetooth is on", Toast.LENGTH_LONG).show()
                }else {
                    //user denied to turn on bluetooth from confirmation dialog
                    Toast.makeText(this, "Couldn't on Bluetooth", Toast.LENGTH_LONG).show()
                }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}