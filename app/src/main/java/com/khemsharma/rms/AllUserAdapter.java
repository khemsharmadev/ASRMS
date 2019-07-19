package com.khemsharma.rms;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.khemsharma.rms.Models.Result;
import com.khemsharma.rms.Models.User;

import java.util.List;

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.ViewHolder> {

    private List<User> users;
    Activity activity;

    public AllUserAdapter(Activity activity, List<User> users) {
        this.activity=activity;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grade_card_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = users.get(position);

        holder.name.setText(user.getName());
        holder.email.setText(user.getEmailID());


        if (user.getRoleID().equals("1"))
            holder.role.setText("Student");
        else if (user.getRoleID().equals("2"))
            holder.role.setText("Teacher");
        else
            holder.role.setText("Admin");


        CardView parent = (CardView) ((ViewGroup)  holder.name.getParent().getParent());

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction;

                fragmentTransaction = activity.getFragmentManager().beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putString("email", user.getEmailID());

                ProfileEdit fragment = new ProfileEdit();
                fragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.maincontainer,fragment);
                fragmentTransaction.addToBackStack(fragment.getClass().getName());
                fragmentTransaction.commit();
            }
        });





    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,email,role;

        public ViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.a1);
            email=itemView.findViewById(R.id.a2);
            role=itemView.findViewById(R.id.a3);


        }
    }


}
