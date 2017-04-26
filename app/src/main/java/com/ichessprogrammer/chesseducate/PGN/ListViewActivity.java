package com.ichessprogrammer.chesseducate.PGN;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ichessprogrammer.chesseducate.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * Created by XNOTE on 2016-08-16.
 */
public class ListViewActivity extends AppCompatActivity implements PgnTagInterface {

    String dataPGN;

    HashMap<Integer, String> dataFEN = new HashMap<>();
    Intent intent;



    ArrayList<String> values = new ArrayList<String>();
    ArrayList<String> values2 = new ArrayList<String>();
    ArrayList<String> values3 = new ArrayList<String>();
    ArrayList<String> values4 = new ArrayList<String>();
    String stringFEN = "";

    ArrayList<StringBuffer> pgnFile = new ArrayList<>();

    int i = 0;

    PgnAdapter pgnAdapter = new PgnAdapter();
    ListView listView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showlistview_layout);
        listView =(ListView)findViewById(R.id.listview);
        intent = ListViewActivity.this.getIntent();
        startActivityForResult(new Intent(this,PGNFileListView.class),12345);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 12345) && (resultCode == Activity.RESULT_OK)) {
            boolean bt = false;
            boolean hasFEN = false;
            View v;
            File file = new File(data.getStringExtra("result"));

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
                                dataFEN.put(i, stringFEN);
                                stringFEN = "";
                                hasFEN = false;
                            }
                            i++;
                            pgnFile.add(pgnData);
                            pgnData = new StringBuffer();
                        }
                    }
                    if(hasFEN) {
                        dataFEN.put(i, stringFEN);
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

            listView.setAdapter(pgnAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Bundle bundle = new Bundle();
                    Log.d("안녕","하세요");
                    bundle.putString("PGN",pgnFile.get(position).toString());
                    Log.d("안녕","하세요2");
                    String fen="";
                    if(dataFEN.containsKey(position)) {
                        Log.d("안녕","하세요3");
                        fen = dataFEN.get(position);
                        bundle.putString("FEN",fen);
                    }

                    intent.putExtras(bundle);

                    ListViewActivity.this.setResult(Activity.RESULT_OK,intent);
                    Log.d("디벅스",pgnFile.get(position).toString());
                    finish();

                    // Toast.makeText(getApplicationContext(), pgnFile.get(position), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            finish();
    }
}