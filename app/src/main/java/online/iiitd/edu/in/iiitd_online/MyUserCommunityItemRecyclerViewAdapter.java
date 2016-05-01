package online.iiitd.edu.in.iiitd_online;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import online.iiitd.edu.in.iiitd_online.UserCommunityItemFragment.OnUserCommunityFragmentInteractionListener;
import online.iiitd.edu.in.iiitd_online.usercommunity.UserCommunityContent.UserCommunityItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link UserCommunityItem} and makes a call to the
 * specified {@link OnUserCommunityFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyUserCommunityItemRecyclerViewAdapter extends RecyclerView.Adapter<MyUserCommunityItemRecyclerViewAdapter.ViewHolder> {

    private final List<UserCommunityItem> mValues;
    private final OnUserCommunityFragmentInteractionListener mListener;

    public MyUserCommunityItemRecyclerViewAdapter(List<UserCommunityItem> items, OnUserCommunityFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_usercommunityitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

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

//    public void clear(){
//        this.mValues.clear();
//    }
//
//    public void newValues(List<UserCommunityItem> mValues){
//        for(UserCommunityItem userCommunityItem: mValues){
//            this.mValues.add(userCommunityItem);
//        }
//    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public UserCommunityItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
