package com.ichessprogrammer.chesseducate.PGN;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ichessprogrammer.chesseducate.R;

import java.util.ArrayList;

/**
 * Created by XNOTE on 2016-08-10.
 */
public class PgnAdapter extends BaseAdapter {

    private ArrayList<ListViewItem> listViewItemArray = new ArrayList<ListViewItem>();

    public PgnAdapter() {
    }
    @Override
    public int getCount() {
        return  listViewItemArray.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater layoutInflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.listview_layout, parent, false);
        }

        ListViewItem listViewItem = listViewItemArray.get(position);


        TextView whiteP = (TextView)convertView.findViewById(R.id.white_player);
        TextView blackP = (TextView)convertView.findViewById(R.id.black_player);
        TextView date = (TextView)convertView.findViewById(R.id.date);
        TextView result = (TextView)convertView.findViewById(R.id.result);

        whiteP.setText(listViewItem.getWhitePlayer());
        blackP.setText(listViewItem.getBlackPlayer());
        date.setText(listViewItem.getDate());
        result.setText(listViewItem.getResult());


        return convertView;
    }

    public void addItem(String w, String b, String d, String r) {

        ListViewItem item = new ListViewItem();
        item.setWhitePlayer(w);
        item.setBlackPlayer(b);
        item.setDate(d);
        item.setResult(r);
        listViewItemArray.add(item);
    }

}
