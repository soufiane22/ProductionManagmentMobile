package ma.premo.productionmanagment.ui.group;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ma.premo.productionmanagment.R;
import ma.premo.productionmanagment.models.Line;
import ma.premo.productionmanagment.models.User;

public class LineGroupeAdapter extends RecyclerView.Adapter<LineGroupeAdapter.ViewHolder>{

    private Context context;
    private List<Line> lineGroupeList;
    private String mode = "";
    private int selectedPosition = -1;
    private LineClickListenner lineClickListenner;

    public LineGroupeAdapter(Context context, List<Line> lineList ,String mode, LineClickListenner lineClickListenner) {
        this.context = context;
        this.lineGroupeList = lineList;
        this.mode = mode;
        this.lineClickListenner = lineClickListenner;
    }

    public void setLineGroupeList(List<Line> lineList){
        this.lineGroupeList = lineList;
        System.out.println("new list"+lineGroupeList.toString());
        notifyDataSetChanged();
    }

    public List<Line> getLineGroupeList() {
        return lineGroupeList;
    }

    @NonNull
    @Override
    public LineGroupeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.line_group_item,viewGroup,false);
        return  new LineGroupeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LineGroupeAdapter.ViewHolder viewHolder, int i) {
        if (lineGroupeList != null && lineGroupeList.size() > 0){
          //  Line line = new Line();
            Line line;
            line  = lineGroupeList.get(i);
            viewHolder.line.setText(line.getDesignation());


            viewHolder.radioButton.setChecked(i == selectedPosition);
            viewHolder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        selectedPosition = viewHolder.getAdapterPosition();
                        lineClickListenner.onClick(line);

                    }
                }
            });



        }else{
            Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView line;
        ImageButton deleteButon;
        RadioButton radioButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            line = itemView.findViewById(R.id.lineView);
            deleteButon=itemView.findViewById(R.id.DeleteButton);
            radioButton =  itemView.findViewById(R.id.radioButton);
            if(mode.equals("All")){
                radioButton.setVisibility(View.VISIBLE);
                deleteButon.setVisibility(View.GONE);
            }else{
                radioButton.setVisibility(View.GONE);
                deleteButon.setVisibility(View.VISIBLE);
            }
            deleteButon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    lineGroupeList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, lineGroupeList.size());
                }
            });


        }


    }

    @Override
    public int getItemCount() {
        return lineGroupeList.size();
    }
}
