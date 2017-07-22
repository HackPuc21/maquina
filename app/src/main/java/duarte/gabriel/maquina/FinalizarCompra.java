package duarte.gabriel.maquina;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class FinalizarCompra extends AppCompatActivity {

    HashMap<Integer, String> products;
    ListView list;
    private ArrayList<HashMap<String, String>> listProducts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_compra);
        Intent intent = getIntent();
        products = (HashMap<Integer, String>) intent.getSerializableExtra("products");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list =(ListView)findViewById(R.id.lista);
        create();
    }

    private void create(){
        listProducts = new ArrayList<HashMap<String,String>>();

        HashMap<String,String> temp=new HashMap<String, String>();
        temp.put("First", "Nome");
        temp.put("Second", "Quant.");
        temp.put("Third", "R$/unid.");
        temp.put("Fourth", "Total");
        listProducts.add(temp);
        for(HashMap.Entry<Integer, String> p : products.entrySet()){
            temp=new HashMap<String, String>();
            temp.put("First", p.getValue().toString());
            temp.put("Second", p.getKey().toString());
            temp.put("Third", "R$ 0,00");
            temp.put("Fourth", "R$ 0,00");
            listProducts.add(temp);
        }


        AdapterListView adapter=new AdapterListView(this, listProducts);
        list.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.action_finalizar) {
            Toast.makeText(this, "SALVAAA", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
