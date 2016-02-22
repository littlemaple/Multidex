package ga.imagination;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by 44260 on 2016/1/25.
 */
public class BaseTextAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_layout_item, null);
        return new RecyclerView.ViewHolder(view) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((TextView) holder.itemView.findViewById(R.id.tv_collapsing)).setText(position + "=>" + holder.itemView.getResources().getString(R.string.str_tag));
        holder.itemView.findViewById(R.id.tv_collapsing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    onItemClickListener listener;

    public void setListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public interface onItemClickListener {
        void onClick(View view);
    }
}
