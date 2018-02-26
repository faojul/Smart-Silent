package com.example.project.smartsilent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ashrin Tamim on 1/14/2017.
 */

public class TimeDateAdapter  extends RecyclerView.Adapter<TimeDateAdapter.ViewHolder> {
    public static final String ITEM_ID_KEY = "item_id_key";
    public static final String ITEM_KEY = "item_key";
    private List<TimeDate> mTimeDataList; // mGeofenceDataList ;//Items;
    private Context mContext;
    private final String TAG = GeofenceController.class.getName();


    public TimeDateAdapter(Context context, List<TimeDate> items) {
        this.mContext = context;
        this.mTimeDataList = items;
    }




    @Override
    public TimeDateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.time_list_item, parent, false);
        TimeDateAdapter.ViewHolder viewHolder = new TimeDateAdapter.ViewHolder(itemView);
        return viewHolder;
    }

    public void onBindViewHolder(TimeDateAdapter.ViewHolder holder, final int position) {
        final TimeDate timeitem = mTimeDataList.get(position);


        String starth=String.valueOf( timeitem.getSilentHour());
        String startm=String.valueOf( timeitem.getSilentMinute());
        String starttotal = starth + " : " + startm;

        String endh=String.valueOf( timeitem.getUnsilentHour());
        String endm=String.valueOf( timeitem.getUnsilentMinute());
        String endtotal = endh + " : " + endm;
        holder.StartTime.setText(starttotal);
        holder.EndTime.setText(endtotal);









        holder.TimeDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(R.string.AreYouSure)
                        .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                               if (listener != null) {
//                                    Log.e(TAG,"i'm save buttom yes fragment");
//                                    listener.onDeleteTapped(Geofenceitem);
//                                }
                                TimeController.getInstance().init(mContext);
                                TimeController.getInstance().removetimeitem(timeitem);
                                reload();







                            }
                        })
                        .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                Log.e(TAG,"i'm save buttom no fragment");
                            }
                        })
                        .create()
                        .show();
            }
        });










    }


    public void reload() {
        Intent myIntent = new Intent(mContext, TimeList.class);

        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        mContext.startActivity(myIntent);
    }





    @Override
    public int getItemCount() {
        return mTimeDataList.size();
    }







    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView StartTime;
        public TextView EndTime;

        public Button TimeDeleteButton;
        public View mView;
        public ViewHolder(View itemView) {
            super(itemView);


            StartTime = (TextView) itemView.findViewById(R.id.listitem_starttime);
            EndTime = (TextView) itemView.findViewById(R.id.listitem_endtime);
            TimeDeleteButton = (Button) itemView.findViewById(R.id.listitem_deleteButton);
            mView = itemView;

        }
    }
}
