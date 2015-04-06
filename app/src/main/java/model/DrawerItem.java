package model;

/**
 * Created by trek2000 on 12/2/2015.
 */
public class DrawerItem {
    private int number_of_notifications = 0;
    private boolean IS_SELECTED = false;

    private String TITLE = null;

    public DrawerItem(boolean IS_SELECTED, int number_of_notifications) {
        this.IS_SELECTED = IS_SELECTED;
        this.number_of_notifications = number_of_notifications;
    }

    public int getNumberOfNotifications() {
        return number_of_notifications;
    }

    public String getTitle() {
        return TITLE;
    }

    public void setTitle(String TITLE) {
        this.TITLE = TITLE;
    }

    public boolean isSelected() {
        return IS_SELECTED;
    }

    public void setSelected(boolean isSelected) {
        this.IS_SELECTED = isSelected;
    }
}
