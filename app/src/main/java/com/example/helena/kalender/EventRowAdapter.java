package com.example.helena.kalender;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class EventRowAdapter extends SimpleCursorAdapter {

    private LayoutInflater mInflater;
    String[] mData;
    Cursor mCursor;
    Context mContext;

    public EventRowAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mData = from;
        mCursor = c;
        mContext = context;

    }

    public static class ViewHolder {
        public ImageButton delete_button;
        public ImageButton info_button;
        public TextView event_item_name;
        public TextView event_item_time;
        public TextView event_item_location;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.customlayout, null);
            holder = new ViewHolder();
            holder.info_button = convertView.findViewById(R.id.event_item_info);
            holder.delete_button = convertView.findViewById(R.id.event_item_delete);
            holder.event_item_name = convertView.findViewById(R.id.event_item_name);
            holder.event_item_time = convertView.findViewById(R.id.event_item_time);
            holder.event_item_location = convertView.findViewById(R.id.event_item_location);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.event_item_name.setText(mCursor.getString(1));
        holder.event_item_time.setText(mCursor.getString(4));
        holder.event_item_location.setText(mCursor.getString(2));

        final ImageButton delete_button = holder.delete_button.findViewById(R.id.event_item_delete);
        delete_button.setTag(Integer.parseInt(mCursor.getString(0)));
        delete_button.setOnClickListener(new View.OnClickListener() {
            int btnId = (Integer)delete_button.getTag();
            @Override
            public void onClick(View v) {
                Log.v("KALENDER", "DELETE EVENT");
                if (mContext instanceof MainActivity){
                    ((MainActivity)mContext).deleteEvent(btnId);
                }
            }
        });

        ImageButton info_button = holder.info_button.findViewById(R.id.event_item_info);
        info_button.setTag(Integer.parseInt(mCursor.getString(0)));
        info_button.setOnClickListener(new View.OnClickListener() {
            int btnId = (Integer)delete_button.getTag();
            @Override
            public void onClick(View view) {
                Log.v("KALENDER", "INFO EVENT");
                if (mContext instanceof MainActivity){
                    ((MainActivity)mContext).viewEvent(btnId);
                }

            }
        });
        return convertView;

    }

}
