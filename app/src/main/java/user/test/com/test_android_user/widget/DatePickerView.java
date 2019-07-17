package user.test.com.test_android_user.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
public class DatePickerView extends View {
    /**
     * text之间间距和minTextSize之比
     */
    public static final float MARGIN_ALPHA = 3.0f;
    /**
     * 自动回滚到中间的速度
     */
    public static final float SPEED = 10;
    private Context context;
    /**
     * 新增字段 控制是否首尾相接循环显示 默认为循环显示
     */
    private boolean loop = false;
    private List<String> mDataList;
    /**
     * 选中的位置，这个位置是mDataList的中心位置，一直不变
     */
    private int mCurrentSelected;
    private Paint mPaint, nPaint;
    private float mMaxTextSize = dip2px(getContext(), 20);
    private float mMinTextSize = dip2px(getContext(), 15);
    private float mMaxTextAlpha = 255;
    private float mMinTextAlpha = 120;
    private int mViewHeight;
    private int mViewWidth;
    private float mLastDownY;
    /**
     * 滑动的距离
     */
    private float mMoveLen = 0;
    private boolean isInit = false;
    private boolean canScroll = true;
    private onSelectListener mSelectListener;
    private Timer timer;
    private MyTimerTask mTask;
    private String unit; //单位（后面文字）
    private int holdPosition; //左右位置显示微调  1：左边  -1：右边
    private Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (Math.abs(mMoveLen) < SPEED) {
                mMoveLen = 0;
                if (mTask != null) {
                    mTask.cancel();
                    mTask = null;
                    performSelect();
                }
            }else{
                mMoveLen = mMoveLen - mMoveLen / Math.abs(mMoveLen) * SPEED;
            }
            invalidate();
        }
    };
    private Paint linePaint;
    public DatePickerView(Context context) {
        super(context);
    }
    public DatePickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    private void init() {
        timer = new Timer();
        mDataList = new ArrayList<>();
        //第一个paint
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(Color.parseColor("#ff4f4f4f"));
        //第二个paint
        nPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nPaint.setStyle(Paint.Style.FILL);
        nPaint.setTextAlign(Paint.Align.CENTER);
        nPaint.setColor(Color.parseColor("#ff4f4f4f"));
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(Color.parseColor("#ff9b9b9b"));
        linePaint.setStrokeWidth(1f);
    }
    public DatePickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                doDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveLen += (event.getY() - mLastDownY);
                if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {
                    if (!loop && mCurrentSelected == 0) {
                        mLastDownY = event.getY();
                        invalidate();
                        return true;
                    }
                    if (!loop) {
                        mCurrentSelected--;
                    }
                    moveTailToHead();
                    mMoveLen = mMoveLen - MARGIN_ALPHA * mMinTextSize;
                } else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {
                    if (mCurrentSelected == mDataList.size() - 1) {
                        mLastDownY = event.getY();
                        invalidate();
                        return true;
                    }
                    if (!loop) {
                        mCurrentSelected++;
                    }
                    moveHeadToTail();
                    mMoveLen = mMoveLen + MARGIN_ALPHA * mMinTextSize;
                }
                mLastDownY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                doUp();
                break;
            default:
                break;
        }
        return true;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInit) {
            drawData(canvas);
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        isInit = true;
        invalidate();
    }
    private void drawData(Canvas canvas) {
        // 先绘制选中的text再往上往下绘制其余的text
        float scale = parabola(mViewHeight / 3.2f, mMoveLen);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        // text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
        float x = (float) (mViewWidth / 2.0 + dip2px(getContext(), 8) * holdPosition);
        float y = (float) (mViewHeight / 2.0 + mMoveLen);
        Paint.FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
        canvas.drawText(mDataList.get(mCurrentSelected) + unit, x, baseline, mPaint);
        canvas.drawLine(0, (float) (mViewHeight / 2.0 - mViewHeight / 9), mViewWidth, (float) (mViewHeight / 2.0 - mViewHeight / 9), linePaint);
        canvas.drawLine(0, (float) (mViewHeight / 2.0 + mViewHeight / 9), mViewWidth, (float) (mViewHeight / 2.0 + mViewHeight / 9), linePaint);
        // 绘制上方data
        for (int i = 1; (mCurrentSelected - i) >= 0; i++) {
            drawOtherText(canvas, i, -1);
        }
//        // 绘制下方data
        for (int i = 1; (mCurrentSelected + i) < mDataList.size(); i++) {
            drawOtherText(canvas, i, 1);
        }
    }
    private void drawOtherText(Canvas canvas, int position, int type) {
        float d = MARGIN_ALPHA * mMinTextSize * position + type * mMoveLen;
        float scale = parabola(mViewHeight / 3.2f, d);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        nPaint.setTextSize(size);
        nPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        float y = (float) (mViewHeight / 2.0 + type * d );
        Paint.FontMetricsInt fmi = nPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
        canvas.drawText(mDataList.get(mCurrentSelected + type * position) + unit,
                (float) (mViewWidth / 2.0 + dip2px(getContext(), 8) * holdPosition), baseline, nPaint);
    }
    /**
     * 抛物线
     *
     * @param zero 零点坐标
     * @param x    偏移量
     */
    private float parabola(float zero, float x) {
        float f = (float) (1 - Math.pow(x / zero, 2));
        return f < 0 ? 0 : f;
    }
    private void doDown(MotionEvent event) {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mLastDownY = event.getY();
    }
    private void moveTailToHead() {
        if (loop) {
            String tail = mDataList.get(mDataList.size() - 1);
            mDataList.remove(mDataList.size() - 1);
            mDataList.add(0, tail);
        }
    }
    private void moveHeadToTail() {
        if (loop) {
            String head = mDataList.get(0);
            mDataList.remove(0);
            mDataList.add(head);
        }
    }
    private void doUp() {
        if (Math.abs(mMoveLen) < 0.0001) {
            mMoveLen = 0;
            return;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mTask = new MyTimerTask(updateHandler);
        timer.schedule(mTask, 0, 10);
    }
    private void performSelect() {
        if (mSelectListener != null) {
            mSelectListener.onSelect(mDataList.get(mCurrentSelected));
        }
    }
    public void setData(List<String> data) {
        mDataList = data;
        mCurrentSelected = data.size() / 4;
        invalidate();
    }
    public void setUnit(String unit){
        this.unit = unit;
    }
    public void setHoldPosition(int position){
        this.holdPosition = position;
    }
    public void setOnSelectListener(onSelectListener listener) {
        mSelectListener = listener;
    }
    public void setSelected(int selected) {
        mCurrentSelected = selected;
        if (loop) {
            int distance = mDataList.size() / 2 - mCurrentSelected;
            if (distance < 0) {
                for (int i = 0; i < -distance; i++) {
                    moveHeadToTail();
                    mCurrentSelected--;
                }
            } else if (distance > 0) {
                for (int i = 0; i < distance; i++) {
                    moveTailToHead();
                    mCurrentSelected++;
                }
            }
        }
    }
    /**
     * 选择选中的内容
     */
    public void setSelected(String mSelectItem) {
        for (int i = 0; i < mDataList.size(); i++) {
            if (mDataList.get(i).equals(mSelectItem)) {
                setSelected(i);
                break;
            }
        }
    }
    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return canScroll && super.dispatchTouchEvent(event);
    }
    /**
     * 控制内容是否首尾相连
     */
    public void setIsLoop(boolean isLoop) {
        loop = isLoop;
    }
    public interface onSelectListener {
        void onSelect(String text);
    }
    class MyTimerTask extends TimerTask {
        Handler handler;
        public MyTimerTask(Handler handler) {
            this.handler = handler;
        }
        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
