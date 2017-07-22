package duarte.gabriel.maquina;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cielo.orders.domain.Credentials;
import cielo.orders.domain.Order;
import cielo.sdk.order.OrderManager;
import cielo.sdk.order.ServiceBindListener;
import cielo.sdk.order.payment.PaymentError;
import cielo.sdk.order.payment.PaymentListener;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import com.google.zxing.Result;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    Button btnAtivarLeitor;
    final int CODE = 0;
    private ZXingScannerView mScannerView;
    HashMap<Integer, String> products;
    OrderManager orderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAtivarLeitor = (Button)findViewById(R.id.btnTeste);

        btnAtivarLeitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED)
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, CODE);
                else if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED)
                    QrScanner();
            }
        });


    }



    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    public void QrScanner(){

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }


    @Override
    public void handleResult(Result rawResult) {

        Log.e("handler", rawResult.getText());
        Log.e("handler", rawResult.getBarcodeFormat().toString());

        build(rawResult.getText());
        Intent intent = new Intent(MainActivity.this, FinalizarCompra.class);
        intent.putExtra("products", products);
        startActivity(intent);
        //startActivity(new Intent(this,MainActivity.class));
        this.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CODE){
            if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                Toast.makeText(this, "Você precisa fornecer permissões.", Toast.LENGTH_LONG).show();
            else if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                QrScanner();
        }
    }

    private void build(String str){
        products = new HashMap<Integer, String>();

        String args[] = str.split(" ");

        for(String a : args){
            if(a.contains("*")){
                String pieces[] = a.split("\\*");
                products.put(Integer.parseInt(pieces[0]), pieces[1]);
            }
            else{
                products.put(1, a);
            }
        }


        //for(HashMap.Entry<Integer, String> p : products.entrySet())
        //    Toast.makeText(this, "QTD: " + p.getKey() + " - Valor: " + p.getValue(), Toast.LENGTH_LONG).show();

    }

}
