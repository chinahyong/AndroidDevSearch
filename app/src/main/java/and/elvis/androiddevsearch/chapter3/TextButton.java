package and.elvis.androiddevsearch.chapter3;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.TextView;

/**
 * @author Elvis
 */
public class TextButton extends AppCompatTextView implements OnGestureListener, GestureDetector.OnDoubleTapListener {
    // 触摸时间辅助类
    GestureDetector mGestureDetector = new GestureDetector(this);
    // 监听触摸移动速度
    VelocityTracker mVelocityTracker = VelocityTracker.obtain();

    public TextButton(Context context) {
        super(context);
        // 设置长按屏幕后无法拖动的问题
        mGestureDetector.setIsLongpressEnabled(false);
        // 最小触控范围(小于这个值，认为没有做滑动操作)
        ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public TextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 设置长按屏幕后无法拖动的问题
        mGestureDetector.setIsLongpressEnabled(false);
    }

    public TextButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 设置长按屏幕后无法拖动的问题
        mGestureDetector.setIsLongpressEnabled(false);
    }

    private int mLastX;
    private int mLastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 对触摸时间进行监控
        mVelocityTracker.addMovement(event);
        // 计算1000ms内的速度
        mVelocityTracker.computeCurrentVelocity(1000);
        // 横坐标、纵坐标的速度(从下往上、从右往左滑动 X/Y 为负值)
        float velocityX = mVelocityTracker.getXVelocity();
        float velocityY = mVelocityTracker.getYVelocity();
        // 速度监听使用完需要回收
        mVelocityTracker.clear();
        mVelocityTracker.recycle();
        // 监听双击行为，可不添加
        mGestureDetector.setOnDoubleTapListener(this);
        //通过GestureDetector来实现触摸事件
        return mGestureDetector.onTouchEvent(event);
    }


    /**
     * 手指按下的一瞬间触发
     *
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    /**
     * 手指按下，为进行放开、拖动行为时触发
     *
     * @param motionEvent
     */
    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    /**
     * 相当于：ACTION_UP ,属于一个单击行为放开；快速可以为双击行为到onDoubleTap
     *
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    /**
     * 手机按下进行拖动，1个ACTION_DOWN多个ACTION_MOVE触发，拖动行为
     *
     * @param motionEvent
     * @param motionEvent1
     * @param v
     * @param v1
     * @return
     */
    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    /**
     * 长按
     *
     * @param motionEvent
     */
    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    /**
     * 快速滑动行为  ACTION_DOWN->ACTION_MOVE...->ACTION_UP
     *
     * @param motionEvent
     * @param motionEvent1
     * @param v
     * @param v1
     * @return
     */
    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    /**
     * 单击，并且不会是双击
     *
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    /**
     * 双击行为 不会与onSingleTapConfirmed 共存
     *
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        return false;
    }

    /**
     * 发生双击行为：双击期间ACTION_DOWN  ACTION_MOVE ACTION_UP都会触发此事件
     *
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }
}
