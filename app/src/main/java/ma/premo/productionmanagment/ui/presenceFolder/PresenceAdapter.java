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
import ma.premo.productionmanagment.models.User;

public class PresenceAdapter extends RecyclerView.Adapter<PresenceAdapter.ViewHolder> implements  View.OnClickListener {

    Context context;
    List<User> userList;
    ArrayList listLine = new ArrayList() ;
    private  LayoutInflater inflater ;
    private Spinner stateSpinner;
    private static final  int TYPE_HEAD = 0;
    private static final  int TYPE_LIST = 1;

    public PresenceAdapter(Context context, List<User> userList, ArrayList listLine) {
        this.context = context;
        this.userList = userList;
        this.listLine = listLine;
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
        TextView matricule , nom , prenom , fonction ;
        EditText nbrHours;
        Spinner stateSpinner , lineSpinner , shiftSpinner;
        public ViewHolder(@NonNull View itemView, int view_type) {
            super(itemView);

                matricule = itemView.findViewById(R.id.Matricule);
                nom = itemView.findViewById(R.id.Nom);
                prenom = itemView.findViewById(R.id.Prenom);
                fonction = itemView.findViewById(R.id.Fonction);
                stateSpinner = itemView.findViewById(R.id.stateSpinner);
                shiftSpinner = itemView.findViewById(R.id.shifSpinner);
                lineSpinner = itemView.findViewById(R.id.lineSpinner);
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
            if (userList != null && userList.size() > 0){
                User user = new User();
                user = user = userList.get(i);
                viewHolder.matricule.setText(String.valueOf(user.getMatricule()));
                viewHolder.nom.setText(String.valueOf(user.getNom()));
                viewHolder.prenom.setText(String.valueOf(user.getPrenom()));
                viewHolder.fonction.setText(String.valueOf(user.getFonction()));
                int position = viewHolder.getAdapterPosition();

                ArrayAdapter<CharSequence> adapterSate = ArrayAdapter.createFromResource(context,R.array.state, android.R.layout.simple_spinner_item);
                adapterSate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewHolder.stateSpinner.setAdapter(adapterSate);

                ArrayAdapter<CharSequence> adapterShift = ArrayAdapter.createFromResource(context,R.array.shift, android.R.layout.simple_spinner_item);
                adapterSate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewHolder.shiftSpinner.setAdapter(adapterShift);

                 ArrayAdapter<String> adapterLine = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, listLine);
                 adapterLine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                 viewHolder.lineSpinner.setAdapter(adapterLine);
            }
            else{
                Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
                return;
            }



    }

    @Override
    public int getItemCount() {

        return userList.size();
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
