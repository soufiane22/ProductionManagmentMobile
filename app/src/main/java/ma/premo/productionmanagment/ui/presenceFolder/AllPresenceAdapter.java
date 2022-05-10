package ma.premo.productionmanagment.ui.presenceFolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ma.premo.productionmanagment.R;
import ma.premo.productionmanagment.databinding.FragmentPresenseBinding;
import ma.premo.productionmanagment.models.DeclarationPresence;

public class AllPresenceAdapter extends  RecyclerView.Adapter<AllPresenceAdapter.ViewHolder>{

private Context context;
private LayoutInflater inflater;
private ArrayList<DeclarationPresence> listDeclarationPresence=new ArrayList();


    public AllPresenceAdapter(Context context, ArrayList<DeclarationPresence> listDeclarationPresence) {
        this.context = context;
        this.listDeclarationPresence = listDeclarationPresence;
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
        View view = LayoutInflater.from(context).inflate(R.layout.all_presence_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (listDeclarationPresence != null){
            DeclarationPresence declaration = new DeclarationPresence();
             declaration = listDeclarationPresence.get(i);
             viewHolder.group.setText(declaration.getGroup()+"");
            viewHolder.date.setText(declaration.getDate()+"");
            viewHolder.leader.setText(declaration.getLeader()+"");
            viewHolder.engineer.setText(declaration.getEngineer()+"");
            viewHolder.sumOperators.setText(declaration.getSumOperators()+"");
            viewHolder.totalHours.setText(declaration.getTotalHours()+"");

        }else{
            Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return listDeclarationPresence.size();
    }


public static class ViewHolder extends RecyclerView.ViewHolder {
public TextView group , date , leader , engineer,sumOperators, totalHours ;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        group = itemView.findViewById(R.id.IGroupe);
        date = itemView.findViewById(R.id.IDate);
        leader = itemView.findViewById(R.id.ILeader);
        engineer = itemView.findViewById(R.id.IEngineer);
        sumOperators = itemView.findViewById(R.id.ISumOperators);
        totalHours = itemView.findViewById(R.id.ITotalHours);

    }
}
}
