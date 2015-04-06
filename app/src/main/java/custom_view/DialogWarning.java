package custom_view;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.trek2000.android.enterprise.R;

import utils.Utils;

/**
 * Created by trek2000 on 26/12/2014.
 */
public class DialogWarning extends Dialog {

    public DialogWarning(final Context mContext, View v, String content, String title) {
        super(mContext);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(v);

        // Set text
        ((TextView) v.findViewById(R.id.tv_content_in_dialog_warning)).setText(content);
        ((TextView) v.findViewById(R.id.tv_title_in_dialog_warning)).setText(title);

        // Set onclick listener
        v.findViewById(R.id.btn_ok_in_dialog_warning)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isShowing()) dismiss();
                    }
                });

        Utils.initialFullWidthScreenDialog(this, Gravity.CENTER);

        try {
            if (!isShowing()) {
                show();

                // After 3 seconds, automatically dismiss
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (mContext != null) {
//                            dismiss();
//                        }
//                    }
//                }, 3000);
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
            e.printStackTrace();
        } catch (final Exception e) {
            // Handle or log or ignore
            e.printStackTrace();
        } finally {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    DialogWarning.this.cancel();
                }
            }, 3000);
        }
    }
}
