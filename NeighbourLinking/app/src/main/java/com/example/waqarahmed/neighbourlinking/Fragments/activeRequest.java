package com.example.waqarahmed.neighbourlinking.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.Activities.AllContacts;
import com.example.waqarahmed.neighbourlinking.Activities.Conversations;
import com.example.waqarahmed.neighbourlinking.Activities.Delete_Post;
import com.example.waqarahmed.neighbourlinking.Activities.MainActivity;
import com.example.waqarahmed.neighbourlinking.Activities.RequestDetailActivity;
import com.example.waqarahmed.neighbourlinking.Activities.ServiceManProfile;
import com.example.waqarahmed.neighbourlinking.Classes.ServiceRequest;
import com.example.waqarahmed.neighbourlinking.Interfaces.LongClickListener;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Services.DeAvtiveRequest;
import com.example.waqarahmed.neighbourlinking.Services.RetrievAllRequestList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class activeRequest extends Fragment {

    RecyclerView recyclerView;
    TextView textView;
    FirebaseAuth mAuth;
    RVAdapter rvAdapter;
    int p;  // position
    ArrayList<ServiceRequest> ActiveList;



    public activeRequest() {
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

                //   Toast.makeText(getActivity().getApplicationContext()," "+s.get(1).getCause().toString(),Toast.LENGTH_LONG).show();
                for(int i=0; i<s.size(); i++){
                    if(s.get(i).getStatus().equals("active"))
                        ActiveList.add(s.get(i));
                }

                if(ActiveList.size()>0) {
                    textView.setVisibility(View.INVISIBLE);
                     rvAdapter = new RVAdapter(ActiveList, getActivity().getApplicationContext());
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
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();


    }

    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RequestViewHolder> {


        List<ServiceRequest> list;
        Context mContext;
        String name;
        ServiceRequest serviceRequest;


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
        public void onBindViewHolder(final RVAdapter.RequestViewHolder holder, int position) {
            ServiceRequest request = list.get(position);

            if(request.getStatus().equals("active")) {
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


        }

        @Override
        public void onViewRecycled(RequestViewHolder holder) {   // may not required
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
        public void getItemSelected(MenuItem item){
          //  Toast.makeText(mContext,name +" "+item.getTitle(),Toast.LENGTH_LONG).show();
            if(item.getTitle().equals("Message")){
                Intent convIntent = new Intent(getActivity().getApplicationContext(),Conversations.class);
                convIntent.putExtra("receverId",serviceRequest.getId());
                convIntent.putExtra("receverName",serviceRequest.getPowerMan_name());

                startActivity(convIntent);

            }
            if(item.getTitle().equals("De Active")) {
//
//                AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(mContext);
//                builder1.setTitle("Delete Post");
//                builder1.setMessage("Are you sure you want to delete this entry?");
//                builder1.setCancelable(true);
//
//                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
                        DeAvtiveRequest deAvtiveRequest = (DeAvtiveRequest) new DeAvtiveRequest(getActivity().getApplicationContext()).execute("login", String.valueOf(serviceRequest.getId()));
                     ActiveList.remove(p);
                   rvAdapter.notifyDataSetChanged();

                // dialogInterface.dismiss();

                //    }
//                });
//                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.cancel();
//                    }
//                });
//                AlertDialog alert11 = builder1.create();
//                alert11.show();
            }





            if(item.getTitle().equals("Request Detail")){
                Intent rDetailintent = new Intent(getActivity().getApplicationContext(),RequestDetailActivity.class);
                rDetailintent.putExtra("rObject", serviceRequest);


                startActivity(rDetailintent);

            }
            if(item.getTitle().equals("Service Man's About")){
                Intent rDetailintent = new Intent(getActivity().getApplicationContext(),ServiceManProfile.class);
                rDetailintent.putExtra("id", serviceRequest.getPowerMan_id());
              //  Toast.makeText(getActivity().getApplicationContext(), serviceRequest.getPowerMan_id(), Toast.LENGTH_SHORT).show();



                startActivity(rDetailintent);

            }
        }

        public  class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnCreateContextMenuListener {

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

            @Override
            public boolean onLongClick(View view) {
                this.longClickListener.onItemLongClick(getLayoutPosition());
                return false;
            }

            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.setHeaderTitle("Seleect Acton");
                contextMenu.add(0,0,0,"Message");
                contextMenu.add(0,0,0,"De Active");
                contextMenu.add(0,0,0,"Request Detail");
                contextMenu.add(0,0,0,"Service Man's About");


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
    public void showConformationDialog() {
//        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity().getApplicationContext());
//        builder1.setTitle("Delete Post");
//        builder1.setMessage("Are you sure you want to delete this entry?");
//        builder1.setCancelable(true);
//
//        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        DeAvtiveRequest deAvtiveRequest = (DeAvtiveRequest) new DeAvtiveRequest(getActivity().getApplicationContext()).execute("login", String.valueOf(serviceRequest.getId()));
//                          dialogInterface.dismiss();
//
//                    }
//                });
//        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
//        AlertDialog alert11 = builder1.create();
//       alert11.show();
//    }
    }
}
