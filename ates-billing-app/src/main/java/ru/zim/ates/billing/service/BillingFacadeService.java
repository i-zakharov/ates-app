package ru.zim.ates.billing.service;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zim.ates.billing.model.Task;

@Service
public class BillingFacadeService {

    @Autowired
    private PricingService pricingService;
    @Autowired
    private TaskService taskService;

    @Transactional
    public PricingService.TaskPrices calculateAndSetTaskPrice(UUID publicId) {
        PricingService.TaskPrices prices = pricingService.getPrices();
        taskService.setPrice(publicId, prices);
        return prices;
    }

    @Transactional
    public void assignTask(UUID taskPublicId, UUID assigneePublicId) {
        //Обновление таска не производим пусть оно все делается по CUD событиям
        //TODO сделать списание
    }

    @Transactional
    public void closeTask(UUID taskPublicId, UUID assigneePublicId) {
        taskService.closeTask(taskPublicId);
        //TODO сделать зачисление
    }
}
