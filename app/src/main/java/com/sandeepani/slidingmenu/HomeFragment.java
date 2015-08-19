package com.sandeepani.slidingmenu;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sandeepani.view.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
	
	public HomeFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
         
        return rootView;
    }

	public static class NavDrawerItem
    {
        private String title;
        private int icon;
        private String count = "0";
        // boolean to set visiblity of the counter
        private boolean isCounterVisible = false;
        public NavDrawerItem()
        {

        }
        public NavDrawerItem(String title, int icon)
        {
            this.title = title;
            this.icon = icon;
        }

        public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count)
        {
            this.title = title;
            this.icon = icon;
            this.isCounterVisible = isCounterVisible;
            this.count = count;
        }

        public String getTitle()
        {
            return this.title;
        }

        public int getIcon()
        {
            return this.icon;
        }

        public String getCount()
        {
            return this.count;
        }

        public boolean getCounterVisibility()
        {
            return this.isCounterVisible;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public void setIcon(int icon)
        {
            this.icon = icon;
        }

        public void setCount(String count)
        {
            this.count = count;
        }

        public void setCounterVisibility(boolean isCounterVisible)
        {
            this.isCounterVisible = isCounterVisible;
        }
    }

	public static class NavDrawerListAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<NavDrawerItem> navDrawerItems;

        public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
            this.context = context;
            this.navDrawerItems = navDrawerItems;
        }

        @Override
        public int getCount() {
            return navDrawerItems.size();
        }

        @Override
        public Object getItem(int position) {
            return navDrawerItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater)
                        context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.drawer_list_item, null);
            }

            ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
            TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

            imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
            txtTitle.setText(navDrawerItems.get(position).getTitle());

            // displaying count
            // check whether it set visible or not
            if(navDrawerItems.get(position).getCounterVisibility()){
                txtCount.setText(navDrawerItems.get(position).getCount());
            }else{
                // hide the counter view
                txtCount.setVisibility(View.GONE);
            }

            return convertView;
        }

    }
}
