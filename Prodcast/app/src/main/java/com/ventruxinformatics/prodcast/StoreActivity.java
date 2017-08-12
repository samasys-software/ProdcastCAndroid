package com.ventruxinformatics.prodcast;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.ventruxinformatics.prodcast.connect.ProdcastServiceManager;
import com.ventruxinformatics.prodcast.domain.AdminDTO;
import com.ventruxinformatics.prodcast.domain.CustomerLoginDTO;
import com.ventruxinformatics.prodcast.domain.CustomersLogin;
import com.ventruxinformatics.prodcast.domain.Distributor;
import com.ventruxinformatics.prodcast.domain.EmployeeDetails;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import businessObjects.SessionInformations;
import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreActivity extends AppCompatActivity {

    ListView listhistory;

    Context context;
    private static final int IO_BUFFER_SIZE = 4 * 1024;




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


        listhistory = (ListView) findViewById(R.id.listOfStores);
        final long accessId=SessionInformations.getInstance().getCustomerDetails().getAccessId();
        System.out.println(accessId);

        final ProgressDialog  mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("One Moment Please");
        mProgressDialog.show();
        Call<CustomerLoginDTO> distributorDTO = new ProdcastServiceManager().getClient().getAllDistributors(accessId);
        distributorDTO.enqueue(new Callback<CustomerLoginDTO>() {
            @Override
            public void onResponse(Call<CustomerLoginDTO> call, Response<CustomerLoginDTO> response) {
                try {
                    String responseString = null;


                    CustomerLoginDTO dto = response.body();
                    System.out.println(dto.toString());
                    if (dto.isError()) {
                        Toast.makeText(context, "Nothing To show", Toast.LENGTH_LONG).show();
                    } else {

                        ArrayList<Distributor> distributors = new ArrayList<>();
                        Distributor distributor;
                        int count = -1;
                        List<Distributor> dist = dto.getDistributors();
                        List<Distributor> distOpen = dto.getDistributorsPublic();
                        System.out.println(dist.size());
                        System.out.println(distOpen.size());

                        for (int i = 0; i < dist.size(); i++) {
                            count++;
                            distributor = dist.get(i);
                            distributors.add(count, distributor);
                            String fileName = distributor.getLogo();
                            String url = "http://ec2-52-91-5-22.compute-1.amazonaws.com:8080/prodcastweb/V5/images/" + fileName;
                            String[] params = {url};
                            StoreImageTask task=new StoreImageTask();
                            task.execute(params);
                            Bitmap bmp = task.get();


                            prgmImages.add(count, bmp);
                            prgmNameList.add(count, distributor.getCompanyName());
                            prgmAddressList.add(count, distributor.getAddress1() + " " + distributor.getAddress2() + " " + distributor.getAddress3());
                        }
                        for (int i = 0; i < distOpen.size(); i++) {
                            count++;
                            distributor = distOpen.get(i);
                            distributors.add(count, distributor);
                            String fileName = distributor.getLogo();
                            String url = "http://ec2-52-91-5-22.compute-1.amazonaws.com:8080/prodcastweb/V5/images/" + fileName;
                            String[] params = {url};
                            StoreImageTask task=new StoreImageTask();
                            task.execute(params);
                            Bitmap bmp = task.get();

                            prgmImages.add(count, bmp);
                            prgmNameList.add(count, distributor.getCompanyName());
                            prgmAddressList.add(count, distributor.getAddress1() + " " + distributor.getAddress2() + " " + distributor.getAddress3());
                        }
                        SessionInformations.getInstance().setAllDistributors(distributors);
                    }
                    listhistory.setAdapter(new CustomStoreList(StoreActivity.this, prgmNameList,prgmAddressList,prgmImages));
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    listhistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            long distributorId= SessionInformations.getInstance().getAllDistributors().get(position).getDistributorId();
                            mProgressDialog.show();
                            Call<AdminDTO<EmployeeDetails>> getCustomerDTO = new ProdcastServiceManager().getClient().getCustomerDetails(accessId,distributorId);
                            getCustomerDTO.enqueue(new Callback<AdminDTO<EmployeeDetails>>() {
                                @Override
                                public void onResponse(Call<AdminDTO<EmployeeDetails>> call, Response<AdminDTO<EmployeeDetails>> response) {
                                    String responseString = null;
                                    AdminDTO<EmployeeDetails> dto = response.body();
                                    if(dto.isError()) {

                                        Toast.makeText(context,dto.getErrorMessage(),Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        SessionInformations.getInstance().setEmployee(dto.getResult());
                                        EmployeeDetails emp=SessionInformations.getInstance().getEmployee();
                                        Toast.makeText(context, "customerId " + emp.getCustomerId()+" distributorId"+emp.getDistributor().getDistributorId(), Toast.LENGTH_LONG).show();
                                        mProgressDialog.dismiss();
                                        Intent intent=new Intent(StoreActivity.this, HomeActivity.class);
                                        startActivity(intent);



                                    }

                                }

                                @Override
                                public void onFailure(Call<AdminDTO<EmployeeDetails>> call, Throwable t) {
                                    t.printStackTrace();

                                }
                            });
                        }
                    });
                }
                catch(Exception e)
                {

                }
            }

            @Override
            public void onFailure(Call<CustomerLoginDTO> call, Throwable t) {
                t.printStackTrace();

            }
        });


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
}





