package in.astudentzone.pranjal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AssetsFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    public AssetsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_assets, container, false);
        tabLayout = view.findViewById(R.id.tabLayoutAssets);
        viewPager2 = view.findViewById(R.id.viewPagerAssets);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(requireActivity().getSupportFragmentManager(),
                getLifecycle());
        viewPager2.setAdapter(viewPagerAdapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, true,
                true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Add Assets");
                        break;

                    case 1:
                        tab.setText("My Assets");
                        break;

                    case 2:
                        tab.setText("Status");
                        break;
                }
            }
        });

        tabLayoutMediator.attach();

        return view;
    }
}