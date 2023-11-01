package com.example.olympinav;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.olympinav.generators.Generator;
import com.example.olympinav.models.ServiceUpdate;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ServiceUpdatesActivity extends BaseActivity {

    private List<ServiceUpdate> serviceUpdates;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_updates);
        setupActivity("Service Updates");

        final int serviceUpdatesCount = 12;
        serviceUpdates = new ArrayList<>(serviceUpdatesCount);
        for (int i = 0; i < serviceUpdatesCount; i++)
            serviceUpdates.add(Generator.generateServiceUpdate());
        Log.i("HHH", "onCreate: " + serviceUpdates.size());
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ServiceUpdatesAdapter adapter = new ServiceUpdatesAdapter(serviceUpdates);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private class ServiceUpdatesAdapter extends RecyclerView.Adapter<ServiceUpdateViewHolder> {
        private List<ServiceUpdate> serviceUpdates;

        public ServiceUpdatesAdapter(List<ServiceUpdate> serviceUpdates) {
            this.serviceUpdates = serviceUpdates;
        }

        @NonNull
        @Override
        public ServiceUpdateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(ServiceUpdatesActivity.this).inflate(R.layout.row_service_update, parent, false);
            return new ServiceUpdateViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ServiceUpdateViewHolder holder, int position) {
            ServiceUpdate su = serviceUpdates.get(position);
            holder.icon.setImageDrawable(getResources().getDrawable(su.getIcon()));
            holder.message.setText(su.getMessage());
            holder.lastUpdatedAt.setText("Last Updated: " + su.getLastUpdated().format(DateTimeFormatter.ofPattern("mm")) + " minutes ago");
        }

        @Override
        public int getItemCount() {
            return serviceUpdates.size();
        }
    }

    private class ServiceUpdateViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView message;
        private TextView lastUpdatedAt;

        public ServiceUpdateViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            message = itemView.findViewById(R.id.message);
            lastUpdatedAt = itemView.findViewById(R.id.lastUpdated);
        }
    }
}
