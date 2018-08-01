package harsh.keshwala.com.carpool;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPageAdapter extends FragmentStatePagerAdapter {


    int tabCount;

    public TabPageAdapter(FragmentManager fm, int numebrOfTabs) {
        super(fm);
        this.tabCount = numebrOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                DriverHomeFragment driverHomeFragment = new DriverHomeFragment();
                return driverHomeFragment;
            case 1:
                DriverPastTripsFragment driverPastTripsFragment = new DriverPastTripsFragment();
                return driverPastTripsFragment;
            case 2:
                DriverRequestsFragment driverRequestsFragment = new DriverRequestsFragment();
                return driverRequestsFragment;
            case 3:
                DriverCarFragment driverCarFragment = new DriverCarFragment();
                return  driverCarFragment;
            case 4:
                DriverSupportFragment driverSupportFragment = new DriverSupportFragment();
                return driverSupportFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }


}
