package com.samayu.prodcastc.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.samayu.prodcastc.R;


/**
 * Created by nandhini on 01/09/17.
 */

public class AlertDialogFragment extends DialogFragment {


            public interface AlertDialogListener {
                public void onDialogPositiveClick(DialogFragment dialog);
                public void onDialogNegativeClick(DialogFragment dialog);
            }


            // Use this instance of the interface to deliver action events
            AlertDialogListener mListener;

             @Override
             public Dialog onCreateDialog(Bundle savedInstanceState) {
                 // Build the dialog and set up the button click handlers
                 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                 // Get the layout inflater
                 LayoutInflater inflater = getActivity().getLayoutInflater();

                 // Inflate and set the layout for the dialog
                 // Pass null as the parent view because its going in the dialog layout
                 builder.setView(inflater.inflate(R.layout.qty_dialog, null));

                 builder.setPositiveButton("YES",
                         new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog,int which) {
                                 mListener.onDialogPositiveClick(AlertDialogFragment.this);
                                 // Write your code here to execute after dialog
                                        /*    String quantity=qty.getText().toString();
                                            if(TextUtils.isEmpty(quantity))
                                            {
                                                qty.setError(getString(R.string.required_quantity));
                                                qty.requestFocus();
                                            }
                                            if(!TextUtils.isEmpty(quantity))
                                            {
                                                final OrderDetails orderDetails=new OrderDetails();

                                                System.out.println(qty.getText() + "success");
                                                orderDetails.setProduct(product);
                                                orderDetails.setQuantity(Integer.parseInt(quantity));

                                                SessionInfo.getInstance().getEntry().add(orderDetails);
                                                //SessionInfo.getInstance().setEntry(null);
                                                // SessionInfo.getInstance().setEntry(entries);
                                            }
        */


                             }
                         });
                 // Setting Negative "NO" Button
                 builder.setNegativeButton("NO",
                         new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 // Write your code here to execute after dialog
                                 mListener.onDialogNegativeClick(AlertDialogFragment.this);
                                 dialog.cancel();
                                 //      System.out.println("Closed");
                             }
                         });

                 return builder.create();
                }
    }


