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

package com.liferay.mobile.sdk;

import com.liferay.mobile.sdk.http.Response;
import com.liferay.mobile.sdk.service.CustomGroupService;
import com.liferay.mobile.sdk.service.Site;
import com.liferay.mobile.sdk.v7.group.GroupService;

import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Bruno Farache
 */
public class GroupServiceTest extends BaseTest {

	public GroupServiceTest() throws IOException {
		super();
	}

	@Test
	public void disableStaging() throws Exception {
		GroupService service = ServiceBuilder.build(GroupService.class);
		long groupId = props.getGroupId();
		Call<Response> call = service.disableStaging(groupId);
		Response response = call.execute();
		assertEquals(200, response.statusCode());
	}

	@Test
	public void disableStagingAsync() throws Exception {
		GroupService service = ServiceBuilder.build(GroupService.class);
		long groupId = props.getGroupId();
		Call<Response> call = service.disableStaging(groupId);
		final CountDownLatch lock = new CountDownLatch(1);

		call.async(new Callback<Response>() {

			@Override
			public void onSuccess(Response response) {
				assertEquals(200, response.statusCode());
				lock.countDown();
			}

			@Override
			public void onFailure(Exception exception) {
				lock.countDown();
			}

		});

		lock.await(500, TimeUnit.MILLISECONDS);
	}

	@Test
	public void getGroup() throws Exception {
		GroupService service = ServiceBuilder.build(GroupService.class);
		long groupId = props.getGroupId();
		Call<JSONObject> call = service.getGroup(groupId);
		JSONObject group = call.execute();
		assertEquals(groupId, group.getLong("groupId"));
	}

	@Test
	public void getGroupAsMap() throws Exception {
		CustomGroupService service = new CustomGroupService();
		long groupId = props.getGroupId();
		Call<Map<String, Object>> call = service.getGroupAsMap(groupId);
		Map<String, Object> group = call.execute();
		assertEquals(groupId, ((Double)group.get("groupId")).longValue());
	}

	@Test
	public void getGroupAsMapAsync() throws Exception {
		CustomGroupService service = new CustomGroupService();
		final long groupId = props.getGroupId();
		Call<Map<String, Object>> call = service.getGroupAsMap(groupId);
		final CountDownLatch lock = new CountDownLatch(1);

		call.async(new Callback<Map<String, Object>>() {

			@Override
			public void onSuccess(Map<String, Object> group) {
				assertEquals(
					groupId, ((Double)group.get("groupId")).longValue());

				lock.countDown();
			}

			@Override
			public void onFailure(Exception exception) {
				fail(exception.getMessage());
				lock.countDown();
			}

		});

		lock.await(500, TimeUnit.MILLISECONDS);
	}

	@Test
	public void getGroupAsSite() throws Exception {
		CustomGroupService service = new CustomGroupService();
		long groupId = props.getGroupId();
		Call<Site> call = service.getGroupAsSite(groupId);
		Site group = call.execute();
		assertEquals(groupId, group.groupId);
	}

	@Test
	public void getGroupAsSiteAsync() throws Exception {
		CustomGroupService service = new CustomGroupService();
		final long groupId = props.getGroupId();
		Call<Site> call = service.getGroupAsSite(groupId);
		final CountDownLatch lock = new CountDownLatch(1);

		call.async(new Callback<Site>() {

			@Override
			public void onSuccess(Site group) {
				assertEquals(groupId, group.groupId);
				lock.countDown();
			}

			@Override
			public void onFailure(Exception exception) {
				fail(exception.getMessage());
				lock.countDown();
			}

		});

		lock.await(500, TimeUnit.MILLISECONDS);
	}

	@Test
	public void getGroupAsync() throws Exception {
		GroupService service = ServiceBuilder.build(GroupService.class);
		final long groupId = props.getGroupId();
		Call<JSONObject> call = service.getGroup(groupId);
		final CountDownLatch lock = new CountDownLatch(1);

		call.async(new Callback<JSONObject>() {

			@Override
			public void onSuccess(JSONObject group) {
				assertEquals(groupId, group.optLong("groupId"));
				lock.countDown();
			}

			@Override
			public void onFailure(Exception exception) {
				fail(exception.getMessage());
				lock.countDown();
			}

		});

		lock.await(500, TimeUnit.MILLISECONDS);
	}

