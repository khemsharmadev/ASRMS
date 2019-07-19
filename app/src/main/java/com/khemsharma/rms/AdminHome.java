package com.khemsharma.rms;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminHome extends Fragment {


    View view;

    GridLayout gridLayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        gridLayout=view.findViewById(R.id.adminHome);

        setSingleEvent(gridLayout);



        return view;
    }

    private void setSingleEvent(GridLayout mainGrid) {

        for (int i = 0; i < mainGrid.getChildCount(); i++) {

            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;


            final FragmentTransaction fragmentTransaction;

            fragmentTransaction = getFragmentManager().beginTransaction();

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (finalI == 0) {

                        DownloadAssignment fragment = new DownloadAssignment();
                        fragmentTransaction.replace(R.id.maincontainer,fragment);
                        fragmentTransaction.addToBackStack(fragment.getClass().getName());
                        fragmentTransaction.commit();

                    }
                    else if (finalI==1){

                        AddMarks fragment = new AddMarks();
                        fragmentTransaction.replace(R.id.maincontainer,fragment);
                        fragmentTransaction.addToBackStack(fragment.getClass().getName());
                        fragmentTransaction.commit();


                    }
                    else if (finalI==2){

                        UpdateStudentMarks fragment = new UpdateStudentMarks();
                        fragmentTransaction.replace(R.id.maincontainer,fragment);
                        fragmentTransaction.addToBackStack(fragment.getClass().getName());
                        fragmentTransaction.commit();


                    }else if (finalI==3){

                        TeaStudentReport fragment = new TeaStudentReport();
                        fragmentTransaction.replace(R.id.maincontainer,fragment);
                        fragmentTransaction.addToBackStack(fragment.getClass().getName());
                        fragmentTransaction.commit();


                    }else if (finalI==4){

                        UserManagement fragment = new UserManagement();
                        fragmentTransaction.replace(R.id.maincontainer,fragment);
                        fragmentTransaction.addToBackStack(fragment.getClass().getName());
                        fragmentTransaction.commit();


                    }



                }
            });


        }

    }

}
