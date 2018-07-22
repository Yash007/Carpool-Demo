package harsh.keshwala.com.carpool;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class RiderPastTripFragment extends android.support.v4.app.Fragment {
    Activity context;
    public  RiderPastTripFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        View view =  inflater.inflate(R.layout.fragment_rider_past_trip,container,false);
        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
