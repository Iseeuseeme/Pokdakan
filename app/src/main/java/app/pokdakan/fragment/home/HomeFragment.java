package app.pokdakan.fragment.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import app.pokdakan.R;
import app.pokdakan.fragment.home.item.CreateItem;
import app.pokdakan.fragment.home.item.CreateItemSupplier;
import app.pokdakan.fragment.home.item.EventItem;
import app.pokdakan.fragment.home.item.ExploreItem;
import app.pokdakan.fragment.home.item.ProfileItem;

import static app.pokdakan.ConstantValues.TAG_ID;
import static app.pokdakan.ConstantValues.TAG_ROLE;
import static app.pokdakan.ConstantValues.my_shared_preferences;

public class HomeFragment extends Fragment {

    BottomNavigationView navigationView;
    SharedPreferences sharedpreferences;
    String id;
    String roles;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        sharedpreferences = getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString(TAG_ID, null);
        roles = sharedpreferences.getString(TAG_ROLE, null);
        navigationView = root.findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        if(!roles.equals("supplier") && !roles.equals("budidaya")){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.bottom_nav_menu_2);
        }
        ExploreItem fragment = new ExploreItem();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, "");
        fragmentTransaction.commit();
        return root;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.navigation_explode:
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Explore");
                    ExploreItem fragment = new ExploreItem();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, fragment, "");
                    fragmentTransaction.commit();
                    return true;

                case R.id.navigation_event:
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Event");
                    EventItem fragment1 = new EventItem();
                    FragmentTransaction fragmentTransaction1 =  getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.replace(R.id.content, fragment1);
                    fragmentTransaction1.commit();
                    return true;

                case R.id.navigation_create:
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Create Event");
                    CreateItem fragment2 = new CreateItem();
                    FragmentTransaction fragmentTransaction2 =  getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.content, fragment2);
                    fragmentTransaction2.commit();
                    return true;

                case R.id.navigation_profile:
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Profile");
                    ProfileItem fragment3 = new ProfileItem();
                    FragmentTransaction fragmentTransaction3 =  getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.content, fragment3, "");
                    fragmentTransaction3.commit();
                    return true;
            }
            return false;
        }
    };
}
