package com.xbpsolutions.appsadviseradmin;

import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Arrays;
import java.util.Date;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {


    FirebaseUser user;
    private RecyclerView recyclerViewChat;
    private FirebaseRecyclerAdapter<ChatItem, MessageViewHolder>
            mFirebaseAdapter;

    private DatabaseReference mFirebaseDatabaseReference;
    private LinearLayoutManager mLinearLayoutManager;

    private EditText edMessage;
    private ImageView btnSend;
    private String uID;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.btnSend:

                processSend();
                break;
        }
    }

    private void processSend() {
        if (edMessage.getText().toString() != null) {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRefChat = database.getReference("chats");
            ChatItem item = new ChatItem();
            item.date = new Date().toString();
            item.message = edMessage.getText().toString();
            item.sender = "Adviser";
            item.senderId = user.getUid();
            item.senderImage = "";
            myRefChat.child("" + uID).push().setValue(item);
            edMessage.setText("");
        }
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        public CircularImageView imgChatIcon;
        public TextView txtChatText;


        public MessageViewHolder(View v) {
            super(v);
            imgChatIcon = (CircularImageView) itemView.findViewById(R.id.imgChatIcon);
            txtChatText = (TextView) itemView.findViewById(R.id.txtChatText);

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

       user= FirebaseAuth.getInstance().getCurrentUser();

        recyclerViewChat = (RecyclerView) findViewById(R.id.recyclerChat);
        mLinearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mLinearLayoutManager.setStackFromEnd(true);
        recyclerViewChat.setLayoutManager(mLinearLayoutManager);
        recyclerViewChat.addItemDecoration(new VerticalSpaceItemDecoration(4));

        uID = getIntent().getExtras().getString("uid");


        btnSend = (ImageView) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        edMessage = (EditText) findViewById(R.id.edMessage);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference("chats");

        setupFirebase();

    }

    private void setupFirebase() {

        mFirebaseAdapter = new FirebaseRecyclerAdapter<ChatItem,
                MessageViewHolder>(
                ChatItem.class,
                R.layout.item_chat,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.child(uID)) {

            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder,
                                              ChatItem friendlyMessage, int position) {

                viewHolder.txtChatText.setText(friendlyMessage.message);
                if (friendlyMessage.senderImage != null && !friendlyMessage.senderImage.isEmpty()) {
                    Glide.with(ChatActivity.this).load(Uri.parse(friendlyMessage.senderImage)).asBitmap().into(viewHolder.imgChatIcon);
                }


            }

        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerViewChat.scrollToPosition(positionStart);
                }
            }

        });
        recyclerViewChat.setLayoutManager(mLinearLayoutManager);
        recyclerViewChat.setAdapter(mFirebaseAdapter);
    }

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mVerticalSpaceHeight;

        public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
            this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = mVerticalSpaceHeight;
        }
    }
}
