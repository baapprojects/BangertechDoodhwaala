package com.bangertech.doodhwaala.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bangertech.doodhwaala.manager.AsyncResponse;
import com.bangertech.doodhwaala.manager.DialogManager;
import com.bangertech.doodhwaala.manager.MyAsynTaskManager;
import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.manager.PreferenceManager;
import com.bangertech.doodhwaala.utils.AppUrlList;
import com.bangertech.doodhwaala.utils.CGlobal;
import com.bangertech.doodhwaala.utils.CUtils;
import com.bangertech.doodhwaala.utils.ConstantVariables;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NumberRule;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by annutech on 9/23/2015.
 */
public class AddEditAddress extends AppCompatActivity implements AsyncResponse, Validator.ValidationListener{
    private Toolbar app_bar;
    private List<BeanCity> bucketCityAndLocality = new ArrayList<BeanCity>();
    private List<BeanLocality> bucketLocality = new ArrayList<BeanLocality>();
    private Spinner spCity, spLocality;

    /*@Required(order = 1)
    @NumberRule(order = 2, message = "Enter Flat Number in Numeric", type = NumberRule.NumberType.LONG)
    @TextRule(order = 3, message = "Enter valid Flat Number", minLength = 1, maxLength = 5)*/
    private EditText editTextFlat;

    @Required(order = 1)
    @TextRule(order = 2, minLength = 2, message = "Enter valid Building name with minimum 2 chars")
    private EditText editTextBuilding;

    @Required(order = 3)
    @TextRule(order = 4, minLength = 2, message = "Enter valid Street name with minimum 2 chars")
    private EditText editTextStreet;

    /*@Required(order = 8)
    @TextRule(order = 9, minLength = 2, message = "Enter valid Landmark name with minimum 2 chars")*/
    private EditText editTextLandmark;

    @Required(order = 5)
    @NumberRule(order = 6, message = "Enter Pin code in Numeric", type = NumberRule.NumberType.LONG)
    @TextRule(order = 7, message = "Enter valid Pin code Number", minLength = 6, maxLength = 6)
    private EditText editTextPincode;
    private CityAndLocalityAdapter localityAdapter;

