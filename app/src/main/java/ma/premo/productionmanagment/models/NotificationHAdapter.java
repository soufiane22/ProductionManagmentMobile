package ma.premo.productionmanagment.models;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ma.premo.productionmanagment.R;

public class NotificationHAdapter extends RecyclerView.Adapter<NotificationHAdapter.ViewHolder>   implements  View.OnClickListener{

    Context context;
    List<Notification_Hours> notificationList;
    ProgressDialog progressDoalog;

    public NotificationHAdapter(Context context , List<Notification_Hours> list ){
        this.context = context;
        this.notificationList = list;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView date , shift,line,of,nbrOperators,Htotal,Hextra,Hdevolution,Hstopped,HnewProject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.Date);
            shift = itemView.findViewById(R.id.Shift);
            of= itemView.findViewById(R.id.Of);
            line = itemView.findViewById(R.id.Line);
            nbrOperators = itemView.findViewById(R.id.NbrOperator);
            Htotal = itemView.findViewById(R.id.Htotal);
            Hextra = itemView.findViewById(R.id.Hextrat);
            Hdevolution = itemView.findViewById(R.id.Hdevolution);
            Hstopped = itemView.findViewById(R.id.Hstopped);
            HnewProject = itemView.findViewById(R.id.HnewProject);

        }
    }

    @NonNull
    @Override
    public NotificationHAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_notification,viewGroup,false);
        progressDoalog = new ProgressDialog(context);
        return new ViewHolder(view);
    }

    Notification_Hours notif;
    @Override
    public void onBindViewHolder(@NonNull NotificationHAdapter.ViewHolder viewHolder, int i) {
        if (notificationList != null && notificationList.size() > 0){
            notif = notificationList.get(i);
           // System.out.println(i+"--"+notif.getId());
            viewHolder.date.setText(notif.getDate());
            viewHolder.line.setText(String.valueOf(notif.getLigne()));
            viewHolder.shift.setText(String.valueOf(notif.getShift()));
            viewHolder.of.setText(String.valueOf(notif.getOF()));
            viewHolder.nbrOperators.setText(String.valueOf(notif.getNbr_operateurs()));
            viewHolder.Htotal.setText(String.valueOf(notif.getTotal_h()));
            viewHolder.Hextra.setText(String.valueOf(notif.getH_sup()));
            viewHolder.Hdevolution.setText(String.valueOf(notif.getH_devolution()));
            viewHolder.Hstopped.setText(String.valueOf(notif.getH_arrete()));
            viewHolder.HnewProject.setText(String.valueOf(notif.getH_nouvau_projet()));

            int position = viewHolder.getAdapterPosition();

        }
        else {
            return;
        }


    }

    @Override
    public int getItemCount() {

        return notificationList.size();
    }

    @Override
    public void onClick(View v) {

    }


}
