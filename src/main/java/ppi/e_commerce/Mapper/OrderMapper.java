package ppi.e_commerce.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ppi.e_commerce.Dto.OrderDetailDto;
import ppi.e_commerce.Dto.OrderDto;
import ppi.e_commerce.Model.Order;
import ppi.e_commerce.Model.OrderDetail;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    OrderDto toDto(Order order);

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.imageUrl", target = "productImageUrl")
    @Mapping(target = "subtotal", expression = "java(detail.getPrice() * detail.getQuantity())")
    OrderDetailDto toDetailDto(OrderDetail detail);

    List<OrderDto> toDtoList(List<Order> orders);
}
