package dog.snow.androidrecruittest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import dog.snow.androidrecruittest.Models.DataModel;
import dog.snow.androidrecruittest.R;

/**
 * Created by Rohail on 1/24/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private ArrayList<DataModel> dataModelList;
    private Context context;

    public RecyclerViewAdapter(ArrayList<DataModel> dataModelList, Context context) {
        this.dataModelList = dataModelList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new RecyclerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvTitle.setText(dataModelList.get(position).getName());
        holder.tvDescription.setText(dataModelList.get(position).getDescription());
        Picasso.with(context).load(dataModelList.get(position).getIcon()).
                placeholder(R.color.colorPrimaryDark).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView tvTitle;
        public TextView tvDescription;

        public MyViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.icon_ic);
            tvTitle = (TextView) itemView.findViewById(R.id.name_tv);
            tvDescription = (TextView) itemView.findViewById(R.id.description_tv);
        }
    }

}
