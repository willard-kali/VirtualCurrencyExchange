package com.business.exchange.model;

import java.util.List;

public class Menu {
    private List<MenuButton> menuButtons;

    public Menu(List<MenuButton> menuButtons) {
        this.menuButtons = menuButtons;
    }

    public List<MenuButton> getMenuButtons() {
        return menuButtons;
    }

    public void setMenuButtons(List<MenuButton> menuButtons) {
        this.menuButtons = menuButtons;
    }
}
