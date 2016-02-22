package ga.imagination.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 44260 on 2016/2/18.
 */
public class GraduatedScale extends View {

    private boolean isPressed = false;
    private Paint paint;
    private PointF cenPoint = new PointF();
    private PointF pressPoint = new PointF();
    private float radius = 60;
    private float min = 0;
    private float max = 100;

    public GraduatedScale(Context context) {
        this(context, null);
    }

    public GraduatedScale(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GraduatedScale(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent();
    }

    private void initComponent() {
        paint = new Paint();
        paint.setColor(Color.BLUE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        cenPoint.set(radius, getMeasuredHeight() / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(cenPoint.x, cenPoint.y, radius, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isPressed = true;
                pressPoint.set(event.getX(), event.getY());
                cenPoint.x = pressPoint.x;
                postInvalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                float distance = event.getX() - pressPoint.x;
                cenPoint.x += distance;
                pressPoint.x = cenPoint.x;
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                isPressed = false;
                break;
        }
        return true;
    }
}
