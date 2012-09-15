package com.huewu.alarme.service.network;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import android.content.Context;
import android.net.http.HttpResponseCache;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;

/**
 * JSON Network Worker for Alarme
 * @author huewu
 *
 */
public class JsonNetworkWorker {

	private static final String TAG = "NetworkWokrer";

	private static final int REQUEST_READY = 101;
	private static final int REQUEST_WORKING = 102;
	private static final int REQUEST_FINISHED = 103;

	private NetworkMessageHandler mMessageHandler;
	private boolean mCacheInstalled = false; //every request is sent after this flag is set true.
	private ExecutorService mWorkerPool = Executors.newFixedThreadPool(3);

	private class MessageExecutor implements Runnable {

		private JsonRequest<?> mReq;
		public MessageExecutor(JsonRequest<?> req) {
			mReq = req;
		}

		@Override
		public void run() {
			// do http request.
			try {
				//TODO why should I make a new url? (it looks like related to a cache...)
				URL url = new URL( mReq.getURL().toString() );
				HttpURLConnection conn = null;
				conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod(mReq.getMethod().name());
				conn.setConnectTimeout(1000 * 5);
				conn.setReadTimeout(1000 * 5);
				conn.setDefaultUseCaches(true);

				if( mReq.useForceCache() )
					conn.setRequestProperty("Cache-Control", "only-if-cached");

				for( Entry<String, String>  h : mReq.headers.entrySet() ){
					conn.setRequestProperty(h.getKey(), h.getValue());
				}

				OutputStream os;
				if( mReq.data != null ){
					conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					os = conn.getOutputStream();
					os.write(mReq.data);
					os.close();
				}

				//convert stream to json. before that we should know the gson type.
				InputStream is = getContent( conn );

				if( mReq.decoder != null && is != null ) {

					InputStreamReader isr = new InputStreamReader(is);
					JsonReader reader = new JsonReader(isr);
					JsonToken token = reader.peek();
					switch( token ) {
					case BEGIN_OBJECT:
						handleObj( reader );
						break;
					case BEGIN_ARRAY:
						reader.beginArray();
						while( reader.hasNext() && mReq.isCanceled() == false )
							handleObj( reader );
						reader.endArray();
						break;
					default:
						//do nothing. (invalid json resp)
					}
					reader.close();
					isr.close();
				}

				if( is != null )
					is.close();
				conn.disconnect();

				Message msg = mMessageHandler.obtainMessage(REQUEST_FINISHED);
				msg.obj = mReq;
				mMessageHandler.sendMessage(msg);

			} catch (Exception e) {
				Log.w(TAG, "Fail to parse:" + e);
				e.printStackTrace();

				if( mReq.retryCount < mReq.maxRetryCount) {
					Log.w(TAG, "Auto retry request:" + mReq.url);
					mReq.retryCount++;
					sendRequest(mReq, mReq.retryCount * 30);	//retry
				}
				else {
					Message msg = mMessageHandler.obtainMessage(REQUEST_FINISHED);
					mReq.exception = e;
					msg.obj = mReq;
					mMessageHandler.sendMessage(msg);
				}

			}
		}//end of run.

		private void handleObj(JsonReader reader) {
			Object obj = mReq.decoder.decode(reader);
			if(obj != null)
				mReq.addResponse( obj );

			Message msg = mMessageHandler.obtainMessage(REQUEST_WORKING);
			msg.obj = new Pair<JsonRequest<?>, Object>( mReq, obj );
			mMessageHandler.sendMessage(msg);
		}
	}

	public JsonNetworkWorker(Context ctx) {
		mMessageHandler = new NetworkMessageHandler(ctx.getMainLooper());
		initHttpCache(ctx);
	}

	public InputStream getContent(HttpURLConnection conn) throws IOException {

		int code = conn.getResponseCode();
		Log.d(TAG, "Resp Code:" + code + ":" + conn.getRequestMethod() + ":" + conn.getURL() );

		switch(code)
		{
		case -1:
			return conn.getInputStream();
		case 502:	//gateway timeout
		case 504:	//bad gateway
		case 400:	//invalid request.
			return conn.getErrorStream();
		default:
			return conn.getInputStream();
		}
	}

	// shut down worker
	public void shutdown() {
		// if needed invoke callbacks.
		// remove all message.
		if( mMessageHandler != null ){
			mMessageHandler.removeMessages(REQUEST_FINISHED);
			mMessageHandler.removeMessages(REQUEST_READY);
			mMessageHandler.removeMessages(REQUEST_WORKING);
		}
		mWorkerPool.shutdown();
	}

	public void sendRequest(JsonRequest<?> req) {
		if (req == null) {
			return;
		}

		Message msg = mMessageHandler.obtainMessage(REQUEST_READY);
		msg.obj = req;
		mMessageHandler.sendMessage(msg);
	}

	public void sendRequest(JsonRequest<?> req, long delay) {
		if (req == null) {
			return;
		}

		Message msg = mMessageHandler.obtainMessage(REQUEST_READY);
		msg.obj = req;
		mMessageHandler.sendMessageDelayed(msg, delay);
	}

	private boolean mInitCache = false;
	private synchronized void initHttpCache(Context context){

		if(mInitCache == true)
			return;

		mInitCache = true;

		final File cache_dir = context.getCacheDir();
		// set http cache.
		mWorkerPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					File httpCacheDir = new File(cache_dir, "http");
					long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
					HttpResponseCache.install(httpCacheDir, httpCacheSize);
				} catch (IOException e) {
					Log.d(TAG, "Fail to install a cache");
				}
				mCacheInstalled = true;	//every request is sent after this flag is set true.
			}
		});
	}

	private class NetworkMessageHandler extends Handler {
		public NetworkMessageHandler(Looper mainLooper) {
			super(mainLooper);
		}

		@Override
		public void handleMessage(Message msg) {

			if( mCacheInstalled == false ){
				//handle this msg after cache is installed.
				Message copy_msg = Message.obtain(msg);
				copy_msg.obj = msg.obj;
				sendMessageDelayed(copy_msg, 100);
				return;
			}

			switch (msg.what) {
			case REQUEST_READY:
			{
				JsonRequest<?> req = (JsonRequest<?>) msg.obj;
				req.notifiyOnStart();
				if(mWorkerPool != null && mWorkerPool.isShutdown() == false)
					mWorkerPool.execute(new MessageExecutor((JsonRequest<?>) msg.obj));
			}
			break;
			case REQUEST_WORKING:
			{
				@SuppressWarnings("unchecked")
				Pair<JsonRequest<?>, Object> pair = (Pair<JsonRequest<?>, Object>) msg.obj;
				//invoke callback.
				pair.first.notifiyResponse(pair.second);
				break;
			}
			case REQUEST_FINISHED:
			{
				JsonRequest<?> req = (JsonRequest<?>) msg.obj;
				//invoke callback.
				req.notifyOnFinishOrError();
				break;
			}
			}
		}
	}//end of inner class

}// end of class
