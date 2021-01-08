package star.example.stargaze.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;


import star.example.stargaze.R;
import star.example.stargaze.activities.MainActivity;
import star.example.stargaze.activities.SearchActivity;
import star.example.stargaze.adapters.AllorderListAdapter;
import star.example.stargaze.adapters.OrderHistoryAdapter;

import star.example.stargaze.databinding.FragmentOrdersBinding;
import star.example.stargaze.databinding.SearchLayoutBindingImpl;
import star.example.stargaze.models.AllOrderModelData;
import star.example.stargaze.models.PastData;
import star.example.stargaze.models.response.OrderHistoryResp;
import star.example.stargaze.sharedPref.MyPreferences;
import star.example.stargaze.sharedPref.PrefConf;
import star.example.stargaze.utils.AppUtils;
import star.example.stargaze.view_presenter.OrderHistoryPresenter;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentOrders extends Fragment implements OrderHistoryPresenter.OrderHistoryView, OrderHistoryAdapter.OnOrderItemListener, View.OnClickListener{
    private FragmentOrdersBinding binding;
    private OrderHistoryPresenter presenter;
    private Dialog dialog;
    public View view;
    private Context context;
    RequestQueue requestQueue;
    String eventName,artistName;

   ArrayList<AllOrderModelData> allOrderModelData;
    CharSequence search="";
    AllOrderModelData allOrderModelData1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders,container,false);
        dialog = AppUtils.hideShowProgress(getContext());

                       allOrderModelData=new ArrayList<AllOrderModelData>();
        getdata();





        presenter = new OrderHistoryPresenter(this);
        presenter.getOrders(context);

//        binding.imgBackArrow.setOnClickListener(this);

        view = binding.getRoot();

      binding.searchTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getContext(),SearchActivity.class));

            }
        });
        return binding.getRoot();
    }




    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof MainActivity){
            this.context = context;
        }
        super.onAttach(context);
    }

    private List<PastData> getPastData(){
        List<PastData>data = new ArrayList<>();
        int [] images ={R.drawable.concert,R.drawable.concert2,R.drawable.concert3,R.drawable.concert4,R.drawable.concert,R.drawable.concert2,R.drawable.concert3,R.drawable.concert4,R.drawable.concert,R.drawable.concert2};
        String[]eventNames ={"comedy show","Shaha-Jan-Night-drama","Shane_E_Bihar","College Fun Festive","comedy show","Shaha-Jan-Night-drama","Shane_E_Bihar","College Fun Festive","comedy show","Shaha-Jan-Night-drama"};

        for(int i=0;i<images.length;i++){
            PastData pastData = new PastData(eventNames[i],"Sunday 09:00 PM","District 9- Vancouver,India",images[i],"District Presents");
            data.add(pastData);
        }
        return data;
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void showHideProgress(boolean isShow) {
        if(isShow){
            dialog.show();
        }else {
            dialog.dismiss();
        }
    }

    @Override
    public void onError(String message) {
        if(message.equalsIgnoreCase("Token Expired")){
            AppUtils.alertMessage((AppCompatActivity) context,"Your account is logged in to new device or your session is expired!");
        }else {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess(List<OrderHistoryResp> historyResp) {
      if(historyResp!=null && historyResp.size()>0 ) {
         /*   binding.recyclerOrderHistory.setLayoutManager(new GridLayoutManager(context, 1));
            binding.recyclerOrderHistory.setAdapter(new OrderHistoryAdapter(historyResp, context, this));
            binding.txtListIsEmpty.setVisibility(View.GONE);
            binding.recyclerOrderHistory.setVisibility(View.VISIBLE);
        }else {
            binding.txtListIsEmpty.setText(getResources().getString(R.string.No_Data_Found));
            binding.txtListIsEmpty.setVisibility(View.VISIBLE);
            binding.recyclerOrderHistory.setVisibility(View.GONE);*/
        }
    }

    @Override
    public void onFailure(Throwable t) {
        Snackbar.make(view,t.getMessage(),Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void onOrderItemClick() {

    }

    public void getdata(){

        String url="https://api.stargaze.digital/api/order/allWithPaymentSuccess";

        String  token = MyPreferences.getInstance(context).getString(PrefConf.KEY_LOGIN_TOKEN, "");

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object  = new JSONObject(response);
                    String order = object.getString("orders");
                    JSONArray jsonArray =new JSONArray(order);
                    for (int i=0 ; i<jsonArray.length();i++){
                        String data =jsonArray.getString(i);
                        JSONObject  object1=new JSONObject(data);

                        allOrderModelData1=new AllOrderModelData();

                        String order_no=object1.getString("orderNumber");
                        String paymentStatus=object1.getString("paymentStatus");

                        String event = object1.getString("event");

                        if(!event.equals("null")){
                            JSONObject eventDetail =new JSONObject(event);
                             eventName=eventDetail.getString("name");
                             artistName=eventDetail.getString("artist");
                            String price=eventDetail.getString("price");
                            String startDate=eventDetail.getString("startDate");
                            String image=eventDetail.getString("images");
                            JSONArray jsonArray1=new JSONArray(image);
                            for(int j=0;j<jsonArray1.length();j++){
                                String data1 =jsonArray1.getString(j);
                                JSONObject  object2=new JSONObject(data1);
                                String imageUrl = object2.getString("url");
                                allOrderModelData1.setImageurl(imageUrl);
                            }

                            allOrderModelData1.setArtistName(artistName);
                            allOrderModelData1.setEventName(eventName);
                            allOrderModelData1.setPrice(price);
                            allOrderModelData1.setStartDate(startDate);

                        }

                        String status_details=object1.getString("statusDetails");
                        JSONArray jsonArray2=new JSONArray(status_details);
                        for(int j=0;j<jsonArray2.length();j++){
                            String data1 =jsonArray2.getString(j);
                            JSONObject  object3=new JSONObject(data1);
                            String status = object3.getString("status");
                            allOrderModelData1.setStatus(status);
                        }

                        allOrderModelData1.setOrder_Number(order_no);
                        allOrderModelData1.setPayment(paymentStatus);
                        allOrderModelData.add(allOrderModelData1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(allOrderModelData!=null && allOrderModelData.size()>0 ) {
                    binding.recyclerOrderHistory.setLayoutManager(new GridLayoutManager(context, 1));

                    AllorderListAdapter allorderListAdapter = new AllorderListAdapter(allOrderModelData,context);

                    binding.recyclerOrderHistory.setAdapter(allorderListAdapter);
                    binding.txtListIsEmpty.setVisibility(View.GONE);
                    binding.recyclerOrderHistory.setVisibility(View.VISIBLE);



                    /*------------------Searching Filter---------------------
                     */
                   /* binding.searchTxt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            allorderListAdapter.getFilter().filter(s);

                            String result= s.toString();

                            if(result.equals(eventName) || result.equals(artistName) ){

                                Toast.makeText(context, ""+eventName+artistName, Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Event Not found ", Toast.LENGTH_SHORT).show();
                            }




                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });


*/
                  /*  progressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);*/









            }else {
                    binding.txtListIsEmpty.setText(getResources().getString(R.string.No_Data_Found));
                    binding.txtListIsEmpty.setVisibility(View.VISIBLE);
                    binding.recyclerOrderHistory.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                /* params.put("_id", id);*/

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization", token);

                return headers;
            }

        };
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
