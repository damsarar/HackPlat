package com.industrialmaster.hackplat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class EventListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Event> DataList;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public EventListAdapter(Activity activity, List<Event> dataitem)
    {
        this.activity = activity;
        this.DataList = dataitem;
    }

    @Override
    public int getCount() {
        return DataList.size();
    }

    @Override
    public Object getItem(int position) {
        return DataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.activity_event_view, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView image = (NetworkImageView) convertView.findViewById(R.id.eventPic);
        TextView name = (TextView) convertView.findViewById(R.id.evenName);
        TextView venue = (TextView) convertView.findViewById(R.id.eventVenue);
        TextView organizer = (TextView) convertView.findViewById(R.id.eventOrganizer);
        TextView date_at = (TextView) convertView.findViewById(R.id.eventDate);
        TextView created_at = (TextView) convertView.findViewById(R.id.eventCreatedDate);

        Event m = DataList.get(position);

        image.setImageUrl(m.getImgURL(), imageLoader);
        name.setText(m.getName());
        venue.setText(m.getVenue());
        organizer.setText(m.getOrganizer());
        date_at.setText(m.getDate_at());
        created_at.setText(m.getDate_created());

        return convertView;

    }
}
