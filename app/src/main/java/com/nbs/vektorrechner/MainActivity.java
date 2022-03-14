package com.nbs.vektorrechner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nbs.vektorrechner.ui.AboutActivity;
import com.nbs.vektorrechner.ui.ebeneGerade.EbeneGeradeFragment;
import com.nbs.vektorrechner.ui.ebenenUmwandler.ebenenUmwandlerFragment;
import com.nbs.vektorrechner.ui.fehlendeKoor.FehlendeKoorFragment;
import com.nbs.vektorrechner.ui.geradeGerade.GeradeGeradeFragment;
import com.nbs.vektorrechner.ui.geraden.GeradenFragment;
import com.nbs.vektorrechner.ui.home.HomeFragment;
import com.nbs.vektorrechner.ui.laenge.LaengeFragment;
import com.nbs.vektorrechner.ui.matrix.MatrixFragment;
import com.nbs.vektorrechner.ui.matrix2.Matrix2Fragment;
import com.nbs.vektorrechner.ui.skalar.SkalarFragment;
import com.nbs.vektorrechner.ui.spatprodukt.SpatproduktFragment;
import com.nbs.vektorrechner.ui.spiegelpunkte.SpiegelpunkteFragment;
import com.nbs.vektorrechner.ui.spurpunkte.SpurpunkteFragment;
import com.nbs.vektorrechner.ui.spurpunkteEbene.SpurpunkteEbeneFragment;
import com.nbs.vektorrechner.ui.vektorAddieren.VektorAddierenFragment;
import com.nbs.vektorrechner.ui.vektorenMultiplizieren.VektorenMultiplizierenFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    ArrayList<TextView> tvClicked = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tvToolbar = toolbar.findViewById(R.id.toolbarTextView);
        tvToolbar.setText("Start");

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });*/

        expandableListView = findViewById(R.id.expandableListView);
        prepareMenuData();
        populateExpandableList();


        View view = this.getCurrentFocus();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                /*InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);*/
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                /*InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);*/
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        openFragment(new HomeFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        /*int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void prepareMenuData() {

        MenuModel menuModel = new MenuModel("Start", true, false, R.drawable.ic_start);
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }

        menuModel = new MenuModel("Vektoren", true, true, R.drawable.ic_vektoren); //Menu of Java Tutorials
        headerList.add(menuModel);

        List<MenuModel> childModelsList = new ArrayList<>();
        MenuModel childModel = new MenuModel("Längen", false, false, R.drawable.ic_laenge);
        childModelsList.add(childModel);
        childModel = new MenuModel("Grundrechenarten", false, false, R.drawable.ic_grundrechenarten);
        childModelsList.add(childModel);
        childModel = new MenuModel("Kreuzmultiplikation", false, false, R.drawable.ic_kreuzmultiplikation);
        childModelsList.add(childModel);
        childModel = new MenuModel("Skalarprodukt", false, false, R.drawable.ic_skalarprodukt);
        childModelsList.add(childModel);
        childModel = new MenuModel("Spatprodukt", false, false, R.drawable.ic_spatprodukt);
        childModelsList.add(childModel);
        childModel = new MenuModel("Spiegelpunkte", false, false, R.drawable.ic_spiegelpunkt);
        childModelsList.add(childModel);
        childModel = new MenuModel("Fehlende Koordinate", false, false, R.drawable.ic_fehlende_koordinate);
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }


        menuModel = new MenuModel("Matrix", true, false, R.drawable.ic_matrix);
        headerList.add(menuModel);/**/

        menuModel = new MenuModel("LGS", true, false, R.drawable.ic_lgs);
        headerList.add(menuModel);

        menuModel = new MenuModel("Geraden", true, true, R.drawable.ic_geraden);
        headerList.add(menuModel);

        childModelsList = new ArrayList<>();
        childModel = new MenuModel("Spurpunkte Gerade", false, false, R.drawable.ic_spurpunkte);
        childModelsList.add(childModel);
        childModel = new MenuModel("Gerade - Gerade", false, false, R.drawable.ic_gerade_gerade);
        childModelsList.add(childModel);
        childModel = new MenuModel("Gerade - Ebene", false, false, R.drawable.ic_gerade_ebene);
        childModelsList.add(childModel);
        childModel = new MenuModel("Gerade - Kugel", false, false, R.drawable.ic_kugel_gerade);
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }

        menuModel = new MenuModel("Ebenen", true, true, R.drawable.ic_ebene);
        headerList.add(menuModel);

        childModelsList = new ArrayList<>();
        childModel = new MenuModel("Form Umwandeln", false, false, R.drawable.ic_spurpunkte_ebene);
        childModelsList.add(childModel);
        childModel = new MenuModel("Spurpunkte Ebene", false, false, R.drawable.ic_spurpunkte_ebene);
        childModelsList.add(childModel);
        childModel = new MenuModel("Ebene - Ebene", false, false, R.drawable.ic_ebene_ebene);
        childModelsList.add(childModel);
        childModel = new MenuModel("Ebene - Gerade", false, false, R.drawable.ic_gerade_ebene);
        childModelsList.add(childModel);
        childModel = new MenuModel("Ebene - Kugel", false, false, R.drawable.ic_ebene_kugel);
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }

        menuModel = new MenuModel("Kugeln", true, true, R.drawable.ic_kugel);
        headerList.add(menuModel);

        childModelsList = new ArrayList<>();
        childModel = new MenuModel("Kugel - Kugel", false, false, R.drawable.ic_kugel_kugel);
        childModelsList.add(childModel);
        childModel = new MenuModel("Kugel - Gerade", false, false, R.drawable.ic_kugel_gerade);
        childModelsList.add(childModel);
        childModel = new MenuModel("Kugel - Ebene", false, false, R.drawable.ic_ebene_kugel);
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.content_main_layout, fragment).commit();
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        dl.closeDrawers();
    }

    private void clearColor() {
        for(TextView tv : tvClicked) {
            tv.setTextColor(Color.parseColor("#000000"));
        }
    }

    private void populateExpandableList() {

        expandableListAdapter = new com.nbs.vektorrechner.ExpandableListAdapter(this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                MenuModel element = headerList.get(groupPosition);
                if (element.isGroup) {
                    Toolbar toolbar = findViewById(R.id.toolbar);
                    TextView tvToolbar = toolbar.findViewById(R.id.toolbarTextView);
                    tvToolbar.setText(element.menuName);
                    if (!element.hasChildren) {
                        switch (element.menuName) {
                            case "Start":
                                openFragment(new HomeFragment());
                                break;
                            case "Matrix":
                                openFragment(new MatrixFragment());
                            default:
                                break;
                        }
                        clearColor();
                        TextView tv = v.findViewById(R.id.lblListHeader);
                        tv.setTextColor(Color.parseColor("#1976D2"));
                        tvClicked.add(tv);
                    }
                }
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                MenuModel element = childList.get(headerList.get(groupPosition)).get(childPosition);
                Toolbar toolbar = findViewById(R.id.toolbar);
                TextView tvToolbar = toolbar.findViewById(R.id.toolbarTextView);
                tvToolbar.setText(element.menuName);
                if (childList.get(headerList.get(groupPosition)) != null) {
                    switch (element.menuName) {
                        case "Längen":
                            openFragment(new LaengeFragment());
                            break;
                        case "Matrix":
                            openFragment(new Matrix2Fragment());
                            break;
                        case "LGS":
                            System.out.println("test");
                            openFragment(new MatrixFragment());
                            break;
                        case "Grundrechenarten":
                            openFragment(new VektorAddierenFragment());
                            break;
                        case "Kreuzmultiplikation":
                            openFragment(new VektorenMultiplizierenFragment());
                            break;
                        case "Skalarprodukt":
                            openFragment(new SkalarFragment());
                            break;
                        case "Spatprodukt":
                            openFragment(new SpatproduktFragment());
                            break;
                        case "Spiegelpunkte":
                            openFragment(new SpiegelpunkteFragment());
                            break;
                        case "Fehlende Koordinate":
                            openFragment(new FehlendeKoorFragment());
                            break;
                        case "Spurpunkte Gerade":
                            openFragment(new SpurpunkteFragment());
                            break;
                        case "Gerade - Gerade":
                            openFragment(new GeradeGeradeFragment());
                            openFragment(new GeradenFragment());
                            break;
                        case "Gerade - Ebene":
                            openFragment(new EbeneGeradeFragment());
                            break;
                        case "Gerade - Kugel":
                            break;
                        case "Form Umwandeln":
                            openFragment(new ebenenUmwandlerFragment());
                            break;
                        case "Spurpunkte Ebene":
                            openFragment(new SpurpunkteEbeneFragment());
                            break;
                        case "Ebene - Ebene":
                            break;
                        case "Ebene - Gerade":
                            openFragment(new EbeneGeradeFragment());
                            break;
                        case "Ebene - Kugel":
                            break;
                        case "Kugel - Kugel":
                            break;
                        case "Kugel - Gerade":
                            break;
                        case "Kugel - Ebene":
                            break;
                        default:
                            break;
                    }
                    clearColor();
                    TextView tv = v.findViewById(R.id.lblListItem);
                    tv.setTextColor(Color.parseColor("#1976D2"));
                    tvClicked.add(tv);
                }
                return false;
            }
        });
    }
}
