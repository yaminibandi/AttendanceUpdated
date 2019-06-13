package com.develop.android.attendance;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ShowFullActivity extends AppCompatActivity {
    private ChildEventListener mAttendanceChildEventListener;
    private static List<RollNumbers> totalRoll =new ArrayList<>();
    private final Set<String> fullatt=new HashSet<>();
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mCoursesDatabaseReference,mAttendanceDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_full);
        Intent intent = getIntent();
        final String courseYear = intent.getStringExtra("CourseYear");
totalRoll.clear();
fullatt.clear();
        final LinearLayout ll1 = (LinearLayout)findViewById(R.id.my_layout1);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mAttendanceDatabaseReference=mFirebaseDatabase.getReference().child("Attendance");
        mAttendanceDatabaseReference.child(courseYear).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    GenericTypeIndicator<List<RollNumbers>> t = new GenericTypeIndicator<List<RollNumbers>>() {};
                    totalRoll = snapshot.getValue(t);
                    final LinearLayout ll1 = (LinearLayout)findViewById(R.id.my_layout1);
                    for(int i=0;i<totalRoll.size();i++)
                        fullatt.add(totalRoll.get(i).getRollnum());

                }
                getData(ll1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getData(LinearLayout ll){
        //fullatt.sort();
        //Collections.sort(fullatt);
        Set<String> tree_Set = new TreeSet<String>(fullatt);
        List<String> stringsList = new ArrayList<>(tree_Set);
        for(int i = 0; i < stringsList.size(); i++) {
            Log.d("TAG", "onCreate: "+stringsList.get(i));
            TextView cb = new TextView(this);
            cb.setText(stringsList.get(i));
            cb.setId(i);
            ll.addView(cb);

        }
    }
}
