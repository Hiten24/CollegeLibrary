package com.example.onlinecollegelibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompatExtras;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminDashboard extends AppCompatActivity implements View.OnClickListener {
    CardView manageDb,addUsers,addBooks,addNotice,requestedBooks,issuedBooks,message;
    Button signOut;
    TextView userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        userName = findViewById(R.id.admin_dashboard_user_name);

        // to display admin name in toolbar
        userName.setText(new SessionManager(getApplicationContext()).getUserName());


        manageDb = findViewById(R.id.admin_manage_database_view);
        addUsers = findViewById(R.id.admin_add_users_view);
        addBooks = findViewById(R.id.admin_add_books_view);
        addNotice = findViewById(R.id.admin_add_notice_view);
        requestedBooks = findViewById(R.id.admin_requested_books_view);
        issuedBooks = findViewById(R.id.admin_issued_books_view);
        message = findViewById(R.id.admin_message_view);
        signOut = findViewById(R.id.admin_db_log_out_view);

        manageDb.setOnClickListener(this);
        addUsers.setOnClickListener(this);
        addBooks.setOnClickListener(this);
        addNotice.setOnClickListener(this);
        requestedBooks.setOnClickListener(this);
        issuedBooks.setOnClickListener(this);
        message.setOnClickListener(this);
        signOut.setOnClickListener(this);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        Context context = AdminDashboard.this;
        switch (id){
            case R.id.admin_manage_database_view:
                intent = new Intent(context,AdminDashboardTab.class);
                break;
            case R.id.admin_add_users_view:
                intent = new Intent(context,RegisterForm.class);
                break;
            case R.id.admin_add_books_view:
                intent = new Intent(context,AddBooks.class);
                break;
            case R.id.admin_add_notice_view:
                intent = new Intent(context,AddNoticeAdmin.class);
                intent.putExtra("isEditClicked",false);
                break;
            case R.id.admin_requested_books_view:
                intent = new Intent(context,RequestedBook.class);
                break;
            case R.id.admin_issued_books_view:
                intent = new Intent(context,AdminIssuedBooksActivity.class);
                break;
            case R.id.admin_message_view:
                intent = new Intent(AdminDashboard.this,ChatUserList.class);
                intent.putExtra("isFromAdmin",true);
                break;
            case R.id.admin_db_log_out_view:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(AdminDashboard.this,MainActivity.class);
                finish();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + id);
        }
        startActivity(intent);
    }

    /*@Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        if (id == R.id.card_view_1){
//            intent = new Intent(AdminDashboard.this,RegisterForm.class);
            intent = new Intent(AdminDashboard.this,AdminDashboardTab.class);
            startActivity(intent);
        }else if(id == R.id.card_view_2){
            intent = new Intent(AdminDashboard.this,AddBooks.class);
            startActivity(intent);
        }else if(id == R.id.card_view_4){
            intent = new Intent(AdminDashboard.this,IssuedBooks.class);
            startActivity(intent);
        }else if(id == R.id.card_view_3){
            intent = new Intent(AdminDashboard.this,UsersManageInAdmin.class);
            startActivity(intent);
        }else if(id == R.id.card_view_2){
            intent = new Intent(AdminDashboard.this,BookDetails.class);
            startActivity(intent);
        }else if(id == R.id.card_view_5){
            intent = new Intent(AdminDashboard.this,RequestedBooksAdmin.class);
//            intent = new Intent(AdminDashboard.this,AddNoticeAdmin.class);
            startActivity(intent);
        }else if(id == R.id.card_view_6){
            intent = new Intent(AdminDashboard.this, notice_list.class);
            startActivity(intent);
        }else if(id == R.id.menu_button){
            Toast.makeText(getApplicationContext(),"menu is opening",Toast.LENGTH_SHORT).show();
            drawerLayout.openDrawer(GravityCompat.START);
        }else if(id == R.id.card_view_7){
            intent = new Intent(AdminDashboard.this,ChatUserList.class);
            intent.putExtra("isFromAdmin",true);
            startActivity(intent);
        }else if(id == R.id.card_view_8){
//            intent = new Intent(AdminDashboard.this,AddNoticeAdmin.class);
//            startActivity(intent);
            String sapId = new SessionManager(getApplicationContext()).getSapId();
            Toast.makeText(getApplicationContext(),sapId,Toast.LENGTH_SHORT).show();
        }
        else if(id == R.id.admin_logout){
            FirebaseAuth.getInstance().signOut();
            Intent logOutIntent = new Intent(AdminDashboard.this,MainActivity.class);
            finish();
            startActivity(logOutIntent);
        }
    }*/
}