package com.example.waqarahmed.neighbourlinking.Fragments.AdminFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.waqarahmed.neighbourlinking.Activities.BrandActivities.AboutBrand;
import com.example.waqarahmed.neighbourlinking.Activities.ServiceManActivities.AboutServiceman;
import com.example.waqarahmed.neighbourlinking.Classes.Brand;
import com.example.waqarahmed.neighbourlinking.Classes.ServiceMan;
import com.example.waqarahmed.neighbourlinking.Interfaces.LongClickListener;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Services.AdminSevices.RetrievAllBrandList_admin;
import com.example.waqarahmed.neighbourlinking.Services.AdminSevices.RetrievAllMenPowerList_admin;
import com.example.waqarahmed.neighbourlinking.Services.AdminSevices.setDeActiveBrand;
import com.example.waqarahmed.neighbourlinking.Services.AdminSevices.setDeActiveServiceMan;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ActiveBrand extends Fragment {

    String getIntent_skillName;
    RecyclerView menPowerList_recyclerView;
    TextView textView;
    ArrayList<Brand> brandlist;
    int p;  // position
    RVAdapterForMenList rvAdapterForMenList;

    public ActiveBrand() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_active_employee, container, false);
        menPowerList_recyclerView= (RecyclerView) view.findViewById(R.id.recyclerView_menPower);
        textView = (TextView) view.findViewById(R.id.messageView);
        menPowerList_recyclerView.setHasFixedSize(true);
        menPowerList_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        brandlist = new ArrayList<Brand>();
        registerForContextMenu(menPowerList_recyclerView);
        rvAdapterForMenList = new RVAdapterForMenList(brandlist,getActivity());


//        RetrievAllserviceMenList retrievAllserviceMenList =new RetrievAllserviceMenList(getActivity());
//        retrievAllserviceMenList.asyncResponse = (AsynResonseForMenPowerList) getActivity().getApplicationContext();
//        retrievAllserviceMenList.execute();

         new RetrievAllBrandList_admin(getActivity()){

             @Override
             protected void onPostExecute(ArrayList<Brand> s) {
                 super.onPostExecute(s);
                 if(s != null) {
                     menPowerList_recyclerView.setVisibility(View.VISIBLE);
                     textView.setVisibility(View.INVISIBLE);
                     for(int i = 0; i < s.size(); i++){
                        // ServiceMan s = serviceMen.get(i);
                         if(s.get(i).getStatus().equals("active")){
                             brandlist.add(s.get(i));
                         }

                     }
                     //menlist = serviceMen;
                     rvAdapterForMenList = new RVAdapterForMenList(brandlist, getActivity());
                     menPowerList_recyclerView.setAdapter(rvAdapterForMenList);



                 }
                 else{
                     menPowerList_recyclerView.setVisibility(View.INVISIBLE);
                     textView.setVisibility(View.VISIBLE);


                 }
             }
         }.execute();


        return view;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    public class RVAdapterForMenList extends RecyclerView.Adapter<RVAdapterForMenList.contactViewHolder> {


        List<Brand> list;
        Context mContext;
        Brand brand2;  // use for Contextual menue

        RVAdapterForMenList(List<Brand> bList, Context context) {
            list = bList;
            mContext = context;
        }

        @Override
        public RVAdapterForMenList.contactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_list_item, parent, false);
            contactViewHolder cvh = new contactViewHolder(v);
            return cvh;
        }

        @Override
        public void onBindViewHolder(RVAdapterForMenList.contactViewHolder holder, int position) {
            Brand brnd = list.get(position);

            holder.setContact_name(brnd.getName());
            holder.setContact_image(brnd.getImage_url(), mContext);
            holder.setContact_no(brnd.getContact());
            holder.setLongClickListener(new LongClickListener() {
                @Override
                public void onItemLongClick(int pos) {
                    p = pos;
                    brand2 = list.get(pos);    // here get position of selected recycler item position
                }
            });
        }

        @Override
        public void onViewRecycled(contactViewHolder holder) {   // may not required
            super.onViewRecycled(holder);
            holder.itemView.setOnLongClickListener(null);
        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public long getItemId(int position) {
            // return position;
            return super.getItemId(position);


        }

        public void getItemSelected(MenuItem item) {
            //  Toast.makeText(mContext,"hello" +" "+item.getTitle(),Toast.LENGTH_LONG).show();
            if (item.getTitle().equals("About")) {

                Intent intent = new Intent(mContext, AboutBrand.class);
                intent.putExtra("id", String.valueOf(brand2.getId()));
                startActivity(intent);
            }
            if(item.getTitle().equals("Block Brand")){
               new setDeActiveBrand(getActivity()).execute("login", String.valueOf(brand2.getId()));
                list.remove(brand2);
                rvAdapterForMenList.notifyDataSetChanged();

            }

        }

        public class contactViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnCreateContextMenuListener {

            View mView;
            ImageView imageView;
            TextView nameView, dateView;
            LongClickListener longClickListener;

            public contactViewHolder(View itemView) {
                super(itemView);

                mView = itemView;
                mView.setOnLongClickListener(this);
                mView.setOnCreateContextMenuListener(this);

                imageView = (ImageView) mView.findViewById(R.id.contentImage_imageView_contentView);
                nameView = (TextView) itemView.findViewById(R.id.contentName_textView_contentView);
                dateView = (TextView) itemView.findViewById(R.id.created_date_contentView);
            }

            public void setContact_name(String name) {

                nameView.setText(name);

            }

            public void setContact_image(final String image, final Context cxt) {
                Picasso.with(cxt).load(image).centerCrop().resize(75, 75).placeholder(R.mipmap.ic_launcher).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(cxt).load(image).centerCrop().placeholder(R.mipmap.ic_launcher).resize(75, 75).into(imageView);
                    }
                });
            }

            public void setContact_no(String no) {

                dateView.setText(no);

            }

            public void setLongClickListener(LongClickListener lc) {
                this.longClickListener = lc;
            }

            @Override
            public boolean onLongClick(View view) {
                this.longClickListener.onItemLongClick(getLayoutPosition());
                return false;
            }

            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {


                contextMenu.setHeaderTitle("Seleect Acton");
                contextMenu.add("About");
                contextMenu.add("Block Brand");


            }

        }


    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        rvAdapterForMenList.getItemSelected(item);
        return false;
    }


}
