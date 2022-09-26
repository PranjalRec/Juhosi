package in.astudentzone.pranjal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAssetsAdapter extends RecyclerView.Adapter<MyAssetsViewHolder> {

    ArrayList<MyAssetsModel> myAssetsList;
    Context context;

    public MyAssetsAdapter(ArrayList<MyAssetsModel> myAssetsList, Context context) {
        this.myAssetsList = myAssetsList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAssetsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_assets_card,parent, false);
        return new MyAssetsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAssetsViewHolder holder, int position) {
        holder.textViewPolicyName.setText(myAssetsList.get(position).getPolicyName());
        holder.textViewPolicyNumber.setText(myAssetsList.get(position).getPolicyNumber());
        holder.textViewPremium.setText(myAssetsList.get(position).getPremium());
        holder.textViewDueDate.setText(myAssetsList.get(position).getDueDate());
    }

    @Override
    public int getItemCount() {
        return myAssetsList.size();
    }
}

 class MyAssetsViewHolder extends RecyclerView.ViewHolder {

    TextView textViewPolicyName, textViewPolicyNumber, textViewPremium, textViewDueDate;
     public MyAssetsViewHolder(@NonNull View itemView) {
         super(itemView);

         textViewPolicyName = itemView.findViewById(R.id.textViewPolicyNameMyAssets);
         textViewPolicyNumber = itemView.findViewById(R.id.textViewPolicyNumberMyAssets);
         textViewPremium = itemView.findViewById(R.id.textViewPremiumMyAssets);
         textViewDueDate = itemView.findViewById(R.id.textViewDueDateMyAssets);
     }
 }
