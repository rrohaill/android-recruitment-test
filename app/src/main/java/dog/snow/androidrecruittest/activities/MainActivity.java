package dog.snow.androidrecruittest.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import dog.snow.androidrecruittest.Models.DataModel;
import dog.snow.androidrecruittest.R;
import dog.snow.androidrecruittest.adapters.DatabaseHelper;
import dog.snow.androidrecruittest.adapters.NetworkCalls;
import dog.snow.androidrecruittest.adapters.RecyclerViewAdapter;
import dog.snow.androidrecruittest.interfaces.JSONCommunicationManager;
import dog.snow.androidrecruittest.utils.Constants;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, Filterable {

    private SwipeRefreshLayout swipeView;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private TextView tvNoItem;
    private ArrayList<DataModel> dataModelList;
    private ArrayList<DataModel> filterList;
    private RecyclerViewAdapter recyclerViewAdapter;
    private DatabaseHelper db;
    private ValueFilter valueFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        initView();
        initSearch();
        checkDatabase();
    }

    private void checkDatabase() {
        try {
            dataModelList = db.selectAllData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dataModelList != null && dataModelList.size() > 0) {
            filterList = dataModelList;
            initAdapter();
        } else {
            showLoading();
            fetchData();
        }
    }

    private void fetchData() {
        new NetworkCalls(new JSONCommunicationManager() {
            @Override
            public void onResponse(String response, JSONCommunicationManager jsonCommunicationManager) {
                try {
                    Log.i("Response :", response);
                    JSONObject jsonObject;// = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray(response);
                    Gson gson = new Gson();

                    DataModel dataModel;
                    dataModelList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        dataModel = gson.fromJson(jsonObject.toString(), DataModel.class);
                        dataModelList.add(dataModel);
                    }

                    filterList = dataModelList;

                    hideLoading();

                    onProcessNext(null);
                } catch (Exception e) {
                    hideLoading();
                    e.printStackTrace();
                    swipeView.setRefreshing(false);
                }
            }

            @Override
            public void onProcessNext(ArrayList<Object> listObject) {
                db.dropDataTable();
                db.insertAllData(dataModelList);
                initAdapter();
                swipeView.setRefreshing(false);
            }

            @Override
            public void onPreRequest() {
            }

            @Override
            public void onError(String s) {
                hideLoading();
                showAlert(getString(R.string.title_alert), s);
                swipeView.setRefreshing(false);
            }
        }, this).execute(Constants.items);
    }

    private void initAdapter() {
        recyclerViewAdapter = new RecyclerViewAdapter(filterList, this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

        tvNoItem.setVisibility(View.INVISIBLE);
    }

    private void initView() {
        swipeView = findViewById(R.id.swipe_view);
        recyclerView = findViewById(R.id.items_rv);
        tvNoItem = findViewById(R.id.empty_list_tv);
        swipeView.setOnRefreshListener(this);
    }

    private void initSearch() {
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint(getString(R.string.title_search));

        searchView.setFocusableInTouchMode(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    @Override
    public void onRefresh() {
        fetchData();
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                filterList = new ArrayList<>();
                for (int i = 0; i < dataModelList.size(); i++) {
                    if ((dataModelList.get(i).getName().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(dataModelList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                filterList = dataModelList;
                results.count = dataModelList.size();
                results.values = dataModelList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            initAdapter();
        }

    }
}
