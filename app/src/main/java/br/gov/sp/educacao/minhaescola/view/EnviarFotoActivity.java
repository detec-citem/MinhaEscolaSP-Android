package br.gov.sp.educacao.minhaescola.view;

import android.Manifest;

import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;

import android.provider.MediaStore;
import android.support.transition.Fade;
import android.support.transition.TransitionManager;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;

import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import br.gov.sp.educacao.minhaescola.R;
import br.gov.sp.educacao.minhaescola.task.EnviarFotoAsyncTask;
import br.gov.sp.educacao.minhaescola.util.ImageCrop;
import butterknife.BindView;
import butterknife.ButterKnife;

import static br.gov.sp.educacao.minhaescola.util.ImageCrop.takeAlbumAction;
import static br.gov.sp.educacao.minhaescola.util.ImageCrop.takeCameraAction;
import static br.gov.sp.educacao.minhaescola.util.ImageHelper.bitmapToByteArray;

public class EnviarFotoActivity
        extends AppCompatActivity {

    int PERMISSION_ALL = 1;

    String[] PERMISSIONS_CAMERA = {

            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    String[] PERMISSIONS_ALBUM = {

            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private static final int READ_PERMISSION = 0x01;

    public @BindView(R.id.cart_layoutSelecionar) RelativeLayout layoutSelecionar;
    public @BindView(R.id.cart_layoutPai) ViewGroup layoutPai;
    public @BindView(R.id.cart_sombra) RelativeLayout layoutSombra;

    public ProgressDialog progressDialog;

    private Bitmap btmEnviar;

    private String base64Foto;

    private EnviarFotoAsyncTask enviarFotoAsyncTask;

    private File foto;

    String caminhoArquivoFoto;

    String nomeArquivoFoto;

    private byte[] byteFoto = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_enviar_foto);

        ButterKnife.bind(this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        contentOnCreate();
    }

    private void contentOnCreate() {

        Window window = this.getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul_carteirinha));
        }

        layoutSombra.setVisibility(View.GONE);
        layoutSelecionar.setVisibility(View.GONE);

        requisitarPermissoes();
    }

    private void requisitarPermissoes() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            }

            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
            }

            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_PERMISSION);
            }
        }
    }

    public void goSelecaoFoto(View v) {

        final Animation minhaAnima = AnimationUtils.loadAnimation(this, R.anim.transition_slide);

        layoutSombra.setVisibility(View.VISIBLE);
        layoutSelecionar.startAnimation(minhaAnima);
        layoutSelecionar.setVisibility(View.VISIBLE);
    }

    public void selecionarFoto(View v) {

        switch (v.getId()) {

            case R.id.cart_btnCamera:

                if(!hasPermissions(this, PERMISSIONS_CAMERA)) {

                    ActivityCompat.requestPermissions(this, PERMISSIONS_CAMERA, PERMISSION_ALL);
                }
                else {

                    try {

                        tirarFoto();

                        dismissSelecao(new View(this));
                    }
                    catch (Exception e) {

                        e.printStackTrace();
                    }
                }
                break;

            case R.id.cart_btnGaleria:

                if(!hasPermissions(this, PERMISSIONS_ALBUM)) {

                    ActivityCompat.requestPermissions(this, PERMISSIONS_ALBUM, PERMISSION_ALL);
                }
                else {

                    takeAlbumAction(EnviarFotoActivity.this);
                }
                break;
        }
    }

    public void dismissSelecao(View v) {

        TransitionManager.beginDelayedTransition(layoutPai, new Fade());

        layoutSelecionar.setVisibility(View.GONE);
        layoutSombra.setVisibility(View.GONE);
    }

    private File criarArquivoFoto() throws IOException {

        File arquivoFoto = null;

        File diretorioParaSalvar = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));

        arquivoFoto = File.createTempFile("pic3_",".jpg", diretorioParaSalvar);

        nomeArquivoFoto = arquivoFoto.getName();

        caminhoArquivoFoto = arquivoFoto.getAbsolutePath();

        return arquivoFoto;
    }

    private void tirarFoto() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            try {

                foto = criarArquivoFoto();

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

                Uri contentUri = Uri.fromFile(foto);

                mediaScanIntent.setData(contentUri);

                this.sendBroadcast(mediaScanIntent);
            }
            catch (Exception ex) {

                ex.printStackTrace();
            }

            if (foto != null) {

                try {

                    Uri photoURI = FileProvider.getUriForFile(this, "br.gov.sp.educacao.minhaescola.fileprovider", foto);//Uri.fromFile(foto);

                    grantUriPermission("br.gov.sp.educacao.minhaescola.fileprovider", photoURI, takePictureIntent.FLAG_GRANT_READ_URI_PERMISSION);

                    grantUriPermission("br.gov.sp.educacao.minhaescola.fileprovider", photoURI, takePictureIntent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                    startActivityForResult(takePictureIntent, 5);
                }
                catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(data != null) {

            switch (requestCode) {

                case UCrop.REQUEST_CROP: {

                    UCrop.getOutput(data);

                    Uri resultUri = UCrop.getOutput(data);

                    String resultPath = ImageCrop.getRealPathFromURI(getApplicationContext(), resultUri);

                    if (resultPath == null && resultUri != null) {

                        resultPath = resultUri.getPath();
                    }
                    else {

                        Toast.makeText(this, "É necessário checar as permissões de acesso", Toast.LENGTH_SHORT).show();

                        return;
                    }

                    /////Mudança/////
                    if(foto != null) {

                        try {

                            FileInputStream fileInputStream = new FileInputStream(foto);

                            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

                            byte[] byteArray = new byte[bufferedInputStream.available()];

                            bufferedInputStream.read(byteArray, 0, byteArray.length);

                            byteFoto = byteArray;

                            String base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);

                            base64Foto = base64;

                        }
                        catch(Exception e) {

                            e.printStackTrace();
                        }
                    }
                    else {

                        try {

                            File file = new File(resultPath);

                            FileInputStream fileInputStream = new FileInputStream(file);

                            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

                            byte[] byteArray = new byte[bufferedInputStream.available()];

                            bufferedInputStream.read(byteArray, 0, byteArray.length);

                            byteFoto = byteArray;

                            String base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);

                            base64Foto = base64;
                        }
                        catch(Exception e) {

                            e.printStackTrace();
                        }
                    }

                    /////Mudança//////
                    if (base64Foto != null  ) {

                        enviarImagem();
                    }
                    else {

                        Toast.makeText(this, "Falha ao selecionar foto!", Toast.LENGTH_SHORT).show();
                    }

                    break;
                }

                case 5 : {

                    if (resultCode == RESULT_OK) {

                        try {

                            Bundle extras = data.getExtras();

                            btmEnviar = (Bitmap) extras.get("data");

                            Uri uri = Uri.parse("file:///storage/emulated/0/Pictures/" + foto.getName());

                            ImageCrop.pickFromAlbum2(this, uri);

                        }
                        catch (Exception e) {

                            e.printStackTrace();
                        }
                    }

                    break;
                }

                case ImageCrop.PICK_FROM_ALBUM: {

                    ImageCrop.pickFromAlbum(this, data);

                    break;
                }

                case ImageCrop.PICK_FROM_CAMERA: {

                    ImageCrop.pickFromCamera(this, data);

                    break;
                }
            }
        }
        else if(data != null && requestCode == ImageCrop.PICK_FROM_ALBUM) {

            ImageCrop.pickFromAlbum(this, data);
        }
        else {

            if(foto != null && requestCode != ImageCrop.PICK_FROM_ALBUM && requestCode != UCrop.REQUEST_CROP) {

                Uri uri = Uri.parse("file:///storage/emulated/0/Pictures/" + foto.getName());

                ImageCrop.pickFromCamera2(this, uri);
            }
        }
    }

    public boolean hasPermissions(Context context, String... permissions) {

        if (context != null
                && permissions != null) {

            for (String permission : permissions) {

                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        ImageCrop.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    public void enviarImagem() {

        try {

            byte[] fotoByteArray;

            if(byteFoto != null){

                fotoByteArray = byteFoto;
            }
            else{

               fotoByteArray = Base64.decode(base64Foto, Base64.NO_WRAP);
            }


            enviarFotoAsyncTask = new EnviarFotoAsyncTask(fotoByteArray, this);

            enviarFotoAsyncTask.execute();
        }
        catch (IllegalStateException e) {

            e.printStackTrace();
            Toast.makeText(this, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
        }
    }

    public void voltarFotoMenu(View v) {

        onBackPressed();

        finish();
    }
}
