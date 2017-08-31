package businessObjects.dto;

import businessObjects.domain.Order;

/**
 * Created by nandhini on 29/08/17.
 */

public class OrderDTO extends  ProdcastDTO {
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    private Order order ;
}
