package ma.premo.productionmanagment.ui.Presence;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;

import ma.premo.productionmanagment.R;
import ma.premo.productionmanagment.databinding.FragmentPresenceBinding;

public class PresenceFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private FragmentPresenceBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(SlideshowViewModel.class);
        View view =  inflater.inflate(R.layout.fragment_presence, container, false);

        binding = FragmentPresenceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}