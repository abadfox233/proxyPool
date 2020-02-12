package org.ning.proxypool.plugincore;

import cn.wanghaomiao.seimi.struct.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ning Wang
 * Date 2019/12/5 11:15
 * Description
 */

public abstract class BasePlugin {

    /**
     * 不处理的网址(提高搜索精确度)
     */
    protected final List<String> DENY_RULES = new ArrayList<>();

    private Logger logger = LoggerFactory.getLogger(getClass());

    public abstract  void before(Response response);
    public abstract  void after(Response response);

    /**
     * 判断网址是否被过滤
     * @param responseUrl -
     * @return -
     */
    protected boolean testDeny(String responseUrl){
        for(String rule:DENY_RULES){
            if(responseUrl.matches(rule)){
                return true;
            }
        }
        return false;
    }



}
