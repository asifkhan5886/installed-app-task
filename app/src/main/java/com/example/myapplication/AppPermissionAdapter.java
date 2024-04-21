package com.example.myapplication;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AppPermissionAdapter extends RecyclerView.Adapter<AppPermissionAdapter.ViewHolder> {

    private Context context;
    private List<ApplicationInfo> installedApps;
    private PackageManager packageManager;

    public AppPermissionAdapter(Context context, List<ApplicationInfo> installedApps, PackageManager packageManager) {
        this.context = context;
        this.installedApps = installedApps;
        this.packageManager = packageManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_permission, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApplicationInfo appInfo = installedApps.get(position);
        String appName = appInfo.loadLabel(packageManager).toString();
        holder.appName.setText("App Name: " + appName);

        String packageName = appInfo.packageName;
        holder.packageName.setText("Package Name: " + packageName);

        String permissionsText = getPermissionsText(packageName);
        holder.permissions.setText("Permissions: \n" + permissionsText);
    }

    @Override
    public int getItemCount() {
        return installedApps.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView appName;
        TextView packageName;
        TextView permissions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appName = itemView.findViewById(R.id.app_name);
            packageName = itemView.findViewById(R.id.package_name);
            permissions = itemView.findViewById(R.id.permissions);
        }
    }

    private String getPermissionsText(String packageName) {
        StringBuilder permissionsText = new StringBuilder();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            String[] permissions = packageInfo.requestedPermissions;
            if (permissions != null) {
                for (String permission : permissions) {
                    try {
                        PermissionInfo permissionInfo = packageManager.getPermissionInfo(permission, 0);
                        String permissionName = permissionInfo.loadLabel(packageManager).toString();
                        permissionsText.append(permissionName).append("\n");
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return permissionsText.toString();
    }
}
