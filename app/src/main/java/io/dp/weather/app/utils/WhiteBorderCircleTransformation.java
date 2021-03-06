package io.dp.weather.app.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.squareup.picasso.Transformation;

/**
 * Created by dp on 10/10/14.
 */
public class WhiteBorderCircleTransformation implements Transformation {

  private final Paint whitePaint = new Paint();

  public WhiteBorderCircleTransformation() {
    whitePaint.setColor(Color.WHITE);
    whitePaint.setAntiAlias(true);
  }

  @Override
  public Bitmap transform(Bitmap source) {
    int size = Math.min(source.getWidth(), source.getHeight());

    int x = (source.getWidth() - size) / 2;
    int y = (source.getHeight() - size) / 2;

    Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
    if (squaredBitmap != source) {
      source.recycle();
    }

    Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

    Canvas canvas = new Canvas(bitmap);
    Paint paint = new Paint();
    BitmapShader
        shader =
        new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
    paint.setShader(shader);
    paint.setAntiAlias(true);

    float r = size / 2f;
    float margin = r - 2.0f;

    canvas.drawCircle(r, r, r, whitePaint);
    canvas.drawCircle(r, r, margin, paint);

    squaredBitmap.recycle();
    return bitmap;
  }

  @Override
  public String key() {
    return "white_border_circle";
  }
}
