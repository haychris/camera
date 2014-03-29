package bg.geo.camera;

import java.io.IOException;

import android.hardware.Camera;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
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
         camera2 = Camera.open(1);
         
         SurfaceView surfaceView = (SurfaceView)findViewById(R.id.surface1);
         surfaceView.getHolder().addCallback(new CameraHandler(camera1, this));
         

    }
    
    public void PictureTaken() {
    	if (pictureTaken)
    		return;
    	pictureTaken = true;
        camera2 = Camera.open(1);
        SurfaceView surfaceView = (SurfaceView)findViewById(R.id.surface2);
        surfaceView.getHolder().addCallback(new CameraHandler(camera2, this));
       
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    
}
