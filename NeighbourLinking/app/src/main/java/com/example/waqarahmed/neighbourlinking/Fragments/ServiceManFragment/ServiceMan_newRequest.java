package com.example.waqarahmed.neighbourlinking.Fragments.ServiceManFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.waqarahmed.neighbourlinking.Activities.Profile;
import com.example.waqarahmed.neighbourlinking.Activities.RequestDetailActivity;
import com.example.waqarahmed.neighbourlinking.Activities.ServiceManProfile;
import com.example.waqarahmed.neighbourlinking.Classes.ServiceRequest;
import com.example.waqarahmed.neighbourlinking.Interfaces.LongClickListener;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Services.DeAvtiveRequest;
import com.example.waqarahmed.neighbourlinking.Services.RetrievAllRequestList;
import com.example.waqarahmed.neighbourlinking.Services.ServiceManServices.AccepRequestService;
import com.example.waqarahmed.neighbourlinking.Services.ServiceManServices.ServiceManSpecificRetrievAllRequestList;
import com.example.waqarahmed.neighbourlinking.Services.ServiceManServices.setRejectRequestService;
import com.example.waqarahmed.neighbourlinking.Shared.SharedPref;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ServiceMan_newRequest extends Fragment
{

    RecyclerView recyclerView;
    TextView textView;
    RVAdapter rvAdapter;

    int p;  // position
    int currentUserId ;
    ArrayList<ServiceRequest> ActiveList;
    public ServiceMan_newRequest()
    {
        // Required empty public constructor android.support.design.widget.CoordinatorLayout
    }



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_active_request, container, false);
        Log.i("TAGTAG","activeRequest onCreateView");
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_activeList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
       // registerForContextMenu(recyclerView);
        textView = (TextView) view.findViewById(R.id.messageView);
        SharedPref.init(getActivity().getApplicationContext());
        currentUserId = SharedPref.read(SharedPref.ID,0);
        ActiveList = new ArrayList<ServiceRequest>();
        rvAdapter = new RVAdapter(ActiveList , getActivity().getApplicationContext());

        return view;
    }


    @Override
    public void onStart()
    {
        super.onStart();


        if(currentUserId != 0)
        {
            new ServiceManSpecificRetrievAllRequestList(getActivity())
            {
                @Override
                protected void onPostExecute(ArrayList<ServiceRequest> s) {
                    super.onPostExecute(s);
                    ActiveList.clear();
                    if (s != null)
                    {
                        for (int i = 0; i < s.size(); i++) {
                            if (s.get(i).getStatus().equals("active") && s.get(i).getStatus().equals("pending") )
                                ActiveList.add(s.get(i));
                        }

                        if (ActiveList.size() > 0) {
                            textView.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            rvAdapter = new RVAdapter(ActiveList, getActivity());
                            recyclerView.setAdapter(rvAdapter);
                        } else {


                            recyclerView.setVisibility(View.INVISIBLE);
                            textView.setVisibility(View.VISIBLE);

                        }

                    }else{
                        textView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);

                    }
                }


            }.execute("login",String.valueOf(currentUserId));
        }

        Log.i("TAGTAG","activeRequest onStart");


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


                holder.setServiceTaker_image(request.getOwner_image_url(), mContext);
                holder.setServiceTaker_name(request.getOwner_name());
                holder.setRequest_date(request.getCreated_at());
             //  holder.mView.setLongClickable(true);
                holder.setLongClickListener(new LongClickListener()
                {
                    @Override
                    public void onItemLongClick(int pos) {
                        p = pos;
                        serviceRequest = list.get(pos);    // here get position of selected recycler item position
                    }
                });





        }

        @Override
        public void onViewRecycled(RequestViewHolder holder)
        {   // may not required
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
        public void getItemSelected(MenuItem item)
        {
          //  Toast.makeText(mContext,name +" "+item.getTitle(),Toast.LENGTH_LONG).show();
            if(item.getTitle().equals("Message"))
            {
                Intent convIntent = new Intent(getActivity().getApplicationContext(),Conversations.class);
                convIntent.putExtra("receverId",serviceRequest.getSender_id());
                convIntent.putExtra("receverName",serviceRequest.getOwner_name());

                startActivity(convIntent);
            }
           else if(item.getTitle().equals("Accept it"))
            {
                     AccepRequestService Accep = (AccepRequestService) new AccepRequestService(getActivity().getApplicationContext()).execute("login", String.valueOf(serviceRequest.getId()));
                     ActiveList.remove(p);
                     rvAdapter.notifyDataSetChanged();


            }
            else if(item.getTitle().equals("Reject it"))
            {
                setRejectRequestService Reject = (setRejectRequestService) new setRejectRequestService(getActivity().getApplicationContext()).execute("login", String.valueOf(serviceRequest.getId()));
                ActiveList.remove(p);
                rvAdapter.notifyDataSetChanged();


            }
         else    if(item.getTitle().equals("Request Detail")){
                Intent rDetailintent = new Intent(getActivity().getApplicationContext(),RequestDetailActivity.class);
                if(!serviceRequest.equals(null)) {
                    rDetailintent.putExtra("rObject", serviceRequest);
                }else{
                    Toast.makeText(getActivity(),"Object null",Toast.LENGTH_SHORT).show();
                }

                startActivity(rDetailintent);

            }
        else  if(item.getTitle().equals("Service Taker About"))
            {
                Intent rDetailintent = new Intent(getActivity(),About.class);
                String id = serviceRequest.getSender_id();
               rDetailintent.putExtra("id",id);
            //    Toast.makeText(getActivity(),"Service Taker About",Toast.LENGTH_LONG).show();


              //  Toast.makeText(getActivity().getApplicationContext(), serviceRequest.getPowerMan_id(), Toast.LENGTH_SHORT).show();
                startActivity(rDetailintent);

            }
            else{

            }
        }

        public  class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnCreateContextMenuListener
        {

            View mView;
            ImageView imageView ;
            TextView nameView,dateView;
            LongClickListener longClickListener;


            public RequestViewHolder(View itemView) {
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
            public void setServiceTaker_name(String name) {

                nameView.setText(name);

            }


            public void setServiceTaker_image(final String image , final Context cxt) {
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

            @Override
            public boolean onLongClick(View view) {
                this.longClickListener.onItemLongClick(getLayoutPosition());
                return false;
            }

            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {


                contextMenu.setHeaderTitle("Seleect Acton");
                contextMenu.add(0,0,0,"Message");
                contextMenu.add(0,0,0,"Accept it");
                contextMenu.add(0,0,0,"Reject it");
                contextMenu.add(0,0,0,"Request Detail");
                contextMenu.add(0,0,0,"Service Taker About");


            }
        }
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
    public boolean onContextItemSelected(MenuItem item) {

        rvAdapter.getItemSelected(item);
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterForContextMenu(recyclerView);
        Log.i("TAGTAG","activeRequest onPause");

    }
    @Override
    public void onResume()
    {
        super.onResume();
        registerForContextMenu(recyclerView);
        Log.i("TAGTAG","activeRequest onResum");

    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterForContextMenu(recyclerView);
        Log.i("TAGTAG","activeRequest onStop");
    }


}
