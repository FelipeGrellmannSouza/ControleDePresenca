package com.example.controledepresenca.model;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class GeradorQrCode {


    //Gera um QRcode a partir de uma String a ser definida
    public Bitmap gerarQRCode(String presenca){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        //Gerador De QrCode
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(presenca, BarcodeFormat.QR_CODE, 500, 500);

            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            return bitmap;
        }catch (WriterException e){
            throw new RuntimeException(e);
        }
    }




}
