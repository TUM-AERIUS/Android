package michaelblind.aeriusapp;

import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int DOWN = 0;
    int MOVE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action  = MotionEventCompat.getActionMasked(event);
        if (action != DOWN && action != MOVE)
            return super.onTouchEvent(event);

        float x = event.getX();
        float y = event.getY();

        View field = findViewById(R.id.all);

        float[] movement = transmit(x, y, field, getCar().getHeight() * 0.5f);

        setPointers(movement);

        return super.onTouchEvent(event);
    }

    private void setPointers(float[] movement) {
        View[] pointers = getPointers();
        View car = getCar();

        //Start X & Y
        float sX = car.getX() + halfWidth(car) - halfWidth(pointers[0]);
        float sY = car.getY() - 0.5f * pointers[0].getHeight();

        if (movement[0] <  0) sY += car.getHeight();
        if (movement[0] == 0) sY = car.getY();


        float step = car.getHeight() * 0.225f;

        if (movement[0] < 0) step *= -1;

        float[] start = new float[]{sX, sY};
        float[][] dest = triangulateLocations(start, step, movement[1]);

        drawPointers(pointers, dest, movement[0]);
    }

    private float[][] triangulateLocations(float[] start, float step, float deg) {
        float microDeg = deg / 5.0f;
        float[][] loc = new float[10][2];
        loc[0] = start;

        deg = 0;

        for (int i = 1; i < 10; i++) {
            deg += microDeg;
            double rad = Math.toRadians(deg);

            float[] c = loc[i];
            c[0] = (float) (loc[i - 1][0] - step * Math.sin(rad));  //sin deg * step + loc[i-1][0]
            c[1] = (float) (loc[i - 1][1] - step * Math.cos(rad));  //cos deg * step + loc[i-1][1]
        }

        return loc;

    }

    private void drawPointers(View[] p, float[][] dest, float velocity) {
        float dec = 6.0f / (Math.abs(velocity) + 0.001f);
        for (int i = 0; i < 10; i++) {
            View v = p[i];
            float[] pos = dest[i];

            v.setX(pos[0]);
            v.setY(pos[1]);
            v.setAlpha(Math.max(0, 1.0f - i * dec));
        }

    }

    private float halfWidth(View v) { return v.getWidth() * 0.5f; }

    private View getCar() { return findViewById(R.id.car); }

    private View[] getPointers() {
        return new View[]{
                findViewById(R.id.pointer0),
                findViewById(R.id.pointer1),
                findViewById(R.id.pointer2),
                findViewById(R.id.pointer3),
                findViewById(R.id.pointer4),
                findViewById(R.id.pointer5),
                findViewById(R.id.pointer6),
                findViewById(R.id.pointer7),
                findViewById(R.id.pointer8),
                findViewById(R.id.pointer9),
                };
    }

    private float[] transmit(float x, float y, View field, float y_car) {
        float y_center = (float) field.getHeight() / 2.0f;
        float x_center = (float) field.getWidth() / 2.0f;

        float x_abs = x - x_center;
        float y_abs = y - y_center;

        //Check if inside Car
        if (Math.abs(y_abs) <= y_car) y_abs = 0;

        float velocity = y_abs * (-100.0f) / y_center;
        float steering = x_abs * (-40.0f)  / x_center;  //40.0: Conversion to 80° Field


        TextView status = (TextView) findViewById(R.id.output);
        status.setText(String.format("%.1f%%  %.1f°", velocity, steering));

        return new float[]{velocity, steering};
    }

}