    private static final int LIST_CITIES = 0;
    private static final int LIST_LOCALITIES = 1;
    private boolean isNewAddress=true;
    private String address_id="";
    private int localityIndex=0;
    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_address);

        validator = new Validator(this);
        validator.setValidationListener(this);

        app_bar = (Toolbar) findViewById(R.id.app_bar);
        spCity = (Spinner) findViewById(R.id.spCity);
        spLocality = (Spinner) findViewById(R.id.spLocality);

        editTextFlat = (EditText) findViewById(R.id.editTextFlat);
        editTextBuilding = (EditText) findViewById(R.id.editTextBuilding);
        editTextStreet = (EditText) findViewById(R.id.editTextStreet);
        editTextLandmark = (EditText) findViewById(R.id.editTextLandmark);
        editTextPincode = (EditText) findViewById(R.id.editTextPincode);

        setSupportActionBar(app_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);
        getSupportActionBar().setTitle(R.string.addresses);

        ((Button) app_bar.findViewById(R.id.butSave)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        editTextPincode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    validator.validate();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;
            }

        });

        bucketCityAndLocality.clear();
        bucketLocality.clear();
        isNewAddress=getIntent().getExtras().getBoolean("ADD_ADDRESS");
          //  CUtils.printLog("ADD_ADDRESS",getIntent().getExtras().toString(), ConstantVariables.LOG_TYPE.ERROR);
        if(!isNewAddress)
            address_id=getIntent().getExtras().getString("ADDRESS_ID");

        fetchCitiesAndLocalities();


    }

  private void fetchAddressDetailFromServer()
  {
      MyAsynTaskManager myAsyncTask = new MyAsynTaskManager();
      myAsyncTask.delegate = this;
      myAsyncTask.setupParamsAndUrl("fetchAddressDetails", AddEditAddress.this, AppUrlList.ACTION_URL,
              new String[]{"module", "action","address_id"},
              new String[]{"user", "fetchAddressDetails",address_id});
      myAsyncTask.execute();
  }
    private void addEditCityOnServer()
    {
        if(isInputValid()) {
            addEditAddressOnServer();
        }

    }

    private void addEditAddressOnServer() {
        int cityIndex = spCity.getSelectedItemPosition();
        int localityIndex = spLocality.getSelectedItemPosition();

        MyAsynTaskManager myAsyncTask = new MyAsynTaskManager();
        myAsyncTask.delegate = this;
        myAsyncTask.setupParamsAndUrl("addOrEditAddress", AddEditAddress.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action","new_address","address_id","user_id","city_id","locality_id","flat_number",
                        "building_or_society_name","street_details","landmark","pincode"},
                new String[]{"user", "addOrEditAddress",isNewAddress?"true":"false",address_id, PreferenceManager.getInstance().getUserId(),
                        bucketCityAndLocality.get(cityIndex).getCityId(),bucketLocality.get(localityIndex).getLocalityId(),
                        editTextFlat.getText().toString(),
                        editTextBuilding.getText().toString(),
                        editTextStreet.getText().toString(),
                        editTextLandmark.getText().toString(),
                        editTextPincode.getText().toString()});
        myAsyncTask.execute();


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isInputValid()
    {

         boolean isValid=true;
        if(TextUtils.isEmpty(editTextFlat.getText().toString())) {
            editTextFlat.requestFocus();
            editTextFlat.setError(getString(R.string.please_enter_flat_or_house_number));
            isValid=false;
        }
        if(TextUtils.isEmpty(editTextBuilding.getText().toString())) {
            editTextBuilding.setError(getString(R.string.please_enter_building_or_society_name));
            isValid=false;
        }
        if(TextUtils.isEmpty(editTextStreet.getText().toString())) {
            editTextStreet.setError(getString(R.string.please_enter_street_details));
            isValid=false;
        }
        /*if(TextUtils.isEmpty(editTextLandmark.getText().toString())) {
            editTextLandmark.setError(getString(R.string.please_enter_landmarks));
            isValid=false;
        }*/
        if(TextUtils.isEmpty(editTextPincode.getText().toString())) {
            editTextPincode.setError(getString(R.string.please_enter_pincode));
            isValid=false;
        }

        return isValid;

    }

    private void fetchCitiesAndLocalities() {

        MyAsynTaskManager myAsyncTask = new MyAsynTaskManager();
        myAsyncTask.delegate = this;
        myAsyncTask.setupParamsAndUrl("fetchCitiesAndLocalities", AddEditAddress.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action"},
                new String[]{"user", "fetchCitiesAndLocalities"});
        myAsyncTask.execute();


    }
    private void showAddressDetail(String output)
    {
        if(output!=null) {
            try {
                JSONObject object = new JSONObject(output);
                // CUtils.printLog("AddressDetail",output, ConstantVariables.LOG_TYPE.ERROR);
                if (object.getBoolean("result")) {
                    JSONArray jsonArrayAddress = new JSONArray(object.getString("address_details"));
                    if (jsonArrayAddress.length() > 0) {
                        JSONObject objectAddress = jsonArrayAddress.getJSONObject(0);

                        editTextFlat.setText(objectAddress.getString("flat_number"));
                        editTextBuilding.setText(objectAddress.getString("building_or_society_name"));
                        editTextStreet.setText(objectAddress.getString("street_details"));
                        editTextLandmark.setText(objectAddress.getString("landmark"));
                        editTextPincode.setText(objectAddress.getString("pincode"));
                        String cityID = objectAddress.getString("city_id");
                        String localityId = objectAddress.getString("locality_id");


                        // spCity.setSelection(Integer.parseInt(cityID));
                        int citySize = bucketCityAndLocality.size();
                        for (int index = 0; index < citySize; index++)
                            if (bucketCityAndLocality.get(index).city_id.equalsIgnoreCase(cityID)) {
                                spCity.setSelection(index);
                                displayCityAndLocalityOnEdit(index, localityId);
                                break;
                            }
                    }

                }

            } catch (Exception e) {

            }
        } else {
            DialogManager.showDialog(AddEditAddress.this, "Server Error Occurred! Try Again!");
        }
    }
    private void displayCityAndLocalityOnEdit(int cityIndex,String localityId)
    {

        BeanLocality beanLocality=null;

        int bucketLength = bucketCityAndLocality.get(cityIndex).getLocality().size();
        bucketLocality.clear();

        for (int size = 0; size < bucketLength; size++) {
            beanLocality=bucketCityAndLocality.get(cityIndex).getLocalityByIndex(size);
            bucketLocality.add(beanLocality);

            if(beanLocality.getLocalityId().equalsIgnoreCase(localityId))
                localityIndex=size;

        }

        //spCity.setSelection(cityIndex);
        if (bucketLocality.size() > 0) {
            localityAdapter.notifyDataSetChanged();
        }
        spCity.setSelection(cityIndex);
        spLocality.setSelection(localityIndex);

    }

    @Override
    public void backgroundProcessFinish(String from, String output) {
        CUtils.printLog(from,output, ConstantVariables.LOG_TYPE.ERROR);
        if (from.equalsIgnoreCase("fetchCitiesAndLocalities")) {
            parseFetchCitiesAndLocalities(output);
            if(!isNewAddress)
                fetchAddressDetailFromServer();

        }
        if (from.equalsIgnoreCase("fetchAddressDetails"))
            showAddressDetail(output);

        if (from.equalsIgnoreCase("addOrEditAddress")) {
            if(output!=null) {
                try {
                    JSONObject object = new JSONObject(output);
                    if (object.getBoolean("result")) {
                        if (isNewAddress)
                            Toast.makeText(AddEditAddress.this, getString(R.string.new_address_added_successfully), Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(AddEditAddress.this, getString(R.string.address_updated_successfully), Toast.LENGTH_SHORT).show();

                        setResult(RESULT_OK);
                        AddEditAddress.this.finish();
                    } else
                        Toast.makeText(AddEditAddress.this, object.getString("msg"), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                }
            } else {
                DialogManager.showDialog(AddEditAddress.this, "Server Error Occurred! Try Again!");
            }
            }



    }

    private void parseFetchCitiesAndLocalities(String output) {
        if(output!=null) {
            try {
                JSONObject jsonObject = new JSONObject(output);
                if (jsonObject.getBoolean("result")) {
                    BeanCity beanCity;
                    BeanLocality beanLocality;
                    JSONObject jsonCity, jsonLocalities, jsonLocality;
                    JSONArray jsonArrayLocalities, jsonArrayLocalitiesInformation;

                    JSONArray jsonArrayCities = new JSONArray(jsonObject.getString("cities_and_localities"));
                    if (jsonArrayCities.length() > 0) {
                        //FETCH CITIES
                        int citySize = jsonArrayCities.length();
                        for (int index = 0; index < citySize; index++) {
                            jsonCity = jsonArrayCities.getJSONObject(index);
                            beanCity = new BeanCity();
                            beanCity.setCityId(jsonCity.getString("city_id"));
                            beanCity.setCityName(jsonCity.getString("city_name"));
                            jsonLocalities = new JSONObject(jsonCity.getString("localities"));
                            //CHECK LOCALITY EXISTS
                            if (jsonLocalities.getBoolean("result")) {
                                jsonArrayLocalitiesInformation = new JSONArray(jsonLocalities.getString("localities_information"));
                                int localitySize = jsonArrayLocalitiesInformation.length();
                                JSONObject obj = null;
                                for (int i = 0; i < localitySize; i++) {
                                    obj = jsonArrayLocalitiesInformation.getJSONObject(i);
                                    beanLocality = new BeanLocality();
                                    beanLocality.setLocalityId(obj.getString("locality_id"));
                                    beanLocality.setLocalityName(obj.getString("locality_name"));
                                    beanCity.setLocality(beanLocality);
                                }

                            }
                            bucketCityAndLocality.add(beanCity);
                        }
                    }
                }
            } catch (Exception e) {

            }
        } else {
            DialogManager.showDialog(AddEditAddress.this, "Server Error Occurred! Try Again!");
        }

        int bucketLength = bucketCityAndLocality.size();
        if (bucketLength > 0) {
            spCity.setAdapter(new CityAndLocalityAdapter(LIST_CITIES));
            localityAdapter = new CityAndLocalityAdapter(LIST_LOCALITIES);
            spLocality.setAdapter(localityAdapter);
            intiBucketLocality(0);
            spCity.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(
                                AdapterView<?> parent, View view, int position, long id) {
                            intiBucketLocality(position);
                        }

                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
        }
    }

    private void intiBucketLocality(int cityIndex) {
        BeanLocality beanLocality=null;

        int bucketLength = bucketCityAndLocality.get(cityIndex).getLocality().size();
        bucketLocality.clear();

        for (int size = 0; size < bucketLength; size++) {
            beanLocality=bucketCityAndLocality.get(cityIndex).getLocalityByIndex(size);
                bucketLocality.add(beanLocality);

        }

        if (bucketLocality.size() > 0) {
            localityAdapter.notifyDataSetChanged();

        }


    }

    @Override
    public void onValidationSucceeded() {
        if(editTextFlat.getText().toString().matches("^[a-zA-Z0-9_ ]*$")) {
            if (editTextBuilding.getText().toString().matches("^[a-zA-Z0-9_ ]*$")) {

                if (editTextStreet.getText().toString().matches("^[a-zA-Z0-9_ ]*$")) {

                    if (editTextLandmark.getText().toString().matches("^[a-zA-Z0-9_ ]*$")) {
                        addEditCityOnServer();
                    } else {
                        editTextLandmark.requestFocus();
                        editTextLandmark.setError("Enter alphanumeric chars only");
                    }
                } else {
                    editTextStreet.requestFocus();
                    editTextStreet.setError("Enter alphanumeric chars only");
                }
            } else {
                editTextBuilding.requestFocus();
                editTextBuilding.setError("Enter alphanumeric chars only");
            }
        } else {
            editTextFlat.requestFocus();
            editTextFlat.setError("Enter alphanumeric chars only");
        }


    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();
        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
            //((EditText) failedView).setError(Html.fromHtml("<font color='black'>" + message + "</font>"));
        }

    }

    class BeanCity {
        private String city_id;
        private String city_name;
        private List<BeanLocality> localities = new ArrayList<BeanLocality>();

        public String getCityId() {
            return city_id;
        }

        public void setCityId(String cityId) {
            this.city_id = cityId;
        }


        public String getCityName() {
            return city_name;
        }

        public void setCityName(String cityName) {
            this.city_name = cityName;
        }


        public List<BeanLocality> getLocality() {
            return localities;
        }

        public void setLocality(BeanLocality locality) {
            this.localities.add(locality);
        }

        public BeanLocality getLocalityByIndex(int index) {
            return localities.get(index);
        }


        BeanCity() {
            localities.clear();

        }

    }

    class BeanLocality {
        private String locality_id;
        private String locality_name;
        public String getLocalityId() {
            return locality_id;
        }

        public void setLocalityId(String localityId) {
            this.locality_id = localityId;
        }


        public String getLocalityName() {
            return locality_name;
        }

        public void setLocalityName(String localityName) {
            this.locality_name = localityName;
        }


    }

    class CityAndLocalityAdapter extends BaseAdapter {
        int _listType = 0;

        CityAndLocalityAdapter(int listType) {
            _listType = listType;
        }


        @Override
        public int getCount() {
            int size = 0;
            if (_listType == LIST_CITIES)
                size = bucketCityAndLocality.size();
            if (_listType == LIST_LOCALITIES)
                size = bucketLocality.size();
            return size;
        }

        @Override
        public Object getItem(int position) {
            Object object = null;
            if (_listType == LIST_CITIES)
                object = bucketCityAndLocality.get(position);
            if (_listType == LIST_LOCALITIES)
                object = bucketLocality.get(position);

            return object;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(AddEditAddress.this).inflate(R.layout.row_city_locality, parent, false);
            TextView txtViewCityLocality = (TextView) view.findViewById(R.id.txtViewCityLocality);

            if (_listType == LIST_CITIES)
                txtViewCityLocality.setText(bucketCityAndLocality.get(position).getCityName());
            if (_listType == LIST_LOCALITIES)
                txtViewCityLocality.setText(bucketLocality.get(position).getLocalityName());


            return view;
        }
    }


}
