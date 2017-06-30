package com.example.waqarahmed.neighbourlinking.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.waqarahmed.neighbourlinking.Activities.RequestRelatedDetail;
import com.example.waqarahmed.neighbourlinking.Classes.ServiceRequest;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Services.RetrievAllRequestList;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class DeActiveRequests extends Fragment {
    RecyclerView recyclerView;
    TextView textView;
    FirebaseAuth mAuth;
    ArrayList<ServiceRequest> deActiveList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_de_active_requests, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_activeList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        getActivity().setTitle("De Active");

        textView = (TextView) view.findViewById(R.id.messageView);
        mAuth = FirebaseAuth.getInstance();
        deActiveList = new ArrayList<ServiceRequest>();
        new RetrievAllRequestList(getActivity()){
            @Override
            protected void onPostExecute(ArrayList<ServiceRequest> s) {
                super.onPostExecute(s);

                //   Toast.makeText(getActivity().getApplicationContext()," "+s.get(1).getCause().toString(),Toast.LENGTH_LONG).show();

                for(int i=0; i<s.size(); i++){
                    if(s.get(i).getStatus().equals("DeActive"))
                    deActiveList.add(s.get(i));
                }

                if(deActiveList.size()>0) {
                    textView.setVisibility(View.INVISIBLE);
                    RVAdapter rvAdapter = new RVAdapter(deActiveList, getActivity().getApplicationContext());
                    recyclerView.setAdapter(rvAdapter);
                }else{

                    recyclerView.setVisibility(View.INVISIBLE);

                }
                // onStart();
                //  progress.dismiss();
            }
        }.execute("login",mAuth.getCurrentUser().getUid().toString());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RequestViewHolder> {


        List<ServiceRequest> list;
        Context mContext;

        RVAdapter(List<ServiceRequest> requestList, Context context) {
            list = requestList;
            mContext = context;
        }

        @Override
        public RVAdapter.RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_list_item, parent, false);
            RVAdapter.RequestViewHolder cvh = new RVAdapter.RequestViewHolder(v);
            return cvh;
        }

        @Override
        public void onBindViewHolder(RVAdapter.RequestViewHolder holder, int position) {
            ServiceRequest request = list.get(position);
            if(request.getStatus().equals("DeActive")) {
                holder.setServiceMan_image(request.getPowerMan_image_url(), mContext);
                holder.setServiceType_name(request.getType());
                holder.setRequest_date(request.getCreated_at());
            }
            else{

            }

            // holder.setContact_name(contact.getFirst_name());
            // holder.setContact_image(contact.getImage(),mContext);
            // holder.setContact_date(contact.getAddress());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public  class RequestViewHolder extends RecyclerView.ViewHolder{

            View mView;
            ImageView imageView ;
            TextView nameView,dateView;

            public RequestViewHolder(View itemView) {
                super(itemView);

                mView = itemView;
                imageView = (ImageView)mView.findViewById(R.id.serviceManImage_imageView_RequestView);
                nameView = (TextView) itemView.findViewById(R.id.RequestTypeName_textView_requestView);
                dateView = (TextView)itemView.findViewById(R.id.Requestcreated_date_requesttView);
            }
            public void setServiceType_name(String name) {

                nameView.setText(name);

            }
            public void setServiceMan_image(final String image , final Context cxt) {
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
            public void setRequest_date(String date) {

                dateView.setText(date);

            }





        }
    }


}
