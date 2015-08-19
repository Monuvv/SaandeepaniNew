package com.sandeepani.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
 * Created by sandeep on 5/15/2015.
 */
public class GalleryGridAdapter extends ArrayAdapter<GalleryItemModel> {
    private Context context;
    private int resource;
    ArrayList<GalleryItemModel> list;
    private DisplayImageOptions options;
    private LayoutInflater inflater;
    private int imageWidth;

    public GalleryGridAdapter(Context context, int resource, ArrayList<GalleryItemModel> list, int imageWidth) {
        super(context, resource, list);
        this.resource = resource;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
        options = CommonUtils.returnDisplayOptions();
        this.imageWidth = imageWidth;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image_grid_item_iv);
            holder.pBar = (ProgressBar) convertView.findViewById(R.id.pBar);
            holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(imageWidth, imageWidth);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            holder.image.setLayoutParams(layoutParams);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            GalleryItemModel galleryItemModel = getItem(position);
            holder.image.setTag(galleryItemModel);
            ImageLoadingListener imageLoader = new ImageLoadingListener(holder.pBar);
            ImageLoader.getInstance().displayImage(galleryItemModel.getFilePath(), holder.image, options, imageLoader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView image;
        private ProgressBar pBar;
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }
}
