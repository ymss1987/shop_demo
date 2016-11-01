package com.ymss.adapter;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ymss.tinyshop.MainActivity;
import com.ymss.tinyshop.R;

public class GridViewAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Map<String, Object>> numberList;
    private int selectedPosition=0;
	private GridViewClickListener listener;

	public interface GridViewClickListener{
		public void onItemClick(View view, int position);
	}

	public GridViewAdapter(Context context, List<Map<String, Object>> numberList, GridViewClickListener listener) {
		inflater = LayoutInflater.from(context);
		this.numberList = numberList;
		this.listener = listener;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return numberList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return numberList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public void clearSelection(int position) {
		selectedPosition = position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.gridview_item, null);
			holder.imageview = (ImageView) convertView.findViewById(R.id.image);
			holder.textview = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();

		holder.textview.setText(numberList.get(position).get("text").toString());
		holder.imageview.setBackgroundResource(Integer.valueOf(numberList.get(position).get("image").toString()));
		holder.position = position;
		convertView.setOnTouchListener(onTouchListener);
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ViewHolder holder =(ViewHolder) v.getTag();
				//Toast.makeText(v.getContext(),"点击了"+holder.position,Toast.LENGTH_SHORT).show();
				if (holder.position > 3){
					return;
				}
				listener.onItemClick(v,holder.position);
			}
		});

		/*if(selectedPosition==position){
			holder.textview.setTextColor(Color.parseColor("#ff6501"));
				}else{
			holder.textview.setTextColor(Color.parseColor("#404040"));
		}*/
		return convertView;
	}

	 public class ViewHolder {
		 public ImageView imageview;
		public  TextView textview;
		 int position;
	}

	public View.OnTouchListener onTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			ViewHolder holder =(ViewHolder) view.getTag();
			if (holder.position > 3){
				return false;
			}
			switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					changeLight((ImageView) holder.imageview, 0);
					holder.textview.setTextColor(Color.parseColor("#000000"));
					// onclick
					break;
				case MotionEvent.ACTION_DOWN:
					changeLight((ImageView) holder.imageview, -80);
					holder.textview.setTextColor(Color.parseColor("#ff6501"));
					break;
				case MotionEvent.ACTION_MOVE:
					// changeLight(view, 0);
					break;
				case MotionEvent.ACTION_CANCEL:
					changeLight((ImageView) holder.imageview, 0);
					holder.textview.setTextColor(Color.parseColor("#000000"));
					break;
				default:
					break;
			}
			return false;
		}

	};

	public final float[] BG_PRESSED = new float[] { 1, 0, 0, 0, -50, 0, 1, 0, 0, -50, 0, 0, 1, 0, -50, 0, 0, 0, 1, 0 };
	public final float[] BG_NOT_PRESSED = new float[] { 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0 };

	private void changeLight(ImageView imageview, int brightness) {
		ColorMatrix matrix = new ColorMatrix();
		/*if (brightness == 0){
			imageview.setColorFilter( new ColorMatrixColorFilter(BG_NOT_PRESSED) ) ;
			imageview.getBackground().setColorFilter(new ColorMatrixColorFilter(BG_NOT_PRESSED));
		}else {
			imageview.setDrawingCacheEnabled(true);
			imageview.setColorFilter(new ColorMatrixColorFilter(BG_PRESSED));
			imageview.getBackground().setColorFilter(new ColorMatrixColorFilter(BG_PRESSED));
		}*/
		matrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness, 0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });
		imageview.setColorFilter(new ColorMatrixColorFilter(matrix));
		imageview.getBackground().setColorFilter(new ColorMatrixColorFilter(matrix));

	}

}
