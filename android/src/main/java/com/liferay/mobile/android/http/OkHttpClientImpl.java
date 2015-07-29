/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.mobile.android.http;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Farache
 */
public class OkHttpClientImpl implements HttpClient {

	public OkHttpClientImpl() {
		client = new OkHttpClient();
	}

	@Override
	public Response send(Request request) throws Exception {
		OkHttpClient client = getClient(request.getConnectionTimeout());
		Builder builder = new Builder().url(request.getURL());
		Method method = request.getMethod();

		if (method == Method.POST) {
			String body = request.getBody();

			if (body != null) {
				MediaType type = MediaType.parse(
					"application/json; charset=utf-8");

				builder.post(RequestBody.create(type, body));
			}
		}
		else if (method == Method.HEAD) {
			builder.head();
		}

		Map<String, String> headers = request.getHeaders();

		if (headers != null) {
			for (Map.Entry<String, String> header : headers.entrySet()) {
				builder.addHeader(header.getKey(), header.getValue());
			}
		}

		com.squareup.okhttp.Response response = client
			.newCall(builder.build())
			.execute();

		String responseBody = null;

		if (method != Method.HEAD) {
			responseBody = response.body().string();
		}

		return new Response(
			response.code(), _toMap(response.headers().toMultimap()),
			responseBody);
	}

	protected OkHttpClient getClient(int connectionTimeout) {
		OkHttpClient clone = client.clone();

		clone.setConnectTimeout(connectionTimeout, TimeUnit.MILLISECONDS);
		clone.setReadTimeout(connectionTimeout, TimeUnit.MILLISECONDS);
		clone.setWriteTimeout(connectionTimeout, TimeUnit.MILLISECONDS);

		clone.setFollowRedirects(false);

		return clone;
	}

	protected OkHttpClient client;

	private Map<String, String> _toMap(Map<String, List<String>> headers) {
		Map<String, String> map = new HashMap<String, String>();

		for (Map.Entry<String, List<String>> header : headers.entrySet()) {
			map.put(header.getKey(), header.getValue().get(0));
		}

		return map;
	}

}