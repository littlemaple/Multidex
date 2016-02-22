package ga.imagination;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ga.imagination.lib.Tag;
import ga.imagination.lib.TagView;

/**
 * Created by 44260 on 2016/1/6.
 */
public class TagViewViewHolder extends RecyclerView.ViewHolder {
    private TextView textView;
    private TagView tagView;

    public TagViewViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.tv_head);
        tagView = (TagView) itemView.findViewById(R.id.tag_view);
    }

    public void fillView(BaseModel model) {
        textView.setText(model.title);
        tagView.addTags(model.getTags());
    }
}
