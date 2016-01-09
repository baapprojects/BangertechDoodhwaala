package com.bangertech.doodhwaala.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bangertech.doodhwaala.manager.AsyncResponse;
import com.bangertech.doodhwaala.manager.DialogManager;
import com.bangertech.doodhwaala.manager.MyAsynTaskManager;
import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.utils.AppUrlList;
import com.bangertech.doodhwaala.utils.CUtils;
import com.bangertech.doodhwaala.utils.ConstantVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by annutech on 10/8/2015.
 */
public class ProductDetail extends AppCompatActivity implements AsyncResponse {
    //private Toolbar app_bar;

    private TextView txtViewProductName,txtViewProductPrice,txtViewProductDesc, txtViewtitleTwo, txtViewRecoDesc;
    private ImageView imageViewProduct;
    private RecyclerView recyclerViewPackaging,recyclerViewPackQty,recyclerViewAttribute;
    private RecyclerView.LayoutManager mLayoutManagerPackaging,mLayoutManagerPackQty,mLayoutManagerAttribute;
    private List<BeanPackagingAndQty> bucketPackagingAndQty=new ArrayList<BeanPackagingAndQty>();
    private List<BeanPackagingAndQty> bucketPackaging=new ArrayList<BeanPackagingAndQty>();
    private List<BeanPackagingAndQty> optionsBucket=new ArrayList<BeanPackagingAndQty>();

