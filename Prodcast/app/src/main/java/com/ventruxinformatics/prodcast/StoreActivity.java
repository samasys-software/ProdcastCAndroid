package com.ventruxinformatics.prodcast;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import businessObjects.SessionInformations;
import cz.msebera.android.httpclient.Header;

public class StoreActivity extends AppCompatActivity {

    ListView listhistory;

    Context context;



    //public static int[] prgmImages ={};                               //prev. code

    public static ArrayList<Bitmap> prgmImages=new ArrayList<Bitmap>() ;

    public static ArrayList<String> prgmNameList=new ArrayList<String>() ;
    public static ArrayList<String> prgmAddressList=new ArrayList<String>() ;
   // public static String[] prgmNameList={};       //prev. code
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        context = this;

        JSONArray[] jsonArrays;
        try {
            ArrayList<JSONObject> distributors=new ArrayList<>();
            int count=-1;
            StoreActivityTask MAuthTask = new StoreActivityTask();
            MAuthTask.execute((Void) null);
             jsonArrays = MAuthTask.get();
            if(jsonArrays==null){
                Toast.makeText(context, "It is null", Toast.LENGTH_LONG).show();
            }
            else {

                System.out.println("Length=" + jsonArrays.length);
                //JSONArray jArray = new JSONArray(jsonArrays);
                for (int i = 0; i < jsonArrays.length; i++) {
                    JSONArray list = jsonArrays[i];
                    for (int j = 0; j < list.length(); j++) {

                        System.out.println("jsonArrays=" + list);
                        JSONObject object = list.getJSONObject(j);
                        System.out.println("jsonArrays=" + object);
                        count++;
                        distributors.add(count, object);
                        prgmNameList.add(count, object.getString("companyName"));
                        prgmAddressList.add(count, object.getString("address1") + " " + object.getString("address2") + " " + object.getString("address3"));
                        System.out.println("jsonArrays=" + object.getString("companyName"));
                        String fileName = object.getString("logo");
                        String url = "http://ec2-52-91-5-22.compute-1.amazonaws.com:8080/prodcastweb/V5/images/" + fileName;
                        String[] params = {url};
                        StoreImageTask task = new StoreImageTask();
                        task.execute(params);
                        Bitmap bmp = task.get();
                        prgmImages.add(count, bmp);


                    }

                    SessionInformations.getInstance().setAllDistributors(distributors);


                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }


        listhistory = (ListView) findViewById(R.id.listOfStores);
        listhistory.setAdapter(new CustomStoreList(this, prgmNameList,prgmAddressList, prgmImages));

        listhistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                 getDistributorTask task = new getDistributorTask(position);
                Toast.makeText(context, "customerId  Ljusu", Toast.LENGTH_LONG).show();
                task.execute( (Void) null);

                try {
                    JSONObject set=task.get();
                    SessionInformations.getInstance().setDistributor(set);
                    JSONObject object = SessionInformations.getInstance().getDistributor();
                    JSONObject obj =(JSONObject) object.get("distributor");
                    Toast.makeText(context, "customerId " + object.getLong("customerId")+" distributorId"+obj.getString("distributorId"), Toast.LENGTH_LONG).show();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                Intent i = new Intent(StoreActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });


    }


    public class StoreActivityTask extends AsyncTask<Void, Void, JSONArray[]> {


       StoreActivityTask(){

        }

        @Override
        protected JSONArray[] doInBackground(Void... params) {
             final JSONArray[] jsonObject=new JSONArray[2];
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                SyncHttpClient asyncHttpClient = new SyncHttpClient();


                long accessId = SessionInformations.getInstance().getCustomerDetails().getLong("accessId");
                String url = "http://ec2-52-91-5-22.compute-1.amazonaws.com:8080/prodcast/customer/getDistributorList?accessId=" + accessId;

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

                                jsonObject[0] =  (JSONArray)response.get("distributors");
                                jsonObject[1] =  (JSONArray)response.get("distributorsPublic");


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


    public class StoreImageTask extends AsyncTask<String, Void, Bitmap> {

        private static final int IO_BUFFER_SIZE = 4 * 1024;
        //JSONArray jsonObject;

        StoreImageTask(){

        }

        @Override
        protected Bitmap doInBackground(String... url) {

                Bitmap bitmap = null;
                InputStream in = null;
                BufferedOutputStream out = null;

                try {
                    in = new BufferedInputStream(new URL(url[0]).openStream(), IO_BUFFER_SIZE);

                    final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                    out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
                    copy(in, out);
                    out.flush();

                    final byte[] data = dataStream.toByteArray();
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bitmap;






        }
        private void copy(InputStream in, OutputStream out) throws IOException {
            byte[] b = new byte[IO_BUFFER_SIZE];
            int read;
            while ((read = in.read(b)) != -1) {
                out.write(b, 0, read);
            }
        }



    }

    public class getDistributorTask extends AsyncTask<Void, Void, JSONObject> {

        private final int position;
        JSONObject jsonObject;


        getDistributorTask(int position) {
            this.position=position;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.

                SyncHttpClient asyncHttpClient = new SyncHttpClient();
                RequestParams requestParams = new RequestParams();
                 String distributorId= SessionInformations.getInstance().getAllDistributors().get(position).getString("distributorId");
                 String accessId= SessionInformations.getInstance().getCustomerDetails().getString("accessId");
                System.out.println("DistributorId="+distributorId);
                System.out.println("AccessId="+accessId);
                requestParams.put("accessId", accessId);
                requestParams.put("distributorId", distributorId);

                String url = "http://ec2-52-91-5-22.compute-1.amazonaws.com:8080/prodcast/customer/getCustomerDetails";

                asyncHttpClient.post(url, requestParams,new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try{
                            Boolean error=response.getBoolean("error");
                            System.out.println(error);
                            if(error)
                            {
                                System.out.print("fdsvgsffe");

                            }
                            else {

                                jsonObject=(JSONObject) response.get("result");

                                //SessionInformations.getInstance().setDistributor(jsonObject);
                            }

                        }
                        catch(Exception er)
                        {
                            er.printStackTrace();
                        }

                    }
                    public void onFailure(int statusCode,Header[] headers,String responseString,Throwable e){
                        e.printStackTrace();
                    }

                });
            }


            catch (Exception e) {
                e.printStackTrace();
                return jsonObject;
            }



            // TODO: register the new account here.
            return jsonObject;
        }




    }

}
