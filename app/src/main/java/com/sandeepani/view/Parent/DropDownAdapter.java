package com.sandeepani.view.Parent;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.sandeepani.view.R;

import java.util.ArrayList;
import java.util.List;

public class DropDownAdapter extends ArrayAdapter<String> {
	private Typeface typefaceNormal;
	public static ArrayList<String> temporarylist;
	
	public DropDownAdapter(Context context, int resource, int textViewResourceId,
						   List<String> objects) {
		super(context, resource, textViewResourceId, objects);
		/*typefaceNormal = Typeface.createFromAsset(context.getAssets(),
				fontPath2);*/
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		TextView textname = (TextView) view.findViewById(R.id.searchdropdown);
		textname.setTypeface(typefaceNormal);
		textname.setTextColor(Color.parseColor("#9f8e66"));
		if (position % 2 == 1) {
			view.setBackgroundColor(Color.parseColor("#efe8c9"));
		} else {
			view.setBackgroundColor(Color.parseColor("#fffff1"));
		}
		
		return view;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return super.getFilter();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return super.getCount();
	}
}
