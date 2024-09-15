/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.jsonutil;

import models.Response;

/**
 *
 * @author Mohammed
 */
  public interface ResponseCallback {

        void onResponse(Response response);
  }
