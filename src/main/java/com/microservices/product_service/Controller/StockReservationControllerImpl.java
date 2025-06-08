package com.microservices.product_service.Controller;

import com.microservices.product_service.Request.ReserveStockRequest;
import com.microservices.product_service.Response.ProductResponse;
import com.microservices.product_service.Response.ReserveStockResponse;
import com.microservices.product_service.Service.ProductService;
import com.microservices.product_service.Service.StockReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/products")
@RestController
@RequiredArgsConstructor
public class StockReservationControllerImpl implements StockReservationController{
    private final StockReservationService stockReservationService;

    @Override
    @GetMapping("/reserve-stock")
    public ReserveStockResponse reserveStock(@RequestBody ReserveStockRequest request) {
        return stockReservationService.reserveStock(request);
    }
}
