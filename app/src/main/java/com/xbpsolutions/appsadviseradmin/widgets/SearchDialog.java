package com.xbpsolutions.appsadviseradmin.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;
import com.xbpsolutions.appsadviseradmin.R;
import com.xbpsolutions.appsadviseradmin.Utility.CallWebService;
import com.xbpsolutions.appsadviseradmin.Utility.Constants;
import com.xbpsolutions.appsadviseradmin.apps.AppItemRecyclerViewAdapter;
import com.xbpsolutions.appsadviseradmin.apps.ResponseApps;

import java.net.URL;

/**
 * Created by dhruvil on 06-06-2016.
 */
public class SearchDialog extends AppCompatDialog implements View.OnClickListener {

    private Context mContext;
    private EditText edSearch;
    private ImageView btnSearch;
    private ImageView btnBack;
    private Button btnSuggest;
    private RecyclerView listSearchApps;


    public SearchDialog(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public SearchDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        init();
    }

    private void init() {
        setContentView(R.layout.item_search_dialog);
        edSearch = (EditText) this.findViewById(R.id.edSearch);
        btnSearch = (ImageView) this.findViewById(R.id.btnSearch);
        btnBack = (ImageView) this.findViewById(R.id.btnBackDialog);
        btnSuggest = (Button) this.findViewById(R.id.btnSuggestDialog);
        listSearchApps = (RecyclerView) this.findViewById(R.id.listSearchApps);
        listSearchApps.setLayoutManager(new GridLayoutManager(mContext, 2));
        listSearchApps.addItemDecoration(new SpacesItemDecoration(2));
        btnSearch.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnSuggest.setOnClickListener(this);

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                processTextChanged(s);
            }
        });

    }

    private void processTextChanged(Editable s) {
        String inputString = s.toString();
        if (inputString.length() > 0) {

            btnSearch.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        } else {
            btnSearch.setColorFilter(Color.parseColor("#d1d1d1"), PorterDuff.Mode.SRC_ATOP);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSearch:

                if (edSearch.getText().length() > 0) {
                    doSearch();
                }

                break;
        }

    }


    private void doSearch() {

        String url = Constants.SEARCH_URL + "?q=" + edSearch.getText().toString().trim() + "&key=" + Constants.KEY;

        new CallWebService(url, CallWebService.TYPE_JSONOBJECT) {
            @Override
            public void response(String response) {

                ResponseApps beanCustomerInfo = new GsonBuilder().create().fromJson(response, ResponseApps.class);

                if (beanCustomerInfo.apps != null && beanCustomerInfo.apps.size() > 0) {
                    AppItemRecyclerViewAdapter appItemRecyclerViewAdapter = new AppItemRecyclerViewAdapter(mContext, beanCustomerInfo.apps);
                    listSearchApps.setAdapter(appItemRecyclerViewAdapter);
                }

            }

            @Override
            public void error(VolleyError error) {

            }
        }.call();

    }


    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = space;
            } else {
                outRect.top = 0;
            }
        }
    }
}
