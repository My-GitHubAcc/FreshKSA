package com.nibrasco.freshksa;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.database.*;
import com.nibrasco.freshksa.Model.Cart;
import com.nibrasco.freshksa.Model.Session;

/**
 * A simple {@link Fragment} subclass.
 */
public class HalfFragment extends Fragment {

    Button btnConfirm;
    EditText edtQuantity;
    Cart.Item currentItem;
    private TextView txtTotal;
    public HalfFragment() {
        // Required empty public constructor
        currentItem = Session.getInstance().Item();
        currentItem.setQuantity(1);
        currentItem.setIntestine(false);
        currentItem.setWeight(0);
        currentItem.setPackaging(Cart.ePackaging.Bags.Value());
        currentItem.setSlicing(Cart.eSlicing.Fridge.Value());
        currentItem.setNotes("");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nesfnaemiorder, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    private void LoadContent(View v){
        btnConfirm = (Button)v.findViewById(R.id.btnItemOrder);
        edtQuantity = (EditText)v.findViewById(R.id.edtQuantity);
        txtTotal = (TextView)v.findViewById(R.id.txtTotalItem);

        edtQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int qte = Integer.parseInt(s.toString().equals("") ? "0" : s.toString());
                currentItem.setQuantity(qte);
                txtTotal.setText(Float.toString(currentItem.getTotal()));
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Session.getInstance().Item().getCategory() != Cart.eCategory.None) {
                    //Add the item to the cart at this point
                    if(SaveChanges(v))
                    {
                        CartFragment f = new CartFragment();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.homeContainer, f);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
            }
        });
    }
    private Boolean SaveChanges(View v) {
        final Snackbar snack = Snackbar.make(v, "Saving Your Order", Snackbar.LENGTH_LONG);
        snack.show();
        final Boolean[] success = {true};
        Session.getInstance().Cart().AddItem(Session.getInstance().Item());
        //if(Session.getInstance().User().getCart().equals("0"))
        //{
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference tblCart = db.getReference("Cart");
        tblCart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot cartsSnap) {

                DatabaseReference cartRef = tblCart.child(Session.getInstance().User().getCart());
                Session.getInstance().Item().MapToDbRef(cartRef.child("Items"));

                snack.dismiss();
                success[0] = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                success[0] = false;
            }
        });
        //}
        return success[0];
    }
}
