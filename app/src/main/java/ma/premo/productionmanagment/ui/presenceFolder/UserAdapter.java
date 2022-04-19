package ma.premo.productionmanagment.ui.presenceFolder;

import android.arch.lifecycle.Observer;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import ma.premo.productionmanagment.R;
import ma.premo.productionmanagment.models.Groupe;
import ma.premo.productionmanagment.models.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> implements Filterable {

    Context context;
    private List<User> listUser = new ArrayList<>();
    private List<User> listUserFull = new ArrayList<>();
    private List<User> listUserSelected = new ArrayList<>();
    private  Userselected userselected;

    public UserAdapter(Context context, List<User> listUser, Userselected userListenner) {
        this.context = context;
        this.listUser = listUser;
        this.userselected = userListenner;
        listUserFull = new ArrayList<>(listUser);
    }

    public void setUserList(List<User> userList){
        this.listUser = userList;
        System.out.println("list user is updated");
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item,viewGroup,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder viewHolder, int i) {
        if (listUser != null && listUser.size() > 0){
            User user = listUser.get(i);
            viewHolder.checkBox.setOnCheckedChangeListener(null);
            viewHolder.matricule.setText(user.getMatricule()+"");
            viewHolder.lastname.setText(user.getNom()+"");
            viewHolder.firstname.setText(user.getPrenom()+"");
            viewHolder.function.setText(user.getFonction()+"");
            viewHolder.phone.setText(user.getTele()+"");
            viewHolder.checkBox.setChecked(user.getSelected());
/*
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            */


            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

              @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                  listUser.get(viewHolder.getAdapterPosition()).setSelected(isChecked);
                  /*
                    if(viewHolder.checkBox.isChecked()){
                        user.setSelected(true);
                        listUserSelected.add(user);
                    }else{
                        user.setSelected(false);
                        listUserSelected.remove(user);
                    }
                    */
                  if(isChecked)
                      listUserSelected.add(user);
                  else
                      listUserSelected.remove(user);


                    userselected.getUserCheked(listUserSelected);
                }
            });


        }else{
            Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
            return;
        }

    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView matricule , lastname, firstname , function , phone;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            matricule = itemView.findViewById(R.id.UMatricule);
            lastname = itemView.findViewById(R.id.ULastName);
            firstname = itemView.findViewById(R.id.UFirstName);
            function = itemView.findViewById(R.id.UFunction);
            phone = itemView.findViewById(R.id.UTele);
            checkBox = itemView.findViewById(R.id.checkBox);


            /*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    User currentuser= listUserFull.get(position);
                    if (checkBox.isChecked()){
                        currentuser.setSelected(true);
                        listUserSelected.add(currentuser);
                    }else if( !checkBox.isChecked()){
                        currentuser.setSelected(false);
                        listUserSelected.remove(currentuser);
                    }
                    userselected.getUserCheked(listUserSelected);

                }
            });

             */
        }
    }

    @Override
    public int getItemCount() {
        if (listUser != null){
            return listUser.size();
        }else{
            return 0;
        }


    }

    public interface Userselected {
        public void getUserCheked(List<User> list);
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<User> filtredList = new ArrayList<>();

            if(constraint == null || constraint.length()==0){
               //filtredList.addAll(listUserFull);
                results.count = listUserFull.size();
                results.values = listUserFull;
            }else{
                for(User u : listUserFull){
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    if(u.getNom().toLowerCase().startsWith(filterPattern) || String.valueOf(u.getMatricule()).startsWith(filterPattern)){
                        filtredList.add(u);
                    }
                }
                results.count = filtredList.size();
                results.values = filtredList;
            }

           // results.values = filtredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //listUser.clear();
            //listUser.addAll((List<User> results.values ));
            listUser = (ArrayList<User>) results.values;
            notifyDataSetChanged();
        }
    };

    public void getfilter(String str){
        ArrayList<User> myListUser = new ArrayList<>();
        if(str.equals(""))
            setUserList(listUserFull);
        else{
            for(User u : listUserFull){
                if(u.getNom().toLowerCase().startsWith(str.toLowerCase()) || String.valueOf(u.getMatricule()).startsWith(str)){
                    myListUser.add(u);
                }
            }
            setUserList(myListUser);
        }


    }
}
