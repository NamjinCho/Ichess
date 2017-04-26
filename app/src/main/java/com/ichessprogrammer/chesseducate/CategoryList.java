package com.ichessprogrammer.chesseducate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ichessprogrammer.chesseducate.Diolog.Custom_Dialog;
import com.ichessprogrammer.chesseducate.PGN.PGNLoading1;
import com.ichessprogrammer.chesseducate.Problem.QuizLoading;
import com.ichessprogrammer.chesseducate.Server.PHPConnector;
import com.ichessprogrammer.chesseducate.lecture.LecturLoading;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.util.ArrayList;

/**
 * Created by 남지니 on 2016-09-01.
 */
public class CategoryList extends AppCompatActivity {


    public int selectPosition = -1;
    GridView mGridView;
    ArrayList<CategoryItem> mListItem = new ArrayList<>();
    CategoryAdapter mAdapter;
    String myCategory;
    ThumnailSave1 thumnailSave1;
    ThumnailSave2 thumnailSave2;
    ThumnailSave3 thumnailSave3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myCategory = getIntent().getStringExtra("Category");
        if(myCategory.startsWith("기보")){
            setContentView(R.layout.layout_activity_category2);
        }else
        {
            setContentView(R.layout.layout_activity_category);
        }

        FrameLayout root = (FrameLayout) findViewById(R.id.root);

