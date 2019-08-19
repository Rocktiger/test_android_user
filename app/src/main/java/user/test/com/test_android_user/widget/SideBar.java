package user.test.com.test_android_user.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sj on 13/04/2017.
 */

public class SideBar extends View {

    private static final int TEXT_SIZE_DEF = 12;

    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    public final String[] sNomalLetters = {"#", "热", "A", "B", "C", "D", "E", "F", "G", "H",
            "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T",
            "W", "X", "Y", "Z"};

    public ArrayList<String> mLetterData = new ArrayList<>();

    private int choose = -1;
    private int textSize = 0;
    private Paint paint = new Paint();
    private TextView mTextDialog;
    private Context mContext;
    private boolean isNightMode = false;

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        textSize = sp2px(mContext, TEXT_SIZE_DEF);

        for (int i = 0; i < sNomalLetters.length; i++) {
            mLetterData.add(sNomalLetters[i]);
        }
        invalidate();
        setVisibility(VISIBLE);
    }

    public SideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context) {
        this(context, null);
    }

    private int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public void setSortData(HashMap<String, String> data) {
        if (data != null && data.size() > 0) {
            mLetterData.clear();
            for (int i = 0; i < sNomalLetters.length; i++) {
                if (data.containsKey(sNomalLetters[i])) {
                    mLetterData.add(sNomalLetters[i]);
                }
            }
        }
        invalidate();
        setVisibility(VISIBLE);
    }

    // 起始Y
    int startY = 0;
    // 结束Y
    int endY = 0;
    // 每项高度
    int itemHeight = 0;
    // 每项高度 = 字体高度 * 倍率
    final float paddingMagnification = 1.0f;

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);

        int height = getHeight();
        int width = getWidth();

        // 每项高度
        itemHeight = (int) (getFontHeight() * paddingMagnification);
        int size = mLetterData.size();
        int realityHeight = size * itemHeight;

        startY = (height - realityHeight) / 2;
        endY = startY + realityHeight;


        int colorNormal = isNightMode ? Color.parseColor("#9fa2a3") : Color.parseColor("#00A1CC");
        int colorSelect = isNightMode ? Color.parseColor("#8dbfd0") : Color.parseColor("#00A1CC");

        for (int i = 0; i < mLetterData.size(); i++) {
            paint.setColor(colorNormal);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(textSize);
            // 选中的状态
            if (i == choose) {
                paint.setColor(colorSelect);
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(mLetterData.get(i)) / 2;
            float yPos = itemHeight * i + startY;
            canvas.drawText(mLetterData.get(i), xPos, yPos, paint);
            paint.reset();
        }
    }

    /**
     * 获取字体高度
     *
     * @return
     */
    public int getFontHeight() {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.top) + 2;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;

        int min = startY - itemHeight;
        int max = endY - itemHeight;
        if (y < min || y > max) {
            setBackgroundDrawable(new ColorDrawable(0x00000000));
            choose = -1;//
            invalidate();
            if (mTextDialog != null) {
                mTextDialog.setVisibility(View.INVISIBLE);
            }
            return true;
        }


        final int c = (int) ((y + itemHeight - startY) / itemHeight);
        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                choose = -1;//
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                if (oldChoose != c) {
                    if (c >= 0 && c < mLetterData.size()) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(mLetterData.get(c));
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(mLetterData.get(c));
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        choose = c;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }

}
