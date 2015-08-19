package com.sandeepani.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sandeepani.model.GalleryDTO;
import com.sandeepani.utils.CommonUtils;
import com.sandeepani.utils.ImageLoadingListener;
import com.sandeepani.view.CommonToApp.GalleryAlbumActivity;
import com.sandeepani.view.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by sandeep on 5/15/2015.
 */
public class GalleryListAdapter extends ArrayAdapter<GalleryDTO> implements View.OnClickListener {
    private LayoutInflater inflater;
    private int resource;
    private List<GalleryDTO> list;
    private DisplayImageOptions options;
    private Context context;

    public GalleryListAdapter(Context context, int resource, List<GalleryDTO> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
        this.list = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        options = CommonUtils.returnDisplayOptions();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
            holder = new ViewHolder();
            holder.coverPicIV = (ImageView) convertView.findViewById(R.id.coverpic_iv);
            holder.pBar = (ProgressBar) convertView.findViewById(R.id.pBar);
            holder.galleryNameTV = (TextView) convertView.findViewById(R.id.gallery_name_tv);
            holder.photosTV = (TextView) convertView.findViewById(R.id.photos_tv);
            holder.viewAlbumTV = (TextView) convertView.findViewById(R.id.view_album_tv);
            holder.viewAlbumTV.setOnClickListener(this);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GalleryDTO dto = getItem(position);
        ImageLoadingListener imageLoader = new ImageLoadingListener(holder.pBar);
        holder.galleryNameTV.setText(dto.getFileName());
        holder.photosTV.setText(dto.getFilesCount() + " Photos | Posted " + dto.getPostedDate());
        holder.viewAlbumTV.setTag(dto);
        ImageLoader.getInstance().displayImage(getItem(position).getCoverPicUrl(), holder.coverPicIV, options, imageLoader);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, GalleryAlbumActivity.class);
        intent.putExtra(context.getString(R.string.album_data), (GalleryDTO) v.getTag());
        context.startActivity(intent);

    }

    private class ViewHolder {
        private TextView galleryNameTV, photosTV, viewAlbumTV;
        private ImageView coverPicIV;
        private ProgressBar pBar;
    }
}
