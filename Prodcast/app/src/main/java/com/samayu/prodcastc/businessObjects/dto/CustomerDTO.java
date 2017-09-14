package com.samayu.prodcastc.businessObjects.dto;

import com.samayu.prodcastc.businessObjects.domain.Customer;

/**
 * Created by God on 8/19/2017.
 */
 public class CustomerDTO extends ProdcastDTO{
        public Customer getCustomer() {
            return customer;
        }

        public void setCustomer(Customer customer) {
            this.customer = customer;
        }

        private Customer customer;
 }


