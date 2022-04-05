package ma.premo.productionmanagment.ui.Presence;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ma.premo.productionmanagment.R;


public class AddPresenceFragment extends Fragment {



    public AddPresenceFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
          View  view = inflater.inflate(R.layout.fragment_add_presence, container, false);
        return view;
    }
}