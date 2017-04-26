package com.ichessprogrammer.chesseducate;

/**
 * Created by 남지니 on 2016-07-24.
 */
public class ItemListData {;
    String mItemName;    //아이템 이름 저장
    String mItemGroup;   //아이템 분류 저장
    int mImgId;      // 아이템 이미지 저장

    public ItemListData(String itemname, String itemgroup, int imgId) {
        // TODO Auto-generated constructor stub
        //생성자함수로 전달받은 Member의 정보를 멤버변수에 저장..
        mItemName=itemname;
        mItemGroup=itemgroup;
        mImgId=imgId;
    }
    public void setItemName(String name) {
        mItemName= name;
    }
    public void setmItemGroup(String group) {
        mItemGroup=group;
    }
    public void setImgId(int imgId) {
        mImgId = imgId;
    }
    public String getItemName() {
        return mItemName;
    }
    public String getmItemGroup() {
        return mItemGroup;
    }
    public int getImgId() {
        return mImgId;
    }

}
