package duarte2.gabriel2.maquina2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import cielo.orders.domain.Credentials;
import cielo.orders.domain.Order;
import cielo.sdk.order.OrderManager;
import cielo.sdk.order.ServiceBindListener;
import cielo.sdk.order.payment.PaymentError;
import cielo.sdk.order.payment.PaymentListener;

public class FinalizarCompra extends AppCompatActivity {

    HashMap<String, Integer> products;
    ListView list;
    private ArrayList<HashMap<String, String>> listProducts;
    OrderManager orderManager;
    Order order;
    TextView txtValue, txtQuantidade;
    ArrayList<Produto> produtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_compra);
        Intent intent = getIntent();
        products = (HashMap<String, Integer>) intent.getSerializableExtra("products");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtValue = (TextView)findViewById(R.id.txtValor);
        txtQuantidade = (TextView)findViewById(R.id.txtQuantidade);

        list =(ListView)findViewById(R.id.lista);
        build();
        create();
    }

    private void create(){
        listProducts = new ArrayList<HashMap<String,String>>();
        produtos = new ArrayList<>();
        int total = 0;
        long val = 0;
        HashMap<String,String> temp=new HashMap<String, String>();
        temp.put("First", "Nome");
        temp.put("Second", "Qtd.");
        temp.put("Third", "R$/unid.");
        temp.put("Fourth", "Total");
        listProducts.add(temp);
        for(HashMap.Entry<String, Integer> p : products.entrySet()){
            temp=new HashMap<String, String>();
            Produto pp = getProduto(p.getKey().toString(), p.getValue());
            if(pp != null){
                temp.put("First", pp.getDescricao());
                temp.put("Second", p.getValue().toString());
                temp.put("Third", "R$ " + pp.getValor()/100 + "," + String.format("%02d", pp.getValor()%100));
                temp.put("Fourth", "R$ " + (pp.getValor() * pp.getQuantidade())/100 + "," +String.format("%02d", (pp.getValor() * pp.getQuantidade())%100));
                val += pp.getValor() * pp.getQuantidade();
                listProducts.add(temp);
                total += p.getValue();
                    produtos.add(pp);
            }
        }


        txtValue.setText("R$ " + val/100 + "," + String.format("%02d", val%100));
        AdapterListView adapter=new AdapterListView(this, listProducts);
        list.setAdapter(adapter);
        if(total == 1)
            txtQuantidade.setText(total+" item.");
        else
            txtQuantidade.setText(total+" itens.");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            goBack();
            return true;
        }
        if (item.getItemId() == R.id.action_finalizar) {
            createOrder();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void build(){
        Credentials credentials = new Credentials("9IFh9sWlm1T8", "d2mvVkMcOV2Z");
        //Credentials credentials = new Credentials("2ED3O7cvHJPD", "yKEOgnhXyJAU");

        orderManager = new OrderManager(credentials, this);
        ServiceBindListener serviceBindListener = new ServiceBindListener() {
            @Override
            public void onServiceBound() {
                // Service is bound
                order = orderManager.createDraftOrder("PEDIDO");
            }

            @Override
            public void onServiceUnbound() {
                // Service was unbound
            }
        };
        orderManager.bind(this, serviceBindListener);
    }

    private void createOrder(){
        if(order != null){
            for(Produto p : produtos)
                order.addItem(p.getId(), p.getDescricao(), p.getValor(), p.getQuantidade(), "UNIDADE");

                orderManager.placeOrder(order);
                PaymentListener paymentListener = new PaymentListener() {
                    @Override
                    public void onStart() {
                        Log.d("MinhaApp", "O pagamento começou.");
                    }

                    @Override
                    public void onPayment(@NotNull Order order) {
                        Log.d("MinhaApp", "Um pagamento foi realizado.");
                        Toast.makeText(FinalizarCompra.this, "Pagamento realizado!", Toast.LENGTH_LONG).show();
                        goBack();
                    }

                    @Override public void onCancel() {
                        Log.d("MinhaApp", "A operação foi cancelada.");
                        Toast.makeText(FinalizarCompra.this, "Pagamento não efetuado.", Toast.LENGTH_LONG).show();
                    }

                    @Override public void onError(@NotNull PaymentError paymentError) {
                        Log.d("MinhaApp", "Houve um erro no pagamento.");
                        Toast.makeText(FinalizarCompra.this, "Pagamento não efetuado.", Toast.LENGTH_LONG).show();
                    }
                };

                String orderId = order.getId();
                long value = order.getPrice();
                orderManager.checkoutOrder(orderId, value, paymentListener);

        }
    }

    private void goBack(){
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    private Produto getProduto(String id, int qtd){
        Produto p;
        if(id.trim().equals("9788501073525"))
            p = new Produto(id, "Livro", qtd, 5000);
        else if(id.trim().equals("7791293025780"))
            p = new Produto(id, "Desodorante", qtd, 700);
        else if(id.trim().equals("7891962026763"))
            p = new Produto(id, "Biscoito", qtd, 500);
        else
            p = new Produto(id, "Não encontrado", qtd, new Random().nextInt(1000));

        /*
            try{
            JSONObject jsonObject = getJSONObjectFromURL("" + id);
            //Toast.makeText(this,, Toast.LENGTH_SHORT).show();
            Log.d("JSON", jsonObject.toString());
        } catch (Exception e) {

        }
        */
        return p;
    }

    public static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();
        System.out.println("JSON: " + jsonString);

        return new JSONObject(jsonString);
    }


}
