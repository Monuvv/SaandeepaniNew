package com.sandeepani.view.CommonToApp;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sandeepani.adapters.FullScreenPagerAdapter;
import com.sandeepani.model.GalleryDTO;
import com.sandeepani.view.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class GalleryFullImageActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private FullScreenPagerAdapter libraryViewPagerAdapter;
    private int position;
    private GalleryDTO galleryDTO = null;
    private TextView photosCountTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_full_image);
        Bundle bundle = getIntent().getExtras();
        ImageView backArrowIV = (ImageView) findViewById(R.id.back_arrow_iv);
        TextView albumNameTV = (TextView) findViewById(R.id.album_name_tv);
        TextView photosDateTV = (TextView) findViewById(R.id.photos_date_tv);
        photosCountTV = (TextView) findViewById(R.id.photos_count_tv);
        if (bundle != null) {
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(this));
            galleryDTO = (GalleryDTO) bundle.getSerializable(getString(R.string.album_list_data));
            position = bundle.getInt(getString(R.string.position));
            viewPager = (ViewPager) findViewById(R.id.pager);
            setViewpagerAdapter();
            albumNameTV.setText(galleryDTO.getFileName());
            photosDateTV.setText(galleryDTO.getFilesCount() + " Photos, Posted " + galleryDTO.getPostedDate());
            int currentPosition = position + 1;
            photosCountTV.setText(currentPosition + "/" + galleryDTO.getFilesCount());
            viewPager.setOnPageChangeListener(this);
        }
        //register with listeners
        backArrowIV.setOnClickListener(this);
    }

    /**
     * This method is used for setting adapter for ViewPager
     */
    private void setViewpagerAdapter() {
        libraryViewPagerAdapter = new FullScreenPagerAdapter(this, galleryDTO.getGalleryItemsList());
        viewPager.setAdapter(libraryViewPagerAdapter);
        // displaying selected image first
        viewPager.setCurrentItem(position);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int position) {
        int currentPosition = position + 1;
        photosCountTV.setText(currentPosition + "/" + galleryDTO.getFilesCount());
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back_arrow_iv:
                onBackPressed();
                break;
            default:
                break;

        }
    }
}
