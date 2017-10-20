package com.samayu.prodcastc.businessObjects.dto;

import com.samayu.prodcastc.businessObjects.domain.ServiceTicket;

import java.util.List;

/**
 * Created by kdsdh on 9/25/2017.
 */

public class ServiceSupportDTO extends ProdcastDTO {


    public List<ServiceTicket> getServiceTicketList() {
        return serviceTicketList;
    }

    public void setServiceTicketList(List<ServiceTicket> serviceTicketList) {
        this.serviceTicketList = serviceTicketList;
    }

    private List<ServiceTicket> serviceTicketList;


}
