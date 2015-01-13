package yeapp.com.burracoscore.custom.adapter;

import java.util.List;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import yeapp.com.burracoscore.R;

/**
 * Created by iacopo on 13/01/15.
 */
public class DoubleTextAdapter extends BaseAdapter {

    private Context context;
    private List<String> textL;
    private List<String> textR;


    public DoubleTextAdapter(Context context, List<String> textLeft, List<String> textRight) {
        super();
        textL = textLeft;
        textR = textRight;
        this.context = context;
    }

    @Override
    public int getCount() {
        return textL.size();
    }

    @Override
    public Object getItem(int position) {
        return new Pair(textL.get(position), textR.get(position));
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
        ((TextView) convertView.findViewById(R.id.pointText)).setText(textL.get(position));
        ((TextView) convertView.findViewById(R.id.pointGame)).setText(textR.get(position));
        return convertView;
    }
}