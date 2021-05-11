package com.example.langlearn.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.langlearn.MainActivity;
import com.example.langlearn.R;
import com.example.langlearn.Util;
import com.example.langlearn.model.Message;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.net.URL;
import java.util.List;

import static com.example.langlearn.Util.translate;

public class MessageAdapter extends RecyclerView.Adapter {

    Context mContext;
    List<Message> mMessages;

    public MessageAdapter(Context context, List<Message> messages){
        mContext = context;
        mMessages = messages;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_message, parent, false);

        return new ViewHoldertwo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = mMessages.get(position);
        Log.d("MESSAGE", message.getKeyMessage());
        ViewHoldertwo ViewHoldertwo = (ViewHoldertwo) holder;
        final String[] otherImage = new String[1];
        String meImage;

        final boolean isMe = message.get("from").equals(ParseUser.getCurrentUser().getObjectId());

        if (isMe){
            ViewHoldertwo.ivMe.setVisibility(View.VISIBLE);
            ViewHoldertwo.ivOther.setVisibility(View.GONE);
            ViewHoldertwo.tvMessage.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            meImage = ParseUser.getCurrentUser().getString(Util.PROFILE_IMG);
            final ImageView profileView = ViewHoldertwo.ivMe;
            Glide.with(mContext).load(meImage).into(profileView);
            ViewHoldertwo.tvMessage.setText(message.getKeyMessage());
        } else {
            ViewHoldertwo.ivOther.setVisibility(View.VISIBLE);
            ViewHoldertwo.ivMe.setVisibility(View.GONE);
            ViewHoldertwo.tvMessage.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
            query.whereEqualTo("objectId",message.get("from"));
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        //List contain object with specific user id.
                        otherImage[0] = objects.get(0).getString(Util.PROFILE_IMG);
                        final ImageView profileView = ViewHoldertwo.ivOther;
                        Glide.with(mContext).load(otherImage[0]).into(profileView);
                        translate(message.getString("message"), objects.get(0).getString("nativelang"), ParseUser.getCurrentUser().getString("nativelang"), (msg)-> {

                            ViewHoldertwo.tvMessage.setText(msg.getData().getString("result"));
                            return false;
                        });
                    } else {
                        // error
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ViewHoldertwo extends RecyclerView.ViewHolder {

        TextView tvMessage;
        ImageView ivOther;
        ImageView ivMe;

        public ViewHoldertwo(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            ivOther = itemView.findViewById(R.id.ivProfileOther);
            ivMe = itemView.findViewById(R.id.ivProfileMe);
        }
    }


}
