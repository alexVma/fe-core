/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.core.integrator.webService;

/**
 *
 * @author caf
 */
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC) //this annotation stipulates the style of your ws, document or rpc based. rpc is more straightforward and simpler. And old.
public interface WebServiceInterface
{
    
}
