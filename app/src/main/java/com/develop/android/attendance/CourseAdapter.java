package com.develop.android.attendance;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends ArrayAdapter<Courses> {
    private Context mContext;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mCoursesDatabaseReference,mRollDatabaseReference,mUsersDatabaseReference,mStatusDatabaseReference;

    public CourseAdapter(Context context, int resource, List<Courses> objects) {
        super(context, resource,objects);
        this.mContext=context;
    }
    public View getView(final int position, View convertView, ViewGroup parent){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mCoursesDatabaseReference = mFirebaseDatabase.getReference().child("Courses");
        mRollDatabaseReference=mFirebaseDatabase.getReference().child("Roll");
        mStatusDatabaseReference=mFirebaseDatabase.getReference().child("Status");

        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.adminlist_item, parent, false);
        }
        TextView courseTextView = (TextView) convertView.findViewById(R.id.course);
        TextView facultyTextView = (TextView) convertView.findViewById(R.id.faculty);
        TextView emailsTextView = (TextView) convertView.findViewById(R.id.emails);
       // Button delcourse=(Button)convertView.findViewById(R.id.delete);
      //  Button addexcel=(Button)convertView.findViewById(R.id.addexcel);
      //  Button addatt=(Button)convertView.findViewById(R.id.add);
    //    Button showatt=(Button)convertView.findViewById(R.id.show);
        final Button status=(Button)convertView.findViewById(R.id.status);
        final Button go=(Button)convertView.findViewById(R.id.go);
        final Courses thisCourse=getItem((position));
        Query query=mStatusDatabaseReference.child(thisCourse.year).child(thisCourse.courseName);
        final int[] flag = {0};
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("staiteys",dataSnapshot.getValue()+"");
                if(dataSnapshot.exists()){
                String stat= (String) dataSnapshot.getValue();
                if(stat.equals("False")){
                    flag[0] =1;
                    status.setText("Enable");
                }
                else{
                    flag[0]=0;
                    status.setText("Disable");
                }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        addatt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mContext instanceof ShowCourses){
//                    Courses thisCourse=getItem((position));
//                    ((ShowCourses)mContext).permitaddatt(thisCourse);
//                }
//                if(mContext instanceof FacultyActivity){
//                    Courses thisCourse=getItem((position));
//                    ((FacultyActivity)mContext).permitaddatt(thisCourse);
//                }
//            }
//        });
//        showatt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mContext instanceof ShowCourses){
//                    Courses thisCourse=getItem((position));
//                   ((ShowCourses)mContext).showAtt(thisCourse);
//                }
//                if(mContext instanceof FacultyActivity){
//                    Courses thisCourse=getItem((position));
//                    ((FacultyActivity)mContext).showAtt(thisCourse);
//                }
//            }
//        });
//        delcourse.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mContext instanceof ShowCourses){
//                    Courses thisCourse=getItem((position));
//                    ((ShowCourses)mContext).deleteCourse(thisCourse);
//                }
//
//            }
//        });
//        addexcel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mContext instanceof ShowCourses){
//                    Courses thisCourse=getItem((position));
//                    ((ShowCourses)mContext).addExcel(thisCourse);
//                }
//            }
//        });
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContext instanceof ShowCourses){
                    Courses thisCourse=getItem((position));
                    ((ShowCourses)mContext).gocourse(thisCourse);
                }
            }
        });
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Courses thisCourse=getItem((position));
                    Query query=mStatusDatabaseReference.child(thisCourse.year).child(thisCourse.courseName);
                    final int[] flag = {0};
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String stat= (String) dataSnapshot.getValue();
                            Log.d("staitey",dataSnapshot.getValue()+"");
                            if(stat.equals("False")){
                                mStatusDatabaseReference.child(thisCourse.year).child(thisCourse.courseName).setValue("True");
                                flag[0] =1;
                                status.setText("Disable");
                            }
                            else{
                                flag[0]=0;
                                mStatusDatabaseReference.child(thisCourse.year).child(thisCourse.courseName).setValue("False");
                                status.setText("Enable");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

        });
        Courses course=getItem(position);
        courseTextView.setText(course.getCourseName());
        //facultyTextView.setText(course.getCourseFaculty());
        ArrayList<String>emails=course.getEmails();
        String allemails="";
        if(!emails.isEmpty()) {
            for (int i = 0; i < emails.size(); i++) {
                String now = emails.get(i);
                allemails = allemails + now + "\n";
            }
        }
        //emailsTextView.setText(allemails);


        return convertView;
    }
}
