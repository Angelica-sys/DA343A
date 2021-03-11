package Ludvig;
import Ludvig.Client.Controller.ClientController;
import Ludvig.Client.View.MainView;

public class TestGUI {
    public static void main(String[] args) {
        new ClientController("127.0.0.1", 23);
    }
}
