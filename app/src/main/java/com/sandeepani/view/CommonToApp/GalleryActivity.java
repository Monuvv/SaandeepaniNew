package com.sandeepani.view.CommonToApp;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.sandeepani.adapters.GalleryListAdapter;
import com.sandeepani.interfaces.AsyncTaskInterface;
import com.sandeepani.model.GalleryDTO;
import com.sandeepani.model.GalleryItemModel;
import com.sandeepani.threads.HttpConnectThread;
import com.sandeepani.utils.CommonUtils;
import com.sandeepani.utils.TopBar1;
import com.sandeepani.view.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GalleryActivity extends BaseActivity implements View.OnClickListener, AsyncTaskInterface {

    private TopBar1 topBar;
    private ListView gallertLV;
    private ArrayList<GalleryDTO> galleryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        topBar = (TopBar1) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.titleTV.setText(getString(R.string.gallery));
        topBar.backArrowIV.setOnClickListener(this);
       // topBar.logoutIV.setOnClickListener(this);
        gallertLV = (ListView) findViewById(R.id.gallery_lv);
       // topBar.logoutIV.setVisibility(View.GONE);
        if (CommonUtils.isNetworkAvailable(this)) {
            httpConnectThread = new HttpConnectThread(this, null, this);
            httpConnectThread.execute(getString(R.string.base_url)+getString(R.string.gallery_url));
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
    }

    @Override
    public void setAsyncTaskCompletionListener(String object) {
        if (object != null && !object.equals("")) {
            try {
                galleryList = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(object);
                int size = jsonArray.length();
                for (int i = 0; i < size; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    GalleryDTO galleryDTO = new GalleryDTO();
                    galleryDTO.setFileType(jsonObject.optString("fileType"));
                    galleryDTO.setFileName(jsonObject.optString("fileName"));
                    galleryDTO.setCoverPicUrl(getString(R.string.base_url)+jsonObject.optString("coverpicUrl"));
                    galleryDTO.setFilesCount(Integer.parseInt(jsonObject.optString("filecount")));
                    galleryDTO.setPostedDate(jsonObject.optString("postedDate"));
                    JSONArray filesArray = jsonObject.optJSONArray("files");
                    if (filesArray != null) {
                        ArrayList<GalleryItemModel> galleryItemsList = new ArrayList<GalleryItemModel>();
                        int filesSize = filesArray.length();
                        for (int j = 0; j < filesSize; j++) {

                            JSONObject innerObj = filesArray.getJSONObject(j);
                            JSONObject fileObj = innerObj.optJSONObject("file");
                            if (fileObj != null) {
                                GalleryItemModel galleryItemModel = new GalleryItemModel();
                                galleryItemModel.setFileName(fileObj.optString("fileName"));
                                galleryItemModel.setFilePath(getString(R.string.base_url)+fileObj.optString("filePath"));
                                galleryItemModel.setDescription(fileObj.optString("description"));
                                galleryItemsList.add(galleryItemModel);
                                galleryItemModel = null;
                                fileObj = null;
                            }
                        }
                        galleryDTO.setGalleryItemList(galleryItemsList);
                        galleryList.add(galleryDTO);
                        galleryDTO = null;
                    }
                }
                //setting lstview adapter
                GalleryListAdapter adapter = new GalleryListAdapter(this, R.layout.gallery_list_item, galleryList);
                gallertLV.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back_arrow_iv:
                onBackPressed();
                break;
//            case R.id.logoutIV:
//                SharedPreferences clearSharedPreferenceForLogout;
//                clearSharedPreferenceForLogout = getSharedPreferences("MyChild_Preferences", 0);
//                SharedPreferences.Editor editor = clearSharedPreferenceForLogout.edit();
//                editor.clear();
//                editor.commit();
//                finish();
//                startActivity(new Intent(this, LoginActivity.class));

            default:
                break;
        }
    }
}
