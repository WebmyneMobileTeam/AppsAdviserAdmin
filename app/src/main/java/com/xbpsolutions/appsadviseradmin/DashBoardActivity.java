package com.xbpsolutions.appsadviseradmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;

public class DashBoardActivity extends AppCompatActivity {

    FirebaseUser user;
    private Toolbar toolbar;
    private DatabaseReference mFirebaseDatabaseReference;
    private RecyclerView recyclerUser;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter<AppUser, UserViewHolder>
            mFirebaseAdapter;

    private ProgressDialog pd;

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        public CircularImageView imgUserIcon;
        public TextView txtUserText;
        public LinearLayout parent;


        public UserViewHolder(View v) {
            super(v);
            imgUserIcon = (CircularImageView) itemView.findViewById(R.id.imgUserIcon);
            txtUserText = (TextView) itemView.findViewById(R.id.txtUserText);
            parent = (LinearLayout) itemView.findViewById(R.id.parentUser);

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        user = FirebaseAuth.getInstance().getCurrentUser();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(user.getEmail());
        setSupportActionBar(toolbar);

        recyclerUser = (RecyclerView) findViewById(R.id.recyclerUser);
        mLinearLayoutManager = new LinearLayoutManager(DashBoardActivity.this);
        mLinearLayoutManager.setStackFromEnd(false);
        recyclerUser.setLayoutManager(mLinearLayoutManager);
        recyclerUser.addItemDecoration(new VerticalSpaceItemDecoration(4));
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();


        pd = ProgressDialog.show(this, "Please Wait", "Fetching Users");

        setupFirebase();

    }

    private void setupFirebase() {

        mFirebaseAdapter = new FirebaseRecyclerAdapter<AppUser,
                UserViewHolder>(
                AppUser.class,
                R.layout.item_user,
                UserViewHolder.class,
                mFirebaseDatabaseReference.child("users")) {

            @Override
            protected void populateViewHolder(UserViewHolder viewHolder,
                                              final AppUser friendlyMessage, int position) {

                if (pd.isShowing())
                    pd.dismiss();

                viewHolder.txtUserText.setText(friendlyMessage.name);
                Glide.with(DashBoardActivity.this).load(Uri.parse(friendlyMessage.image)).asBitmap().into(viewHolder.imgUserIcon);

                viewHolder.parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent iChat = new Intent(DashBoardActivity.this, ChatActivity.class);
                        iChat.putExtra("uid", friendlyMessage.uid);
                        startActivity(iChat);
                    }
                });

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
                    recyclerUser.scrollToPosition(positionStart);
                }
            }

        });
        recyclerUser.setLayoutManager(mLinearLayoutManager);
        recyclerUser.setAdapter(mFirebaseAdapter);


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
