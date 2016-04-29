package online.iiitd.edu.in.iiitd_online;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by atul on 4/30/16.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private final Context context;
    private JSONArray mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Context context;
        // each data item is just a string in this case

        protected TextView title;
        protected int id;

        public ViewHolder(View v, Context context) {
            super(v);
//            mTextView = v;
            this.context = context;
            title  = (TextView) v.findViewById(R.id.title);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToCommunity(v);
                }
            });
        }

        private void goToCommunity(View v) {
            Intent i = new Intent(v.getContext(), Community.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("id", this.id);
            context.startActivity(i);
        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardAdapter(JSONArray myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_adapter, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v, context);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        try {
            JSONObject jb =  (JSONObject) mDataset.get(position);
            Log.v("DEBUG", jb.getString("name"));
            holder.title.setText(jb.getString("name"));
            holder.id = jb.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length();
    }
}
