package clavardage;

import javax.swing.*;
import java.awt.*;

public class GraphicalTools {
    public static void setTheme(String theme) {
        try {
            UIManager.setLookAndFeel(theme);
        } catch(ClassNotFoundException e) {
            System.err.println("Couldn’t find class for specified look and feel:" + theme);
            System.err.println("Did you include the L&F library in the class path?");
            System.err.println("Using the default look and feel.");
        } catch(UnsupportedLookAndFeelException e) {
            System.err.println("Can't use the specified look and feel (" + theme + ") on this platform.");
            System.err.println("Using the default look and feel.");
        } catch(Exception e) {
            System.err.println("Couldn't get specified look and feel (" + theme + "), for some reason.");
            System.err.println("Using the default look and feel.");
            e.printStackTrace();
        }
    }
    public static void addManyComponents(Container containingComponent, Component[] componentsToAdd) {
        for(int i=0; i<componentsToAdd.length; i++) {
            containingComponent.add(componentsToAdd[i]);
        }
    }
}
