/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handlingplayerrequests;

import java.util.ArrayList;
import java.util.List;
import models.Player;

/**
 *
 * @author Mohammed
 */
public interface PlayerRequestHandlerInterface {
    
    void login(String userName, String password);
    void register(String userName, String password);
    void logout();
    void getAllOnlinePlayers();
    void wantToPaly(String namePlayerThatIWillPlayWhitHim);
    void sendMove(String move,String namePlayerThatIWillPlayWhitHim);

    
}
