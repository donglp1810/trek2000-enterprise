package custom_view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import com.trek2000.android.enterprise.R;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;

/**
 * Created by trek2000 on 24/10/2014.
 */
public class ButtonTag extends Button {

    /**
     * String section
     */
    private static String TEXT = "";

    /**
     * View section
     */
    private AlertDialog.Builder mDialogConfirm;

    public ButtonTag(final Context context, String text) {
        super(context);

        // Set background color
        setBackgroundColor(context.getResources().getColor(R.color.orange));

        // Set text
        setText(text);

        setTextColor(Color.WHITE);

        // Layout params for margin
        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                FlowLayout.LayoutParams.WRAP_CONTENT, 50);
        layoutParams.setMargins(10, 5, 10, 5);
        setLayoutParams(layoutParams);

        // Set padding
        setPadding(5, 0, 5, 0);

        // Set on click listener
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                TEXT = getText().toString();
                initialDialog(context);

                /**
                 * Check if title can be edited, the user want to edit something,
                 * so need show the confirm dialog
                 */
            }
        });
    }

    private void initialDialog(Context context) {
        mDialogConfirm = new AlertDialog.Builder(context);
        mDialogConfirm.setTitle("");
        mDialogConfirm.setCancelable(false);
        mDialogConfirm.setMessage(context.getString(R.string.content_delete) + " " + TEXT);
        mDialogConfirm.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int whichButton) {
                // Remove the selected tag out of parent view
            }
        });
        mDialogConfirm.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    private void removeView(int pos, FlowLayout flowlayout, ArrayList<String> mAlTags) {
        if (flowlayout.getChildAt(pos) != null) {
            mAlTags.remove(pos);
            flowlayout.removeViewAt(pos);
        }
    }
}
