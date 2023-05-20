package com.nak.fakeblock;

import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class FoundationActivity extends AppCompatActivity {
    private ProgressDialog memberProgressDialog;

    public void showProgressDialog() {
        if (memberProgressDialog == null) {
            memberProgressDialog = new ProgressDialog(this);
            memberProgressDialog.setCancelable(false);
            memberProgressDialog.setMessage("Carregando...");
        }
        memberProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (memberProgressDialog != null && memberProgressDialog.isShowing()) {
            memberProgressDialog.dismiss();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
