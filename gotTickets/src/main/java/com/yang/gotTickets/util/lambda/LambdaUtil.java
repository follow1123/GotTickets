package com.yang.gotTickets.util.lambda;

import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * @auther YF
 * @create 2021-04-27-16:55
 */
public class LambdaUtil {

    public static  <T>  String lambdaToName(SFunction<T, ?> column){
        return new WrapperUtil<T, LambdaQueryWrapper<T>>().getColumn(column);
    }

    static class WrapperUtil<T, Children extends AbstractLambdaWrapper<T, Children>> extends AbstractLambdaWrapper<T, Children>{
        @Override
        protected Children instance() {
            return null;
        }

        public String getColumn(SFunction<T, ?> column){
            return columnToString(column);
        }
    }
}
