package com.khemsharma.rms;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class StudentHome extends Fragment {

    View view;

    GridLayout gridLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_student_home, container, false);
        gridLayout=view.findViewById(R.id.stuMainGrid);

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

                        /*AssignmentSubmission fragment = new AssignmentSubmission();
                        fragmentTransaction.replace(R.id.maincontainer,fragment);
                        fragmentTransaction.addToBackStack(fragment.getClass().getName());
                        fragmentTransaction.commit();*/

                        Intent intent = new Intent(getActivity(),AssignmentSubmission.class);
                        startActivity(intent);


                    }
                    else if (finalI==1){
                        GradeCard fragment = new GradeCard();
                        fragmentTransaction.replace(R.id.maincontainer,fragment);
                        fragmentTransaction.addToBackStack(fragment.getClass().getName());
                        fragmentTransaction.commit();
                    }
                    else if (finalI==2){
                        StuPercentageReport fragment = new StuPercentageReport();
                        fragmentTransaction.replace(R.id.maincontainer,fragment);
                        fragmentTransaction.addToBackStack(fragment.getClass().getName());
                        fragmentTransaction.commit();
                    }
                    else if (finalI==3){
                        StuScholarshipStatus fragment = new StuScholarshipStatus();
                        fragmentTransaction.replace(R.id.maincontainer,fragment);
                        fragmentTransaction.addToBackStack(fragment.getClass().getName());
                        fragmentTransaction.commit();
                    }
                    else if (finalI==4){
                        StuFinalResult fragment = new StuFinalResult();
                        fragmentTransaction.replace(R.id.maincontainer,fragment);
                        fragmentTransaction.addToBackStack(fragment.getClass().getName());
                        fragmentTransaction.commit();
                    }



                }
            });


        }

    }

}
