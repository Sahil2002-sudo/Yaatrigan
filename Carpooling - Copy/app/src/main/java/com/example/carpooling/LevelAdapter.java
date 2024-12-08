package com.example.carpooling;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LevelAdapter extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] itemname;
    private final String[] desc;
    private final String[] emails;
    public LevelAdapter(Activity context, String[] itemname, String[] desc, String emails[]) {
        super(context, R.layout.levellist, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.desc=desc;
        this.emails=emails;
    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.levellist, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.name);
        TextView extratxt = (TextView) rowView.findViewById(R.id.contact);
        TextView price = (TextView) rowView.findViewById(R.id.address);
        txtTitle.setText(itemname[position]);
        extratxt.setText(desc[position]);
        price.setText(emails[position]);

        return rowView;

    };

}
