package nnk.com.imageproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by SRATHORE on 4/25/2017.
 */

public class defaultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    int images[];
    ImageView imageView;
    InterfOnclick interfOnclick;


    public interface InterfOnclick {
        void click(View view, int pos);
    }

    public defaultAdapter(Context context, int images[],defaultAdapter.InterfOnclick interfOnclick)
    {
        this.context = context;
        this.images = images;
        this.interfOnclick=interfOnclick;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View images = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_layout,parent,false);
        return new defaultAdapter.ViewHolder(images,viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        defaultAdapter.ViewHolder viewHolder=(defaultAdapter.ViewHolder) holder;
        viewHolder.pos=position;
        imageView.setImageResource(images[position]);

    }

    @Override
    public int getItemCount() {
        return images.length;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener
    {
        int pos;
        public ViewHolder(View itemView,int pos) {
            super(itemView);
            //this.pos=pos;
            imageView = (ImageView)itemView.findViewById(R.id.image_custom);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            interfOnclick.click(v,pos);
        }

    }

}
