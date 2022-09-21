package ma.premo.productionmanagment.ui.presenceFolder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.provider.CalendarContract;
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

public class PresenceAdapter extends RecyclerView.Adapter<PresenceAdapter.ViewHolder> implements  View.OnClickListener {

    Context context;
    List<Presence> listPresence = new ArrayList<>();
    Boolean isOnTextChange = false;
    String mode ;
    Map<Integer, Integer> mSpinnerSelectedItem = new HashMap<Integer, Integer>();
    Map<Integer, Integer> shiftSpinnerSelectedItem = new HashMap<Integer, Integer>();
    private  LayoutInflater inflater ;

    private  ArrayAdapter<CharSequence> adapterSate;
    private  ArrayAdapter<CharSequence> adapterShift;
    private String status;

    public PresenceAdapter(Context context,List<Presence> listPresence , String mode , String status) {
        this.context = context;
        this.listPresence = listPresence;
        this.mode = mode;
        this.status = status;
    }


 public void setUserList(List<User> userList){
       // this.userList = userList;
        notifyDataSetChanged();
 }


    @NonNull
    @Override
    public PresenceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

          View view = LayoutInflater.from(context).inflate(R.layout.presence_item,viewGroup,false);

          return new ViewHolder(view);

    }


    String state;
    String shift = " ";
    public class ViewHolder extends RecyclerView.ViewHolder{
        int view_type;
        //Variable for list
        TextView matricule , nom , prenom , fonction , line ;
        EditText nbrHours;
        Spinner stateSpinner;
        Spinner shiftSpinner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
                matricule = itemView.findViewById(R.id.Matricule);
                nom = itemView.findViewById(R.id.Nom);
                prenom = itemView.findViewById(R.id.Prenom);
                fonction = itemView.findViewById(R.id.Fonction);
                stateSpinner = itemView.findViewById(R.id.stateSpinner);
                line = itemView.findViewById(R.id.line);
                nbrHours = itemView.findViewById(R.id.NbrHours);
               shiftSpinner = itemView.findViewById(R.id.shiftSpinner);



                stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        state  = parent.getItemAtPosition(position).toString();
                        listPresence.get(getAdapterPosition()).setEtat(state);
                        mSpinnerSelectedItem.put(getAdapterPosition(), position);


                       if (!state.equals("Present") && !state.equals("Retard")){
                           nbrHours.setEnabled(false);
                           nbrHours.setText("0");

                       }else{
                           if(!mode.equals("view") && !status.equals("Validate"))
                           nbrHours.setEnabled(true);
                           //if(mode.equals("add"))
                          // nbrHours.setText("");
                       }
                       // Toast.makeText(parent.getContext(),state,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                shiftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        shiftSpinnerSelectedItem.put(getAdapterPosition(), i);
                        shift = shiftSpinner.getSelectedItem().toString();
                        System.out.println("shift selected "+shift);
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                        listPresence.get(getAdapterPosition()).setShift(shiftSpinner.getSelectedItem().toString());
                        if(shift.equals("Morning"))
                            shiftSpinner.setBackgroundColor(Color.rgb(226 ,196,3));
                        if(shift.equals("Evening"))
                            shiftSpinner.setBackgroundColor(Color.rgb(51 ,106,216));
                        if(shift.equals("Night"))
                            shiftSpinner.setBackgroundColor(Color.rgb(100 ,100,100));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                nbrHours.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int p, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int p, int i1, int i2) {
                    // editModelArrayList.get(g).setEditTextValue(editText.getText().toString());
                    isOnTextChange= true;
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(!editable.toString().equals("") && isOnTextChange){
                        listPresence.get(getAdapterPosition()).setNbrHeurs(Double.parseDouble(nbrHours.getText().toString()));
                        isOnTextChange = false;

                    }

                    else{
                        // nbrHours.setText("0");
                        //  listPresence.get(getAdapterPosition()).setNbrHeurs(Integer.parseInt(nbrHours.getText().toString()));
                    }

                }
            });

        }

        }





    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        adapterSate = ArrayAdapter.createFromResource(context,R.array.state, android.R.layout.simple_spinner_item);
        adapterSate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapterShift =ArrayAdapter.createFromResource(context,R.array.shift, android.R.layout.simple_spinner_item);
        adapterShift.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewHolder.shiftSpinner.setAdapter(adapterShift);
        viewHolder.stateSpinner.setAdapter(adapterSate);

        if (mSpinnerSelectedItem.containsKey(i)) {
            viewHolder.stateSpinner.setSelection(mSpinnerSelectedItem.get(i));
        }

        if (shiftSpinnerSelectedItem.containsKey(i)) {
            viewHolder.shiftSpinner.setSelection(shiftSpinnerSelectedItem.get(i));
        }

            if (listPresence != null && listPresence.size() > 0){
                Presence presence = new Presence();
                presence = listPresence.get(i);
                viewHolder.matricule.setText(String.valueOf(presence.getMatriculePerson()));
                viewHolder.nom.setText(String.valueOf(presence.getNomPerson()));
                viewHolder.prenom.setText(String.valueOf(presence.getPrenomPerson()));
                viewHolder.fonction.setText(String.valueOf(presence.getFunctionPerson()));
                viewHolder.line.setText(String.valueOf(presence.getLine()));
                int position = viewHolder.getAdapterPosition();
                viewHolder.itemView.setTag("itemView "+position);
                viewHolder.shiftSpinner.setSelection(adapterShift.getPosition(presence.getShift()));
                //getStatePosion(viewHolder.stateSpinner,presence.getEtat());
                viewHolder.stateSpinner.setSelection(adapterSate.getPosition(presence.getEtat()));
                if(mode.equals("update")){
                   viewHolder.nbrHours.setText(String.valueOf(presence.getNbrHeurs()));
                    if(mode.equals("view")|| status.equals("Validate")){
                        viewHolder.nbrHours.setEnabled(false);
                        viewHolder.stateSpinner.setEnabled(false);
                        viewHolder.shiftSpinner.setEnabled(false);
                    }
                }



            }
            else{
                Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
                return;
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

    public List<Presence> getListPresence(){
        return listPresence;
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