	@Test
	public void getUserSites() throws Exception {
		GroupService service = ServiceBuilder.build(GroupService.class);
		Call<JSONArray> call = service.getUserSitesGroups();
		assertUserSites(call.execute());
	}

	@Test
	public void getUserSitesAsListOfMap() throws Exception {
		CustomGroupService service = new CustomGroupService();
		Call<List<Map<String, Object>>> call =
			service.getUserSitesAsListOfMap();

		assertUserSitesAsMap(call.execute());
	}

	@Test
	public void getUserSitesAsListOfMapAsync() throws InterruptedException {
		CustomGroupService service = new CustomGroupService();
		Call<List<Map<String, Object>>> call =
			service.getUserSitesAsListOfMap();

		final CountDownLatch lock = new CountDownLatch(1);

		call.async(new Callback<List<Map<String, Object>>>() {

			@Override
			public void onSuccess(List<Map<String, Object>> sites) {
				assertUserSitesAsMap(sites);
				lock.countDown();
			}

			@Override
			public void onFailure(Exception exception) {
				fail(exception.getMessage());
				lock.countDown();
			}

		});

		lock.await(500, TimeUnit.MILLISECONDS);
	}

	@Test
	public void getUserSitesAsListOfSites() throws Exception {
		CustomGroupService service = new CustomGroupService();
		Call<List<Site>> call = service.getUserSitesAsListOfSites();
		assertUserSites(call.execute());
	}

	@Test
	public void getUserSitesAsListOfSitesAsync() throws InterruptedException {
		CustomGroupService service = new CustomGroupService();
		Call<List<Site>> call = service.getUserSitesAsListOfSites();
		final CountDownLatch lock = new CountDownLatch(1);

		call.async(new Callback<List<Site>>() {

			@Override
			public void onSuccess(List<Site> sites) {
				assertUserSites(sites);
				lock.countDown();
			}

			@Override
			public void onFailure(Exception exception) {
				fail(exception.getMessage());
				lock.countDown();
			}

		});

		lock.await(500, TimeUnit.MILLISECONDS);
	}

	@Test
	public void getUserSitesAsync() throws InterruptedException {
		CustomGroupService service = new CustomGroupService();
		Call<JSONArray> call = service.getUserSites();
		final CountDownLatch lock = new CountDownLatch(1);

		call.async(new Callback<JSONArray>() {

			@Override
			public void onSuccess(JSONArray sites) {
				assertUserSites(sites);
				lock.countDown();
			}

			@Override
			public void onFailure(Exception exception) {
				fail(exception.getMessage());
				lock.countDown();
			}

		});

		lock.await(500, TimeUnit.MILLISECONDS);
	}

	protected static void assertUserSites(JSONArray sites) {
		assertNotNull(sites);
		assertTrue(sites.length() == 2);

		JSONObject jsonObj = sites.optJSONObject(0);
		assertNotNull(jsonObj);
		assertEquals("/test", jsonObj.optString("friendlyURL"));

		jsonObj = sites.optJSONObject(1);
		assertNotNull(jsonObj);
		assertEquals("/guest", jsonObj.optString("friendlyURL"));
	}

	protected static void assertUserSites(List<Site> sites) {
		assertNotNull(sites);
		assertTrue(sites.size() == 2);

		Site site = sites.get(0);
		assertNotNull(site);
		assertEquals("/test", site.friendlyURL);

		site = sites.get(1);
		assertNotNull(site);
		assertEquals("/guest", site.friendlyURL);
	}

	protected static void assertUserSitesAsMap(
		List<Map<String, Object>> sites) {

		assertNotNull(sites);
		assertTrue(sites.size() == 2);

		Map<String, Object> site = sites.get(0);
		assertNotNull(site);
		assertEquals("/test", site.get("friendlyURL"));

		site = sites.get(1);
		assertNotNull(site);
		assertEquals("/guest", site.get("friendlyURL"));
	}

}