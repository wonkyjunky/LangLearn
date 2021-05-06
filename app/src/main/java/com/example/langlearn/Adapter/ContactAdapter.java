package com.example.langlearn.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langlearn.MainActivity;
import com.example.langlearn.R;
import com.example.langlearn.Util;
import com.example.langlearn.fragment.MessageFragment;
import com.parse.ParseUser;

import java.net.URL;
import java.util.List;

import static com.example.langlearn.Util.langNameFromCode;

public class ContactAdapter extends RecyclerView.Adapter{

    private List<ParseUser> mUser;
    private Context mContext;


    public ContactAdapter(Context context, List<ParseUser> user){
        mContext = context;
        mUser = user;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_contact, parent, false);

        return new ViewHoldertwo(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ParseUser user = mUser.get(position);
        ViewHoldertwo ViewHoldertwo = (ViewHoldertwo) holder;
        ViewHoldertwo.tvUsername.setText(user.getUsername());
        ViewHoldertwo.tvNativeLang.setText(langNameFromCode((String) user.get("nativelang")));

        new Thread(() -> {
            try {
                URL imageUrl = new URL(user.getString(Util.PROFILE_IMG));
                Bitmap bmp = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                if (bmp != null) {
                    ((MainActivity) mContext).runOnUiThread(() -> ViewHoldertwo.ivProfile.setImageBitmap(bmp));
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }).start();

        ViewHoldertwo.btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageFragment mf = new MessageFragment();
                Bundle arguments = new Bundle();
                arguments.putString("OID", user.getObjectId());
                arguments.putString("name",user.getUsername());
                arguments.putString("lang",(String) user.get("nativelang"));
                mf.setArguments(arguments);
                FragmentTransaction fragmentTransaction = ((MainActivity) mContext).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flContainer, mf);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHoldertwo extends RecyclerView.ViewHolder {
        TextView tvUsername;
        TextView tvNativeLang;
        ImageView ivProfile;
        ImageButton btnMessage;

        public ViewHoldertwo(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvNativeLang = itemView.findViewById(R.id.tvNativeLang);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            btnMessage = itemView.findViewById(R.id.btnMessage);
        }
    }
}