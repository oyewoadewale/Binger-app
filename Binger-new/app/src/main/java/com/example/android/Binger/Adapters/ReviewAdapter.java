package com.example.android.Binger.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.Binger.Database.AppDatabase;
import com.example.android.Binger.Objects.Review;
import com.example.android.Binger.Objects.Youtube;
import com.example.android.Binger.R;

import java.util.ArrayList;


/**
 * Created by Adewale on 22/11/2018.
 */

//public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
//
//    Context mContext;
//    private ArrayList<Review> mReview = null;

public class ReviewAdapter extends ArrayAdapter<Review> {

    public ReviewAdapter(Context context, ArrayList<Review> keys) {
        super(context, 0, keys);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Review k = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_view, parent, false);
        }

        final TextView author = convertView.findViewById(R.id.author);
        final TextView content = convertView.findViewById(R.id.review_text);

        if (k != null){
            author.setText(k.getAuthor());
            content.setText(k.getContent());
        }

        return convertView;
        }
    }






//    public ReviewAdapter(Context context, ArrayList<Review> r){
//        mContext = context;
//        mReview = r;
//    }
//
//    @Override
//    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        Context context = viewGroup.getContext();
//        int layoutIdForListItem = R.layout.review_view;
//        LayoutInflater inflater = LayoutInflater.from(context);
//        boolean shouldAttachToParentImmediately = false;
//
//        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
//        ReviewViewHolder viewHolder = new ReviewViewHolder(view);
//        return viewHolder;
//    }
//
//
//    @Override
//    public void onBindViewHolder(ReviewViewHolder holder, int position) {
//
//        //populate views
//        final Review rev = mReview.get(position);
//
//        holder.author.setText(rev.getAuthor());
//        holder.content.setText(rev.getContent());
//    }
//
//    @Override
//    public int getItemCount() {
//        if (mReview == null){
//            return 0;
//        }
//        return mReview.size();
//    }
//
//    public class ReviewViewHolder extends RecyclerView.ViewHolder {
//        TextView author;
//        TextView content;
//
//
//        public ReviewViewHolder(View itemView) {
//            super(itemView);
//
//            author = itemView.findViewById(R.id.author);
//            content = itemView.findViewById(R.id.review_text);
//
//
//        }
//    }

//    public ArrayList<Review> getMovieList() {
//        return mReview;
//    }
//
//    public void setmReviewList(List<DataObject> dataObject) {
//
//        dataObjects = dataObject;
//        DataObject d = new DataObject();
//        d.setMovieReviews(mReview);
//        dataObject.add(d);
//        notifyDataSetChanged();
//    }
//}