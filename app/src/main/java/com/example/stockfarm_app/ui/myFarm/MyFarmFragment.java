package com.example.stockfarm_app.ui.myFarm;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.stockfarm_app.R;
import com.example.stockfarm_app.StockFarmApplication;
import com.example.stockfarm_app.VolleyApiKeyUrl;
import com.example.stockfarm_app.data.UserStockData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class MyFarmFragment extends Fragment {
    private StockFarmApplication app;
    FloatingActionButton floatingActionButton;
    private ViewPager2 farmPager;
    private ImageView farmReel;
    int pageNum;
    boolean refresh;
    boolean refreshFirst;
    ViewPagerAdapter viewPagerAdapter;
    LinkedList<UserStockData> activeStocks;
    VolleyApiKeyUrl volleyApiKeyUrl;
    RequestQueue queue;
    RelativeLayout openNote;
    Button buttonGotIt;
    View view;
    LinearLayout linearLayout;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_farm, container, false);
        app = (StockFarmApplication) getActivity().getApplication();
        farmReel = view.findViewById(R.id.farm_reel);
        farmPager = view.findViewById(R.id.farm_pager);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        buttonGotIt = view.findViewById(R.id.button_got_it);
        activeStocks = app.userData.getActiveStocks();
        viewPagerAdapter = new ViewPagerAdapter(getActivity(), activeStocks);
        farmPager.setAdapter(viewPagerAdapter);
        farmPager.registerOnPageChangeCallback(new PagerAnimationCallback());
        refreshFirst = true;
        volleyApiKeyUrl = new VolleyApiKeyUrl();
        queue = Volley.newRequestQueue(getContext());


        pageNum = app.userData.getActiveStocks().size() + 1; // TODO update this field during trade

        refresh = true;
        refreshDataFromServer();
        openNoteCheck();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (!refresh) {
                    refresh = true;
                    refreshDataFromServer();
                }
            }
        });

        buttonGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openNote.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.INVISIBLE);
            }
        });
        return view;
    }

    private void openNoteCheck() {

        if (app.sp.getBoolean(app.userData.getName()+"S", true)) {
            linearLayout = view.findViewById(R.id.backk);
            openNote = view.findViewById(R.id.open_note);
            linearLayout.setVisibility(View.VISIBLE);
            openNote.setVisibility(View.VISIBLE);
            app.sp.edit().putBoolean(app.userData.getName()+"S", false).apply();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //farmPager.setCurrentItem(0,true);
        for (Fragment fragment : viewPagerAdapter.mFragments)
        {
            if (fragment instanceof CropFragment && ((CropFragment) fragment).stockName != null)
            {   // the last condition is important because we can't update fragment which haven't yet been viewed
                ((CropFragment) fragment).setWindowText();
                ((CropFragment) fragment).updateTrees();
            } else if (fragment instanceof SignFragment && ((SignFragment) fragment).playerName != null) {
                ((SignFragment) fragment).getData();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        queue.cancelAll("All");
    }

    private void refreshDataFromServer() {
        String url = volleyApiKeyUrl.getCorrectUrlA();
        final String finalUrl = url;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < 30; i++){
                        JSONObject jsonObject = response.getJSONObject(i);
                        app.userData.getStocks().get(jsonObject.getString("symbol"))
                                .setLastPrice(Double.parseDouble(jsonObject.getString("price")));
                    }
                    if (refresh) {
//                        if (farmPager.getCurrentItem() == 0) {
//                            farmPager.setAdapter(viewPagerAdapter);
//                        } else {
//                            farmPager.setCurrentItem(0,true);
//                        }
                        for (Fragment fragment : viewPagerAdapter.mFragments) // SAGI NOTICE: example for how crop fragments are updated
                        {
                            if (fragment instanceof CropFragment && ((CropFragment) fragment).stockName != null)
                            {   // the last condition is important because we can't update fragment which haven't yet been viewed
                                ((CropFragment) fragment).setWindowText();
                                ((CropFragment) fragment).setTrees();
                            } else if (fragment instanceof SignFragment && ((SignFragment) fragment).playerName != null) {
                                ((SignFragment) fragment).getData();
                            }
                        }
                        if (refreshFirst) {
                            refreshFirst = false;
                        } else {
                            Toast toast = Toast.makeText(getContext(),"Refreshing", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (refresh) {
                        Toast.makeText(getContext(),"Refreshing Failed - check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
                refresh = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getContext(),"Refreshing prices failed", Toast.LENGTH_SHORT).show();
                refresh = false;
            }
        });
        jsonArrayRequest.setTag("All");
        queue.add(jsonArrayRequest);
    }

    protected int getReelMovementStep()
    {
        int reelWidth = farmReel.getMeasuredWidth();
        int screenWidth = this.getResources().getDisplayMetrics().widthPixels;
        return (int) (reelWidth - screenWidth) / (pageNum - 1);
    }

    // === Utility nested Classes: === //

    private class PagerAnimationCallback extends ViewPager2.OnPageChangeCallback
    {
        int prevPage;

        public PagerAnimationCallback()
        {
            super();
            prevPage = 0;
        }

        @Override
        public void onPageSelected(int position)
        {
            super.onPageSelected(position);
            if (app.autoTransition != null)
            {
                if (app.autoTransition.equals("sign"))
                    {if (position != 0) {
                        farmPager.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                farmPager.setCurrentItem(0, true);
                            }
                        }, 100);
                        }
                    }
                else
                {
                    for (int i = 1; i < viewPagerAdapter.mFragments.size(); i++)
                    {
                        CropFragment fragment = (CropFragment) viewPagerAdapter.mFragments.get(i);
                        if (fragment.getStock().getStockName().equals(app.autoTransition))
                        {
                            final int pos = i;
                            farmPager.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    farmPager.setCurrentItem(pos, true);
                                }
                            }, 100);
                            break;
                        }
                    }
                }
                app.autoTransition = null;
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            if (positionOffset == 0.0 && prevPage != position)
            {
                int changeInPixels = (prevPage - position) * getReelMovementStep();
                TranslateAnimation animation = new TranslateAnimation(0, changeInPixels, 0, 0);
                animation.setDuration(200);
                animation.setFillAfter(false);
                animation.setAnimationListener(new MyAnimationListener(changeInPixels));
                farmReel.startAnimation(animation);
                prevPage = position;
            }
        }
    }

    public class ViewPagerAdapter extends FragmentStateAdapter {

        private LinkedList<Fragment> mFragments;

        public ViewPagerAdapter(FragmentActivity fa, LinkedList<UserStockData> activeStocks){//Pager constructor receives Activity instance
            super(fa);
            mFragments = new LinkedList<>();
            mFragments.add(new SignFragment());
            for (UserStockData stock : activeStocks)
            {
                mFragments.add(new CropFragment(stock));
            }
        }

        @Override
        public int getItemCount() {
            return mFragments.size();//Number of fragments displayed
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return mFragments.get(position);
        }
    }

    private class MyAnimationListener implements Animation.AnimationListener {
        int movement;

        public MyAnimationListener(int movement)
        {
            super();
            this.movement = movement;
        }

        @Override
        public void onAnimationEnd(Animation animation)
        {
            farmReel.clearAnimation();
            farmReel.setX(farmReel.getX() + movement);
        }

        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationRepeat(Animation animation) {}


    }
}