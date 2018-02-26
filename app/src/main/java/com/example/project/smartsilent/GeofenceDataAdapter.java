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
 * Created by Faojul Ahsan on 1/14/2017.
 */

public class GeofenceDataAdapter extends RecyclerView.Adapter<GeofenceDataAdapter.ViewHolder> {
    public static final String ITEM_ID_KEY = "item_id_key";
    public static final String ITEM_KEY = "item_key";
    private List<GeofenceData> mGeofenceDataList ;//Items;
    private Context mContext;
    private final String TAG = GeofenceController.class.getName();


    public GeofenceDataAdapter(Context context, List<GeofenceData> items) {
        this.mContext = context;
        this.mGeofenceDataList = items;
    }

    //for deleting the item from fragment interface has been created
    //  private List<GeofenceData> mGeofenceData;//namedGeofences;

    private AllGeofencesAdapterListener listener;

    public void setListener(AllGeofencesAdapterListener listener) {
        this.listener = listener;
    }

    // endregion

    // Constructors

    // public GeofenceDataAdapter(List<GeofenceData> Geofence) {
    //   this.mGeofenceDataList = Geofence;
    // }


    @Override
    public GeofenceDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    public void onBindViewHolder(GeofenceDataAdapter.ViewHolder holder, final int position) {
        final GeofenceData Geofenceitem = mGeofenceDataList.get(position);


        holder.GeofenceName.setText(Geofenceitem.getName());
        holder.GeofenceLatitude.setText(String.valueOf(Geofenceitem.getLatitude()));
        holder.Geofencelongitude.setText(String.valueOf(Geofenceitem.getLongitude()));
        holder.GeofenceRadius.setText(String.valueOf(Geofenceitem.getRadius()));



      /*  holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "You selected " + item.getItemName(),
//                        Toast.LENGTH_SHORT).show();
//                String itemId = item.getItemId();
                //Intent intent = new Intent(mContext, DetailActivity.class);
                //intent.putExtra(ITEM_KEY, item);
                //mContext.startActivity(intent);
            }
        }); */




        holder.GeofenceDeleteButton.setOnClickListener(new View.OnClickListener() {
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
                                GeofenceController.getInstance().init(mContext);
                                GeofenceController.getInstance().removeGeofences(Geofenceitem);
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
        Intent myIntent = new Intent(mContext, PlaceList.class);

        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        mContext.startActivity(myIntent);
    }





    @Override
    public int getItemCount() {
        return mGeofenceDataList.size();
    }

    //for deleting list this interface  has been added

// region Interfaces

    public interface AllGeofencesAdapterListener {
        void onDeleteTapped(GeofenceData mGeofence);
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView GeofenceName;
        public TextView GeofenceLatitude;
        public TextView Geofencelongitude;
        public TextView GeofenceRadius;
        public Button GeofenceDeleteButton;
        public View mView;
        public ViewHolder(View itemView) {
            super(itemView);

            GeofenceName = (TextView) itemView.findViewById(R.id.listitem_geofenceName);
            GeofenceLatitude = (TextView) itemView.findViewById(R.id.listitem_geofenceLatitude);
            Geofencelongitude = (TextView) itemView.findViewById(R.id.listitem_geofenceLongitude);
            GeofenceRadius = (TextView) itemView.findViewById(R.id.listitem_geofenceRadius);
            GeofenceDeleteButton = (Button) itemView.findViewById(R.id.listitem_deleteButton);
            mView = itemView;

        }
    }
}
