package bhanu.applock.Adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bhanu.applock.MainActivity;
import bhanu.applock.R;
import bhanu.applock.Model.appDetails;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<appDetails> appList = new ArrayList<>();
    PackageManager mPackageManager;

    public AppListAdapter(Context context, ArrayList<appDetails> appList,PackageManager packageManager) {
        mContext = context;
        this.appList = appList;
        mPackageManager = packageManager;
            }

    @NonNull
    @Override
    public AppListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_list,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AppListAdapter.MyViewHolder holder, int position) {

        final appDetails details = appList.get(position);
        holder.appIcon.setImageDrawable(details.getAppIcon());
        //Picasso.get().load("nothing").error(icon).into(holder.appIcon);
        holder.appName.setText(details.getAppTitle());

        holder.appIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)mContext).playGif(details.getPackageName());
//                Glide.with(mContext)
//                        .load(R.drawable.unlock)
//                        .into(holder.appIcon);
            }
        });

    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView appIcon,lockImg;
        TextView appName;
        public MyViewHolder(View itemView) {
            super(itemView);
            appIcon = itemView.findViewById(R.id.app_row_img);
            appName = itemView.findViewById(R.id.app_row_title);
            lockImg = itemView.findViewById(R.id.lock_img);
        }
    }
}
