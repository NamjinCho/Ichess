package com.ichessprogrammer.chesseducate;
//클래스의 이름은 다른 이름이어도 상관없지만 상속만 BaseAdapter를 잘 받으면 됩니다.

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemListAdapter extends BaseAdapter {
    public ArrayList<ItemListData> datas;
    LayoutInflater inflater;
    public ItemListAdapter(LayoutInflater inflater, ArrayList<ItemListData> datas) {
        // TODO Auto-generated constructor stub
        //객체를 생성하면서 전달받은 datas(MemberData 객체배열)를 멤버변수로 전달
        //아래의 다른 멤버 메소드에서 사용하기 위해서.멤버변수로 참조값 전달
        this.datas= datas;
        this.inflater= inflater;
    }
    public void datachange()
    {
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return datas.size(); //datas의 개수를 리턴
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return datas.get(position);//datas의 특정 인덱스 위치 객체 리턴.
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if( convertView==null){
            convertView= inflater.inflate(R.layout.list_items, null);
        }
        TextView text_item_category= (TextView)convertView.findViewById(R.id.text_item_category);
        TextView text_item_name= (TextView)convertView.findViewById(R.id.text_item_name);
        ImageView img_flag= (ImageView)convertView.findViewById(R.id.img_flag);
        //현재 position( getView()메소드의 첫번재 파라미터 )번째의 Data를 위 해당 View들에 연결..
        text_item_name.setText( datas.get(position).getItemName() );
        text_item_category.setText( datas.get(position).getmItemGroup() );
        img_flag.setImageResource( datas.get(position).getImgId() );
        return convertView;
    }
}
