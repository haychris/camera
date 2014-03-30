package bg.geo.camera;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;

public class CameraHandler implements Callback, ShutterCallback, PictureCallback {
	private Camera camera;
	private IPictureTakenListener pictureTakenListener;
	
	private ShutterCallback shutterCallback;
	private PictureCallback pictureCallback;
	
	
	Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {                       
        	camera.takePicture(shutterCallback, null, null, pictureCallback);
        }
    };
	
	public CameraHandler(Camera camera, IPictureTakenListener pictureTakenListener) {
		this.camera = camera;		
		this.pictureTakenListener = pictureTakenListener;
		shutterCallback = this;
		pictureCallback = new ImageCaptureCallback(new ByteArrayOutputStream());
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int arg1, int arg2, int arg3) {
		
	}


	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {
		try {
			camera.setPreviewDisplay(surfaceHolder);			
			camera.startPreview();
			timerHandler.postDelayed(timerRunnable, 3000);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}


	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		
		this.camera.release();
		
		InputStream is = new ByteArrayInputStream(data);

		Bitmap bmp = BitmapFactory.decodeStream(is); //now do what you want with it. 
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	    byte[] byteArray = stream.toByteArray();
		//pictureTakenListener.PictureTaken();		
	}

	@Override
	public void onShutter() {
		// TODO Auto-generated method stub
		
	}

}
