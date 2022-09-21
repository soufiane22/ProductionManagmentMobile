package ma.premo.productionmanagment.ui.statistics;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ma.premo.productionmanagment.R;
import ma.premo.productionmanagment.models.Statistic;

public class StatisticAdapter  extends  RecyclerView.Adapter<StatisticAdapter.ViewHolder>{
    private Context context;
    private List<Statistic> listStatistics ;

    public StatisticAdapter(Context context , List<Statistic> listStatisticss){
        this.context = context;
        this.listStatistics =listStatisticss;
    }
    @NonNull
    @Override
    public StatisticAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.statistic_item,viewGroup,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull StatisticAdapter.ViewHolder viewHolder, int i) {
        Statistic st = new Statistic();
        if (listStatistics!= null && listStatistics.size() > 0){
            st = listStatistics.get(i);
           // double prod = (double) Math.round(st.getProductivity() * 100) / 100;
          //  double scrapratio = (double) Math.round(st.getTauxScrap() * 100) / 100;
            viewHolder.leader.setText(st.getLeaderName()+"");
            viewHolder.sumOutput.setText(st.getSumOutput()+"");
            viewHolder.productivity.setText(st.getProductivity()+"%");
            viewHolder.scrapRatio.setText(st.getTauxScrap()+"%");
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView leader , sumOutput, productivity , scrapRatio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leader = itemView.findViewById(R.id.gLeader);
            sumOutput = itemView.findViewById(R.id.sumOutput);
            productivity = itemView.findViewById(R.id.gvProductivity);
            scrapRatio = itemView.findViewById(R.id.gvScrapRatio);
        }
    }

    @Override
    public int getItemCount() {
        if (listStatistics != null)
            return listStatistics.size();
        return 0;
    }


}
