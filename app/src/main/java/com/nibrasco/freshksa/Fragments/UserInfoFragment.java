package com.nibrasco.freshksa.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.nibrasco.freshksa.Model.Session;
import com.nibrasco.freshksa.Model.User;
import com.nibrasco.freshksa.R;


public class UserInfoFragment extends Fragment {

    public UserInfoFragment() {
        // Required empty public constructor
    }

    private TextView txtName, txtPhone;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_userprofil, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final View v = getView();

        LinkControls(v);
        LoadData(v);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        //final View v = getView();

    }
    private void LinkControls(View v)
    {
        txtName = (TextView)v.findViewById(R.id.txtUserInfoName);
        txtPhone = (TextView)v.findViewById(R.id.txtUserInfoPhone);
    }
    private void LoadData(final View v)
    {
        String message = getResources().getString(R.string.msgUserInfoLoading);
        Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        User usr = Session.getInstance().User();
        txtPhone.setText(usr.getPhone());
        txtName.setText(usr.getName());
        snackbar.dismiss();
    }
}