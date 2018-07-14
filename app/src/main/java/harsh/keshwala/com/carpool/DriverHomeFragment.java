package harsh.keshwala.com.carpool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class DriverHomeFragment extends Fragment {
    FloatingActionButton createTrip;
    Activity context;
    public DriverHomeFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();

        createTrip = (FloatingActionButton) context.findViewById(R.id.createTripButtonFloatingButton);
        createTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,DriverCreateTrip.class);
                startActivity(intent);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        View view =  inflater.inflate(R.layout.fragment_driver_home,container,false);
        return  view;
    }


}
