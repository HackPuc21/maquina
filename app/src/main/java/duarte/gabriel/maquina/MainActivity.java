package duarte.gabriel.maquina;

import android.Manifest;
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

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import com.google.zxing.Result;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    Button btnAtivarLeitor;
    final int CODE = 0;
    private ZXingScannerView mScannerView;

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
        //mScannerView.stopCamera();
    }

    public void QrScanner(){

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();

    }

    @Override
    public void handleResult(Result rawResult) {

        Log.e("handler", rawResult.getText()); // Prints scan results
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)

        Toast.makeText(this, "QR: " + rawResult.getText(), Toast.LENGTH_LONG).show();
        mScannerView.stopCamera();
        mScannerView.resumeCameraPreview(this);
        setContentView(R.layout.activity_main);
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
}
