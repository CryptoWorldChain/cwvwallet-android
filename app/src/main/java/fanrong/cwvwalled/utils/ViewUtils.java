package fanrong.cwvwalled.utils;

import android.graphics.Color;
import android.widget.TextView;

public class ViewUtils {

    public static TextView copyTextView(TextView textView){
        TextView copy = new TextView(textView.getContext());
        copy.setLayoutParams(textView.getLayoutParams());
        copy.setText(textView.getText());
        copy.setTag("error");
        copy.setTextColor(Color.parseColor("#e70f0f"));
        copy.setTextSize(15);
        return copy;
    }
}
