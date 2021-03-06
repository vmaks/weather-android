package io.dp.weather.app.activity.debug;

import android.os.Bundle;
import com.squareup.otto.Bus;
import io.dp.weather.app.DebugBusSubcomponent;
import io.dp.weather.app.R;
import io.dp.weather.app.WeatherApp;
import io.dp.weather.app.activity.ActivityComponent;
import io.dp.weather.app.activity.ActivityModule;
import io.dp.weather.app.activity.BaseActivity;
import io.dp.weather.app.activity.BaseActivityComponent;
import io.dp.weather.app.activity.DaggerActivityComponent;
import javax.inject.Inject;

public class DebugActivity extends BaseActivity {
  @Inject
  Bus bus;

  @Override
  public BaseActivityComponent createComponent() {
    WeatherApp app = (WeatherApp) getApplication();
    ActivityComponent activityComponent = DaggerActivityComponent.builder()
        .appComponent(app.getComponent())
        .activityModule(new ActivityModule(this))
        .build();

    return activityComponent.plus(new DebugBusModule());
  }

  @Override
  protected void onCreate(Bundle state) {
    super.onCreate(state);
    setContentView(R.layout.activity_debug);
    ((DebugBusSubcomponent) getComponent()).inject(this);
  }
}

