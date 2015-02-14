package yeapp.com.burracoscore.layoutModel.adapter;

import android.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

import yeapp.com.burracoscore.R;

/**
 * Created by iacopo on 03/02/15.
 */
public class ScoreRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<String> textL;
    private ArrayList<String> textR;
    private boolean mainTextLeft = true;


    public ScoreRecyclerAdapter(boolean mainTextLeft) {
        super();
        textL = new ArrayList<String>();
        textR = new ArrayList<String>();
        this.mainTextLeft = mainTextLeft;
    }

    public ScoreRecyclerAdapter(ArrayList<String> textLeft, ArrayList<String> textRight, boolean mainTextLeft) {
        super();
        textL = textLeft;
        textR = textRight;
        this.mainTextLeft = mainTextLeft;
    }

    public boolean isMainTextLeft() {
        return mainTextLeft;
    }

    public void setMainTextLeft(boolean mainTextLeft) {
        this.mainTextLeft = mainTextLeft;

    }

    public Pair<String, String> getLastDoubleText() {
        if (textL.isEmpty() && textR.isEmpty()) {
            return new Pair<String, String>(null, null);
        }
        return new Pair<String, String>(textL.get(textL.size() - 1), textR.get(textR.size() - 1));
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

    public void addLastDoubleText(String points, String game) {
        if (mainTextLeft) {
            textL.add(formatPoints(points));
            textR.add(game);
        } else {
            textL.add(game);
            textR.add(formatPoints(points));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_point, parent, false);
        if (mainTextLeft) {
            View text = (TextView) v.findViewById(R.id.textRowPointRight);
            text.setBackgroundResource(R.drawable.shape_image_game);
        } else {
            View textL = v.findViewById(R.id.textRowPointLeft);
            textL.setBackgroundResource(R.drawable.shape_image_game);
        }

        return new ViewHolder(v) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
    }

    public void clearData() {
        textL.clear();
        textR.clear();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ((TextView) holder.itemView.findViewById(R.id.textRowPointLeft)).setText(textL.get(position));
        ((TextView) holder.itemView.findViewById(R.id.textRowPointRight)).setText(textR.get(position));
    }

    @Override
    public int getItemCount() {
        return textL.size();
    }
}
