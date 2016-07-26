package vn.com.vshome;

import java.io.File;
import java.io.FileOutputStream;
import vn.com.vshome.R.id;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.Utils;
import vn.com.vshome.view.customview.CropableImageView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class CropActivity extends Activity {

    private CropableImageView imageView;
    private int floorId, roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        Uri uri = Uri.parse(getIntent().getExtras().getString("URI"));
        floorId = getIntent().getExtras().getInt(Define.INTENT_FLOOR_ID, 0);
        roomId = getIntent().getExtras().getInt(Define.INTENT_ROOM_ID, 0);

        imageView = (CropableImageView) findViewById(id.crop_image);
        imageView.setBitmap(Utils.getImageResized(this, uri));

        ImageButton ok = (ImageButton) findViewById(id.crop_ok);
        ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String path = roomId + "";
                new SaveImageTask(imageView.getBitmapFromView())
                        .execute(path);
            }
        });
        ImageButton cancel = (ImageButton) findViewById(id.crop_cancel);
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }

    class SaveImageTask extends AsyncTask<String, String, String> {

        private Bitmap bitmap;

        public SaveImageTask(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected String doInBackground(String... params) {
            String path = params[0];
            saveImage(bitmap, path);
            bitmap.recycle();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            setResult(Activity.RESULT_OK);
            CropActivity.this.finish();
        }
    }

    private void saveImage(Bitmap finalBitmap, String path) {
        File myDir = null;
        myDir = new File(Define.BASE_IMAGE_PATH + File.separator
                + path);
        myDir.mkdirs();
        String fname = "Image" + ".png";
        File file = new File(myDir, fname);

        try {
            if (!file.exists()){
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