    private static final int PACKAGING_LIST=0;
    private static final int PACK_QUANTITY_LIST=1;
    private String productId,productName,brandId,productTypeId,productDescription, productReco, productPrice="38.00",productMappingId, productImageUrl,quantityId;
    private PackagingAndQtyAdapter bucketPackagingAdapter, bucketPackagingAndQtyAdapter;
    private BeanPackagingAndQty  beanPackagingAndQtyDefault;
    private TextView txtViewPackaging, txtViewtitle, tviCurrency;
    private ImageView ic_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_detail);
       // txtViewSelectedQuantity = (TextView) findViewById(R.id.txtViewSelectedQuantity);
        txtViewProductName = (TextView) findViewById(R.id.txtViewProductName);
        txtViewProductPrice = (TextView) findViewById(R.id.txtViewProductPrice);
        txtViewProductDesc = (TextView) findViewById(R.id.txtViewProductDesc);
        txtViewPackaging = (TextView) findViewById(R.id.txtViewPackaging);
        tviCurrency      = (TextView) findViewById(R.id.tviCurrency);
        txtViewtitle     = (TextView) findViewById(R.id.txtViewtitle);
        imageViewProduct = (ImageView) findViewById(R.id.imageViewProduct);
        ic_close = (ImageView) findViewById(R.id.ic_close);
        txtViewtitleTwo = (TextView) findViewById(R.id.txtViewtitleTwo);
        txtViewRecoDesc = (TextView) findViewById(R.id.txtViewRecoDesc);


        recyclerViewPackaging = (RecyclerView) findViewById(R.id.recyclerViewPackaging);
        recyclerViewPackQty = (RecyclerView) findViewById(R.id.recyclerViewPackQty);
        recyclerViewAttribute = (RecyclerView) findViewById(R.id.recyclerViewAttribute);


        recyclerViewPackaging.setHasFixedSize(true);
        mLayoutManagerPackaging = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPackaging.setLayoutManager(mLayoutManagerPackaging);
        bucketPackagingAdapter=new PackagingAndQtyAdapter(bucketPackaging, PACKAGING_LIST);
        recyclerViewPackaging.setAdapter(bucketPackagingAdapter);

        recyclerViewPackQty.setHasFixedSize(true);
        mLayoutManagerPackQty = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPackQty.setLayoutManager(mLayoutManagerPackQty);
        bucketPackagingAndQtyAdapter=new PackagingAndQtyAdapter(bucketPackagingAndQty, PACK_QUANTITY_LIST);
        recyclerViewPackQty.setAdapter(bucketPackagingAndQtyAdapter);


        recyclerViewAttribute.setHasFixedSize(true);
        mLayoutManagerAttribute = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewAttribute.setLayoutManager(mLayoutManagerAttribute);

        ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /*app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cross_black);*/

        productId=getIntent().getStringExtra(ConstantVariables.PRODUCT_ID_KEY);
        productMappingId=getIntent().getStringExtra(ConstantVariables.PRODUCT_MAPPING_ID_KEY);
        if((!TextUtils.isEmpty(productId))&&(!TextUtils.isEmpty(productMappingId)))
            fetchProductDetailFromServer(productId,productMappingId);

        /*((Button) findViewById(R.id.butDecreaseQty)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedQuantity>1) {
                    --selectedQuantity;
                    txtViewSelectedQuantity.setText(String.valueOf(selectedQuantity));
                }

            }
        });
        ((Button) findViewById(R.id.butIncreaseQty)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++selectedQuantity;
                txtViewSelectedQuantity.setText(String.valueOf(selectedQuantity));
            }
        });*/
       // initForTesting();

        ((FloatingActionButton) findViewById(R.id.fabSubscribe)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSubscribe(v);
            }
        });
    }

    public void gotoSubscribe(View view)
    {
        try {

                JSONObject obj = new JSONObject();
                obj.put("product_name",productName);
                obj.put("product_id",productId);
                obj.put("product_mapping_id",productMappingId);
                obj.put("quantity_id",beanPackagingAndQtyDefault.getQuantityId());
                obj.put("product_price",beanPackagingAndQtyDefault.getPrice());
                obj.put("product_image_url",beanPackagingAndQtyDefault.getProductImage());
                startActivity(new Intent(ProductDetail.this, ShowQuantity.class).putExtra(ConstantVariables.SELECTED_USER_PLAN_KEY,obj.toString()).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                finish();
        }
        catch(Exception e)
        {

        }
    }
     private void fetchProductDetailFromServer( String productId,String  productMappingId)
     {
           MyAsynTaskManager myAsyncTask = new MyAsynTaskManager();
           myAsyncTask.delegate = this;
           myAsyncTask.setupParamsAndUrl("fetchProductDetails", ProductDetail.this, AppUrlList.ACTION_URL,
                    new String[]{"module", "action","product_id","product_mapping_id"},
                    new String[]{"products", "fetchProductDetails",productId,productMappingId});
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

    @Override
    public void backgroundProcessFinish(String from, String output) {
        if(from.equalsIgnoreCase("fetchProductDetails")) {
            if(output!=null) {
                parseProductDetails(output);
            } else {
                DialogManager.showDialog(ProductDetail.this, "Server Error Occurred! Try Again!");
            }
           /* bucketPackagingAdapter.notifyDataSetChanged();
            bucketPackagingAndQtyAdapter.notifyDataSetChanged();*/
        }

    }

    private  void parseProductDetails(String output)
    {
        List<BeanProductAttribute> bucketAttribute=new ArrayList<BeanProductAttribute>();
        try {
            JSONObject jsonObject = new JSONObject(output);
            /*if (jsonObject.getString("result").equalsIgnoreCase("true"))*/
            if (jsonObject.getBoolean("result"))
            {
                //productName,brandId,productTypeId,productDescription;
                JSONObject jsonObjectProduct=jsonObject.getJSONObject("product_information");
                productName=jsonObjectProduct.getString("product_name");
                brandId=jsonObjectProduct.getString("brand_id");
                productTypeId=jsonObjectProduct.getString("product_type_id");
                productDescription=jsonObjectProduct.getString("description");
                productReco = jsonObjectProduct.getString("recommended_for");

                //INITIALIZE PRODUCT  ATTRIBUTE
                JSONObject jsonObjectProductStickers=new JSONObject(jsonObjectProduct.getString("product_stickers"));
                CUtils.printLog("attributes",jsonObjectProductStickers.toString(), ConstantVariables.LOG_TYPE.ERROR);
               //* if (jsonObjectProductAttribute.getString("result").equalsIgnoreCase("true"))*//*
                if (jsonObjectProductStickers.getBoolean("result"))
                {
                    JSONArray jsonArrayStickers=new JSONArray(jsonObjectProductStickers.getString("stickers"));

                    if(jsonArrayStickers!=null)
                    {
                        bucketAttribute.clear();
                        BeanProductAttribute beanProductAttribute=null;
                        int attributeSize=jsonArrayStickers.length();
                        JSONObject obj=null;
                        for(int index=0;index<attributeSize;index++)
                        {
                            obj=jsonArrayStickers.getJSONObject(index);
                            if(obj!=null)
                            {
                                beanProductAttribute=new BeanProductAttribute();
                                beanProductAttribute.setAttributeImage(obj.getString("sticker_image"));
                                //beanProductAttribute.setAttributeName(obj.getString("attribute_name"));
                                //beanProductAttribute.setAttributeType(obj.getString("attribute_type"));
                                bucketAttribute.add(beanProductAttribute);
                            }

                        }

                    }


                }
                    //INITIALIZE PACKAGING AND
                JSONArray jsonArrayOptions=new JSONArray(jsonObjectProduct.getString("options"));
                //CUtils.printLog("BIJEMDRA", jsonArrayOptions.toString(), ConstantVariables.LOG_TYPE.ERROR);
               if(jsonArrayOptions!=null)
                {
                    int size=jsonArrayOptions.length();
                    if(size>0)
                    {
                        JSONObject objProduct;
                        BeanPackagingAndQty beanPackagingAndQty;
                        for(int index=0;index<size;index++)
                        {
                            objProduct=jsonArrayOptions.getJSONObject(index);
                            beanPackagingAndQty=new BeanPackagingAndQty();
                            beanPackagingAndQty.setDefault(objProduct.getString("default").equalsIgnoreCase("true"));
                            beanPackagingAndQty.setProductMappingId(objProduct.getString("product_mapping_id"));
                            beanPackagingAndQty.setQuantityId(objProduct.getString("quantity_id"));
                            beanPackagingAndQty.setQuantity(objProduct.getString("quantity"));
                            beanPackagingAndQty.setPackagingId(objProduct.getString("packaging_id"));
                            beanPackagingAndQty.setPackagingName(objProduct.getString("packaging_name"));
                            beanPackagingAndQty.setPrice(objProduct.getString("price"));
                            beanPackagingAndQty.setProductImage(objProduct.getString("product_image"));
                            optionsBucket.add(beanPackagingAndQty);
                            if(beanPackagingAndQty.isDefault())
                                beanPackagingAndQtyDefault=beanPackagingAndQty;
                        }
                        initProductDetail(beanPackagingAndQtyDefault,bucketAttribute);//List<BeanProductAttribute> bucketAttribute
                    }

                }

            }
        }
        catch(Exception e)
        {

        }

    }
    private void initProductDetail(BeanPackagingAndQty beanPackagingAndQty,List<BeanProductAttribute> bucketAttribute)
    {
        try {
            bucketPackaging.clear();
            bucketPackagingAndQty.clear();

            productPrice = beanPackagingAndQty.getPrice();
            txtViewProductName.setTypeface(CUtils.RegularTypeFace(ProductDetail.this));
            txtViewProductName.setText(productName);
            txtViewProductPrice.setTypeface(CUtils.RegularTypeFace(ProductDetail.this));
            tviCurrency.setText("Rs");
            tviCurrency.setTypeface(CUtils.LightTypeFace(ProductDetail.this));
            txtViewProductPrice.setText(productPrice);
            txtViewPackaging.setTypeface(CUtils.RegularTypeFace(ProductDetail.this));

            if(productDescription.equals("") || productDescription==null) {
                txtViewtitle.setVisibility(View.GONE);
                txtViewProductDesc.setVisibility(View.GONE);
            } else {
                txtViewtitle.setText("Nutritionist's Advice:");
                txtViewtitle.setVisibility(View.VISIBLE);
                txtViewProductDesc.setVisibility(View.VISIBLE);
            }
            if(productReco.equals("") || productReco==null) {
                txtViewtitleTwo.setVisibility(View.GONE);
                txtViewRecoDesc.setVisibility(View.GONE);
            } else {
                txtViewtitleTwo.setText("Recommended for:");
                txtViewtitleTwo.setVisibility(View.VISIBLE);
                txtViewRecoDesc.setVisibility(View.VISIBLE);
            }
            txtViewtitle.setTypeface(CUtils.RegularTypeFace(ProductDetail.this));
            txtViewProductDesc.setTypeface(CUtils.LightTypeFace(ProductDetail.this));
            txtViewProductDesc.setText(productDescription);

            txtViewtitleTwo.setTypeface(CUtils.RegularTypeFace(ProductDetail.this));
            txtViewRecoDesc.setTypeface(CUtils.LightTypeFace(ProductDetail.this));
            txtViewRecoDesc.setText(productReco);

            productImageUrl =beanPackagingAndQty.getProductImage();
            CUtils.downloadImageFromServer(this, imageViewProduct, beanPackagingAndQty.getProductImage());

            //FILTER PACKAGING
            bucketPackaging.add(beanPackagingAndQty);
            int optionsBucketSize = optionsBucket.size();
            int bucketPackagingSize = 0;
            BeanPackagingAndQty temp, temp1;
            boolean isExists = false;
            for (int index = 0; index < optionsBucketSize; index++) {
                temp = optionsBucket.get(index);
                bucketPackagingSize = bucketPackaging.size();

                for (int size = 0; size < bucketPackagingSize; size++) {
                    temp1 = bucketPackaging.get(size);
                    isExists = temp.getQuantityId().equalsIgnoreCase(temp1.getQuantityId());
                    if (isExists)
                        break;
                }
                if (!isExists)
                    bucketPackaging.add(temp);
            }
            //FILTER PACK QTY
            for (int index = 0; index < optionsBucketSize; index++) {
                temp = optionsBucket.get(index);
                isExists = temp.getQuantityId().equalsIgnoreCase(beanPackagingAndQty.getQuantityId());
                if (isExists) {
                    if (temp.getPackagingId().equalsIgnoreCase(beanPackagingAndQty.getPackagingId()))
                        temp.setDefault(true);
                    bucketPackagingAndQty.add(temp);
                }
            }
            bucketPackagingAdapter.notifyDataSetChanged();
            bucketPackagingAndQtyAdapter.notifyDataSetChanged();
            //INITIALIZE ATTRIBUTE
            recyclerViewAttribute.setAdapter(new ProductAttributeAdapter(bucketAttribute));

        }
        catch(Exception e)
        {

        }

    }

    private void showSelectedPackagingOrPackQty(String value)
    {

        String[] splitArray = CUtils.parseCustomString("|",value);
        if(splitArray!=null)
        {
            if(splitArray.length>1) {
                int listType = Integer.parseInt(splitArray[0]);
                int position = Integer.parseInt(splitArray[1]);
                int optionsBucketSize=optionsBucket.size();
                BeanPackagingAndQty temp;
                boolean isExists=false;

                if(listType==PACKAGING_LIST) {
                    beanPackagingAndQtyDefault = bucketPackaging.get(position);
                    int size=bucketPackaging.size();
                    for(int length=0;length<size;length++) {
                        BeanPackagingAndQty bean=bucketPackaging.get(length);
                        if ((bean.getPackagingId().equalsIgnoreCase(beanPackagingAndQtyDefault.getPackagingId()))&&
                                (bean.getQuantityId().equalsIgnoreCase(beanPackagingAndQtyDefault.getQuantityId())))
                            bucketPackaging.get(length).setDefault(true);
                        else
                            bucketPackaging.get(length).setDefault(false);
                    }

                    //REFILL PACK_QUANTITY_LIST
                    bucketPackagingAndQty.clear();
                    for(int index=0;index<optionsBucketSize;index++)
                    {
                        temp=optionsBucket.get(index);
                        isExists=temp.getQuantityId().equalsIgnoreCase(beanPackagingAndQtyDefault.getQuantityId());
                        if(isExists) {
                            if(temp.getPackagingId().equalsIgnoreCase(beanPackagingAndQtyDefault.getPackagingId()))
                                temp.setDefault(true);
                            else
                                temp.setDefault(false);

                            bucketPackagingAndQty.add(temp);
                        }
                    }
                   // bucketPackagingAndQtyAdapter.notifyDataSetChanged();

                }

                if(listType==PACK_QUANTITY_LIST) {
                    beanPackagingAndQtyDefault = bucketPackagingAndQty.get(position);


                    int size=bucketPackagingAndQty.size();
                    for(int length=0;length<size;length++) {
                        BeanPackagingAndQty bean=bucketPackagingAndQty.get(length);
                        if ((bean.getPackagingId().equalsIgnoreCase(beanPackagingAndQtyDefault.getPackagingId()))&&
                                (bean.getQuantityId().equalsIgnoreCase(beanPackagingAndQtyDefault.getQuantityId())))
                        bucketPackagingAndQty.get(length).setDefault(true);
                          else
                        bucketPackagingAndQty.get(length).setDefault(false);
                    }
                   // CUtils.printLog("PACKAGING_LIST",beanPackagingAndQtyDefault.getQuantityId()+" "+beanPackagingAndQtyDefault.getQuantity()+"<>"+
                      //      beanPackagingAndQtyDefault.getPackagingId()+" "+beanPackagingAndQtyDefault.getPackagingName(), ConstantVariables.LOG_TYPE.ERROR);
                    //PACKAGING_LIST
                    bucketPackaging.clear();
                    for(int index=0;index<optionsBucketSize;index++)
                    {
                        temp=optionsBucket.get(index);
                        isExists=temp.getPackagingId().equalsIgnoreCase(beanPackagingAndQtyDefault.getPackagingId());
                        if(isExists)
                        {
                            if(temp.getQuantityId().equalsIgnoreCase(beanPackagingAndQtyDefault.getQuantityId()))
                                temp.setDefault(true);
                            else
                                temp.setDefault(false);

                            bucketPackaging.add(temp);
                        }
                }
                   /* bucketPackagingAdapter.notifyDataSetChanged();
                    bucketPackagingAndQtyAdapter.notifyDataSetChanged();*/
                }
                bucketPackagingAdapter.notifyDataSetChanged();
                bucketPackagingAndQtyAdapter.notifyDataSetChanged();

            }
            txtViewProductPrice.setText(beanPackagingAndQtyDefault.getPrice());
            CUtils.downloadImageFromServer(this, imageViewProduct, beanPackagingAndQtyDefault.getProductImage());
            //initialize values

           /* productMappingId=beanPackagingAndQtyDefault.getProductMappingId();
            productPrice= beanPackagingAndQtyDefault.getPrice();
            productImageUrl= beanPackagingAndQtyDefault.getProductImage();*/


        }
    }


    class BeanPackagingAndQty
    {
        private boolean _default=false;
        private String product_mapping_id;
        private String quantity_id;
        private String quantity;
        private String packaging_id;
        private String packaging_name;
        private String price;
        private String product_image;

        public boolean isDefault() {
            return _default;
        }

        public void setDefault(boolean defaultVal) {
            this._default = defaultVal;
        }


        public String getProductMappingId() {
            return product_mapping_id;
        }

        public void setProductMappingId(String productMappingId) {
            this.product_mapping_id = productMappingId;
        }


        public String getQuantityId() {
            return quantity_id;
        }

        public void setQuantityId(String quantityId) {
            this.quantity_id = quantityId;
        }


        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }


        public String getPackagingId() {
            return packaging_id;
        }

        public void setPackagingId(String packagingId) {
            this.packaging_id = packagingId;
        }


        public String getPackagingName() {
            return packaging_name;
        }

        public void setPackagingName(String packagingName) {
            this.packaging_name = packagingName;
        }


        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }


        public String getProductImage() {
            return product_image;
        }

        public void setProductImage(String productImage) {
            this.product_image = productImage;
            if(this.product_image.length()>0)
                this.product_image=this.product_image.replace("\\/","/");
        }


    }
    public class PackagingAndQtyViewHolder extends RecyclerView.ViewHolder {
        protected TextView PackagingAnQty;


        public PackagingAndQtyViewHolder(View v) {
            super(v);
            PackagingAnQty = (TextView) v.findViewById(R.id.textViewPackagingAnQty);
        }
    }
     class PackagingAndQtyAdapter extends RecyclerView.Adapter<PackagingAndQtyViewHolder> {
      private List<BeanPackagingAndQty> _bucket;
      private int _listType;
      public PackagingAndQtyAdapter(List<BeanPackagingAndQty> bucket,int listType)
      {
          this._bucket=bucket;
          this._listType=listType;
      }

        @Override
        public PackagingAndQtyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.row_product_packaging_and_qty, parent, false);
            return new PackagingAndQtyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(PackagingAndQtyViewHolder holder, int position) {
            try {
                    BeanPackagingAndQty beanPackagingAndQty= this._bucket.get(position);
                    holder.PackagingAnQty.setTypeface(CUtils.LightTypeFace(ProductDetail.this));
                    if(_listType==PACKAGING_LIST) {
                        holder.PackagingAnQty.setText(beanPackagingAndQty.getQuantity());

                    }
                    if(_listType==PACK_QUANTITY_LIST) {
                        holder.PackagingAnQty.setText(beanPackagingAndQty.getPackagingName());
                    }
                if(beanPackagingAndQty.isDefault()) {
                    holder.PackagingAnQty.setTextColor(getResources().getColor(R.color.primaryColor));
                    holder.PackagingAnQty.setBackgroundColor(getResources().getColor(R.color.primaryColorTransparent));
                }
                else {
                    holder.PackagingAnQty.setTextColor(Color.BLACK);
                    holder.PackagingAnQty.setBackgroundColor(getResources().getColor(R.color.white));
                }

                holder.PackagingAnQty.setTag(String.valueOf(_listType)+"|"+String.valueOf(position));
                holder.PackagingAnQty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showSelectedPackagingOrPackQty(v.getTag().toString());
                        //CUtils.printLog("PackagingAnQty_CLicked", v.getTag().toString(), ConstantVariables.LOG_TYPE.ERROR);
                    }
                });


            }
            catch(Exception e)
            {

            }
        }

        @Override
        public int getItemCount() {
            return this._bucket.size();
        }

    }

    class ProductAttributeAdapter extends RecyclerView.Adapter<ProductAttributeViewHolder> {
        private List<BeanProductAttribute> _bucketAttribute;
        BeanProductAttribute bean=null;

        public ProductAttributeAdapter(List<BeanProductAttribute> bucket)
        {
            this._bucketAttribute=bucket;

        }

        @Override
        public ProductAttributeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.row_product_detail_attributes, parent, false);
            return new ProductAttributeViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProductAttributeViewHolder holder, int position) {
            try {
                bean=this._bucketAttribute.get(position);
                //holder.Attribute.setText(bean.getAttributeName());
               CUtils.downloadImageFromServer(ProductDetail.this,holder.ImageDisplay,bean.getAttributeImage());
             }
            catch(Exception e)
            {

            }
        }

        @Override
        public int getItemCount() {
            return this._bucketAttribute.size();
        }

    }


    public class ProductAttributeViewHolder extends RecyclerView.ViewHolder {
        //protected TextView Attribute;//txtViewAttribute
        protected ImageView ImageDisplay;//imageViewAttribute


        public ProductAttributeViewHolder(View v) {
            super(v);
            //Attribute = (TextView) v.findViewById(R.id.txtViewAttribute);
            ImageDisplay = (ImageView) v.findViewById(R.id.imageViewAttribute);
        }
    }
    class BeanProductAttribute
    {
        private String attribute_name;
        private  String image;
        private  String attribute_type;

        public String getAttributeType() {
            return attribute_type;
        }
        public void setAttributeType(String attributeType) {
            this.attribute_type = attributeType;
        }


        public String getAttributeName() {
            return attribute_name;
        }
        public void setAttributeName(String attributeName) {
            this.attribute_name = attributeName;
        }

        public String getAttributeImage()
        {
            return this.image;

        }
        public void setAttributeImage(String attributeImage) {
            this.image = attributeImage;
            if(this.image.length()>0)
                this.image=this.image.replace("\\/","/");
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        super.onBackPressed();
    }
}
