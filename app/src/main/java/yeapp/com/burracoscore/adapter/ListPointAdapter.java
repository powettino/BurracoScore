package yeapp.com.burracoscore.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import yeapp.com.burracoscore.R;
import yeapp.com.burracoscore.core.model.Hand2;
import yeapp.com.burracoscore.utils.Constants;

public class ListPointAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> textPoint;
    private ArrayList<Integer> textStatus;
    private int lastPosition = -1;
    private boolean mainTextLeft = true;

    public ListPointAdapter(Context context, boolean mainTextLeft) {
        super();
        textPoint = new ArrayList<String>();
        textStatus = new ArrayList<Integer>();
        this.context = context;
        this.mainTextLeft = mainTextLeft;
    }

    @SuppressWarnings("unused")
    public void clearData() {
        textPoint.clear();
        textStatus.clear();
        lastPosition = -1;
    }

    @SuppressWarnings("unused")
    public ListPointAdapter(Context context, ArrayList<String> textLeft, ArrayList<Integer> textRight, boolean mainTextLeft) {
        super();
        textPoint = textLeft;
        textStatus = textRight;
        this.context = context;
        this.mainTextLeft = mainTextLeft;
    }

    @SuppressWarnings("unused")
    public boolean isMainTextLeft() {
        return mainTextLeft;
    }

    @SuppressWarnings("unused")
    public void setMainTextLeft(boolean mainTextLeft) {
        this.mainTextLeft = mainTextLeft;
    }

    @SuppressWarnings("unused")
    public void setArrayLists(ArrayList<String> textLeft, ArrayList<Integer> textRight) {
        textPoint = textLeft;
        textStatus = textRight;
    }

    public void restore(ArrayList<Integer> pointText, ArrayList<Integer> status) {
        textPoint.clear();
        textStatus.clear();
        for (int i = 0; i < pointText.size(); i++) {
            textPoint.add(String.valueOf(pointText.get(i)));
            textStatus.add(status.get(i));
        }
    }

    private String formatPoints(String points) {
        if (!points.startsWith("-")) {
            return "  " + points;
        }
        return points;
    }

    @SuppressWarnings("unused")
    public Pair<String, Integer> getLastDoubleText() {
        if (textPoint.isEmpty() && textStatus.isEmpty()) {
            return new Pair<String, Integer>(null, null);
        }
        return new Pair<String, Integer>(textPoint.get(textPoint.size() - 1), textStatus.get(textStatus.size() - 1));
    }

    public void addLastDoubleText(String points, int status) {
        textPoint.add(formatPoints(points));
        textStatus.add(status);
    }

    @SuppressWarnings("unused")
    public ArrayList<String> getTextLeft() {
        return textPoint;
    }

    @SuppressWarnings("unused")
    public ArrayList<Integer> getTextRight() {
        return textStatus;
    }

    @Override
    public int getCount() {
        return textPoint.size();
    }

    @Override
    public Object getItem(int position) {
        return new Pair<String, Integer>(textPoint.get(position), textStatus.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.row_list_result, parent, false);
        }
        Drawable background = context.getResources().getDrawable(R.drawable.shape_image_game);
        if(textStatus.get(position) == Hand2.WON){
            background.setColorFilter(context.getResources().getColor(R.color.Verde), PorterDuff.Mode.SRC_IN);
        }else if (textStatus.get(position) == Hand2.LOST){
            background.setColorFilter(context.getResources().getColor(R.color.Rosso), PorterDuff.Mode.SRC_IN);
        }else{
            background.setColorFilter(context.getResources().getColor(R.color.Grigio), PorterDuff.Mode.SRC_IN);
        }
        if (mainTextLeft) {
            TextView left = (TextView)convertView.findViewById(R.id.textRowPointLeft);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(left.getLayoutParams());
            lp.weight = 1;
            lp.width = 0;
            lp.rightMargin = Constants.margin;
            lp.gravity = Gravity.RIGHT;
//            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            left.setLayoutParams(lp);

            left.setGravity(Gravity.RIGHT);
            //left.setTypeface(null, Typeface.BOLD);
            left.setText(textPoint.get(position));
            TextView right = (TextView) convertView.findViewById(R.id.textRowPointRight);
            right.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimension(R.dimen.mediumText));
            right.setBackground(background);
            right.setText("G" + (position + 1));
        } else {
            TextView right = (TextView) convertView.findViewById(R.id.textRowPointRight);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(right.getLayoutParams());
//            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            lp.weight = 1;
            lp.width = 0;
            lp.leftMargin = Constants.margin;
            lp.gravity = Gravity.LEFT;
            right.setLayoutParams(lp);
            right.setGravity(Gravity.LEFT);
            //right.setTypeface(null, Typeface.BOLD);
            right.setText(textPoint.get(position));
            TextView left = (TextView) convertView.findViewById(R.id.textRowPointLeft);
            left.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.mediumText));
            left.setBackground(background);
            left.setText("G" + (position + 1));
        }
        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.bouncing_down);
            convertView.startAnimation(animation);
        }
        lastPosition = position > lastPosition ? position : lastPosition;
        return convertView;
    }
}