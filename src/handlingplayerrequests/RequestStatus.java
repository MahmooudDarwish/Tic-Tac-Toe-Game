/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handlingplayerrequests;

/**
 *
 * @author Mohammed
 */
public interface RequestStatus {
    void onSuccess (String msg);
    void onFailure (String msg);
}
