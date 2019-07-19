package com.khemsharma.rms;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeaStudentReport extends Fragment {


    View view;

    EditText teaEnrol;

    Button teaEnrolSubmit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tea_student_report, container, false);

        teaEnrol=view.findViewById(R.id.teaEnrol);

        teaEnrolSubmit=view.findViewById(R.id.teaEnrolSubmit);

        teaEnrolSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enrol = teaEnrol.getText().toString();

                FragmentTransaction fragmentTransaction;

                fragmentTransaction = getFragmentManager().beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putString("enrol", "146895491");

                GradeCard fragment = new GradeCard();

                fragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.maincontainer,fragment);
                fragmentTransaction.addToBackStack(fragment.getClass().getName());
                fragmentTransaction.commit();
            }
        });




        return view;
    }

}
