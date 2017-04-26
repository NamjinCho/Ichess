package com.ichessprogrammer.chesseducate.NormalBoard;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ichessprogrammer.chesseducate.BoardDB;
import com.ichessprogrammer.chesseducate.Diolog.Custom_Dialog;
import com.ichessprogrammer.chesseducate.MyInformation;
import com.ichessprogrammer.chesseducate.R;
import com.ichessprogrammer.chesseducate.Server.PHPConnector;

import java.util.ArrayList;

/**
 * Created by 남지니 on 2016-08-30.
 */
public class BoardList extends Activity {

    TextView titleView;
    ListView mListView;
    ArrayList<BoardData> mListData;
    ItemListAdapter adapter;
    String mResult;
    int mGlobalPoisition = 0;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_activity_board_list);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        titleView = (TextView) findViewById(R.id.title);
        mListView = (ListView) findViewById(R.id.listView);
        mResult = getIntent().getStringExtra("result");
        init(mResult);
    }

    public void editNormal(boolean flag) {
        final Custom_Dialog dialog2 = new Custom_Dialog(BoardList.this, R.style.AlertDialogCustom);
        dialog2.setContentView(R.layout.dialog_edit_normalboard);
        final EditText titleT = (EditText) dialog2.findViewById(R.id.editTitle);
        final EditText ContentsT = (EditText) dialog2.findViewById(R.id.editContents);
        final Button OK, Cancle;
        final boolean editFlag = flag;
        if (editFlag) {
            titleT.setText(mListData.get(mGlobalPoisition).Title);
            ContentsT.setText(mListData.get(mGlobalPoisition).Contents);
        }
        OK = (Button) dialog2.findViewById(R.id.buttonOK);
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleT.getText().toString();
                String Contents = ContentsT.getText().toString();
                if (title.length() > 3 && Contents.length() > 5) {
                    if (editFlag==false) {
                        new PHPConnector().ConnectServer("title=" + title + "&contents=" + Contents
                                        + "&producer=" + MyInformation.ID + "&table=normalBoard_" + BoardDB.Title.get(BoardDB.curPos),
                                "insertNormalBoard.php", "insert");
                        BoardData temp = new BoardData();
                        if (adapter.getCount() != 1) {
                            if (MyInformation.level < 999)
                                temp.ID = (Integer.parseInt(mListData.get(mListData.size() - 1).ID) + 1) + "";
                            else
                                temp.ID = (Integer.parseInt(mListData.get(mListData.size() - 2).ID) + 1) + "";
                        } else
                            temp.ID = "1";
                        if (MyInformation.level >= 50)
                            mListData.remove(mListData.size() - 1);
                        temp.Producer = MyInformation.ID;
                        temp.Title = title;
                        temp.Contents = Contents;
                        mListData.add(temp);
                        if (MyInformation.level >= 50) {
                            temp = new BoardData();
                            temp.Title = "게시글 올리기";
                            mListData.add(temp);
                        }
                        adapter.mdatas = mListData;
                        adapter.datachange();
                    } else {
                        mListData.get(mGlobalPoisition).Title = title;
                        mListData.get(mGlobalPoisition).Contents = Contents;
                        new PHPConnector().ConnectServer("title=" + title + "&contents=" + Contents
                                        + "&id=" + mListData.get(mGlobalPoisition).ID + "&table=normalBoard_" + BoardDB.Title.get(BoardDB.curPos),
                                "updateNormalBoard.php", "insert");
                        adapter.mdatas = mListData;
                        adapter.datachange();
                        dialog2.cancel();
                    }
                    dialog2.cancel();
                } else {
                    Toast.makeText(BoardList.this, "제목은 3글자 이상 , 내용은 5글자 이상 입력 해야합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Cancle = (Button) dialog2.findViewById(R.id.buttonCancle);
        Cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.cancel();
            }
        });
        dialog2.show();
    }

    public void showNormalBoard(int pos) {
        final Custom_Dialog dialog2 = new Custom_Dialog(BoardList.this, R.style.AlertDialogCustom);
        dialog2.setContentView(R.layout.dialog_view_normalboard);
        final TextView titleT = (TextView) dialog2.findViewById(R.id.editContents);
        final TextView ContentsT = (TextView) dialog2.findViewById(R.id.editContents);
        titleT.setText(mListData.get(pos).Title);
        ContentsT.setText(mListData.get(pos).Contents);

        dialog2.show();
    }

    public void showSelect(int pos) {
        final Custom_Dialog dialog2 = new Custom_Dialog(BoardList.this, R.style.AlertDialogCustom);
        dialog2.setContentView(R.layout.dialog_select_normal_menu);
        final Button buttonDel, buttonEdit;
        buttonDel = (Button) dialog2.findViewById(R.id.delete_button);
        buttonEdit = (Button) dialog2.findViewById(R.id.edit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BoardDB.Category.get(BoardDB.curPos).equals("동영상")) {
                    editVideo(true);
                } else {
                    editNormal(true);
                }
            }
        });
        buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoardData temp = mListData.get(mGlobalPoisition);
                if (BoardDB.Category.get(BoardDB.curPos).equals("게시판")) {
                    new PHPConnector().ConnectServer("id=" + temp.ID + "&table=normalBoard_" + BoardDB.Title.get(BoardDB.curPos), "deleteNormalBoard.php", "s");
                } else {
                    new PHPConnector().ConnectServer("id=" + temp.ID + "&table=videoBoard_" + BoardDB.Title.get(BoardDB.curPos), "deleteNormalBoard.php", "s");
                }
                mListData.remove(mGlobalPoisition);
                adapter.mdatas=mListData;
                adapter.datachange();
            }
        });
        dialog2.show();
    }

    public void editVideo(boolean flag) {
        final Custom_Dialog dialog2 = new Custom_Dialog(BoardList.this, R.style.AlertDialogCustom);
        dialog2.setContentView(R.layout.dialog_edit_videoboard);
        final EditText titleT = (EditText) dialog2.findViewById(R.id.editTitle);
        final EditText ContentsT = (EditText) dialog2.findViewById(R.id.editURL);
        final Button OK, Cancle;
        final boolean editFlag = flag;
        if (editFlag) {
            titleT.setText(mListData.get(mGlobalPoisition).Title);
            ContentsT.setText(mListData.get(mGlobalPoisition).Contents);
        }
        OK = (Button) dialog2.findViewById(R.id.buttonOK);
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleT.getText().toString();
                String Contents = ContentsT.getText().toString();
                if (title.length() > 3 && Contents.length() > 5) {
                    if (editFlag == false) {
                        new PHPConnector().ConnectServer("title=" + title + "&contents=" + Contents
                                        + "&producer=" + MyInformation.ID + "&table=videoBoard_" + BoardDB.Title.get(BoardDB.curPos),
                                "insertNormalBoard.php", "insert");
                        BoardData temp = new BoardData();
                        if (adapter.getCount() != 1) {
                            if (MyInformation.level < 999)
                                temp.ID = (Integer.parseInt(mListData.get(mListData.size() - 1).ID) + 1) + "";
                            else
                                temp.ID = (Integer.parseInt(mListData.get(mListData.size() - 2).ID) + 1) + "";
                        } else
                            temp.ID = "1";
                        if (MyInformation.level >= 50)
                            mListData.remove(mListData.size() - 1);
                        temp.Producer = MyInformation.ID;
                        temp.Title = title;
                        temp.Contents = Contents;
                        mListData.add(temp);
                        if (MyInformation.level >= 50) {
                            temp = new BoardData();
                            temp.Title = "동영상 올리기";
                            mListData.add(temp);
                        }
                        adapter.mdatas = mListData;
                        adapter.datachange();
                    } else {
                        mListData.get(mGlobalPoisition).Title = title;
                        mListData.get(mGlobalPoisition).Contents = Contents;
                        new PHPConnector().ConnectServer("title=" + title + "&contents=" + Contents
                                        + "&id=" + mListData.get(mGlobalPoisition).ID + "&table=videoBoard_" + BoardDB.Title.get(BoardDB.curPos),
                                "updateNormalBoard.php", "insert");

                        adapter.mdatas = mListData;
                        adapter.datachange();
                        dialog2.cancel();
                    }
                    dialog2.cancel();
                } else {
                    Toast.makeText(BoardList.this, "제목은 3글자 이상 , 내용은 5글자 이상 입력 해야합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Cancle = (Button) dialog2.findViewById(R.id.buttonCancle);
        Cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.cancel();
            }
        });
        dialog2.show();
    }

    private void init(String result) {
        mListData = new ArrayList<>();
        if (!result.equals("False")) {
            String[] sub = result.split("!@#");
            for (int i = 0; i < sub.length; i = i + 4) {
                BoardData temp = new BoardData();
                temp.ID = sub[i];
                temp.Title = sub[i + 1];
                temp.Producer = sub[i + 2];
                temp.Contents = sub[i + 3];
                mListData.add(temp);
            }
        }
        titleView.setText(BoardDB.Category.get(BoardDB.curPos));
        BoardData temp = new BoardData();
        temp.ID = "";
        if (titleView.getText().toString().equals("게시판"))
            temp.Title = "게시글 올리기";
        else
            temp.Title = "동영상 올리기";
        temp.Producer = "";
        temp.Contents = "";
        mListData.add(temp);
        adapter = new ItemListAdapter(getLayoutInflater(), mListData);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (MyInformation.level >= 50 && position == adapter.getCount() - 1) {
                    if (BoardDB.Category.get(BoardDB.curPos).equals("게시판")) {
                        editNormal(false);
                    } else {
                        editVideo(false);
                    }
                } else {
                    if (BoardDB.Category.get(BoardDB.curPos).equals("게시판")) {
                        showNormalBoard(position);
                    } else {
                        Intent intent = new Intent(BoardList.this,VideoViewer.class);
                        intent.putExtra("Path",mListData.get(position).Contents);
                        startActivity(intent);
                    }
                }
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListData.get(position).Producer.equals(MyInformation.ID) || MyInformation.level>=999) {
                    mGlobalPoisition = position;
                    if (position != mListData.size() - 1)
                        showSelect(mGlobalPoisition);

                }
                return false;
            }
        });
    }

    public class ItemListAdapter extends BaseAdapter {
        public ArrayList<BoardData> mdatas;
        LayoutInflater inflater;

        public ItemListAdapter(LayoutInflater inflater, ArrayList<BoardData> datas) {
            // TODO Auto-generated constructor stub
            //객체를 생성하면서 전달받은 datas(MemberData 객체배열)를 멤버변수로 전달
            //아래의 다른 멤버 메소드에서 사용하기 위해서.멤버변수로 참조값 전달
            this.mdatas = datas;
            this.inflater = inflater;
        }

        public void datachange() {
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mdatas.size(); //datas의 개수를 리턴
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mdatas.get(position);//datas의 특정 인덱스 위치 객체 리턴.
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.board_list_item, null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.boardTitle);
            textView.setText(mdatas.get(position).Title);

            return convertView;
        }
    }
}
