package com.stap.erpstap_avangra.Clases;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stap.erpstap_avangra.R;

public class BottomNavigationController {

    public static BottomNavigationView NavView;

    public BottomNavigationView getNavView() {
        return NavView;
    }

    public void setNavView(BottomNavigationView navView) {
        NavView = navView;
    }

    public void setNavigationBottomView(BottomNavigationView _bottomNavigationView){
        setNavView(_bottomNavigationView);

    }

    public void updateCountBadgeCart(int size){
        if(size <= 0){
            badgeCartRemove();
        }
        else{
            getNavView().getOrCreateBadge(R.id.navigation_carro_compra).setNumber(size);
        }

    }
    public void countBadgeCartClear(){
        getNavView().getOrCreateBadge(R.id.navigation_carro_compra).clearNumber();
    }
    public void badgeCartRemove(){
        getNavView().removeBadge(R.id.navigation_carro_compra);
    }

    public void changeItemPosition(int id){
        getNavView().setSelectedItemId(id);
    }
}
