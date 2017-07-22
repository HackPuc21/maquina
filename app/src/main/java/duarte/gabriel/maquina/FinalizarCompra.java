package duarte.gabriel.maquina;

import android.app.ActionBar;
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

import java.util.ArrayList;
import java.util.HashMap;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_compra);
        Intent intent = getIntent();
        products = (HashMap<String, Integer>) intent.getSerializableExtra("products");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtValue = (TextView)findViewById(R.id.valorFinal);
        txtQuantidade = (TextView)findViewById(R.id.txtQuantidade);

        list =(ListView)findViewById(R.id.lista);
        build();
        create();
    }

    private void create(){
        listProducts = new ArrayList<HashMap<String,String>>();
        int total = 0;
        HashMap<String,String> temp=new HashMap<String, String>();
        temp.put("First", "Nome");
        temp.put("Second", "Quant.");
        temp.put("Third", "R$/unid.");
        temp.put("Fourth", "Total");
        listProducts.add(temp);
        for(HashMap.Entry<String, Integer> p : products.entrySet()){
            temp=new HashMap<String, String>();
            temp.put("First", p.getKey().toString());
            temp.put("Second", p.getValue().toString());
            temp.put("Third", "R$ 0,00");
            temp.put("Fourth", "R$ 0,00");
            listProducts.add(temp);
            total += p.getValue();
        }


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
        Credentials credentials = new Credentials("2ED3O7cvHJPD", "yKEOgnhXyJAU");

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
            // Identificação do produto (Stock Keeping Unit)
            String sku = "2891820317391823";
            String name = "Coca-cola lata";

            int unitPrice = 550;
            int quantity = 3;

            order.addItem(sku, name, unitPrice, quantity, "UNIDADE");

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
            txtValue.setText("R$ " + value/100 + "," + value%100);

            orderManager.checkoutOrder(orderId, value, paymentListener);
        }
    }

    private void goBack(){
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }
}
