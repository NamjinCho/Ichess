package com.ichessprogrammer.chesseducate.Problem;

import com.ichessprogrammer.chesseducate.MyInformation;

import java.util.ArrayList;

/**
 * Created by 남지니 on 2016-08-17.
 */
public class ParameterGetter {
    static String answer="";
    static String hint="";
    static String explain="";
    public static String getParmas(ArrayList<AnswerList> list)
    {
        String parms="";
        String temp="";
        for(int i=0;i<list.size();i++)
        {
            for(int j=0;j<list.get(i).hintList.size();j++)
            {
                if(list.get(i).hintList.get(j).pos!=6)
                    hint=hint+"("+(i+1)+". "+list.get(i).hintList.get(j).pos+list.get(i).hintList.get(j).row+list.get(i).hintList.get(j).col+")"+"/";
                else if(list.get(i).hintList.get(j).pos==6)
                    hint=hint+"("+(i+1)+". "+list.get(i).hintList.get(j).pos+list.get(i).hintList.get(j).row+list.get(i).hintList.get(j).col+
                            list.get(i).hintList.get(j).row2+list.get(i).hintList.get(j).col2+")"+"/";
            }
        }
        for(int i=0;i<list.size();i++)
        {
            answer=answer+"("+(i+1)+". "+list.get(i).Answer+"/"+list.get(i).preMove+")/";
        }
        for(int i=0;i<list.size();i++)
        {
            explain=explain+"("+(i+1)+"."+list.get(i).Explain+")/";
        }
        String ID;
        if(ProblemSave.probID==-1)
             ID= MyInformation.ID;
        else
            ID=ProblemSave.prod;
        parms="fen="+ProblemSave.FEN+"&answer="+answer+"&hint="+hint+"&lecID="+ProblemSave.datas.ID+"&title="+ProblemSave.datas.Title+"&explain="+explain
        +"&id="+ ID;
        if(ProblemSave.probID!=-1)
            parms+="&probID="+ProblemSave.probID;
        answer="";
        hint="";
        explain="";
        return parms;
    }
}
