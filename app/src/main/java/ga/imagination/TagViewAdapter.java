package ga.imagination;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 44260 on 2016/1/6.
 */
public class TagViewAdapter extends RecyclerView.Adapter<TagViewViewHolder> {
    public List<BaseModel> list;

    public void setList(List<BaseModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addTag(int position, BaseModel model) {
        list.add(position, model);
        notifyDataSetChanged();
    }

    public void addTag(BaseModel model) {
        list.add(model);
        notifyDataSetChanged();
    }

    @Override
    public TagViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TagViewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, null));
    }

    @Override
    public void onBindViewHolder(TagViewViewHolder holder, int position) {
        holder.fillView(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}
