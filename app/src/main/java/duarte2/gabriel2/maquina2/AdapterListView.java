package duarte2.gabriel2.maquina2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterListView extends BaseAdapter {

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;
    TextView txtFourth;

    public AdapterListView(Activity activity,ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub



        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.layout_list_view, null);

            txtFirst=(TextView) convertView.findViewById(R.id.nome);
            txtSecond=(TextView) convertView.findViewById(R.id.quantidade);
            txtThird=(TextView) convertView.findViewById(R.id.valorUnitario);
            txtFourth=(TextView) convertView.findViewById(R.id.valorFinal);

        }

        HashMap<String, String> map=list.get(position);
        txtFirst.setText(map.get("First"));
        txtSecond.setText(map.get("Second"));
        txtThird.setText(map.get("Third"));
        txtFourth.setText(map.get("Fourth"));

        return convertView;
    }

}