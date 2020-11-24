package com.project.githubsearchrepo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.githubsearchrepo.Listener.RecyclerViewItemClick;
import com.project.githubsearchrepo.Model.Repository;
import com.project.githubsearchrepo.R;

import java.util.List;

public class AdapterRepository extends RecyclerView.Adapter<AdapterRepository.ViewHolder> {

    private RecyclerViewItemClick clickListener;
    private List<Repository> items;
    private Context context;

    public AdapterRepository(Context context, List<Repository> items, RecyclerViewItemClick clickListener) {
        this.items          = items;
        this.clickListener  = clickListener;
        this.context        = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOwnerName;
        TextView tvRepoName;
        ImageView ivOwnerImage;
        View parentView;

        ViewHolder(View v) {
            super(v);
            tvOwnerName     = v.findViewById(R.id.tvOwnerName);
            tvRepoName      = v.findViewById(R.id.tvRepoName);
            ivOwnerImage    = v.findViewById(R.id.ivOwnerImage);
            parentView      = v.findViewById(R.id.lyt_parent);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_repo, parent, false);
        context = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Repository repository = getItem(position);
        holder.tvOwnerName.setText(repository.getOwner().getLogin());
        holder.tvRepoName.setText(repository.getFull_name());

        Glide.with(context).load(repository.getOwner().getAvatar_url()).placeholder(R.drawable.ic_logo).into(holder.ivOwnerImage);

        holder.parentView.setTag(position);
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener == null) return;
                clickListener.viewClickForRepo(v, repository, (int) v.getTag());
            }
        });

        holder.ivOwnerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener == null) return;
                clickListener.viewClickForUser(v, repository, position);
            }
        });
    }

    private Repository getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}