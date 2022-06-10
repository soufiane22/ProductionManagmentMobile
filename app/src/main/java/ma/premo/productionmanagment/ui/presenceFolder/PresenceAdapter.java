package ma.premo.productionmanagment.ui.presenceFolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import java.util.List;


import ma.premo.productionmanagment.R;
import ma.premo.productionmanagment.models.Presence;
import ma.premo.productionmanagment.models.User;

public class PresenceAdapter extends RecyclerView.Adapter<PresenceAdapter.ViewHolder> implements  View.OnClickListener {

    Context context;
    List<User> userList;
    List<Presence> listPresence = new ArrayList<>();
    String mode ;
    private  LayoutInflater inflater ;
    private static final  int TYPE_HEAD = 0;
    private static final  int TYPE_LIST = 1;
    private  ArrayAdapter<CharSequence> adapterSate;

    public PresenceAdapter(Context context, List<User> userList,List<Presence> listPresence , String mode) {
        this.context = context;
        this.userList = userList;
        this.listPresence = listPresence;
        this.mode = mode;
    }

 public void setUserList(List<User> userList){
        this.userList = userList;
        System.out.println("list user is updated");
        notifyDataSetChanged();
 }


    @NonNull
    @Override
    public PresenceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

          View view = LayoutInflater.from(context).inflate(R.layout.presence_item,viewGroup,false);
          return new ViewHolder(view,viewType);

    }


    String state;
    public class ViewHolder extends RecyclerView.ViewHolder{
        int view_type;
        //Variable for list
        TextView matricule , nom , prenom , fonction , line ;
        EditText nbrHours;
        Spinner stateSpinner;

        public ViewHolder(@NonNull View itemView, int view_type) {
            super(itemView);
                matricule = itemView.findViewById(R.id.Matricule);
                nom = itemView.findViewById(R.id.Nom);
                prenom = itemView.findViewById(R.id.Prenom);
                fonction = itemView.findViewById(R.id.Fonction);
                stateSpinner = itemView.findViewById(R.id.stateSpinner);
                line = itemView.findViewById(R.id.line);
                nbrHours = itemView.findViewById(R.id.NbrHours);

                stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        state  = parent.getItemAtPosition(position).toString();
                       if (!state.equals("Present") && !state.equals("Retard")){
                           nbrHours.setVisibility(EditText.GONE);
                           nbrHours.setText("0");
                       }else{
                           nbrHours.setVisibility(EditText.VISIBLE);
                       }
                       // Toast.makeText(parent.getContext(),state,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        }

        }





    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            //System.out.println("2---view_type list ======>"+viewHolder.view_type);

        adapterSate = ArrayAdapter.createFromResource(context,R.array.state, android.R.layout.simple_spinner_item);
        adapterSate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewHolder.stateSpinner.setAdapter(adapterSate);

        if(mode.equals("add")){
            if (userList != null && userList.size() > 0){
                User user = new User();
                user = user = userList.get(i);
                viewHolder.matricule.setText(String.valueOf(user.getMatricule()));
                viewHolder.nom.setText(String.valueOf(user.getNom()));
                viewHolder.prenom.setText(String.valueOf(user.getPrenom()));
                viewHolder.fonction.setText(String.valueOf(user.getFonction()));
                viewHolder.line.setText(String.valueOf(user.getLine()));
                int position = viewHolder.getAdapterPosition();

            }
            else{
                Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
                return;
            }
        }else{
            Presence presence = new Presence();
            presence = listPresence.get(i);
            viewHolder.matricule.setText(String.valueOf(presence.getMatriculePerson()));
            viewHolder.nom.setText(String.valueOf(presence.getNomPerson()));
            viewHolder.prenom.setText(String.valueOf(presence.getPrenomPerson()));
            viewHolder.fonction.setText(String.valueOf(presence.getFunctionPerson()));
            viewHolder.line.setText(String.valueOf(presence.getLine()));
           // viewHolder.stateSpinner.setSelection(getStatePosion(viewHolder.stateSpinner,presence.getEtat()));
            viewHolder.nbrHours.setText(String.valueOf(presence.getNbrHeurs()));
            getStatePosion(viewHolder.stateSpinner,presence.getEtat());
            if(mode.equals("view")){
                viewHolder.nbrHours.setEnabled(false);
                viewHolder.stateSpinner.setEnabled(false);
            }

        }




    }

    private  void getStatePosion(Spinner spinner, String state) {
        for(int i = 0; i <adapterSate.getCount(); i++)
        {
            if (adapterSate.getItem(i).equals(state) )
            {
                spinner.setSelection(i); //(false is optional)
                break;
            }
        }
    }



    @Override
    public int getItemCount() {
     if(mode.equals("add")){
         return userList.size();
     }else{
         return listPresence.size();
     }




    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEAD;
            return TYPE_LIST;
    }

    @Override
    public void onClick(View v) {

    }
}
