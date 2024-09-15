/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handlingplayerrequests;

import models.Player;

/**
 *
 * @author Mohammed
 */
public interface HandleWantToPlayResponseInterface {
     void onAccept(Player playerIwillPalyWith);
     void onRefuse(Player playerIwillPalyWith);  
}
