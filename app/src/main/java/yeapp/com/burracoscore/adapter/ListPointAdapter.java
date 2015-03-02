package yeapp.com.burracoscore.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import yeapp.com.burracoscore.R;
import yeapp.com.burracoscore.core.model.Hand;

public class ListPointAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> textPoint;
    private ArrayList<Integer> textStatus;
    private int lastPosition = -1;
    private boolean mainTextLeft = true;
    private static final int padding = 60;

    public ListPointAdapter(Context context, boolean mainTextLeft) {
        super();
        textPoint = new ArrayList<String>();
        textStatus = new ArrayList<Integer>();
        this.context = context;
        this.mainTextLeft = mainTextLeft;
    }

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
//        if (textL.size() != 0) {
//            textL.remove(textL.size() - 1);
//            textR.remove(textR.size() - 1);
//        }
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
        if(textStatus.get(position) == Hand.WON){
            background.setColorFilter(context.getResources().getColor(R.color.Verde), PorterDuff.Mode.SRC_IN);
        }else if (textStatus.get(position) == Hand.LOST){
            background.setColorFilter(context.getResources().getColor(R.color.Rosso), PorterDuff.Mode.SRC_IN);
        }else{
            background.setColorFilter(context.getResources().getColor(R.color.Grigio), PorterDuff.Mode.SRC_IN);
        }
        if (mainTextLeft) {
            convertView.setPadding(padding, convertView.getPaddingTop(), convertView.getPaddingRight(), convertView.getPaddingBottom());
            TextView text = (TextView) convertView.findViewById(R.id.textRowPointRight);
            text.setBackground(background);
            text.setText("G"+(position+1));
            ((TextView) convertView.findViewById(R.id.textRowPointLeft)).setText(textPoint.get(position));
        } else {
            convertView.setPadding(convertView.getPaddingLeft(), convertView.getPaddingTop(), padding, convertView.getPaddingBottom());
            TextView text = (TextView) convertView.findViewById(R.id.textRowPointLeft);
            text.setBackground(background);
            text.setText("G"+(position+1));
            ((TextView) convertView.findViewById(R.id.textRowPointRight)).setText(textPoint.get(position));
        }
        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.bouncing_down);
            convertView.startAnimation(animation);
        }
        lastPosition = position > lastPosition ? position : lastPosition;
        return convertView;
    }

//    @Override
//    public boolean isEnabled(int position) {
//        return false;
//    }
}