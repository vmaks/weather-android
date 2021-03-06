package io.dp.weather.app.fragment;

import android.app.Application;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import io.dp.weather.app.BuildConfig;
import io.dp.weather.app.MockAppComponent;
import io.dp.weather.app.TestApp;
import io.dp.weather.app.activity.MockActivity;
import io.dp.weather.app.annotation.ConfigPrefs;
import io.dp.weather.app.db.DatabaseHelper;
import io.dp.weather.app.db.table.Place;
import io.dp.weather.app.event.AddPlaceEvent;
import io.dp.weather.app.event.DeletePlaceEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowSQLiteConnection;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by dp on 10/10/14.
 */
@RunWith(RobolectricGradleTestRunner.class) @Config(constants = BuildConfig.class,
    application = TestApp.class,
    manifest = "app/src/test/TestAndroidManifest.xml",
    resourceDir = "../main/res",
    sdk = 21) public class WeatherFragmentTest {

  @Inject Application app;

  @Inject Geocoder geocoder;

  @Inject DatabaseHelper databaseHelper;

  @Inject @ConfigPrefs SharedPreferences prefs;

  @Before public void setUp() throws Exception {
    TestApp app = ((TestApp) RuntimeEnvironment.application);

    ((MockAppComponent) app.getComponent()).inject(this);
  }

  @After public void tearDown() {
    ShadowSQLiteConnection.reset();
  }

  @Test public void testAddRemovePlaceFragment() throws Exception {
    WeatherFragment f = WeatherFragment.newInstance();

    SupportFragmentTestUtil.startFragment(f, MockActivity.class);
    assertNotNull(f.adapter);

    final String placeName = "Shanghai";

    Address address = mock(Address.class);
    when(address.getLatitude()).thenReturn(-1.0);
    when(address.getLongitude()).thenReturn(-1.0);

    List<Address> addresses = new ArrayList<>();
    addresses.add(address);

    try {
      when(geocoder.getFromLocationName(placeName, 1)).thenReturn(addresses);
    } catch (IOException e) {
      e.printStackTrace();
    }

    f.onAddPlace(new AddPlaceEvent(placeName));

    List<Place> places = databaseHelper.getPlaceDao().queryForEq(Place.NAME, placeName);

    assertEquals(1, places.size());
    assertEquals(placeName, places.get(0).getName());

    assertEquals(5, f.adapter.getCount());

    f.onDeletePlace(new DeletePlaceEvent(1L));
    f.adapter.notifyDataSetInvalidated();

    List<Place> placeList = databaseHelper.getPlaceDao().queryForAll();
    assertEquals(4, placeList.size());
    assertEquals(4, f.adapter.getCount());
  }
}
