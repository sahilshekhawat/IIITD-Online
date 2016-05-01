package online.iiitd.edu.in.iiitd_online;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import online.iiitd.edu.in.iiitd_online.usercommunity.UserCommunityContent;

/**
 * Created by sahil on 5/1/16.
 */
public class CommunityPostsItemRecyclerViewAdapter extends RecyclerView.Adapter<CommunityPostsItemRecyclerViewAdapter.ViewHolder> {

    private final List<UserCommunityContent.UserCommunityItem> mValues;
    private final UserCommunityItemFragment.OnUserCommunityFragmentInteractionListener mListener;

    public CommunityPostsItemRecyclerViewAdapter(List<UserCommunityContent.UserCommunityItem> items, UserCommunityItemFragment.OnUserCommunityFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.communitypostlist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        Log.d(" POSTS TEXT: ", mValues.get(position).name);
        holder.mContentView.setText(mValues.get(position).name);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onUserCommunityFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public UserCommunityContent.UserCommunityItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}

