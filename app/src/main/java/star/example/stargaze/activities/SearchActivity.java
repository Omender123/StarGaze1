package star.example.stargaze.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import okhttp3.ResponseBody;
import star.example.stargaze.R;

import star.example.stargaze.adapters.SearchEventAdapter;
import star.example.stargaze.databinding.ActivitySearchBinding;
import star.example.stargaze.models.AllOrderModelData;
import star.example.stargaze.models.SearchModalData;
import star.example.stargaze.models.request.Filters;
import star.example.stargaze.models.request.SearchReqBody;
import star.example.stargaze.models.response.EventResp;
import star.example.stargaze.sharedPref.MyPreferences;
import star.example.stargaze.sharedPref.PrefConf;
import star.example.stargaze.utils.AppUtils;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivitySearchBinding binding;
    private Context context;
    private View view;
    private Dialog dialog;
    private String search_txt;
    ArrayList<SearchModalData> searchModalData;
    KProgressHUD progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_search);
        context = SearchActivity.this;

        searchModalData =new ArrayList<SearchModalData>();
        initControl();
    }

    private void initControl() {
        binding.ivBack.setOnClickListener(this);

        binding.etSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search,0,0,0);
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
               // updateUI(false, binding.progressBar);
            }


            @Override
            public void afterTextChanged(final Editable s) {

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        search_txt = s.toString();
                        Filters searchFilters = new Filters(search_txt);
                        SearchReqBody requestBody = new SearchReqBody(searchFilters);

                        if(this != null)
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (binding.etSearch.getText().length() > 0) {
                                        setDataWhenEventPresent();
                                        saveToServerDB(search_txt);
                                     //   searching(SearchActivity.this, binding.progressBar, requestBody);
                                    }else setDataWhenClinicNotPresent();
                                }
                            });

                    }

                }, 2500);


            }
        });
    }







/*
    public void searching(final Context context, final ProgressBar progress_bar, SearchReqBody searchRequestBody) {

        if (AppUtils.isNetworkConnected(context)) {
//            updateUI(false, progress_bar);
            Call<EventResp> call = AppUtils.getApi(context).searchEvents(searchRequestBody);

            call.enqueue(new Callback<EventResp>() {
                @Override
                public void onResponse(Call<EventResp> call, Response<EventResp> response) {
                    updateUI(true, progress_bar);
                    String  s=null;
                    if (response.isSuccessful() && response.body() != null && response.code() == 200) {

                        if (response.body() != null && response.body().getEvents().size() > 0) {





                            binding.rvList.setLayoutManager(new GridLayoutManager(SearchActivity.this,1));
                            binding.rvList.setAdapter(new SearchEventAdapter(response.body().getEvents(),context,listener ));

                             } else setDataWhenClinicNotPresent();

                    } else {
                        setDataWhenClinicNotPresent();
                        Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<EventResp> call, Throwable t) {
                    updateUI(true, binding.progressBar);
                    call.cancel();
                    Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();

                }
            });
        }else{
            Toast.makeText(context,getString(R.string.txt_plz_connect_internet), Toast.LENGTH_SHORT).show();
        }

    }
*/


    public  void updateUI(Boolean isEnable, ProgressBar progressBar) {
        if (isEnable)
            progressBar.setVisibility(View.GONE);
        else
            progressBar.setVisibility(View.VISIBLE);
        if (isEnable) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            );
        }
    }




    private void saveToServerDB(String search) {

      progressDialog = KProgressHUD.create(SearchActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call =  AppUtils.getApi(context).EventSearch(search);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
            hidepDialog();

                if (response.code() == 200) {
                    try {
                        s=response.body().string();

                        JSONArray jsonArray =new JSONArray(s);
                        for (int i=0;i<=jsonArray.length();i++){
                            String data =jsonArray.getString(i);
                            JSONObject  object1=new JSONObject(data);

                            SearchModalData searchModalData1 = new SearchModalData();
                            String eventName=object1.getString("name");
                            String artistName=object1.getString("artist");
                            String image=object1.getString("images");
                            JSONArray jsonArray1=new JSONArray(image);
                            for(int j=0;j<jsonArray1.length();j++){
                                String data1 =jsonArray1.getString(j);
                                JSONObject  object2=new JSONObject(data1);
                                String imageUrl = object2.getString("url");
                               searchModalData1.setImageUrl(imageUrl);
                               // Toast.makeText(SearchActivity.this, ""+imageUrl, Toast.LENGTH_SHORT).show();
                            }

                            searchModalData1.setArtistName(artistName);
                            searchModalData.add(searchModalData1);

                        }




                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    if (searchModalData !=null && searchModalData.size()>0){

                        binding.rvList.setLayoutManager(new GridLayoutManager(SearchActivity.this,1));
                        binding.rvList.setAdapter(new SearchEventAdapter(searchModalData,context));

                    }else{
                        setDataWhenClinicNotPresent();
                        Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }

                } else if (response.code() == 400) {

                     Toast.makeText(SearchActivity.this, "Event Not Found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
              hidepDialog();
                Toast.makeText(SearchActivity.this, "net", Toast.LENGTH_SHORT).show();

            }
        });

    }


        private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
    public void setDataWhenClinicNotPresent(){
        binding.rvList.setVisibility(View.GONE);
//        tv.setText(ViewUtils.dataInSelectedLanguage(this,data.getTxt_no_record_found().getEn(),data.getTxt_no_record_found().getAr()));
        //tv.setText("No record found.");
        updateUI(true,binding.progressBar);
        binding.tv.setVisibility(View.VISIBLE);
    }

    public void setDataWhenEventPresent(){
        binding.rvList.setVisibility(View.VISIBLE);
        binding.tv.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

   /* SearchEventAdapter.OnEventItemListener listener = new SearchEventAdapter.OnEventItemListener() {
        @Override
        public void onEventItemClick(List<SearchModalData> data, int position) {
            Gson gson = new Gson();
            String eventListStr = gson.toJson(data);

            MyPreferences.getInstance(context).putString(PrefConf.EVENTLISTSTR,eventListStr);

            Intent intent = new Intent(context, EventDetailsActivity.class);
            intent.putExtra("pos",position);
            intent.putExtra("isFromHomeItemClick",true);
            startActivity(intent);
        }
    };*/
}
