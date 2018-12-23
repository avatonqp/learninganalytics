package kosmoglou.antogkou.learninganalytics.Classroom;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.WindowManager;

import kosmoglou.antogkou.learninganalytics.R;

public class StudentClassroomActivity extends AppCompatActivity {
  private SectionsPagerAdapter mSectionsPagerAdapter;
  private ViewPager mViewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // hides notification bar
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_student_classroom);

    Intent intent = getIntent();
    String classroomId = intent.getExtras().getString("classroom_id");

    Toolbar toolbar = findViewById(R.id.toolbar);
    toolbar.setTitle(classroomId);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    // Create the adapter that will return a fragment for each of the three
    // primary sections of the activity.
    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

    // Set up the ViewPager with the sections adapter.
    mViewPager = findViewById(R.id.container);
    mViewPager.setAdapter(mSectionsPagerAdapter);

    TabLayout tabLayout = findViewById(R.id.tabs);

    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_student_classroom, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if(id == android.R.id.home){
      finish();
    }

    return super.onOptionsItemSelected(item);
  }

  public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      Fragment fragment = null;
      switch(position){
        case 0:
          fragment = new fragment_class_info();
          fragment.setArguments(sendClassroomId());
          break;
        case 1:
          fragment = new fragment_class_quizes();
          fragment.setArguments(sendClassroomId());
          break;
        case 2:
          fragment = new fragment_class_discussions();
          fragment.setArguments(sendClassroomId());
          break;
      }
      return fragment;
    }

    @Override
    public int getCount() {
      // Show 3 total pages.
      return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
      switch(position){
        case 0:
          return "CLASS INFO";
        case 1:
          return "QUIZES";
        case 2:
          return "DISCUSSIONS";
      }
      return null;
    }
  }

  public Bundle sendClassroomId(){
    Bundle bundle = new Bundle();
    Intent intent = getIntent();
    Boolean isAdmin = false;
    String classroomId = intent.getExtras().getString("classroom_id");
    bundle.putBoolean("isAdmin", isAdmin);
    bundle.putString("classroom_id", classroomId);
    return bundle;
  }
}