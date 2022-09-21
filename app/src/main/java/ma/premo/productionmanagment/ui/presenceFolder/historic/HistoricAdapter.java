package ma.premo.productionmanagment.ui.presenceFolder.historic;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.premo.productionmanagment.R;
import ma.premo.productionmanagment.models.Presence;
import ma.premo.productionmanagment.models.User;

public class HistoricAdapter extends RecyclerView.Adapter<HistoricAdapter.ViewHolder> implements  View.OnClickListener {

    Context context;
    List<Presence> listPresence = new ArrayList<>();
    Boolean isOnTextChange = false;
    Double sumHours = 0.0;



    private  LayoutInflater inflater ;


    public HistoricAdapter(Context context, List<Presence> listPresence ) {
        this.context = context;
        this.listPresence = listPresence;
         calculateSum();
    }


 public void setUserList(List<User> userList){
       // this.userList = userList;
        notifyDataSetChanged();
 }


    @NonNull
    @Override
    public HistoricAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

          View view = LayoutInflater.from(context).inflate(R.layout.historic_item,viewGroup,false);

          return new ViewHolder(view);

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView  date ,matricule , nom , prenom , fonction , line ,state ,shift , hours ;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
                date = itemView.findViewById(R.id.DateView);
                matricule = itemView.findViewById(R.id.Matricule);
                nom = itemView.findViewById(R.id.Nom);
                prenom = itemView.findViewById(R.id.Prenom);
                fonction = itemView.findViewById(R.id.Fonction);
                state = itemView.findViewById(R.id.state);
                line = itemView.findViewById(R.id.line);
                hours = itemView.findViewById(R.id.Hours);
               shift = itemView.findViewById(R.id.shiftPresence);

        }

        }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            sumHours=0.0;
            if (listPresence != null && listPresence.size() > 0){
                Presence presence = new Presence();
                presence = listPresence.get(i);
                viewHolder.date.setText(String.valueOf(presence.getDate()));
                viewHolder.matricule.setText(String.valueOf(presence.getMatriculePerson()));
                viewHolder.nom.setText(String.valueOf(presence.getNomPerson()));
                viewHolder.prenom.setText(String.valueOf(presence.getPrenomPerson()));
                viewHolder.fonction.setText(String.valueOf(presence.getFunctionPerson()));
                viewHolder.line.setText(String.valueOf(presence.getLine().getDesignation()));
                viewHolder.state.setText(String.valueOf(presence.getEtat()));
                viewHolder.shift.setText(String.valueOf(presence.getShift()));
                viewHolder.hours.setText(String.valueOf(presence.getNbrHeurs()));
                if(presence.getShift().equals("Morning"))
                    viewHolder.shift.setBackgroundColor(Color.rgb(226 ,196,3));
                if(presence.getShift().equals("Evening"))
                    viewHolder.shift.setBackgroundColor(Color.rgb(51 ,106,216));
                if(presence.getShift().equals("Night"))
                    viewHolder.shift.setBackgroundColor(Color.rgb(100 ,100,100));

               // sumHours += presence.getNbrHeurs();


                //int position = viewHolder.getAdapterPosition();
                //viewHolder.itemView.setTag("itemView "+position);

            }
            else{
                Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
                return;
            }

    }



    public List<Presence> getListPresence(){
        return listPresence;
    }

    public void setListPresence(List<Presence> listPresenceFiltred){
        this.listPresence = new ArrayList<>(listPresenceFiltred);
        calculateSum();
        notifyDataSetChanged();
    }

    public void calculateSum(){
        sumHours = 0.0;
        for(Presence p : listPresence){
            sumHours += p.getNbrHeurs();
        }

    }


    public Double getSumHours() {
        return sumHours;
    }

    @Override
    public int getItemCount() {
         return listPresence.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onClick(View v) {

    }
}
