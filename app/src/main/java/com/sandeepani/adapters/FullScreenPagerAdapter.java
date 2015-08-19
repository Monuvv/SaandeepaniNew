package com.sandeepani.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.sandeepani.model.GalleryItemModel;
import com.sandeepani.utils.CommonUtils;
import com.sandeepani.utils.ImageLoadingListener;
import com.sandeepani.view.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by sandeep on 5/16/2015.
 */
public class FullScreenPagerAdapter extends PagerAdapter {
    private Activity _activity;
    private LayoutInflater inflater;
    private ArrayList<GalleryItemModel> images;
    private DisplayImageOptions options;

    // constructor
    public FullScreenPagerAdapter(Activity activity, ArrayList<GalleryItemModel> images) {
        this._activity = activity;
        this.images = images;
        options = CommonUtils.returnDisplayOptions();

    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView viewPagerIV;

        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.gallery_viewpager_item, container,
                false);
        GalleryItemModel galleryItemModel = images.get(position);
        viewPagerIV = (ImageView) viewLayout.findViewById(R.id.view_pager_iv);
        ProgressBar pBar = (ProgressBar) viewLayout.findViewById(R.id.pBar);
        ImageLoadingListener imageLoader = new ImageLoadingListener(pBar);
        ImageLoader.getInstance().displayImage(galleryItemModel.getFilePath(), viewPagerIV, options, imageLoader);
        ((ViewPager) container).addView(viewLayout);
        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}
