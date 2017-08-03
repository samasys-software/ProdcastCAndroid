package com.ventruxinformatics.prodcast;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import businessObjects.SessionInformations;
import cz.msebera.android.httpclient.Header;

public class BillActivity extends AppCompatActivity {
    Button newOrderPin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        newOrderPin=(Button) findViewById(R.id.newOrderPin);
        newOrderPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BillActivity.this,OrderNowActivity.class);
                startActivity(intent);
            }
            });

        //WebView mWebView = (WebView) findViewById(R.id.myWebView);
        try {
            BillActivityTask MAuthTask = new BillActivityTask();
            MAuthTask.execute((Void) null);
            JSONObject jsonArrays = MAuthTask.get();
            JSONObject distributor=(JSONObject)SessionInformations.getInstance().getDistributor().get("distributor");
            String currencySymbol=distributor.getString("currencySymbol");

            JSONArray outstandingBill=jsonArrays.getJSONArray("outstandingBill");
            if(outstandingBill.length()>0) {
                createSome(outstandingBill);
               /* StringBuilder html = new StringBuilder();


               html.append( "<html><body>" +

                        "<table border=\"1\" width=\"100%\">\n" +
                        "  <tr>\n" +
                        "    <td align=\"center\">Bill No.</td>\n" +
                        "    <td align=\"center\">Status</td>\t\t\n" +
                        "    <td align=\"center\">Bill Date</td>\t\t\n" +
                        "    <td align=\"center\">Total("+currencySymbol+")</td>\t\t\n" +
                        "    <td align=\"center\">Balance("+currencySymbol+")</td>\t\t\n" +
                        "  </tr>\n");

               System.out.println("OutstandingBill Length="+outstandingBill.length());
                for (int counter = 0; counter < outstandingBill.length(); counter++){
                    JSONObject object = outstandingBill.getJSONObject(counter);
                    System.out.println(object.getLong("billNumber"));
                    String orderStatus="NEW";
                    if(object.getString("orderStatus").equals("F")){
                        orderStatus="READY";
                    }
                   html.append( "  <tr>\n" +
                            "    <td align=\"center\"><a >"+object.getLong("billNumber")+"</a></td>\n" +
                            "    <td align=\"center\">"+orderStatus+"</td>\t\t\n" +
                            "    <td align=\"center\">"+object.getString("billDate")+"</td>\t\t\n" +
                            "    <td align=\"center\">"+object.getDouble("billAmount")+"</td>\t\t\n" +
                            "    <td align=\"center\">"+object.getDouble("outstandingBalance")+"</td>\t\t\n" +
                            "  </tr>\n" );
                }




               String myHtmlString=html.toString()+ "</table>" +
                        "</body></html>";
                //myHtmlString.concat()
                    mWebView.loadData(myHtmlString, "text/html", null);*/
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public class BillActivityTask extends AsyncTask<Void, Void, JSONObject> {

        JSONObject jsonObject=null;
        BillActivityTask(){

        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                SyncHttpClient asyncHttpClient = new SyncHttpClient();


                long customerId = SessionInformations.getInstance().getDistributor().getLong("customerId");
                long employeeId=SessionInformations.getInstance().getDistributor().getLong("employeeId");


                String url = "http://ec2-52-91-5-22.compute-1.amazonaws.com:8080/prodcast/global/customer?id="+customerId+"&employeeId="+employeeId;
                asyncHttpClient.get(url, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println(response);
                            Boolean error = response.getBoolean("error");
                            System.out.println(error);
                            if (error) {
                                // jsonObject = null;
                                System.out.print("fdsvgsffe");

                            } else {

                                jsonObject=  (JSONObject) response.get("customer");



                            }
                        } catch (Exception er) {
                            er.printStackTrace();
                        }

                    }

                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                        e.printStackTrace();
                    }

                });
            }
            catch (Exception e) {
                e.printStackTrace();
                //return false;
            }


            // TODO: register the new account here.
            return jsonObject;
        }



    }


    public void createSome(JSONArray outstandingBills){
      TableLayout ll = (TableLayout) findViewById(R.id.table);
        CheckBox checkBox;
        Button tv;


        for (int counter = 0; counter <= outstandingBills.length(); counter++){

           TableRow row= new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);

            row.setLayoutParams(lp);
            Button billNumber;
            Button status;
            if(counter==0)
            {
                //checkBox = new CheckBox(this);
              billNumber = new Button(this);
                billNumber.setText("Bill Number");
                billNumber.setClickable(false);
                billNumber.setBackgroundColor(1);
              //  row.addView(checkBox);
                row.addView(billNumber);
               status = new Button(this);
                status.setText("Status");
                status.setClickable(false);

                status.setBackgroundColor(6);
                //  row.addView(checkBox);
                row.addView(status);
                //  row.addView(checkBox);

            }
            else {
                try {
                    billNumber=new Button(this);
                    JSONObject object = outstandingBills.getJSONObject(counter - 1);
                    String s=""+object.getLong("billNumber");
                    billNumber.setText(s);
                    billNumber.setClickable(true);
                   status=new Button(this);
                    String orderStatus="NEW";
                    if(object.getString("orderStatus")=="F")
                    {
                        orderStatus="READY";
                    }
                    status.setText(orderStatus);
                    status.setClickable(false);
          /* tv = new TextView(this);
           addBtn = new ImageButton(this);
            addBtn.setImageResource(R.drawable.add);
            minusBtn = new ImageButton(this);
            minusBtn.setImageResource(R.drawable.minus);
            qty = new TextView(this);
            checkBox.setText("hello");
            qty.setText("10");
            row.addView(checkBox);
            row.addView(minusBtn);
            row.addView(qty);*/
                   // row.addView(checkBox);
                    row.addView(billNumber);
                   row.addView(status);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            ll.addView(row,counter);
        }
    }
}
