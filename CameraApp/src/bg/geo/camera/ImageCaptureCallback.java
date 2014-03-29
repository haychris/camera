package bg.geo.camera;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;

public class ImageCaptureCallback implements PictureCallback {
	protected final static String TAG = "PUPA";
	private OutputStream filoutputStream;
	private HttpURLConnection connection;
	private DataOutputStream dataOutputStream;
	private DataInputStream inputStream;
	int bytesRead, bytesAvailable, bufferSize;
	int maxBufferSize = 2 * 1024 * 1024;
	String lineEnd = "rn";
	String twoHyphens = "--";
	String boundary = "*****";
	byte[] buffer;
	private String string = "/sdcard/DCIM/100media/IMAG0001.jpg";

	public ImageCaptureCallback(OutputStream filoutputStream) {
		this.filoutputStream = filoutputStream;
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		try {
			Log.v(TAG, "onPictureTaken=" + data + " length = " + data.length);
			filoutputStream.write(data);
			FileInputStream arrayInputStream = new FileInputStream(new File(string));
			// Log.v(TAG,"Read Data:"+fileInputStream.read());
			URL url = new URL("http://www.geo.bg/upload/uploader.php");
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			// Use a post method.
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			
			dataOutputStream = new DataOutputStream(connection
					.getOutputStream());
			dataOutputStream
					.writeBytes("Content-Disposition: form-data; name='uploadedfile';filename='"
							+ "IMAG0001.jpg" + "'" + lineEnd);
			dataOutputStream.writeBytes(lineEnd);
			
			bytesAvailable = arrayInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			Log.v(TAG, "Buffer Size:" + bufferSize);
			buffer = new byte[bufferSize];
			bytesRead = arrayInputStream.read(buffer, 0, bufferSize);
			Log.v(TAG, "connect to server:" + connection.getResponseMessage());
			
			while (bytesRead > 0) {
				Log.v(TAG, "Read:" + bytesRead);
				dataOutputStream.write(buffer, 0, bufferSize);
				bytesAvailable = arrayInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = arrayInputStream.read(buffer, 0, bufferSize);
			}
			
			dataOutputStream.writeBytes(lineEnd);
			dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens
					+ lineEnd);
			
			Log.e(TAG, "File is written");
			arrayInputStream.close();
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

	}

}