        if (myCategory.startsWith("기보")) {
            thumnailSave3 = new ThumnailSave3();
            TextView textView = (TextView) findViewById(R.id.Title);
            textView.setText("기보 보기");
            root.setBackgroundColor(Color.parseColor("#f18f6b"));
        } else if (myCategory.startsWith("문제")) {
            thumnailSave1 = new ThumnailSave1();
            root.setBackgroundColor(Color.parseColor("#6e9aca"));
            TextView textView = (TextView) findViewById(R.id.Title);
            textView.setText("문제 보기");
        } else if (myCategory.startsWith("강의")) {
            thumnailSave2 = new ThumnailSave2();
            root.setBackgroundColor(Color.parseColor("#377061"));
            TextView textView = (TextView) findViewById(R.id.Title);
            textView.setText("강의 보기");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().toString().startsWith("메인")) {
                    finish();
                } else if (item.getTitle().toString().startsWith("갤러리")) {
                    startActivity(new Intent(CategoryList.this, Gallery.class));
                } else if (item.getTitle().toString().startsWith("설정")) {
                    startActivity(new Intent(CategoryList.this, SettingActivity.class));
                } else if (item.getTitle().toString().startsWith("동영상")) {

                } else if (item.getTitle().toString().startsWith("약도")) {

                } else if (item.getTitle().toString().startsWith("로그아웃")) {

                    KakaoTalkMainActivity.trimCache(getApplicationContext());
                    SharedPreferences preferences = getSharedPreferences("Ichess", 0);
                    if (preferences.getString("Password", "null").startsWith("kakao")) {
                        UserManagement.requestLogout(new LogoutResponseCallback() {
                            @Override
                            public void onCompleteLogout() {
                                //
                                Log.i("로그아웃", "> onCompleteLogout()");
                            }
                        });
                    }
                    preferences.edit().putString("ID", "null").commit();
                    preferences.edit().remove("Password").commit();
                    //clearApplicationCache(null);
                    Intent intent = new Intent(CategoryList.this, KakaoTalkMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
        init();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        mGridView = (GridView) findViewById(R.id.category_list_view);
        //mGridView.setColumnWidth(width / 4);
        mAdapter = new CategoryAdapter(CategoryList.this, getLayoutInflater(), mListItem);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             @Override
                                             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                 if (mListItem.get(position).Title.equals("null")) {
                                                         Intent intent = new Intent(CategoryList.this,Eddit_Menu.class);
                                                         intent.putExtra("Category",myCategory);
                                                         startActivity(intent);
                                                         finish();

                                                 } else if (myCategory.equals("강의")) {
                                                     BoardDB.curPos = mListItem.get(position).globalPosition;
                                                     Intent intent = new Intent(getApplicationContext(), LecturLoading.class);
                                                     startActivity(intent);
                                                     finish();
                                                 } else if (myCategory.equals("기보")) {
                                                     BoardDB.curPos = mListItem.get(position).globalPosition;
                                                     Intent intent = new Intent(getApplicationContext(), PGNLoading1.class);
                                                     startActivity(intent);
                                                     finish();
                                                 } else if (myCategory.equals("문제풀기")) {
                                                     BoardDB.curPos = mListItem.get(position).globalPosition;
                                                     Intent intent = new Intent(getApplicationContext(), QuizLoading.class);
                                                     startActivity(intent);
                                                     finish();
                                                 }
                                             }
                                         }
        );
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(MyInformation.level>=999)
                {
                    if(position==mListItem.size()-1)
                        return false;
                }
                Log.d("디버깅","디벅스");
                selectPosition = position;
                mAdapter.dataChange();
                //ShowDialog();
                return true;
            }
        });
    }

    public void BackButton(View v) {
        finish();
    }

    public void onRestart() {
        super.onRestart();

    }

    public void onStart() {
        super.onStart();
    }

    public void onResume() {
        super.onResume();
        ImageView back = (ImageView) findViewById(R.id.Background);
        Resources r = getResources();
        if (myCategory.startsWith("기보")) {
            BitmapDrawable bd = (BitmapDrawable) r.getDrawable(R.drawable.bg_orange);
            Bitmap pic = bd.getBitmap();
            back.setImageBitmap(pic);
        } else if (myCategory.startsWith("문제")) {
            BitmapDrawable bd = (BitmapDrawable) r.getDrawable(R.drawable.bg_blue);
            Bitmap pic = bd.getBitmap();
            back.setImageBitmap(pic);
        } else if (myCategory.startsWith("강의")) {
            BitmapDrawable bd = (BitmapDrawable) r.getDrawable(R.drawable.bg_green);
            Bitmap pic = bd.getBitmap();
            back.setImageBitmap(pic);
        }
        mAdapter.dataChange();
    }

    public void onPause() {
        super.onPause();

        ImageView back = (ImageView) findViewById(R.id.Background);
        back.setImageBitmap(null);
        System.gc();
    }

    public void onStop() {
        super.onStop();
        System.gc();
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void init() {
        for (int i = 0; i < BoardDB.Category.size(); i++) {
            if (BoardDB.Category.get(i).equals(myCategory)) {
                CategoryItem temp = new CategoryItem();
                temp.Category = BoardDB.Category.get(i);
                temp.globalPosition = i;
                temp.Title = BoardDB.Title.get(i);
                mListItem.add(temp);
            }
        }
        if (MyInformation.level >= 999) {
            CategoryItem temp = new CategoryItem();
            temp.Category = myCategory;
            temp.Title = "null";
            mListItem.add(temp);
        }
    }

    public void ShowEditDialog() {
        final int position = selectPosition;
        final Custom_Dialog dialog2 = new Custom_Dialog(CategoryList.this, R.style.AlertDialogCustom);
        dialog2.setContentView(R.layout.dialog_edit_title);
        final EditText editTitle = (EditText) dialog2.findViewById(R.id.editContents);
        final Button ButtonOK, ButtonCancle;
        ButtonOK = (Button) dialog2.findViewById(R.id.buttonOK);
        ButtonCancle = (Button) dialog2.findViewById(R.id.buttonCancle);
        ButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Category = mListItem.get(position).Category;
                String Title = mListItem.get(position).Title;
                String newTitle = editTitle.getText().toString();
                if (newTitle.equals("")) {
                    return;
                }
                if (Category.equals("강의")) {
                    Log.d("여기가", "들어오나?");
                    new PHPConnector().ConnectServer("table=lecture_" + Title
                            + "&newTable=lecture_" + newTitle, "renameTable.php", "drop");
                    new PHPConnector().ConnectServer("table=clearLecture_" + Title
                            + "&newTable=clearLecture_" + newTitle, "renameTable.php", "drop");
                    new PHPConnector().ConnectServer("table=problem_" + Title
                            + "&newTable=problem_" + newTitle, "renameTable.php", "drop");
                    new PHPConnector().ConnectServer("table=clearProblem_" + Title
                            + "&newTable=clearProblem_" + newTitle, "renameTable.php", "drop");
                } else if (Category.equals("기보")) {
                    new PHPConnector().ConnectServer("table=PGN_" + Title
                            + "&newTable=PGN_" + newTitle, "renameTable.php", "drop");
                    new PHPConnector().ConnectServer("table=clearPGNproblem_" + Title
                            + "&newTable=clearPGNproblem_" + newTitle, "renameTable.php", "drop");

                } else if (Category.equals("게시판")) {
                    new PHPConnector().ConnectServer("table=normalBoard_" + Title
                            + "&newTable=normalBoard_" + newTitle, "renameTable.php", "drop");
                } else if (Category.equals("동영상")) {
                    new PHPConnector().ConnectServer("table=videoBoard_" + Title
                            + "&newTable=videoBoard_" + newTitle, "renameTable.php", "drop");
                }
                new PHPConnector().ConnectServer("category=" + Category + "&title=" + Title + "&newTitle=" + newTitle, "updateBoard.php", "sss");
                BoardDB.Title.remove(mListItem.get(position).globalPosition);
                BoardDB.Title.add(mListItem.get(position).globalPosition, newTitle);
                BoardDB.ImgPos.put(Category + "_" + newTitle, BoardDB.ImgPos.get(Category + "_" + Title));
                BoardDB.ImgPos.remove(Category + "_" + Title);
                mListItem.get(position).Title = newTitle;
                mAdapter.mListData = mListItem;
                mAdapter.dataChange();
                editTitle.getText().toString();
                selectPosition = -1;
                mAdapter.dataChange();
                dialog2.cancel();
            }
        });
        ButtonCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.cancel();
                selectPosition = -1;
                mAdapter.dataChange();
            }
        });
        dialog2.show();
    }

    public void ShowDialog() {


        final int position = selectPosition;
        final Custom_Dialog dialog2 = new Custom_Dialog(CategoryList.this, R.style.AlertDialogCustom);
        dialog2.setContentView(R.layout.dialog_select_lec_menu2);
        final Button[] buttons = new Button[2];
        buttons[0] = (Button) dialog2.findViewById(R.id.edit);
        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowEditDialog();
                dialog2.cancel();
            }
        });
        buttons[1] = (Button) dialog2.findViewById(R.id.delete_button);
        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //SetData();
                //adapter.notifyDataSetInvalidated();
                dialog2.cancel();
            }
        });
        dialog2.show();
    }
    public void DeleteButton(View v)
    {
        int position = selectPosition;
        String Category = mListItem.get(position).Category;
        String Title = mListItem.get(position).Title;
        if (Category.equals("강의")) {
            new PHPConnector().ConnectServer("table=lecture_" + Title, "dropTable.php", "drop");
            new PHPConnector().ConnectServer("table=clearLecture_" + Title, "dropTable.php", "drop");
            new PHPConnector().ConnectServer("table=problem_" + Title, "dropTable.php", "drop");
            new PHPConnector().ConnectServer("table=clearProblem_" + Title, "dropTable.php", "drop");
            MyInformation.ClearLecID.remove(Title);
            BoardDB.Category.remove(mListItem.get(position).globalPosition);
            BoardDB.Title.remove(mListItem.get(position).globalPosition);
            BoardDB.ImgPos.remove(Category + "_" + Title);
        } else if (Category.equals("기보")) {
            new PHPConnector().ConnectServer("table=PGN_" + Title, "dropTable.php", "drop");
            new PHPConnector().ConnectServer("table=clearPGNproblem_" + Title, "dropTable.php", "drop");
            BoardDB.Category.remove(mListItem.get(position).globalPosition);
            BoardDB.Title.remove(mListItem.get(position).globalPosition);
            BoardDB.ImgPos.remove(Category + "_" + Title);
        } else if (Category.equals("동영상")) {
            new PHPConnector().ConnectServer("table=videoBoard_" + Title, "dropTable.php", "drop");
            BoardDB.Category.remove(mListItem.get(position).globalPosition);
            BoardDB.Title.remove(mListItem.get(position).globalPosition);
            BoardDB.ImgPos.remove(Category + "_" + Title);
        } else if (Category.equals("게시판")) {
            new PHPConnector().ConnectServer("table=normalBoard_" + Title, "dropTable.php", "drop");
            BoardDB.Category.remove(mListItem.get(position).globalPosition);
            BoardDB.Title.remove(mListItem.get(position).globalPosition);
            BoardDB.ImgPos.remove(Category + "_" + Title);
        }else
        {

            new PHPConnector().ConnectServer("table=PGNproblem_" + Title, "dropTable.php", "drop");
            new PHPConnector().ConnectServer("table=clearPGNproblem_" + Title, "dropTable.php", "drop");
            BoardDB.Category.remove(mListItem.get(position).globalPosition);
            BoardDB.Title.remove(mListItem.get(position).globalPosition);
            BoardDB.ImgPos.remove(Category + "_" + Title);
        }
        mListItem.remove(position);
        mAdapter.mListData = mListItem;
        mAdapter.dataChange();
        new PHPConnector().ConnectServer("category=" + Category + "&title=" + Title, "deleteBoard.php", "drop");
        selectPosition=-1;
        mAdapter.dataChange();
    }
    public void EditButton(View v)
    {
        ShowEditDialog();
        selectPosition=-1;
    }
    public class CategoryItem {
        public String Title;
        public String Category;
        public int globalPosition;
    }

    public class CategoryAdapter extends BaseAdapter {
        LayoutInflater inflater;
        private Context mContext = null;
        private ArrayList<CategoryItem> mListData = new ArrayList<>();

        public CategoryAdapter(Context mContext, LayoutInflater inflater, ArrayList<CategoryItem> listdatas) {
            super();
            this.mContext = mContext;
            this.inflater = inflater;
            mListData = listdatas;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        public ArrayList<CategoryItem> getListData() {
            return mListData;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void remove(int position) {
            mListData.remove(position);
            dataChange();
        }

        public void dataChange() {
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (true) {
                if(myCategory.startsWith("기보"))
                    convertView = inflater.inflate(R.layout.category_list_item2, null);
                else
                    convertView = inflater.inflate(R.layout.category_list_item, null);

            }
            if(selectPosition!=-1 && selectPosition == position)
            {

                if(myCategory.startsWith("기보"))
                    convertView = inflater.inflate(R.layout.category_list_item2_edit, null);
                else
                    convertView = inflater.inflate(R.layout.category_list_item_edit, null);
                Log.d("디벅스","디버깅");
            }
            TextView text_item_title = (TextView) convertView.findViewById(R.id.title);
            ImageView thumb = (ImageView) convertView.findViewById(R.id.thumb);

            //현재 position( getView()메소드의 첫번재 파라미터 )번째의 Data를 위 해당 View들에 연결..
            if (!mListData.get(position).Title.equals("null")) {
                text_item_title.setText(mListData.get(position).Title);
                if (myCategory.startsWith("강의")) {
                    int imgpos = BoardDB.ImgPos.get(mListData.get(position).Category + "_" + mListData.get(position).Title);
                    thumb.setImageResource(thumnailSave2.thums[imgpos]);
                    text_item_title.setTextColor(Color.parseColor("#4d8176"));
                } else if (myCategory.startsWith("기보")) {
                    int imgpos = BoardDB.ImgPos.get(mListData.get(position).Category + "_" + mListData.get(position).Title);
                    thumb.setImageResource(thumnailSave3.thums[imgpos]);
                    text_item_title.setTextColor(Color.parseColor("#f18f6b"));
                } else if (myCategory.startsWith("문제")) {
                    int imgpos = BoardDB.ImgPos.get(mListData.get(position).Category + "_" + mListData.get(position).Title);
                    thumb.setImageResource(thumnailSave1.thums[imgpos]);
                    text_item_title.setTextColor(Color.parseColor("#6e9aca"));
                }
            } else {
                text_item_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                if (myCategory.startsWith("문제")) {
                    text_item_title.setText("문제만들기");
                    text_item_title.setTextColor(Color.parseColor("#6e9aca"));
                    thumb.setImageResource(R.drawable.icon_plus_blue);
                    ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) thumb.getLayoutParams();
                    params.height = (int) (params.height / 2);
                    params.width = (int) (params.width / 2);
                    thumb.setLayoutParams(params);
                    ViewGroup.MarginLayoutParams lpimgFooter = (ViewGroup.MarginLayoutParams) thumb.getLayoutParams();
                    lpimgFooter.topMargin = params.height / 2;
                    lpimgFooter.leftMargin = params.width / 2;
                    lpimgFooter.rightMargin = params.width / 2;
                    thumb.setLayoutParams(lpimgFooter);
                } else if (myCategory.startsWith("강의")) {
                    text_item_title.setText("강의만들기");
                    thumb.setImageResource(R.drawable.icon_plus_green);
                    text_item_title.setTextColor(Color.parseColor("#4d8176"));
                    ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) thumb.getLayoutParams();
                    params.height = (int) (params.height / 2);
                    params.width = (int) (params.width / 2);
                    thumb.setLayoutParams(params);
                    ViewGroup.MarginLayoutParams lpimgFooter = (ViewGroup.MarginLayoutParams) thumb.getLayoutParams();
                    lpimgFooter.topMargin = params.height / 2;
                    lpimgFooter.leftMargin = params.width / 2;
                    lpimgFooter.rightMargin = params.width / 2;
                    thumb.setLayoutParams(lpimgFooter);
                } else if (myCategory.startsWith("기보")) {
                    convertView = inflater.inflate(R.layout.category_list_item3,null);
                    return convertView;
                    /*
                    text_item_title.setText("기보올리기");
                    thumb.setImageResource(R.drawable.icon_plus_orange);
                    text_item_title.setTextColor(Color.parseColor("#f18f6b"));
                    ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) thumb.getLayoutParams();
                    params.height = (int) (params.height / 2);
                    params.width = (int) (params.width / 2);
                    thumb.setLayoutParams(params);
                    ViewGroup.MarginLayoutParams lpimgFooter = (ViewGroup.MarginLayoutParams) thumb.getLayoutParams();
                    lpimgFooter.topMargin = params.height / 2;
                    lpimgFooter.leftMargin = params.width / 2;
                    lpimgFooter.rightMargin = params.width / 2;
                    thumb.setLayoutParams(lpimgFooter);
                    */
                }
            }
            return convertView;
        }
    }
}
