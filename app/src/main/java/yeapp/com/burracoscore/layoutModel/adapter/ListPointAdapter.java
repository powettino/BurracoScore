package yeapp.com.burracoscore.layoutModel.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import yeapp.com.burracoscore.R;
@Deprecated
public class ListPointAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> textL;
    private ArrayList<String> textR;
    private int lastPosition = -1;

    public ListPointAdapter(Context context) {
        super();
        textL = new ArrayList<String>();
        textR = new ArrayList<String>();
        this.context = context;
    }

    public void clearData() {
        textL.clear();
        textR.clear();
    }

    public ListPointAdapter(Context context, ArrayList<String> textLeft, ArrayList<String> textRight) {
        super();
        textL = textLeft;
        textR = textRight;
        this.context = context;
    }

    public void setArrayLists(ArrayList<String> textLeft, ArrayList<String> textRight) {
        textL = textLeft;
        textR = textRight;
    }

    public void restore(ArrayList<Integer> textLeft) {
        textL.clear();
        textR.clear();
        for (int i = 0; i < textLeft.size(); i++) {
            textL.add(String.valueOf(textLeft.get(i)));
            textR.add("G" + (i + 1));
        }
    }

    public Pair<String, String> getLastDoubleText() {
        if (textL.isEmpty() && textR.isEmpty()) {
            return new Pair<String, String>(null, null);
        }
        return new Pair<String, String>(textL.get(textL.size() - 1), textR.get(textR.size() - 1));
    }

    public void addLastDoubleText(String lastTextL, String lastTextR) {
        textL.add(lastTextL);
        textR.add(lastTextR);
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
//        ((TextView) convertView.findViewById(R.id.pointText)).setText(textL.get(position));
//        ((TextView) convertView.findViewById(R.id.pointGame)).setText(textR.get(position));
        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.go_down : R.anim.fading);
        convertView.startAnimation(animation);
        lastPosition = position;
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}