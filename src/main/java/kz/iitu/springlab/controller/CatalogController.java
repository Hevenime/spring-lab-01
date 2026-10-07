package kz.iitu.springlab.controller;

import kz.iitu.springlab.aspect.MetricsAspect;
import kz.iitu.springlab.service.CatalogService;
import org.springframework.aop.support.AopUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lab4")
public class CatalogController {

    private final CatalogService catalogService;
    private final MetricsAspect metricsAspect;

    public CatalogController(CatalogService catalogService, MetricsAspect metricsAspect) {
        this.catalogService = catalogService;
        this.metricsAspect = metricsAspect;
    }

    @GetMapping("/item/{id}")
    public String findById(@PathVariable("id") long id) {
        return catalogService.findById(id);
    }

    @GetMapping("/items")
    public List<String> findAll(@RequestParam(name = "limit", defaultValue = "5") int limit) {
        return catalogService.findAll(limit);
    }

    @DeleteMapping("/item/{id}")
    public String remove(@PathVariable("id") long id) {
        return catalogService.remove(id);
    }

    @GetMapping("/proxy")
    public Map<String, String> proxyInfo() {
        return Map.of(
                "className", catalogService.getClass().getName(),
                "superClass", catalogService.getClass().getSuperclass().getSimpleName(),
                "isAopProxy", String.valueOf(AopUtils.isAopProxy(catalogService)),
                "isCglib", String.valueOf(AopUtils.isCglibProxy(catalogService)));
    }

    @GetMapping("/remove-twice/{id}")
    public String removeTwice(@PathVariable("id") long id) {
        return catalogService.removeTwice(id);
    }

    @GetMapping("/metrics")
    public Map<String, String> metrics() {
        return metricsAspect.summary();
    }
}