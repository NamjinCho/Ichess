package com.ichessprogrammer.chesseducate.PGN;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ichessprogrammer.chesseducate.BoardDB;
import com.ichessprogrammer.chesseducate.Gallery;
import com.ichessprogrammer.chesseducate.KakaoTalkMainActivity;
import com.ichessprogrammer.chesseducate.MyInformation;
import com.ichessprogrammer.chesseducate.Problem.QuizLoading;
import com.ichessprogrammer.chesseducate.Problem.make_prob_select;
import com.ichessprogrammer.chesseducate.R;
import com.ichessprogrammer.chesseducate.Server.PHPConnector;
import com.ichessprogrammer.chesseducate.SettingActivity;
import com.ichessprogrammer.chesseducate.lecture.LecAndProb;
import com.ichessprogrammer.chesseducate.lecture.LectureLoading2;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 남지니 on 2016-08-26.
 */
public class UploadPGN extends AppCompatActivity implements PgnTagInterface {
    ListView mListView;
    ListView mListView2;
    String []PGNFileNames;
    ArrayList<File> filelist;
    PgnAdapter mGlobalAdapter;
    //게임목록 변수
    HashMap<Integer, String> dataFEN = new HashMap<>();
    Intent intent;
    public static int caller;
    ArrayList<String> values;
    ArrayList<String> values2;
    ArrayList<String> values3;
    ArrayList<String> values4;
    String stringFEN = "";
    int pos=0;
    ArrayList<StringBuffer> pgnFile;
    //컨트롤러 변수들
    EditText titleEdit;
    String pgns="";
    levelAdapter mLevelAdapter;
    int level=1;
    int PGN_File_pos=-1;
    boolean waitFlag = false;
    TestDialogFragment dialogFragment;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        }
    };
    @Override
    public void onCreate(Bundle savebundle)
    {
        super.onCreate(savebundle);
        //1강의 2 문제 3 기보
        if(caller<3)
            setContentView(R.layout.layout_activity_upload_pgn);
        else
            setContentView(R.layout.layout_activity_upload_pgn2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().toString().startsWith("메인")) {
                    finish();
                } else if (item.getTitle().toString().startsWith("갤러리")) {
                    startActivity(new Intent(UploadPGN.this, Gallery.class));
                } else if (item.getTitle().toString().startsWith("설정")) {
                    startActivity(new Intent(UploadPGN.this, SettingActivity.class));
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
                    Intent intent = new Intent(UploadPGN.this, KakaoTalkMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
        //mListView = (ListView)findViewById(R.id.listView_FileList);
        //mListView2=(ListView)findViewById(R.id.listView_PGNList);
        filelist = new ArrayList<>();
        //titleEdit=(EditText)findViewById(R.id.editText_Title);
        File root = new File(Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS).getPath());
        File[] files2 = root.listFiles();
        intent = getIntent();
        for(int i=0;i<files2.length;i++)
        {
            if(files2[i].getName().endsWith(".pgn") || files2[i].getName().endsWith(".Pgn") || files2[i].getName().endsWith(".PGN"))
                filelist.add(files2[i]);

            Log.d("PATH",files2[i].getPath());
        }
        PGNFileNames = new String[filelist.size()];
        for(int i=0;i<filelist.size();i++) {
            PGNFileNames[i] = filelist.get(i).getName();
            Log.d("PATH",filelist.get(i).getPath());
        }
        mLevelAdapter= new levelAdapter();
        for(int i=1;i<31;i++)
        {
            mLevelAdapter.listViewItemArray.add(i);
        }
        Spinner levelView = (Spinner)findViewById(R.id.level_spinner);
        levelView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                level=position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        levelView.setAdapter(mLevelAdapter);
        /*
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String path = filelist.get(position).getPath();
                pgns="";
                Thread passer = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        datachange(path);
                    }
                });
                passer.start();

            }
        });
        */

    }

    public void onBackPressed()
    {
        BackButton(null);
    }
    public void BackButton(View v) {
        if(caller<3) {
            Intent intent = new Intent(UploadPGN.this, make_prob_select.class);
            //intent.putExtra("Category", myCategory);
            startActivity(intent);
            System.gc();
            finish();
        }else
        {

            Intent intent = new Intent(UploadPGN.this, PGNLoading1.class);
            //intent.putExtra("Category", myCategory);
            startActivity(intent);
            System.gc();
            finish();
        }
    }
    public void onResume()
    {
        super.onResume();
        ImageView back =(ImageView)findViewById(R.id.background);
        if(caller<3)
        {
            back.setImageResource(R.drawable.bg_blue);
        }else
        {
            back.setImageResource(R.drawable.bg_orange);
        }
    }
    public void onPause()
    {
        super.onPause();
        ImageView back=(ImageView)findViewById(R.id.background);
        back.setImageBitmap(null);
        System.gc();
    }
    public void ButtonFind(View v) {
        FragmentManager fm = getSupportFragmentManager();
        dialogFragment = new TestDialogFragment();
        dialogFragment.setting(PGNFileNames);
        dialogFragment.show(fm, "as");
    }
    public void ButtonCreate(View v)
    {
        if(!waitFlag){
            Toast.makeText(getApplicationContext(),"아직 파일을 확인중 입니다. \n잠시만 기다려주세요",Toast.LENGTH_SHORT).show();
            return;
        }
        titleEdit=(EditText)findViewById(R.id.titlebox);
        String title=titleEdit.getText().toString();
        //String level=;
        if(!title.equals(""))
        {
            String Params;
            Params="level="+level;
            if(caller==3) {
                Params += "&producer=" + MyInformation.ID + "&table=PGN_" + BoardDB.Title.get(BoardDB.curPos);
                if(!pgns.equals(""))
                    Params+="&pgns="+pgns.replaceAll("&","~~~~");
                else
                    return;
                new PHPConnector().ConnectServer(Params,"insertPGN.php","insertPGN");
            }
            if(caller==1) {
                Params += "&id=" + MyInformation.ID + "&table=" + "problem_" + BoardDB.Title.get(BoardDB.curPos)
                        + "&lecID=" + LecAndProb.LectureData.lecID;
                if(!pgns.equals(""))
                    Params+="&pgns="+pgns.replaceAll("&","~~~~");
                else
                    return;
                Log.d("디벅",Params);
                new PHPConnector().ConnectServer(Params, "insertProb.php", "insertPGN");
            }
            if(caller==2){
                Params+="&producer=" + MyInformation.ID + "&table=" + "PGNproblem_" + BoardDB.Title.get(BoardDB.curPos)
                        + "&title=" + title;
                if(!pgns.equals(""))
                    Params+="&pgns="+pgns.replaceAll("&","~~~~");
                else
                    return;
                new PHPConnector().ConnectServer(Params, "insertPGNProblem.php", "insertPGN");
            }
            Log.d("디버깅",Params);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true)
                    {
                        if(PHPConnector.MessageQue.get("insertPGN")!=null)
                        {
                            Log.d("디벅",PHPConnector.MessageQue.get("insertPGN"));
                            PHPConnector.MessageQue.clear();
                            break;
                        }
                    }
                }
            });
            thread.start();
            if (caller == 1) {
                Intent intent = new Intent(UploadPGN.this, QuizLoading.class);
                //intent.putExtra("Category", myCategory);
                startActivity(intent);
                System.gc();
                finish();
            } else if (caller == 2) {
                Intent intent = new Intent(UploadPGN.this, LectureLoading2.class);
                //intent.putExtra("Category", myCategory);
                startActivity(intent);
                System.gc();
                finish();
            } else {

                Intent intent = new Intent(UploadPGN.this, PGNLoading1.class);
                //intent.putExtra("Category", myCategory);
                startActivity(intent);
                System.gc();
                finish();
            }
        }
    }
    public void datachange(String path)
    {
        values = new ArrayList<String>();
        values2 = new ArrayList<String>();
        values3 = new ArrayList<String>();
        values4 = new ArrayList<String>();
        dataFEN = new HashMap<>();
        pgnFile = new ArrayList<>();
        pos=0;
        stringFEN="";
        PgnAdapter pgnAdapter = new PgnAdapter();
        Log.d("디벅스","세여");
        boolean bt = false;
        boolean hasFEN = false;
        View v;
        File file = new File(path);

        try {

            int readcount=0;
            //InputStream in = this.getResources().openRawResource(R.raw.hello);
            FileInputStream in = new FileInputStream(file);
            if (in != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String str = "";
                //StringBuilder buf = new StringBuilder();
                StringBuffer pgnData =  new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    pgns+=str+"||";
                    if(str.startsWith("\n") && str.length()==1)
                        continue;
                    if(str.startsWith("[")) {
                        bt=false;
                        String string =  "";

                        if(str.contains(PGN_EVENTDATE)){

                        }
                        else if(str.contains(PGN_EVENT)){

                        }
                        else if (str.contains(PGN_WHITE)) {
                            string = str.substring(str.indexOf("\"")+ 1, str.lastIndexOf("\""));
                            values.add(string);
                        } else if (str.contains(PGN_BLACK)) {
                            string = str.substring(str.indexOf("\"")+ 1, str.lastIndexOf("\""));
                            values2.add(string);

                        } else if (str.contains(PGN_DATE)) {
                            string = str.substring(str.indexOf("\"")+ 1, str.lastIndexOf("\""));
                            values3.add(string);

                        } else if (str.contains(PGN_RESULT)) {
                            string = str.substring(str.indexOf("\"")+ 1, str.lastIndexOf("\""));
                            values4.add(string);
                        } else if (str.contains(PGN_FEN)) {
                            stringFEN  = str.substring(str.indexOf("\"")+ 1, str.lastIndexOf("\""));
                            Log.d("펀확",stringFEN);
                            hasFEN = true;
                        }
                    }
                    else
                        bt=true;
                    if(bt) {
                        String s = str + " ";
                        pgnData.append(s);
                    } else if(pgnData.length() > 1) {

                        if(hasFEN) {
                            dataFEN.put(pos, stringFEN);
                            stringFEN = "";
                            hasFEN = false;
                        }
                        pos++;
                        pgnFile.add(pgnData);
                        pgnData = new StringBuffer();
                    }
                }
                if(hasFEN) {
                    dataFEN.put(pos, stringFEN);
                    stringFEN = "";
                    hasFEN = false;
                }
                pgnFile.add(pgnData);
                in.close();
            }
        } catch (java.io.FileNotFoundException e) { // that's OK, we probably haven't created it yet
        } catch (Throwable t) {
            Toast.makeText(getApplicationContext(), "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
            Log.e("error", t.toString());
        }
        Log.d("디벅스",values.size()+"?"+values2.size()+"?"+values3.size()+"?"+values4.size());
        for(int i = 0; i < values.size(); i++) {
            pgnAdapter.addItem(values.get(i),values2.get(i),values3.get(i),values4.get(i));
        }
        mGlobalAdapter=pgnAdapter;
        Message msg = mHandler.obtainMessage(1,"success");
        mHandler.sendMessage(msg);
    }
    public void selectIcon(View v)
    {
        //dialogFragment.selectIcon(v);
        PGN_File_pos = dialogFragment.pgn_pos;

        dialogFragment.dismiss();
        EditText text = (EditText)findViewById(R.id.editText);
        if(PGN_File_pos!=-1)
        {
            text.setText(PGNFileNames[PGN_File_pos]);
            pgns="";
            waitFlag=false;
            Thread passer = new Thread(new Runnable() {
                @Override
                public void run() {
                    datachange(filelist.get(PGN_File_pos).getPath());
                    waitFlag=true;
                }
            });
            passer.start();
        }
    }
    public void cancleIcon(View v)
    {
        dialogFragment.dismiss();
        EditText text = (EditText)findViewById(R.id.editText);

    }
    /**
     * Created by XNOTE on 2016-08-10.
     */
    public class levelAdapter extends BaseAdapter {

        private ArrayList<Integer> listViewItemArray = new ArrayList<>();

        public levelAdapter() {
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
                convertView = layoutInflater.inflate(R.layout.level_item, parent, false);
            }
            TextView textView = (TextView)convertView.findViewById(R.id.level_item);
            textView.setText(""+listViewItemArray.get(position)+"");
            return convertView;
        }
    }

    public static class TestDialogFragment extends DialogFragment {

        ListViewAdapter mAdpater;
        Dialog myLayout;
        ArrayList<String> mData;
        int pgn_pos=-1;

        public TestDialogFragment(){
            super();
        }
        public void setting(String [] data){
            mData=new ArrayList<>();
            for (int i=0;i<data.length;i++)
            {
                mData.add(data[i]);
            }
        }
        @Override
        public void setArguments(Bundle args) {
            super.setArguments(args);
        }

        //int[] buttonId = {R.id.rook, R.id.knight, R.id.bishop, R.id.queen };

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            super.onCreateDialog(savedInstanceState);

            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
            LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();


            mBuilder.setView(mLayoutInflater
                    .inflate(R.layout.dialog_select_pgn_file, null));


            myLayout=mBuilder.create();
            myLayout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            return myLayout;
        }

        @Override
        public void onResume() {
            ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();


            params.height =  (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 277, getActivity().getApplicationContext().getResources().getDisplayMetrics()
            );
            params.width =  (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 464, getActivity().getApplicationContext().getResources().getDisplayMetrics()
            );
            mAdpater = new ListViewAdapter(getContext(),getActivity().getLayoutInflater());//(LayoutInflater)context.getSystemService(Context.LAYOUT_INFL‌​ATER_SERVICE)
            mAdpater.mListData = mData;
            ListView list = (ListView)myLayout.findViewById(R.id.fileView);
            list.setAdapter(mAdpater);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    pgn_pos = position;
                }
            });
            getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams)params);

            super.onResume();
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        public class thumData {
            int pos = 0;
            boolean check = false;
        }

        public class ListViewAdapter extends BaseAdapter {
            LayoutInflater inflater;
            private Context mContext = null;
            private ArrayList<String> mListData;

            public ListViewAdapter(Context mContext, LayoutInflater inflater) {
                super();
                this.mContext = mContext;
                this.inflater = inflater;
            }

            @Override
            public int getCount() {
                return mListData.size();
            }

            @Override
            public Object getItem(int position) {
                return mListData.get(position);
            }

            public ArrayList<String> getListData() {
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
                this.notifyDataSetChanged();

            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.file_list_item, null);
                }
                TextView fileName = (TextView)convertView.findViewById(R.id.fileName);
                fileName.setText(mListData.get(position));
                return convertView;
            }
        }
    }

}
