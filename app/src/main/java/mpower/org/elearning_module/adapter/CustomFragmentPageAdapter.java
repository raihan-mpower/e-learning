package mpower.org.elearning_module.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import mpower.org.elearning_module.fragments.CategoriesFragment;
import mpower.org.elearning_module.fragments.FavoritesFragment;
import mpower.org.elearning_module.fragments.HomeFragment;
import mpower.org.elearning_module.fragments.SavedFragment;


/**
 * Created by sabbir on 4/10/17.
 * @author Sabbir ,sabbir@mpower-social.com
 */

public class CustomFragmentPageAdapter extends FragmentStatePagerAdapter {
    private static final int FRAGMENT_COUNT = 4;

    public CustomFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }



    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new FavoritesFragment();
            case 2:
                return new SavedFragment();
            case 3:
                return new CategoriesFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            /*case 0:
                return "Home";
            case 1:
                return "Favorites";
            case 2:
                return "Saved";
            case 3:
                return "Others";*/
        }
        return null;
    }
}
