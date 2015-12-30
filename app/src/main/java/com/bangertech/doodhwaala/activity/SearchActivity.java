package com.bangertech.doodhwaala.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.TextView;

import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.adapter.FiltersAppliedProductAdapter;
import com.bangertech.doodhwaala.beans.BeanBrand;
import com.bangertech.doodhwaala.beans.BeanFilteredProduct;
import com.bangertech.doodhwaala.beans.BeanProduct;
import com.bangertech.doodhwaala.beans.BeanSearchProducts;
import com.bangertech.doodhwaala.cinterfaces.ISelectedProduct;
import com.bangertech.doodhwaala.manager.AsyncResponse;
import com.bangertech.doodhwaala.manager.MyAsynTaskManager;
import com.bangertech.doodhwaala.utils.AppUrlList;
import com.bangertech.doodhwaala.utils.ConstantVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by annutech on 29/12/2015.
 */
public class SearchActivity extends AppCompatActivity implements AsyncResponse,ISelectedProduct {

    private String searchString;
    SearchView searchView;
    private GridView gridFiltersAppliedProductList;
    private TextView no_result;
    private List<BeanFilteredProduct> lstProductsFiltersApplied=new ArrayList<BeanFilteredProduct>();
    private FiltersAppliedProductAdapter filtersAppliedProductAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setPadding(0, CUtils.getStatusBarHeight(PrivatePolicy.this), 0, 0);
        //getSupportActionBar().setTitle(data);


        no_result = (TextView) findViewById(R.id.no_result);
        gridFiltersAppliedProductList = (GridView) findViewById(R.id.gridFiltersAppliedProductList);

        filtersAppliedProductAdapter = new FiltersAppliedProductAdapter(SearchActivity.this,
                lstProductsFiltersApplied);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem search = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) SearchActivity.this.getSystemService(Context.SEARCH_SERVICE);


        if (search != null) {
            searchView = (SearchView) search.getActionView();
            searchView.setIconified(false);
            //searchView.clearFocus();
            search.expandActionView();
            setupSearchView(search);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                String text = newText.toLowerCase(Locale.getDefault());
                //FriendsFragment.adapter.getFilter().filter(text);
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                //Here u can get the value "query" which is entered in the search box.
                searchString = query.toLowerCase(Locale.getDefault());
                fetchFiltersAppliedProductList(searchString);
                return false;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupSearchView(MenuItem searchItem) {

        if (isAlwaysExpanded()) {
            searchView.setIconifiedByDefault(false);
        } else {
            searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
    }

    private void fetchFiltersAppliedProductList(String searchString)
    {

        if(searchString!=null) {
            MyAsynTaskManager myAsyncTask = new MyAsynTaskManager();
            myAsyncTask.delegate = this;
            myAsyncTask.setupParamsAndUrl("fetchProductsBasedSearch", SearchActivity.this, AppUrlList.ACTION_URL,
                    new String[]{"module", "action","search_string"},
                    new String[]{"products", "fetchProductsBasedSearch",searchString});
            myAsyncTask.execute();
        }

    }

    @Override
    public void backgroundProcessFinish(String from, String output) {
        if(from.equalsIgnoreCase("fetchProductsBasedSearch")) {
            lstProductsFiltersApplied.clear();
            if(isProductListExists(output)) {
                no_result.setVisibility(View.GONE);
                gridFiltersAppliedProductList.setVisibility(View.VISIBLE);
                parseAndFormateFilteredProductList(output);
            } else {

            }
        }
        filtersAppliedProductAdapter.notifyDataSetChanged();
    }


    private void parseAndFormateFilteredProductList(String filtersAppliedProductList)
    {

        try {
            JSONObject jsonObject = new JSONObject(filtersAppliedProductList);

            JSONArray array = jsonObject.getJSONArray("products_information");
            if (array.length() > 0) {
                BeanFilteredProduct beanFilteredProduct;
                JSONObject obj;
                for (int i = 0; i < array.length(); i++) {

                    obj = array.getJSONObject(i);
                    if (obj != null) {

                        beanFilteredProduct = new BeanFilteredProduct();
                        beanFilteredProduct.setPrice(obj.getString("price"));
                        beanFilteredProduct.setAvailable(obj.getString("available"));
                        beanFilteredProduct.setBrandId(obj.getString("brand_id"));
                        beanFilteredProduct.setDescription(obj.getString("description"));
                        beanFilteredProduct.setNoOfPurchases(obj.getString("no_of_purchases"));
                        beanFilteredProduct.setPackagingId(obj.getString("packaging_id"));
                        beanFilteredProduct.setProductId(obj.getString("product_id"));
                        beanFilteredProduct.setProductImage(obj.getString("product_image"));
                        beanFilteredProduct.setProductMappingId(obj.getString("product_mapping_id"));
                        beanFilteredProduct.setProductName(obj.getString("product_name"));
                        beanFilteredProduct.setQuantityId(obj.getString("quantity_id"));
                        beanFilteredProduct.setProductTypeId(obj.getString("product_type_id"));

                        lstProductsFiltersApplied.add(beanFilteredProduct);
                    }
                }
                gridFiltersAppliedProductList.setAdapter(filtersAppliedProductAdapter);

            }


        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        }


    }

    private boolean isProductListExists(String filtersAppliedProductList)
    {
        boolean isSuccess=false;

        try {
            JSONObject jsonObject = new JSONObject(filtersAppliedProductList);
            //if(jsonObject.getString("result").equalsIgnoreCase("true"))
            if(jsonObject.getBoolean("result"))
            {
                if (Integer.parseInt(jsonObject.getString("no_of_products"))>0)
                    return true;
            }
            else {
                no_result.setVisibility(View.VISIBLE);
                gridFiltersAppliedProductList.setVisibility(View.GONE);
            }
        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            isSuccess=false;
        }

        return false;
    }

    @Override
    public void onSelectProduct(String productId, String productMappingId) {
        Intent intent=new Intent(SearchActivity.this, ProductDetail.class);
        intent.putExtra(ConstantVariables.PRODUCT_ID_KEY,productId);
        intent.putExtra(ConstantVariables.PRODUCT_MAPPING_ID_KEY,productMappingId);
        startActivity(intent);

    }

    protected boolean isAlwaysExpanded() {
        return true;
    }
}
