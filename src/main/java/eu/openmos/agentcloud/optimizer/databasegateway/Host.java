/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.optimizer.databasegateway;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 */
public class Host {
    protected String addr;
    protected int port;

    public Host(String addr) {
        if(addr.contains("/")) {
            try {
                this.addr = addr.contains("0.0.0.0") ? Inet4Address.getLocalHost().getHostAddress() : addr.substring(0, addr.indexOf("/"));                
            } catch (UnknownHostException ex) {
                Logger.getLogger(Host.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            this.addr = addr.substring(0, addr.indexOf(":"));
        }
        this.port = Integer.parseInt(addr.substring(addr.indexOf(":") + 1));
    }   
    
    @Override
    public String toString() {
        return addr + ":" + port;
    }

    public String getAddr() {
        return addr;
    }

    public int getPort() {
        return port;
    }
    
}
