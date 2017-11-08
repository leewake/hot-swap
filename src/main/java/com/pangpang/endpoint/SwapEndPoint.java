package com.pangpang.endpoint;

import com.pangpang.service.SwapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.mvc.AbstractMvcEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;

/**
 * Created by leewake on 2017/9/13 0013.
 */

@Component
public class SwapEndPoint extends AbstractMvcEndpoint{

    @Autowired
    private SwapService swapService;

    /**
     * 刚开始构造函数写成有参，导致一直报错
     * 访问链接为：localhost:8081/hot-swap/swap?bean=haiServiceImpl
     */
    public SwapEndPoint() {
        super("/swap", true);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public SwapBean invoke(@RequestParam("bean") String bean){
        String old = swapService.swap(bean);
        return new SwapBean(old, bean);
    }


    private static class SwapBean implements Serializable{

        private String oldBean;
        private String newBean;

        public SwapBean(String oldBean, String newBean) {
            this.oldBean = oldBean;
            this.newBean = newBean;
        }

        public String getOldBean() {
            return oldBean;
        }

        public void setOldBean(String oldBean) {
            this.oldBean = oldBean;
        }

        public String getNewBean() {
            return newBean;
        }

        public void setNewBean(String newBean) {
            this.newBean = newBean;
        }
    }

}
