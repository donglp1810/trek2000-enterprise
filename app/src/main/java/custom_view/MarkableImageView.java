package custom_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;

import com.trek2000.android.enterprise.R;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by trek2000 on 23/8/2014.
 */
public class MarkableImageView extends GifImageView {
    /**
     * String section
     */
    private String DURATION_OF_VIDEO = "";

    private boolean IS_CHECKED = false;
    private boolean IS_VIDEO = false;

    /**
     * The others section
     */
    private Context mContext;

    /**
     * @param context
     */
    public MarkableImageView(Context context) {
        super(context);

        this.mContext = context;
    }

    public MarkableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mContext = context;
    }

    public MarkableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.mContext = context;
    }

    public boolean isChecked() {
        return IS_CHECKED;
    }

    public void setChecked(boolean checked) {
        this.IS_CHECKED = checked;
        invalidate();
    }

    public boolean isVideo() {
        return IS_VIDEO;
    }

    public void setDurationOfVideo(String DURATION_OF_VIDEO) {
        this.DURATION_OF_VIDEO = DURATION_OF_VIDEO;
    }

    public void setVideo(boolean isVideo) {
        this.IS_VIDEO = isVideo;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * If item was video, load overlay icon on it
         */
        if (IS_VIDEO) {
            /**
             * Show Video icon overlay on preview image of video
             */
            canvas.drawBitmap(
                    BitmapFactory.decodeResource(getResources(), R.drawable.iv_video_fl),
                    20, mContext.getResources().getInteger(R.integer.height_file_view) - 50, new Paint());

            /**
             * Need to show time length of video overlay on preview image of video
             */
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(50);
            // draw text to the Canvas center
            canvas.drawText(DURATION_OF_VIDEO,
                    mContext.getResources().getInteger(R.integer.width_file_view) - 75,
                    mContext.getResources().getInteger(R.integer.height_file_view) - 15,
                    paint);
        }

        /**
         * If item was selected
         */
        if (IS_CHECKED) {
            canvas.drawBitmap(Bitmap.createScaledBitmap(
                    BitmapFactory.decodeResource(getResources(), R.drawable.ibtn_rectangle_selected_thinner),
                    mContext.getResources().getInteger(R.integer.height_file_view),
                    mContext.getResources().getInteger(R.integer.width_file_view),
                    false), 0, 0, new Paint());
        }
    }
}
