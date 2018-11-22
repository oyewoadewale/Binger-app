package com.example.android.Binger.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.Binger.Objects.DataObject;
import com.example.android.Binger.Objects.Review;
import com.example.android.Binger.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Adewale on 22/11/2018.
 */


public class FavouriteReviewsAdapter extends RecyclerView.Adapter<FavouriteReviewsAdapter.FavouriteReviewViewHolder> {


    static Context mContext;
    private List<DataObject> mMovieList = null;
    ArrayList<Review> reviews = null;

    public FavouriteReviewsAdapter(Context context, List<DataObject> movieList) {
        mContext = context;
        mMovieList = movieList;
    }


    @Override
    public FavouriteReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_view;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        FavouriteReviewViewHolder viewHolder = new FavouriteReviewViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(FavouriteReviewsAdapter.FavouriteReviewViewHolder holder, int position) {

        //populate views
        final DataObject movie = mMovieList.get(position);
        //reviews = movie.getMovieReviews();
        Review r = new Review();
        r = movie.getMovieReviews().get(position);

        holder.author.setText(r.getAuthor());
        holder.content.setText(r.getContent());

//        Intent intent = new Intent(mContext, ReviewActivity.class);
//                intent.putExtra("Image ", imageFileName);
//                intent.putExtra("Overview", movie.getOverview());
//                intent.putExtra("Name", movie.getName());
//                intent.putExtra("releaseDate", movie.getReleaseDate());
//                intent.putExtra("rating", movie.getRating());
//                intent.putExtra("backdrop", backdropFileName);
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("ArrayOfReviews",movie);
//        intent.putExtras(bundle);
//        mContext.startActivity(intent);
    }


        @Override
        public int getItemCount () {
            if (reviews == null) {
                return 0;
            }
            return reviews.size();
        }

        public class FavouriteReviewViewHolder extends RecyclerView.ViewHolder {
            TextView author;
            TextView content;


            public FavouriteReviewViewHolder(View itemView) {
                super(itemView);

                author = itemView.findViewById(R.id.author);
                content = itemView.findViewById(R.id.review_text);


            }
        }


    public List<DataObject> getMovieList() {
        return mMovieList;
    }

    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */

    public void setmMovieList(List<DataObject> movies) {
        mMovieList = movies;
        notifyDataSetChanged();
    }

}
