package bg.geo.camera;

import java.io.IOException;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class MainActivity extends Activity implements IPictureTakenListener{
	
	private Camera camera1; 
	private Camera camera2;
	private boolean pictureTaken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
         camera1 = Camera.open(0);
         Parameters parameters = camera1.getParameters();
         android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
         android.hardware.Camera.getCameraInfo(0, info);
         
         int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
         int degrees = 0;
         switch (rotation) {
             case Surface.ROTATION_0: degrees = 0; break;
             case Surface.ROTATION_90: degrees = 90; break;
             case Surface.ROTATION_180: degrees = 180; break;
             case Surface.ROTATION_270: degrees = 270; break;
         }

         int result;
         if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
             result = (info.orientation + degrees) % 360;
             result = (360 - result) % 360;  // compensate the mirror
         } else {  // back-facing
             result = (info.orientation - degrees + 360) % 360;
         }
         camera1.setDisplayOrientation(result);     
         
//         camera2 = Camera.open(1);
         
         SurfaceView surfaceView = (SurfaceView)findViewById(R.id.surface1);
         surfaceView.getHolder().addCallback(new CameraHandler(camera1, this));
         

    }
    
    public void PictureTaken() {
    	//if (pictureTaken)
    	//	return;
    	//pictureTaken = true;
        //camera2 = Camera.open(1);
        //SurfaceView surfaceView = (SurfaceView)findViewById(R.id.surface2);
        //surfaceView.getHolder().addCallback(new CameraHandler(camera2, this));
    	
    	this.finish();       
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    
}
