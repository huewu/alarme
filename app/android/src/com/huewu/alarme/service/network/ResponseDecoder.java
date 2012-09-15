package com.huewu.alarme.service.network;

import com.google.gson.stream.JsonReader;

public interface ResponseDecoder<T> {
	T decode( JsonReader reader ); 
}//end of interface
