package com.example.sala_bd.myapplication;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class Permitido extends AppCompatActivity{

        TextView caja;
        String texto;
        String[]mProjection;
        ContactsCursor mCursorAdapter;
        Cursor mContactsCursor;
        ListView list;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_permitido);
                list=(ListView)findViewById(R.id.list);
                mProjection=new String[]{
                        ContactsContract.Profile._ID,
                        ContactsContract.Profile.DISPLAY_NAME_PRIMARY,
                };
                mCursorAdapter=new ContactsCursor(getBaseContext(),null,0);
                list.setAdapter(mCursorAdapter);

                mContactsCursor=getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                        mProjection,null,null,null);
                mCursorAdapter.changeCursor(mContactsCursor);

        }
}