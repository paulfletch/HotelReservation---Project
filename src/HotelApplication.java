import menu.MainMenu;

import java.util.Scanner;

public class HotelApplication {

    Scanner scanner;

    public static void main(String[] args) {
        System.out.println("Hotel Reservation Application by Paul Fletcher.  Udacity Nanodegree Project");
        MainMenu hotelMainMenu = MainMenu.getInstance();
        hotelMainMenu.initialise();



    }
}
