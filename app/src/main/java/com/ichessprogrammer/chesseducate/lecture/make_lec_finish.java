package com.ichessprogrammer.chesseducate.lecture;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.ichessprogrammer.chesseducate.BoardDB;
import com.ichessprogrammer.chesseducate.MyInformation;
import com.ichessprogrammer.chesseducate.R;
import com.ichessprogrammer.chesseducate.Server.PHPConnector;

/**
 * Created by 남지니 on 2016-07-27.
 */
public class make_lec_finish extends Activity {

    EditText title;
    EditText levleText;
    ImageView buttonNext;
    ImageView buttonPre;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_lec_finish);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        init();
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LecureSave.lecID == -1) {
                    String titleText = title.getText().toString();
                    String level = levleText.getText().toString();
                    if (level.equals(""))
                        level = "1";
                    else if (Integer.parseInt(level) > 30)
                        level = "30";

                    String fen = LecureSave.FEN;
                    String marks = getMarkFromSave();
                    String pgn = getPGNFromSave();
                    String explain = "1. " + titleText + " 입니다. " + getExplainFromSave();
                    String params = "level=" + level + "&fen=" + fen + "&pgn=" + "1. null " + pgn + "&explain=" + explain + "&title=" + titleText +
                            "&id=" + MyInformation.ID + "&mark=" + marks + "&table=" + "lecture_" + BoardDB.Title.get(BoardDB.curPos);

                    new PHPConnector().ConnectServer(params, "insertLec.php", "insertLecPHP");
                    LecureSave.PGN_Explain = null;
                    LecureSave.callFragment = "";
                    LecureSave.FEN = "";
                    LecureSave.saveBoard = null;
                    finish();
                }else
                {
                    String titleText = title.getText().toString();
                    String level = levleText.getText().toString();
                    if (level.equals(""))
                        level = "1";
                    else if (Integer.parseInt(level) > 30)
                        level = "30";

                    String fen = LecureSave.FEN;
                    String marks = getMarkFromSave();
                    String pgn = getPGNFromSave();
                    String explain = "1. " + titleText + " 입니다. " + getExplainFromSave();

                    String params = "level=" + level +"&lecID="+LecureSave.lecID+"&fen=" + fen + "&pgn=" + "1. null " + pgn + "&explain=" + explain + "&title=" + titleText +
                            "&id=" + LecureSave.prod + "&mark=" + marks + "&table=" + "lecture_" + BoardDB.Title.get(BoardDB.curPos);

                    new PHPConnector().ConnectServer(params, "insertLec2.php", "insertLecPHP");
                    LecureSave.PGN_Explain = null;
                    LecureSave.callFragment = "";
                    LecureSave.FEN = "";
                    LecureSave.saveBoard = null;
                    finish();
                }
            }

        });
        buttonPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Make_Lec_Act2.class);
                startActivity(intent);
                finish();
            }
        });
    }
// Call this method from onDestroy()
    private String getMarkFromSave()
    {
        String result = "";
        for (int i = 0; i < LecureSave.PGN_Explain.size(); i++) {
            for(int j=0;j<LecureSave.PGN_Explain.get(i).marks.size();j++)
            {
                String pos = ""+LecureSave.PGN_Explain.get(i).marks.get(j).pos;
                String row = ""+LecureSave.PGN_Explain.get(i).marks.get(j).row;
                String col =""+LecureSave.PGN_Explain.get(i).marks.get(j).col;

                if(!pos.startsWith("6"))
                    result += ("("+(i + 2) + ". " +pos+row+col+")");
                else{
                    String row2 = ""+LecureSave.PGN_Explain.get(i).marks.get(j).row2;
                    String col2 =""+LecureSave.PGN_Explain.get(i).marks.get(j).col2;
                    result += ("("+(i + 2) + ". " +pos+row+col+row2+col2+")");
                }

            }
        }
        return result;
    }
    private String getPGNFromSave() {
        String result = "";
        for (int i = 0; i < LecureSave.PGN_Explain.size(); i++) {
            result += ((i + 2) + ". " + LecureSave.PGN_Explain.get(i).PGN);
        }
        return result;
    }

    private String getExplainFromSave() {
        String result = "";

        for (int i = 0; i < LecureSave.PGN_Explain.size(); i++) {
            result += ((i + 2) + ". " + LecureSave.PGN_Explain.get(i).explain);
        }
        return result;
    }

    protected void init() {
        title = (EditText) findViewById(R.id.titleText);
        buttonNext = (ImageView) findViewById(R.id.buttonNext);
        buttonPre = (ImageView) findViewById(R.id.buttonPre);
        levleText=(EditText) findViewById(R.id.editTextLevel);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
