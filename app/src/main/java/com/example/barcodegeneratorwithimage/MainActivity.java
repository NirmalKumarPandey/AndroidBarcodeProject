package com.example.barcodegeneratorwithimage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private EditText mBarcodeName;
    private ImageView iImageView;
    private Button bScan,bGenerate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("Log", "Test");
        Log.d("Log", "Test1");
        setContentView(R.layout.activity_main);
        mBarcodeName=findViewById(R.id.barcode_address);
        iImageView=findViewById(R.id.barcode_image);
        bScan=findViewById(R.id.barcode_scan);
        bGenerate=findViewById(R.id.barcode_generate);
        bScan.setOnClickListener(this);
        bGenerate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == bScan) {

            IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setCameraId(0);
            integrator.setOrientationLocked(false);
            integrator.setPrompt("Scanning");
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
        }
        if (view == bGenerate) {
            String barcode = mBarcodeName.getText().toString().trim();
            if (!barcode.isEmpty()) {
                try {
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    BitMatrix bitMatrix = multiFormatWriter.encode(barcode, BarcodeFormat.QR_CODE, 250, 250);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    iImageView.setImageBitmap(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(MainActivity.this, "Please Write Barcode", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        final IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null && result.getContents()!=null)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Scan Result")
                    .setIcon(R.drawable.nirmal1)
                    .setMessage("This is the address of :")
                    .setPositiveButton("COPY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            ClipboardManager clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                            ClipData data=ClipData.newPlainText("result",result.getContents());
                            clipboardManager.setPrimaryClip(data);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setNeutralButton("Nutral", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();

                        }
                    })
                    .create().show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
