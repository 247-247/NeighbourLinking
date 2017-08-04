package com.example.waqarahmed.neighbourlinking.Fragments;

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

import com.example.waqarahmed.neighbourlinking.Activities.RequestDetailActivity;
import com.example.waqarahmed.neighbourlinking.Activities.ServiceManActivities.AboutServiceman;
import com.example.waqarahmed.neighbourlinking.Activities.ServiceManProfile;
import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities.Conversations;
import com.example.waqarahmed.neighbourlinking.Classes.ServiceRequest;
import com.example.waqarahmed.neighbourlinking.Interfaces.LongClickListener;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Services.DeAvtiveRequest;
import com.example.waqarahmed.neighbourlinking.Services.RetrievAllRequestList;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class historyRequest_tanantFragment extends Fragment {

    RecyclerView recyclerView;
    TextView textView;
    FirebaseAuth mAuth;
    RVAdapter rvAdapter;
    int p;  // position
    ArrayList<ServiceRequest> ActiveList;



    public historyRequest_tanantFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_active_request, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_activeList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        registerForContextMenu(recyclerView);

        textView = (TextView) view.findViewById(R.id.messageView);

        mAuth = FirebaseAuth.getInstance();
        ActiveList = new ArrayList<ServiceRequest>();
        new RetrievAllRequestList(getActivity()){
            @Override
            protected void onPostExecute(ArrayList<ServiceRequest> s) {
                super.onPostExecute(s);
                textView.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
      if(s != null) {

          for (int i = 0; i < s.size(); i++) {

              if (s.get(i).getStatus().equals("active") && s.get(i).getS().equals("Accept"))
                  ActiveList.add(s.get(i));
          }

          if (ActiveList.size() > 0) {
              textView.setVisibility(View.INVISIBLE);
              recyclerView.setVisibility(View.VISIBLE);
              rvAdapter = new RVAdapter(ActiveList, getActivity());
              recyclerView.setAdapter(rvAdapter);
          } else {
              textView.setVisibility(View.VISIBLE);
              recyclerView.setVisibility(View.INVISIBLE);

          }
      }else {

          textView.setVisibility(View.VISIBLE);
          recyclerView.setVisibility(View.INVISIBLE);
      }

            }
        }.execute("login",mAuth.getCurrentUser().getUid().toString());

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

    }

    @Override
    public void onStart()
    {
        super.onStart();


    }

    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RequestViewHolder>
    {


        List<ServiceRequest> list;
        Context mContext;
        String name;
        ServiceRequest serviceRequest;


        RVAdapter(List<ServiceRequest> requestList, Context context)
        {
            list = requestList;
            mContext = context;

        }


        @Override
        public RVAdapter.RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_list_item, parent, false);
            RVAdapter.RequestViewHolder cvh = new RVAdapter.RequestViewHolder(v);
            return cvh;
        }

        @Override
        public void onBindViewHolder(final RVAdapter.RequestViewHolder holder, int position)
        {
            ServiceRequest request = list.get(position);


                holder.setServiceMan_image(request.getPowerMan_image_url(), mContext);
                holder.setServiceType_name(request.getType());
                holder.setRequest_date(request.getCreated_at());
             //  holder.mView.setLongClickable(true);
                holder.setLongClickListener(new LongClickListener() {
                    @Override
                    public void onItemLongClick(int pos) {
                        p = pos;
                        serviceRequest = list.get(pos);    // here get position of selected recycler item position
                    }
                });





        }

        @Override
        public void onViewRecycled(RequestViewHolder holder) {   // may not required
            super.onViewRecycled(holder);
            holder.itemView.setOnLongClickListener(null);
        }

        @Override
        public int getItemCount()
        {

            return list.size();
        }

        @Override
        public long getItemId(int position)
        {
           // return position;
            return super.getItemId(position);


        }
        public void getItemSelected(MenuItem item)
        {

            if(item.getTitle().equals("Request Detail"))
            {
                Intent rDetailintent = new Intent(getActivity().getApplicationContext(),RequestDetailActivity.class);
                rDetailintent.putExtra("rObject", serviceRequest);


                startActivity(rDetailintent);

            }
            if(item.getTitle().equals("Service Man's About"))
            {
                Intent rDetailintent = new Intent(getActivity().getApplicationContext(),AboutServiceman.class);
                rDetailintent.putExtra("id", serviceRequest.getPowerMan_id());
                startActivity(rDetailintent);

            }
        }

        public  class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnCreateContextMenuListener
        {

            View mView;
            ImageView imageView ;
            TextView nameView,dateView;
            LongClickListener longClickListener;


            public RequestViewHolder(View itemView)
            {
                super(itemView);

                mView = itemView;

                mView.setOnLongClickListener(this);
                mView.setOnCreateContextMenuListener(this);

                imageView = (ImageView)mView.findViewById(R.id.serviceManImage_imageView_RequestView);
                nameView = (TextView) itemView.findViewById(R.id.RequestTypeName_textView_requestView);
                dateView = (TextView)itemView.findViewById(R.id.Requestcreated_date_requesttView);
            }


            public  void setLongClickListener(LongClickListener lc){
                this.longClickListener = lc;
            }
            public void setServiceType_name(String name)
            {

                nameView.setText(name);

            }


            public void setServiceMan_image(final String image , final Context cxt)
            {
                Picasso.with(cxt).load(image).centerCrop().placeholder(R.mipmap.ic_launcher).resize(75,75).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(cxt).load(image).centerCrop().placeholder(R.mipmap.ic_launcher).resize(75,75).into(imageView);
                    }
                });
            }
            public void setRequest_date(String date)
            {

                dateView.setText(date);

            }

            @Override
            public boolean onLongClick(View view)
            {
                this.longClickListener.onItemLongClick(getLayoutPosition());
                return false;
            }

            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo)
            {
                contextMenu.setHeaderTitle("Seleect Acton");


                contextMenu.add(0,0,0,"Request Detail");
                contextMenu.add(0,0,0,"Service Man's About");


            }
        }
    }






    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

    }

    @Override
    public void onDetach()
    {
        super.onDetach();

    }



    @Override
    public boolean onContextItemSelected(MenuItem item)
    {

        rvAdapter.getItemSelected(item);
        return false;
    }
    public void showConformationDialog()
    {

    }
}
