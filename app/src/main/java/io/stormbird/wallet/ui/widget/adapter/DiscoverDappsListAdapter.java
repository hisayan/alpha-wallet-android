package io.stormbird.wallet.ui.widget.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.net.URISyntaxException;
import java.util.List;

import io.stormbird.wallet.R;
import io.stormbird.wallet.entity.DApp;
import io.stormbird.wallet.ui.widget.OnDappAddedListener;
import io.stormbird.wallet.ui.widget.OnDappClickListener;
import io.stormbird.wallet.ui.widget.OnDappRemovedListener;
import io.stormbird.wallet.util.DappBrowserUtils;
import io.stormbird.wallet.util.Utils;

public class DiscoverDappsListAdapter extends RecyclerView.Adapter<DiscoverDappsListAdapter.ViewHolder> {
    private List<DApp> data;
    private OnDappClickListener listener;
    private OnDappAddedListener onDappAddedListener;
    private OnDappRemovedListener onDappRemovedListener;

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name, description, remove, add;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            remove = itemView.findViewById(R.id.remove);
            add = itemView.findViewById(R.id.add);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
        }
    }

    public DiscoverDappsListAdapter(List<DApp> data,
                                    OnDappClickListener listener,
                                    OnDappAddedListener onDappAddedListener,
                                    OnDappRemovedListener onDappRemovedListener) {
        this.data = data;
        this.listener = listener;
        this.onDappAddedListener = onDappAddedListener;
        this.onDappRemovedListener = onDappRemovedListener;
    }

    public void setDapps(List<DApp> dapps) {
        this.data = dapps;
    }

    @NonNull
    @Override
    public DiscoverDappsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_discover_dapps, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverDappsListAdapter.ViewHolder viewHolder, int i) {
        DApp dApp = data.get(i);
        String visibleUrl = null;
        try {
            visibleUrl = Utils.getDomainName(dApp.getUrl());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        viewHolder.name.setText(dApp.getName());
        viewHolder.description.setText(dApp.getDescription());

        String favicon;
        if (visibleUrl != null) {
            favicon = DappBrowserUtils.getIconUrl(visibleUrl);
            Glide.with(viewHolder.icon.getContext())
                    .load(favicon)
                    .apply(new RequestOptions()
                            .placeholder(R.mipmap.ic_launcher)
                            .circleCrop()
                            .fitCenter())
                    .into(viewHolder.icon);

            viewHolder.icon.setOnClickListener(v -> {
                listener.onDappClick(dApp);
            });
        }


        if (dApp.isAdded()) {
            viewHolder.add.setVisibility(View.GONE);
            viewHolder.remove.setVisibility(View.VISIBLE);
        } else {
            viewHolder.add.setVisibility(View.VISIBLE);
            viewHolder.remove.setVisibility(View.GONE);
        }

        viewHolder.add.setOnClickListener(v -> {
            onDappAddedListener.onDappAdded(dApp);
            viewHolder.add.setVisibility(View.GONE);
            viewHolder.remove.setVisibility(View.VISIBLE);
        });

        viewHolder.remove.setOnClickListener(v -> {
            onDappRemovedListener.onDappRemoved(dApp);
            viewHolder.add.setVisibility(View.VISIBLE);
            viewHolder.remove.setVisibility(View.GONE);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
