package ma.premo.productionmanagment.ui.presenceFolder;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.List;

import ma.premo.productionmanagment.R;
import ma.premo.productionmanagment.Utils.JsonConvert;
import ma.premo.productionmanagment.models.PresenceGroup;

public class PresenceGroupAdapter extends  RecyclerView.Adapter<PresenceGroupAdapter.ViewHolder>{

private Context context;
private LayoutInflater inflater;
private List<PresenceGroup> listPresenceGroupe ;
private String idLeader ="" ;
private DeleteitemLestener deleteitemLestener ;
private NavController navController;


    public PresenceGroupAdapter(Context context, List<PresenceGroup> listDeclarationPresence , String idLeader ,DeleteitemLestener deleteitemLestener ) {
        this.context = context;
        this.listPresenceGroupe = listDeclarationPresence;
        this.idLeader = idLeader;
        this.deleteitemLestener = deleteitemLestener;
    }

    public void setListPresenceGroupe(List<PresenceGroup> listPresenceGroupe) {
        this.listPresenceGroupe = listPresenceGroupe;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        /*
        if(inflater == null){
             inflater = LayoutInflater.from(viewGroup.getContext());
        }
        FragmentPresenseBinding binding = FragmentPresenseBinding.inflate(inflater ,viewGroup , false);

         */
        View view = LayoutInflater.from(context).inflate(R.layout.groupe_presence_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        PresenceGroup declaration = new PresenceGroup();
        if (listPresenceGroupe != null && listPresenceGroupe.size() > 0){
             declaration = listPresenceGroupe.get(i);
             viewHolder.group.setText(declaration.getGroup()+"");
            viewHolder.status.setText(declaration.getStatus()+"");
            viewHolder.date.setText(declaration.getDate()+"");
            viewHolder.shift.setText(declaration.getShift()+"");
            viewHolder.leader.setText(declaration.getLeaderName());
            viewHolder.engineer.setText(declaration.getEngineer()+"");
            viewHolder.sumOperators.setText(declaration.getTotalOperators()+"");
            viewHolder.totalHours.setText(declaration.getSumHours()+"");

            if(declaration.getStatus().equals("Validate")){
                viewHolder.status.setTextColor(context.getResources().getColor(R.color.green));
            }else{
                viewHolder.status.setTextColor(context.getResources().getColor(R.color.orange1));
            }

            if(!idLeader.equals(declaration.getLeaderId())){
                viewHolder.deleteButton.setVisibility(View.GONE);
            }


        }else{
            Toast.makeText(context,"no data available",Toast.LENGTH_SHORT).show();
            //viewHolder. noData.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return listPresenceGroupe.size();
    }


public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView group , date , leader , engineer,sumOperators, totalHours,shift ,noData , status;
    public ImageButton deleteButton;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        group = itemView.findViewById(R.id.IGroupe);
        status = itemView.findViewById(R.id.IStatus);
        date = itemView.findViewById(R.id.IDate);
        leader = itemView.findViewById(R.id.ILeader);
        engineer = itemView.findViewById(R.id.IEngineer);
        sumOperators = itemView.findViewById(R.id.ISumOperators);
        totalHours = itemView.findViewById(R.id.ITotalHours);
        shift = itemView.findViewById(R.id.IShift);
        deleteButton = itemView.findViewById(R.id.IDeleteButton);
        noData = itemView.findViewById(R.id.INodata);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                PresenceGroup p = listPresenceGroupe.get(position);
                    Bundle bundle = new Bundle();
                    String presenceJsonString = JsonConvert.getGsonParser().toJson(p);
                    bundle.putString("presenceJsonString",presenceJsonString);
                    bundle.putString("mode","update");
                    //AppCompatActivity activity
                    navController = Navigation.findNavController(itemView);
                    navController.navigate(R.id.add_presence,bundle);

                /*
                else{
                    Toast.makeText(context,"You can't update this declaration", Toast.LENGTH_LONG).show();
                }

                 */
     ;

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PresenceGroup p = new PresenceGroup();
                int position = getAdapterPosition();
                p = listPresenceGroupe.get(position);

                if(p.getStatus().equals("Invalidate")){
                    deleteitemLestener.deleteitem(p.getId() , position);
                }else{
                    Toast.makeText(context,"You can't delete this declaration", Toast.LENGTH_LONG).show();
                }

               // personsList.remove(position);
               // notifyItemRemoved(position);
              //  notifyItemRangeChanged(position, personsList.size());
            }
        });
    }
}
}

