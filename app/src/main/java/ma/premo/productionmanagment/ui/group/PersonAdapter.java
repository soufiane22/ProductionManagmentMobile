package ma.premo.productionmanagment.ui.group;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ma.premo.productionmanagment.MainActivity;
import ma.premo.productionmanagment.R;
import ma.premo.productionmanagment.models.PresenceGroup;
import ma.premo.productionmanagment.models.User;
import ma.premo.productionmanagment.ui.presenceFolder.PresenceAdapter;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder>{

    private Context context;
    private List<User> personsList;
    private EditlineInterface editlineInterface;
    private View editLineView;
    public PersonAdapter(Context context, List<User> personsList ,EditlineInterface editlineInterface) {
        this.context = context;
        this.personsList = personsList;
        this.editlineInterface =editlineInterface;
    }

    public void setPersonList(List<User> userList){
        this.personsList = userList;
        notifyDataSetChanged();
    }

    public List<User> getPersonsList() {
        return personsList;
    }

    @NonNull
    @Override
    public PersonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.person_item,viewGroup,false);
        editLineView = LayoutInflater.from(context).inflate(R.layout.popup_person,viewGroup,false);
        return  new PersonAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonAdapter.ViewHolder viewHolder, int i) {
        if (personsList != null && personsList.size() > 0){
            User user = new User();
            user = user = personsList.get(i);
            viewHolder.matricule.setText(String.valueOf(user.getMatricule()));
            viewHolder.nom.setText(String.valueOf(user.getNom()));
            viewHolder.prenom.setText(String.valueOf(user.getPrenom()));
            viewHolder.fonction.setText(String.valueOf(user.getFonction()));
            //viewHolder.phone.setText(String.valueOf(user.getTele()));
            if(user.getLine() != null)
            viewHolder.line.setText(user.getLine().getDesignation());
            else
                viewHolder.line.setText("null");
        }else{
            Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView matricule , nom , prenom , fonction ,phone , line;
        ImageButton deleteButon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            matricule = itemView.findViewById(R.id.PMatricule);
            nom = itemView.findViewById(R.id.PLastName);
            prenom = itemView.findViewById(R.id.PFirstName);
            fonction = itemView.findViewById(R.id.PFunction);
            //phone= itemView.findViewById(R.id.PhoneP);
            line = itemView.findViewById(R.id.LineP);
            deleteButon=itemView.findViewById(R.id.DeleteButton);
            deleteButon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    personsList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, personsList.size());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    User u = personsList.get(position);



                    /*
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.popup_person);
                    dialog.getWindow().setLayout(600,650);
                    dialog.show();

                     */
                    editlineInterface.editLine(u.getId(),position);

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return personsList.size();
    }
}

 interface EditlineInterface{
    public  void editLine(String id , int position);
}
