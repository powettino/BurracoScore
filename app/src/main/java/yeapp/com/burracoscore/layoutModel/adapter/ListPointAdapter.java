package yeapp.com.burracoscore.layoutModel.adapter;

import android.content.Context;
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

public class ListPointAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> textL;
    private ArrayList<String> textR;
    private int lastPosition = -1;
    private boolean mainTextLeft = true;

    public ListPointAdapter(Context context, boolean mainTextLeft) {
        super();
        textL = new ArrayList<String>();
        textR = new ArrayList<String>();
        this.context = context;
        this.mainTextLeft = mainTextLeft;
    }

    public void clearData() {
        textL.clear();
        textR.clear();
        lastPosition = -1;
    }

    public ListPointAdapter(Context context, ArrayList<String> textLeft, ArrayList<String> textRight, boolean mainTextLeft) {
        super();
        textL = textLeft;
        textR = textRight;
        this.context = context;
        this.mainTextLeft = mainTextLeft;
    }

    public boolean isMainTextLeft() {
        return mainTextLeft;
    }

    public void setMainTextLeft(boolean mainTextLeft) {
        this.mainTextLeft = mainTextLeft;
    }

    public void setArrayLists(ArrayList<String> textLeft, ArrayList<String> textRight) {
        textL = textLeft;
        textR = textRight;
    }

    public void restore(ArrayList<Integer> pointText) {
        textL.clear();
        textR.clear();
        for (int i = 0; i < pointText.size(); i++) {
            if (mainTextLeft) {
                textL.add(String.valueOf(pointText.get(i)));
                textR.add("G" + (i + 1));
            } else {
                textL.add("G" + (i + 1));
                textR.add(String.valueOf(pointText.get(i)));
            }
        }
    }

    private String formatPoints(String points) {
        if (!points.startsWith("-")) {
            return "  " + points;
        }
        return points;
    }

    public Pair<String, String> getLastDoubleText() {
        if (textL.isEmpty() && textR.isEmpty()) {
            return new Pair<String, String>(null, null);
        }
        return new Pair<String, String>(textL.get(textL.size() - 1), textR.get(textR.size() - 1));
    }

    public void addLastDoubleText(String points, String game) {
        if (mainTextLeft) {
            textL.add(formatPoints(points));
            textR.add(game);
        } else {
            textL.add(game);
            textR.add(formatPoints(points));
        }
    }

    public ArrayList<String> getTextLeft() {
        return textL;
    }

    public ArrayList<String> getTextRight() {
        return textR;
    }

    @Override
    public int getCount() {
        return textL.size();
    }

    @Override
    public Object getItem(int position) {
        return new Pair<String, String>(textL.get(position), textR.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.row_point, parent, false);
        }
        if (mainTextLeft) {
            convertView.setPadding(50, convertView.getPaddingTop(), convertView.getPaddingRight(), convertView.getPaddingBottom());
            TextView text = (TextView) convertView.findViewById(R.id.textRowPointRight);
            text.setBackgroundResource(R.drawable.shape_image_game);
            text.setText(textR.get(position));
            ((TextView) convertView.findViewById(R.id.textRowPointLeft)).setText(textL.get(position));
        } else {
            convertView.setPadding(convertView.getPaddingLeft(), convertView.getPaddingTop(), 50, convertView.getPaddingBottom());
            TextView text = (TextView) convertView.findViewById(R.id.textRowPointLeft);
            text.setBackgroundResource(R.drawable.shape_image_game);
            text.setText(textL.get(position));
            ((TextView) convertView.findViewById(R.id.textRowPointRight)).setText(textR.get(position));

        }
        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.go_down);
            convertView.startAnimation(animation);
        }
        lastPosition = position > lastPosition ? position : lastPosition;
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}