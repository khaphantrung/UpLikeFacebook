package kha.uplikefacebook;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kha on 4/6/2016.
 */
public class CustomRecyclerAdapter extends
        RecyclerView.Adapter<CustomRecyclerAdapter.RecyclerViewHolder> {
    private List<Post> mPostList;
    private View.OnClickListener mListenerLike;
    private View.OnClickListener mListenerComment;

    public CustomRecyclerAdapter(List<Post> postList, View.OnClickListener listenerLike, View.OnClickListener listenerComment) {
        this.mListenerLike = listenerLike;
        this.mPostList = postList;
        this.mListenerComment = listenerComment;

    }
    public void removeAll(){
        mPostList.clear();
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {

        return mPostList.size();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup,
                                                 int i) {
        ViewGroup view;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        view = (ViewGroup)inflater.inflate(R.layout.adapter_recycler_posts,null);
        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, final int position) {
        //viewHolder.mTxtId.setText(mPostList.get(position).getPostId());
        viewHolder.mTxtContext.setText(mPostList.get(position).getPostContext());
        viewHolder.mImageLike.setPostId(mPostList.get(position).getPostId());
        viewHolder.mImageLike.setOnClickListener(mListenerLike);
        viewHolder.mImageComment.setPostId(mPostList.get(position).getPostId());
        viewHolder.mImageComment.setOnClickListener(mListenerComment);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }



    /**
     * ViewHolder for item view of list
     */

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView mTxtContext, mTxtId;
        public MyImageView mImageLike, mImageComment;
        public RecyclerViewHolder(ViewGroup itemView) {
            super(itemView);
            mTxtContext = (TextView)  itemView.findViewById(R.id.txt_context);
            mTxtId = (TextView) itemView.findViewById(R.id.txt_id);
            mImageLike =(MyImageView) itemView.findViewById(R.id.image_like);
            mImageComment = (MyImageView) itemView.findViewById(R.id.image_comment);

            // set listener for button delete
        }

    }

}

