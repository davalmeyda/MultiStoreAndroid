package com.panaceasoft.psmultistore.ui.product;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.utils.PSDialogMsg;
import com.panaceasoft.psmultistore.viewmodel.category.CategoryViewModel;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.*;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AgregarProductosActivity extends AppCompatActivity {

    Button agregarProducto, botonCamara;
    ImageButton imagenBoton;
    TextInputEditText nomProduct,nomPrecio;
    private PSDialogMsg psDialogMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);
        agregarProducto = findViewById(R.id.agregarProducto);
        imagenBoton = findViewById(R.id.imagenBoton);
        nomPrecio = findViewById(R.id.nomPrecio);
        nomProduct= findViewById(R.id.nomProduct);
        botonCamara = findViewById(R.id.boton_camara);

        String shopId = getIntent().getStringExtra("shop_id");

        agregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AgregarProducto("123",shopId);
            }
        });
        imagenBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        botonCamara.setOnClickListener(new View.OnClickListener() {
            private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {


                if (ContextCompat.checkSelfPermission(AgregarProductosActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(AgregarProductosActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                } else {
                    // Permission has already been granted
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,0);
                }

            }
        });
    }

    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private String URL = "http://demo.brufat.com/multi-store-admin/sql/agregar_producto.php";

    private void AgregarProducto(String cat_id,String shop_id){


        String nomProducto = nomProduct.getText().toString();
        String nomPrecio1 = nomPrecio.getText().toString();
        psDialogMsg = new PSDialogMsg(this, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                if(!response.isEmpty()){

                    if(response.equals("Agregado")){
                        psDialogMsg.showSuccessDialog("Producto Agregado correctamente", getString(R.string.app__ok));
                        psDialogMsg.show();
                        nomPrecio.setText("");
                        nomProduct.setText("");
                    }else{
                        psDialogMsg.showErrorDialog("Hubo un error al agregar", getString(R.string.app__ok));
                        psDialogMsg.show();
                    }

                }else{
                    psDialogMsg.showErrorDialog("Hubo un error al agregar", getString(R.string.app__ok));
                    psDialogMsg.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                psDialogMsg.showErrorDialog(error.toString(), getString(R.string.app__ok));
                psDialogMsg.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String imagen = getStringImagen(bitmap);
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("cat_id",cat_id);
                parametros.put("shop_id",shop_id);
                parametros.put("nomProducto",nomProducto);
                parametros.put("nomPrecio", nomPrecio1);
                parametros.put("imagen",imagen);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    public String getStringImagen(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Configuración del mapa de bits en ImageView
                imagenBoton.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            bitmap = (Bitmap)data.getExtras().get("data");
            imagenBoton.setImageBitmap(bitmap);
        }

    }


}
