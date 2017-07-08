package com.example.waqarahmed.neighbourlinking.Fragments.ServiceManFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.Activities.About;
import com.example.waqarahmed.neighbourlinking.Activities.Conversations;
import com.example.waqarahmed.neighbourlinking.Activities.RequestDetailActivity;
import com.example.waqarahmed.neighbourlinking.Classes.ServiceRequest;
import com.example.waqarahmed.neighbourlinking.Interfaces.LongClickListener;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Services.ServiceManServices.AcctveAndAcceptRequest;
import com.example.waqarahmed.neighbourlinking.Services.ServiceManServices.ServiceManSpecificRetrievAllRequestList;
import com.example.waqarahmed.neighbourlinking.Shared.SharedPref;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class AceptAndActiveRequest extends android.support.v4.app.Fragment {
    RecyclerView activeAndAccept_ReclerView;
    TextView textView;
    ActiveAndAcceptRequestRVA activeAndAcceptRequestRVA;

    int p;  // position
    int currentUserId;
    ArrayList<ServiceRequest> ActiveList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acept_and_active_request, container, false);
        Log.i("TAGTAG","AceptAndActiveRequest onCreateView");
        activeAndAccept_ReclerView = (RecyclerView) view.findViewById(R.id.activeAndAcept_rcyclrView);
        activeAndAccept_ReclerView.setHasFixedSize(true);
        activeAndAccept_ReclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
       // registerForContextMenu(activeAndAccept_ReclerView);
        SharedPref.init(getActivity().getApplicationContext());
        currentUserId = SharedPref.read(SharedPref.ID,0);
        ActiveList = new ArrayList<ServiceRequest>();




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

    @Override
    public void onStart() {
        super.onStart();
        if(currentUserId != 0){
            new AcctveAndAcceptRequest(getActivity()){
                @Override
                protected void onPostExecute(ArrayList<ServiceRequest> s) {
                    super.onPostExecute(s);

                    for(int i=0; i<s.size(); i++)
                    {
                        if(s.get(i).getStatus().equals("active"))
                            ActiveList.add(s.get(i));
                    }

                    if(ActiveList.size()>0)
                    {
                        // textView.setVisibility(View.INVISIBLE);
                        activeAndAcceptRequestRVA = new ActiveAndAcceptRequestRVA(ActiveList, getActivity());
                        activeAndAccept_ReclerView.setAdapter(activeAndAcceptRequestRVA);
                    }

                }
            }.execute("login", String.valueOf(currentUserId));

        }

        Log.i("TAGTAG","AceptAndActiveRequest onStart");
        activeAndAccept_ReclerView.setAdapter(activeAndAcceptRequestRVA);
        registerForContextMenu(activeAndAccept_ReclerView);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterForContextMenu(activeAndAccept_ReclerView);
        Log.i("TAGTAG","AceptAndActiveRequest onStop");
    }

    public class ActiveAndAcceptRequestRVA extends RecyclerView.Adapter<ActiveAndAcceptRequestRVA.ActiveAndAcceptRequestViewHolder> {
        List<ServiceRequest> list;
        Context mContext;
        String name;
        ServiceRequest serviceRequest;

        public ActiveAndAcceptRequestRVA(List<ServiceRequest> list, Context mContext) {
            this.list = list;
            this.mContext = mContext;
        }

        @Override
        public ActiveAndAcceptRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_list_item, parent, false);
            ActiveAndAcceptRequestViewHolder activeAndAcceptRequestRVA = new ActiveAndAcceptRequestViewHolder(v);

            return activeAndAcceptRequestRVA;
        }

        @Override
        public void onBindViewHolder(ActiveAndAcceptRequestViewHolder holder, int position) {
            ServiceRequest request = list.get(position);
            if (request.getStatus().equals("active")) {
                holder.setServiceTaker_image(request.getOwner_image_url(), mContext);
                holder.setServiceTaker_name(request.getOwner_name());
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
        }

        @Override
        public void onViewRecycled(ActiveAndAcceptRequestViewHolder holder) {   // may not required
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
            //  Toast.makeText(mContext,name +" "+item.getTitle(),Toast.LENGTH_LONG).show();
            if (item.getTitle().equals("Message")) {
                Intent convIntent = new Intent(getActivity().getApplicationContext(),Conversations.class);
                convIntent.putExtra("receverId",serviceRequest.getSender_id());
                convIntent.putExtra("receverName",serviceRequest.getOwner_name());

                      startActivity(convIntent);
            } else if (item.getTitle().equals("Request Detail")) {
                Intent requestDetailintent = new Intent(getActivity().getApplicationContext(), RequestDetailActivity.class);
                if (!serviceRequest.equals(null)) {
                    requestDetailintent.putExtra("rObject", serviceRequest);
                } else {
                    Toast.makeText(getActivity(), "Object null", Toast.LENGTH_SHORT).show();
                }

                startActivity(requestDetailintent);

            } else if (item.getTitle().equals("Service Taker About")) {
                Intent rDetailintent = new Intent(getActivity(), About.class);
                String id = serviceRequest.getSender_id();
                rDetailintent.putExtra("id", id);
                //    Toast.makeText(getActivity(),"Service Taker About",Toast.LENGTH_LONG).show();


                //  Toast.makeText(getActivity().getApplicationContext(), serviceRequest.getPowerMan_id(), Toast.LENGTH_SHORT).show();
                startActivity(rDetailintent);

            } else {

            }
        }


        public class ActiveAndAcceptRequestViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnCreateContextMenuListener {

            View mView;
            ImageView imageView;
            TextView nameView, dateView;
            LongClickListener longClickListener;


            public ActiveAndAcceptRequestViewHolder(View itemView) {
                super(itemView);

                mView = itemView;

                mView.setOnLongClickListener(this);
                mView.setOnCreateContextMenuListener(this);

                imageView = (ImageView) mView.findViewById(R.id.serviceManImage_imageView_RequestView);
                nameView = (TextView) itemView.findViewById(R.id.RequestTypeName_textView_requestView);
                dateView = (TextView) itemView.findViewById(R.id.Requestcreated_date_requesttView);
            }


            public void setLongClickListener(LongClickListener lc) {
                this.longClickListener = lc;
            }

            public void setServiceTaker_name(String name) {

                nameView.setText(name);

            }


            public void setServiceTaker_image(final String image, final Context cxt) {
                Picasso.with(cxt).load(image).centerCrop().placeholder(R.mipmap.ic_launcher).resize(75, 75).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(cxt).load(image).centerCrop().placeholder(R.mipmap.ic_launcher).resize(75, 75).into(imageView);
                    }
                });
            }

            public void setRequest_date(String date) {

                dateView.setText(date);

            }

            @Override
            public boolean onLongClick(View view) {
                this.longClickListener.onItemLongClick(getLayoutPosition());
                return false;
            }

            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.setHeaderTitle("Seleect Acton");
                contextMenu.add(0, 0, 0, "Message");
                contextMenu.add(0, 0, 0, "Request Detail");
                contextMenu.add(0, 0, 0, "Service Taker About");


            }
        }
    }




    @Override
    public boolean onContextItemSelected(MenuItem item) {

        activeAndAcceptRequestRVA.getItemSelected(item);
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterForContextMenu(activeAndAccept_ReclerView);
        Log.i("TAGTAG","AceptAndActiveRequest onPause");

    }

    @Override
    public void onResume() {
        super.onResume();
        registerForContextMenu(activeAndAccept_ReclerView);

        Log.i("TAGTAG","AceptAndActiveRequest onResum");

    }


}

















