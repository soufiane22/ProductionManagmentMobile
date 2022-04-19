package ma.premo.productionmanagment.ui.presenceFolder;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProvider;

import ma.premo.productionmanagment.R;
import ma.premo.productionmanagment.databinding.FragmentPresenseBinding;

public class PresenceFragment extends Fragment {

    private PresenceViewModel presenceViewModel;
    private FragmentPresenseBinding binding;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenceViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(PresenceViewModel.class);
       // View view = inflater.inflate(R.layout.fragment_presense, container, false);
        binding = FragmentPresenseBinding.inflate(inflater, container, false);
        binding.setViewModel(presenceViewModel);
        binding.setLifecycleOwner(this);
        View view  = binding.getRoot();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        //presenceViewModel.getText().observe(getViewLifecycleOwner(), binding.text::setText);

        binding.AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPresenceFragment addPresenceFragment = new AddPresenceFragment();
                Bundle bundle = new Bundle();
                bundle.putString("mode","add");
                addPresenceFragment.setArguments(bundle);
                fragmentTransaction.setReorderingAllowed(true);
                fragmentTransaction.replace( R.id.nav_host_fragment_content_main,addPresenceFragment,null).addToBackStack(null);
                fragmentTransaction.commit();
            }


        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }
}