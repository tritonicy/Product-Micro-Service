package com.microservices.product_service.Service;

import com.microservices.product_service.DTO.ReservationInfoDTO;
import com.microservices.product_service.Entity.StockReservation;
import com.microservices.product_service.Request.AddProductRequest;
import com.microservices.product_service.Response.EventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationConsumer {
    private final ProductService productService;
    private final StockReservationService stockReservationService;

    @KafkaListener(topics = "${spring.kafka.topic.name}",groupId = "${spring.kafka.consumer.group-id}")
    public void consume(EventResponse event) {
        System.out.println("mesaj alindi");
        List<ReservationInfoDTO> allResponses = event.getPayload().getReservationInfoDTOList();
        for(ReservationInfoDTO reservationInfoDTO: allResponses) {
            if(reservationInfoDTO.getStockReservationId() != null) {
                stockReservationService.setReservedStockComplete(reservationInfoDTO.getStockReservationId());
                StockReservation reservation = stockReservationService.findById(reservationInfoDTO.getStockReservationId());
                AddProductRequest addProductRequest = new AddProductRequest();
                productService.findById(reservationInfoDTO.getProductid()).ifPresent(product -> {
                    addProductRequest.setStockQuantity(product.getStockQuantity() - reservation.getReservedQuantity());
                    System.out.println(product.getStockQuantity() - reservation.getReservedQuantity());
                });
                productService.updateStock(reservationInfoDTO.getProductid(),addProductRequest);

            }
        }
        event.setStatus("DONE");

    }
}
