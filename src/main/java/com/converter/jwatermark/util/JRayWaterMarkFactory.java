package com.converter.jwatermark.util;

import com.converter.jwatermark.util.impl.WithOutWaterMarker;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 工厂模式，
 */
@Component
public class JRayWaterMarkFactory implements ApplicationListener, ApplicationContextAware {
    
    
    private List<JRayWaterMarker> waterMarkers = new ArrayList<>();
    
    private ApplicationContext applicationContext;
    
    @Resource
    private WithOutWaterMarker withOutWaterMarker;
    

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 监听事件，从spring工厂中获取所有JRayWaterMarker Bean
     * @param event
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if( event instanceof ContextRefreshedEvent){
            Map<String, JRayWaterMarker> beansOfType = this.applicationContext.getBeansOfType(JRayWaterMarker.class);
            waterMarkers = beansOfType.values().stream().collect(Collectors.toList());
        }
    }

    public JRayWaterMarker support(String filename) {
        for (JRayWaterMarker waterMarker : waterMarkers) {
            if(waterMarker.support( filename)){
                return waterMarker;
            }
        }
        return withOutWaterMarker;
    }


}
