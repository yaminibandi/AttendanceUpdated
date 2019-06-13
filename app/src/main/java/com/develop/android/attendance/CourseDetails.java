package com.develop.android.attendance;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CourseDetails extends AppCompatActivity {
    String presentCourse,presentyear;
    AlertDialog.Builder builder;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mCoursesDatabaseReference,mRollDatabaseReference,mUsersDatabaseReference,mStatusDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mCoursesDatabaseReference = mFirebaseDatabase.getReference().child("Courses");
        mRollDatabaseReference=mFirebaseDatabase.getReference().child("Roll");
        mStatusDatabaseReference=mFirebaseDatabase.getReference().child("Status");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
          Button delcourse=(Button)findViewById(R.id.delete);
          Button addexcel=(Button)findViewById(R.id.addexcel);
          Button addatt=(Button)findViewById(R.id.add);
          Button showatt=(Button)findViewById(R.id.show);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = getIntent();
        final String value = intent.getStringExtra("CourseName");
        final String yearval=intent.getStringExtra("Year");
        Toast.makeText(this,value,Toast.LENGTH_LONG);
        Log.d("hhh",value);
        delcourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(CourseDetails.this);
                builder.setMessage("Do you want to close this application ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                confirmdelete(value,yearval);
                                finish();
                            }
                        })
                        .setNegativeButton("No, Keep it", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("AlertDialogExample");
                alert.show();
            }
        });
        showatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query=FirebaseDatabase.getInstance().getReference().child("Attendance").child(yearval).child(value);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Intent intent=new Intent(CourseDetails.this,ShowAttActivity.class);
                            intent.putExtra("CourseName",value);
                            intent.putExtra("Year",yearval);
                            CourseDetails.this.startActivity(intent);
                        } else {
                            Log.e("hhh", "N");
                            Toast.makeText(CourseDetails.this, "Attendance Not Added", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        addatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String courseName=value;
                final String Year=yearval;
                Query query=FirebaseDatabase.getInstance().getReference().child("Roll").child(Year).child(courseName);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Intent intent = new Intent(CourseDetails.this, AddAttActivity.class);
                            intent.putExtra("CourseName", courseName);
                            intent.putExtra("Year",Year);
                            CourseDetails.this.startActivity(intent);
                            Log.e("hhhh", "Y" + "");
                        } else {
                            Log.e("hhh", "N");
                            Toast.makeText(CourseDetails.this, "Please Add Excel", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        addexcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                //  intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                intent.setType("*/*");
                intent.putExtra("name",value);
                presentCourse=value;
                presentyear=yearval;
               // Toast.makeText(this, "before!"+thisCourse.courseName, Toast.LENGTH_LONG).show();
                Log.d("chek", "addexcel in");
                startActivityForResult(intent, 2);
               // Toast.makeText(this, "before!"+thisCourse.courseName, Toast.LENGTH_LONG).show();
            }
        });
    }
    public void confirmdelete(String value,String yearval){
        String x=value;
        String y=yearval;
        Toast.makeText(CourseDetails.this, x, Toast.LENGTH_SHORT).show();
        Query query=FirebaseDatabase.getInstance().getReference().child("Courses").child(y).orderByChild("courseName").equalTo(x);
        FirebaseDatabase.getInstance().getReference().child("Roll").child(y).child(x).setValue(null);
        FirebaseDatabase.getInstance().getReference().child("Attendance").child(y).child(x).setValue(null);
        FirebaseDatabase.getInstance().getReference().child("Courses").child(y).child(x).setValue(null);
        FirebaseDatabase.getInstance().getReference().child("Courses").child("All").child(x).setValue(null);
        FirebaseDatabase.getInstance().getReference().child("Status").child(y).child(x).setValue(null);
        FirebaseDatabase.getInstance().getReference().child("Time").child(y).child(x).setValue(null);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                dataSnapshot.getRef().setValue(null);
                // mCourseAdapter.remove(thisCourse);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toast.makeText(CourseDetails.this, "Deleted", Toast.LENGTH_SHORT).show();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<RollNumbers> roll =new ArrayList<>();
        // HashMap<String, String> rolls = new HashMap()<String,String>;
        if (data == null)
            return;
        String temp,Name = null;

        switch (requestCode) {
            case 2:
                try {
                    if (resultCode == RESULT_OK) {
                        try {
                            Log.d("chek", "addexcel in try");
                            Toast.makeText(getApplicationContext(),"Ushh",Toast.LENGTH_LONG).show();
                            InputStream is = getContentResolver().openInputStream(data.getData());
                            XSSFWorkbook workbook = new XSSFWorkbook(is);
                            XSSFSheet sheet = workbook.getSheetAt(0);
                            //Toast.makeText(getApplicationContext(),sheet.getLastRowNum()+"",Toast.LENGTH_LONG).show();
                            Bundle extras=data.getExtras();
                            if(extras!=null)
                                Name=extras.getString("name");
                            //Toast.makeText(getApplicationContext(),"is"+Name,Toast.LENGTH_LONG).show();
                            Iterator<Row> rowIterator = sheet.iterator();
                            while (rowIterator.hasNext()) {
                                Row row = rowIterator.next();
                                // For each row, iterate through all the columns
                                Iterator<Cell> cellIterator = row.cellIterator();

                                while (cellIterator.hasNext()) {
                                    Cell cell = cellIterator.next();
                                    if((cell.getColumnIndex()==1)&&(cell.getRowIndex()>0)){
                                        switch (cell.getCellType()) {
                                            case Cell.CELL_TYPE_NUMERIC:
                                                //Toast.makeText(getApplicationContext(),cell.getNumericCellValue()+"",Toast.LENGTH_LONG).show();
                                                break;
                                            case Cell.CELL_TYPE_STRING:
                                                temp=cell.getStringCellValue();
                                                RollNumbers rollnumbers= new RollNumbers(temp);
                                                roll.add(new RollNumbers(temp));
                                                // Toast.makeText(getApplicationContext(),cell.getStringCellValue()+"",Toast.LENGTH_LONG).show();
                                                break;
                                        }
                                    }

                                }
                                mRollDatabaseReference.child(presentyear).child(presentCourse).setValue(roll);

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }
}
