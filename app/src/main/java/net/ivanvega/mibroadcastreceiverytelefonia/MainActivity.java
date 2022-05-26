package net.ivanvega.mibroadcastreceiverytelefonia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.ivanvega.mibroadcastreceiverytelefonia.receivers.MiReceiverTelefonia;
import net.ivanvega.mibroadcastreceiverytelefonia.receivers.MyBroadcastReceiver;

public class MainActivity extends AppCompatActivity {
    MyBroadcastReceiver myBroadcastReceiver=
            new MyBroadcastReceiver();

    MiReceiverTelefonia miReceiverTelefonia = new MiReceiverTelefonia();

    Button btnS;
    TextView lbl;
    EditText txtTel, txtMessage;
    String message;
    String num;
    TextView lblMensaje;
    TextView lblNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lblMensaje = findViewById(R.id.lblMensajeProgramado);
        message = "Hello world";
        num = "";
        lblMensaje.setText(message);
        btnS = findViewById(R.id.btnSend);
        lbl = findViewById(R.id.lbl);
        txtTel = findViewById(R.id.txtPhone);
        txtMessage = findViewById(R.id.txtTexto);
        lblNum = findViewById(R.id.lblNum);
        String leido = FileManager.readFromFile(getApplicationContext());
        if(leido.contains("%!%")){
            String result[] = leido.replace("\n","").replace("\r","").split("%!%");
            num = result[0];
            message = result[1];
        }
        lblNum.setText(num);
        lblMensaje.setText(message);
        btnS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent broadcast = new Intent();
                //net.ivanvega.mibroadcastreceiverytelefonia.MIBROACAST

                broadcast.setAction(getString(R.string.action_broadcast));
                broadcast.putExtra("key1", "parametro de la difusion");
                sendBroadcast(broadcast);
            }
        });

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        filter.addAction(getString(R.string.action_broadcast));
        this.registerReceiver(myBroadcastReceiver, filter);

        //Telephony.Sms .Intents.SMS_RECEIVED_ACTION

        IntentFilter intentFilterTel = new IntentFilter(Telephony.Sms .Intents.SMS_RECEIVED_ACTION);

        getApplicationContext().registerReceiver(miReceiverTelefonia,
                intentFilterTel
        );
    }

    private void enviarSMS(String tel, String msj) {
         SmsManager smsManager =  SmsManager.getDefault();

         smsManager.sendTextMessage(tel,null, msj,
         null, null);



        Toast.makeText(
                this, "Mensaje enviado",
                Toast.LENGTH_LONG
        ).show();
    }

    public void btnProgramarMensaje(View v) {
        String cadAgregar = txtTel.getText().toString() + "%!%" + txtMessage.getText().toString();
        FileManager.writeToFile(cadAgregar,getApplicationContext());
        lblNum.setText(txtTel.getText());
        lblMensaje.setText(txtMessage.getText());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(myBroadcastReceiver);
    }

    public void btnSMS_onclick(View v){
        enviarSMS(txtTel.getText().toString(), txtMessage.getText().toString());
    }

}