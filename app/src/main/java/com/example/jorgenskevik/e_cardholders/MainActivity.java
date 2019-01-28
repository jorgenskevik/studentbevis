package com.example.jorgenskevik.e_cardholders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jorgenskevik.e_cardholders.Variables.KVTVariables;
import com.example.jorgenskevik.e_cardholders.models.Unit;
import com.example.jorgenskevik.e_cardholders.remote.UserAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jorgenskevik on 21.07.2018.
 */

public class MainActivity extends Activity {
    private LinearLayout llContainer;
    private EditText etSearch;
    private ListView lvProducts;
    private int unit_id;
    private TextView is_logged_in;
    String picture;

    private static ArrayList<Unit> mProductArrayList = new ArrayList<Unit>();
    private MyAdapter adapter1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);


        is_logged_in = (TextView) findViewById(R.id.textView18);
        etSearch = (EditText) findViewById(R.id.etSearch);
        lvProducts = (ListView)findViewById(R.id.lvProducts);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    int selectedColor = Color.rgb(254, 0, 0);
                    if(!hasActiveInternetConnection()){
                        is_logged_in.setText(R.string.nonet);
                        is_logged_in.setTextColor(selectedColor);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();


        // Add Text Change Listener to EditText
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                adapter1.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
                adapter1.getFilter().filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter1.getFilter().filter(s.toString());

            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public boolean hasActiveInternetConnection() {
        if (isNetworkAvailable()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(KVTVariables.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        UserAPI userapi = retrofit.create(UserAPI.class);
        userapi.getSchools().enqueue(new Callback<List<Unit>>() {
            @Override
            public void onResponse(Call<List<Unit>> call, Response<List<Unit>> response) {
                List<Unit> unit = response.body();
                for (int i = 0; i < unit.size() ; i++) {
                    mProductArrayList.add(unit.get(i));
                }
            }

            @Override
            public void onFailure(Call<List<Unit>> call, Throwable t) {
            }
        });

        adapter1 = new MyAdapter(this, mProductArrayList);
        lvProducts.setAdapter(adapter1);
    }


    // Adapter Class
    public class MyAdapter extends BaseAdapter implements Filterable {

        private ArrayList<Unit> mOriginalValues; // Original Values
        private ArrayList<Unit> mDisplayedValues;    // Values to be displayed
        LayoutInflater inflater;

        public MyAdapter(Context context, ArrayList<Unit> mProductArrayList) {
            this.mOriginalValues = mProductArrayList;
            this.mDisplayedValues = mProductArrayList;
            inflater = LayoutInflater.from(context);
            mDisplayedValues.clear();
        }

        @Override
        public int getCount() {
            return mDisplayedValues.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            LinearLayout llContainer;
            ImageView imageView;
            TextView subtitle;
            TextView tvName;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.mylist, null);
                holder.llContainer = (LinearLayout)convertView.findViewById(R.id.llContainer);
                holder.tvName = (TextView) convertView.findViewById(R.id.mainTitle);
                holder.imageView = convertView.findViewById(R.id.icon);
                holder.subtitle = (TextView) convertView.findViewById(R.id.subtitle);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            picture = mDisplayedValues.get(position).getUnit_logo();
            holder.tvName.setText(mDisplayedValues.get(position).getName());
            holder.subtitle.setText(getResources().getString(R.string.Acr) + ": " +  mDisplayedValues.get(position).getShort_name());
            Picasso.get().load(mDisplayedValues.get(position).getUnit_logo()).into(holder.imageView);
            holder.llContainer.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    Intent intent_loggin = new Intent(MainActivity.this, LoginActivity.class);
                    unit_id = mDisplayedValues.get(position).getId();
                    intent_loggin.putExtra("unit_id_i_need", unit_id);
                    startActivity(intent_loggin);
                }
            });

            return convertView;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint,FilterResults results) {

                    mDisplayedValues = (ArrayList<Unit>) results.values; // has the filtered values
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    ArrayList<Unit> FilteredArrList = new ArrayList<Unit>();

                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<Unit>(mDisplayedValues); // saves the original data in mOriginalValues
                    }

                    /********
                     *
                     *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                     *  else does the Filtering and returns FilteredArrList(Filtered)
                     *
                     ********/
                    if (constraint == null || constraint.length() == 0) {

                        // set the Original result to return
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;
                    } else {
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < mOriginalValues.size(); i++) {
                            String data_name = mOriginalValues.get(i).getName();
                            if (data_name.toLowerCase().startsWith(constraint.toString())) {
                                FilteredArrList.add(new Unit(mOriginalValues.get(i).getName(), mOriginalValues.get(i).getUnit_logo(), mOriginalValues.get(i).getShort_name(), mOriginalValues.get(i).getId(), mOriginalValues.get(i).getShort_name()));
                            }
                        }
                        // set the Filtered result to return
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;
                    }
                    return results;
                }
            };
            return filter;
        }
    }
}