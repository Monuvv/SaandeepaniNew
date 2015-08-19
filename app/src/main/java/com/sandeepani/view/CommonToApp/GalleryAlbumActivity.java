package com.sandeepani.view.CommonToApp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sandeepani.adapters.GalleryGridAdapter;
import com.sandeepani.model.GalleryDTO;
import com.sandeepani.model.GalleryItemModel;
import com.sandeepani.utils.TopBar;
import com.sandeepani.view.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class GalleryAlbumActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TopBar topBar;
    private GridView gridView;
    private GalleryGridAdapter customGridAdapter;
    //Number of columns of Grid View
    private static final int NUM_OF_COLUMNS = 3;
    // Gridview image padding
    private static final int GRID_PADDING = 3; // in dp
    private int columnWidth;
    private GalleryDTO galleryDTO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_album);
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setVisibility(View.GONE);
        topBar.titleTV.setText(getString(R.string.gallery));
      //  topBar.logoutIV.setOnClickListener(this);
        gridView = (GridView) findViewById(R.id.gridView);
        ImageView backArrowIV = (ImageView) findViewById(R.id.go_back_arrow_iv);
        TextView albumNameTV = (TextView) findViewById(R.id.album_name_tv);
        TextView photosDateTV = (TextView) findViewById(R.id.photos_date_tv);
        InitilizeGridLayout();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        Bundle bundle = getIntent().getExtras();
        //getting data which is sending form gallery home screen
        if (bundle != null) {
            galleryDTO = (GalleryDTO) bundle.getSerializable(getString(R.string.album_data));
            albumNameTV.setText(galleryDTO.getFileName());
            photosDateTV.setText(galleryDTO.getFilesCount() + " Photos, Posted " + galleryDTO.getPostedDate());
            customGridAdapter = new GalleryGridAdapter(this, R.layout.album_grid_item, galleryDTO.getGalleryItemsList(), columnWidth);
            gridView.setAdapter(customGridAdapter);
            gridView.setOnItemClickListener(this);
        }

        //regidter with listeners
        backArrowIV.setOnClickListener(this);
    }

    /**
     * By this method Initializing Gridview properties like number columns, padding, width etc
     */
    private void InitilizeGridLayout() {
        Resources resources = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, GRID_PADDING, resources.getDisplayMetrics());
        columnWidth = (int) ((getScreenWidth() - ((NUM_OF_COLUMNS + 1) * padding)) / NUM_OF_COLUMNS);
        gridView.setNumColumns(NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding, (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }

    /**
     * By this method getting Screen width
     */
    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ImageView iv = (ImageView) view.findViewById(R.id.image_grid_item_iv);
        GalleryItemModel galleryItemModel = (GalleryItemModel) iv.getTag();
        Intent intent = new Intent(this, GalleryFullImageActivity.class);
        intent.putExtra(getString(R.string.album_list_data), galleryDTO);
        intent.putExtra(getString(R.string.position), position);
        startActivity(intent);
        intent = null;

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.go_back_arrow_iv:
                onBackPressed();
                break;
            case R.id.logoutIV:
                SharedPreferences clearSharedPreferenceForLogout;
                clearSharedPreferenceForLogout = getSharedPreferences("MyChild_Preferences", 0);
                SharedPreferences.Editor editor = clearSharedPreferenceForLogout.edit();
                editor.clear();
                editor.commit();
                finish();
                startActivity(new Intent(this, LoginActivity.class));

            default:
                break;

        }
    }
}
