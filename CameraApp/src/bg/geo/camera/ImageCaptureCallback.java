package bg.geo.camera;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.StrictMode;
import android.util.Log;

public class ImageCaptureCallback implements PictureCallback {
	protected final static String TAG = "PUPA";
	//private OutputStream filoutputStream;
	private HttpURLConnection connection;
	private DataOutputStream dataOutputStream;
	private DataInputStream inputStream;
	int bytesRead, bytesAvailable, bufferSize;
	int maxBufferSize = 2 * 1024 * 1024;
	String urlServer = "http://192.168.1.1/handle_upload.php";
	String lineEnd = "\r\n";
	String twoHyphens = "--";
	String boundary =  "*****";
	byte[] buffer;
	
	CameraHandler cameraHandler;

	public ImageCaptureCallback(CameraHandler cameraHandler) {
		this.cameraHandler = cameraHandler;
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		try {
			
			 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		        StrictMode.setThreadPolicy(policy);
			
			//Uncomment this if you want to decrease the size and quality of the picture
			InputStream is = new ByteArrayInputStream(data);
			Bitmap bmp = BitmapFactory.decodeStream(is); 			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();			
			bmp.compress(Bitmap.CompressFormat.JPEG, 50, stream);
		    byte[] byteArray = stream.toByteArray();
			
			
			//filoutputStream.write(data);
			//FileInputStream arrayInputStream = new FileInputStream(new File(string));
			// Log.v(TAG,"Read Data:"+fileInputStream.read());
			URL url = new URL("http://www.geo.bg/upload/uploader.php");
			connection = (HttpURLConnection) url.openConnection();
			connection.setReadTimeout(10000 /* milliseconds */);
			connection.setConnectTimeout(30000 /* milliseconds */);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			// Use a post method.
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

			
			dataOutputStream = new DataOutputStream(connection.getOutputStream());
			dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
			dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"TEMP.JPG\"" + lineEnd);
			dataOutputStream.writeBytes(lineEnd);
		
			dataOutputStream.write(data);
			
			dataOutputStream.writeBytes(lineEnd);
			dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			
			Log.e(TAG, "File is written");
			//arrayInputStream.close();
			dataOutputStream.flush();

		} catch (MalformedURLException ex) {
			Log.v(TAG, "Ex:" + ex);
		} catch (IOException e) {
			Log.v(TAG, "Ex:" + e);
		}

		try {
			inputStream = new DataInputStream(connection.getInputStream());
			String str;
			while ((str = inputStream.readLine()) != null) {
				Log.v(TAG, "Server Response:" + str);
			}
			dataOutputStream.close();
		} catch (IOException e) {
			Log.v(TAG, "Ex:" + e);
		}
		
		cameraHandler.onPictureTaken();

	}

}

