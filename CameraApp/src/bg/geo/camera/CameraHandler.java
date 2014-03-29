package bg.geo.camera;

import java.io.IOException;

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
        	camera.takePicture(shutterCallback, pictureCallback, pictureCallback, pictureCallback);
        }
    };
	
	public CameraHandler(Camera camera, IPictureTakenListener pictureTakenListener) {
		this.camera = camera;		
		this.pictureTakenListener = pictureTakenListener;
		shutterCallback = this;
		pictureCallback = this;
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
		//pictureTakenListener.PictureTaken();		
	}

	@Override
	public void onShutter() {
		// TODO Auto-generated method stub
		
	}

}
